package org.rstudio.studio.client.common.satellite;

import java.util.ArrayList;

import org.rstudio.core.client.Size;
import org.rstudio.core.client.dom.WindowEx;
import org.rstudio.studio.client.RStudioGinjector;
import org.rstudio.studio.client.workbench.model.Session;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Singleton;

// TODO: Implement for Desktop


@Singleton
public class SatelliteManager implements CloseHandler<Window>
{
   @Inject
   public SatelliteManager(Session session)
   {
      session_ = session;
   }
   
   // the main window should call this method during startup to set itself
   // up to manage and communicate with the satellite windows
   public void initialize()
   {
      // export the registration hook used by satellites
      exportSatelliteRegistrationCallback();
      
      // handle onClosed to automatically close all satellites
      Window.addCloseHandler(this);
   }
    
   // open a satellite window (re-activate existing if requested)
   public void openSatellite(String name, 
                             Size preferredSize,
                             boolean activateExisting)
   {
      // if requested, first try to find an existing window
      if (activateExisting)
      {
         for (ActiveSatellite satellite : satellites_)
         {
            if (satellite.getName().equals(name))
            {
               WindowEx window = satellite.getWindow();
               if (!window.isClosed())
               {
                  window.focus();
                  return;
               }
            }
         }
      }
      
      // open a new satellite (respect any existing URL params)
      UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
      urlBuilder.setParameter("mode", name);
      RStudioGinjector.INSTANCE.getGlobalDisplay().openMinimalWindow(
                                              urlBuilder.buildString(),
                                              preferredSize.width,
                                              preferredSize.height);
   }
   
   // close all satellite windows
   public void closeAllSatellites()
   {
      for (ActiveSatellite satellite : satellites_)
      {
         try
         {
            satellite.getWindow().close();
         }
         catch(Throwable e)
         {
         }
      } 
      satellites_.clear();  
   }
   
   // dispatch an event to all satellites
   public void dispatchEvent(JavaScriptObject clientEvent)
   {
      // list of windows to remove (because they were closed)
      ArrayList<ActiveSatellite> removeWindows = null;
        
      // iterate over the satellites (make a copy to avoid races if
      // for some reason firing an event creates or destroys a satellite)
      @SuppressWarnings("unchecked")
      ArrayList<ActiveSatellite> satellites = 
                           (ArrayList<ActiveSatellite>)satellites_.clone();
      for (ActiveSatellite satellite : satellites)
      {
         try
         {
            WindowEx satelliteWnd = satellite.getWindow();
            if (satelliteWnd.isClosed())
            {
               if (removeWindows == null)
                  removeWindows = new ArrayList<ActiveSatellite>();
               removeWindows.add(satellite);
            }
            else
            {
               callDispatchEvent(satelliteWnd, clientEvent);
            }
         }
         catch(Throwable e)
         {
         }
      } 
      
      // remove windows if necessary
      if (removeWindows != null)
      {
         for (ActiveSatellite satellite : removeWindows)
         {
            satellites_.remove(satellite);
         }
      }
   }
   
   // close all satellites when we are closed
   @Override
   public void onClose(CloseEvent<Window> event)
   {
      closeAllSatellites();
   }
   
   // called by satellites to connect themselves with the main window
   private void registerAsSatellite(String name, JavaScriptObject wnd)
   {
      // get the satellite and add it to our list
      WindowEx satelliteWnd = wnd.<WindowEx>cast();
      satellites_.add(new ActiveSatellite(name, satelliteWnd));
      
      // call setSessionInfo
      callSetSessionInfo(satelliteWnd, session_.getSessionInfo());
   }
   
   // export the global function requried for satellites to register
   private native void exportSatelliteRegistrationCallback() /*-{
      var manager = this;     
      $wnd.registerAsRStudioSatellite = $entry(
         function(name, satelliteWnd) {
            manager.@org.rstudio.studio.client.common.satellite.SatelliteManager::registerAsSatellite(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)(name, satelliteWnd);
         }
      ); 
   }-*/;
   
   // call setSessionInfo on a satellite
   private native void callSetSessionInfo(JavaScriptObject satellite,
                                          JavaScriptObject sessionInfo) /*-{
      satellite.setRStudioSatelliteSessionInfo(sessionInfo);
   }-*/;
   
   // dispatch event to a satellite
   private native void callDispatchEvent(JavaScriptObject satellite,
                                         JavaScriptObject clientEvent) /*-{
      satellite.dispatchEventToRStudioSatellite(clientEvent);
   }-*/;
   
   private final Session session_;
   private final ArrayList<ActiveSatellite> satellites_ = 
                                          new ArrayList<ActiveSatellite>();

   private class ActiveSatellite
   {
      public ActiveSatellite(String name, WindowEx window)
      {
         name_ = name;
         window_ = window;
      }
      
      public String getName()
      {
         return name_;
      }
      
      public WindowEx getWindow()
      {
         return window_;
      }
      
      private final String name_;
      private final WindowEx window_;
   }
   
}



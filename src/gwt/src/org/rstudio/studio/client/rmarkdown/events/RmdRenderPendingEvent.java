/*
 * RmdRenderPendingEvent.java.java
 *
 * Copyright (C) 2009-14 by RStudio, PBC
 *
 * Unless you have received this program directly from RStudio pursuant
 * to the terms of a commercial license agreement with RStudio, then
 * this program is licensed to you under the terms of version 3 of the
 * GNU Affero General Public License. This program is distributed WITHOUT
 * ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
 * AGPL (http://www.gnu.org/licenses/agpl-3.0.txt) for more details.
 *
 */

package org.rstudio.studio.client.rmarkdown.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class RmdRenderPendingEvent extends GwtEvent<RmdRenderPendingEvent.Handler>
{  
   public interface Handler extends EventHandler
   {
      void onRmdRenderPending(RmdRenderPendingEvent event);
   }

   public RmdRenderPendingEvent(String docId)
   {
      docId_ = docId;
   }
    
   @Override
   public Type<Handler> getAssociatedType()
   {
      return TYPE;
   }
   
   public String getDocId()
   {
      return docId_;
   }

   @Override
   protected void dispatch(Handler handler)
   {
      handler.onRmdRenderPending(this);
   }

   public final String docId_;
   
   public static final Type<Handler> TYPE = new Type<Handler>();
}

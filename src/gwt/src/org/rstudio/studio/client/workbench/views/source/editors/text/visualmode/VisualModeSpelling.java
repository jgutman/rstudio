/*
 * VisualModeSpellChecker.java
 *
 * Copyright (C) 2020 by RStudio, PBC
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


package org.rstudio.studio.client.workbench.views.source.editors.text.visualmode;

import org.rstudio.core.client.Rectangle;
import org.rstudio.studio.client.panmirror.spelling.PanmirrorAnchor;
import org.rstudio.studio.client.panmirror.spelling.PanmirrorRect;
import org.rstudio.studio.client.panmirror.spelling.PanmirrorSpellingDoc;
import org.rstudio.studio.client.panmirror.spelling.PanmirrorWordRange;
import org.rstudio.studio.client.workbench.views.source.editors.text.spelling.SpellingDoc;

public class VisualModeSpelling
{
   public VisualModeSpelling()
   {
     
   }
   
   public void checkSpelling(PanmirrorSpellingDoc doc)
   {
      
   }
   
   
   
   // shim the SpellingDoc interface over the panmirror js interface
   private SpellingDoc spellingDoc(PanmirrorSpellingDoc doc)
   {
      return new SpellingDoc() {

         @Override
         public Iterable<WordRange> getWordSource(int start, Integer end)
         {
            // TODO Auto-generated method stub
            return null;
         }

         @Override
         public Anchor createAnchor(int position)
         {
            PanmirrorAnchor anchor = doc.createAnchor(position);
            return new Anchor() {
               @Override
               public int getPosition()
               {
                  return anchor.getPosition();
               }
            };
         }

         @Override
         public boolean shouldCheck(WordRange range)
         {
            // TODO Auto-generated method stub
            return false;
         }

         @Override
         public void setSelection(WordRange range)
         {
            // TODO Auto-generated method stub
            
         }

         @Override
         public String getText(WordRange range)
         {
            // TODO Auto-generated method stub
            return null;
         }

         @Override
         public int getCursorPosition()
         {
            // TODO Auto-generated method stub
            return 0;
         }

         @Override
         public void replaceSelection(String text)
         {
            // TODO Auto-generated method stub
            
         }

         @Override
         public int getSelectionStart()
         {
            return doc.getSelectionStart();
         }

         @Override
         public int getSelectionEnd()
         {
            return doc.getSelectionEnd();
         }

         @Override
         public Rectangle getCursorBounds()
         {
            PanmirrorRect rect = doc.getCursorBounds();
            return new Rectangle(rect.x, rect.y, rect.width, rect.height);
            
         }

         @Override
         public void moveCursorNearTop()
         {
            doc.moveCursorNearTop();
         }
         
         
         
      };
   }
   
   
}



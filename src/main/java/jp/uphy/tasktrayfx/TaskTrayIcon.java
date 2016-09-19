/**
 * Copyright (C) 2015 uphy.jp
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.uphy.tasktrayfx;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;


/**
 * @author Yuhi Ishikura
 */
public class TaskTrayIcon {

  private final TrayIcon icon;
  private final PopupMenu popupMenu;

  TaskTrayIcon() {
    final BufferedImage image = new IconGenerator().generate("");
    icon = new TrayIcon(image);
    popupMenu = new PopupMenu();
    icon.setImageAutoSize(true);
    icon.setPopupMenu(popupMenu);
  }

  public void addMouseListener(MouseListener l) {
    this.icon.addMouseListener(l);
  }

  public MenuItem addMenuItem(String title, ActionListener l) {
    final MenuItem menuItem = createMenuItem(title, l);
    this.popupMenu.add(menuItem);
    return menuItem;
  }

  public void addSeparator() {
    this.popupMenu.addSeparator();
  }

  public void setImage(Image image) {
    this.icon.setImage(image);
  }

  public void setTooltip(String tooltip) {
    this.icon.setToolTip(tooltip);
  }

  private static MenuItem createMenuItem(String title, ActionListener l) {
    final MenuItem item = new MenuItem(title);
    item.addActionListener(l);
    return item;
  }

  TrayIcon getIcon() {
    return icon;
  }
}

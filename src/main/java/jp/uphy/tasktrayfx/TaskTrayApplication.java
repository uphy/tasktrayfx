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

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import javax.swing.SwingUtilities;
import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.SystemTray;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * @author Yuhi Ishikura
 */
public abstract class TaskTrayApplication extends Application {

  private Stage stage;
  private TaskTrayIcon icon;
  private MenuItem showMenuItem;
  private double x = -1;
  private double y = -1;
  private double width = -1;
  private double height = -1;

  @Override
  public final void start(final Stage primaryStage) throws Exception {
    Platform.setImplicitExit(false);
    this.icon = new TaskTrayIcon();
    this.stage = primaryStage;
    this.stage.setOnCloseRequest(e -> {
      e.consume();
      hide();
    });
    SwingUtilities.invokeLater(() -> {
      try {
        SystemTray.getSystemTray().add(icon.getIcon());
      } catch (AWTException e) {
        throw new RuntimeException(e);
      }
      initSystemTrayIcon(this.icon);
      hide();
      Platform.runLater(() -> {
        try {
          startImpl(stage);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
    });
  }

  protected void initSystemTrayIcon(TaskTrayIcon icon) {
    icon.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(final MouseEvent e) {
        if (e.getButton() == 1 && e.getClickCount() == 2) {
          show();
        }
      }
    });
    this.showMenuItem = icon.addMenuItem("Hide", e -> {
      toggle();
    });
    icon.addSeparator();
    icon.addMenuItem("Exit", e -> {
      exit();
    });
  }

  protected abstract void startImpl(final Stage stage) throws Exception;

  void toggle() {
    if (this.stage.isShowing()) {
      hide();
    } else {
      show();
    }
  }

  void show() {
    Platform.runLater(() -> {
      if (stage.isShowing()) {
        return;
      }
      restoreStage();
      stage.show();
    });
    this.showMenuItem.setLabel("Hide");
  }

  void hide() {
    Platform.runLater(() -> {
      if (stage.isShowing() == false) {
        return;
      }
      storeStage();
      stage.hide();
    });
    this.showMenuItem.setLabel("Show");
  }

  private void restoreStage() {
    if (isStored() == false) {
      return;
    }
    this.stage.setX(this.x);
    this.stage.setY(this.y);
    this.stage.setWidth(this.width);
    this.stage.setHeight(this.height);
  }

  private void storeStage() {
    this.x = this.stage.getX();
    this.y = this.stage.getY();
    this.width = this.stage.getWidth();
    this.height = this.stage.getHeight();
  }

  private boolean isStored() {
    return this.x >= 0 && this.y >= 0 || this.width >= 0 || this.height >= 0;
  }

  void exit() {
    System.exit(0);
  }

}

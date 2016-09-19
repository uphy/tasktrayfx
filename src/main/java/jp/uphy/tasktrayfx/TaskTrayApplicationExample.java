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

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.Color;


/**
 * @author Yuhi Ishikura
 */
public class TaskTrayApplicationExample extends TaskTrayApplication {

  final IntegerProperty property = new SimpleIntegerProperty(-1);

  @Override
  protected void initSystemTrayIcon(final TaskTrayIcon icon) {
    icon.addMenuItem("Clear Count", e -> {
      Platform.runLater(() -> property.setValue(0));
    });
    property.addListener((observable, oldValue, newValue) -> {
      icon.setImage(new IconGenerator()
        .backgroundColor(newValue.intValue() % 2 == 0 ? Color.GRAY : Color.RED)
        .shape(IconGenerator.Shape.CIRCLE)
        .generate(newValue.toString()));
    });
    super.initSystemTrayIcon(icon);
    new Thread(() -> {
      while (true) {
        Platform.runLater(() -> property.setValue(property.getValue().intValue() + 1));
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    }).start();
  }

  @Override
  protected void startImpl(final Stage stage) throws Exception {
    final Text text = new Text();
    text.textProperty().bind(this.property.asString());
    final BorderPane pane = new BorderPane();
    pane.setCenter(text);
    stage.setScene(new Scene(pane));
    stage.show();
    stage.centerOnScreen();
  }
}

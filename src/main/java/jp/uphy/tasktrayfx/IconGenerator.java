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

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;


/**
 * @author Yuhi Ishikura
 */
public class IconGenerator {

  enum Shape {
    RECTANGLE {
      @Override
      void draw(final Graphics g, final int x, final int y, final int width, final int height) {
        g.fillRect(0, 0, width, height);
      }
    },
    CIRCLE {
      @Override
      void draw(final Graphics g, final int x, final int y, final int width, final int height) {
        g.fillOval(0, 0, width, height);
      }
    };

    abstract void draw(Graphics g, int x, int y, int width, int height);
  }

  private int imageWidth = 128;
  private int imageHeight = imageWidth;
  private Font font = Font.getFont(Font.MONOSPACED);
  private Color backgroundColor = Color.GRAY;
  private Color foregroundColor = Color.BLACK;
  private Shape shape = Shape.RECTANGLE;

  public IconGenerator backgroundColor(Color backgroundColor) {
    this.backgroundColor = backgroundColor;
    return this;
  }

  public IconGenerator foregroundColor(Color foregroundColor) {
    this.foregroundColor = foregroundColor;
    return this;
  }

  public IconGenerator font(Font font) {
    this.font = font;
    return this;
  }

  public IconGenerator font(int style, float fontSize) {
    this.font = this.font.deriveFont(style, fontSize);
    return this;
  }

  public IconGenerator fontSize(float fontSize) {
    this.font = this.font.deriveFont(fontSize);
    return this;
  }

  public IconGenerator shape(Shape shape) {
    this.shape = shape;
    return this;
  }

  public BufferedImage generate(String text) {
    final BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
    final Graphics g = image.getGraphics();
    g.setColor(this.backgroundColor);
    this.shape.draw(g, 0, 0, image.getWidth(), image.getHeight());
    g.setColor(this.foregroundColor);
    g.setFont(g.getFont().deriveFont(Font.BOLD, (int)(imageHeight * 0.8)));
    final FontMetrics fontMetrics = g.getFontMetrics();
    final int width = fontMetrics.stringWidth(text);
    g.drawString(text, (imageWidth - width) / 2, fontMetrics.getAscent());
    return image;
  }

}

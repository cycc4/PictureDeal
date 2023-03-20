/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.mwplay.cocostudio.ui.widget;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;


/**
 * 让Label支持TTF,使用ttf后Label的font不会发生变化,每次修改Text的时候重新创建font
 */
public class TTFLabel extends Label {

    public int labelAlign;
    public int lineAlign;

    public TTFLabel(CharSequence text, LabelStyle ttfLabelStyle) {
        super(text, ttfLabelStyle);
    }

    @Override
    public void setText(CharSequence newText) {
        LabelStyle style = getStyle();
        style.font = createFont("" + newText);

        super.setStyle(style);
        super.setText(newText);
    }

    @Override
    public void setAlignment(int labelAlign, int lineAlign) {
        this.labelAlign = labelAlign;
        this.lineAlign = lineAlign;
        super.setAlignment(labelAlign, lineAlign);
    }

    @Override
    public void setStyle(LabelStyle style) {
        LabelStyle ttfLabelStyle = (LabelStyle) style;
        if (ttfLabelStyle.font != null) {
            style.font = ttfLabelStyle.font;
        } else {
            style.font = createFont("" + getText());
        }
        super.setStyle(style);
    }

    private BitmapFont createFont(String text) {
        return new BitmapFont();
    }
}

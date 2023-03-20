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

package net.mwplay.cocostudio.ui.parser.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.qs.ui.util.NUtils;

import net.mwplay.cocostudio.ui.BaseCocoStudioUIEditor;
import net.mwplay.cocostudio.ui.model.objectdata.widget.TextFieldObjectData;
import net.mwplay.cocostudio.ui.parser.WidgetParser;


public class CCTextField extends WidgetParser<TextFieldObjectData> {


    @Override
    public Class getClassName() {
        return TextFieldObjectData.class;
    }

    @Override
    public Actor parse(BaseCocoStudioUIEditor editor, TextFieldObjectData widget) {

        final Label.LabelStyle labelStyle = editor
                .createLabelStyle(widget, widget.PlaceHolderText, NUtils.getColor(widget.CColor, 0));

        TextFieldStyle style = new TextFieldStyle(labelStyle.font, labelStyle.fontColor, null, null, null);

        TextField textField = new TextField(widget.LabelText, style) {

            @Override
            public void setText(String text) {

                super.setText(text);
            }

            @Override
            public void setMessageText(String messageText) {

                super.setMessageText(messageText);
            }

        };

        textField.setMaxLength(widget.MaxLengthText);
        textField.setMessageText(widget.PlaceHolderText);
        textField.setPasswordMode(widget.PasswordEnable);
        textField.setPasswordCharacter(widget.PasswordStyleText);
        return textField;
    }
}

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

package net.mwplay.cocostudio.ui.parser.group;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.qs.ui.util.NUtils;

import net.mwplay.cocostudio.ui.BaseCocoStudioUIEditor;
import net.mwplay.cocostudio.ui.model.objectdata.group.ButtonObjectData;
import net.mwplay.cocostudio.ui.parser.GroupParser;

public class CCButton extends GroupParser<ButtonObjectData> {

    protected BaseCocoStudioUIEditor editor;

    @Override
    public Class getClassName() {
        return ButtonObjectData.class;
    }

    @Override
    public Actor parse(BaseCocoStudioUIEditor editor, final ButtonObjectData widget) {
        this.editor = editor;

        Button button;
        //分开解决TextButton和ImageButton
        if (!isEmpty(widget.ButtonText)) {
            BitmapFont bitmapFont = editor.createLabelStyleBitmapFont(widget, widget.ButtonText, NUtils.getColor(widget.TextColor, widget.Alpha));

            TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(
                    editor.findDrawable(widget, widget.NormalFileData),
                    editor.findDrawable(widget, widget.PressedFileData),
                    null, bitmapFont);

            button = new TextButton(widget.ButtonText, textButtonStyle);

        } else {
            Button.ButtonStyle buttonStyle = new Button.ButtonStyle(
                    editor.findDrawable(widget, widget.NormalFileData),
                    editor.findDrawable(widget, widget.PressedFileData), null);
            buttonStyle.disabled = editor.findDrawable(widget, widget.DisabledFileData);

            button = new Button(buttonStyle);
        }

        button.setTransform(true);
        button.setDisabled(widget.DisplayState);

        return button;
    }

}

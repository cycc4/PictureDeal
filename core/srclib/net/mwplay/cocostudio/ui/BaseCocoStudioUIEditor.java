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

package net.mwplay.cocostudio.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Json;
import com.qs.ui.GlobalParsers;
import com.qs.utils.global.GlobalJson;

import net.mwplay.cocostudio.ui.model.CCExport;
import net.mwplay.cocostudio.ui.model.FileData;
import net.mwplay.cocostudio.ui.model.ObjectData;

public abstract class BaseCocoStudioUIEditor {
    protected CCExport export;

    public BaseCocoStudioUIEditor(FileHandle jsonFile) {
        System.out.println(jsonFile.file().getAbsolutePath());
        String json = jsonFile.readString("utf-8");
        Json jj = GlobalJson.getCocosJson();
        export = jj.fromJson(CCExport.class, json);
    }

    public abstract <T extends ObjectData> Drawable findDrawable(T widget, FileData textureFile);

    public abstract BitmapFont createLabelStyleBitmapFont(ObjectData widget, String buttonText, Color color);

    public abstract BaseCocoStudioUIEditor findCoco(FileData fileData);

    public Actor parseWidget(Group parent, ObjectData widget) {

        Class className = widget.getClass();
        BaseWidgetParser parser = GlobalParsers.getParsers().get(className);

        if (parser == null) {
            Gdx.app.debug(widget.getClass().toString(), "not support Widget:" + className);
            return null;
        }
        Actor actor = parser.parse(this, widget);

        actor = parser.commonParse(this, widget, parent, actor);

        return actor;
    }

    public Group createGroup() {
        Actor actor = parseWidget(null, export.Content.Content.ObjectData);

        return (Group) actor;
    }

    public abstract Label.LabelStyle createLabelStyle(ObjectData widget, String labelText, Color color);

    public abstract BitmapFont findBitmapFont(FileData labelBMFontFile_cnb);

    public abstract TextureRegion findTextureRegion(ObjectData widget, FileData path);

}

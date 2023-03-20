package com.qs.utils.global;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import net.mwplay.cocostudio.ui.model.GameFileData;
import net.mwplay.cocostudio.ui.model.objectdata.group.ButtonObjectData;
import net.mwplay.cocostudio.ui.model.objectdata.group.CheckBoxObjectData;
import net.mwplay.cocostudio.ui.model.objectdata.group.LayerObjectData;
import net.mwplay.cocostudio.ui.model.objectdata.group.PanelObjectData;
import net.mwplay.cocostudio.ui.model.objectdata.group.ProjectNodeObjectData;
import net.mwplay.cocostudio.ui.model.objectdata.group.ScrollViewObjectData;
import net.mwplay.cocostudio.ui.model.objectdata.group.SingleNodeObjectData;
import net.mwplay.cocostudio.ui.model.objectdata.widget.ImageViewObjectData;
import net.mwplay.cocostudio.ui.model.objectdata.widget.SpriteObjectData;
import net.mwplay.cocostudio.ui.model.objectdata.widget.TextBMFontObjectData;
import net.mwplay.cocostudio.ui.model.objectdata.widget.TextObjectData;

public class GlobalJson {
    private static Json json;

    public static Json getJson() {
        if (json == null) {
            System.err.println("生成默认 Json");
            json = new Json(JsonWriter.OutputType.json);
            json.setIgnoreUnknownFields(true);

        }
        return json;
    }

    private static Json cocosJson;

    public static Json getCocosJson() {
        if (cocosJson == null) {
            System.err.println("生成默认 Json");
            cocosJson = new Json(JsonWriter.OutputType.json);
            cocosJson.setTypeName("ctype");

            cocosJson.addClassTag("SingleNodeObjectData", SingleNodeObjectData.class);
            cocosJson.addClassTag("SpriteObjectData", SpriteObjectData.class);

            cocosJson.addClassTag("ButtonObjectData", ButtonObjectData.class);
            cocosJson.addClassTag("CheckBoxObjectData", CheckBoxObjectData.class);
            cocosJson.addClassTag("ImageViewObjectData", ImageViewObjectData.class);
            cocosJson.addClassTag("TextObjectData", TextObjectData.class);
            cocosJson.addClassTag("TextBMFontObjectData", TextBMFontObjectData.class);

            cocosJson.addClassTag("PanelObjectData", PanelObjectData.class);
            cocosJson.addClassTag("ScrollViewObjectData", ScrollViewObjectData.class);

            cocosJson.addClassTag("ProjectNodeObjectData", ProjectNodeObjectData.class);

            cocosJson.addClassTag("LayerObjectData", LayerObjectData.class);

            cocosJson.addClassTag("GameFileData", GameFileData.class);
//		jj.addClassTag("TimelineActionData", CCTimelineActionData.class);

            cocosJson.setIgnoreUnknownFields(true);
        }
        return cocosJson;
    }
}

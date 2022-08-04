package com.mygdx.game.CombinationPicture;

import com.alibaba.fastjson.JSON;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.mygdx.game.Tools.FileTool;

import java.io.File;


/**
 * Created by Doodle on 2019/12/10.
 */

public class Common {
    static TexturePacker.Settings settings = new TexturePacker.Settings();

    public static void setTexturePackerSettings(String externSettingPath) {
        if (externSettingPath != null) {
            File externSettingFile = new File(externSettingPath);
            if (externSettingFile.exists()) {
                try {
                    settings = JSON.parseObject(FileTool.file2String(new FileHandle(externSettingFile)), TexturePacker.Settings.class);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        settings.maxHeight = 2048;
        settings.maxWidth = 2048;
        settings.duplicatePadding = true;
        settings.paddingX = 2;
        settings.paddingY = 2;
        settings.bleed = true;
        settings.bleedIterations = 20;
        //跳过空白区域
        settings.stripWhitespaceX = true;
        settings.stripWhitespaceY = true;
        settings.format = Pixmap.Format.RGBA8888;
        settings.filterMag = Texture.TextureFilter.Linear;
        settings.filterMin = Texture.TextureFilter.Linear;
        settings.useIndexes = false;
        settings.pot = true;

        if (externSettingPath != null) {
            FileHandle fileHandle = new FileHandle(externSettingPath);
            String ss = JSON.toJSONString(settings);
            fileHandle.writeString(ss, false);
        }
    }

}

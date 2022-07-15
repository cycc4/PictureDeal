package com.mygdx.game.Logic.Tool.Atlas;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.mygdx.game.Logic.Tool.SettingTool;

import java.io.File;

/**
 * Created by Doodle on 2019/12/10.
 */

public class PictruePackaging {

    public static void process(File pngFile, File atlasFile) {
        process(SettingTool.getTexturePackerSetting(), pngFile, atlasFile, null);
    }

    public static void process(File pngFile, File atlasFile, TexturePacker.ProgressListener progress) {
        process(SettingTool.getTexturePackerSetting(), pngFile, atlasFile, progress);
    }

    private static void process(TexturePacker.Settings setting, File srcFile, File desFile, TexturePacker.ProgressListener progress) {
        for (File a : srcFile.listFiles()) {
            if (a.isDirectory()) {
                String s = a.getName();
//                if (s.endsWith(".png") || s.endsWith(".jpg")) {
                int length = s.length();
                String name = s.substring(0, length);
                TexturePacker.process(setting, a.getAbsolutePath(), desFile.getAbsolutePath(), name, progress);
//                }
            }
        }
    }
}

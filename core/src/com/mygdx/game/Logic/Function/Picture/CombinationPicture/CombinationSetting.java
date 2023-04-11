package com.mygdx.game.Logic.Function.Picture.CombinationPicture;//package com.mygdx.game.Logic.Function.Picture.CombinationPicture;
//
//import com.badlogic.gdx.graphics.Pixmap;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.tools.texturepacker.TexturePacker;
//
//public class CombinationSetting {
//    private static TexturePacker.Settings settings;
//
//    public static TexturePacker.Settings getTexturePackerSetting() {
//        if (settings == null) {
//            settings = new TexturePacker.Settings();
//            settings.maxHeight = 1024;
//            settings.maxWidth = 1024;
//            settings.duplicatePadding = true;
//            settings.paddingX = 2;
//            settings.paddingY = 2;
//            settings.bleed = true;
//            settings.bleedIterations = 20;
//            settings.stripWhitespaceX = true;
//            settings.stripWhitespaceY = true;
//            settings.format = Pixmap.Format.RGBA8888;
//            settings.filterMag = Texture.TextureFilter.Linear;
//            settings.filterMin = Texture.TextureFilter.Linear;
//            settings.useIndexes = false;
//            settings.pot = true;
//        }
//        return settings;
//    }
//}

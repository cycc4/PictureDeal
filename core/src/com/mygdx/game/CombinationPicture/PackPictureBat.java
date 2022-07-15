package com.mygdx.game.CombinationPicture;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import java.io.File;

public class PackPictureBat {
    public PackPictureBat(String string) {
        File pngDirFile = new File(string);
        if (pngDirFile.exists()) {
            String path = pngDirFile.getAbsolutePath();
            String dirPath = pngDirFile.getParent();
            TexturePacker.process(Common.settings, path, dirPath, pngDirFile.getName(), null);
        } else {
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<  path not exists  >>>>>>>>>>>>>>>>>>>>>>>>");
        }
    }
}

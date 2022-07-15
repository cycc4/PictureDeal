package com.mygdx.game.CombinationPicture;

import com.badlogic.gdx.tools.texturepacker.TextureUnpacker;

import java.io.File;

public class UnPackPictureBat {
    public UnPackPictureBat(String s) {
        File atlasFile = new File(s);
        String name = atlasFile.getName();
        if (name.endsWith(".atlas")) {
            String atlasDirPath = atlasFile.getParent();
            String fileName = name.substring(0, name.length() - 6);
            String pngPath = atlasDirPath + File.separator + fileName + ".png";
            if (new File(pngPath).exists()) {
                TextureUnpacker.main(new String[]{atlasFile.getAbsolutePath(), atlasDirPath, atlasDirPath + File.separator + fileName});
                System.out.println("切图完成:");
            } else {
                System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<  path not exists  >>>>>>>>>>>>>>>>>>>>>>>>");

            }
        }else{
            System.out.println("<<<<<<<<<<<<<<<<<<<<<< file is not atlas file >>>>>>>>>>>>>>>>>>>");
        }
    }
}

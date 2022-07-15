package com.mygdx.game.Logic.Tool.Atlas;

import com.badlogic.gdx.tools.texturepacker.TextureUnpacker;

import java.io.File;

/**
 * Created by Doodle on 2019/12/10.
 */

public class UnPackAtlas {

    public static void unpackAtlas(File srcFile, File desFile) {
        String atlasFile = null;
        String imageDir = null;
        for (File a : srcFile.listFiles()) {
            if (a.isDirectory()) {
                unpackAtlas(a, desFile);
            } else {
                String fileName = a.getName();
                if (a.getName().endsWith(".atlas") || a.getName().endsWith(".png")) {
                    if (a.getName().endsWith(".atlas")) {
                        atlasFile = a.getAbsolutePath();
                    } else if (a.getName().endsWith(".png")) {
                        imageDir = a.getParent();
                    }

                    if (atlasFile != null && imageDir != null) {
                        TextureUnpacker.main(new String[]{atlasFile, imageDir, desFile.getAbsolutePath() + File.separator + fileName});

                        atlasFile = null;
                        imageDir = null;
                    }
                }
            }
        }
        System.out.println("切图完成:");
    }
}

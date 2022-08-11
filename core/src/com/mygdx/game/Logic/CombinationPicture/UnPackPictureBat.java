package com.mygdx.game.Logic.CombinationPicture;

import com.badlogic.gdx.tools.texturepacker.TextureUnpacker;
import com.mygdx.game.Logic.Tools.PictureTool;

import java.io.File;

/*
 * 拆图代码
 */
public class UnPackPictureBat {

    public UnPackPictureBat(String atlasPath) {
        this(new File(atlasPath));
    }

    public UnPackPictureBat(File atlasFile) {
        this(atlasFile, atlasFile.getParent());
    }

    public UnPackPictureBat(File atlasFile, String imageDirPath) {
        this(atlasFile.getAbsolutePath(), imageDirPath, imageDirPath + File.separator + PictureTool.deletePictureNameExtension(atlasFile.getName()));
    }

    public UnPackPictureBat(String atlasPath, String imageDirPath, String unPackDirPath) {
        if (atlasPath.endsWith(".atlas")) {
            System.out.println("<<<<<<<<<<<<<<<<<< unpack picture file is: " + atlasPath + " >>>>>>>>>>>>>>>>>>");
            TextureUnpacker.main(new String[]{atlasPath, imageDirPath, unPackDirPath});
            System.out.println("切图完成:");
        } else {
            System.out.println("<<<<<<<<<<<<<<<<<<<<<< file is not atlas file >>>>>>>>>>>>>>>>>>>");
        }
    }

    /*
    public UnPackPictureBat(File atlasFile) {
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
        } else {
            System.out.println("<<<<<<<<<<<<<<<<<<<<<< file is not atlas file >>>>>>>>>>>>>>>>>>>");
        }
    }
     */
}

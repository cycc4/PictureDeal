package com.mygdx.game.CombinationPicture;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import java.io.File;

public class PackPictureBat {
    public PackPictureBat(){

    }

    public PackPictureBat(File pngDirFile) {
        packPicture(pngDirFile);
    }

    public PackPictureBat(String pngDirPath) {
        packPicture(new File(pngDirPath));
    }

    public PackPictureBat(String pngDirPath, String outDirPath, String atlasName) {
        packPicture(pngDirPath, outDirPath, atlasName);
    }

    public PackPictureBat(String pngDirPath, String outDirPath, String atlasName, TexturePacker.Settings settings) {
        packPicture(pngDirPath, outDirPath, atlasName, settings);
    }

    public void packPicture(File pngsDirFile) {
        if (pngsDirFile.exists()) {
            String pngsDirPath = pngsDirFile.getAbsolutePath();
            String atlasDirPath = pngsDirFile.getParent();
            packPicture(pngsDirPath, atlasDirPath, pngsDirFile.getName());
        } else {
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<  path not exists  >>>>>>>>>>>>>>>>>>>>>>>>");
        }
    }

    public void packPicture(String pngsDirPath, String atlasDirPath, String atlasFileName) {
        Common.setTexturePackerSettings(null); //设置setting，可以在外部调用
        packPicture(pngsDirPath, atlasDirPath, atlasFileName, Common.settings);
    }

    public void packPicture(String pngsDirPath, String atlasDirPath, String atlasFileName, TexturePacker.Settings settings) {
        Common.setTexturePackerSettings(null); //设置setting，可以在外部调用
        TexturePacker.process(settings, pngsDirPath, atlasDirPath, atlasFileName, null);
    }

    /*
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
     */
}

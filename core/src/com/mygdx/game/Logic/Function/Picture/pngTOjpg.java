package com.mygdx.game.Logic.Function.Picture;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;

import java.io.File;

/**
 * Created by Doodle on 2020/7/21.
 */

public class pngTOjpg extends Game {
    private static File file = new File("");
    private static final String rootPath = new File(file.getAbsolutePath()).getParentFile().getParent() +
            File.separator + "desktop" + File.separator + "src" + File.separator + "com" +
            File.separator + "nonogrampuzzle" + File.separator + "game" + File.separator + "Tools";

    private static final String srcPath = rootPath + File.separator + "DealPictureRGB" + File.separator + "NewPicture";
    private static final String desPath = rootPath + File.separator + "DealPictureRGB" + File.separator + "JpgPicture";

    @Override
    public void create() {
        File readFile = new File(srcPath);
        for (File a : readFile.listFiles()) {
            if (a != null && a.isFile() && a.getName().endsWith(".png")) {
                setPicture(a, new File(desPath + File.separator + a.getName()));
            }
        }
    }

    static void setPicture(File srcFile, File desFile) {
        Pixmap px = new Pixmap(new FileHandle(srcFile));
        Pixmap.Format format = px.getFormat();
        Pixmap px2 = new Pixmap(px.getWidth(), px.getHeight(), format);
        px2.setBlending(Pixmap.Blending.None);
        for (int x = 0; x < px.getWidth(); x++) {
            for (int y = 0; y < px.getHeight(); y++) {
                Color c = new Color(px.getPixel(x, y));
                c.a = 1;
                px2.drawPixel(x, y, c.rgba8888(c));
            }
        }
        PixmapIO.writePNG(new FileHandle(desFile), px2);
    }
}

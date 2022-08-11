package com.mygdx.game.Logic.Function.NinePath;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;

import java.io.File;

/**
 * Created by Doodle on 2020/4/17.
 * 将图切为点九图
 * * ** a *** b ***
 * c $$ $ $$$ $ $$$
 * * ** $ *** $ ***
 * d $$ $ $$$ $ $$$
 * * ** $ *** $ ***
 */


public class DealTextureToNinePath extends Game {
    private static final File file = new File("");
    private static final String rootPath = new File(file.getAbsolutePath()).getParentFile().getParent() +
            File.separator + "desktop" + File.separator + "src" + File.separator + "com" +
            File.separator + "nonogrampuzzle" + File.separator + "game" + File.separator + "Tools";
    private static final int a = 67;
    private static final int b = 53;
    private static final int c = 86;
    private static final int d = 0;

//    public static final void main(String[] cc) {
//        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
//        config.width = 1280;
//        config.height = 720;
//        new LwjglApplication(new DealTextureToNinePath(), config);
//    }

    @Override
    public void create() {
        File srcFile = new File(rootPath + File.separator + "NinePathTexture" + File.separator + "OldTexture");
        String desath = rootPath + File.separator + "NinePathTexture" + File.separator + "NewTexture" + File.separator;

        for (File f : srcFile.listFiles()) {
            if (f.getName().endsWith(".png") || f.getName().endsWith(".jpg")) {
                int width = a + b;
                int height = c + d;
                Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
                pixmap.setBlending(Pixmap.Blending.None);
                Pixmap oldPixmap = new Pixmap(new FileHandle(f));
                int widthDiff = oldPixmap.getWidth() - width;
                int heightDiff = oldPixmap.getHeight() - height;
                File desFile = new File(desath + f.getName());
                for (int i = 0; i < width; ++i) {
                    for (int j = 0; j < height; ++j) {
                        int color;
                        if (i >= 0 && i <= a) {
                            if (j >= 0 && j <= c) {
                                color = oldPixmap.getPixel(i, j);
                                pixmap.drawPixel(i, j, color);
                            } else {
                                color = oldPixmap.getPixel(i, j + heightDiff);
                                pixmap.drawPixel(i, j, color);
                            }
                        } else {
                            if (j >= 0 && j <= c) {
                                color = oldPixmap.getPixel(i + widthDiff, j);
                                pixmap.drawPixel(i, j, color);
                            } else {
                                color = oldPixmap.getPixel(i + widthDiff, j + heightDiff);
                                pixmap.drawPixel(i, j, color);
                            }
                        }
//                        if (i >= 0 && i <= c) {
//                            if (j >= 0 && j <= a) {
//                                color = oldPixmap.getPixel(i, j);
//                                pixmap.drawPixel(i, j, color);
//                            } else if (j > a && j <= width) {
//                                color = oldPixmap.getPixel(a + heightDiff, j);
//                                pixmap.drawPixel(i, j, color);
//                            }
//                        } else if (i > c && i <= height) {
//                            if (j >= 0 && j <= a) {
//                                color = oldPixmap.getPixel(i, c + widthDiff);
//                                pixmap.drawPixel(i, j, color);
//                            } else if (j > a && j <= width) {
//                                color = oldPixmap.getPixel(a + heightDiff, c + widthDiff);
//                                pixmap.drawPixel(i, j, color);
//                            }
//                        }
                    }
                }
                PixmapIO.writePNG(new FileHandle(desFile), pixmap);
            }
        }
    }

}

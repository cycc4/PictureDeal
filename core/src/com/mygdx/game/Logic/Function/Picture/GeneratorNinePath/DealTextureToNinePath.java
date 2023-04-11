package com.mygdx.game.Logic.Function.Picture.GeneratorNinePath;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.mygdx.game.Logic.CallBack.RecursionReversalDir;
import com.mygdx.game.Logic.ToolInterface.DealInterface;

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


public class DealTextureToNinePath implements DealInterface {
    public static int a = 37;
    public static int b = 37;
    public static int c = 56;
    public static int d = 30;

    private FileHandle readFile;

    @Override
    public void deal(String readPath, String writePath) {
        readFile = new FileHandle(readPath);

        new RecursionReversalDir(readFile, writePath){
            @Override
            protected void callback(FileHandle readFile, String writeFile) {
                if (readFile.name().endsWith(".png") || readFile.name().endsWith(".jpg")) {
                    int width = a + b;
                    int height = c + d;
                    Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
                    pixmap.setBlending(Pixmap.Blending.None);
                    Pixmap oldPixmap = new Pixmap(readFile);
                    int widthDiff = oldPixmap.getWidth() - width;
                    int heightDiff = oldPixmap.getHeight() - height;
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
                    PixmapIO.writePNG(new FileHandle(writeFile+File.separator + readFile.name()), pixmap);
                }
            }
        };
    }
}

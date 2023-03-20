package com.mygdx.game;


import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;

import java.io.File;

public class Debug {
    //////////////////////// picture ///////////////////////////
    public static void debugPicture_Alpha_1(Pixmap picturePixmap) {
        debugPicture_Alpha_1(picturePixmap, Constants.DIR_PATH);
    }

    public static void debugPicture_Alpha_1(Pixmap picturePixmap, String writePath) {
        debugPicture_Alpha_1(picturePixmap, writePath, null);
    }

    public static void debugPicture_Alpha_1(Pixmap picturePixmap, String writePath, String debugName) {
        //<<<<<<<<<<<<<<<<<<<< debug >>>>>>>>>>>>>>>>>>>>
        Pixmap px3 = new Pixmap(picturePixmap.getWidth(), picturePixmap.getHeight(), picturePixmap.getFormat());
        for (int x = 0; x < picturePixmap.getWidth(); x++) {
            for (int y = 0; y < picturePixmap.getHeight(); y++) {
                Color c = new Color(picturePixmap.getPixel(x, y));
                if (c.a < 1) {
                    c.a = 1;
                }
                px3.drawPixel(x, y, c.rgba8888(c));
            }
        }

        String printPixmapName = "debug.png";
        if (debugName != null) {
            printPixmapName = debugName;
        }
        PixmapIO.writePNG(new FileHandle(writePath + File.separator + printPixmapName), px3);
        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
}

package com.mygdx.game.Logic.Function.Picture.ProcessPicturePixels.ModifyBlankPixels;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.mygdx.game.Debug;
import com.mygdx.game.Logic.CallBack.RecursionReversalDir;
import com.mygdx.game.Logic.ToolInterface.DealInterface;

import java.io.File;

/**
 * bukejianxiangshuzhichengbaise
 * alpha == 0 r = 1 g = 1 b = 1
 */
public class DealPictureRGB4 implements DealInterface {


    @Override
    public void deal(String readPath, String writePath) {
        if (readPath == null) {
            return;
        }

        File readFile = new File(readPath);

        if (!readFile.exists()) {
            return;
        }

        new RecursionReversalDir(readFile, writePath) {
            @Override
            protected void callback(File readFile, String writePath) {
                String pictureName = readFile.getName();
                if (pictureName.endsWith(".png")) {

                    Pixmap px = new Pixmap(new FileHandle(readFile));
                    Pixmap px2 = new Pixmap(px.getWidth(), px.getHeight(), px.getFormat());
                    px2.setBlending(Pixmap.Blending.None);
                    //查找所有有颜色边界像素点集合
                    for (int x = 0; x < px.getWidth(); x++) {
                        for (int y = 0; y < px.getHeight(); y++) {
                            Color c = new Color(px.getPixel(x, y));
                            if(c.a == 0) {
                                c.r = 1;
                                c.g = 1;
                                c.b = 1;
                            }

                            px2.drawPixel(x, y, c.rgba8888(c));
                        }
                    }
                    PixmapIO.writePNG(new FileHandle(writePath + File.separator + pictureName), px2);

                    //<<<<<<<<<<<<<<<<<<<< debug >>>>>>>>>>>>>>>>>>>>
                    Debug.debugPicture_Alpha_1(px2, writePath, "debug_" + pictureName);
                    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                }
            }
        };
    }

}

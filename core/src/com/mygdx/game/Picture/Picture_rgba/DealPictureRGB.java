package com.mygdx.game.Picture.Picture_rgba;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.Logic.ToolInterface.DealInterface;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Doodle on 2020/1/22.
 * 处理图片的rgba
 */

public class DealPictureRGB implements DealInterface {
    protected HashMap<String, Color> pixelHashMap = new HashMap<>();

    @Override
    public void deal(String readPath, String writePath) {
        if (readPath == null) {
            return;
        }
        File readFile = new File(readPath);
        if (!readFile.exists()) {
            return;
        }

        pixelHashMap.clear();
        String pictureName = readFile.getName();
        Pixmap px = new Pixmap(new FileHandle(readFile));
        Pixmap px2 = new Pixmap(px.getWidth(), px.getHeight(), px.getFormat());
        px2.setBlending(Pixmap.Blending.None);
        for (int x = 0; x < px.getWidth(); x++) {
            for (int y = 0; y < px.getHeight(); y++) {
                Color c = getPixelColor(px, x, y);
                if (c.a == 0) {
                    float r = 0, g = 0, b = 0;
                    int num = 0;
                    //left
                    Color c1 = getPixelColor(px, x - 1, y);
                    if (c1 != null && c1.a > 0) {
                        r += c1.r;
                        g += c1.g;
                        b += c1.b;
                        num += 1;
                        System.out.println("11111111111111111111111111111111111111111111111111111111111:  " + c1.r + "   " + c1.g + "   " + c1.b);
                    }
                    //right
                    Color c2 = getPixelColor(px, x + 1, y);
                    if (c2 != null && c2.a > 0) {
                        r += c2.r;
                        g += c2.g;
                        b += c2.b;
                        num += 1;
                        System.out.println("22222222222222222222222222222222222222222222222222222222222: " + c2.r + "   " + c2.g + "   " + c2.b);
                    }
                    //top
                    Color c3 = getPixelColor(px, x, y + 1);
                    if (c3 != null && c3.a > 0) {
                        r += c3.r;
                        g += c3.g;
                        b += c3.b;
                        num += 1;
                        System.out.println("33333333333333333333333333333333333333333333333333333333333: " + c3.r + "   " + c3.g + "   " + c3.b);
                    }
                    //bottom
                    Color c4 = getPixelColor(px, x, y - 1);
                    if (c4 != null && c4.a > 0) {
                        r += c4.r;
                        g += c4.g;
                        b += c4.b;
                        num += 1;
                        System.out.println("44444444444444444444444444444444444444444444444444444444444: " + c4.r + "   " + c4.g + "   " + c4.b);
                    }

                    if (num > 0) {
                        System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz: " + x + "   " + y + "   " + r + "   " + g + "   " + b + "   " + num);
                        c.r = r / num;
                        c.r = MathUtils.clamp(c.r, 0, 1);
                        c.g = g / num;
                        c.g = MathUtils.clamp(c.g, 0, 1);
                        c.b = b / num;
                        c.b = MathUtils.clamp(c.b, 0, 1);
                        System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy:  " + c.r + "   " + c.g + "   " + c.b);
                    } else {
                        c.r = 1;
                        c.g = 1;
                        c.b = 1;
                    }
                }

                px2.drawPixel(x, y, c.rgba8888(c));
            }
        }
        PixmapIO.writePNG(new FileHandle(writePath + File.separator + pictureName), px2);

        Pixmap px3 = new Pixmap(px.getWidth(), px.getHeight(), px.getFormat());
        for (int x = 0; x < px.getWidth(); x++) {
            for (int y = 0; y < px.getHeight(); y++) {
                Color c = new Color(px2.getPixel(x, y));
                if (c.a == 0) {
                    c.a = 1;
                }
                px3.drawPixel(x, y, c.rgba8888(c));
            }
        }

        PixmapIO.writePNG(new FileHandle(writePath + File.separator + "debug.png"), px3);
    }


    protected Color getPixelColor(Pixmap px, int x, int y) {
        if (x < px.getWidth() && y < px.getHeight()) {
            String s = x + "_" + y;
            Color c = pixelHashMap.get(s);
            if (c == null) {
                c = new Color(px.getPixel(x, y));
                pixelHashMap.put(s, c);
            }
            return c;
        }
        return null;
    }


}

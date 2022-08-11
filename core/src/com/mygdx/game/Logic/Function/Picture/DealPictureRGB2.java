package com.mygdx.game.Logic.Function.Picture;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.Logic.ToolInterface.DealInterface;

import java.io.File;
import java.util.HashMap;

public class DealPictureRGB2 implements DealInterface {
    //用來保存用于参与计算的rgb
    private HashMap<String, Color> pixelBoundaryRGBHashMap = new HashMap<>();
    private HashMap<String, Color> pixelHashMap = new HashMap<>();

    @Override
    public void deal(String readPath, String writePath) {
        if(readPath == null){
            return;
        }
        File readFile = new File(readPath);
        if(!readFile.exists()){
            return;
        }

        String pictureName = readFile.getName();
        pixelBoundaryRGBHashMap.clear();
        pixelHashMap.clear();

        Pixmap px = new Pixmap(new FileHandle(readFile));
        Pixmap px2 = new Pixmap(px.getWidth(), px.getHeight(), px.getFormat());
        px2.setBlending(Pixmap.Blending.None);

        for (int n = 0; n < 2; ++n) {
            for (int x = 0; x < px.getWidth(); x++) {
                for (int y = 0; y < px.getHeight(); y++) {
                    String s = getHashKey(x, y);
                    Color c = pixelBoundaryRGBHashMap.get(s);
                    if (c == null) {
                        c = getPixelColor(s, px, x, y);
                        if (c.a == 0) {
                            float r = 0, g = 0, b = 0;
                            int num = 0;
                            //left
                            String leftK = getHashKey(x - 1, y);
                            Color c1 = pixelBoundaryRGBHashMap.get(leftK);
                            if (c1 == null) {
                                c1 = getPixelColor(leftK, px, x - 1, y);
                                if (c1 != null && c1.a > 0) {
                                    pixelBoundaryRGBHashMap.put(leftK, c1);
                                    r += c1.r;
                                    g += c1.g;
                                    b += c1.b;
                                    num += 1;
                                }
                            } else {
                                r += c1.r;
                                g += c1.g;
                                b += c1.b;
                                num += 1;
                            }
                            //right
                            String rightK = getHashKey(x + 1, y);
                            Color c2 = pixelBoundaryRGBHashMap.get(rightK);
                            if (c2 == null) {
                                c2 = getPixelColor(rightK, px, x + 1, y);
                                if (c2 != null && c2.a > 0) {
                                    pixelBoundaryRGBHashMap.put(rightK, c2);
                                    r += c2.r;
                                    g += c2.g;
                                    b += c2.b;
                                    num += 1;
                                }
                            } else {
                                r += c2.r;
                                g += c2.g;
                                b += c2.b;
                                num += 1;
                            }
                            //top
                            String topK = getHashKey(x, y + 1);
                            Color c3 = pixelBoundaryRGBHashMap.get(topK);
                            if (c3 == null) {
                                c3 = getPixelColor(topK, px, x, y + 1);
                                if (c3 != null && c3.a > 0) {
                                    pixelBoundaryRGBHashMap.put(topK, c3);
                                    r += c3.r;
                                    g += c3.g;
                                    b += c3.b;
                                    num += 1;
                                }
                            } else {
                                r += c3.r;
                                g += c3.g;
                                b += c3.b;
                                num += 1;
                            }
                            //bottom
                            String bottomK = getHashKey(x, y - 1);
                            Color c4 = pixelBoundaryRGBHashMap.get(bottomK);
                            if (c4 == null) {
                                c4 = getPixelColor(bottomK, px, x, y - 1);
                                if (c4 != null && c4.a > 0) {
                                    pixelBoundaryRGBHashMap.put(bottomK, c4);
                                    r += c4.r;
                                    g += c4.g;
                                    b += c4.b;
                                    num += 1;
                                }
                            } else {
                                r += c4.r;
                                g += c4.g;
                                b += c4.b;
                                num += 1;
                            }

                            if (num > 0) {
                                c.r = r / num;
                                c.r = MathUtils.clamp(c.r, 0, 1);
                                c.g = g / num;
                                c.g = MathUtils.clamp(c.g, 0, 1);
                                c.b = b / num;
                                c.b = MathUtils.clamp(c.b, 0, 1);
                                pixelBoundaryRGBHashMap.put(s, c);
                            } else {
                                c.r = 1;
                                c.g = 1;
                                c.b = 1;
                                c.a = 0;
                            }
                        }
                        px2.drawPixel(x, y, c.rgba8888(c));
                    }
                }
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

    public String getHashKey(int x, int y) {
        return x + "_" + y;
    }

    public Color getPixelColor(String key, Pixmap px, int x, int y) {
        Color c = pixelHashMap.get(key);
        if (c == null) {
            c = new Color(px.getPixel(x, y));
            pixelHashMap.put(key, c);
        }
        return c;
    }
}

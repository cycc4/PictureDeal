package com.mygdx.game.Picture.Picture_rgba;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.CallBack.RecursionReversalDir;
import com.mygdx.game.Logic.ToolInterface.DealInterface;

import java.io.File;
import java.util.HashMap;

public class DealPictureRGB3 implements DealInterface {
    private HashMap<String, Color> pixelHashMap = new HashMap();
    private Array<PixelPoint> pixelBonusaryArray = new Array<>();
    private Array<PixelPoint> pixelBonusaryTempArray = new Array<>();

    //
    private final int times = 4;

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
                    pixelHashMap.clear();
                    pixelBonusaryArray.clear();

                    Pixmap px = new Pixmap(new FileHandle(readFile));
                    Pixmap px2 = new Pixmap(px.getWidth(), px.getHeight(), px.getFormat());
                    px2.setBlending(Pixmap.Blending.None);
                    //查找所有有颜色边界像素点集合
                    for (int x = 0; x < px.getWidth(); x++) {
                        for (int y = 0; y < px.getHeight(); y++) {
                            String s = getKey(x, y);
                            Color c = getPixelColor(s, px, x, y);
                            if (c.a > 0) {
                                //left
                                Color cl = getPixelColor(getKey(x - 1, y), px, x - 1, y);
                                if (cl != null && cl.a == 0) {
                                    pixelBonusaryArray.add(new PixelPoint(s, x, y));
                                    continue;
                                }
                                //right
                                Color cr = getPixelColor(getKey(x + 1, y), px, x + 1, y);
                                if (cr != null && cr.a == 0) {
                                    pixelBonusaryArray.add(new PixelPoint(s, x, y));
                                    continue;
                                }

                                //top
                                Color ct = getPixelColor(getKey(x, y + 1), px, x, y + 1);
                                if (ct != null && ct.a == 0) {
                                    pixelBonusaryArray.add(new PixelPoint(s, x, y));
                                    continue;
                                }

                                //bottom
                                Color cb = getPixelColor(getKey(x, y - 1), px, x, y - 1);
                                if (cb != null && cb.a == 0) {
                                    pixelBonusaryArray.add(new PixelPoint(s, x, y));
                                    continue;
                                }
                            } else {
                                c.r = 1;
                                c.g = 1;
                                c.b = 1;
                            }

                            px2.drawPixel(x, y, c.rgba8888(c));
                        }
                    }
//        for (PixelPoint pp : pixelBonusaryArray) {
//            Color c = getPixelColor(pp.key, px, pp.x, pp.y);
//            c.r = 1;
//            c.g = 0;
//            c.b = 0;
//            px2.drawPixel(pp.x, pp.y, c.rgba8888(c));
//        }
                    //修改边界点像素
                    for (int n = 0; n < times; ++n) {
                        pixelBonusaryTempArray.clear();
                        for (PixelPoint pp : pixelBonusaryArray) {
                            //left
                            int x = pp.x - 1;
                            int y = pp.y;
                            if (x < px.getWidth() && y < px.getHeight()) {
                                Color cl = bonusaryPixel(x, y, px);
                                px2.drawPixel(x, y, cl.rgba8888(cl));
                            }
                            //right
                            x = pp.x + 1;
                            y = pp.y;
                            if (x < px.getWidth() && y < px.getHeight()) {
                                Color cr = bonusaryPixel(x, y, px);
                                px2.drawPixel(x, y, cr.rgba8888(cr));
                            }
                            //top
                            x = pp.x;
                            y = pp.y + 1;
                            if (x < px.getWidth() && y < px.getHeight()) {
                                Color ct = bonusaryPixel(x, y, px);
                                px2.drawPixel(x, y, ct.rgba8888(ct));
                            }
                            //bottom
                            x = pp.x;
                            y = pp.y - 1;
                            if (x < px.getWidth() && y < px.getHeight()) {
                                Color cb = bonusaryPixel(x, y, px);
                                px2.drawPixel(x, y, cb.rgba8888(cb));
                            }
                        }

                        Array<PixelPoint> t = pixelBonusaryArray;
                        pixelBonusaryArray = pixelBonusaryTempArray;
                        pixelBonusaryTempArray = t;
                    }
                    PixmapIO.writePNG(new FileHandle(writePath + File.separator + pictureName), px2);

                    //<<<<<<<<<<<<<<<<<<<< debug >>>>>>>>>>>>>>>>>>>>
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
                    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                }
            }
        };
    }

    protected String getKey(int x, int y) {
        return x + "_" + y;
    }

    protected Color bonusaryPixel(int x, int y, Pixmap px) {
        if (x < px.getWidth() && y < px.getHeight()) {
            String key = getKey(x, y);
            Color c = getPixelColor(key, px, x, y);
            if (c.a == 0) {
                float r = 0, g = 0, b = 0;
                int num = 0;
                //left
                Color cl = getPixelColor(getKey(x - 1, y), px, x - 1, y);
                if (cl != null && cl.a > 0) {
                    r += cl.r;
                    g += cl.g;
                    b += cl.b;
                    num += 1;
                }
                //right
                Color cr = getPixelColor(getKey(x + 1, y), px, x + 1, y);
                if (cr != null && cr.a > 0) {
                    r += cr.r;
                    g += cr.g;
                    b += cr.b;
                    num += 1;
                }
                //top
                Color ct = getPixelColor(getKey(x, y + 1), px, x, y + 1);
                if (ct != null && ct.a > 0) {
                    r += ct.r;
                    g += ct.g;
                    b += ct.b;
                    num += 1;
                }
                //bottom
                Color cb = getPixelColor(getKey(x, y - 1), px, x, y - 1);
                if (cb != null && cb.a > 0) {
                    r += cb.r;
                    g += cb.g;
                    b += cb.b;
                    num += 1;
                }

                if (num > 0) {
                    c.r = 1;
                    c.b = 0;
                    c.g = 0;
                    c.r = MathUtils.clamp(r / num, 0, 1);
                    c.g = MathUtils.clamp(g / num, 0, 1);
                    c.b = MathUtils.clamp(b / num, 0, 1);
                    pixelBonusaryTempArray.add(new PixelPoint(key, x, y));
                } else {
                    c.r = 1;
                    c.g = 1;
                    c.b = 1;
                }
            }
            return c;
        }
        return null;
    }

    protected Color getPixelColor(String key, Pixmap px, int x, int y) {
        if (x < px.getWidth() && y < px.getHeight()) {
            Color c = pixelHashMap.get(key);
            if (c == null) {
                c = new Color(px.getPixel(x, y));
                pixelHashMap.put(key, c);
            }
            return c;
        }
        return null;
    }

    public class PixelPoint {
        String key;
        int x;
        int y;

        public PixelPoint(String key, int x, int y) {
            this.key = key;
            this.x = x;
            this.y = y;
        }
    }
}

package com.mygdx.game.Logic.Function.Picture.ProcessPicturePixels.ModifyBlankPixels;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;

import java.io.File;

/**
 * Created by Doodle on 2022/1/5.
 */

public class DealPictureLinner extends Game {
    private File file = new File("");
    private final String rootPath = new File(file.getAbsolutePath()).getParentFile().getParent() +
            File.separator + "desktop" + File.separator + "src" + File.separator + "com" +
            File.separator + "nonogrampuzzle" + File.separator + "game" + File.separator + "Tools";

    private final String srcPath = rootPath + File.separator + "DealPictureRGB" + File.separator + "OldPicture";
    private final String desPath = "New/";

    @Override
    public void create() {
        File readPictures = new File("Old/");

        for (File a : readPictures.listFiles()) {
            if (a.getName().endsWith(".png")) {
                dealPicture(a);
            }
        }
    }

    public void dealPicture(File a) {
//        Pixmap pixmap = new Pixmap(new FileHandle(a));
//        pixmap.setBlending(Pixmap.Blending.None);
//        Color[][] colorArray = getColorArray(pixmap.getHeight(), pixmap.getWidth());
//        for (int r = 0; r < pixmap.getHeight(); ++r) {
//            for (int l = 0; l < pixmap.getWidth(); ++l) {
//                pixmap.drawPixel(l, r, Color.rgba8888(getColorRange(r, l, pixmap, colorArray)));
//            }
//        }
//        pixmap.setBlending(Pixmap.Blending.SourceOver);
//        PixmapIO.writePNG(new FileHandle(desPath + File.separator + a.getName()), pixmap);
//        pixmap.dispose();


        Pixmap pixmap = new Pixmap(new FileHandle(a));
        pixmap.setBlending(Pixmap.Blending.None);
        for (int r = 0; r < pixmap.getHeight(); ++r) {
            for (int l = 0; l < pixmap.getWidth(); ++l) {
                Color cc = new Color(pixmap.getPixel(r, l));
                if(cc.a == 0) {
                    pixmap.drawPixel(l, r, Color.rgba8888(1, 1, 1, 1));
                }
            }
        }
        pixmap.setBlending(Pixmap.Blending.SourceOver);
        PixmapIO.writePNG(new FileHandle(desPath + File.separator + a.getName()), pixmap);
        pixmap.dispose();
    }

    //只计算有四周有A值的像素，但并不修改透明的背景色块
    //row, list
    public Color getColorRange(int i, int j, Pixmap pixmap, Color[][] colors) {
        int minI = -1;
        int maxI = colors.length;
        int minJ = -1;
        int maxJ = colors[0].length;

        int c = j;
        j = i;
        i = c;

        if (colors[i][j] == null) {
            colors[i][j] = new Color(pixmap.getPixel(i, j));
        }

        if (colors[i][j].a > 0) {
            return colors[i][j];
        }

        float rn = colors[i][j].r;
        float gn = colors[i][j].g;
        float bn = colors[i][j].b;
        int n = 0;

        if (i + 1 > minI && i + 1 < maxI && j - 1 > minJ && j - 1 < maxJ) {
            if (colors[i + 1][j - 1] == null) {
                colors[i + 1][j - 1] = new Color(pixmap.getPixel(i + 1, j - 1));
            }

            if (colors[i + 1][j - 1].a > 0) {
                rn += colors[i + 1][j - 1].r;
                gn += colors[i + 1][j - 1].g;
                bn += colors[i + 1][j - 1].b;
                n++;
            }
        }

        if (i + 1 > minI && i + 1 < maxI && j > minJ && j < maxJ) {
            if (colors[i + 1][j] == null) {
                colors[i + 1][j] = new Color(pixmap.getPixel(i + 1, j));
            }
            if (colors[i + 1][j].a > 0) {
                rn += colors[i + 1][j].r;
                gn += colors[i + 1][j].g;
                bn += colors[i + 1][j].b;
                n++;
            }
        }

        if (i + 1 > minI && i + 1 < maxI && j + 1 > minJ && j + 1 < maxJ) {
            if (colors[i + 1][j + 1] == null) {
                colors[i + 1][j + 1] = new Color(pixmap.getPixel(i + 1, j + 1));
            }
            if (colors[i + 1][j + 1].a > 0) {
                rn += colors[i + 1][j + 1].r;
                gn += colors[i + 1][j + 1].g;
                bn += colors[i + 1][j + 1].b;
                n++;
            }
        }

        if (i > minI && i < maxI && j - 1 > minJ && j - 1 < maxJ) {
            if (colors[i][j - 1] == null) {
                colors[i][j - 1] = new Color(pixmap.getPixel(i, j - 1));
            }
            if (colors[i][j - 1].a > 0) {
                rn += colors[i][j - 1].r;
                gn += colors[i][j - 1].g;
                bn += colors[i][j - 1].b;
                n++;
            }
        }

        if (i > minI && i < maxI && j + 1 > minJ && j + 1 < maxJ) {
            if (colors[i][j + 1] == null) {
                colors[i][j + 1] = new Color(pixmap.getPixel(i, j + 1));
            }
            if (colors[i][j + 1].a > 0) {
                rn += colors[i][j + 1].r;
                gn += colors[i][j + 1].g;
                bn += colors[i][j + 1].b;
                n++;
            }
        }

        if (i - 1 > minI && i - 1 < maxI && j - 1 > minJ && j - 1 < maxJ) {
            if (colors[i - 1][j - 1] == null) {
                colors[i - 1][j - 1] = new Color(pixmap.getPixel(i - 1, j - 1));
            }
            if (colors[i - 1][j - 1].a > 0) {
                rn += colors[i - 1][j - 1].r;
                gn += colors[i - 1][j - 1].g;
                bn += colors[i - 1][j - 1].b;
                n++;
            }
        }

        if (i - 1 > minI && i - 1 < maxI && j > minJ && j < maxJ) {
            if (colors[i - 1][j] == null) {
                colors[i - 1][j] = new Color(pixmap.getPixel(i - 1, j));
            }
            if (colors[i - 1][j].a > 0) {
                rn += colors[i - 1][j].r;
                gn += colors[i - 1][j].g;
                bn += colors[i - 1][j].b;
                n++;
            }
        }

        if (i - 1 > minI && i - 1 < maxI && j + 1 > minJ && j + 1 < maxJ) {
            if (colors[i - 1][j + 1] == null) {
                colors[i - 1][j + 1] = new Color(pixmap.getPixel(i - 1, j + 1));
            }
            if (colors[i - 1][j + 1].a > 0) {
                rn += colors[i - 1][j + 1].r;
                gn += colors[i - 1][j + 1].g;
                bn += colors[i - 1][j + 1].b;
                n++;
            }
        }

        if (n > 0) {
            rn /= n;
            gn /= n;
            bn /= n;
        }

        if (rn > 1) {
            rn = 1;
        } else if (rn < 0) {
            rn = 0;
        }
        if (gn > 1) {
            gn = 1;
        } else if (gn < 0) {
            gn = 0;
        }
        if (bn > 1) {
            bn = 1;
        } else if (bn < 0) {
            bn = 0;
        }

        return new Color(rn, gn, bn, 0);
    }

    //全计算取均值，并将透明像素背景变成白色
    public Color getColorRange2(int i, int j, Pixmap pixmap, Color[][] colors) {
        int minI = -1;
        int maxI = colors.length;
        int minJ = -1;
        int maxJ = colors[0].length;

        int c = j;
        j = i;
        i = c;

        if (colors[i][j] == null) {
            colors[i][j] = new Color(pixmap.getPixel(i, j));
        }

        if (colors[i][j].a > 0) {
            return colors[i][j];
        }

        float rn = colors[i][j].r = 1;
        float gn = colors[i][j].g = 1;
        float bn = colors[i][j].b = 1;
        int n = 0;

        if (i + 1 > minI && i + 1 < maxI && j - 1 > minJ && j - 1 < maxJ) {
            if (colors[i + 1][j - 1] == null) {
                colors[i + 1][j - 1] = new Color(pixmap.getPixel(i + 1, j - 1));
                if (colors[i + 1][j - 1].a == 0) {
                    colors[i + 1][j - 1].r = 1;
                    colors[i + 1][j - 1].g = 1;
                    colors[i + 1][j - 1].b = 1;
                }
            }

            rn += colors[i + 1][j - 1].r;
            gn += colors[i + 1][j - 1].g;
            bn += colors[i + 1][j - 1].b;
            n++;
        }

        if (i + 1 > minI && i + 1 < maxI && j > minJ && j < maxJ) {
            if (colors[i + 1][j] == null) {
                colors[i + 1][j] = new Color(pixmap.getPixel(i + 1, j));
                if (colors[i + 1][j].a == 0) {
                    colors[i + 1][j].r = 1;
                    colors[i + 1][j].g = 1;
                    colors[i + 1][j].b = 1;
                }
            }
            rn += colors[i + 1][j].r;
            gn += colors[i + 1][j].g;
            bn += colors[i + 1][j].b;
            n++;
        }

        if (i + 1 > minI && i + 1 < maxI && j + 1 > minJ && j + 1 < maxJ) {
            if (colors[i + 1][j + 1] == null) {
                colors[i + 1][j + 1] = new Color(pixmap.getPixel(i + 1, j + 1));
                if (colors[i + 1][j + 1].a == 0) {
                    colors[i + 1][j + 1].r = 1;
                    colors[i + 1][j + 1].g = 1;
                    colors[i + 1][j + 1].b = 1;
                }
            }
            rn += colors[i + 1][j + 1].r;
            gn += colors[i + 1][j + 1].g;
            bn += colors[i + 1][j + 1].b;
            n++;
        }

        if (i > minI && i < maxI && j - 1 > minJ && j - 1 < maxJ) {
            if (colors[i][j - 1] == null) {
                colors[i][j - 1] = new Color(pixmap.getPixel(i, j - 1));
                if (colors[i][j - 1].a == 0) {
                    colors[i][j - 1].r = 1;
                    colors[i][j - 1].g = 1;
                    colors[i][j - 1].b = 1;
                }
            }
            rn += colors[i][j - 1].r;
            gn += colors[i][j - 1].g;
            bn += colors[i][j - 1].b;
            n++;
        }

        if (i > minI && i < maxI && j + 1 > minJ && j + 1 < maxJ) {
            if (colors[i][j + 1] == null) {
                colors[i][j + 1] = new Color(pixmap.getPixel(i, j + 1));
                if (colors[i][j + 1].a == 0) {
                    colors[i][j + 1].r = 1;
                    colors[i][j + 1].g = 1;
                    colors[i][j + 1].b = 1;
                }
            }
            rn += colors[i][j + 1].r;
            gn += colors[i][j + 1].g;
            bn += colors[i][j + 1].b;
            n++;
        }

        if (i - 1 > minI && i - 1 < maxI && j - 1 > minJ && j - 1 < maxJ) {
            if (colors[i - 1][j - 1] == null) {
                colors[i - 1][j - 1] = new Color(pixmap.getPixel(i - 1, j - 1));
                if (colors[i - 1][j - 1].a == 0) {
                    colors[i - 1][j - 1].r = 1;
                    colors[i - 1][j - 1].g = 1;
                    colors[i - 1][j - 1].b = 1;
                }
            }
            rn += colors[i - 1][j - 1].r;
            gn += colors[i - 1][j - 1].g;
            bn += colors[i - 1][j - 1].b;
            n++;
        }

        if (i - 1 > minI && i - 1 < maxI && j > minJ && j < maxJ) {
            if (colors[i - 1][j] == null) {
                colors[i - 1][j] = new Color(pixmap.getPixel(i - 1, j));
                if (colors[i - 1][j].a == 0) {
                    colors[i - 1][j].r = 1;
                    colors[i - 1][j].g = 1;
                    colors[i - 1][j].b = 1;
                }
            }
            rn += colors[i - 1][j].r;
            gn += colors[i - 1][j].g;
            bn += colors[i - 1][j].b;
            n++;
        }

        if (i - 1 > minI && i - 1 < maxI && j + 1 > minJ && j + 1 < maxJ) {
            if (colors[i - 1][j + 1] == null) {
                colors[i - 1][j + 1] = new Color(pixmap.getPixel(i - 1, j + 1));
                if (colors[i - 1][j + 1].a == 0) {
                    colors[i - 1][j + 1].r = 1;
                    colors[i - 1][j + 1].g = 1;
                    colors[i - 1][j + 1].b = 1;
                }
            }
            rn += colors[i - 1][j + 1].r;
            gn += colors[i - 1][j + 1].g;
            bn += colors[i - 1][j + 1].b;
            n++;
        }

        if (n > 0) {
            rn /= n;
            gn /= n;
            bn /= n;
        }

        if (rn > 1) {
            rn = 1;
        } else if (rn < 0) {
            rn = 0;
        }
        if (gn > 1) {
            gn = 1;
        } else if (gn < 0) {
            gn = 0;
        }
        if (bn > 1) {
            bn = 1;
        } else if (bn < 0) {
            bn = 0;
        }

        return new Color(rn, gn, bn, 0);
    }

    private Color[][] getColorArray(int width, int height) {
        Color[][] colors = new Color[height][width];
        for (int i = 0; i < colors.length; ++i) {
            for (int j = 0; j < colors[i].length; ++j) {
                colors[i][j] = null;
            }
        }
        return colors;
    }
}

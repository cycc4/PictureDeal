package com.mygdx.game.Picture.Picture_rgba;

import com.badlogic.gdx.Game;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * Created by Doodle on 2021/6/29.
 */

public class DealScalePicture extends Game {
    private File file = new File("");
    private final String rootPath = new File(file.getAbsolutePath()).getParentFile().getParent() +
            File.separator + "desktop" + File.separator + "src" + File.separator + "com" +
            File.separator + "nonogrampuzzle" + File.separator + "game" + File.separator + "Tools";

    private final String srcPath = rootPath + File.separator + "DealPictureRGB" + File.separator + "OldPicture";
    private final String desPath = rootPath + File.separator + "DealPictureRGB" + File.separator + "NewPicture";

    private float scale = 1;

    @Override
    public void create() {
        File readPictures = new File(srcPath);

        for (File a : readPictures.listFiles()) {
            if (a.getName().endsWith(".png")) {
                dealPicture(a);
            }
        }
    }

//    public void dealPicture(File a) {
//        try {
//            BufferedImage im = ImageIO.read(a);
//            int w = (int) (im.getWidth() * scale);
//            int h = (int) (im.getHeight() * scale);
//
//            Image _img = im.getScaledInstance(w, h, Image.SCALE_DEFAULT);
//
//            //新建一个和Image对象相同大小的画布
//            BufferedImage image = new BufferedImage(im.getWidth(), im.getHeight(), BufferedImage.TYPE_INT_RGB);
//            //获取画笔
//            Graphics2D graphics = image.createGraphics();
//            //将Image对象画在画布上,最后一个参数,ImageObserver:接收有关 Image 信息通知的异步更新接口,没用到直接传空
//            graphics.drawImage(im, 0, 0, null);
//            //释放资源
//            graphics.dispose();
//            //使用ImageIO的方法进行输出,记得关闭资源
//            OutputStream out = new FileOutputStream(desPath + File.separator + a.getName());
//            ImageIO.write(image, a.getName(), out);
//            out.close();
//            System.out.println("UUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU uuuuu UUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void dealPicture(File a) {
        try {
            BufferedImage oldImage = ImageIO.read(a);
            int type = oldImage.getType();
            int w = oldImage.getWidth();
            int h = oldImage.getHeight();
            BufferedImage newImage = null;
            if (type == BufferedImage.TYPE_CUSTOM) {
                ColorModel cm = oldImage.getColorModel();
                WritableRaster raster = cm.createCompatibleWritableRaster(w, h);
                boolean alphaPremultiplied = cm.isAlphaPremultiplied();
                newImage = new BufferedImage(cm, raster, alphaPremultiplied, null);
            } else {
                System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz:  ");
                newImage = new BufferedImage(w, h, type);
                Graphics2D g = newImage.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g.drawRenderedImage(oldImage, AffineTransform.getScaleInstance(1f, 1f));
                g.dispose();
            }

            File saveFile = new File(desPath + File.separator + a.getName());
            boolean isSuccess = ImageIO.write(newImage, "PNG", saveFile);
            if (isSuccess) {
                System.out.println("success: ");
            } else {
                System.out.println("defail: ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    public static void main(String[] strings) {
//        new LwjglApplication(new DealScalePicture());
//    }
    public static void main(String[] strings) {
        new DealScalePicture().create();
    }
}

package com.mygdx.game.Manager;

import com.mygdx.game.ConstantValue;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Logic.Tool.Particle.ParticleComposite;
import com.mygdx.game.Picture.Picture_rgba.DealPictureRGB;
import com.mygdx.game.Picture.Picture_rgba.DealPictureRGB2;
import com.mygdx.game.Picture.Picture_rgba.DealPictureRGB3;

import java.io.File;

public class ToolManager {
    MyGdxGame myGdxGame;


    public ToolManager(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;

    }

    public ParticleComposite getParticleComposite(String readPath, String writePath) {
        return new ParticleComposite(readPath, writePath);
    }

    //将图片中有颜色的区域周围加一圈像素
    public void dealPictureRGB(File pictureF) {
        DealPictureRGB3 deal = new DealPictureRGB3();
        if (ConstantValue.isDebug) {
            deal.deal(new File(ConstantValue.SRC_PATH + File.separator + "BRONZE.png"), new File(ConstantValue.DIR_PATH));
        }
    }
}

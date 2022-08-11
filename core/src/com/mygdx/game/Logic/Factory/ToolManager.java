package com.mygdx.game.Logic.Factory;

import com.mygdx.game.ConstantValue;
import com.mygdx.game.Logic.Function.NinePath.DealTextureToNinePath;
import com.mygdx.game.Logic.Function.Particle.ParticleCombination.ParticleComposite;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Logic.Function.Picture.DealPictureRGB3;
import com.mygdx.game.Logic.Function.Spine.SpineCombination.SpineCombination;


public class ToolManager {
    MyGdxGame myGdxGame;

    public ToolManager(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;

    }

    public ParticleComposite dealParticleCombination() {
        ParticleComposite pc = new ParticleComposite();
        pc.deal(getReadPath(), getWritePath());
        return pc;
    }

    //将图片中有颜色的区域周围加一圈像素
    public void dealPictureRGB() {
        DealPictureRGB3 deal = new DealPictureRGB3();
        deal.deal(getReadPath(), getWritePath());
    }

    public void dealSpineCombination() {
        SpineCombination spineCombination = new SpineCombination();
        spineCombination.deal(getReadPath(), getWritePath());
    }

    public void dealNinePacture(int left, int right, int top, int bottom) {
        DealTextureToNinePath.a = left;
        DealTextureToNinePath.b = right;
        DealTextureToNinePath.c = top;
        DealTextureToNinePath.d = bottom;
        DealTextureToNinePath dealTextureToNinePath = new DealTextureToNinePath();
        dealTextureToNinePath.deal(getReadPath(), getWritePath());
    }

    public void dealLabel() {

    }

    public String getReadPath() {
        return ConstantValue.isDebug ? ConstantValue.SRC_PATH : myGdxGame.getFormalParameter()[0];
    }

    public String getWritePath() {
        return ConstantValue.isDebug ? ConstantValue.DIR_PATH : myGdxGame.getFormalParameter()[1];
    }
}

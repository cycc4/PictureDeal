package com.mygdx.game.Logic;

import com.mygdx.game.Constants;
import com.mygdx.game.Logic.Function.Particle.ParticleCombination.ParticleComposite;
import com.mygdx.game.Logic.Function.Picture.ProcessPicturePixels.ModifyBlankPixels.DealPictureRGB4;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Logic.Function.Spine.SpineCombination.SpineCombination;

public class ToolsFactory {
    private MyGdxGame myGdxGame;

    public ToolsFactory(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
    }

    public ParticleComposite dealParticleCombination() {
        ParticleComposite pc = new ParticleComposite();
        pc.deal(getReadPath(), getWritePath());
        return pc;
    }

    //将图片中有颜色的区域周围加一圈像素
    public void dealPictureRGB() {
//        DealPictureRGB4 deal = new DealPictureRGB4();
        DealPictureRGB4 deal = new DealPictureRGB4();
        deal.deal(getReadPath(), getWritePath());
    }

    //将spin合图
    public void dealSpineCombination() {
        SpineCombination spineCombination = new SpineCombination();
        spineCombination.deal(getReadPath(), getWritePath());
    }

    //拆图
    public void dealDisassemblyPicture() {

    }

    public void dealNinePacture(int left, int right, int top, int bottom) {
//        DealTextureToNinePath.a = left;
//        DealTextureToNinePath.b = right;
//        DealTextureToNinePath.c = top;
//        DealTextureToNinePath.d = bottom;
//        DealTextureToNinePath dealTextureToNinePath = new DealTextureToNinePath();
//        dealTextureToNinePath.deal(getReadPath(), getWritePath());
    }

    //调整字体
    public void adjustLabel() {

    }

    public String getReadPath() {
        return Constants.isDebug ? Constants.SRC_PATH : myGdxGame.getFormalParameter()[0];
    }

    public String getWritePath() {
        return Constants.isDebug ? Constants.DIR_PATH : myGdxGame.getFormalParameter()[1];
    }
}

package com.mygdx.game.Logic.Tool.Atlas;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class CombinationListener extends TexturePacker.ProgressListener {
    private boolean isFinish = false;

    @Override
    public void progress(float v) {
        if (!isFinish && v >= 1) {
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<  finish  >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            isFinish = true;
            combinationFinish();
        }
    }

    public void combinationFinish() {

    }
}

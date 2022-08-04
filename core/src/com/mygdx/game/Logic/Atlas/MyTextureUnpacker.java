package com.mygdx.game.Logic.Atlas;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.tools.texturepacker.TextureUnpacker;

public class MyTextureUnpacker extends TextureUnpacker {

    @Override
    public void splitAtlas(TextureAtlas.TextureAtlasData atlas, String outputDir) {
        super.splitAtlas(atlas, outputDir);
        UnpackFinish();
    }

    public void UnpackFinish(){

    }
}

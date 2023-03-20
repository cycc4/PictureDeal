package com.mygdx.game.Draw.Layout;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.MyGdxGame;

public class PictureLayer extends BaseLayout {
    protected int width = 853;
    protected int height = 913;
    protected TextureRegion textureRegion;

    public PictureLayer(MyGdxGame myGdxGame) {
        super(853, 913);
        textureRegion = new TextureRegion();
    }
}

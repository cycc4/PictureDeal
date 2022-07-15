package com.mygdx.game.Draw.Actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TextureRegionActor extends Actor {
    TextureRegion textureRegion;

    public TextureRegionActor(TextureRegion textureRegion) {
        setTextureRegion(textureRegion);
    }

    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (textureRegion != null) {
            Color oldColor = batch.getColor();
            batch.setColor(getColor());
            batch.draw(textureRegion, getX(), getY(), getWidth(), getHeight());
            batch.setColor(oldColor);
        }
    }
}

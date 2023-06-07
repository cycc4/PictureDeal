package com.mygdx.game.Draw.Actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Res;
import com.qs.utils.assets.Assets;

public class SelectActor extends Actor {
    final TextureRegion region = new TextureRegion(Assets.getIns().getTexture(Res.PICTURE_DIR + "pot.png"));
    private boolean isSelected = false;

    public void setIsSelected(boolean selected){
        isSelected = selected;
    }

    public boolean isSelected(){
        return isSelected;
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isSelected) {
            Color color = batch.getColor();
            batch.setColor(0, 1, 0, 1);
            batch.draw(region, 0, 0, 1, getHeight());
            batch.draw(region, 0, getHeight(), getWidth(), 1);
            batch.draw(region, getWidth(), 0, 1, getHeight());
            batch.draw(region, 0, 0, getWidth(), 1);
            batch.setColor(color);
        }
    }
}

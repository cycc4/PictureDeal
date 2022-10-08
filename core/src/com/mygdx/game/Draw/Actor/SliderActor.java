package com.mygdx.game.Draw.Actor;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

public class SliderActor extends TextureRegionActor {
    float MaximumDistance;
    boolean isHorize;

    public SliderActor(TextureRegion textureRegion, boolean isHorize) {
        super(textureRegion);
        this.isHorize = isHorize;
        setSize(textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
        addListener(new ActorGestureListener(){
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                super.pan(event, x, y, deltaX, deltaY);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        if (isHorize) {
            MaximumDistance = width;
        } else {
            MaximumDistance = height;
        }
    }

    @Override
    public void setWidth(float width) {
        super.setWidth(width);
        if (isHorize) {
            MaximumDistance = width;
        }
    }

    @Override
    public void setHeight(float height) {
        super.setHeight(height);
        if (!isHorize) {
            MaximumDistance = height;
        }
    }
}
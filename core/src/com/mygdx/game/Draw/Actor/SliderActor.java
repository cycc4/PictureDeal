package com.mygdx.game.Draw.Actor;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

public class SliderActor extends TextureRegionActor {
    float Percentage;
    boolean isHorize;

    public SliderActor(TextureRegion textureRegion, final boolean isHorize) {
        super(textureRegion);
        this.isHorize = isHorize;
        setSize(textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
        addListener(new ActorGestureListener() {
            float startX, startY;

            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                startX = x;
                startY = y;
            }

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                if (isHorize) {
                    Percentage = (x - startX) / getWidth();
                } else {
                    Percentage = (y - startY) / getHeight();
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                startX = 0;
                startY = 0;
            }
        });
    }

    public void setPercentage(float percentage) {
        Percentage = percentage;
    }

    public float getPercentage() {
        return Percentage;
    }

    public void setPercentagePosition(float position) {
        if (isHorize) {
            setPercentage(MathUtils.clamp(position, 0, getHeight()) / getWidth());
        } else {
            setPercentage(MathUtils.clamp(position, 0, getHeight()) / getHeight());
        }
    }
}
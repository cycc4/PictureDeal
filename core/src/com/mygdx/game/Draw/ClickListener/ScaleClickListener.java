package com.mygdx.game.Draw.ClickListener;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ScaleClickListener extends ClickListener {
    private float touchDownScale;
    private float touchUpScale;

    public ScaleClickListener(float touchDownScale, float touchUpScale) {
        this.touchDownScale = touchDownScale;
        this.touchUpScale = touchUpScale;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if (pointer == 0 && super.touchDown(event, x, y, pointer, button)) {
            Actor a = event.getTarget();
            a.setScale(touchDownScale);
            return true;
        }
        return false;
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        super.touchUp(event, x, y, pointer, button);
        Actor a = event.getTarget();
        a.setScale(touchUpScale);
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {

    }
}

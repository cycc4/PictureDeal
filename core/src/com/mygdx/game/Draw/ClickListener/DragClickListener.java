package com.mygdx.game.Draw.ClickListener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class DragClickListener extends ClickListener {
    protected float touchDownX;
    protected float touchDownY;

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if (super.touchDown(event, x, y, pointer, button)) {
            touchDownX = x;
            touchDownY = y;
            return true;
        }
        return false;
    }

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        super.touchDragged(event, x, y, pointer);
        event.getTarget().moveBy(x - touchDownX, y - touchDownY);
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        super.touchUp(event, x, y, pointer, button);
    }
}

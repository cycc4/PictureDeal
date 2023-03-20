package com.qs.utils.listeners;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by Administrator on 2017/7/19.
 */
public class ShadowClickListener extends ClickListener {

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        downAction(event);
        playSound();
        return super.touchDown(event, x, y, pointer, button);
    }

    protected void downAction(InputEvent event) {
        event.getListenerActor().setColor(Color.GRAY);
    }

    protected void playSound() {
//		SoundPlayer.instance.playsound(SoundData.Setting_Click);
    }

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        super.touchDragged(event, x, y, pointer);
        if (isPressed())
            downAction(event);
        else {
            upAction(event);
        }
    }

    protected void upAction(InputEvent event) {
        event.getListenerActor().setColor(Color.WHITE);
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        upAction(event);
        super.touchUp(event, x, y, pointer, button);
    }
}

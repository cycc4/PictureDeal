package com.qs.utils.listeners;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;

/**
 * Created by Administrator on 2017/7/19.
 */
public abstract class ShadowClickListener2Hold extends ClickListener {

    private Actor listenerActor;
    private final Timer.Task longPressTask = new Timer.Task() {
        @Override
        public void run() {
            if (longPress()) {
                ShadowClickListener2Hold.this.cancel();
                if (listenerActor != null)
                    listenerActor.setColor(Color.WHITE);
            }
        }
    };

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        listenerActor = event.getListenerActor();
        listenerActor.setColor(Color.GRAY);
        Timer.schedule(longPressTask, 1.1f);
        playSound();
        return super.touchDown(event, x, y, pointer, button);
    }

//	protected void playSound () {
//		SoundPlayer.instance.playsound(SoundData.button);
//	}

    abstract protected void playSound();

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        super.touchDragged(event, x, y, pointer);
        listenerActor = event.getListenerActor();
        if (isPressed()) {
            listenerActor.setColor(Color.GRAY);
        } else {
            longPressTask.cancel();
            listenerActor.setColor(Color.WHITE);
        }
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        event.getListenerActor().setColor(Color.WHITE);
        longPressTask.cancel();
        super.touchUp(event, x, y, pointer, button);
    }

    public boolean longPress() {
        return true;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
    }

    @Override
    public void cancel() {
        super.cancel();
    }
}

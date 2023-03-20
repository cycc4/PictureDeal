package com.qs.utils.listeners;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
//import com.qs.slot.music.SoundData;
//import com.qs.slot.music.SoundPlayer;

/**
 * Created by Administrator on 2017/7/19.
 */
public abstract class ShadowClickListener4HoldGroup extends ClickListener {

    private Actor listenerActor;
    private final Timer.Task longPressTask = new Timer.Task() {
        @Override
        public void run() {
            if (longPress()) {
                ShadowClickListener4HoldGroup.this.cancel();
                if (listenerActor != null)
                    showUp(listenerActor);
            }
        }
    };

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        listenerActor = event.getListenerActor();
        showDown(listenerActor);
        if (!longPressTask.isScheduled()) {
            Timer.schedule(longPressTask, 1.1f);
        }
        playSound();
        return super.touchDown(event, x, y, pointer, button);
    }

//	protected void playSound () {
//		SoundPlayer.instance.playsound(SoundData.button);
//	}

    protected abstract void playSound();

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        super.touchDragged(event, x, y, pointer);
        listenerActor = event.getListenerActor();
        if (isPressed()) {
            showDown(listenerActor);
        } else {
            longPressTask.cancel();
            showUp(listenerActor);
        }
    }

    private void showDown(Actor listenerActor) {
        listenerActor.setColor(Color.GRAY);
        if (listenerActor instanceof Group) {
            Group listenerGroup = (Group) listenerActor;
            listenerGroup.setColor(Color.GRAY);
            for (Actor child : listenerGroup.getChildren()) {
                showDown(child);
            }
        }
    }

    private void showUp(Actor listenerActor) {
        listenerActor.setColor(Color.WHITE);
        if (listenerActor instanceof Group) {
            Group listenerGroup = (Group) listenerActor;
            listenerGroup.setColor(Color.WHITE);
            for (Actor child : listenerGroup.getChildren()) {
                showUp(child);
            }
        }
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        listenerActor = event.getListenerActor();
        showUp(listenerActor);
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

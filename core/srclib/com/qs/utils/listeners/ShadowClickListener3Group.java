package com.qs.utils.listeners;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
//import com.qs.ball.music.SoundData;
//import com.qs.ball.music.SoundPlayer;
//import com.qs.slot.SG;
//import com.qs.slot.music.SoundData;
//import com.qs.slot.music.SoundPlayer;

/**
 * Created by Administrator on 2017/7/19.
 */
public class ShadowClickListener3Group extends ClickListener {

    boolean grayed = false;
    private Group listenerGroup;

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        showDown(event);
        playSound();
        return super.touchDown(event, x, y, pointer, button);
    }

//	protected void playSound () {
//	}

    protected void playSound() {
//		SoundPlayer.instance.playsound(SoundData.button_click);
    }

    protected void showDown(InputEvent event) {
        if (grayed)
            return;
        grayed = true;

        Actor listenerActor = event.getListenerActor();
        listenerActor.getColor().mul(Color.GRAY);
        if (listenerActor instanceof Group) {
            setGroupAll((Group) listenerActor, grayed);
        }
    }

    private void setGroupAll(Group listenerActor, boolean grayed) {
        if (grayed) {
            listenerActor.getColor().mul(Color.GRAY);
        } else {
            listenerActor.getColor().mul(1 / Color.GRAY.r, 1 / Color.GRAY.g, 1 / Color.GRAY.b, 1 / Color.GRAY.a);
        }
        for (Actor child : listenerActor.getChildren()) {
            if (grayed) {
                child.getColor().mul(Color.GRAY);
            } else {
                child.getColor().mul(1 / Color.GRAY.r, 1 / Color.GRAY.g, 1 / Color.GRAY.b, 1 / Color.GRAY.a);
            }
            if (child instanceof Group) {
                setGroupAll((Group) child, grayed);
            }
        }
    }

    protected void showUp(InputEvent event) {
        if (!grayed)
            return;
        grayed = false;

        Actor listenerActor = event.getListenerActor();
        listenerActor.getColor().mul(1 / Color.GRAY.r, 1 / Color.GRAY.g, 1 / Color.GRAY.b, 1 / Color.GRAY.a);
        if (listenerActor instanceof Group) {
            setGroupAll((Group) listenerActor, grayed);
        }
    }

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        super.touchDragged(event, x, y, pointer);
        if (isPressed())
            showDown(event);
        else
            showUp(event);
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        showUp(event);
        super.touchUp(event, x, y, pointer, button);
    }
}

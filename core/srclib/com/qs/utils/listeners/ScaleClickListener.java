package com.qs.utils.listeners;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class ScaleClickListener extends ClickListener {

    protected Actor onActor;
    protected float onScale;

    public ScaleClickListener() {
        this(null, 1f);
    }

    public ScaleClickListener(float scale) {
        this(null, scale);
    }

    public ScaleClickListener(Actor actor, float scale) {
        this.onActor = actor;
        this.onScale = scale;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if (pointer == 0 && super.touchDown(event, x, y, pointer, button)) {
            showDown(event);
            playSound();
            return true;
        }
        return false;
    }

    protected void playSound() {
    }

    Actor actor;
    float oldScale;
    boolean big;

    protected void showUp(InputEvent event) {
        if (!big)
            return;
        big = false;

        if (this.onActor != null)
            this.actor = onActor;
        else
            this.actor = event.getListenerActor();
        actor.setScale(oldScale);
    }

    protected void showDown(InputEvent event) {
        if (Math.abs(onScale - 1) < 0.01f)
            return;

        if (big)
            return;
        big = true;

        if (this.onActor != null)
            this.actor = onActor;
        else
            this.actor = event.getListenerActor();

        oldScale = actor.getScaleX();
        float scale = onScale * oldScale;

        actor.setOrigin(Align.center);
        actor.setScale(scale);
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

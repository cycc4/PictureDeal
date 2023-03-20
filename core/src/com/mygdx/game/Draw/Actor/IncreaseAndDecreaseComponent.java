package com.mygdx.game.Draw.Actor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.qs.utils.actor.ActorFactory;
import com.mygdx.game.Draw.ClickListener.ScaleClickListener;
import com.mygdx.game.Res;

public class IncreaseAndDecreaseComponent extends Group {
    protected int changeText = 0;
    protected Label creaseLabel;
    protected Actor increaseActor;
    protected Actor decreaseActor;

    public IncreaseAndDecreaseComponent(Actor increaseActor, Actor decreaseActor) {
        this.increaseActor = increaseActor;
        this.decreaseActor = decreaseActor;

        creaseLabel = ActorFactory.getLabel(changeText + "", Res.DEFAULT_36_PATH);
        creaseLabel.setAlignment(Align.center);
        addActor(creaseLabel);

        increaseActor.setOrigin(Align.center);
        increaseActor.addListener(new ScaleClickListener(0.9f, 1) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ++changeText;
                setText();
                change(1, changeText);
            }
        });
        addActor(increaseActor);

        decreaseActor.setOrigin(Align.center);
        decreaseActor.addListener(new ScaleClickListener(0.9f, 1) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                --changeText;
                setText();
                change(-1, changeText);
            }
        });
        addActor(decreaseActor);

        debugAll();
    }

    @Override
    protected void sizeChanged() {
        creaseLabel.setPosition(getWidth() / 2f, getHeight() / 2f, Align.center);
        increaseActor.setPosition(getWidth(), getHeight() / 2f, Align.left);
        decreaseActor.setPosition(0, getHeight() / 2f, Align.right);
    }

    protected void setText() {
        creaseLabel.setText(changeText);
    }

    public void change(int changeTextOffset, int changeText) {

    }
}

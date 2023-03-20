package com.mygdx.game.Draw.Actor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Draw.ClickListener.ScaleClickListener;

public class TwoIncreaseAndDecreaseComponent extends IncreaseAndDecreaseComponent {
    protected Actor downIncreaseActor;
    protected Actor upDecreaseActor;
    protected int changeVerticalText;

    public TwoIncreaseAndDecreaseComponent(Actor leftIncreaseActor, Actor rightDecreaseActor, Actor downIncreaseActor, Actor upDecreaseActor) {
        super(leftIncreaseActor, rightDecreaseActor);
        this.downIncreaseActor = downIncreaseActor;
        this.upDecreaseActor = upDecreaseActor;

        downIncreaseActor.setOrigin(Align.center);
        downIncreaseActor.addListener(new ScaleClickListener(0.9f, 1) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ++changeVerticalText;
                setText();
                changeVertical(1, changeVerticalText);
            }
        });
        addActor(downIncreaseActor);

        upDecreaseActor.setOrigin(Align.center);
        upDecreaseActor.addListener(new ScaleClickListener(0.9f, 1) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                --changeVerticalText;
                setText();
                changeVertical(-1, changeVerticalText);
            }
        });
        addActor(upDecreaseActor);

        debugAll();
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        if (downIncreaseActor != null) {
            downIncreaseActor.setPosition(getWidth() / 2f, 0, Align.top);
        }

        if (upDecreaseActor != null) {
            upDecreaseActor.setPosition(getWidth() / 2f, getHeight(), Align.bottom);
        }

    }

    @Override
    protected void setText() {
        creaseLabel.setText(changeText + "/" + changeVerticalText);
    }

    public void changeVertical(int changeTextOffset, int changeText) {

    }
}

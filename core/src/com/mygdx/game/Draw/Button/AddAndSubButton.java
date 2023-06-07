package com.mygdx.game.Draw.Button;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.Draw.ClickListener.ScaleClickListener;

public class AddAndSubButton {
    Group rootGroup;
    Label textLabel;

    public AddAndSubButton(Group buttonGroup) {
        rootGroup = buttonGroup;
        textLabel = buttonGroup.findActor("text");

        Actor addButton = buttonGroup.findActor("right");
        addButton.setTouchable(Touchable.enabled);
        addButton.addListener(new ScaleClickListener(0.9f, 1) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonClick(event, true);
            }
        });
        Label addLabel = buttonGroup.findActor("add");
        addLabel.setTouchable(Touchable.disabled);

        Actor subButton = buttonGroup.findActor("left");
        subButton.setTouchable(Touchable.enabled);
        subButton.addListener(new ScaleClickListener(0.9f, 1) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonClick(event, false);
            }
        });
        Label subLabel = buttonGroup.findActor("sub");
        subLabel.setTouchable(Touchable.disabled);
    }

    public void buttonClick(InputEvent event, boolean isAdd) {

    }

    public void setText(String s) {
        textLabel.setText(s);
    }

    public void setTouchable(boolean isTouch) {
        if (isTouch) {
            rootGroup.getColor().a = 1;
            rootGroup.setTouchable(Touchable.enabled);
        } else {
            rootGroup.getColor().a = 0.2f;
            rootGroup.setTouchable(Touchable.disabled);
        }
    }
}

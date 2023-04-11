package com.mygdx.game.Draw.Button;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.Draw.ClickListener.ScaleClickListener;

public class AddAndSubButton {
    Label textLabel;

    public AddAndSubButton(Group buttonGroup) {
        textLabel = buttonGroup.findActor("text");

        Actor addButton = buttonGroup.findActor("right");
        addButton.addListener(new ScaleClickListener(0.9f, 1) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonClick(event, x, y);
            }
        });
        Label addLabel = buttonGroup.findActor("add");
        addLabel.setTouchable(Touchable.disabled);

        Actor subButton = buttonGroup.findActor("left");
        subButton.addListener(new ScaleClickListener(0.9f, 1){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonClick(event, x, y);
            }
        });
    }

    public void buttonClick(InputEvent event, float x, float y) {

    }

    public void setText(String s) {
        textLabel.setText(s);
    }
}

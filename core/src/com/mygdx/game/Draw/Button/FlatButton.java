package com.mygdx.game.Draw.Button;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.Draw.ClickListener.ScaleClickListener;

public class FlatButton {
    private Actor leftActor;
    private Label leftText;
    private Actor rightActor;
    private Label rightText;

    private int flatText = 0;

    public FlatButton(Group flatGroup) {
        this(flatGroup, "left", "right");
    }

    public FlatButton(Group flatGroup, String leftButtonName, String rightButtonName) {
        initButton(flatGroup, leftButtonName, rightButtonName);
    }

    protected void initButton(Group group, String leftButtonName, String rightButtonName) {
        leftActor = group.findActor(leftButtonName);
        leftActor.addListener(new ScaleClickListener(0.9f, 1) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                flatText--;
                setText();
                leftClick(-1);
                System.out.println("left:");
            }
        });
        leftActor.setTouchable(Touchable.enabled);
        leftText = group.findActor(leftButtonName + "Text");

        rightActor = group.findActor(rightButtonName);
        rightActor.addListener(new ScaleClickListener(0.9f, 1) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                flatText++;
                setText();
                leftClick(1);
                System.out.println("right: ");
            }
        });
        rightActor.setTouchable(Touchable.enabled);
        rightText = group.findActor(rightButtonName + "Text");
    }

    protected void setText() {
        leftText.setText(-flatText);
        rightText.setText(flatText);
    }

    public int getFlatText() {
        return flatText;
    }

    public void leftClick(int flatText) {

    }

    public void rightClick(int flatText) {

    }
}

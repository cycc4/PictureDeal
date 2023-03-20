package com.mygdx.game.Draw.Button;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Draw.ClickListener.ScaleClickListener;

import org.graalvm.compiler.lir.aarch64.AArch64LIRFlagsVersioned;


public class TextButton extends Group {
    private Label textLabel;
    private Image bgImage;

    public TextButton(Group textGroup, String text) {
        setSize(textGroup.getWidth(), textGroup.getHeight());
        setPosition(textGroup.getX(), textGroup.getY());
        textGroup.setPosition(0, 0);

        textGroup.getParent().addActor(this);
        addActor(textGroup);

        textLabel = textGroup.findActor("text");
        textLabel.setText(text);
        bgImage = textGroup.findActor("bg");

        setOrigin(Align.center);

        addListener(new ScaleClickListener(0.9f, 1) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                click();
            }
        });
    }

    public void setText(String text) {
        textLabel.setText(text);
    }

    public Image getBgImage() {
        return bgImage;
    }

    public void click() {

    }

}

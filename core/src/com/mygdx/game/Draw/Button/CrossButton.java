package com.mygdx.game.Draw.Button;

import com.badlogic.gdx.scenes.scene2d.Group;

public class CrossButton {
    private FlatButton horizontalFlat;
    private FlatButton verticalFlat;

    public CrossButton(Group crossButtonGroup) {
        horizontalFlat = new FlatButton(crossButtonGroup) {
            @Override
            public void leftClick(int flatText) {
                CrossButton.this.leftClick(flatText);
            }

            @Override
            public void rightClick(int flatText) {
                CrossButton.this.rightClick(flatText);
            }
        };

        verticalFlat = new FlatButton(crossButtonGroup, "down", "up") {
            @Override
            public void leftClick(int flatText) {
                CrossButton.this.topClick(flatText);
            }

            @Override
            public void rightClick(int flatText) {
                CrossButton.this.bottomClick(flatText);
            }
        };
    }

    public int getHorizeontalText() {
        return horizontalFlat.getFlatText();
    }

    public int getVerticalText() {
        return verticalFlat.getFlatText();
    }

    public void leftClick(int num) {
    }

    public void rightClick(int num) {

    }

    public void topClick(int num) {

    }

    public void bottomClick(int num) {

    }
}

package com.mygdx.game.Draw.Layout;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.mygdx.game.Draw.Actor.TextureRegionActor;
import com.mygdx.game.Res;
import com.qs.utils.actor.ActorFactory;
import com.qs.utils.assets.Assets;

public class PictureLayer extends BaseLayout {
    Actor lineH;
    Actor lineP;
    boolean isNeedLine;
    Group backGroundGroup;

    public PictureLayer(int width, int height) {
        super(width, height);
        TextureRegion textureRegion = new TextureRegion(
                ActorFactory.getTexture(Assets.getIns().getAssetManager(), Res.PICTURE_DIR + "pot.png")
        );

        lineH = new TextureRegionActor(textureRegion);
        lineH.setSize(width, 2);
        addActor(lineH);

        lineP = new TextureRegionActor(textureRegion);
        lineP.setSize(2, height);
        addActor(lineP);

        backGroundGroup = new Group() {
            @Override
            protected void positionChanged() {
                lineH.setY(backGroundGroup.getHeight() / 2f + backGroundGroup.getY());
                lineP.setY(backGroundGroup.getWidth() / 2f + backGroundGroup.getX());
            }
        };
        backGroundGroup.setSize(width, height);
        addActor(backGroundGroup);
        backGroundGroup.addListener(new InputListener() {
            float startX, startY;

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                startX = x;
                startY = y;
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                event.getTarget().moveBy(x - startX, y - startY);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                startY = 0;
                startX = 0;
            }

            @Override
            public boolean scrolled(InputEvent event, float x, float y, int amount) {
                System.out.println("asd scrolled x: " + x + " y: " + y + " amount: " + amount);
                return false;
            }
        });
    }

    public void setNeedLine(boolean isNeedLine) {
        lineH.setVisible(isNeedLine);
        lineP.setVisible(isNeedLine);
    }

    @Override
    public void addActor(Actor actor) {
        backGroundGroup.addActor(actor);
    }

    public Group getLayoutGroup() {
        return this;
    }

    public Group getBackGroundGroup() {
        return backGroundGroup;
    }
}

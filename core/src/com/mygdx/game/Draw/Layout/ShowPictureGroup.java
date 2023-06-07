package com.mygdx.game.Draw.Layout;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Draw.Actor.TextureRegionActor;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Res;
import com.qs.utils.assets.Assets;

public class ShowPictureGroup extends Group {
    protected Actor lineH;
    protected Actor lineP;
    protected Group rootGroup;
    protected float centerX, centerY;


    protected MyGdxGame myGdxGame;

    public ShowPictureGroup(final MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        TextureRegion textureRegion = new TextureRegion(
                Assets.getIns().getTexture(Res.PICTURE_DIR + "pot.png")
        );

        lineH = new TextureRegionActor(textureRegion);

        lineP = new TextureRegionActor(textureRegion);

        rootGroup = new Group();
        super.addActor(rootGroup);
        rootGroup.debug();

        addListener(new InputListener() {
            float startX, startY;
            float rootStartX, rootStartY;

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                startX = x;
                startY = y;
                rootStartX = rootGroup.getX();
                rootStartY = rootGroup.getY();
                myGdxGame.getStage().setScrollFocus(rootGroup); //注意,需要加这句话,因为scroll touchdown中会修改
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                float offsetX = x - startX;
                float offsetY = y - startY;

                rootGroup.setPosition(rootStartX + offsetX, rootStartY + offsetY);
                lineP.setPosition(rootGroup.getX(Align.center), 0);
                lineH.setPosition(0, rootGroup.getY(Align.center));
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                startY = 0;
                startX = 0;
                rootStartX = 0;
                rootStartY = 0;
            }

            @Override
            public boolean scrolled(InputEvent event, float x, float y, int amount) {
                if (amount < 0) {
                    rootGroup.setScale(rootGroup.getScaleX() + 0.01f, rootGroup.getScaleY() + 0.01f);
                } else {
                    rootGroup.setScale(rootGroup.getScaleX() - 0.01f, rootGroup.getScaleY() - 0.01f);
                }
                return false;
            }
        });
    }

    @Override
    protected void sizeChanged() {
        centerX = getWidth() / 2f;
        centerY = getHeight() / 2f;

        lineH.setSize(getWidth(), 2);
        lineH.setPosition(centerX, centerY, Align.center);
        super.addActor(lineH);

        lineP.setSize(2, getHeight());
        lineP.setPosition(centerX, centerY, Align.center);
        super.addActor(lineP);

        rootGroup.setSize(getWidth(), getHeight());
        rootGroup.setOrigin(Align.center);
    }

    @Override
    public void addActor(Actor actor) {
        actor.setPosition(centerX, centerY);
        rootGroup.addActor(actor);
    }

    public void addCenterActor(Actor actor) {
        actor.setPosition(centerX, centerY, Align.center);
        rootGroup.addActor(actor);
    }

    public void setNeedLine(boolean isNeedLine) {
        lineH.setVisible(isNeedLine);
        lineP.setVisible(isNeedLine);
    }

    public void removeChilds() {
        rootGroup.clearChildren();
        resetChilds();
    }

    public void resetChilds() {
        rootGroup.setOrigin(Align.center);
        rootGroup.setScale(1, 1);
        rootGroup.setPosition(0, 0);

        lineH.setSize(getWidth(), 2);
        lineH.setPosition(centerX, centerY, Align.center);

        lineP.setSize(2, getHeight());
        lineP.setPosition(centerX, centerY, Align.center);
    }

    public Group getRootGroup() {
        return rootGroup;
    }
}

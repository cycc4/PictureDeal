package com.mygdx.game.Draw.Layout;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Constants;
import com.mygdx.game.MyGdxGame;

public class BaseLayout extends Group implements Disposable {
    protected MyGdxGame mainGame;

    public BaseLayout(MyGdxGame myGdxGame) {
        mainGame = myGdxGame;
    }

    protected void cameraBaseLayerout() {
        float widthScale = Constants.WORLD_WIDTH / getWidth();
        float heightScale = Constants.WORLD_HEIGHT / getHeight();
        setOrigin(Align.center);
        setScale(Math.min(widthScale, heightScale));

        setPosition(Constants.WORLD_WIDTH / 2f, Constants.WORLD_HEIGHT / 2f, Align.center);
    }

    public BaseLayout(int width, int height) {
        setSize(width, height);
    }

    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {

    }
}

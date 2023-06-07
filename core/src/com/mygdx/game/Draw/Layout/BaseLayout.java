package com.mygdx.game.Draw.Layout;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Constants;
import com.mygdx.game.MyGdxGame;
import com.qs.utils.actor.ActorFactory;
import com.qs.utils.assets.Assets;

public class BaseLayout extends Group implements Disposable {
    protected MyGdxGame mainGame;

    public BaseLayout(MyGdxGame myGdxGame) {
        mainGame = myGdxGame;
    }

    public BaseLayout(MyGdxGame myGdxGame, int width, int height) {
        mainGame = myGdxGame;
        setSize(width, height);
    }

    public BaseLayout(MyGdxGame myGdxGame, String cocosLayerPath) {
        mainGame = myGdxGame;
        Group cocosGroup = ActorFactory.createCsdJson(Assets.getIns().getAssetManager(), cocosLayerPath);
        setSize(cocosGroup.getWidth(), cocosGroup.getHeight());
        addActor(cocosGroup);
    }

    /*
     * 对当前layout进行适配
     */
    protected Vector2 cameraBaseLayout() {
        float widthScale = Constants.WORLD_WIDTH / getWidth();
        float heightScale = Constants.WORLD_HEIGHT / getHeight();
        setOrigin(Align.center);
        setScale(Math.min(widthScale, heightScale));
        setPosition(Constants.WORLD_WIDTH / 2f, Constants.WORLD_HEIGHT / 2f, Align.center);
        return new Vector2(
                Constants.WORLD_WIDTH - getWidth() * getScaleX(),
                Constants.WORLD_HEIGHT - getHeight() * getScaleY()
        );
    }

    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {

    }
}

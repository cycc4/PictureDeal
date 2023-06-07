package com.qs.utils.stage;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;

public class ShipeiStage extends Stage {
    protected final Group shiPeiGroup = new Group();
    protected final ShipeiExtendViewport mViewport;

    public ShipeiStage(float width, float height, Batch batch) {
        this(new ShipeiExtendViewport(width, height), batch);
    }

    public ShipeiStage(ShipeiExtendViewport viewport, Batch batch) {
        super(viewport, batch);
        viewport.setStage(this);
        mViewport = viewport;

        shiPeiGroup.setSize(viewport.getMinWorldWidth(), viewport.getMinWorldHeight());
        shiPeiGroup.setOrigin(viewport.getMinWorldWidth() / 2, viewport.getMinWorldHeight() / 2);
        super.addActor(shiPeiGroup);
    }

    @Override
    public void addActor(Actor actor) {
        if (actor == shiPeiGroup)
            super.addActor(shiPeiGroup);
        else
            shiPeiGroup.addActor(actor);
    }

    @Override
    public Group getRoot() {
        return shiPeiGroup;
    }

    public void update(float worldWidth, float worldHeight) {
        shiPeiGroup.setPosition(worldWidth / 2, worldHeight / 2, Align.center);
    }

    public float getModX() {
        return mViewport.getModX();
    }

    public float getModY() {
        return mViewport.getModY();
    }

    public float getXMore() {
        return mViewport.getXMore();
    }

    public float getYMore() {
        return mViewport.getYMore();
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    //    public Group getExactlyRoot() {
//        return super.getRoot();
//    }
//
//    public void addExactlyActor(Actor actor) {
//        super.addActor(actor);
//    }
//
//    public float getModScale() {
//        return mViewport.getModScale();
//    }
//
//    public float getModScaleX() {
//        return mViewport.getModScaleX();
//    }
//
//    public float getModScaleY() {
//        return mViewport.getModScaleY();
//    }

}

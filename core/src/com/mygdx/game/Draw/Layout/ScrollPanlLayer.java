package com.mygdx.game.Draw.Layout;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.mygdx.game.MyGdxGame;

public class ScrollPanlLayer extends BaseLayout {
    ScrollPane scrollPane;
    Group group;

    int lineHeight;
    public ScrollPanlLayer(MyGdxGame myGdxGame, int width, int height, boolean isHorize) {
        super(myGdxGame);
        setSize(width, height);

        group = new Group();
        group.setSize(width, height);

        scrollPane = new ScrollPane(group, new ScrollPane.ScrollPaneStyle()) {
            @Override
            public void layout() {
                ScrollPanlLayer.this.layout();
                super.layout();
            }
        };
        scrollPane.setSize(width, height);
        scrollPane.setOverscroll(isHorize, !isHorize);
        addActor(scrollPane);
    }

    public void addActor(Actor actor) {
        group.addActor(actor);
    }

    protected void layout() {
        int maxlineHeight = 0;
        for (Actor actor : group.getChildren()) {
            maxlineHeight = (int) Math.max(maxlineHeight, actor.getHeight());
        }

        float centerX = group.getWidth() / 2f;
        for (int i = group.getChildren().size - 1, h = lineHeight / 2; i > -1; --i, h += lineHeight) {
            Actor a = group.getChildren().get(i);
            a.setPosition(centerX, h);
        }
    }
}

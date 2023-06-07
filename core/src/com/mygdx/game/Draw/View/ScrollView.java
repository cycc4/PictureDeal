package com.mygdx.game.Draw.View;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Draw.Actor.TextureRegionActor;
import com.qs.utils.assets.Assets;

public class ScrollView extends Group {
    protected ScrollPane.ScrollPaneStyle style;
    protected ScrollPane scrollPane;
    protected Group group;
    protected float scrollWidth, scrollHeight;
    protected float offsetLeftOrTop, offsetRightOrBottom;
    protected boolean isHorize;
    protected TextureRegionActor chickRegionActor;
    protected Vector2 tmp = new Vector2();
    protected Array<Actor> childArray = new Array<>();
    private Actor hitActor;

    public ScrollView() {
        group = new Group() {
            @Override
            public Actor hit(float x, float y, boolean touchable) {
                return hitActor = super.hit(x, y, touchable);
            }
        };
        style = new ScrollPane.ScrollPaneStyle();
        scrollPane = new ScrollPane(group, style) {
            @Override
            public void layout() {
                ScrollView.this.layout();
                super.layout();
            }
        };
        scrollPane.setOverscroll(isHorize, !isHorize);
        setSize(200, 200);
        super.addActor(scrollPane);
    }

    public void addActor(Actor actor) {
        group.addActor(actor);
        childArray.add(actor);
        actor.debug();
    }


    protected void layout() {
        int maxLength = 0;
        if (scrollWidth == 0) {
            scrollWidth = getWidth();
        }
        if (scrollHeight == 0) {
            scrollHeight = getHeight();
        }
        group.setSize(scrollWidth, scrollHeight);
        group.debug();

        if (isHorize) {
            for (Actor actor : childArray) {
                maxLength = (int) Math.max(maxLength, actor.getWidth());
            }

            maxLength += offsetLeftOrTop;
            maxLength += offsetRightOrBottom;

            group.setWidth(Math.max(maxLength * childArray.size, scrollWidth));
            float centerY = group.getHeight() / 2f;

            float positionX = 0;
            for (int i = 0, size = childArray.size; i < size; ++i) {
                positionX = positionX + maxLength + offsetLeftOrTop;
                Actor a = childArray.get(i);
                a.setPosition(positionX, centerY, Align.left);
                positionX += offsetRightOrBottom;
            }
        } else {
            for (Actor actor : childArray) {
                maxLength = (int) Math.max(maxLength, actor.getHeight());
            }

            maxLength += offsetLeftOrTop;
            maxLength += offsetRightOrBottom;

            group.setHeight(Math.max(maxLength * childArray.size, scrollHeight));
            float centerX = group.getWidth() / 2f;

            float positionY = group.getHeight();
            for (int i = 0, size = childArray.size; i < size; ++i) {
                positionY = positionY - maxLength - offsetLeftOrTop;
                Actor a = childArray.get(i);
                a.setPosition(centerX, positionY, Align.bottom);
                positionY -= offsetRightOrBottom;
            }
        }

        group.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (super.touchDown(event, x, y, pointer, button)) {
                    System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz:  " + hitActor.getX() + "   " + hitActor.getY());
                    if (chickRegionActor != null && hitActor != null) {
                        chickRegionActor.setPosition(hitActor.getX(), hitActor.getY());
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void setOffsetLeftOrTop(float leftOrTop) {
        offsetLeftOrTop = leftOrTop;
    }

    public void setOffsetRightOrBottom(float rightOrBottom) {
        this.offsetRightOrBottom = rightOrBottom;
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        scrollPane.setSize(width, height);
    }

    public void setScrollSize(float width, float height) {
        this.scrollWidth = width;
        this.scrollHeight = height;
        scrollPane.invalidate();
    }

    public void setHorize(boolean isHorize) {
        this.isHorize = isHorize;
        scrollPane.setOverscroll(isHorize, !isHorize);
        scrollPane.invalidate();
    }

    public void setChickRegionActor(String chick, float width, float height, Color color) {
        TextureRegionActor t = new TextureRegionActor(new TextureRegion(Assets.getIns().getTexture(chick)));
        t.setSize(width, height);
        t.setColor(color);
        setChickRegionActor(t);
    }

    public void setChickRegionActor(TextureRegionActor textureRegionActor) {
        chickRegionActor = textureRegionActor;
        group.addActorAt(0, chickRegionActor);
    }

    public void setBackGround(String backGround, float leftWidth, float rightWidth, float topHeight, float bottomHeight) {
        TextureRegionDrawable td = new TextureRegionDrawable(Assets.getIns().getTexture(backGround));
        td.setLeftWidth(leftWidth);
        td.setRightWidth(rightWidth);
        td.setTopHeight(topHeight);
        td.setBottomHeight(bottomHeight);
        if (leftWidth != 0 || rightWidth != 0 || topHeight != 0 || bottomHeight != 0) {
            scrollPane.invalidate();
        }
        setBackGround(td);
    }

    public void setBackGround(Drawable drawable) {
        style.background = drawable;
    }

    @Override
    public void clear() {
        super.clear();
        childArray.clear();
    }
}

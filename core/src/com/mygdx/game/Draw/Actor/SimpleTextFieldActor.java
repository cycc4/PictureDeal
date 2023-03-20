package com.mygdx.game.Draw.Actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.utils.Clipboard;
import com.qs.utils.assets.Assets;
import com.mygdx.game.Draw.Data.TextureRegionData;
import com.mygdx.game.Res;

public class SimpleTextFieldActor extends TextureRegionActor {
    protected Clipboard clipboard;
    protected TextureRegionData waitRegionData;
    protected BitmapFont bitmapFont;
    private StringBuilder displayText;

    //
    private boolean isWaitRegionAnimation = true;
    private RepeatAction repeatAction;
    private float twinkleTime = 0.32f;

    public SimpleTextFieldActor() {
        this(new TextureRegion(Assets.getIns().getTexture(Res.PICTURE_DIR + "pot.png")),
                new TextureRegionData(new TextureRegion(Assets.getIns().getTexture(Res.PICTURE_DIR + "pot.png"))),
                Assets.getIns().getBitmapFont(Res.DEFAULT_36_PATH)
        );
        waitRegionData.setTextureRegionWidth(5);
        waitRegionData.setColor(Color.BLACK);
    }

    public SimpleTextFieldActor(TextureRegion backGroundRegion, TextureRegion waitRegion, BitmapFont bitmapFont) {
        this(backGroundRegion, new TextureRegionData(waitRegion), bitmapFont);
    }

    public SimpleTextFieldActor(TextureRegion backgroundRegion, TextureRegionData waitRegionData, BitmapFont bitmapFont) {
        super(backgroundRegion);
        this.waitRegionData = waitRegionData;
        this.bitmapFont = bitmapFont;
        init();
    }

    protected void init() {
        clipboard = Gdx.app.getClipboard();
        displayText = new StringBuilder();
        displayText.append("10");
        addAction(getWaitRegionAction());
    }

    @Override
    protected void sizeChanged() {
        waitRegionData.setTextureRegionSize(waitRegionData.getTextureRegionWidth(), getHeight() - 4);
    }

    @Override
    protected void positionChanged() {
        waitRegionData.setTextureRegionPosition(getX() + waitRegionData.getTextureRegionX(), getY() + waitRegionData.getTextureRegionY() + 2);
    }

    public String getClipBoard() {
        return clipboard.getContents();
    }

    //通过这个类来设置单独引用的waitRegion 数据
    public TextureRegionData getWaitRegionData() {
        return waitRegionData;
    }

    protected RepeatAction getWaitRegionAction() {
        return Actions.forever(
                Actions.sequence(
                        Actions.delay(0.1f),
                        Actions.alpha(0, twinkleTime),
                        Actions.alpha(1, twinkleTime)
                )
        );
    }

    protected void moveWaitRegion(int num) {
        num = MathUtils.clamp(num, 0, displayText.length());
        float w = 0;
        for (int i = 0; i < num; ++i) {
            w += bitmapFont.getCache().getLayouts().get(num).width;
        }
        waitRegionData.setTextureRegionX(getX() + w);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        drawDisplayFont(batch);
        drawWaitRegion(batch);
    }

    protected void drawWaitRegion(Batch batch) {
        if (waitRegionData.getTextrueRegion() != null) {
            Color oldColor = batch.getColor();
            batch.setColor(waitRegionData.getColor());
            batch.draw(
                    waitRegionData.getTextrueRegion(),
                    waitRegionData.getTextureRegionX(), waitRegionData.getTextureRegionY(),
                    waitRegionData.getTextureRegionWidth(), waitRegionData.getTextureRegionHeight()
            );
            batch.setColor(oldColor);
        }
    }

    protected void drawDisplayFont(Batch batch) {
        if (bitmapFont != null) {
            if (isWaitRegionAnimation) {
                if (repeatAction == null) {
                    repeatAction = getWaitRegionAction();
                    addAction(repeatAction);
                }
            } else {
                if (repeatAction != null) {
                    removeAction(repeatAction);
                    repeatAction = null;
                }
            }
            bitmapFont.draw(batch, displayText, getX(), getY());
        }
    }
}

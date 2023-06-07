package com.mygdx.game.Logic.Function.Label;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * 2   |2 width|
 * < 2 xadvance >
 * 23  |2 offset|   ||3 offset|3 width|  getPrefer = (2 xadvance) + (3 offset) + (3 width) - (2 offset);
 * < 2 xadvance >< 3 xadvance >
 * 234 |2 offset|   ||3 offset|   ||4 offset|4 width| getPrefer = (2 xadvance) + (3 xadvance) + (4 offset) + (4 width) - (2 offset);
 * <p>
 * 新增lineHeightdebug, 默认是红色
 */

public class DebugLabel extends Label {
    public boolean debugLineHeight = true;
    public boolean debugChar = true;

    public Color debugLineHeightColor = Color.RED;
    public final Color debugCharColor = new Color(0, 0, 1, 0.2f);
    public String adjustString;

    public DebugLabel(CharSequence text, LabelStyle style) {
        super(text, style);


    }

    @Override
    public void setText(CharSequence newText) {
        super.setText(newText);
        adjustString = newText.toString();
    }

    public void updata() {
        if (getText().length > 0) {
            invalidate();
//            setSize(getPrefWidth(), getPrefHeight());
        }
        layout();
    }

    public void debugLineHeight(ShapeRenderer shapes) {
        if (!debugLineHeight) return;
        shapes.set(ShapeRenderer.ShapeType.Line);
        shapes.setColor(debugLineHeightColor);
        shapes.rect(getX(), getY(), getOriginX(), getOriginX(), getWidth(), getStyle().font.getLineHeight(), getScaleX(), getScaleY(), getRotation());
    }

    public void debugChar(ShapeRenderer shapes) {
        if (getText() == null || getText().length < 1) {
            return;
        }
        shapes.set(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(debugCharColor);
    }

    @Override
    public void drawDebug(ShapeRenderer shapes) {
        super.drawDebug(shapes);
        debugLineHeight(shapes);
        debugChar(shapes);
    }

    public DebugBitmapFontCache getCash() {
        return (DebugBitmapFontCache) getBitmapFontCache();
    }
}

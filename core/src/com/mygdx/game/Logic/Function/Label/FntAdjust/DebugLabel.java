package com.mygdx.game.Logic.Function.Label.FntAdjust;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * 新增lineHeightdebug, 默认是红色
 */

public class DebugLabel extends Label {
    public boolean debugLineHeight = true;
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

        for (int i = 0; i < getText().length; ++i) {
            int id = getText().charAt(i);
            if (((DebugBitmapFont) (getStyle().font)).verticesMap != null) {
                float[] vertices = ((DebugBitmapFont) (getStyle().font)).verticesMap.get(id);
                if (vertices != null) {
                    shapes.rect(vertices[0] + getBitmapFontCache().getX(), vertices[1] + getBitmapFontCache().getY(), vertices[2], vertices[3]);
                }
            }
        }

//        for (int i = 0; i < getText().length; ++i) {
//            int id = getText().charAt(i);
//            BitmapFont.Glyph g = getStyle().font.getData().getGlyph((char) id);
//            if (g != null && g.width > 0 && g.height > 0) {
//                int page = g.page;
//                int idX = getBitmapFontCache().getVertexCount(page);
//                if (idX > 0) {
//                    float[] vertices = getBitmapFontCache().getVertices(page);
//                    shapes.rect(vertices[0], vertices[1], vertices[10] - vertices[0], vertices[6] - vertices[1]);
//                }
//                //注意:shape 的 vertices 只有 xy，没有 color 和 uv
////                float[] vertices = new float[20];
////                vertices[0] = 250;
////                vertices[1] = 286;
////
////                vertices[2] = 250;
////                vertices[3] = 326;
////
////                vertices[4] = 286;
////                vertices[5] = 326;
////
////                vertices[6] = 286;
////                vertices[7] = 286;
////                shapes.rect(250, 286, 36, 40);
//            }
//        }
    }

    @Override
    public void drawDebug(ShapeRenderer shapes) {
        super.drawDebug(shapes);
        debugLineHeight(shapes);
        debugChar(shapes);
    }
}

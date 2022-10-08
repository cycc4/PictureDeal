package com.mygdx.game.Draw.Graphs;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class DrawLine {
    public DrawLine(int startX, int startY, int endX, int endY) {
        int width = Math.abs(endX - startX);
        int height = Math.abs(endY - startY);
        if (width <= 0 || height <= 0) {
            throw new GdxRuntimeException("pixmap width or height less than or equal to Zero");
        }
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.drawLine(startX, startY, endX, endY);
    }

    public static class DrawLineStyle{

    }
}

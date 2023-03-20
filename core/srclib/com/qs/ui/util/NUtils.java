package com.qs.ui.util;

import com.badlogic.gdx.graphics.Color;

import net.mwplay.cocostudio.ui.model.CColor;

public class NUtils {
    public static Color getColor(CColor c, int alpha) {
        Color color = null;
        if (c == null) {
            color = new Color(Color.WHITE);
        } else {
            color = new Color();
            color.a = 1;
            color.r = c.R / 255f;
            color.g = c.G / 255f;
            color.b = c.B / 255f;
        }

        if (alpha != 0) {
            color.a = alpha / 255f;
        }

        return color;
    }

}

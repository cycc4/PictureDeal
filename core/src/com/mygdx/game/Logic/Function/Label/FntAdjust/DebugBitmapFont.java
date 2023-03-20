package com.mygdx.game.Logic.Function.Label.FntAdjust;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;

import java.util.HashMap;

/**
 * 新增修改region的uv坐标函数
 */
public class DebugBitmapFont extends BitmapFont {
    public HashMap<Integer, float[]> verticesMap = new HashMap<>();

    public DebugBitmapFont(BitmapFontData data, TextureRegion region, boolean integer) {
        super(data, region != null ? Array.with(region) : null, integer);
    }

    @Override
    public BitmapFontCache newFontCache() {
        return new BitmapFontCache(this, usesIntegerPositions()) {
            @Override
            public void addText(GlyphLayout layout, float x, float y) {
                super.addText(layout, x, y);
                addVerticesMap(layout, x, y + getFont().getData().ascent);
            }

            public void addVerticesMap(GlyphLayout layout, float x, float y) {
                for (int i = 0, n = layout.runs.size; i < n; i++) {
                    GlyphLayout.GlyphRun run = layout.runs.get(i);
                    Array<Glyph> glyphs = run.glyphs;
                    FloatArray xAdvances = run.xAdvances;
                    float gx = x + run.x, gy = y + run.y;

                    for (int ii = 0, nn = glyphs.size; ii < nn; ii++) {
                        Glyph glyph = glyphs.get(ii);
                        gx += xAdvances.get(ii);
                        initVerticesMap(glyph, gx, gy);
                    }
                }
            }

            public void initVerticesMap(Glyph glyph, float x, float y) {
                float scaleX = getFont().getData().scaleX, scaleY = getFont().getData().scaleY;

                x += glyph.xoffset * scaleX;
                y += glyph.yoffset * scaleY;
                float width = glyph.width * scaleX, height = glyph.height * scaleY;

                if (usesIntegerPositions()) {
                    x = Math.round(x);
                    y = Math.round(y);
                    width = Math.round(width);
                    height = Math.round(height);
                }

//                float x2 = x + width, y2 = y + height;
                verticesMap.put(glyph.id, new float[]{
                        x, y, width, height
                });
                //      0  1  2   3   4
            }
        };
    }

    //用来调整图片的uv
    public void loadFontRegionChar(Glyph glyph) {
        if (glyph != null) {
            getData().setGlyphRegion(glyph, getRegions().get(glyph.page));
        }
    }
}

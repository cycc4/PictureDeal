package com.mygdx.game.Logic.Function.Label;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

/**
 * 新增修改region的uv坐标函数
 */
public class DebugBitmapFont extends BitmapFont {

    public DebugBitmapFont(BitmapFontData data, TextureRegion region, boolean integer) {
        super(data, region != null ? Array.with(region) : null, integer);
    }

    @Override
    public BitmapFontCache newFontCache() {
        return new DebugBitmapFontCache(this, usesIntegerPositions());
    }

    //用来调整图片的uv
    public void loadFontRegionChar(Glyph glyph) {
        if (glyph != null) {
            getData().setGlyphRegion(glyph, getRegions().get(glyph.page));
        }
    }
}

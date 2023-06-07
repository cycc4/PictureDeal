package com.mygdx.game.Logic.Function.Label;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.Res;
import com.qs.utils.assets.Assets;

public class DebugBitmapFontCache extends BitmapFontCache {
    private TextureRegion region = new TextureRegion(Assets.getIns().getTexture(Res.PICTURE_DIR + "pot.png"));
    private Pool<DebugChar> adjustActorPool = new Pool<DebugChar>() {
        @Override
        protected DebugChar newObject() {
            DebugChar debug = new DebugChar();
            debug.id = 0;
            debug.x = 0;
            debug.y = 0;
            debug.width = 0;
            debug.height = 0;
            return debug;
        }
    };
    public Array<DebugChar> textDebugArray = new Array<>();
    public ArrayMap<Integer, DebugChar> textDebugArrayMap = new ArrayMap<>();

    public DebugBitmapFontCache(BitmapFont font, boolean integer) {
        super(font, integer);
    }

    @Override
    public void addText(GlyphLayout layout, float x, float y) {
        super.addText(layout, x, y);

        free();
        if (layout.runs.size > 0) {
            GlyphLayout.GlyphRun run = layout.runs.first();
            FloatArray xAdvances = run.xAdvances;
            int i = 0;
            float a = 0;
            for (BitmapFont.Glyph g : run.glyphs) {
                if (g != null) {
                    DebugChar debug = getAdjustActor();
                    debug.id = g.id;
                    debug.width = g.width;
                    debug.height = g.height;
                    a += xAdvances.get(i);
                    debug.x = a;
                    debug.y = (y + getFont().getData().ascent + run.y + g.yoffset);
                    textDebugArray.add(debug);
                    textDebugArrayMap.put(g.id, debug);
                    ++i;
                }
            }
        }
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);

    }

    @Override
    public void translate(float xAmount, float yAmount) {
        super.translate(xAmount, yAmount);
        for (DebugChar debug : textDebugArray) {
            debug.x += xAmount;
            debug.y += yAmount;
        }
    }

    public DebugChar getAdjustActor() {
        return adjustActorPool.obtain();
    }

    public void free() {
        for (DebugChar t : textDebugArray) {
            free(t);
        }
        textDebugArrayMap.clear();
        textDebugArray.clear();
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        Color oldColor = batch.getColor();
        batch.setColor(1, 0, 0, 0.3f);
        for (DebugChar debug : textDebugArray) {
            if(debug.isDraw) {
                batch.draw(region, debug.x, debug.y, debug.width, debug.height);
            }
        }
        batch.setColor(oldColor);
    }

    public void free(DebugChar actor) {
        adjustActorPool.free(actor);
    }

    public void reset(){
        for(DebugChar debug:textDebugArray){
            debug.isDraw = false;
        }
    }

    public class DebugChar {
        public float x, y, width, height;
        public int id;
        public boolean isDraw;
    }
}

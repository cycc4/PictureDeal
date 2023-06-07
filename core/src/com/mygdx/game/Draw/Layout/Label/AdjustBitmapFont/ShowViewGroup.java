package com.mygdx.game.Draw.Layout.Label.AdjustBitmapFont;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Logic.Function.Label.DebugBitmapFontCache;
import com.mygdx.game.Logic.Function.Label.DebugLabel;

public class ShowViewGroup extends Group {
    private FntAdjustLayout fntAdjustLayout;
    public DebugLabel debugLabel;
    public final String showLabelString = "0123456789MTGB";
    private DebugBitmapFontCache cache;

    public ShowViewGroup(FntAdjustLayout fntAdjustLayout) {
        this.fntAdjustLayout = fntAdjustLayout;
    }

    public void init() {
        final String defaultS = fntAdjustLayout.getDefalutString(showLabelString);
        debugLabel = new DebugLabel(
                defaultS,
                new Label.LabelStyle(fntAdjustLayout.adjustLabel.getBitMapFont(), Color.WHITE)) {
            Array<DebugBitmapFontCache.DebugChar> debugArray = ((DebugBitmapFontCache) getBitmapFontCache()).textDebugArray;
        };
        addActor(debugLabel);
        debugLabel.debug();
        cache = debugLabel.getCash();

        debugLabel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
//                GlyphLayout layout = getGlyphLayout();
//                FloatArray floatArray = layout.runs.get(0).xAdvances;
//                int index = -1;
//                for(int i=1; i<floatArray.size; ++i){
//                    if(floatArray.get(i-1)<=x && floatArray.get(i)> x){
//                        index = i;
//                        break;
//                    }
//                }
                x += debugLabel.getX();

                for(DebugBitmapFontCache.DebugChar debug:cache.textDebugArray){
                    if(x >= debug.x && x < debug.width+debug.x){
                        debug.isDraw = true;
                        char c = (char)debug.id;
                        fntAdjustLayout.barViewGroup.showSelectedText(c+"");
                    }else {
                        debug.isDraw = false;
                    }
                }

            }
        });

        debugLabel.moveBy(100, 100);
        fntAdjustLayout.showText(defaultS);
    }

    public void showSelectedText(String string) {
        if(string == null) return;
        char[] cs = string.toCharArray();
        cache.reset();
        for(char c:cs){
            int id = (int)c;
            DebugBitmapFontCache.DebugChar debug = cache.textDebugArrayMap.get(id);
            if(debug != null){
                debug.isDraw = true;
            }
        }
    }


    public void showText(String string) {
        debugLabel.setText(string);
//        debugLabel.setPosition(debugLabel.getPrefWidth(), debugLabel.getPrefHeight());
    }
}

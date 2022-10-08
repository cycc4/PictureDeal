package com.mygdx.game.Draw.Button;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
/*
 * 该工具类特有
 */

public class TextFieldExtend extends TextField {
    public TextFieldExtend(String text, Skin skin) {
        super(text, skin);
    }

    public TextFieldExtend(String text, Skin skin, String styleName) {
        super(text, skin, styleName);
    }

    public TextFieldExtend(String text, TextFieldStyle style) {
        super(text, style);
    }

//    public TextFieldExtend(BitmapFont font, String text) {
////        super(text, new TextFieldStyle(font, Color.WHITE, ))
//    }
}

//package com.mygdx.game.Draw.Layout.Label.Struct;
//
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.scenes.scene2d.Group;
//import com.badlogic.gdx.scenes.scene2d.InputEvent;
//import com.badlogic.gdx.scenes.scene2d.ui.Label;
//import com.badlogic.gdx.utils.Array;
//import com.badlogic.gdx.utils.ArrayMap;
//import com.mygdx.game.Draw.Button.TextButton;
//import com.mygdx.game.Draw.Layout.ScrollPanlLayer;
//import com.mygdx.game.Logic.Function.Label.FntAdjust.AdjustLabel;
//import com.mygdx.game.MyGdxGame;
//import com.mygdx.game.Res;
//import com.qs.utils.actor.ActorFactory;
//import com.qs.utils.assets.Assets;
//
//public class BitmapFontName1 {
//    public String bitmapfontName;
//    public ScrollPanlLayer charLayer;
//    public AdjustLabel adjustLabel;
//
//    public ArrayMap<Integer, Group> charsDataMap = new ArrayMap<>();
//    public TextButton textButton;
//
//    public BitmapFontName1(MyGdxGame myGdxGame) {
//        bitmapfontName = fileHandle.name();
//        charLayer = new ScrollPanlLayer((int) width, 460, false);
//
//        adjustLabel = new AdjustLabel();
//        adjustLabel.read(fileHandle);
//
//        Array<Integer> charArray = adjustLabel.getBitmapFontData().bitMapCharArray;
//        for (int i = 0; i < charArray.size; ++i) {
//            Group charDataGroup = ActorFactory.createCsdJson(Assets.getIns().getAssetManager(), Res.FNT_CHAR_DATA);
//            int charId = charArray.get(i);
//            charsDataMap.put(charId, charDataGroup);
//            adjustChar(charId, true);
//        }
//
//        TextButton.TextStyle style = new TextButton.TextStyle();
//        style.upBitmapFont = Assets.getIns().getBitmapFont(Res.DEFAULT_FNT_36);
//        textButton = new TextButton(style) {
//            @Override
//            public void click(InputEvent event, float x, float y, TextButton textButton) {
////                if (currentbitmapfontData != null) {
////                    currentbitmapfontData.adjustCharData.charLayer.remove();
////                }
////                currentbitmapfontData = adjustLabelArray.get(bitmapfontName);
////                addActor(currentbitmapfontData.adjustCharData.charLayer);
//            }
//        };
//        addActor(textButton);
//    }
//
//    public String getChars() {
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0, size = adjustLabel.getBitmapFontData().bitMapCharArray.size; i < size; ++i) {
//            sb.append(adjustLabel.getBitmapFontData().bitMapCharArray.get(i));
//        }
//        return sb.toString();
//    }
//
//    public void adjustChar(int c, boolean isInit) {
//        BitmapFont.Glyph glyph = adjustLabel.getGlyPh((char) c);
//        if (glyph == null) {
//            return;
//        }
//        Group charDataGroup = charsDataMap.get(c);
//        if (charDataGroup == null) {
//            return;
//        }
//
//        Label offsetLabel;
//        if (isInit) {
//            offsetLabel = charDataGroup.findActor("title");
//            offsetLabel.setText(c);
//        }
//
//        Group dataGroup = charDataGroup.findActor("xoffset");
//        offsetLabel = dataGroup.findActor("data");
//        offsetLabel.setText(glyph.xoffset);
//        if (isInit) {
//            offsetLabel = dataGroup.findActor("text");
//            offsetLabel.setText("xoffset:");
//        }
//
//        dataGroup = charDataGroup.findActor("yoffset");
//        offsetLabel = dataGroup.findActor("data");
//        offsetLabel.setText(glyph.yoffset);
//        if (isInit) {
//            offsetLabel = dataGroup.findActor("text");
//            offsetLabel.setText("yoffset:");
//        }
//
//        dataGroup = charDataGroup.findActor("width");
//        offsetLabel = dataGroup.findActor("data");
//        offsetLabel.setText(glyph.width);
//        if (isInit) {
//            offsetLabel = dataGroup.findActor("text");
//            offsetLabel.setText("width:");
//        }
//
//        dataGroup = charDataGroup.findActor("height");
//        offsetLabel = dataGroup.findActor("data");
//        offsetLabel.setText(glyph.height);
//        if (isInit) {
//            offsetLabel = dataGroup.findActor("text");
//            offsetLabel.setText("height:");
//        }
//
//        dataGroup = charDataGroup.findActor("x");
//        offsetLabel = dataGroup.findActor("data");
//        offsetLabel.setText(glyph.srcX);
//        if (isInit) {
//            offsetLabel = dataGroup.findActor("text");
//            offsetLabel.setText("x:");
//        }
//
//        dataGroup = charDataGroup.findActor("y");
//        offsetLabel = dataGroup.findActor("data");
//        offsetLabel.setText(glyph.srcY);
//        if (isInit) {
//            offsetLabel = dataGroup.findActor("text");
//            offsetLabel.setText("y:");
//        }
//
//        dataGroup = charDataGroup.findActor("xadvance");
//        offsetLabel = dataGroup.findActor("data");
//        offsetLabel.setText(glyph.xadvance);
//        if (isInit) {
//            offsetLabel = dataGroup.findActor("text");
//            offsetLabel.setText("xadvance:");
//        }
//    }
//
//    @Override
//    public void setSize(float width, float height) {
//
//    }
//}

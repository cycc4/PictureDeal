package com.mygdx.game.Draw.Button;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Res;
import com.qs.utils.assets.Assets;
/*
 * cocos 对 textField支持与libgdx不好，这里写死，并不是读取cocos中的
 */

public class TextFieldButton {
    protected TextField textField;

    public TextFieldButton(Group textFieldGroup, String hintText) {
        Texture potTexture = Assets.getIns().getTexture(Res.PICTURE_DIR + "pot.png");
        TextureRegionDrawable bgDrawable = new TextureRegionDrawable(potTexture);
        bgDrawable.setMinWidth(100);
        bgDrawable.setMinHeight(30);

        TextureRegionDrawable cursorDrawabel = new TextureRegionDrawable(potTexture);
        cursorDrawabel.setMinHeight(textFieldGroup.getHeight());

        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
//        style.background = bgDrawable;
        style.cursor = cursorDrawabel;
        style.font = Assets.getIns().getBitmapFont(Res.DEFAULT_FNT_30);
        style.font.getData().setScale(0.5f);
        style.fontColor = Color.WHITE;

        textField = new TextField(hintText, style);
        textField.setAlignment(Align.left);
        textField.debug();

        textField.setSize(textFieldGroup.getWidth(), textFieldGroup.getHeight());
        textFieldGroup.addActor(textField);

//        getTextField().addListener(new ClickListener(){
//            @Override
//            public boolean keyDown(InputEvent event, int keycode) {
//                if(keycode == Input.Keys.ENTER){
//                    adjustString = adjustChar.getText();
//                    return true;
//                }
//                return false;
//            }
//        });
    }

    public String getText() {
        return textField.getText();
    }

    public TextField getTextField() {
        return textField;
    }
}

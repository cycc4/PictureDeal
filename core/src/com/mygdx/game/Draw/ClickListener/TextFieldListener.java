package com.mygdx.game.Draw.ClickListener;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
import com.badlogic.gdx.utils.FloatArray;

public class TextFieldListener extends InputListener {

    public boolean keyDown(InputEvent event, int keycode) {
        boolean handled = super.keyDown(event, keycode);
        boolean ctrl = UIUtils.ctrl();
        boolean shift = UIUtils.shift();
        if (ctrl) {
            switch (keycode) {
                case Input.Keys.V:
                    paste();
                    break;
                case Input.Keys.C:
                case Input.Keys.INSERT:
                    copy();
                    return true;
                case Input.Keys.X:
                    cut();
                    return true;
                case Input.Keys.A:
                    selectAll();
                    return true;
                case Input.Keys.Z:
                    callBack();
                    return true;
            }
        }

        if (shift) {
            switch (keycode) {
                case Input.Keys.INSERT:
                    paste();
                    break;
                case Input.Keys.FORWARD_DEL:
                    cut();
                    break;
            }
        }

        switch (keycode) {
            case Input.Keys.LEFT:
                moveCursorLeft(ctrl, shift);
                break;
            case Input.Keys.RIGHT:
                moveCursorRight(ctrl, shift);
                break;
            case Input.Keys.HOME:
                goHome(ctrl, shift);
                break;
            case Input.Keys.END:
                goEnd(ctrl, shift);
                break;
        }

        return handled;
    }

    public void copy() {
    }

    public void cut() {
    }

    public void paste() {
    }

    public void selectAll() {
    }

    public void callBack() {
    }

    public void moveCursorLeft(boolean ctrl, boolean shift) {

    }

    public void moveCursorRight(boolean ctrl, boolean shift) {

    }

    public void goHome(boolean ctrl, boolean shift) {

    }

    public void goEnd(boolean ctrl, boolean shift) {

    }
}

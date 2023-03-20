package com.qs.ui.CheckBox;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;

public class CheckBox extends Button {
    private Image image;
    private Cell imageCell;
    private CheckBox.CheckBoxStyle style;

    public CheckBox(Skin skin) {
        this((CheckBox.CheckBoxStyle) skin.get(CheckBox.CheckBoxStyle.class));
    }

    public CheckBox(Skin skin, String styleName) {
        this((CheckBox.CheckBoxStyle) skin.get(styleName, CheckBox.CheckBoxStyle.class));
    }

    public CheckBox(CheckBoxStyle style) {
        super(style);
        this.clearChildren();
        this.imageCell = this.add(this.image = new Image(style.checkboxOff, Scaling.none));
        this.setSize(this.getPrefWidth(), this.getPrefHeight());
    }

    public void setStyle(ButtonStyle style) {
        if (!(style instanceof com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle)) {
            throw new IllegalArgumentException("style must be a CheckBoxStyle.");
        } else {
            this.style = (CheckBox.CheckBoxStyle) style;
            super.setStyle(style);
        }
    }

    public CheckBox.CheckBoxStyle getStyle() {
        return this.style;
    }

    public void draw(Batch batch, float parentAlpha) {
        Drawable checkbox = null;
        if (this.isDisabled()) {
            if (isChecked() && this.style.checkboxOnDisabled != null) {
                checkbox = this.style.checkboxOnDisabled;
            } else {
                checkbox = this.style.checkboxOffDisabled;
            }
        }

        if (checkbox == null) {
            boolean over = this.isOver() && !this.isDisabled();
            if (isChecked() && this.style.checkboxOn != null) {
                checkbox = over && this.style.checkboxOnOver != null ? this.style.checkboxOnOver : this.style.checkboxOn;
            } else if (over && this.style.checkboxOver != null) {
                checkbox = this.style.checkboxOver;
            } else {
                checkbox = this.style.checkboxOff;
            }
        }

        this.image.setDrawable(checkbox);
        super.draw(batch, parentAlpha);
    }

    public Image getImage() {
        return this.image;
    }

    public Cell getImageCell() {
        return this.imageCell;
    }

    public static class CheckBoxStyle extends ButtonStyle {
        public Drawable checkboxOn;
        public Drawable checkboxOff;
        public Drawable checkboxOnOver;
        public Drawable checkboxOver;
        public Drawable checkboxOnDisabled;
        public Drawable checkboxOffDisabled;

        public CheckBoxStyle() {
        }

        public CheckBoxStyle(Drawable checkboxOff, Drawable checkboxOn) {
            this.checkboxOff = checkboxOff;
            this.checkboxOn = checkboxOn;
        }

        public CheckBoxStyle(CheckBoxStyle style) {
            super(style);
            this.checkboxOff = style.checkboxOff;
            this.checkboxOn = style.checkboxOn;
            this.checkboxOnOver = style.checkboxOnOver;
            this.checkboxOver = style.checkboxOver;
            this.checkboxOnDisabled = style.checkboxOnDisabled;
            this.checkboxOffDisabled = style.checkboxOffDisabled;
        }
    }
}

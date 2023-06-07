package com.mygdx.game.Draw.Button;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class TextButton extends Group {
    private String textString;
    private boolean isClick = false;

    private Label downLabel;
    private Label upLabel;
    private Label disableLabel;

    private TextStyle style;

    public TextButton(final TextStyle style) {
        this.style = style;

        if (style.disableBitmapFont != null) {
            disableLabel = getLable(style.disableBitmapFont);
        }

        if (style.upBitmapFont != null) {
            upLabel = getLable(style.upBitmapFont);
        }

        if (style.downBitmapFont != null) {
            downLabel = getLable(style.downBitmapFont);
        }

        addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (super.touchDown(event, x, y, pointer, button)) {
                    isClick = true;
                    changeLabel();
                    return true;
                }
                return false;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                isClick = false;
                changeLabel();
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                click(event, x, y, TextButton.this);
            }
        });
    }

    @Override
    protected void sizeChanged() {
        if (textString != null) {
            setText(textString);
        }
    }

    public void setText(String text) {
        textString = text;

        float centerW = getWidth() / 2f;
        float centerH = getHeight() / 2f;

        if (upLabel != null) {
            upLabel.setPosition(centerW, centerH, Align.center);
            setText(upLabel, text);
        }

        if (downLabel != null) {
            downLabel.setPosition(centerW, centerH, Align.center);
            setText(downLabel, text);
        }

        if (disableLabel != null) {
            disableLabel.setPosition(centerW, centerH, Align.center);
            setText(disableLabel, text);
        }
    }

    private void changeLabel() {
        boolean label = false;
        if (!isTouchable()) {
            if (disableLabel != null) {
                if (upLabel != null) {
                    upLabel.remove();
                }
                if (downLabel != null) {
                    downLabel.remove();
                }
                addActor(disableLabel);
                label = true;
            }
        } else {
            if (isClick) {
                if (downLabel != null) {
                    if (disableLabel != null) {
                        disableLabel.remove();
                    }
                    if (upLabel != null) {
                        upLabel.remove();
                    }
                    addActor(downLabel);
                    label = true;
                }
            }
        }

        if (!label && upLabel != null) {
            if (downLabel != null) {
                downLabel.remove();
            }
            if (disableLabel != null) {
                disableLabel.remove();
            }
            addActor(upLabel);
        }
    }

    @Override
    protected void drawChildren(Batch batch, float parentAlpha) {
        boolean region = false;
        if (!isTouchable()) {
            if (style.disableRegion != null) {
                if (style.isTextButtonSize) {
                    batch.draw(style.disableRegion, 0, 0, getWidth(), getHeight());
                } else {
                    batch.draw(style.disableRegion, 0, 0, style.disableRegion.getRegionWidth(), style.disableRegion.getRegionHeight());
                }
                region = true;
            }
        } else {
            if (isClick) {
                if (style.downRegion != null) {
                    if (style.isTextButtonSize) {
                        batch.draw(style.downRegion, 0, 0, getWidth(), getHeight());
                    } else {
                        batch.draw(style.downRegion, 0, 0, style.downRegion.getRegionWidth(), style.downRegion.getRegionHeight());
                    }
                    region = true;
                }
            }
        }

        if (!region && style.upRegion != null) {
            if (style.isTextButtonSize) {
                batch.draw(style.upRegion, 0, 0, getWidth(), getHeight());
            } else {
                batch.draw(style.upRegion, 0, 0, style.upRegion.getRegionWidth(), style.upRegion.getRegionHeight());
            }
        }
        super.drawChildren(batch, parentAlpha);
    }

    private Label getLable(BitmapFont bitmapFont) {
        Label label = new Label("text", new Label.LabelStyle(bitmapFont, Color.WHITE));
        label.setAlignment(Align.center);
        label.setPosition(getWidth() / 2f, getHeight() / 2f, Align.center);
        addActor(label);
        return label;
    }

    private void setText(Label label, String textString) {
        label.setAlignment(Align.center);
        label.setText(textString);
        label.setFontScale(1);
        label.setScale(1);
        label.layout();
        System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz: " + label.getPrefHeight() + "   " + getHeight() + "   " + label.getPrefWidth() + "   " + getWidth());
        if (label.getPrefHeight() > getHeight() || label.getPrefWidth() > getWidth()) {
            float scale = Math.min(getHeight() / label.getPrefHeight(), getWidth() / label.getPrefWidth());
            label.setScale(scale);
            label.setFontScale(scale);
        }
    }

    public void click(InputEvent event, float x, float y, TextButton textButton) {

    }

    public static class TextStyle {
        public TextureRegion upRegion;
        public TextureRegion downRegion;
        public TextureRegion disableRegion;
        public BitmapFont upBitmapFont;
        public BitmapFont downBitmapFont;
        public BitmapFont disableBitmapFont;
        public boolean isTextButtonSize = true;
    }
}

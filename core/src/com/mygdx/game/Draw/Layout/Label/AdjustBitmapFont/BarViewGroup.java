package com.mygdx.game.Draw.Layout.Label.AdjustBitmapFont;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ArrayMap;
import com.mygdx.game.Constants;
import com.mygdx.game.Draw.Button.AddAndSubButton;
import com.mygdx.game.Draw.Button.TextButton;
import com.mygdx.game.Draw.Button.TextFieldButton;
import com.mygdx.game.Draw.View.ScrollView;
import com.mygdx.game.Logic.Function.Label.DebugBitmapFontData;
import com.mygdx.game.Logic.Function.Label.FntAdjust.AdjustLabel;
import com.mygdx.game.Res;
import com.qs.utils.actor.ActorFactory;
import com.qs.utils.assets.Assets;

import net.mwplay.cocostudio.ui.widget.TCheckBox;

public class BarViewGroup extends Group {
    private FntAdjustLayout fntAdjustLayout;
    private DebugBitmapFontData data;
    public Group rootGroup;

    private TCheckBox chickAll;
    private AddAndSubButton lineHeightActor;
    private AddAndSubButton baseActor;
    private AddAndSubButton stepActor;
    private Label debugChar;
    private AddAndSubButton xActor;
    private AddAndSubButton yActor;
    private AddAndSubButton widthActor;
    private AddAndSubButton heightActor;
    private AddAndSubButton xOffsetActor;
    private AddAndSubButton yOffsetActor;
    private AddAndSubButton xAdvanceActor;
    private TextFieldButton showCharActor;
    private TextFieldButton adjustCharActor;
    //    public ScrollView adjustChars;
//    public ArrayMap<Character, CharGroup> charsDataMap = new ArrayMap<>();
    public TextButton saveButton;

    private int step = 1;
    private boolean isChickAll = true;
    private String adjustText = null;

    public BarViewGroup(FntAdjustLayout fntAdjustLayout) {
        this.fntAdjustLayout = fntAdjustLayout;
    }

    public void init() {
        data = fntAdjustLayout.adjustLabel.getBitmapFontData();

        rootGroup = ActorFactory.createCsdJson(Assets.getIns().getAssetManager(), Res.LAYOUT_DIR + "Fnt/fntSideBar.json");
        addActor(rootGroup);
        setSize(rootGroup.getWidth(), rootGroup.getHeight());
        getLabel("title", fntAdjustLayout.name);
        rootGroup.setPosition(Constants.WORLD_WIDTH - 400, 0);
        fntAdjustLayout.addActor(rootGroup);

        lineHeightActor = new AddAndSubButton(rootGroup.<Group>findActor("lineHeight")) {
            @Override
            public void buttonClick(InputEvent event, boolean isAdd) {
                AdjustLabel adjustLabel = fntAdjustLayout.adjustLabel;
                if (isAdd) {
                    adjustLabel.setLineHeight(adjustLabel.getBitMapFont(), step);
                } else {
                    adjustLabel.setLineHeight(adjustLabel.getBitMapFont(), -step);
                }
                setText(data.lineHeight + "");
            }
        };
        lineHeightActor.setText(data.lineHeight + "");

        baseActor = new AddAndSubButton(rootGroup.<Group>findActor("base")) {
            @Override
            public void buttonClick(InputEvent event, boolean isAdd) {
                AdjustLabel adjustLabel = fntAdjustLayout.adjustLabel;
                if (isAdd) {
                    adjustLabel.setBaseLine(step);
                } else {
                    adjustLabel.setBaseLine(-step);
                }
                setText(data.baseLine + "");
            }
        };
        baseActor.setText(data.baseLine + "");

        stepActor = new AddAndSubButton(rootGroup.<Group>findActor("step")) {
            @Override
            public void buttonClick(InputEvent event, boolean isAdd) {
                if (isAdd) {
                    step++;
                } else {
                    step--;
                    if (step < 1) {
                        step = 1;
                    }
                }
                setText(step + "");
            }
        };
        stepActor.setText(step + "");

        debugChar = rootGroup.findActor("text");

        xActor = new AddAndSubButton(rootGroup.<Group>findActor("x")) {
            @Override
            public void buttonClick(InputEvent event, boolean isAdd) {
                AdjustLabel adjustLabel = fntAdjustLayout.adjustLabel;
                int x = isAdd ? step : -step;
                if (isChickAll) {
                    adjustLabel.setCharacterXAll(x);
                } else {
                    adjustLabel.setCharacterStringX(adjustText, x);
                }
                showAdJustChars(adjustText);
            }
        };

        yActor = new AddAndSubButton(rootGroup.<Group>findActor("y")) {
            @Override
            public void buttonClick(InputEvent event, boolean isAdd) {
                AdjustLabel adjustLabel = fntAdjustLayout.adjustLabel;
                int y = isAdd ? step : -step;
                if (isChickAll) {
                    adjustLabel.setCharacterYAll(y);
                } else {
                    adjustLabel.setCharacterStringY(adjustText, y);
                    showAdJustChars(adjustText);
                }
                showAdJustChars(adjustText);

            }
        };

        widthActor = new AddAndSubButton(rootGroup.<Group>findActor("width")) {
            @Override
            public void buttonClick(InputEvent event, boolean isAdd) {
                AdjustLabel adjustLabel = fntAdjustLayout.adjustLabel;
                int w = isAdd ? step : -step;
                if (isChickAll) {
                    adjustLabel.setCharacterWidthAll(w);
                } else {
                    adjustLabel.setCharacterStringWidth(adjustText, w);
                }
                showAdJustChars(adjustText);

            }
        };

        heightActor = new AddAndSubButton(rootGroup.<Group>findActor("height")) {
            @Override
            public void buttonClick(InputEvent event, boolean isAdd) {
                AdjustLabel adjustLabel = fntAdjustLayout.adjustLabel;
                int h = isAdd ? step : -step;
                if (isChickAll) {
                    adjustLabel.setCharacterHeightAll(h);
                } else {
                    adjustLabel.setCharacterStringHeight(adjustText, h);
                }
                showAdJustChars(adjustText);

            }
        };

        xOffsetActor = new AddAndSubButton(rootGroup.<Group>findActor("xoffset")) {
            @Override
            public void buttonClick(InputEvent event, boolean isAdd) {
                AdjustLabel adjustLabel = fntAdjustLayout.adjustLabel;
                int xo = isAdd ? step : -step;
                if (isChickAll) {
                    adjustLabel.setCharacterOffsetXAll(xo);
                } else {
                    adjustLabel.setCharacterStringOffsetX(adjustText, xo);
                }
                showAdJustChars(adjustText);

            }
        };

        yOffsetActor = new AddAndSubButton(rootGroup.<Group>findActor("yoffset")) {
            @Override
            public void buttonClick(InputEvent event, boolean isAdd) {
                AdjustLabel adjustLabel = fntAdjustLayout.adjustLabel;
                int yo = isAdd ? step : -step;
                if (isChickAll) {
                    adjustLabel.setCharacterOffsetYAll(yo);
                } else {
                    adjustLabel.setCharacterStringOffsetY(adjustText, yo);
                }
                showAdJustChars(adjustText);

            }
        };

        xAdvanceActor = new AddAndSubButton(rootGroup.<Group>findActor("xadvance")) {
            @Override
            public void buttonClick(InputEvent event, boolean isAdd) {
                AdjustLabel adjustLabel = fntAdjustLayout.adjustLabel;
                int yo = isAdd ? step : -step;
                if (isChickAll) {
                    adjustLabel.setXAdvanceAll(yo);
                } else {
                    adjustLabel.setStringXAdvance(adjustText, yo);
                }
                showAdJustChars(adjustText);

            }
        };

        showCharActor = new TextFieldButton(rootGroup.<Group>findActor("showText"), "0");
        showCharActor.getTextField().addListener(new ClickListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    fntAdjustLayout.showText(showCharActor.getText());
                    return true;
                }
                return false;
            }
        });

        adjustCharActor = new TextFieldButton(rootGroup.<Group>findActor("adjsutText"), "0");
        adjustCharActor.getTextField().addListener(new ClickListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    fntAdjustLayout.showSelectText(adjustCharActor.getText());
                    return true;
                }
                return false;
            }
        });

        chickAll = rootGroup.findActor("checkAll");
        chickAll.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isChickAll = !chickAll.isChecked();
                adjustCharActor.getTextField().setDisabled(isChickAll);
            }
        });
//        chickAll.setChecked(true);

        TextButton.TextStyle style = new TextButton.TextStyle();
        style.upRegion = new TextureRegion(Assets.getIns().getTexture(Res.PICTURE_DIR + "Button_Press.png"));
        style.upBitmapFont = Assets.getIns().getBitmapFont(Res.FONT_DIR + "30-export.fnt");
        saveButton = new TextButton(style);
        saveButton.setSize(100, 70);
        saveButton.setText("save");
        saveButton.setPosition(10, 300);
        rootGroup.addActor(saveButton);
        saveButton.debugAll();

        showAdJustChars(adjustText);
//        adjustChars = new ScrollView();
//        adjustChars.setSize(getWidth(), 460);
//        adjustChars.setPosition(0, 0);
//        Array<Integer> charArray = fntAdjustLayout.adjustLabel.getBitmapFontData().bitMapCharArray;
//        for (int i = 0; i < charArray.size; ++i) {
//            int charId = charArray.get(i);
//            char c = (char) charId;
//            CharGroup charDataGroup = new CharGroup(fntAdjustLayout.adjustLabel.getGlyPh(c));
//            charsDataMap.put(c, charDataGroup);
//            adjustChars.addActor(charDataGroup.getRootGroup());
//        }
    }


    private void showAdJustChars(String s) {
        if (isChickAll) {
            debugChar.setText("All");
            hideAdjustChar();
        } else if (s != null && s.length() == 1) {
            debugChar.setText(s.charAt(0));
            showAdjustChar(s.charAt(0));
        } else {
            if (s != null) {
                debugChar.setText(s);
            }
            hideAdjustChar();
        }
    }

    private void hideAdjustChar() {
        xActor.setTouchable(false);
        xActor.setText("-");

        yActor.setTouchable(false);
        yActor.setText("-");

        widthActor.setTouchable(false);
        widthActor.setText("-");

        heightActor.setTouchable(false);
        heightActor.setText("-");

        xOffsetActor.setTouchable(false);
        xOffsetActor.setText("-");

        yOffsetActor.setTouchable(false);
        yOffsetActor.setText("-");

        xAdvanceActor.setTouchable(false);
        xAdvanceActor.setText("-");
    }

    private void showAdjustChar(char c) {
        BitmapFont.Glyph glyph = fntAdjustLayout.adjustLabel.getGlyPh(c);

        xActor.setTouchable(true);
        xActor.setText(Integer.toString(glyph.srcX));

        yActor.setTouchable(true);
        yActor.setText(Integer.toString(glyph.srcY));

        widthActor.setTouchable(true);
        widthActor.setText(Integer.toString(glyph.width));

        heightActor.setTouchable(true);
        heightActor.setText(Integer.toString(glyph.height));

        xOffsetActor.setTouchable(true);
        xOffsetActor.setText(Integer.toString(glyph.xoffset));

        yOffsetActor.setTouchable(true);
        yOffsetActor.setText(Integer.toString(glyph.yoffset));

        xAdvanceActor.setTouchable(true);
        xAdvanceActor.setText(Integer.toString(glyph.xadvance));
    }

    private Label getLabel(String name, String text) {
        Label label = rootGroup.findActor(name);
        if (label != null) {
            label.setText(text);
        }
        return label;
    }

    public void showText(String string) {
        showCharActor.getTextField().setText(string);
    }

    public void showSelectedText(String string) {
        adjustCharActor.getTextField().setText(string);
        adjustText = string;
        showAdJustChars(string);
    }
}

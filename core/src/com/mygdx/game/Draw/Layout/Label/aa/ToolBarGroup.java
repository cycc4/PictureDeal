package com.mygdx.game.Draw.Layout.Label.aa;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.mygdx.game.Draw.Button.AddAndSubButton;
import com.mygdx.game.Draw.Button.TextButton;
import com.mygdx.game.Draw.Button.TextFieldButton;
import com.mygdx.game.Draw.View.ScrollView;
import com.mygdx.game.Logic.Function.Label.DebugBitmapFontData;
import com.mygdx.game.Logic.Function.Label.DebugLabel;
import com.mygdx.game.Logic.Function.Label.FntAdjust.AdjustLabel;
import com.mygdx.game.Res;
import com.qs.utils.actor.ActorFactory;
import com.qs.utils.assets.Assets;

import net.mwplay.cocostudio.ui.widget.TCheckBox;

public class ToolBarGroup extends Group {
    private FntAdjustLayout adjustLayout;
    private ScrollView fntNameScrollView;
    private ArrayMap<String, Group> fntNameMap = new ArrayMap<>(1);
    private TCheckBox chickAll;
    private AddAndSubButton lineHeightActor;
    private AddAndSubButton baseActor;
    private AddAndSubButton stepActor;
    private TextFieldButton showCharActor;
    private TextFieldButton adjustCharActor;

    private String showLabelString = "0123456789012MTGB";
    private String adjustText = showLabelString;
    private boolean isChickAll = true;
    private int step = 1;

    private BitmapFontName selectedFntName;

    public ToolBarGroup(FntAdjustLayout adjustLayout) {
        this.adjustLayout = adjustLayout;
        Group sideBar = ActorFactory.createCsdJson(Assets.getIns().getAssetManager(), Res.LAYOUT_DIR + "Fnt/fntSideBar.json");
        addActor(sideBar);

        setSize(sideBar.getWidth(), sideBar.getHeight());
        fntNameScrollView = new ScrollView();
        fntNameScrollView.setChickRegionActor(Res.PICTURE_DIR + "pot.png", getWidth(), 50, new Color(0, 0, 1, 0.3f));
        fntNameScrollView.setSize(getWidth(), 269);
        fntNameScrollView.setScrollSize(fntNameScrollView.getWidth(), fntNameScrollView.getHeight());
        fntNameScrollView.setPosition(0, 506);
        sideBar.addActor(fntNameScrollView);

        chickAll = sideBar.findActor("checkAll");
        chickAll.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isChickAll = !chickAll.isChecked();
                adjustCharActor.getTextField().setDisabled(isChickAll);
            }
        });
        chickAll.setChecked(true);
        isChickAll = !chickAll.isChecked();

        lineHeightActor = new AddAndSubButton(sideBar.<Group>findActor("lineHeight")) {
            @Override
            public void buttonClick(InputEvent event, boolean isAdd) {
                AdjustLabel adjustLabel = selectedFntName.adjustLabel;
                if (isAdd) {
                    adjustLabel.setLineHeight(adjustLabel.getBitMapFont(), step);
                } else {
                    adjustLabel.setLineHeight(adjustLabel.getBitMapFont(), -step);
                }
            }
        };

        baseActor = new AddAndSubButton(sideBar.<Group>findActor("base")) {
            @Override
            public void buttonClick(InputEvent event, boolean isAdd) {
                AdjustLabel adjustLabel = selectedFntName.adjustLabel;
                if (isAdd) {
                    adjustLabel.setBaseLine(step);
                } else {
                    adjustLabel.setBaseLine(-step);
                }
            }
        };

        stepActor = new AddAndSubButton(sideBar.<Group>findActor("step")) {
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
            }
        };
        stepActor.setText(step + "");

        showCharActor = new TextFieldButton(sideBar.<Group>findActor("showText"), "0");
        showCharActor.getTextField().addListener(new ClickListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (selectedFntName != null && keycode == Input.Keys.ENTER) {
                    if (showCharActor.getText() != null) {
                        selectedFntName.label.setSize(
                                selectedFntName.label.getPrefWidth(),
                                selectedFntName.label.getPrefHeight()
                        );
                    }
                    return true;
                }
                return false;
            }
        });

        adjustCharActor = new TextFieldButton(sideBar.<Group>findActor("adjsutText"), "0");
        adjustCharActor.getTextField().addListener(new ClickListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (selectedFntName != null && keycode == Input.Keys.ENTER) {
                    if (adjustCharActor.getText() != null) {
                        selectedFntName.label.setSize(
                                selectedFntName.label.getPrefWidth(),
                                selectedFntName.label.getPrefHeight()
                        );
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void readBitmapfont(FileHandle readFileHandle) {
        BitmapFontName bitmapFontName = new BitmapFontName(readFileHandle);
        fntNameMap.put(readFileHandle.name(), bitmapFontName.textButton);
        fntNameScrollView.addActor(bitmapFontName.textButton);
    }

    public void reset() {

    }

    public void setBitmapFontName(BitmapFontName bitmapFontName) {
        if (selectedFntName != null) {
            selectedFntName.adjustChars.remove();
        }
        selectedFntName = bitmapFontName;
        addActor(selectedFntName.adjustChars);

        DebugBitmapFontData debugBitmapFontData = bitmapFontName.adjustLabel.getBitmapFontData();
        lineHeightActor.setText(((int) debugBitmapFontData.lineHeight) + "");
        baseActor.setText(((int) debugBitmapFontData.baseLine) + "");

        adjustLayout.getBackageGroup().removeChilds();
        adjustLayout.getBackageGroup().addCenterActor(selectedFntName.label);
        reset();
    }

    public BitmapFontName getSelectBitmapFontName() {
        return selectedFntName;
    }

    public class BitmapFontName {
        public TextButton textButton;
        public String name;
        public Label label;
        public AdjustLabel adjustLabel;

        public ScrollView adjustChars;
        public ArrayMap<Character, Group> charsDataMap = new ArrayMap<>();

        public BitmapFontName(FileHandle fileHandle) {
            name = fileHandle.name();

            TextButton.TextStyle style = new TextButton.TextStyle();
            style.upBitmapFont = Assets.getIns().getBitmapFont(Res.DEFAULT_FNT_30);
            textButton = new TextButton(style) {
                @Override
                public void click(InputEvent event, float x, float y, TextButton textButton) {
                    setBitmapFontName(BitmapFontName.this);
                }
            };
            textButton.setSize(getWidth(), 50);
            textButton.setText(name);

            adjustLabel = new AdjustLabel();
            adjustLabel.read(fileHandle);

            adjustChars = new ScrollView();
            adjustChars.setSize(getWidth(), 460);
            adjustChars.setPosition(0, 0);
            Array<Integer> charArray = adjustLabel.getBitmapFontData().bitMapCharArray;
            for (int i = 0; i < charArray.size; ++i) {
                Group charDataGroup = ActorFactory.createCsdJson(Assets.getIns().getAssetManager(), Res.LAYOUT_DIR + "Fnt/charData.json");
                adjustChars.addActor(charDataGroup);
                int charId = charArray.get(i);
                char c = (char) charId;
                charsDataMap.put(c, charDataGroup);
                adjustChar(c);
            }

            label = new DebugLabel(getDefalutString(showLabelString, adjustLabel), new Label.LabelStyle(adjustLabel.getBitMapFont(), Color.WHITE));
        }

        public void adjustChar(char c) {
            BitmapFont.Glyph glyph = adjustLabel.getGlyPh(c);
            if (glyph == null) {
                return;
            }
            Group charDataGroup = charsDataMap.get(c);
            if (charDataGroup == null) {
                return;
            }

            Label offsetLabel = charDataGroup.findActor("title");
            offsetLabel.setText(c + "");

            offsetLabel = charDataGroup.findActor("xoffset");
            offsetLabel.setText(glyph.xoffset);

            offsetLabel = charDataGroup.findActor("yoffset");
            offsetLabel.setText(glyph.yoffset);

            offsetLabel = charDataGroup.findActor("width");
            offsetLabel.setText(glyph.width);

            offsetLabel = charDataGroup.findActor("height");
            offsetLabel.setText(glyph.height);

            offsetLabel = charDataGroup.findActor("x");
            offsetLabel.setText(glyph.srcX);

            offsetLabel = charDataGroup.findActor("y");
            offsetLabel.setText(glyph.srcY);

            offsetLabel = charDataGroup.findActor("xadvance");
            offsetLabel.setText(glyph.xadvance);
        }
    }


    public String getDefalutString(String string, AdjustLabel adjustLabel) {
        StringBuffer stringBuffer = new StringBuffer();
        int num = 0;
        for (char c : string.toCharArray()) {
            if (adjustLabel.getBitmapFontData().hasGlyph(c)) {
                stringBuffer.append(c);
                num++;
            }
        }
        if (num == 0) {
            return " ";
        } else {
            return stringBuffer.toString();
        }
    }
}

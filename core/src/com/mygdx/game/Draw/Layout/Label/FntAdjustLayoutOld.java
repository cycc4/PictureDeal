package com.mygdx.game.Draw.Layout.Label;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Draw.Layout.BaseLayout;
import com.mygdx.game.Logic.Function.Label.DebugLabel;
import com.mygdx.game.Draw.Button.TextButton;
import com.mygdx.game.Draw.Button.TextFieldButton;
import com.mygdx.game.Draw.ClickListener.DragClickListener;
import com.qs.utils.actor.ActorFactory;
import com.qs.utils.assets.Assets;
import com.mygdx.game.Draw.Button.CrossButton;
import com.mygdx.game.Draw.Button.FlatButton;
import com.mygdx.game.Logic.CallBack.RecursionReversalDir;
import com.mygdx.game.Logic.Function.Label.FntAdjust.AdjustLabel;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Res;

import net.mwplay.cocostudio.ui.widget.TCheckBox;

import java.io.File;

public class FntAdjustLayoutOld extends BaseLayout {
    protected Array<AdjustLabel> adjustLabelArray = new Array<>(1);
    private int currentAdjustLabelIndex;

    private DebugLabel showLabel;
    private CrossButton positionButton;
    private CrossButton sizeButton;
    private CrossButton offsetButton;
    private FlatButton xAdvanceButton;
    private FlatButton lineHeightButton;
    private FlatButton baseLineButton;
    private TextButton saveButton;
    private TextButton resetButton;
    private TextFieldButton adjustChar;
    private TextFieldButton showTableText;
    private TCheckBox chickAll;

    float centerShowLayerX;
    float centerShowLayerY;

    private boolean isChickAll = true;
    private final String defaultText = "0123456789MTB";

    public FntAdjustLayoutOld(MyGdxGame myGdxGame) {
        super(myGdxGame);

        currentAdjustLabelIndex = 0;
        new RecursionReversalDir(new FileHandle(myGdxGame.getToolManager().getReadPath()), null) {
            @Override
            protected void callback(FileHandle readFileHandle, String writeFileHandle) {
                if (readFileHandle.name().endsWith(".fnt")) {
                    readFntFile(readFileHandle);
                }
            }
        };

        Group adjustTableGroup = ActorFactory.createCsdJson(Assets.getIns().getAssetManager(), Res.LAYOUT_DIR + "adjustTableOld.json");
        final Group adjustList = adjustTableGroup.findActor("adjustList");
        setSize(adjustTableGroup.getWidth(), adjustTableGroup.getHeight());
        cameraBaseLayout();
//
        addActor(adjustTableGroup);
        positionButton = new CrossButton(adjustList.<Group>findActor("positionGroup")) {
            @Override
            public void topClick(int num) {
                if (isChickAll) {
                    getAdjustLabel().setCharacterYAll(num);
                } else {
                    getAdjustLabel().setCharacterStringY(adjustChar.getText(), num);
                }
                showLabel.layout();
            }

            @Override
            public void bottomClick(int num) {
                if (isChickAll) {
                    getAdjustLabel().setCharacterYAll(num);
                } else {
                    getAdjustLabel().setCharacterStringY(adjustChar.getText(), num);
                }
                showLabel.layout();
            }

            @Override
            public void leftClick(int num) {
                if (isChickAll) {
                    getAdjustLabel().setCharacterXAll(num);
                } else {
                    getAdjustLabel().setCharacterStringX(adjustChar.getText(), num);
                }
                showLabel.layout();
            }

            @Override
            public void rightClick(int num) {
                if (isChickAll) {
                    getAdjustLabel().setCharacterXAll(num);
                } else {
                    getAdjustLabel().setCharacterStringX(adjustChar.getText(), num);
                }
                showLabel.layout();
            }
        };

        sizeButton = new CrossButton(adjustList.<Group>findActor("sizeGroup")) {
            @Override
            public void topClick(int num) {
                if (isChickAll) {
                    getAdjustLabel().setCharacterHeightAll(num);
                } else {
                    getAdjustLabel().setCharacterStringHeight(adjustChar.getText(), num);
                }
                showLabel.layout();
            }

            @Override
            public void bottomClick(int num) {
                if (isChickAll) {
                    getAdjustLabel().setCharacterHeightAll(num);
                } else {
                    getAdjustLabel().setCharacterStringHeight(adjustChar.getText(), num);
                }
                showLabel.layout();
            }

            @Override
            public void leftClick(int num) {
                if (isChickAll) {
                    getAdjustLabel().setCharacterWidthAll(num);
                } else {
                    getAdjustLabel().setCharacterStringWidth(adjustChar.getText(), num);
                }

                showLabel.updata();
            }

            @Override
            public void rightClick(int num) {
                if (isChickAll) {
                    getAdjustLabel().setCharacterWidthAll(num);
                } else {
                    getAdjustLabel().setCharacterStringWidth(adjustChar.getText(), num);
                }
                showLabel.updata();
            }
        };

        offsetButton = new CrossButton(adjustList.<Group>findActor("offsetGroup")) {
            @Override
            public void topClick(int num) {
                if (isChickAll) {
                    getAdjustLabel().setCharacterOffsetYAll(num);
                } else {
                    getAdjustLabel().setCharacterStringY(adjustChar.getText(), num);
                }
                showLabel.layout();
            }

            @Override
            public void bottomClick(int num) {
                if (isChickAll) {
                    getAdjustLabel().setCharacterOffsetYAll(num);
                } else {
                    getAdjustLabel().setCharacterStringY(adjustChar.getText(), num);
                }
                showLabel.layout();
            }

            @Override
            public void leftClick(int num) {
                if (isChickAll && false) {
                    getAdjustLabel().setCharacterOffsetXAll(num);
                } else {
                    getAdjustLabel().setCharacterOffsetX(adjustChar.getText().toCharArray()[0], num);
                }
                showLabel.updata();
            }

            @Override
            public void rightClick(int num) {
                if (isChickAll && false) {
                    getAdjustLabel().setCharacterOffsetXAll(num);
                } else {
                    getAdjustLabel().setCharacterOffsetX(adjustChar.getText().toCharArray()[0], num);
                }
                showLabel.updata();
            }
        };

        xAdvanceButton = new FlatButton(adjustList.<Group>findActor("xadvanceGroup")) {
            @Override
            public void leftClick(int flatText) {
                if (isChickAll) {
                    getAdjustLabel().setXAdvanceAll(flatText);
                } else {
                    getAdjustLabel().setStringXAdvance(adjustChar.getText(), flatText);
                }
                showLabel.updata();
                System.out.println("FntAdjustLayer:  " + showLabel.getWidth());
            }

            @Override
            public void rightClick(int flatText) {
                if (isChickAll) {
                    getAdjustLabel().setXAdvanceAll(flatText);
                } else {
                    getAdjustLabel().setStringXAdvance(adjustChar.getText(), flatText);
                }
                showLabel.updata();
                System.out.println("FntAdjustLayer:  " + showLabel.getWidth());
            }
        };

        lineHeightButton = new FlatButton(adjustList.<Group>findActor("lineHeight")) {
            @Override
            public void leftClick(int flatText) {
                getAdjustLabel().setLineHeight(getAdjustLabel().getBitMapFont(), flatText);
                showLabel.layout();
            }

            @Override
            public void rightClick(int flatText) {
                getAdjustLabel().setLineHeight(getAdjustLabel().getBitMapFont(), flatText);
                showLabel.layout();
            }
        };

        baseLineButton = new FlatButton(adjustList.<Group>findActor("baseLine")) {
            @Override
            public void leftClick(int flatText) {
                getAdjustLabel().setBaseLine(flatText);
                showLabel.setSize(showLabel.getPrefWidth(), showLabel.getPrefHeight());
                showLabel.layout();
            }

            @Override
            public void rightClick(int flatText) {
                getAdjustLabel().setBaseLine(flatText);
                showLabel.setSize(showLabel.getPrefWidth(), showLabel.getPrefHeight());
                showLabel.layout();
            }
        };
        //需要调整字符 TextField
        adjustChar = new TextFieldButton(adjustList.<Group>findActor("textField"), "0");
        adjustChar.getTextField().addListener(new ClickListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    if (adjustChar.getText() != null) {
                        showLabel.setSize(showLabel.getPrefWidth(), showLabel.getPrefHeight());
                    }
                    return true;
                }
                return false;
            }
        });

        showTableText = new TextFieldButton(adjustList.<Group>findActor("textField2"), defaultText);
        showTableText.getTextField().addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    showLabel.setText(showTableText.getText());
                    showLabel.updata();
                    return true;
                }
                return false;
            }
        });

        chickAll = adjustList.findActor("checkAll");
        chickAll.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isChickAll = !chickAll.isChecked();
                adjustChar.getTextField().setDisabled(isChickAll);
            }
        });
        chickAll.setChecked(true);
        isChickAll = !chickAll.isChecked();

        centerShowLayerX = (adjustTableGroup.getWidth() - adjustList.getWidth()) / 2f;
        centerShowLayerY = adjustTableGroup.getHeight() / 2f;

        try {
            showLabel = new DebugLabel(defaultText, new Label.LabelStyle(getAdjustLabel().getBitMapFont(), Color.WHITE));
        } catch (Exception e) {
            showLabel = new DebugLabel("", new Label.LabelStyle(getAdjustLabel().getBitMapFont(), Color.WHITE));
        }
        showLabel.setAlignment(Align.center);
        showLabel.setPosition(240, 291, Align.center);
        showLabel.addListener(new DragClickListener());
        showLabel.debug();
        adjustTableGroup.addActorAt(0, showLabel);
    }

    public void readFntFile(FileHandle fileHandle) {
        AdjustLabel adjustLabel = new AdjustLabel();
        adjustLabel.read(fileHandle);
        adjustLabelArray.add(adjustLabel);
    }

    public void reset() {
        showLabel.setPosition(centerShowLayerX, centerShowLayerY, Align.center);
        showLabel.setPosition(240, 291, Align.center);
    }

    public AdjustLabel getAdjustLabel() {
        currentAdjustLabelIndex = MathUtils.clamp(currentAdjustLabelIndex, 0, adjustLabelArray.size - 1);
        return adjustLabelArray.get(currentAdjustLabelIndex);
    }
}

package com.mygdx.game.Draw.Layout.Label;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.mygdx.game.Draw.Button.CrossButton;
import com.mygdx.game.Draw.Button.FlatButton;
import com.mygdx.game.Draw.Button.TextButton;
import com.mygdx.game.Draw.Button.TextFieldButton;
import com.mygdx.game.Draw.ClickListener.DragClickListener;
import com.mygdx.game.Draw.Layout.BaseLayout;
import com.mygdx.game.Draw.Layout.PictureLayer;
import com.mygdx.game.Draw.Layout.ScrollPanlLayer;
import com.mygdx.game.Logic.CallBack.RecursionReversalDir;
import com.mygdx.game.Logic.Function.Label.DebugLabel;
import com.mygdx.game.Logic.Function.Label.FntAdjust.AdjustLabel;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Res;
import com.qs.utils.actor.ActorFactory;
import com.qs.utils.assets.Assets;

import net.mwplay.cocostudio.ui.widget.TCheckBox;

import java.io.File;

public class FntAdjustLayout extends BaseLayout {
    private int currentAdjustLabelIndex;

    private DebugLabel showLabel;


    float centerShowLayerX;
    float centerShowLayerY;

    private boolean isChickAll = true;

    //
    protected Group adjustList;
    public static final String defaultText = "0123456789MTB";

    PictureLayer PictureLayer;
    ScrollPanlLayer bitmapDataScrollPanlLayer;
    protected ArrayMap<String, AdjustFntData> adjustLabelArray = new ArrayMap<>(1);

    public FntAdjustLayout(MyGdxGame myGdxGame) {
        super(myGdxGame);

        Group adjustTableGroup = ActorFactory.createCsdJson(Assets.getIns().getAssetManager(), Res.FNT_ADJUST_LAYOUT);
        adjustList = adjustTableGroup.findActor("adjustList");
        setSize(adjustTableGroup.getWidth(), adjustTableGroup.getHeight());
        cameraBaseLayerout();

        bitmapDataScrollPanlLayer = new ScrollPanlLayer(myGdxGame, (int) adjustList.getWidth(), 170, false);

        new RecursionReversalDir(new FileHandle(myGdxGame.getToolManager().getReadPath()), null) {
            @Override
            protected void callback(FileHandle readFileHandle, String writeFileHandle) {
                if (readFileHandle.name().endsWith(".fnt")) {
                    readFntFile(readFileHandle);
                }
            }
        };


//
        addActor(adjustTableGroup);


        centerShowLayerX = (adjustTableGroup.getWidth() - adjustList.getWidth()) / 2f;
        centerShowLayerY = adjustTableGroup.getHeight() / 2f;

        showLabel = new DebugLabel("31", new Label.LabelStyle(getAdjustLabel().getBitMapFont(), Color.WHITE));
        BitmapFont.Glyph g = showLabel.getStyle().font.getData().getGlyph('3');
        System.out.println("id: " + g.id + " offsetX " + g.xoffset + " offsetY " + g.yoffset);
        showLabel.setAlignment(Align.center);
        showLabel.setPosition(240, 291, Align.center);
        showLabel.addListener(new DragClickListener());
        showLabel.debug();
        adjustTableGroup.addActorAt(0, showLabel);
    }

    public void readFntFile(FileHandle fileHandle) {
        bitmapDataScrollPanlLayer.addActor(getFntNameGroup(fileHandle.name()));
        AdjustLabel adjustLabel = new AdjustLabel();
        adjustLabel.read(fileHandle);

        ScrollPanlLayer charLayer = new ScrollPanlLayer(mainGame, (int) adjustList.getWidth(), 170, false);
        for (int i = 0; i < adjustLabel.getBitmapFontData().bitMapCharArray.size; ++i) {
            Group dataG = ActorFactory.createCsdJson(Assets.getIns().getAssetManager(), Res.FNT_FNT_DATA);
        }

        adjustLabelArray.put(fileHandle.name(), new AdjustFntData(charLayer, adjustLabel));
    }

    private Group getFntNameGroup(String name) {
        Group fntData = ActorFactory.createCsdJson(Assets.getIns().getAssetManager(), Res.FNT_FNT_DATA);
        Label nameLabel = fntData.findActor("name");
        nameLabel.setText(name);
        return fntData;
    }

//    private ScrollPanlLayer getCharsScrollPanlLayer() {
//        ScrollPanlLayer p = new ScrollPanlLayer(mainGame, (int) adjustList.getWidth(), 170, false);
//
//    }

    public void reset() {
        showLabel.setPosition(centerShowLayerX, centerShowLayerY, Align.center);
        showLabel.setPosition(240, 291, Align.center);
    }

    public AdjustLabel getAdjustLabel() {
        currentAdjustLabelIndex = MathUtils.clamp(currentAdjustLabelIndex, 0, adjustLabelArray.size - 1);
        return adjustLabelArray.get("aa").adjustLabel;
    }

    public class AdjustFntData {
        public AdjustLabel adjustLabel;
        public ScrollPanlLayer charsPanlLayer;

        public AdjustFntData(ScrollPanlLayer charsPanlLayer, AdjustLabel adjustFntData) {
            this.charsPanlLayer = charsPanlLayer;
            this.adjustLabel = adjustFntData;
        }
    }
}

package com.mygdx.game.Draw.Layout.Label;

import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.Logic.Function.Label.FntAdjust.AdjustEqualWidthLabel;
import com.mygdx.game.MyGdxGame;

public class FntAdjustEqualWidthLayout extends FntAdjustLayoutOld {

    public FntAdjustEqualWidthLayout(MyGdxGame myGdxGame) {
        super(myGdxGame);
    }

    @Override
    public void readFntFile(FileHandle fileHandle) {
        AdjustEqualWidthLabel adjustLabel = new AdjustEqualWidthLabel();
        adjustLabel.read(fileHandle);
        adjustLabelArray.add(adjustLabel);
    }
}

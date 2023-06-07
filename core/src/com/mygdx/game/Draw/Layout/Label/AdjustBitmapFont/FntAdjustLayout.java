package com.mygdx.game.Draw.Layout.Label.AdjustBitmapFont;

import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.Constants;
import com.mygdx.game.Draw.Layout.BaseLayout;
import com.mygdx.game.Draw.Layout.ShowPictureGroup;
import com.mygdx.game.Logic.CallBack.RecursionReversalDir;
import com.mygdx.game.Logic.Function.Label.FntAdjust.AdjustLabel;
import com.mygdx.game.MyGdxGame;

public class FntAdjustLayout extends BaseLayout {
    public ShowViewGroup showViewGroup;
    public BarViewGroup barViewGroup;
    public AdjustLabel adjustLabel;
    public String name;

    public FntAdjustLayout(MyGdxGame myGdxGame) {
        super(myGdxGame);
        ShowPictureGroup showPictureGroup = new ShowPictureGroup(myGdxGame);
        showPictureGroup.setSize(Constants.WORLD_WIDTH - 400, Constants.WORLD_HEIGHT);

        showViewGroup = new ShowViewGroup(this);
        showPictureGroup.addActor(showViewGroup);
        addActor(showPictureGroup);

        barViewGroup = new BarViewGroup(this);

        new RecursionReversalDir(new FileHandle(myGdxGame.getToolManager().getReadPath()), null) {
            @Override
            protected void callback(FileHandle readFileHandle, String writeFileHandle) {
                if (readFileHandle.name().endsWith(".fnt")) {
                    readFntFile(readFileHandle);
                }
            }
        };
    }

    public void readFntFile(FileHandle fileHandle) {
        name = fileHandle.name();
        adjustLabel = new AdjustLabel();
        adjustLabel.read(fileHandle);
        barViewGroup.init();
        showViewGroup.init();
    }

    public String getDefalutString(String string) {
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

    public void showText(String string) {
        if (string != null) {
            string = getDefalutString(string);
            showViewGroup.showText(string);
            barViewGroup.showText(string);
        }
    }

    public void showSelectText(String string) {
        if (string != null) {
            showViewGroup.showSelectedText(string);
            barViewGroup.showSelectedText(string);
        }
    }
}

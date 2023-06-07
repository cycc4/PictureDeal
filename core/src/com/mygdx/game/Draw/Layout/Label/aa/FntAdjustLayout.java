package com.mygdx.game.Draw.Layout.Label.aa;

import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.Constants;
import com.mygdx.game.Draw.Layout.BaseLayout;
import com.mygdx.game.Draw.Layout.ShowPictureGroup;
import com.mygdx.game.Logic.CallBack.RecursionReversalDir;
import com.mygdx.game.MyGdxGame;

public class FntAdjustLayout extends BaseLayout {
    protected ToolBarGroup toolBar;
    protected ShowPictureGroup backageGroup;

    public FntAdjustLayout(MyGdxGame myGdxGame) {
        super(myGdxGame);
        backageGroup = new ShowPictureGroup(myGdxGame);
        backageGroup.setSize(Constants.WORLD_WIDTH - 400, Constants.WORLD_HEIGHT);
        toolBar = new ToolBarGroup(this);
        backageGroup.setPosition(400, 0);

        myGdxGame.getStage().setScrollFocus(backageGroup.getRootGroup());
        addActor(backageGroup);
        addActor(toolBar);


        new RecursionReversalDir(new FileHandle(myGdxGame.getToolManager().getReadPath()), null) {
            @Override
            protected void callback(FileHandle readFileHandle, String writeFileHandle) {
                if (readFileHandle.name().endsWith(".fnt")) {
                    toolBar.readBitmapfont(readFileHandle);
                }
            }
        };
    }

    public ShowPictureGroup getBackageGroup() {
        return backageGroup;
    }

}

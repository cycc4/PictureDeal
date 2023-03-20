package com.mygdx.game.Draw;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Draw.Layout.BaseLayout;
import com.mygdx.game.MyGdxGame;

public class DialogManage {
    private MyGdxGame myGdxGame;
    private Array<BaseLayout> dialogBaseArray = new Array<>();
    public DialogManage(MyGdxGame myGdxGame){
        this.myGdxGame = myGdxGame;
    }


    public void showNineDialog() {
    }

    public void addDialog(){

    }

    public void resize(int width, int height) {
        for (BaseLayout db : dialogBaseArray) {
            db.resize(width, height);
        }
    }
}

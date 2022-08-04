package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.Manager.ToolManager;

public class MyGdxGame extends ApplicationAdapter {
    private Stage stage;
    private ToolManager toolManager;
    private String[] formalParameter;

    public MyGdxGame(String[] strings) {
        formalParameter = strings;
    }

    @Override
    public void create() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        toolManager = new ToolManager(this);
//        String readPath = null;
//        String writePath = null;
//        if (formalParameter != null && formalParameter.length >= 2) {
//            readPath = formalParameter[0];
//            writePath = formalParameter[1];
//        }
//        toolManager.getParticleComposite(readPath, writePath).composite();

//        toolManager.dealPictureRGB();
        toolManager.dealSpineCombination();
    }

    public Stage getStage() {
        return stage;
    }

    public String[] getFormalParameter() {
        return formalParameter;
    }

    @Override
    public void render() {
//        ScreenUtils.clear(1, 1, 1, 1);
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

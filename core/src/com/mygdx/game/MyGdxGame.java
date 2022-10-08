package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.Logic.Factory.ToolManager;

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

        toolManager.dealPictureRGB();
//        toolManager.dealSpineCombination();
//        toolManager.dealParticleCombination();
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

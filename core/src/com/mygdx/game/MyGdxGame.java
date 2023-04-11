package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.game.Draw.DialogManage;
import com.mygdx.game.Draw.Layout.Label.FntAdjustLayoutOld;
import com.mygdx.game.Logic.AdjustJson;
import com.mygdx.game.Logic.ToolsFactory;

public class MyGdxGame extends ApplicationAdapter {
    private Stage stage;
    private ToolsFactory toolManager;
    private DialogManage dialogManage;

    private String[] formalParameter;

    public MyGdxGame(String[] strings) {
        formalParameter = strings;

        for (int i = 0; i < strings.length; ++i) {
            System.out.println("i: " + i + "   " + strings[i]);
        }
    }

    @Override
    public void create() {
        stage = new Stage(new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        dialogManage = new DialogManage(this);
        toolManager = new ToolsFactory(this);

//        toolManager.dealPictureRGB();

//        toolManager.dealNinePacture(101, 37, 30, 65);
//        toolManager.dealSpineCombination();
//        toolManager.dealParticleCombination();
//        toolManager.dealPictureRGB();

//        SimpleTextFieldActor s = new SimpleTextFieldActor();
//        s.setSize(200, 100);
//        s.setPosition(640, 640);
//        s.debug();
//        stage.addActor(s);

//        FntAdjustLayout fntAdjustLayout = new FntAdjustLayout(this);
//        stage.addActor(fntAdjustLayout);

//        FntAdjustLayoutOld fntAdjustEqualWidthLayout = new FntAdjustLayoutOld(this);
//        stage.addActor(fntAdjustEqualWidthLayout);
//        Label ceshiLabel = ActorFactory.getLabel("123", "Old/PR-Columban_96-ZSH.fnt");
//        System.out.println("ppppppppppppppppppppppppppppp:  " + ceshiLabel.getPrefWidth()+"   "+ceshiLabel.getWidth());
//        ceshiLabel.setPosition(Constants.WORLD_WIDTH / 2f, Constants.WORLD_HEIGHT / 2f, Align.center);
//        stage.addActor(ceshiLabel);

        AdjustJson adjustJson = new AdjustJson();
        adjustJson.read(new FileHandle(toolManager.getReadPath()), toolManager.getWritePath());
    }

    public Stage getStage() {
        return stage;
    }

    public ToolsFactory getToolManager() {
        return toolManager;
    }

    public String[] getFormalParameter() {
        return formalParameter;
    }

    public float getWordWidth() {
        return stage.getViewport().getWorldWidth();
    }

    public float getWordHeight() {
        return stage.getViewport().getWorldHeight();
    }

    public float getScreenWidth() {
        return stage.getViewport().getLeftGutterWidth();
    }

    public float getScreenHeight() {
        return stage.getViewport().getBottomGutterHeight();
    }

    @Override
    public void resize(int width, int height) {
        Constants.SCREEN_WIDTH = width;
        Constants.SCREEN_HEIGHT = height;
        dialogManage.resize(width, height);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(Constants.backGround_r, Constants.backGround_g, Constants.backGround_b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

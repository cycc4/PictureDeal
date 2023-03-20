package com.qs.utils.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.qs.utils.global.GlobalBatch;
import com.qs.utils.global.GlobalViewPort;
import com.qs.utils.stage.ShipeiStage;

public class ScreenTemp extends ScreenAdapter {

    Stage stage;

    @Override
    public void show() {
        stage = new ShipeiStage(GlobalViewPort.getShipeiExtendViewport(), GlobalBatch.getCpuPolygonSpriteBatch());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        stage.act(Math.min(delta, 1 / 30f));
        Gdx.gl.glClearColor(Color.SKY.r, Color.SKY.g, Color.SKY.b, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }
}

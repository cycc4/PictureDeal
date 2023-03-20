package com.qs.utils.actor;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.qs.ui.ManagerUIEditor;
import com.qs.ui.loader.ManagerUILoader;
import com.qs.utils.assets.Assets;

public class ActorFactory {
    public static Group createCsdJson(AssetManager manager, String path) {
        ManagerUIEditor managerUIEditor;
        if (!manager.isLoaded(path)) {
            manager.load(path, ManagerUIEditor.class, new ManagerUILoader.ManagerUIParameter(null, manager));
            manager.finishLoading();
        }
        managerUIEditor = manager.get(path);
        return managerUIEditor.createGroup();
    }

    public static Label getLabel(String text, String bitmapPath) {
        return new Label(text, new Label.LabelStyle(Assets.getIns().getBitmapFont(bitmapPath), Color.WHITE));
    }

    public static Texture getTexture(AssetManager manager, String path) {
        if (!manager.isLoaded(path, Texture.class)) {
            manager.load(path, Texture.class, Assets.textureParameter);
            manager.finishLoading();
        }
        return manager.get(path, Texture.class);
    }
}

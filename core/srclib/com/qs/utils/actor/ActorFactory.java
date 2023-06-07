package com.qs.utils.actor;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.mygdx.game.Draw.Actor.MySpineActor;
import com.mygdx.game.Draw.Actor.MySpineGroup;
import com.qs.ui.ManagerUIEditor;
import com.qs.ui.loader.ManagerUILoader;
import com.qs.utils.assets.Assets;

public class ActorFactory {
    public static SkeletonRenderer renderer = new SkeletonRenderer();

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

    public static MySpineActor getSpineActor(String path) {
        return new MySpineActor(Assets.getIns().getSkeletonData(path, null), renderer);
    }

    /**
     * 带themeData参数的资源在第一次被加载时 会在对应的themeData中被管理
     */
    public static MySpineActor getSpineActor(String path, String atlasPath) {
        return new MySpineActor(Assets.getIns().getSkeletonData(path, atlasPath), renderer);
    }

    /**
     * 带themeData参数的资源在第一次被加载时 会在对应的themeData中被管理
     */
    public static MySpineGroup getSpineGroup(String path) {
        return new MySpineGroup(Assets.getIns().getSkeletonData(path, null), renderer);
    }

    /**
     * 带themeData参数的资源在第一次被加载时 会在对应的themeData中被管理
     */
    public static MySpineGroup getSpineGroup(String path, String atlasPath) {
        return new MySpineGroup(Assets.getIns().getSkeletonData(path, atlasPath), renderer);
    }
}

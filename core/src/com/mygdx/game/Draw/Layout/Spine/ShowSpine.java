package com.mygdx.game.Draw.Layout.Spine;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.attachments.AtlasAttachmentLoader;
import com.mygdx.game.Constants;
import com.mygdx.game.Draw.Actor.MySpineActor;
import com.mygdx.game.Draw.Layout.BaseLayout;
import com.mygdx.game.Logic.CallBack.RecursionReversalDir;
import com.mygdx.game.MyGdxGame;
import com.qs.utils.actor.ActorFactory;

public class ShowSpine extends BaseLayout {
    MySpineActor spine;

    public ShowSpine(MyGdxGame myGdxGame) {
        super(myGdxGame);
        setSize(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);


//        new RecursionReversalDir(new FileHandle(myGdxGame.getToolManager().getReadPath()), null) {
//            @Override
//            protected void callback(FileHandle readFileHandle, String writeFileHandle) {
//                if (readFileHandle.name().endsWith(".json")) {
//                    TextureAtlas atlas = new TextureAtlas(readFileHandle.pathWithoutExtension() + ".atlas");
//                    for (TextureAtlas.AtlasRegion a : atlas.getRegions()) {
//                        System.out.println("ooooooooooooooooooooooooooooooooooooooooo:  " + a.name);
//                    }
//                    AtlasAttachmentLoader attachmentLoader = new AtlasAttachmentLoader(atlas);
//                    SkeletonJson skeletonJson = new SkeletonJson(attachmentLoader);
//                    SkeletonData data = skeletonJson.readSkeletonData(readFileHandle);
//                    spine = new MySpineActor(data, ActorFactory.renderer);
//                    spine.setPosition(getWidth() / 2f, getHeight() / 2f);
//                    spine.play("show", true);
//                    addActor(spine);
//                }
//            }
//        };

        MySpineActor actor = ActorFactory.getSpineActor("Old/freegame.json");
        addActor(actor);
    }
}

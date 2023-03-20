//package com.mygdx.game.Logic.Spine;
//
//import com.badlogic.gdx.graphics.g2d.Batch;
//import com.badlogic.gdx.scenes.scene2d.Group;
//import com.badlogic.gdx.scenes.scene2d.InputEvent;
//import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
//import com.casino.godlike.slot.base.ActorFactory;
//import com.casino.godlike.slot.data.GameData;
//import com.casino.godlike.slot.ui.actor.MySpineGroup;
//import com.esotericsoftware.spine.AnimationState;
//import com.esotericsoftware.spine.Skeleton;
//
///*
// *根据手滑动的距离来播放动画的百分比
// * 最大距离根据当前group的宽度来判断
// */
//public class SpineAnimationControl extends Group {
//    public float xOffset, yOffset;
//    public MySpineGroup debugSpine;
//    public AnimationState animationState;
//    public Skeleton skeleton;
//    public float animationTime;
//    public boolean change = false;
//
//    public boolean isPause = false;
//
//    public SpineAnimationControl(String spineJson, String animationName) {
//        setSize(1280, 720);
//
//        debugSpine = ActorFactory.newSpineGroup(spineJson, GameData.ins.themeChoose);
//        debugSpine.setPosition(640, 360);
//        animationState = debugSpine.getSpineActor().getAnimationState();
//        skeleton = debugSpine.getSpineActor().getSkeleton();
//
//        animationTime = debugSpine.getSpineActor().getAniDuration(animationName);
//        debugSpine.play(animationName);
//
//        addListener(new ActorGestureListener() {
//            float startX, startY;
//
//            @Override
//            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                startX = x;
//                startY = y;
//                isPause = true;
//            }
//
//            @Override
//            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
//                if (x < startX) {
//                    x = startX;
//                }
//
//                xOffset = Math.abs(x - startX);
//                yOffset = Math.abs(y - startY);
//
//                skeleton.setSkin("default");
//                animationState.setAnimation(0, animationName, false);
//                if (xOffset > 640) {
//                    xOffset = 640;
//                }
//                float delay = xOffset / 640 * animationTime;
//                animationState.update(delay);
//                animationState.apply(skeleton);
//                debugSpine.getSpineActor().updateTransform();
//            }
//
//            @Override
//            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                super.touchUp(event, x, y, pointer, button);
//                isPause = false;
//            }
//        });
//    }
//
//    @Override
//    public void act(float delta) {
//        super.act(delta);
//
//        if (debugSpine != null && !isPause) {
//            debugSpine.act(delta);
//        }
//    }
//
//    @Override
//    public void draw(Batch batch, float parentAlpha) {
//        if (debugSpine != null) {
//            debugSpine.draw(batch, parentAlpha);
//        }
//    }
//}

package com.mygdx.game.Draw.Actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Event;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.Slot;

/**
 * A scene2d actor that draws a skeleton.
 */

//TODO ： 同一个spine的多个实例其实可以共用一个SkeletonData和AnimationData
public class MySpineActor extends Actor {
    protected SkeletonRenderer renderer;
    protected Skeleton skeleton;

    protected AnimationStateData stateData;
    protected AnimationState state;

    protected float scaleOriginX;
    protected float scaleOriginY;

    boolean clip;
    public boolean isStateUpdateManaged = false;
    public boolean isPaused = false;

    protected int align = Align.center;

    public MySpineActor(MySpineStatus status, SkeletonRenderer renderer) {
        setSpineStatus(status);
    }

    public MySpineActor(SkeletonData skeletonData, SkeletonRenderer renderer) {
        this(null, new Skeleton(skeletonData), new AnimationState(new AnimationStateData(skeletonData)));
        this.renderer = renderer;
    }

    public MySpineActor(SkeletonRenderer renderer, Skeleton skeleton, AnimationState state) {
        setTouchable(Touchable.disabled);
        this.renderer = renderer;
        this.skeleton = skeleton;
        this.state = state;

        scaleOriginX = skeleton.getRootBone().getScaleX();
        scaleOriginY = skeleton.getRootBone().getScaleY();
    }

    public MySpineActor(SkeletonRenderer renderer, MySpineStatus status) {
        setTouchable(Touchable.disabled);
        this.renderer = renderer;
        setSpineStatus(status);
    }

    public MySpineActor setSpineStatus(MySpineStatus status) {
        this.skeleton = status.skeleton;
        this.stateData = status.animationStateData;
        this.state = status.animationState;
        this.isStateUpdateManaged = status.isUpdateManaged;

        scaleOriginX = skeleton.getRootBone().getScaleX();
        scaleOriginY = skeleton.getRootBone().getScaleY();
        return this;
    }

    public void pause(boolean isPause) {
        this.isPaused = isPause;
    }

    public void act(float delta) {
        if (!isVisible()) {
            return;
        }
        super.act(delta);

        if (!isStateUpdateManaged && !isPaused)
            state.update(delta);
        state.apply(skeleton);
        updateTransform();
    }

    public void updateTransform() {
        skeleton.getRootBone().setScale(scaleOriginX * getScaleX(), scaleOriginY * getScaleY());
        if (align == Align.center)
            skeleton.setPosition(getX() + getWidth() / 2, getY());
        else
            skeleton.setPosition(getX(), getY());
        skeleton.updateWorldTransform();
    }

    public void draw(Batch batch, float parentAlpha) {
        if (!isVisible()) return;
        Color color = skeleton.getColor();
        float oldAlpha = color.a;
        skeleton.getColor().a *= getColor().a * parentAlpha;

        int b1 = batch.getBlendDstFunc();
        int b2 = batch.getBlendSrcFunc();

        if (clip) {
            batch.flush();
            clipBegin();
            if (batch instanceof PolygonSpriteBatch)
                renderer.draw((PolygonSpriteBatch) batch, skeleton);
            else
                renderer.draw(batch, skeleton);
            batch.flush();
            clipEnd();
        } else {

//            int ti = 0;
//            int rc = 0;
//            if (Constants.DEBUG && batch instanceof CpuPolygonSpriteBatch) {
////                batch.flush();
//                ti = ((CpuPolygonSpriteBatch) batch).getTriangleIndex();
//                rc = ((CpuPolygonSpriteBatch) batch).renderCalls;
//            }
            if (batch instanceof PolygonSpriteBatch)
                renderer.draw((PolygonSpriteBatch) batch, skeleton);
            else
                renderer.draw(batch, skeleton);

//            if (Constants.DEBUG && batch instanceof CpuPolygonSpriteBatch) {
//                int ti2 = ((CpuPolygonSpriteBatch) batch).getTriangleIndex();
//                int rc2 = ((CpuPolygonSpriteBatch) batch).renderCalls;
//                int count = ti2 - ti;
//                if ((rc2 - rc > 2 || count > 2000) && count < triangleLast) {
//                    LogUtils.logError(skeleton.getData().getName() + "  rendercall:" + (rc2 - rc) + "  tri:" + count + " " + animationName);
//                    triangleLast = count;
//                }
//            }
        }
        batch.setBlendFunction(b2, b1);
        color.a = oldAlpha;

        skeleton.getRootBone().setScale(scaleOriginX, scaleOriginY);
    }

    private int triangleLast = 0;

    public void setRenderer(SkeletonRenderer renderer) {
        this.renderer = renderer;
    }

    public Skeleton getSkeleton() {
        return skeleton;
    }

    public void setSkeleton(Skeleton skeleton) {
        this.skeleton = skeleton;
    }

    public AnimationStateData getAnimationStateData() {
        return stateData;
    }

    public AnimationState getAnimationState() {
        return state;
    }

    public void setAnimationState(AnimationState state) {
        this.state = state;
    }

    public void setClip(boolean b) {
        clip = b;
    }

    protected String skinName = "default";

    public void setSkinName(String skinName) {
        this.skinName = skinName;
    }


    public MySpineActor playDefault() {
        String aniName = getSkeleton().getData().getAnimations().get(0).getName();
        play(aniName, skinName, true);
        return this;
    }

    public void play(String animationName, boolean isLoop) {
        play(animationName, skinName, isLoop);
    }

    public void play(String animationName, String skinName, boolean isLoop) {
//        skeleton.setToSetupPose();
//        clearTracks();
        this.animationName = animationName;
        this.skinName = skinName;
        this.isLoop = isLoop;
        skeleton.setSkin(skinName);
        state.setAnimation(0, animationName, isLoop);
        // act(0); // 防止 act 之前 draw ?
    }

    public void playSkin(String skinName, boolean isLoop) {
//        skeleton.setToSetupPose();
//        clearTracks();
        this.animationName = getSkeleton().getData().getAnimations().get(0).getName();
        this.skinName = skinName;
        this.isLoop = isLoop;
        skeleton.setSkin(skinName);
        state.setAnimation(0, animationName, isLoop);
        // act(0); // 防止 act 之前 draw ?
    }

    public String getDefaultAniName() {
        return getSkeleton().getData().getAnimations().get(0).getName();
    }

    public String getAniName(int index) {
        return getSkeleton().getData().getAnimations().get(index).getName();
    }

    public float getAniDuration(String animationName) {
        Animation animation = skeleton.getData().findAnimation(animationName);
        return animation == null ? 0 : animation.getDuration();
    }

    public float getAniDuration() {
        Animation animation = skeleton.getData().findAnimation(getDefaultAniName());
        return animation == null ? 0 : animation.getDuration();
    }

    protected String animationName = null;
    protected boolean isLoop = false;

    public void resetAnimation() {
        if (animationName == null || skinName == null) {
            stop();
        } else {
            skeleton.setSkin(skinName);
            state.setAnimation(0, animationName, isLoop);
        }
    }

    public int getAnimationIndex(String aniName) {
        if (aniName == null)
            return -1;
        Array<Animation> animations = getSkeleton().getData().getAnimations();
        for (int i = 0; i < animations.size; i++) {
            if (animations.get(i).getName().equals(aniName)) {
                return i;
            }
        }
        return -1;
    }

    public boolean play(int index, boolean isLoop) {
        try {
            Array<Animation> animations = getSkeleton().getData().getAnimations();
            if (index < 0 || index >= animations.size)
                return false;
            play(animations.get(index).getName(), isLoop);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void stop() {
        skeleton.setToSetupPose();
        state.clearTracks();
    }

    public void setTimeScale(float scale) {
        state.setTimeScale(scale);
    }

    public String getAnimationName() {
        return state.getTracks().get(0).getAnimation().getName();
    }

    public void clearTracks() {
        state.clearTracks();
    }

    public void setAlign(int align) {
        this.align = align;
    }

    public void setPosition(float x, float y, int align) {
        super.setPosition(x, y, align);
        this.align = align;
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        if (touchable && this.getTouchable() != Touchable.enabled) {
            return null;
        } else if (!this.isVisible()) {
            return null;
        }

        if (align == Align.center)
            return x > 0 && x < this.getWidth() && y > 0 && y < this.getHeight() ? this : null;
        return Math.abs(x) < this.getWidth() / 2 && Math.abs(y) < this.getHeight() / 2 ? this : null;
    }

    //将actor绑定到spineactor上
    public void binding(String slotName, Actor actor, float offsetX, float offsetY) {
        binding(slotName, actor, offsetX, offsetY, 1);
    }

    public void binding(String slotName, Actor actor, final float offsetX, final float offsetY, final float offsetScale) {
        //将actor居中
        actor.setPosition(0, 0, Align.center);
        actor.setOrigin(Align.center);

        Skeleton skeleton = this.getSkeleton();
        final Slot slot = skeleton.findSlot(slotName);
        final Bone bone = slot == null ? skeleton.findBone(slotName) : slot.getBone();
        final Bone bonePar = bone.getParent();
        actor.setName(slotName);

        final float rootScaleX = bone.getSkeleton().getRootBone().getScaleX();
        final float rootScaleY = bone.getSkeleton().getRootBone().getScaleY();

        final Vector2 posTemp = new Vector2();
        actor.addAction(new Action() {
            @Override
            public boolean act(float v) {
                if (!bone.isActive() || slot != null && slot.getAttachment() == null) {
                    actor.setVisible(false);
                    return false;
                }
                actor.setVisible(true);
                if (bonePar != null) {
                    Vector2 temp = bone.getParent().localToWorld(posTemp.set(bone.getX(), bone.getY()));
                    actor.setPosition(temp.x + offsetX, temp.y + offsetY, Align.center);
                    actor.setRotation(bonePar.localToWorldRotation(bone.getRotation()));
                } else {
                    actor.setPosition(bone.getWorldX() + offsetX, bone.getWorldY() + offsetY, Align.center);
                    actor.setRotation(bone.localToWorldRotation(bone.getRotation()));
                }
                actor.setScale(bone.getWorldScaleX() / rootScaleX * offsetScale, bone.getWorldScaleY() / rootScaleY * offsetScale);
                if (slot != null)
                    actor.setColor(slot.getColor());
                return false;
            }
        });
    }
}

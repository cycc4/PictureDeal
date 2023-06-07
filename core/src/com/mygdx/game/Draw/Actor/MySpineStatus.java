package com.mygdx.game.Draw.Actor;

import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;

public class MySpineStatus implements Cloneable {

    //data
    public SkeletonData skeletondata;

    //status
    public AnimationStateData animationStateData;
    public Skeleton skeleton;
    public AnimationState animationState;

    public float spineOffsetX, spineOffsetY;
    public float scaleOriginX, scaleOriginY;

    public boolean isUpdateManaged = false;

    public MySpineStatus() {
    }

    public MySpineStatus(SkeletonData data, float spineOffsetX, float spineOffsetY) {
        this.spineOffsetX = spineOffsetX;
        this.spineOffsetY = spineOffsetY;
        setSkeletonData(data);
    }

    public MySpineStatus(SkeletonData data) {
        setSkeletonData(data);
    }

    public void setSkeletonData(SkeletonData data) {
        skeletondata = data;
        skeleton = new Skeleton(skeletondata);
        animationStateData = new AnimationStateData(data);
        animationState = new AnimationState(animationStateData);

        scaleOriginX = skeleton.getRootBone().getScaleX();
        scaleOriginY = skeleton.getRootBone().getScaleY();

    }

    public MySpineStatus reset() {
        if (skeleton != null)
            skeleton.getRootBone().setScale(scaleOriginX, scaleOriginY);
        return this;
    }

    public MySpineStatus cloneX() {
        return new MySpineStatus(skeletondata, spineOffsetX, spineOffsetY);
    }

}

package com.mygdx.game.Draw.Actor;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.Slot;
import com.esotericsoftware.spine.attachments.RegionAttachment;

public class MySpineGroup extends Group {
    protected MySpineActor actor;

    public MySpineGroup(SkeletonData skeletonData, SkeletonRenderer renderer) {
        actor = new MySpineActor(skeletonData, renderer);
        addActor(actor);
    }

    public int getAnimationIndex(String aniName) {
        return actor.getAnimationIndex(aniName);
    }

    public void playDefault() {
        actor.playDefault();
    }


    public void play(String animationName) {
        actor.play(animationName, false);
    }

    public void play(String animationName, boolean isLoop) {
        actor.play(animationName, isLoop);
    }

    public void play(String animationName, String skinName, boolean isLoop) {
        actor.play(animationName, skinName, isLoop);
    }

    public boolean play(int index, boolean isLoop) {
        Array<Animation> animations = actor.getSkeleton().getData().getAnimations();
        if (index < 0 || index >= animations.size)
            index = 0;
        actor.play(animations.get(index).getName(), isLoop);
        return true;
    }

    public void resetAnimation() {
        actor.resetAnimation();
    }

    public MySpineActor getSpineActor() {
        return actor;
    }

    public void stop() {
        actor.stop();
    }

    public void addAnimationListener(AnimationState.AnimationStateListener listener) {
        this.addAnimationListener(listener, true);
    }

    public void addAnimationListener(AnimationState.AnimationStateListener listener, boolean clearOthers) {
        if (clearOthers) actor.getAnimationState().clearListeners();
        actor.getAnimationState().addListener(listener);
    }


    public Group replace(String slotName, Actor actor) {
        return replace(slotName, actor, 0, 0, 1);
    }

    public Group replace(String slotName, Actor actor, float offsetX, float offsetY) {
        return replace(slotName, actor, offsetX, offsetY, 1);
    }

    public Group replace(String slotName, Actor actor, final float offsetX, final float offsetY, final float baseScale) {
        Actor a = findActor(slotName);
        if (a != null) {
            a.remove();
        }

        //将actor居中
        actor.setPosition(0, 0, Align.center);
        actor.setOrigin(Align.center);

        Skeleton skeleton = this.actor.getSkeleton();
        final Slot slot = skeleton.findSlot(slotName);
        final Bone bone = slot == null ? skeleton.findBone(slotName) : slot.getBone();
        final Bone bonePar = bone.getParent();
        actor.setName(slotName);

        final float rootScaleX = bone.getSkeleton().getRootBone().getScaleX();
        final float rootScaleY = bone.getSkeleton().getRootBone().getScaleY();


        final Vector2 posTemp = new Vector2();

        Group group = new Group();
        group.addAction(new Action() {
            @Override
            public boolean act(float v) {
                if (!bone.isActive()) {
                    actor.setVisible(false);
                    return false;
                }
                actor.setVisible(true);
                if (bonePar != null) {
                    Vector2 temp = bone.getParent().localToWorld(posTemp.set(bone.getX(), bone.getY()));
                    actor.setPosition(temp.x + offsetX, temp.y + offsetY, Align.center);
                    actor.setRotation(bone.getWorldRotationX());
                } else {
                    actor.setPosition(bone.getWorldX() + offsetX, bone.getWorldY() + offsetY, Align.center);
                    actor.setRotation(bone.localToWorldRotation(bone.getRotation()));
                }
                actor.setScale(baseScale * bone.getWorldScaleX() / rootScaleX, baseScale * bone.getWorldScaleY() / rootScaleY);
                if (slot != null)
                    actor.setColor(slot.getColor());
                return false;
            }
        });
        group.addActor(actor);
        addActor(group);
        return group;
    }

    public Image slotToImage(String slotName) {
        Skeleton skeleton = actor.getSkeleton();
        Slot slot = skeleton.findSlot(slotName);
        if (slot == null) return null;
        RegionAttachment regionAttachment = ((RegionAttachment) slot.getAttachment());
        TextureAtlas.AtlasRegion tempRegion = (TextureAtlas.AtlasRegion) regionAttachment.getRegion();
        Sprite sprite = new TextureAtlas.AtlasSprite(tempRegion);
//        if(tempRegion.rotate){
//            sprite.rotate90(true);
//        }
        Image image = new Image(new SpriteDrawable(sprite));
        replaceHard(slotName, image);
        return image;
    }

    public void replaceHard(String slotName, Actor actor) {
        replaceHard(slotName, actor, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
    }

    public Group replaceHard(String slotName, Actor actor, float offsetX, float offsetY) {
        if (findActor(slotName) != null) return null;

        Skeleton skeleton = this.actor.getSkeleton();
        Slot slot = skeleton.findSlot(slotName);
        RegionAttachment region = (RegionAttachment) slot.getAttachment();
        slot.setAttachment(null);
        actor.setName(slotName);
        float realScaleX;
        float realScaleY;
        TextureAtlas.AtlasRegion tmpRegion = (TextureAtlas.AtlasRegion) region.getRegion();
        if (tmpRegion.rotate) {
            realScaleX = region.getWidth() / tmpRegion.getRegionHeight();
            realScaleY = region.getHeight() / tmpRegion.getRegionWidth();
        } else {
            realScaleX = region.getWidth() / tmpRegion.getRegionWidth();
            realScaleY = region.getHeight() / tmpRegion.getRegionHeight();
        }

        this.actor.getSkeleton().updateWorldTransform();
        if (actor instanceof Image) {
            actor.setSize(region.getWidth(), region.getHeight());
        } else if (actor instanceof Label) {
            Label label = (Label) actor;
            float fontScaleX = label.getFontScaleX();
            float fontScaleY = label.getFontScaleY();
            label.setFontScale(fontScaleX * realScaleX, fontScaleY * realScaleY);
            actor.setSize(region.getWidth(), region.getHeight());
        } else if (actor instanceof Group) {
            actor.setScale(realScaleX, realScaleY);
        } else {
            throw new RuntimeException("该类型尚未实现Actor大小重新设置。");
        }

        //将actor居中
        actor.setPosition(0, 0, Align.center);
        actor.setOrigin(Align.center);

        Group group = new Group();
        if (offsetX != Float.NEGATIVE_INFINITY && offsetY != Float.NEGATIVE_INFINITY) {
            group.addAction(new SlotAction(slot, offsetX, offsetY));
        } else {
            group.addAction(new SlotAction(slot, region.getX() / realScaleX, region.getY() / realScaleY));
        }
        group.addActor(actor);
        addActor(group);
        return group;
    }

    public Group replaceWithSpine(String slotName, MySpineGroup actor, String actorSlotName) {
        return replaceWithSpine(slotName, actor, actorSlotName, new Vector2(), true);
    }

    public Group replaceWithSpine(String slotName, MySpineGroup actor, String actorSlotName, Vector2 offset) {
        return replaceWithSpine(slotName, actor, actorSlotName, offset, true);
    }

    public Group replaceWithSpine(String slotName, MySpineGroup actor, String actorSlotName, final Vector2 offset, final boolean isCheckAttach) {
        if (findActor(slotName) != null) return null;

        Skeleton skeleton = this.actor.getSkeleton();
        final Slot slot = skeleton.findSlot(slotName);
        final Bone bone = slot == null ? skeleton.findBone(slotName) : slot.getBone();
        actor.setName(slotName);

        Skeleton actorSkeleton = actor.getSpineActor().getSkeleton();
        Slot actorSlot = actorSkeleton.findSlot(actorSlotName);
//
        actorSkeleton.updateWorldTransform();
        RegionAttachment regionAttachment = (RegionAttachment) actorSlot.getAttachment();
        actor.setSize(
                regionAttachment.getWidth() * actorSlot.getBone().getWorldScaleX(),
                regionAttachment.getHeight() * actorSlot.getBone().getWorldScaleY()
        );
        actor.setVisible(bone.isActive() && slot != null && slot.getAttachment() != null);

        skeleton.updateWorldTransform();
        final float boneScaleX = bone.getWorldScaleX();
        final float boneScaleY = bone.getWorldScaleY();

        Group group = new Group();
        group.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                if (!bone.isActive() || slot != null && isCheckAttach && slot.getAttachment() == null) {
                    actor.setVisible(false);
                    return false;
                }
                actor.setVisible(true);
                actor.setPosition(bone.getWorldX() + offset.x, bone.getWorldY() + offset.y, Align.center);
                actor.setRotation(bone.getWorldRotationX());
                actor.setScale(bone.getWorldScaleX() / boneScaleX, bone.getWorldScaleY() / boneScaleY);
                if (slot != null)
                    actor.setColor(slot.getColor());
                return false;
            }
        });
        //addBoneSyncAction(group,bone);
        group.addActor(actor);
        addActor(group);
        return group;
    }

    public Group replaceWithSpineHard(String slotName, MySpineGroup actor, String actorSlotName) {
        if (findActor(slotName) != null) return null;

        Skeleton skeleton = this.actor.getSkeleton();
        final Slot slot = skeleton.findSlot(slotName);
        final Bone bone = slot.getBone();
        RegionAttachment region = (RegionAttachment) slot.getAttachment();

        Skeleton actorSkeleton = actor.getSpineActor().getSkeleton();
        Slot actorSlot = actorSkeleton.findSlot(actorSlotName);
        Bone actorBone = actorSlot.getBone();
        RegionAttachment actorRegion = (RegionAttachment) actorSlot.getAttachment();
        TextureAtlas.AtlasRegion tmpRegion = (TextureAtlas.AtlasRegion) actorRegion.getRegion();

        skeleton.updateWorldTransform();
        actorSkeleton.updateWorldTransform();

        float realActorWidth = actorRegion.getWidth() * actorBone.getWorldScaleX();
        float realActorHeight = actorRegion.getHeight() * actorBone.getWorldScaleY();
        actor.setSize(realActorWidth, realActorHeight);
        actor.setVisible(bone.isActive() && slot != null && slot.getAttachment() != null);
        final float rootScaleX = bone.getSkeleton().getRootBone().getScaleX();
        final float rootScaleY = bone.getSkeleton().getRootBone().getScaleY();
        Group group = new Group();
        group.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                if (!bone.isActive()) {
                    actor.setVisible(false);
                    return false;
                }
                actor.setVisible(true);
                actor.setPosition(bone.getWorldX(), bone.getWorldY(), Align.center);
                actor.setRotation(bone.localToWorldRotation(bone.getRotation()));
                actor.setScale(bone.getWorldScaleX() / rootScaleX, bone.getWorldScaleY() / rootScaleY);
                if (slot != null)
                    actor.setColor(slot.getColor());
                return false;
            }
        });
        //addBoneSyncAction(group,bone);
        group.addActor(actor);
        addActor(group);

        slot.setAttachment(null);
        return group;
    }

    public void clearAppendActor() {
        for (Actor child : getChildren()) {
            if (child != actor) child.remove();
        }
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        if (touchable && getTouchable() != Touchable.enabled) {
            return null;
        } else if (!this.isVisible()) {
            return null;
        } else {
            Vector2 point = new Vector2();
            Actor[] childrenArray = (Actor[]) getChildren().items;

            for (int i = getChildren().size - 1; i >= 0; --i) {
                Actor child = childrenArray[i];
                child.parentToLocalCoordinates(point.set(x, y));
                Actor hit = child.hit(point.x, point.y, touchable);
                if (hit != null) {
                    return hit;
                }
            }
            x += getWidth() / 2;
            y += getHeight() / 2;
            return x >= 0.0F && x < getWidth() && y >= 0.0F && y < getHeight() ? this : null;
        }
    }

    public static class SlotAction extends Action {
        float offsetX;
        float offsetY;
        Bone bone;
        Slot slot;

        public SlotAction(Slot slot) {
            this(slot, 0, 0);
        }

        public SlotAction(Slot slot, float offsetX, float offsetY) {
            this.slot = slot;
            this.bone = slot.getBone();
            this.offsetX = offsetX;
            this.offsetY = offsetY;
        }

        @Override
        public boolean act(float v) {
            if (!bone.isActive()) {
                actor.setVisible(false);
                return false;
            }

            actor.setVisible(true);
            actor.setPosition(bone.getWorldX() + offsetX, bone.getWorldY() + offsetY, Align.center);
            actor.setRotation(bone.getWorldRotationX());
            actor.setScale(bone.getWorldScaleX(), bone.getWorldScaleY());
            actor.setColor(slot.getColor());
            return false;
        }
    }
}
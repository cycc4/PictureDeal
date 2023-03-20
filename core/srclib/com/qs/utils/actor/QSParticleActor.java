package com.qs.utils.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.qs.utils.encryption.CipherParticleEffect;

public class QSParticleActor extends Actor {
    public float timescale = 1;
    public CipherParticleEffect particleEffect;

    public QSParticleActor(CipherParticleEffect particleEffect) {
        this(particleEffect, true);
    }

    public QSParticleActor(CipherParticleEffect particleEffect, boolean start) {

        this.particleEffect = particleEffect;
        if (start)
            particleEffect.start();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (isVisible()) {
            particleEffect.setPosition(getX(), getY());
            particleEffect.update(delta * timescale);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        int blendDstFunc = batch.getBlendDstFunc();
        int blendSrcFunc = batch.getBlendSrcFunc();

        particleEffect.draw(batch);
        batch.setBlendFunction(blendSrcFunc, blendDstFunc);
    }

    public CipherParticleEffect getParticleEffect() {
        return particleEffect;
    }
}

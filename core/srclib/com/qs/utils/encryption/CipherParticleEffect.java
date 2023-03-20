package com.qs.utils.encryption;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.StreamUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;

/**
 * @Date 2022/7/5 15:07
 * @Description: TODO
 */
public class CipherParticleEffect implements Disposable {
    private final Array<ParticleEmitter> emitters;
    private BoundingBox bounds;
    private boolean ownsTexture;
    protected float xSizeScale = 1.0F;
    protected float ySizeScale = 1.0F;
    protected float motionScale = 1.0F;

    public CipherParticleEffect() {
        this.emitters = new Array(8);
    }

    public CipherParticleEffect(CipherParticleEffect effect) {
        this.emitters = new Array(true, effect.emitters.size);
        int i = 0;

        for (int n = effect.emitters.size; i < n; ++i) {
            this.emitters.add(this.newEmitter((ParticleEmitter) effect.emitters.get(i)));
        }

    }

    public void start() {
        int i = 0;

        for (int n = this.emitters.size; i < n; ++i) {
            ((ParticleEmitter) this.emitters.get(i)).start();
        }

    }

    public void reset() {
        this.reset(true);
    }

    public void reset(boolean resetScaling) {
        int i = 0;

        for (int n = this.emitters.size; i < n; ++i) {
            ((ParticleEmitter) this.emitters.get(i)).reset();
        }

        if (resetScaling && (this.xSizeScale != 1.0F || this.ySizeScale != 1.0F || this.motionScale != 1.0F)) {
            this.scaleEffect(1.0F / this.xSizeScale, 1.0F / this.ySizeScale, 1.0F / this.motionScale);
            this.xSizeScale = this.ySizeScale = this.motionScale = 1.0F;
        }

    }

    public void update(float delta) {
        int i = 0;

        for (int n = this.emitters.size; i < n; ++i) {
            ((ParticleEmitter) this.emitters.get(i)).update(delta);
        }

    }

    public void draw(Batch spriteBatch) {
        int i = 0;

        for (int n = this.emitters.size; i < n; ++i) {
            ((ParticleEmitter) this.emitters.get(i)).draw(spriteBatch);
        }

    }

    public void draw(Batch spriteBatch, float delta) {
        int i = 0;

        for (int n = this.emitters.size; i < n; ++i) {
            ((ParticleEmitter) this.emitters.get(i)).draw(spriteBatch, delta);
        }

    }

    public void allowCompletion() {
        int i = 0;

        for (int n = this.emitters.size; i < n; ++i) {
            ((ParticleEmitter) this.emitters.get(i)).allowCompletion();
        }

    }

    public boolean isComplete() {
        int i = 0;

        for (int n = this.emitters.size; i < n; ++i) {
            ParticleEmitter emitter = (ParticleEmitter) this.emitters.get(i);
            if (!emitter.isComplete()) {
                return false;
            }
        }

        return true;
    }

    public void setDuration(int duration) {
        int i = 0;

        for (int n = this.emitters.size; i < n; ++i) {
            ParticleEmitter emitter = (ParticleEmitter) this.emitters.get(i);
            emitter.setContinuous(false);
            emitter.duration = (float) duration;
            emitter.durationTimer = 0.0F;
        }

    }

    public void setPosition(float x, float y) {
        int i = 0;

        for (int n = this.emitters.size; i < n; ++i) {
            ((ParticleEmitter) this.emitters.get(i)).setPosition(x, y);
        }

    }

    public void setFlip(boolean flipX, boolean flipY) {
        int i = 0;

        for (int n = this.emitters.size; i < n; ++i) {
            ((ParticleEmitter) this.emitters.get(i)).setFlip(flipX, flipY);
        }

    }

    public void flipY() {
        int i = 0;

        for (int n = this.emitters.size; i < n; ++i) {
            ((ParticleEmitter) this.emitters.get(i)).flipY();
        }

    }

    public Array<ParticleEmitter> getEmitters() {
        return this.emitters;
    }

    public ParticleEmitter findEmitter(String name) {
        int i = 0;

        for (int n = this.emitters.size; i < n; ++i) {
            ParticleEmitter emitter = (ParticleEmitter) this.emitters.get(i);
            if (emitter.getName().equals(name)) {
                return emitter;
            }
        }

        return null;
    }

//    public void preAllocateParticles() {
//        Array.ArrayIterator var1 = (Array.ArrayIterator) this.emitters.iterator();
//
//        while (var1.hasNext()) {
//            ParticleEmitter emitter = (ParticleEmitter) var1.next();
//            emitter.preAllocateParticles();
//        }
//
//    }

    public void save(Writer output) throws IOException {
        int index = 0;
        int i = 0;

        for (int n = this.emitters.size; i < n; ++i) {
            ParticleEmitter emitter = (ParticleEmitter) this.emitters.get(i);
            if (index++ > 0) {
                output.write("\n");
            }

            emitter.save(output);
        }

    }

    public void load(FileHandle effectFile, FileHandle imagesDir) {
        this.loadEmitters(effectFile);
        this.loadEmitterImages(imagesDir);
    }

    public void load(FileHandle effectFile, TextureAtlas atlas) {
        this.load(effectFile, atlas, (String) null);
    }

    public void load(FileHandle effectFile, TextureAtlas atlas, String atlasPrefix) {
        this.loadEmitters(effectFile);
        this.loadEmitterImages(atlas, atlasPrefix);
    }

    public void loadEmitters(FileHandle effectFile) {
        InputStream input = effectFile.read();
        this.emitters.clear();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(input), 512);

            do {
                ParticleEmitter emitter = this.newEmitter(reader);
                this.emitters.add(emitter);
            } while (reader.readLine() != null);
        } catch (IOException var8) {
            throw new GdxRuntimeException("Error loading effect: " + effectFile, var8);
        } finally {
            StreamUtils.closeQuietly(reader);
        }

    }

    public void loadEmitterImages(TextureAtlas atlas) {
        this.loadEmitterImages(atlas, (String) null);
    }

    public void loadEmitterImages(TextureAtlas atlas, String atlasPrefix) {
        int i = 0;

        for (int n = this.emitters.size; i < n; ++i) {
            ParticleEmitter emitter = (ParticleEmitter) this.emitters.get(i);
            if (emitter.getImagePaths().size != 0) {
                Array<Sprite> sprites = new Array();
                Array.ArrayIterator var7 = (Array.ArrayIterator) emitter.getImagePaths().iterator();

                while (var7.hasNext()) {
                    String imagePath = (String) var7.next();
                    String imageName = (new File(imagePath.replace('\\', '/'))).getName();
                    int lastDotIndex = imageName.lastIndexOf(46);
                    if (lastDotIndex != -1) {
                        imageName = imageName.substring(0, lastDotIndex);
                    }

                    if (atlasPrefix != null) {
                        imageName = atlasPrefix + imageName;
                    }

                    Sprite sprite = atlas.createSprite(imageName);
                    if (sprite == null) {
                        throw new IllegalArgumentException("SpriteSheet missing image: " + imageName);
                    }
                    sprite.setSize(sprite.getRegionWidth(), sprite.getRegionHeight());

                    sprites.add(sprite);
                }

                emitter.setSprites(sprites);
            }
        }

    }

    public void loadEmitterImages(FileHandle imagesDir) {
        this.ownsTexture = true;
        ObjectMap<String, Sprite> loadedSprites = new ObjectMap(this.emitters.size);
        int i = 0;

        for (int n = this.emitters.size; i < n; ++i) {
            ParticleEmitter emitter = (ParticleEmitter) this.emitters.get(i);
            if (emitter.getImagePaths().size != 0) {
                Array<Sprite> sprites = new Array();

                Sprite sprite;
                for (Array.ArrayIterator var7 = (Array.ArrayIterator) emitter.getImagePaths().iterator(); var7.hasNext(); sprites.add(sprite)) {
                    String imagePath = (String) var7.next();
                    String imageName = (new File(imagePath.replace('\\', '/'))).getName();
                    sprite = (Sprite) loadedSprites.get(imageName);
                    if (sprite == null) {
                        sprite = new Sprite(this.loadTexture(imagesDir.child(imageName)));
                        loadedSprites.put(imageName, sprite);
                    }
                }

                emitter.setSprites(sprites);
            }
        }

    }

    protected ParticleEmitter newEmitter(BufferedReader reader) throws IOException {
        return new ParticleEmitter(reader);
    }

    protected ParticleEmitter newEmitter(ParticleEmitter emitter) {
        return new ParticleEmitter(emitter);
    }

    protected CipherTexture loadTexture(FileHandle file) {
        return new CipherTexture(file, false);
    }

    public void dispose() {
        if (this.ownsTexture) {
            int i = 0;

            for (int n = this.emitters.size; i < n; ++i) {
                ParticleEmitter emitter = (ParticleEmitter) this.emitters.get(i);
                Array.ArrayIterator var4 = (Array.ArrayIterator) emitter.getSprites().iterator();

                while (var4.hasNext()) {
                    Sprite sprite = (Sprite) var4.next();
                    sprite.getTexture().dispose();
                }
            }

        }
    }

    public BoundingBox getBoundingBox() {
        if (this.bounds == null) {
            this.bounds = new BoundingBox();
        }

        BoundingBox bounds = this.bounds;
        bounds.inf();
        Array.ArrayIterator var2 = (Array.ArrayIterator) this.emitters.iterator();

        while (var2.hasNext()) {
            ParticleEmitter emitter = (ParticleEmitter) var2.next();
            bounds.ext(emitter.getBoundingBox());
        }

        return bounds;
    }

    public void scaleEffect(float scaleFactor) {
        this.scaleEffect(scaleFactor, scaleFactor, scaleFactor);
    }

    public void scaleEffect(float scaleFactor, float motionScaleFactor) {
        this.scaleEffect(scaleFactor, scaleFactor, motionScaleFactor);
    }

    public void scaleEffect(float xSizeScaleFactor, float ySizeScaleFactor, float motionScaleFactor) {
        this.xSizeScale *= xSizeScaleFactor;
        this.ySizeScale *= ySizeScaleFactor;
        this.motionScale *= motionScaleFactor;
        Array.ArrayIterator var4 = (Array.ArrayIterator) this.emitters.iterator();

        while (var4.hasNext()) {
            ParticleEmitter particleEmitter = (ParticleEmitter) var4.next();
            particleEmitter.scaleSize(xSizeScaleFactor, ySizeScaleFactor);
            particleEmitter.scaleMotion(motionScaleFactor);
        }

    }

    public void setEmittersCleanUpBlendFunction(boolean cleanUpBlendFunction) {
        int i = 0;

        for (int n = this.emitters.size; i < n; ++i) {
            ((ParticleEmitter) this.emitters.get(i)).setCleansUpBlendFunction(cleanUpBlendFunction);
        }

    }
}

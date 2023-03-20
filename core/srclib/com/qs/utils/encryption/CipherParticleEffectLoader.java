package com.qs.utils.encryption;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

/**
 * @Date 2022/7/5 14:22
 * @Description: TODO
 */
public class CipherParticleEffectLoader extends SynchronousAssetLoader<CipherParticleEffect, CipherParticleEffectLoader.ParticleEffectParameter> {
    public CipherParticleEffectLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    public CipherParticleEffect load(AssetManager am, String fileName, FileHandle file, CipherParticleEffectLoader.ParticleEffectParameter param) {
        CipherParticleEffect effect = new CipherParticleEffect();
        if (param != null && param.atlasFile != null) {
            effect.load(file, (TextureAtlas) am.get(param.atlasFile, TextureAtlas.class), param.atlasPrefix);
        } else if (param != null && param.imagesDir != null) {
            effect.load(file, param.imagesDir);
        } else {
            effect.load(file, file.parent());
        }

        return effect;
    }

    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, CipherParticleEffectLoader.ParticleEffectParameter param) {
        Array<AssetDescriptor> deps = null;
        if (param != null && param.atlasFile != null) {
            deps = new Array();
            deps.add(new AssetDescriptor(param.atlasFile, TextureAtlas.class));
        }

        return deps;
    }

    public static class ParticleEffectParameter extends AssetLoaderParameters<CipherParticleEffect> {
        public String atlasFile;
        public String atlasPrefix;
        public FileHandle imagesDir;

        public ParticleEffectParameter() {
        }
    }
}

package com.qs.utils.mini;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.qs.utils.encryption.CipherTextureLoader;

import java.io.IOException;

public class MiniTextureLoader extends AsynchronousAssetLoader<Texture, TextureLoader.TextureParameter> {
    static public class TextureLoaderInfo {
        String filename;
        MiniFileTextureData data;
        Texture texture;
    }

    final float scale;

    TextureLoaderInfo info = new TextureLoaderInfo();

    public MiniTextureLoader(FileHandleResolver resolver) {
        this(resolver, 0.5f);
    }

    public MiniTextureLoader(FileHandleResolver resolver, float scale) {
        super(resolver);
        this.scale = scale;
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, TextureLoader.TextureParameter parameter) {
        info.filename = fileName;
        Pixmap preloadedPixmap = null;
        byte[] bytes = null;
        if (parameter == null || parameter.textureData == null) {
            Format format = null;
            info.texture = null;

            if (parameter != null) {
                format = parameter.format;
                info.texture = parameter.texture;
            }
            //若加密则会读取失败，在catch中解密
            try {
                bytes = file.readBytes();
                preloadedPixmap = new Pixmap(new Gdx2DPixmap(bytes, 0, bytes.length, 0));
            } catch (Exception var3) {
                CipherTextureLoader.cipher(bytes);
                try {
                    preloadedPixmap = new Pixmap(new Gdx2DPixmap(bytes, 0, bytes.length, 0));
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new GdxRuntimeException("Couldn't load file: " + file, var3);
                }
            }
            preloadedPixmap.setBlending(Pixmap.Blending.SourceOver);
            preloadedPixmap.setFilter(Pixmap.Filter.BiLinear);
            preloadedPixmap.setColor(0);
            info.data = new MiniFileTextureData(file, preloadedPixmap, format, scale);
        } else {
            throw new GdxRuntimeException("干他妈的孔亚通");
        }
//        if (!info.data.isPrepared())
//            info.data.prepare();
    }

    @Override
    public Texture loadSync(AssetManager manager, String fileName, FileHandle file, TextureLoader.TextureParameter parameter) {
        if (info == null)
            return null;
        Texture texture = info.texture;
        if (texture != null) {
            texture.load(info.data);
        } else {
            texture = new Texture(info.data);
        }
        if (parameter != null) {
            texture.setFilter(parameter.minFilter, parameter.magFilter);
            texture.setWrap(parameter.wrapU, parameter.wrapV);
        }
        return texture;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, TextureLoader.TextureParameter parameter) {
        return null;
    }

}

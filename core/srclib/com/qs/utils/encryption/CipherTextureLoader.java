package com.qs.utils.encryption;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.IOException;

/**
 * @Date 2022/7/4 15:24
 * @Description: TODO
 */
public class CipherTextureLoader extends AsynchronousAssetLoader<Texture, TextureLoader.TextureParameter> {

    static public class TextureLoaderInfo {
        String filename;
        TextureData data;
        Texture texture;
    }


    CipherTextureLoader.TextureLoaderInfo info = new CipherTextureLoader.TextureLoaderInfo();

    public CipherTextureLoader(FileHandleResolver resolver) {
        super(resolver);
    }


    public void loadAsync(AssetManager manager, String fileName, FileHandle file, TextureLoader.TextureParameter parameter) {
        this.info.filename = fileName;
        Pixmap preloadedPixmap = null;
        byte[] bytes = null;
        if (parameter != null && parameter.textureData != null) {
            this.info.data = parameter.textureData;
            this.info.texture = parameter.texture;
        } else {
            Pixmap.Format format = null;
            boolean genMipMaps = false;
            this.info.texture = null;
            if (parameter != null) {
                format = parameter.format;
                genMipMaps = parameter.genMipMaps;
                this.info.texture = parameter.texture;
            }
            //若加密则会读取失败，在catch中解密
            try {
                bytes = file.readBytes();
                preloadedPixmap = new Pixmap(new Gdx2DPixmap(bytes, 0, bytes.length, 0));
            } catch (Exception var3) {
                cipher(bytes);
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
            this.info.data = new FileTextureData(file, preloadedPixmap, format, genMipMaps);
        }

//        if (!this.info.data.isPrepared()) {
//            this.info.data.prepare();
//        }

    }


    public Texture loadSync(AssetManager manager, String fileName, FileHandle file, TextureLoader.TextureParameter parameter) {
        if (this.info == null) {
            return null;
        } else {
            Texture texture = this.info.texture;
            if (texture != null) {
                texture.load(this.info.data);
            } else {
                texture = new Texture(this.info.data);
            }

            if (parameter != null) {
                texture.setFilter(parameter.minFilter, parameter.magFilter);
                texture.setWrap(parameter.wrapU, parameter.wrapV);
            }

            return texture;
        }
    }


    public Array<AssetDescriptor> getDependencies(String s, FileHandle fileHandle, TextureLoader.TextureParameter textureParameter) {
        return null;
    }

    public static void cipher(byte[] bytes) {
        String key = "doodle";//密码
        try {
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) (bytes[i] ^ key.hashCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.qs.utils.encryption;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Date 2022/7/5 14:41
 * @Description: TODO
 */
public class CipherTexture extends Texture {
    private static AssetManager assetManager;
    static final Map<Application, Array<Texture>> managedTextures = new HashMap();

    public CipherTexture(String internalPath) {
        this(Gdx.files.internal(internalPath));
    }
    public CipherTexture(FileHandle file) {
        this((FileHandle)file, (Pixmap.Format)null, false);
    }
    public CipherTexture(FileHandle file, Pixmap.Format format, boolean useMipMaps) {
        this(load(file, format, useMipMaps));
    }
    public CipherTexture(TextureData load) {
        super(load);
    }

    public CipherTexture(FileHandle file, boolean useMipMaps) {
        this((FileHandle)file, (Pixmap.Format)null, useMipMaps);
    }

    public static TextureData load(FileHandle file, Pixmap.Format format, boolean useMipMaps) {
        byte[] bytes = null;
        Pixmap preloadedPixmap = null;
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
        FileTextureData fileTextureData = new FileTextureData(file, preloadedPixmap, format, useMipMaps);
        return fileTextureData;
    }
}

package com.qs.utils.assets;

import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.qs.ui.plist.PlistAtlasLoader;

public class AssetsParameters {
    public static TextureLoader.TextureParameter textureParameter = new TextureLoader.TextureParameter() {
        {
            minFilter = Texture.TextureFilter.Linear;
            magFilter = Texture.TextureFilter.Linear;
            wrapU = Texture.TextureWrap.ClampToEdge;
            wrapV = Texture.TextureWrap.ClampToEdge;
        }
    };

    public static TextureLoader.TextureParameter uiTextureParameter = new TextureLoader.TextureParameter() {
        {
            minFilter = Texture.TextureFilter.MipMapLinearLinear;
            magFilter = Texture.TextureFilter.Linear;
            genMipMaps = true;
            wrapU = Texture.TextureWrap.ClampToEdge;
            wrapV = Texture.TextureWrap.ClampToEdge;
        }
    };

    public static PlistAtlasLoader.PlistAtlasParameter uiPlistAtlasParameter = new PlistAtlasLoader.PlistAtlasParameter() {
        {
            minFilter = Texture.TextureFilter.MipMapLinearLinear;
            magFilter = Texture.TextureFilter.Linear;
            genMipMaps = true;
            wrapU = Texture.TextureWrap.ClampToEdge;
            wrapV = Texture.TextureWrap.ClampToEdge;
        }
    };

    public static BitmapFontLoader.BitmapFontParameter uiBitmapFontParameter = new BitmapFontLoader.BitmapFontParameter() {
        {
            minFilter = Texture.TextureFilter.MipMapLinearLinear;
            magFilter = Texture.TextureFilter.Linear;
            genMipMaps = true;
        }
    };
}

package com.qs.utils.assets;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.qs.ui.ManagerUIEditor;
import com.qs.ui.loader.ManagerUILoader;
import com.qs.ui.plist.PlistAtlas;
import com.qs.ui.plist.PlistAtlasLoader;

public class Assets implements Disposable {
    AssetManager assetManager;
    Array<String> assetArray;
    BitmapFont dfft;
    private static Assets _ins;

    private Assets() {
        assetManager = new AssetManager();
        assetArray = new Array<>();
        assetManager.setLoader(ManagerUIEditor.class, new ManagerUILoader(assetManager.getFileHandleResolver()));
        assetManager.setLoader(PlistAtlas.class, new PlistAtlasLoader(assetManager.getFileHandleResolver()));

        dfft = new BitmapFont();
        dfft.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        assetManager.setLoader(BitmapFont.class, new BitmapFontLoader(assetManager.getFileHandleResolver()) {
//            @Override
//            public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, BitmapFontParameter parameter) {
//                System.out.println("pppppppppppppppppppppppppppppppppppppxx:  "+fileName +"   "+file.file().getAbsolutePath());
//                return super.getDependencies(fileName, file, parameter);
//            }
//
//            @Override
//            public BitmapFont loadSync(AssetManager manager, String fileName, FileHandle file, BitmapFontParameter parameter) {
//                System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz xx:  " + fileName + "    " + file.file().getAbsolutePath());
//                return super.loadSync(manager, fileName, file, parameter);
//            }
        });
    }

    public static Assets getIns() {
        if (_ins == null) {
            _ins = new Assets();
        }
        return _ins;
    }

    public TextureAtlas getTextureAtlas(String textureAtlasName) {
        return LoadRes(textureAtlasName, TextureAtlas.class);
    }

    public Texture getTexture(String textureName) {
        return LoadRes(textureName, Texture.class, textureParameter);
    }

    public BitmapFont getBitmapFont(String bitmapFontName) {
        return LoadRes(bitmapFontName, BitmapFont.class, bitmapFontParameter);
    }

    public <T> T LoadRes(String fileName, Class<T> type) {
        return LoadRes(fileName, type);
    }

    public <T> T LoadRes(String fileName, Class<T> type, AssetLoaderParameters<T> parameter) {
        return LoadRes(fileName, assetManager, type, parameter);
    }

    public <T> T LoadRes(String fileName, AssetManager manager, Class<T> type, AssetLoaderParameters<T> parameter) {
        if (!manager.isLoaded(fileName, type)) {
            assetArray.add(fileName);
            if (parameter == null) {
                manager.load(fileName, type);
            } else {
                manager.load(fileName, type, parameter);
            }
            manager.finishLoading();
        }
        return manager.get(fileName, type);
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public static TextureLoader.TextureParameter textureParameter = new TextureLoader.TextureParameter() {
        {
            minFilter = Texture.TextureFilter.Nearest;
            magFilter = Texture.TextureFilter.Linear;
            wrapU = Texture.TextureWrap.ClampToEdge;
            wrapV = Texture.TextureWrap.ClampToEdge;
        }
    };

    public static TextureLoader.TextureParameter mipmapTextureParameter = new TextureLoader.TextureParameter() {
        {
            minFilter = Texture.TextureFilter.MipMapLinearLinear;
            magFilter = Texture.TextureFilter.Linear;
            genMipMaps = true;
            wrapU = Texture.TextureWrap.ClampToEdge;
            wrapV = Texture.TextureWrap.ClampToEdge;
        }
    };

//    public static PlistAtlasLoader.PlistAtlasParameter uiPlistAtlasParameter = new PlistAtlasLoader.PlistAtlasParameter() {
//        {
//            minFilter = Texture.TextureFilter.MipMapLinearLinear;
//            magFilter = Texture.TextureFilter.Linear;
//            genMipMaps = true;
//            wrapU = Texture.TextureWrap.ClampToEdge;
//            wrapV = Texture.TextureWrap.ClampToEdge;
//        }
//    };

    public static BitmapFontLoader.BitmapFontParameter mipMapBitmapFontParameter = new BitmapFontLoader.BitmapFontParameter() {
        {
            minFilter = Texture.TextureFilter.MipMapLinearLinear;
            magFilter = Texture.TextureFilter.Linear;
            genMipMaps = true;
        }
    };

    public static BitmapFontLoader.BitmapFontParameter bitmapFontParameter = new BitmapFontLoader.BitmapFontParameter() {
        {
            minFilter = Texture.TextureFilter.Nearest;
            magFilter = Texture.TextureFilter.Linear;
            genMipMaps = false;
        }
    };

    public BitmapFont deffont() {
        return dfft;
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}

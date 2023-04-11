/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qs.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.qs.utils.assets.Assets;
import com.qs.ui.loader.ManagerUILoader;
import com.qs.ui.plist.PlistAtlas;
import com.qs.utils.assets.AssetsParameters;

import net.mwplay.cocostudio.ui.BaseCocoStudioUIEditor;
import net.mwplay.cocostudio.ui.model.FileData;
import net.mwplay.cocostudio.ui.model.ObjectData;

import java.util.HashSet;
import java.util.List;

public class ManagerUIEditor extends BaseCocoStudioUIEditor implements Disposable {
    private final String tag = ManagerUIEditor.class.getSimpleName();

    //cocos项目目录
    public String dirName;
    //cocos文件目录
    public String filedir;
    //新加载的资源
    public Array<String> unManagedLoad = new Array<>();
    //用到的assetmanager
    AssetManager assetManager;

    //    //默认图片过滤方式
//    TextureLoader.TextureParameter textureParameter = new TextureLoader.TextureParameter() {
//        {
//            minFilter = Texture.TextureFilter.MipMapLinearLinear;
//            magFilter = Texture.TextureFilter.Linear;
//            genMipMaps = true;
//            wrapU = Texture.TextureWrap.ClampToEdge;
//            wrapV = Texture.TextureWrap.ClampToEdge;
//        }
//    };
//
//    //默认bitmap过滤方式
//    BitmapFontLoader.BitmapFontParameter bitmapFontParameter = new BitmapFontLoader.BitmapFontParameter() {
//        {
//            minFilter = Texture.TextureFilter.MipMapLinearLinear;
//            magFilter = Texture.TextureFilter.MipMapLinearLinear;
//            genMipMaps = true;
//        }
//    };
    //销毁
    private boolean unloaded = false;

    //初始化函数
    public ManagerUIEditor(FileHandle jsonFile) {
        this(jsonFile, null);
    }

    //初始化函数
    public ManagerUIEditor(FileHandle jsonFile, String dirName) {
        this(jsonFile, dirName, null);
    }

    //初始化函数
    public ManagerUIEditor(FileHandle jsonFile, String dirName, AssetManager assetManager) {
        super(jsonFile);

        this.assetManager = assetManager;
        if (this.assetManager == null)
            this.assetManager = Assets.getIns().getAssetManager();

        this.dirName = dirName;
        if (this.dirName == null) {
            this.dirName = jsonFile.parent().toString();

            if (!this.dirName.equals("")) {
                this.dirName += "/";
            }
        }

        filedir = jsonFile.parent().toString();
        if (!filedir.equals("")) {
            filedir += "/";
        }
//        String basePath = jsonFile.parent().file().getAbsolutePath();
        List<String> resourceList = getResources();
        for (int i = 0; i < resourceList.size(); i++) {
            String fPath = filedir + resourceList.get(i);
//            String fPath = resourceList.get(i);
            FileHandle fileHandle = this.assetManager.getFileHandleResolver().resolve(fPath);
            if (!fileHandle.exists()) {
                System.out.println(jsonFile.name() + " dep-file not exist : " + fPath);
                resourceList.remove(i);
                i--;
            }
        }
        resourceSet.clear();
        resourceSet.addAll(resourceList);
    }

    HashSet<String> resourceSet = new HashSet<>();

    public String getRealPath(String path) {
        if (resourceSet.contains(path)) {
            return dirName + path;
        }
        return path;
    }

    //获取依赖资源
    public List<String> getResources() {
        return export.Content.Content.UsedResources;
    }

    //获取textureregion
    @Override
    public TextureRegion findTextureRegion(ObjectData option, FileData fileData) {
        if (fileData == null || fileData.Path.equals("")) {
            return null;
        }

        if (fileData.Plist == null || fileData.Plist.equals("")) {
            String texturepath = getRealPath(fileData.Path);
//            if (fileData.Path.startsWith(dirName))
//                texturepath = fileData.Path;
            if (!assetManager.isLoaded(texturepath)) {
                FileHandle fileHandle = assetManager.getFileHandleResolver().resolve(texturepath);
                if (fileHandle.exists()) {
                    assetManager.load(texturepath, Texture.class, AssetsParameters.uiTextureParameter);
                    assetManager.finishLoading();
                    unManagedLoad.add(texturepath);
                    Gdx.app.debug(tag, "unmanaged load " + texturepath);
                } else {
                    Gdx.app.error(tag, "texture file not exist: " + texturepath);
                    return null;
                }
            }
            return new TextureRegion(assetManager.get(texturepath, Texture.class));
        } else {
            String atlaspath = getRealPath(fileData.Plist);
//            if (fileData.Plist.startsWith(dirName))
//                atlaspath = fileData.Plist;
            if (atlaspath.endsWith(".plist")) {
                if (!assetManager.isLoaded(atlaspath)) {
                    assetManager.load(atlaspath, PlistAtlas.class);
                    assetManager.finishLoading();
                    unManagedLoad.add(atlaspath);
                    Gdx.app.debug(tag, "unmanaged load " + atlaspath);
                }
                PlistAtlas atlas = assetManager.get(atlaspath, PlistAtlas.class);
                String text = fileData.Path.replace(".png", "");
                return atlas.findRegion(text);

            } else {
                if (!assetManager.isLoaded(atlaspath)) {
                    assetManager.load(atlaspath, TextureAtlas.class);
                    assetManager.finishLoading();
                    unManagedLoad.add(atlaspath);
                    Gdx.app.debug(tag, "unmanaged load " + atlaspath);
                }
                TextureAtlas atlas = assetManager.get(atlaspath, TextureAtlas.class);
                String text = fileData.Path.replace(".png", "");
                return atlas.findRegion(text);
            }
        }
    }

    //获取drawable
    @Override
    public Drawable findDrawable(ObjectData option, FileData fileData) {
        //显示Default
        if (fileData == null) {// 默认值不显示
            return null;
        }

        TextureRegion textureRegion = findTextureRegion(option, fileData);

        if (textureRegion == null) {
            return null;
        }

        if (option.Scale9Enable) {// 九宫格支持
            NinePatch np = new NinePatch(textureRegion,
                    option.Scale9OriginX, textureRegion.getRegionWidth() - option.Scale9Width - option.Scale9OriginX,
                    option.Scale9OriginY, textureRegion.getRegionHeight() - option.Scale9Height - option.Scale9OriginY);
            return new NinePatchDrawable(np);
        }

        if (textureRegion instanceof TextureAtlas.AtlasRegion) {
            TextureAtlas.AtlasRegion atlasRegion = (TextureAtlas.AtlasRegion) textureRegion;
            TextureAtlas.AtlasSprite atlasSprite = new TextureAtlas.AtlasSprite(atlasRegion);
            return new SpriteDrawable(atlasSprite);
        } else {
            return new TextureRegionDrawable(textureRegion);
        }
    }

    //获取labelstyle
    @Override
    public LabelStyle createLabelStyle(ObjectData option, String text, Color color) {

        FileHandle fontFile = null;

        if (fontFile == null) {
            Gdx.app.debug(option.getClass().toString(), "ttf字体不存在,使用默认字体");
        }

        BitmapFont font;

        font = Assets.getIns().deffont();
        LabelStyle labelStyle = new LabelStyle(font, color);
        return new LabelStyle(labelStyle);
    }

    //获取字体资源
    @Override
    public BitmapFont findBitmapFont(FileData fileData) {
        if (fileData == null)
            return Assets.getIns().deffont();
        BitmapFont font;
        String bmPath = getRealPath(fileData.Path);
//        if (fileData.Path != null && fileData.Path.startsWith(dirName))
//            bmPath = fileData.Path;
        if (!assetManager.isLoaded(bmPath)) {
            assetManager.load(bmPath, BitmapFont.class, AssetsParameters.uiBitmapFontParameter);
            assetManager.finishLoading();
            unManagedLoad.add(bmPath);
            Gdx.app.debug(tag, "unmanaged load " + bmPath);
        }
        font = assetManager.get(bmPath, BitmapFont.class);
//        font.setUseIntegerPositions(false);
//        font.setFixedWidthGlyphs("0123456789");
        return font;
    }

    //获取字体
    @Override
    public BitmapFont createLabelStyleBitmapFont(ObjectData option, String text, Color color) {
        return Assets.getIns().deffont();
    }

    //获取依赖ui
    @Override
    public BaseCocoStudioUIEditor findCoco(FileData fileData) {
        ManagerUIEditor ui;
        String uipath = getRealPath(fileData.Path);
//        if (fileData.Path != null && fileData.Path.startsWith(dirName))
//            uipath = fileData.Path;
        if (!assetManager.isLoaded(uipath)) {
            assetManager.load(uipath, ManagerUIEditor.class, new ManagerUILoader.ManagerUIParameter(dirName, assetManager));
            assetManager.finishLoading();
            unManagedLoad.add(uipath);
            Gdx.app.debug(tag, "unmanaged load " + uipath);
        }
        ui = assetManager.get(uipath, ManagerUIEditor.class);

        return ui;
    }

    @Override
    public synchronized void dispose() {
        if (!unloaded) {
            unloaded = true;
            for (String unload : unManagedLoad)
                assetManager.unload(unload);
            unManagedLoad.clear();
        }
    }
}

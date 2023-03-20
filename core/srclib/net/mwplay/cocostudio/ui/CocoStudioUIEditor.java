///*
// * Copyright 2016 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package net.mwplay.cocostudio.ui;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.files.FileHandle;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.g2d.NinePatch;
//import com.badlogic.gdx.graphics.g2d.TextureAtlas;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
//import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
//import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
//import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
//import com.badlogic.gdx.utils.Json;
//import com.qs.ui.util.NUtils;
//import com.qs.utils.global.GlobalJson;
//
//import net.mwplay.cocostudio.ui.model.CCExport;
//import net.mwplay.cocostudio.ui.model.FileData;
//import net.mwplay.cocostudio.ui.model.ObjectData;
//
//import java.io.File;
//import java.util.Collection;
//import java.util.Map;
//
///**
// * CocoStudio ui 解析器.根据CocoStudio的ui编辑器生成的json文件,创建出一个对应Group.
// * 本解析器还处于初级阶段,部分控件与属性不支持.
// */
//public class CocoStudioUIEditor extends BaseCocoStudioUIEditor {
//
//    /**
//     * 创建LabelStyle
//     *
//     * @param option
//     * @return
//     */
//
//    /**
//     * json文件所在目录
//     */
//    public String dirName;
//
//    /**
//     * 控件集合
//     */
//
//    /**
//     * 所有纹理
//     */
//    protected Collection<TextureAtlas> textureAtlas;
//    /**
//     * 字体集合
//     */
//    protected Map<String, FileHandle> ttfs;
//    /**
//     * BitmapFont集合,key:font.fnt
//     */
//    protected Map<String, BitmapFont> bitmapFonts;
//    /**
//     * 默认ttf字体文件
//     */
//    protected FileHandle defaultFont;
//
//    /**
//     * 不需要显示文字
//     *
//     * @param jsonFile
//     * @param textureAtlas 资源文件,传入 null表示使用小文件方式加载图片
//     */
//    public CocoStudioUIEditor(FileHandle jsonFile, Collection<TextureAtlas> textureAtlas) {
//        this(jsonFile, null, null, null, textureAtlas);
//    }
//
//    /**
//     * 根据控件名字查找Actor
//     *
//     * @param name 控件名字
//     * @return
//     */
//
//    /**
//     * 查找所有同名的控件
//     */
//
//    /**
//     * 根据json文件创建并返回Group
//     *
//     * @return
//     */
//
//    /**
//     * @param jsonFile     ui编辑成生成的json文件
//     * @param textureAtlas 资源文件,传入 null表示使用小文件方式加载图片.
//     * @param ttfs         字体文件集合
//     * @param bitmapFonts  自定义字体文件集合
//     * @param defaultFont  默认ttf字体文件
//     */
//    public CocoStudioUIEditor(FileHandle jsonFile, Map<String, FileHandle> ttfs, Map<String, BitmapFont> bitmapFonts,
//                              FileHandle defaultFont, Collection<TextureAtlas> textureAtlas) {
//        super(jsonFile);
//        this.textureAtlas = textureAtlas;
//        this.ttfs = ttfs;
//        this.bitmapFonts = bitmapFonts;
//        this.defaultFont = defaultFont;
//
//        dirName = jsonFile.parent().toString();
//
//        if (!dirName.equals("")) {
//            dirName += File.separator;
//        }
//        String json = jsonFile.readString("utf-8");
//        Json jj = GlobalJson.getCocosJson();
//        export = jj.fromJson(CCExport.class, json);
//
//    }
//
//    protected TextureRegion findRegion(String name) {
//        for (TextureAtlas ta : textureAtlas) {
//            if (ta == null) {
//                continue;
//            }
//            TextureRegion tr = ta.findRegion(name);
//            if (tr != null) {
//                return tr;
//            }
//        }
//        return null;
//    }
//
//    protected TextureRegion findRegion(String name, int index) {
//        for (TextureAtlas ta : textureAtlas) {
//            if (ta == null) {
//                continue;
//            }
//            TextureRegion tr = ta.findRegion(name, index);
//            if (tr != null) {
//                return tr;
//            }
//        }
//        return null;
//    }
//
//    public TextureRegion findTextureRegion(ObjectData option, FileData filedata) {
//        String name = filedata.Path;
//        if (name == null || name.equals("")) {
//            return null;
//        }
//        TextureRegion tr = null;
//
//        if (textureAtlas == null || textureAtlas.size() == 0) {// 不使用合并纹理
//            tr = new TextureRegion(new Texture(Gdx.files.internal(dirName + name)));
//        } else {
//            try {
//                String[] arr = name.split("\\/");
//                if (arr.length == 1) {
//                    // support same folder with json file
//                    // add by @xiaozc
//
//                    name = name.substring(0, name.length() - 4);
//                } else {
//                    name = name.substring(arr[0].length() + 1, name.length() - 4);
//                }
//            } catch (Exception e) {
//                Gdx.app.error(option.getClass().toString(), "资源名称不符合约定,无法解析.请查看github项目wiki第十条");
//            }
//
//            // 考虑index下标
//
//            if (name.indexOf("_") == -1) {
//                tr = findRegion(name);
//            } else {
//                try {
//                    int length = name.lastIndexOf("_");
//
//                    Integer index = Integer.parseInt(name.substring(length + 1, name.length()));
//                    // 这里可能报错,属于正常,因为会出现 xx_xx名字的资源而不是xx_2这种
//
//                    name = name.substring(0, length);
//
//                    tr = findRegion(name, index);
//
//                } catch (Exception e) {
//                    tr = findRegion(name);
//                }
//            }
//        }
//
//        if (tr == null) {
//            Gdx.app.debug(option.getClass().toString(), "找不到纹理");
//            return null;
//        }
//
//        if (option.FlipX || option.FlipY) {
//
//            if (textureAtlas == null) {
//                tr.flip(option.FlipX, option.FlipY);
//            } else {
//                tr = new TextureRegion(tr);
//                tr.flip(option.FlipX, option.FlipY);
//            }
//        }
//
//        return tr;
//    }
//
//    @Override
//    public Drawable findDrawable(ObjectData option, FileData fileData) {
//        //显示Default
//        if (fileData == null) {// 默认值不显示
//            return null;
//        }
//
//        if (option.Scale9Enable) {// 九宫格支持
//            TextureRegion textureRegion = findTextureRegion(option, fileData);
//            NinePatch np = new NinePatch(textureRegion, option.Scale9OriginX,
//                    textureRegion.getRegionWidth() - option.Scale9Width - option.Scale9OriginX, option.Scale9OriginY,
//                    textureRegion.getRegionHeight() - option.Scale9Height - option.Scale9OriginY);
//
//            np.setColor(NUtils.getColor(option.CColor, option.Alpha));
//            return new NinePatchDrawable(np);
//        }
//
//        TextureRegion tr = findTextureRegion(option, fileData);
//
//        if (tr == null) {
//            return null;
//        }
//
//        return new TextureRegionDrawable(tr);
//    }
//
//    public LabelStyle createLabelStyle(ObjectData option, String text, Color color) {
//
//        FileHandle fontFile = null;
//        if (ttfs != null && option.FontResource != null) {
//            fontFile = ttfs.get(option.FontResource.Path);
//        }
//
//        if (fontFile == null) {// 使用默认字体文件
//            fontFile = defaultFont;
//        }
//
//        if (fontFile == null) {
//            Gdx.app.debug(option.getClass().toString(), "ttf字体不存在,使用默认字体");
//        }
//
//        BitmapFont font = null;
//        font = new BitmapFont();
//
//        LabelStyle labelStyle = new LabelStyle(font, color);
//        return new LabelStyle(labelStyle);
//    }
//
//    @Override
//    public BitmapFont findBitmapFont(FileData labelBMFontFile_cnb) {
//        BitmapFont font = null;
//        if (bitmapFonts != null) {
//            font = bitmapFonts.get(labelBMFontFile_cnb.Path);
//        }
//        if (font == null) {// 备用创建字体方式
//            font = new BitmapFont(Gdx.files.internal(dirName + labelBMFontFile_cnb.Path));
//        }
//        return font;
//    }
//
//    @Override
//    public BitmapFont createLabelStyleBitmapFont(ObjectData option, String text, Color color) {
//
//        FileHandle fontFile = null;
//        if (ttfs != null && option.FontResource != null) {
//            fontFile = ttfs.get(option.FontResource.Path);
//        }
//
//        if (fontFile == null) {// 使用默认字体文件
//            fontFile = defaultFont;
//        }
//
//        if (fontFile == null) {
//            try {
//                Gdx.app.debug(option.getClass().toString(), "ttf字体:" + option.FontResource.Path + " 不存在,使用默认字体");
//            } catch (Exception e) {
//                Gdx.app.debug(option.getClass().toString(), "不存在字体,使用默认字体");
//            }
//        }
//
//        BitmapFont font = new BitmapFont();
//
//        return font;
//    }
//
//    @Override
//    public BaseCocoStudioUIEditor findCoco(FileData fileData) {
//        BaseCocoStudioUIEditor cocoStudioUIEditor = new CocoStudioUIEditor(Gdx.files.internal(dirName + fileData.Path), ttfs,
//                bitmapFonts, defaultFont, textureAtlas);
//        return cocoStudioUIEditor;
//    }
//}

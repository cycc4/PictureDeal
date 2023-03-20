package com.mygdx.game.Logic.Picture.Atlas;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Logic.ToolInterface.WriteStringInterface;

public class AtlasTitleData implements WriteStringInterface {
    //    public String pngFileName;
    public int width, height;
    public Pixmap.Format format;
    public Texture.TextureFilter minFilter, magFilter;
    public Texture.TextureWrap repeatX, repeatY;
    //保存当前title下所有图标的名称
    public Array<String> atlasPngNameArray = new Array<>();

    @Override
    public String toString() {
        return "ATLASTITLEDATA{" +
                ", width=" + width +
                ", height=" + height +
                ", format=" + format +
                ", minFilter=" + minFilter +
                ", magFilter=" + magFilter +
                ", repeatX=" + repeatX +
                ", repeatY=" + repeatY +
                '}';
    }

    @Override
    public String writeString() {
        return "size: " + width + "," + height + "\n" +
                "format: " + format +"\n"+
                "filter: " + minFilter + "," + magFilter + "\n" +
                "repeat: " + repeatX + "\n";
    }

    @Override
    public void write(FileHandle fileHandle) {

    }

    public void copy(AtlasTitleData atlasTitleData, boolean isCopyAtlasPngNameArray) {
        if (atlasTitleData != null) {
            width = atlasTitleData.width;
            height = atlasTitleData.height;
            format = atlasTitleData.format;
            minFilter = atlasTitleData.minFilter;
            magFilter = atlasTitleData.magFilter;
            repeatX = atlasTitleData.repeatX;
            repeatY = atlasTitleData.repeatY;
            if (isCopyAtlasPngNameArray) {
                atlasPngNameArray.clear();
                for (String s : atlasTitleData.atlasPngNameArray) {
                    atlasPngNameArray.add(s);
                }
            }
        }
    }
}

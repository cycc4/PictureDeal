package com.mygdx.game.Logic.Atlas;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class AtlasTitleData {
    public String pngFileName;
    public int width, height;
    public Pixmap.Format format;
    public Texture.TextureFilter minFilter, magFilter;
    public Texture.TextureWrap repeatX, repeatY;

    @Override
    public String toString() {
        return "ATLASTITLEDATA{" +
                "pngFileName='" + pngFileName + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", format=" + format +
                ", minFilter=" + minFilter +
                ", magFilter=" + magFilter +
                ", repeatX=" + repeatX +
                ", repeatY=" + repeatY +
                '}';
    }
}

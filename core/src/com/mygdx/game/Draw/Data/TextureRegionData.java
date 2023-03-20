package com.mygdx.game.Draw.Data;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*
 * 对textureReigon 位置和大小数据进行保存，
 * 简化需要另外引用这些信息所需函数
 */

public class TextureRegionData {
    protected TextureRegion textureRegion;
    protected float x, y, width, height;
    protected Color color = Color.WHITE;

    public TextureRegionData() {

    }

    public TextureRegionData(TextureRegion textureRegion) {
        setTextureRegion(textureRegion);
        setTextureRegionSize(textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
    }

    public TextureRegionData(TextureRegion textureRegion, float x, float y) {
        setTextureRegion(textureRegion);
        setTextureRegionPosition(x, y);
    }

    public TextureRegionData(TextureRegion textureRegion, float x, float y, float width, float height) {
        setTextureRegion(textureRegion);
        setTextureRegionPosition(x, y);
        setTextureRegionSize(width, height);
    }

    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    public TextureRegion getTextrueRegion() {
        return textureRegion;
    }

    public void setTextureRegionX(float x) {
        this.x = x;
    }

    public void setTextureRegionY(float y) {
        this.y = y;
    }

    public void setTextureRegionPosition(float x, float y) {
        setTextureRegionX(x);
        setTextureRegionY(y);
    }

    public float getTextureRegionX() {
        return x;
    }

    public float getTextureRegionY() {
        return y;
    }

    public void setTextureRegionWidth(float width) {
        this.width = width;
    }

    public void setTextureRegionHeight(float height) {
        this.height = height;
    }

    public void setTextureRegionSize(float width, float height) {
        setTextureRegionWidth(width);
        setTextureRegionHeight(height);
    }

    public float getTextureRegionWidth() {
        return width;
    }

    public float getTextureRegionHeight() {
        return height;
    }

    public Color getColor(){
        return color;
    }

    public void setColor(Color color){
        this.color = color;
    }
}

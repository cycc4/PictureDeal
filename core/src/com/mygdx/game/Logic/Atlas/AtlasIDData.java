package com.mygdx.game.Logic.Atlas;

public class AtlasIDData {
    public boolean rotate;
    public int x, y, width, height, origX, origY, offsetX, offsetY, index;

    @Override
    public String toString() {
        return "ATLASCHARDATA{" +
                "rotate=" + rotate +
                ", x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", origX=" + origX +
                ", origY=" + origY +
                ", offsetX=" + offsetX +
                ", offsetY=" + offsetY +
                ", index=" + index +
                '}';
    }
}

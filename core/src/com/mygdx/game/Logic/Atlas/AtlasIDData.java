package com.mygdx.game.Logic.Atlas;

import com.mygdx.game.Logic.ToolInterface.WriteStringInterface;

public class AtlasIDData implements WriteStringInterface {
    public boolean rotate;
    public int x, y, width, height, origX, origY, offsetX, offsetY, index;
    //获得当前id所对应的titleData的索引
    public String titlePngFileName;

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

    @Override
    public String writeString() {
        return " rotate: " + rotate + "\n" +
                " xy: " + x + "," + y + "\n" +
                " size: " + width + "," + height + "\n" +
                " orig: " + origX + "," + origY + "\n" +
                " offset: " + offsetX + "," + offsetY + "\n" +
                " index: " + index + "\n";
    }

}

package com.mygdx.game.Tools;

public class PictureTool {

    public void copyPicture() {

    }

    //刪除图片后缀名
    public static String deletePictureNameExtension(String fileName){
        return fileName.substring(0, fileName.lastIndexOf("."));
    }
}

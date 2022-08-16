package com.mygdx.game.Logic.Fnt;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Logic.CallBack.RecursionReversalDir;
import com.mygdx.game.Logic.ToolInterface.WriteStringInterface;

import java.util.HashMap;

class ReadFntByBitmapFontData implements WriteStringInterface {
    protected Array<String> fontNameArray = new Array<>();
    protected HashMap<String, BitmapFont.BitmapFontData> bitmapFontDataHashMap = new HashMap<>();
    protected FileHandle readFile;

    public void load(String readPath, String writePath) {
        readFile = new FileHandle(readPath);

        new RecursionReversalDir(readFile, writePath){
            @Override
            protected void callback(FileHandle readFile, String writeFile) {
                String fileName = readFile.name();
                if(fileName.endsWith(".fnt") && !fontNameArray.contains(fileName, true)){
                    BitmapFont.BitmapFontData bfbfd = new BitmapFont.BitmapFontData(readFile, false);
                    bitmapFontDataHashMap.put(fileName, bfbfd);
                    fontNameArray.add(fileName);
                }
            }
        };
    }

    public Array<String> getFontNames() {
        return fontNameArray;
    }

    public float getLineHeight(String fontName){
        return bitmapFontDataHashMap.get(fontName).lineHeight;
    }



    @Override
    public String writeString() {
        StringBuffer sb = new StringBuffer();

        return null;
    }

    @Override
    public void write(FileHandle fileHandle) {

    }
}

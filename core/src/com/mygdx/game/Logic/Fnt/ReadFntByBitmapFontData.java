package com.mygdx.game.Logic.Fnt;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Logic.CallBack.RecursionReversalDir;
import com.mygdx.game.Logic.ToolInterface.WriteStringInterface;

import java.util.HashMap;

class ReadFntByBitmapFontData {
    protected Array<String> fontNameArray = new Array<>();
    protected HashMap<String, MyBitmapFontData> bitmapFontDataHashMap = new HashMap<>();
    protected FileHandle readFile;

    public void load(String readPath, String writePath) {
        readFile = new FileHandle(readPath);

        new RecursionReversalDir(readFile, writePath) {
            @Override
            protected void callback(FileHandle readFile, String writeFile) {
                String fileName = readFile.name();
                if (fileName.endsWith(".fnt") && !fontNameArray.contains(fileName, true)) {
                    MyBitmapFontData bfbfd = new MyBitmapFontData(readFile, false);
                    bitmapFontDataHashMap.put(fileName, bfbfd);
                    fontNameArray.add(fileName);
                }
            }
        };
    }

    public Array<String> getFontNames() {
        return fontNameArray;
    }


}

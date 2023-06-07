package com.mygdx.game.Draw.Layout.Special;

import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.Logic.CallBack.RecursionReversalDir;
import com.mygdx.game.Logic.Tools.JsonTool;
import com.qs.utils.json.PythonArray;
import com.qs.utils.json.PythonDict;

import java.io.File;

public class AdjustJson {

    public void read(FileHandle readFileHandle, String write) {
        new RecursionReversalDir(readFileHandle, write) {
            @Override
            protected void callback(final FileHandle readFileHandle, final String writeFileHandle) {
                if (readFileHandle.name().endsWith(".json")) {
                    JsonTool jsonTool = new JsonTool() {
                        @Override
                        public void adjust(PythonDict dict) {
                            if (dict.containsKey("bones")) {
                                PythonArray bonusDict = dict.geta("bones");
                                PythonDict rootDict = bonusDict.getd(0);
                                if (rootDict.containsKey("name") && rootDict.getString("name").equals("root")) {
                                    if (rootDict.containsKey("scaleX")) {
                                        float scaleX = rootDict.getFloat("scaleX");
                                        rootDict.put("scaleX", scaleX * 1.5f);
                                    } else {
                                        rootDict.put("scaleX", 1.5f);
                                    }
                                    if (rootDict.containsKey("scaleY")) {
                                        float scaleX = rootDict.getFloat("scaleY");
                                        rootDict.put("scaleY", scaleX * 1.5f);
                                    } else {
                                        rootDict.put("scaleY", 1.5f);
                                    }
                                    write(new FileHandle(writeFileHandle + File.separator + readFileHandle.name()));
                                    readFileHandle.delete();
                                }
                            }
                        }
                    };
                    jsonTool.read(readFileHandle);
                }
            }
        };
    }
}

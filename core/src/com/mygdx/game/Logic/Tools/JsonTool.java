package com.mygdx.game.Logic.Tools;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.mygdx.game.Logic.ToolInterface.ReadWriteInterface;
import com.qs.utils.json.PythonDict;

public class JsonTool implements ReadWriteInterface {
    PythonDict data;

    @Override
    public void read(FileHandle fileHandle) {
        if (fileHandle.name().endsWith(".json")) {
            Json json = new Json(JsonWriter.OutputType.json);
            data = json.fromJson(PythonDict.class, FileTool.file2String(fileHandle));
            adjust(data);
        }
    }

    public void adjust(PythonDict dict) {
    }

    @Override
    public void write(FileHandle fileHandle) {
        String aa = data.toJsonStr();
        fileHandle.writeString(aa, false);
    }
}

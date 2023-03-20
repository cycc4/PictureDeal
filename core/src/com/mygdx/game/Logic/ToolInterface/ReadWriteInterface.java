package com.mygdx.game.Logic.ToolInterface;

import com.badlogic.gdx.files.FileHandle;

public interface ReadWriteInterface {
    void read(FileHandle fileHandle);

    void write(FileHandle fileHandle);
}

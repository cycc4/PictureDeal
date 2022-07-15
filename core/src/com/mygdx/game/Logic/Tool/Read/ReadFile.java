package com.mygdx.game.Logic.Tool.Read;

import com.badlogic.gdx.files.FileHandle;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ReadFile {
    public static BufferedReader getBufferedReader(String fPath) {
        if (fPath == null) return null;
        return getBufferedReader(new FileHandle(fPath));
    }

    public static BufferedReader getBufferedReader(FileHandle fileHandle) {
        return getBufferedReader(fileHandle, 64);
    }

    public static BufferedReader getBufferedReader(FileHandle fileHandle, int buffer) {
        return new BufferedReader(new InputStreamReader(fileHandle.read()), buffer);
    }
}

package com.mygdx.game.Logic.Tool.Write;

import com.badlogic.gdx.files.FileHandle;

import java.io.BufferedWriter;

public class WriteFile {

    public static void writeString(String writePath, String string) {

    }

    public static void writeString(FileHandle fileHandle, String string, boolean append){
        BufferedWriter bufferedWriter = new BufferedWriter(fileHandle.writer(append));
    }


}

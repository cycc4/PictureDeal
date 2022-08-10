package com.mygdx.game.CallBack;

import com.badlogic.gdx.files.FileHandle;

import java.io.File;

public class RecursionReversalDir {
    public RecursionReversalDir(File readFile, String writePath) {
        if (readFile != null && readFile.exists()) {
            recursionFunc(readFile, writePath);
        } else {
            System.out.println("ReversalDir dirFile may be empty or nonexistent");
        }
    }

    private void recursionFunc(File readFile, String writePath) {
        if (readFile.isDirectory()) {
            for (File f : readFile.listFiles()) {
                recursionFunc(f, writePath);
            }
        } else {
            callback(readFile, writePath);
        }
    }

    public RecursionReversalDir(FileHandle dirFileHandle, String writePath) {
        if (dirFileHandle != null && dirFileHandle.exists()) {
            recursionFunc(dirFileHandle, writePath);
        } else {
            System.out.println("ReversalDir dirFile may be empty or nonexistent");
        }
    }

    private void recursionFunc(FileHandle readFileHandle, String writePath) {
        if (readFileHandle.isDirectory()) {
            for (FileHandle f : readFileHandle.list()) {
                recursionFunc(f, writePath);
            }
        } else {
            callback(readFileHandle, writePath);
        }
    }

    protected void callback(FileHandle readFileHandle, String writeFileHandle) {

    }

    protected void callback(File readFile, String writeFile) {

    }
}

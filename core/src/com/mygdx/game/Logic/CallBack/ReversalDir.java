package com.mygdx.game.Logic.CallBack;

import com.badlogic.gdx.files.FileHandle;

import java.io.File;
/*
   遍历目录文件夹, 非递归方式
 */

public class ReversalDir {
    public ReversalDir(File dirFile, String writePath) {
        if (dirFile != null && dirFile.exists()) {
            if (dirFile.isDirectory()) {
                for (File f : dirFile.listFiles()) {
                    callback(f, writePath);
                }
            } else {
                callback(dirFile, writePath);
            }
        } else {
            System.out.println("ReversalDir dirFile may be empty or nonexistent");
        }
    }

    public ReversalDir(FileHandle dirFileHandle, String writePath) {
        if (dirFileHandle != null && dirFileHandle.exists()) {
            if (dirFileHandle.isDirectory()) {
                for (FileHandle f : dirFileHandle.list()) {
                    callback(f, writePath);
                }
            } else {
                callback(dirFileHandle, writePath);
            }
        } else {
            System.out.println("ReversalDir dirFile may be empty or nonexistent");
        }
    }

    protected void callback(FileHandle readFileHandle, String writePath) {

    }

    protected void callback(File readFile, String writePath) {

    }
}

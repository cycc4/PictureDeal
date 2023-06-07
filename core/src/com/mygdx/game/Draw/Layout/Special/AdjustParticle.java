package com.mygdx.game.Draw.Layout.Special;

import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.Logic.CallBack.RecursionReversalDir;
import com.mygdx.game.Logic.Tools.ReadFile;

import java.io.BufferedReader;
import java.io.File;

public class AdjustParticle {
    public void read(FileHandle fileHandle, String writes) {
        new RecursionReversalDir(fileHandle, writes) {
            @Override
            protected void callback(FileHandle readFileHandle, String writeString) {
                if (!readFileHandle.name().endsWith(".png") && !readFileHandle.name().endsWith(".jpg") && !readFileHandle.name().endsWith(".atlas")) {
                    StringBuffer sb = new StringBuffer();
                    BufferedReader br = ReadFile.getBufferedReader(readFileHandle);
                    String line;
                    try {
                        boolean isAdjust = false;
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                            sb.append("\n");
                            if (line.startsWith("- ")) {
                                if (line.startsWith("- Emission -") ||
                                        line.startsWith("- X Offset -") ||
                                        line.startsWith("- Y Offset -") ||
                                        line.startsWith("- Spawn") ||
                                        line.startsWith("- X Scale -") ||
                                        line.startsWith("- Y Scale -")) {
                                    isAdjust = true;
                                } else {
                                    isAdjust = false;
                                }
                            }
                            if (isAdjust && line.startsWith("scalingCount:")) {
                                int num = Integer.parseInt(line.substring(14));
                                for (int i = 0; i < num; ++i) {
                                    line = br.readLine();
                                    String[] values = line.split(":");
                                    if (values.length == 2) {
                                        sb.append(values[0]);
                                        sb.append(": ");
                                        sb.append((Float.valueOf(values[1]) * 1.5f));
                                        sb.append("\n");
                                    } else {
                                        sb.append(line);
                                        sb.append("\n");
                                    }
                                }
                            }
                        }

                        System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk:  " + (writeString + File.separator + readFileHandle.name()));
                        new FileHandle(writeString + File.separator + readFileHandle.name()).writeString(sb.toString(), false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        };
    }
}

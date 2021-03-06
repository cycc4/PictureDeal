package com.mygdx.game.Logic.Tool.Particle;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.mygdx.game.ConstantValue;
import com.mygdx.game.Logic.Tool.Read.ReadFile;
import com.mygdx.game.Logic.Tool.SettingTool;

import java.io.BufferedReader;
import java.io.File;

public class ParticleComposite {
    public FileHandle readFileHandle;
    public FileHandle writeFileHandle;
    public String tempPngPath;
    public String particleAtlasName;

    public ParticleComposite(String readPath, String writePath, String particleAtlasName) {
        this(readPath, writePath);
        this.particleAtlasName = particleAtlasName;
    }

    public ParticleComposite(String readPath, String writePath) {
        if (readPath == null) {
            readPath = ConstantValue.SRC_PATH;
        }
        readFileHandle = new FileHandle(readPath);

        if (writePath == null) {
            writePath = ConstantValue.DIR_PATH;
        }
        writeFileHandle = new FileHandle(writePath);

        tempPngPath = writePath + File.separator + "particle" + File.separator;

        File tempPngFile = new File(tempPngPath);
        if (!tempPngFile.exists()) {
            tempPngFile.mkdir();
        }
    }

    public void composite() {
        dealParticle(readFileHandle);
        TexturePacker.process(SettingTool.getTexturePackerSetting(), tempPngPath, writeFileHandle.path(), "particle");
    }

    private void dealParticle(FileHandle dir) {
        if (dir == null) return;
        String dirName = dir.name();

        for (FileHandle f : dir.list()) {
            if (f.isDirectory()) {
                dealParticle(f);
            } else {
                String name = f.name();
                if (name.endsWith(".png") || name.endsWith(".jpg")) {
                    //使用copy將圖片複製到指定目錄
                    f.copyTo(new FileHandle(tempPngPath + dirName + "_" + name));
                } else {
                    StringBuffer sb = new StringBuffer();
                    BufferedReader br = ReadFile.getBufferedReader(f);
                    String line;
                    try {
                        while ((line = br.readLine()) != null) {
                            if (line.equals("- Image Paths -")) {
                                sb.append(line + "\n");
                                while ((line = br.readLine()) != null) {
                                    if (line.endsWith(".png") || line.endsWith(".jpg")) {
                                        sb.append(dirName + "_" + line + "\n");
                                    } else {
                                        sb.append(line + "\n");
                                        break;
                                    }
                                }

                                if (line == null) {
                                    break;
                                }
                            } else {
                                sb.append(line + "\n");
                            }
                        }

                        FileHandle write = new FileHandle(writeFileHandle + File.separator + f.name());
                        write.writeString(sb.toString(), false);
                        br.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

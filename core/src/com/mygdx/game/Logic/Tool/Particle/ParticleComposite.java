package com.mygdx.game.Logic.Tool.Particle;

import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.CallBack.ReversalDir;
import com.mygdx.game.CombinationPicture.PackPictureBat;
import com.mygdx.game.ConstantValue;
import com.mygdx.game.Logic.Tool.Read.ReadFile;

import java.io.BufferedReader;
import java.io.File;

/*
对当前目录下的所有例子进行合图操作
* */
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
        new PackPictureBat(tempPngPath, writeFileHandle.path(), "particle");
//        TexturePacker.process(CombinationSetting.getTexturePackerSetting(), tempPngPath, writeFileHandle.path(), "particle");
    }

    private void dealParticle(FileHandle dir) {
        if (dir == null) return;
        ReversalDir reversalDir = new ReversalDir(dir, null) {
            @Override
            protected void callback(FileHandle f, String writePath) {
                String dirName = f.name();
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
        };
    }
}

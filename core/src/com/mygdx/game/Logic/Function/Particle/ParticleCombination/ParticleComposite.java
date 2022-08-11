package com.mygdx.game.Logic.Function.Particle.ParticleCombination;

import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.Logic.CallBack.RecursionReversalDir;
import com.mygdx.game.Logic.CallBack.ReversalDir;
import com.mygdx.game.Logic.CombinationPicture.PackPictureBat;
import com.mygdx.game.Logic.Tools.ReadFile;
import com.mygdx.game.Logic.ToolInterface.DealInterface;

import java.io.BufferedReader;
import java.io.File;

/**
 * 对当前目录下粒子进行合图操作
 *     注意:需要合图的粒子每个都需要一个文件夹
 */
public class ParticleComposite implements DealInterface {
    public FileHandle writeFileHandle;
    public String tempPngPath;
    @Override
    public void deal(String readPath, String writePath) {
        FileHandle readFileHandle = new FileHandle(readPath);
        writeFileHandle = new FileHandle(writePath);
        tempPngPath = writePath + File.separator + "particle" + File.separator;
        File tempPngFile = new File(tempPngPath);
        if (!tempPngFile.exists()) {
            tempPngFile.mkdir();
        }

        new ReversalDir(readFileHandle, writePath) {
            @Override
            protected void callback(FileHandle readFile, String writeFile) {
                if (readFile.isDirectory()) {
                    final String dirName = readFile.name();
                    new RecursionReversalDir(readFile, writeFile) {
                        @Override
                        protected void callback(FileHandle readFile, String writeFile) {
                            String name = readFile.name();
                            System.out.println("read File name is:  " + name);
                            if (name.endsWith(".png") || name.endsWith(".jpg")) {
                                //使用copy將圖片複製到指定目錄
                                readFile.copyTo(new FileHandle(tempPngPath + dirName + "_" + name));
                            } else {
                                StringBuffer sb = new StringBuffer();
                                BufferedReader br = ReadFile.getBufferedReader(readFile);
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

                                    FileHandle write = new FileHandle(writeFileHandle + File.separator + readFile.name());
                                    write.writeString(sb.toString(), false);
                                    br.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                }
            }
        };
        new PackPictureBat(tempPngPath, writeFileHandle.path(), "particle");
    }
}

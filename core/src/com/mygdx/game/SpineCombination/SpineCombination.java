package com.mygdx.game.SpineCombination;

import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.CallBack.RecursionReversalDir;
import com.mygdx.game.CallBack.ReversalDir;
import com.mygdx.game.CombinationPicture.PackPictureBat;
import com.mygdx.game.CombinationPicture.UnPackPictureBat;
import com.mygdx.game.Logic.Atlas.ReadAtlas;
import com.mygdx.game.Logic.ToolInterface.DealInterface;
import com.mygdx.game.Logic.Atlas.AtlasIDData;
import com.mygdx.game.Logic.Atlas.AtlasTitleData;
import com.mygdx.game.Tools.FileTool;
import com.mygdx.game.Tools.PictureTool;

import java.io.File;
import java.util.HashMap;

public class SpineCombination implements DealInterface {
    protected String unPackImageDir;
    protected String unPackImageDirCommonDir;
    protected ReadAtlas commonAtlas;

    @Override
    public void deal(final String readPath, String writePath) {
        if (readPath == null) return;
        File readFile = new File(readPath);
        if (!readFile.exists()) return;

        unPackImageDir = readPath + File.separator + "UnPack";
        FileTool.mkdir(new File(unPackImageDir));
        unPackImageDirCommonDir = readPath + File.separator + "common";
        FileTool.mkdir(new File(unPackImageDirCommonDir));

        //step1:先对所有动画进行切图操作
        new RecursionReversalDir(readFile, writePath) {
            @Override
            protected void callback(File file, String writePath) {
                String fileName = file.getName();
                if (fileName.endsWith(".atlas")) {
                    String dirName = PictureTool.deletePictureNameExtension(fileName);
                    System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy:  " + dirName);
                    new UnPackPictureBat(
                            file.getAbsolutePath(),
                            file.getParent(),
                            unPackImageDir + File.separator + dirName
                    );
                    //将atlas文件拷贝到目标目录下
                    FileTool.moveTo(file, new File(writePath + File.separator + fileName));
                }
            }
        };
        //step2:对拆图的图片进行重命名并将图片拷贝到unPackImageDirCommonDir目录下,
        // 否则可能会有相同的图片名
        File unPackImageDirFile = new File(unPackImageDir);

        new ReversalDir(unPackImageDirFile, null) {
            @Override
            protected void callback(File readFile, String writePath) {
                if (readFile.isDirectory()) {
                    final String fileName = readFile.getName();
                    new ReversalDir(readFile, unPackImageDirCommonDir) {
                        @Override
                        protected void callback(File readFile, String writePath) {
                            readFile.renameTo(new File(unPackImageDir + File.separator + fileName + "_" + readFile.getName()));
                        }
                    };
                    readFile.delete();
                }
            }
        };

        //step3: 对重命名的图片进行合图
        new PackPictureBat(unPackImageDir, unPackImageDirCommonDir, "common.atlas");

        //step4: 开始合图
        commonAtlas = new ReadAtlas();
        commonAtlas.load(new FileHandle(unPackImageDirCommonDir + File.separator + "common.atlas"));

        //step5: 处理动画的atlas文件
        new ReversalDir(new File(writePath), writePath) {
            @Override
            protected void callback(File readFile, String writeFile) {
                String atlasName = readFile.getName();
                if (atlasName.endsWith(".atlas")) {
                    atlasName = atlasName.substring(0, atlasName.length() - 6);
                    ReadAtlas atlas = new ReadAtlas();
                    atlas.load(new FileHandle(readFile));

                    HashMap<String, AtlasTitleData> newAtlasTitleHash = new HashMap<>();
//                    AtlasTitleData atlasTitleData = atlas.getAtlastitleData();
//                    AtlasTitleData commonAtlasTitleData = commonAtlas.getAtlastitleData();
//                    atlasTitleData.pngFileName = "common.png";
//                    atlasTitleData.width = commonAtlasTitleData.width;
//                    atlasTitleData.height = commonAtlasTitleData.height;
                    for (String key : atlas.getCharHashMap().keySet()) {
                        AtlasIDData atlasAtlasIDData = atlas.getAtlasIDData(key);
                        AtlasIDData commonAtlasIDData = commonAtlas.getAtlasIDData(atlasName + "_" + key);

                        String titleName = commonAtlasIDData.titlePngFileName;
                        AtlasTitleData atd = newAtlasTitleHash.get(titleName);
                        if (atd == null) {
                            atd = new AtlasTitleData();
                            atd.copy(commonAtlas.getAtlastitleData(titleName), false);
                            newAtlasTitleHash.put(titleName, atd);
                        }
                        atd.atlasPngNameArray.add(key);
                        // 重新赋值
                        atlasAtlasIDData.x = commonAtlasIDData.x;
                        atlasAtlasIDData.y = commonAtlasIDData.y;
                        atlasAtlasIDData.width = commonAtlasIDData.width;
                        atlasAtlasIDData.height = commonAtlasIDData.height;
                        atlasAtlasIDData.offsetX = commonAtlasIDData.offsetX;
                        atlasAtlasIDData.offsetY = commonAtlasIDData.offsetY;
                        atlasAtlasIDData.index = commonAtlasIDData.index;
                    }

                    StringBuffer sb = new StringBuffer();
                    for (String key : newAtlasTitleHash.keySet()) {
                        AtlasTitleData atd = newAtlasTitleHash.get(key);
                        sb.append("\n" + key + "\n");
                        sb.append(atd.writeString());
                        for (String s : atd.atlasPngNameArray) {
                            sb.append(s + "\n");
                            sb.append(atlas.getAtlasIDData(s).writeString());
                        }
                    }
                    new FileHandle(writeFile + File.separator + atlasName+".atlas").writeString(sb.toString(), false);
                }
            }
        };
    }

}

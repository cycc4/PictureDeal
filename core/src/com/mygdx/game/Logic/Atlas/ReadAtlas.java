package com.mygdx.game.Logic.Atlas;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.StreamUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ReadAtlas {
    private HashMap<String, AtlasTitleData> titleDataHashMap = new HashMap<>();

    private HashMap<String, AtlasIDData> pngNameHashMap = new HashMap<String, AtlasIDData>();

    public AtlasTitleData getAtlastitleData(String key) {
        return titleDataHashMap.get(key);
    }

    public AtlasIDData getAtlasIDData(String key) {
        return pngNameHashMap.get(key);
    }

    public HashMap<String, AtlasIDData> getCharHashMap() {
        return pngNameHashMap;
    }

    public HashMap<String, AtlasTitleData> getTitleDataHashMap() {
        return titleDataHashMap;
    }

    public void load(FileHandle fileHandle) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileHandle.read()), 64);
        String atlasPngName = null;

        try {
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }

                if (line.trim().length() == 0) {
                    atlasPngName = null;
                } else if (line.endsWith(".png")) {
                    if (titleDataHashMap.containsKey(line)) {
                        while (true) {
                            line = reader.readLine();
                            if (line.trim().length() == 0) {
                                break;
                            }
                        }
                        continue;
                    }
                    atlasPngName = line;

                    AtlasTitleData atlasTitleData = new AtlasTitleData();
                    titleDataHashMap.put(line, atlasTitleData);
                    line = reader.readLine();
                    if ((line = getValueString(line, "size:")) != null) {
                        String[] sArr = line.split(",");
                        atlasTitleData.width = Integer.parseInt(sArr[0]);
                        atlasTitleData.height = Integer.parseInt(sArr[1]);
                    }
                    line = reader.readLine();
                    if ((line = getValueString(line, "format:")) != null) {
                        atlasTitleData.format = Pixmap.Format.valueOf(line);
                    }
                    line = reader.readLine();
                    if ((line = getValueString(line, "filter:")) != null) {
                        String[] sArr = line.split(",");
                        atlasTitleData.minFilter = Texture.TextureFilter.valueOf(sArr[0]);
                        atlasTitleData.magFilter = Texture.TextureFilter.valueOf(sArr[1]);
                    }
                    line = reader.readLine();
                    if ((line = getValueString(line, "repeat")) != null) {
                        Texture.TextureWrap repeatX = Texture.TextureWrap.ClampToEdge;
                        Texture.TextureWrap repeatY = Texture.TextureWrap.ClampToEdge;
                        if (line.equals("x")) {
                            repeatX = Texture.TextureWrap.Repeat;
                        } else if (line.equals("y")) {
                            repeatY = Texture.TextureWrap.Repeat;
                        } else if (line.equals("xy")) {
                            repeatX = Texture.TextureWrap.Repeat;
                            repeatY = Texture.TextureWrap.Repeat;
                        }
                        atlasTitleData.repeatX = repeatX;
                        atlasTitleData.repeatY = repeatY;
                    }
                } else {
                    String id = line;
                    AtlasIDData atlaschardata = new AtlasIDData();
                    line = reader.readLine();
                    atlaschardata.rotate = Boolean.valueOf(getValueString(line, "rotate:"));
                    line = reader.readLine();
                    String s = getValueString(line, "xy:");
                    String[] sArr = s.split(",");
                    atlaschardata.x = Integer.parseInt(sArr[0]);
                    atlaschardata.y = Integer.parseInt(sArr[1]);
                    line = reader.readLine();
                    s = getValueString(line, "size:");
                    sArr = s.split(",");
                    atlaschardata.width = Integer.parseInt(sArr[0]);
                    atlaschardata.height = Integer.parseInt(sArr[1]);
                    line = reader.readLine();
                    s = getValueString(line, "orig:");
                    sArr = s.split(",");
                    atlaschardata.origX = Integer.parseInt(sArr[0]);
                    atlaschardata.origY = Integer.parseInt(sArr[1]);
                    line = reader.readLine();
                    s = getValueString(line, "offset:");
                    sArr = s.split(",");
                    atlaschardata.offsetX = Integer.parseInt(sArr[0]);
                    atlaschardata.offsetY = Integer.parseInt(sArr[1]);
                    line = reader.readLine();
                    atlaschardata.index = Integer.parseInt(getValueString(line, "index:"));

                    atlaschardata.titlePngFileName = atlasPngName;
                    pngNameHashMap.put(id, atlaschardata);
                    getAtlastitleData(atlasPngName).atlasPngNameArray.add(id);

                }
            }
        } catch (Exception var19) {
            throw new GdxRuntimeException("Error reading pack file: ", var19);
        } finally {
            StreamUtils.closeQuietly(reader);
        }
    }

    public void write(FileHandle fileHandle) {
        StringBuffer stringBuffer = new StringBuffer();
        for (String key : titleDataHashMap.keySet()) {
            stringBuffer.append("\n" + key + "\n");
            AtlasTitleData atlasTitleData = titleDataHashMap.get(key);
            stringBuffer.append(atlasTitleData.writeString());
            for (String pngName : atlasTitleData.atlasPngNameArray) {
                stringBuffer.append(pngName + "\n");
                stringBuffer.append(pngNameHashMap.get(key).writeString());
            }
        }

        fileHandle.writeString(stringBuffer.toString(), false);
    }

    public String getValueString(String s, String startWithString) {
        return getValueString(s, startWithString, true);
    }

    public String getValueString(String s, String startWithString, boolean isDeleteSpace) {
        if (isDeleteSpace) {
            s = s.replaceAll(" +", "");
        }
        if (s.startsWith(startWithString)) {
            return s.substring(startWithString.length());
        }
        return null;
    }
}

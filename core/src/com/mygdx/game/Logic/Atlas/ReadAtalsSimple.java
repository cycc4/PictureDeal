package com.mygdx.game.Logic.Atlas;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.StreamUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ReadAtalsSimple {
    private AtlasTitleData atlastitledata = new AtlasTitleData();
    private HashMap<String, AtlasIDData> charHashMap = new HashMap<String, AtlasIDData>();

    public AtlasTitleData getAtlastitleData() {
        return atlastitledata;
    }

    public AtlasIDData getAtlasIDData(String key) {
        return charHashMap.get(key);
    }

    public HashMap<String, AtlasIDData> getCharHashMap() {
        return charHashMap;
    }

    public void load(FileHandle fileHandle) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileHandle.read()), 64);

        try {
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }

                if (line.trim().length() == 0) {
                } else if (atlastitledata == null) {
//                    atlastitledata.pngFileName = line;

                    line = reader.readLine();
                    if ((line = getValueString(line, "size:")) != null) {
                        String[] sArr = line.split(",");
                        atlastitledata.width = Integer.parseInt(sArr[0]);
                        atlastitledata.height = Integer.parseInt(sArr[1]);
                    }
                    line = reader.readLine();
                    if ((line = getValueString(line, "format:")) != null) {
                        atlastitledata.format = Pixmap.Format.valueOf(line);
                    }
                    line = reader.readLine();
                    if ((line = getValueString(line, "filter:")) != null) {
                        String[] sArr = line.split(",");
                        atlastitledata.minFilter = Texture.TextureFilter.valueOf(sArr[0]);
                        atlastitledata.magFilter = Texture.TextureFilter.valueOf(sArr[1]);
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
                        atlastitledata.repeatX = repeatX;
                        atlastitledata.repeatY = repeatY;
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

                    charHashMap.put(id, atlaschardata);
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
        stringBuffer.append("\n");
        stringBuffer.append(atlastitledata.writeString());
        for (String key : charHashMap.keySet()) {
            stringBuffer.append(key + "\n");
            stringBuffer.append(charHashMap.get(key).writeString());
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

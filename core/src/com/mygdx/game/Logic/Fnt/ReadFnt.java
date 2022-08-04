package com.mygdx.game.Logic.Fnt;

/*
 * 解析fnt文件
 */

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.StreamUtils;
import com.mygdx.game.Logic.Fnt.FntIDData;
import com.mygdx.game.Logic.Fnt.FntKerningsData;
import com.mygdx.game.Logic.Fnt.FntTitleData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ReadFnt {
    private HashMap<Integer, FntIDData> charsHashMap = new HashMap<Integer, FntIDData>();
    private HashMap<String, FntKerningsData> kerningHashMap = new HashMap<String, FntKerningsData>();
    private FntTitleData titleData = new FntTitleData();
    private String readPath;
    private String fntName;

    public FntIDData getCharData(int id) {
        return charsHashMap.get(id);
    }

    public FntTitleData getTitleData() {
        return titleData;
    }

    public FntKerningsData getKerningData(String key) {
        return kerningHashMap.get(key);
    }

    public HashMap<Integer, FntIDData> getCharsHashMap() {
        return charsHashMap;
    }

    public HashMap<String, FntKerningsData> getKerningHashMap() {
        return kerningHashMap;
    }

    public String getReadPath() {
        return readPath;
    }

    public String getFntName() {
        return fntName;
    }

    public void load(FileHandle fontFile, boolean flip) {
        readPath = fontFile.file().getParentFile().getAbsolutePath();
        fntName = fontFile.name();

        BufferedReader reader = new BufferedReader(new InputStreamReader(fontFile.read()), 512);
        try {
            String line = reader.readLine();
            if (line == null) {
                throw new GdxRuntimeException("File is empty.");
            } else {
                String[] info = line.split(" ");
                try {
                    titleData.face = getStringValue(info[1], "face=");
                    titleData.size = getIntValue(info[2], "size=");
                    titleData.bold = getIntValue(info[3], "bold=");
                    titleData.italic = getIntValue(info[4], "italic=");
                    titleData.charset = getStringValue(info[5], "charset=");
                    titleData.unicode = getIntValue(info[6], "unicode=");
                    titleData.stretchH = getIntValue(info[7], "stretchH=");
                    titleData.smooth = getIntValue(info[8], "smooth=");
                    titleData.aa = getIntValue(info[9], "aa=");

                    if (info[10].startsWith("padding=")) {
                        String[] paddings = info[10].substring(8).split(",", 4);
                        titleData.padTop = Integer.parseInt(paddings[0]);
                        titleData.padRight = Integer.parseInt(paddings[1]);
                        titleData.padBottom = Integer.parseInt(paddings[2]);
                        titleData.padLeft = Integer.parseInt(paddings[3]);
                    }

                    if (info[11].startsWith("spacing=")) {
                        String[] spacings = info[11].substring(8).split(",", 2);
                        titleData.spacingX = Integer.parseInt(spacings[0]);
                        titleData.spacingY = Integer.parseInt(spacings[1]);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                line = reader.readLine();
                if (line == null) {
                    throw new GdxRuntimeException("Missing common header.");
                } else {
                    String[] common = line.split(" ", 9);
                    if (common.length < 3) {
                        throw new GdxRuntimeException("Invalid common header.");
                    } else if (!common[1].startsWith("lineHeight=")) {
                        throw new GdxRuntimeException("Missing: lineHeight");
                    } else {
                        titleData.lineHeight = Integer.parseInt(common[1].substring(11));
                        if (!common[2].startsWith("base=")) {
                            throw new GdxRuntimeException("Missing: base");
                        } else {
                            titleData.base = Integer.parseInt(common[2].substring(5));
                            int pageCount = 1;
                            if (common.length >= 6 && common[5] != null && common[5].startsWith("pages=")) {
                                try {
                                    pageCount = Math.max(1, Integer.parseInt(common[5].substring(6)));
                                } catch (NumberFormatException var37) {
                                }
                            }
                            titleData.pages = pageCount;
                            titleData.files = new String[pageCount];

                            for (int p = 0; p < pageCount; ++p) {
                                line = reader.readLine();
                                titleData.files[p] = getStringValue(line, "page id=" + p + " file="); //TODO:这里只保存 file 名,不保存路径,
                            }

                            while (true) {
                                String s = reader.readLine();
                                if (s == null) {
                                    break;
                                } else if (s.startsWith("chars count=")) {
                                    continue;
                                } else if (s.startsWith("char")) {
                                    s = s.replaceAll(" {2,}", " ");
                                    String[] sArr = s.split(" ");
                                    int id = getIntValue(sArr[1], "id=");
                                    if (id == -1) {
                                        continue;
                                    }

                                    FntIDData charsdata = new FntIDData();
                                    charsdata.x = getIntValue(sArr[2], "x=");
                                    charsdata.y = getIntValue(sArr[3], "y=");
                                    charsdata.width = getIntValue(sArr[4], "width=");
                                    charsdata.height = getIntValue(sArr[5], "height=");
                                    charsdata.xoffset = getIntValue(sArr[6], "xoffset=");
                                    charsdata.yoffset = getIntValue(sArr[7], "yoffset=");
                                    charsdata.xadvance = getIntValue(sArr[8], "xadvance=");
                                    charsdata.page = getIntValue(sArr[9], "page=");
                                    charsdata.chn1 = getIntValue(sArr[10], "chnl=");
                                    charsHashMap.put(id, charsdata);

                                } else if (s.startsWith("kernings count=")) {
                                    continue;
                                } else if (s.startsWith("kerning")) {
                                    s = s.replaceAll(" {2,}", " ");
                                    FntKerningsData kerningsdata = new FntKerningsData();
                                    String[] sArr = s.split(" ");
                                    kerningsdata.first = getIntValue(sArr[1], "first=");
                                    kerningsdata.second = getIntValue(sArr[2], "second=");
                                    kerningsdata.amount = getIntValue(sArr[3], "amount=");
                                    kerningHashMap.put(kerningsdata.first + "+" + kerningsdata.second, kerningsdata);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception var38) {
            throw new GdxRuntimeException("Error loading font file: " + fontFile, var38);
        } finally {
            StreamUtils.closeQuietly(reader);
        }
    }

    public void write(FileHandle writeFile) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(titleData.toString() + "\n");
        stringBuffer.append("chars count=" + charsHashMap.size() + "\n");
        for (int k : charsHashMap.keySet()) {
            stringBuffer.append("char id=" + k + charsHashMap.get(k).toString() + "\n");
        }
        stringBuffer.append("kernings count=" + kerningHashMap.size() + "\n");
        for (String key : kerningHashMap.keySet()) {
            stringBuffer.append(kerningHashMap.get(key));
        }
        stringBuffer.append("\n");

        writeFile.writeString(stringBuffer.toString(), false);
    }

    public void reset() {

    }

    public int getIntValue(String string, String startWithString) {
        if (string.startsWith(startWithString)) {
            int s = startWithString.length();
            return Integer.parseInt(string.substring(s));
        }
        return -1;
    }

    public String getStringValue(String string, String startWithString) {
        if (string.startsWith(startWithString)) {
            int s = startWithString.length();
            return string.substring(s + 1, string.length() - 1);
        }
        return null;
    }

    public float getFloatValue(String string, String startWithString) {
        if (string.startsWith(startWithString)) {
            int s = startWithString.length();
            return Float.parseFloat(string.substring(s));
        }
        return -1;
    }
}

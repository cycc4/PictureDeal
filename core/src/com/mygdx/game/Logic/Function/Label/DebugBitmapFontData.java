package com.mygdx.game.Logic.Function.Label;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.StreamUtils;
import com.mygdx.game.Draw.MyString;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
    down == lineHeight

    capHeight: 大写字母最大高度
    (注意: 计算方法是从 capChars 找有没有这个字,如果有,直接使用第一个有的,如果都没有,将取所有字体中高度最大的)

    ascent ： 大写字母到最高文字顶部的距离

    -------------******------------------- lineHeight

                 ******    baseLine
    --------------------------------------
 */
public class DebugBitmapFontData extends BitmapFont.BitmapFontData {
    public float baseLine = 0;
    public String firstLine;  //将字体文件第一行作为一个整体，因为一般不需要修改
    public int scaleW;
    public int scaleH;
    public int pageCount;
    public String fileName;

    public HashMap<Integer, ExtraData> bitMapCharHashMap = new HashMap<>();
    public Array<Integer> bitMapCharArray = new Array<>();
    public Array<KerningData> bitMapCharKerningArray = new Array<>();

    public DebugBitmapFontData(FileHandle fontFile, boolean flip) {
        this.fontFile = fontFile;
        this.flipped = flip;
        load(fontFile, flip);
    }

    @Override
    public void load(FileHandle fontFile, boolean flip) {
        if (imagePaths != null) throw new IllegalStateException("Already loaded.");

        BufferedReader reader = new BufferedReader(new InputStreamReader(fontFile.read()), 512);
        try {
            String line = reader.readLine(); // info
            if (line == null) throw new GdxRuntimeException("File is empty.");
            firstLine = line;
            line = line.substring(line.indexOf("padding=") + 8);
            String[] padding = line.substring(0, line.indexOf(' ')).split(",", 4);
            if (padding.length != 4) throw new GdxRuntimeException("Invalid padding.");
            padTop = Integer.parseInt(padding[0]);
            padRight = Integer.parseInt(padding[1]);
            padBottom = Integer.parseInt(padding[2]);
            padLeft = Integer.parseInt(padding[3]);

            line = reader.readLine();
            if (line == null) throw new GdxRuntimeException("Missing common header.");
            String[] common = line.split(" ", 7); // At most we want the 6th element; i.e. "page=N"

            // At least lineHeight and base are required.
            if (common.length < 3) throw new GdxRuntimeException("Invalid common header.");

            if (!common[1].startsWith("lineHeight="))
                throw new GdxRuntimeException("Missing: lineHeight");
            lineHeight = Integer.parseInt(common[1].substring(11));

            if (!common[2].startsWith("base="))
                throw new GdxRuntimeException("Missing: base");
            baseLine = Integer.parseInt(common[2].substring(5));

            if (!common[3].startsWith("scaleW"))
                throw new GdxRuntimeException("Missing scaleW");
            scaleW = Integer.parseInt(common[3].substring(7));

            if (!common[4].startsWith("scaleH"))
                throw new GdxRuntimeException("Missing scaleH");
            scaleH = Integer.parseInt(common[4].substring(7));

            pageCount = 1;
            if (common.length >= 6 && common[5] != null && common[5].startsWith("pages=")) {
                try {
                    pageCount = Math.max(1, Integer.parseInt(common[5].substring(6)));
                } catch (NumberFormatException ignored) { // Use one page.
                }
            }

            //packed = 0; 默认，没用到

            imagePaths = new String[pageCount];

            for (int p = 0; p < pageCount; p++) {
                line = reader.readLine();
                if (line == null)
                    throw new GdxRuntimeException("Missing additional page definitions.");

                Matcher matcher = Pattern.compile(".*id=(\\d+)").matcher(line);
                if (matcher.find()) {
                    String id = matcher.group(1);
                    try {
                        int pageID = Integer.parseInt(id);
                        if (pageID != p)
                            throw new GdxRuntimeException("Page IDs must be indices starting at 0: " + id);
                    } catch (NumberFormatException ex) {
                        throw new GdxRuntimeException("Invalid page id: " + id, ex);
                    }
                }

                matcher = Pattern.compile(".*file=\"?([^\"]+)\"?").matcher(line);
                if (!matcher.find()) throw new GdxRuntimeException("Missing: file");
                fileName = matcher.group(1);

                imagePaths[p] = fontFile.parent().child(fileName).path().replaceAll("\\\\", "/");
            }
            descent = 0;

            while (true) {
                line = reader.readLine();
                if (line == null) break; // EOF
                if (line.startsWith("kernings ")) break; // Starting kernings block.
                if (!line.startsWith("char ")) continue;

                BitmapFont.Glyph glyph = new BitmapFont.Glyph();

                StringTokenizer tokens = new StringTokenizer(line, " =");
                tokens.nextToken();
                tokens.nextToken();
                int ch = Integer.parseInt(tokens.nextToken());
                if (ch <= 0)
                    missingGlyph = glyph;
                else if (ch <= Character.MAX_VALUE)
                    setGlyph(ch, glyph);
                else
                    continue;
                glyph.id = ch;
                tokens.nextToken();
                glyph.srcX = Integer.parseInt(tokens.nextToken());
                tokens.nextToken();
                glyph.srcY = Integer.parseInt(tokens.nextToken());
                tokens.nextToken();
                glyph.width = Integer.parseInt(tokens.nextToken());
                tokens.nextToken();
                glyph.height = Integer.parseInt(tokens.nextToken());
                tokens.nextToken();
                glyph.xoffset = Integer.parseInt(tokens.nextToken());
                tokens.nextToken();

                int oldYOffset = Integer.parseInt(tokens.nextToken());

                if (flip) {
                    glyph.yoffset = oldYOffset;
                } else {
                    glyph.yoffset = -(glyph.height + oldYOffset);
                }
                tokens.nextToken();
                glyph.xadvance = Integer.parseInt(tokens.nextToken());

                // Check for page safely, it could be omitted or invalid.
                if (tokens.hasMoreTokens()) tokens.nextToken();
                if (tokens.hasMoreTokens()) {
                    try {
                        glyph.page = Integer.parseInt(tokens.nextToken());
                    } catch (NumberFormatException ignored) {
                    }
                }

                if (glyph.width > 0 && glyph.height > 0)
                    descent = Math.min(baseLine + glyph.yoffset, descent);

                addBitMapChar(ch, new ExtraData(glyph, oldYOffset));
            }
            descent += padBottom;

            while (true) {
                line = reader.readLine();
                if (line == null) break;
                if (!line.startsWith("kerning ")) break;

                StringTokenizer tokens = new StringTokenizer(line, " =");
                tokens.nextToken();
                tokens.nextToken();
                int first = Integer.parseInt(tokens.nextToken());
                tokens.nextToken();
                int second = Integer.parseInt(tokens.nextToken());
                if (first < 0 || first > Character.MAX_VALUE || second < 0 || second > Character.MAX_VALUE)
                    continue;
                BitmapFont.Glyph glyph = getGlyph((char) first);
                tokens.nextToken();
                int amount = Integer.parseInt(tokens.nextToken());
                if (glyph != null) { // Kernings may exist for glyph pairs not contained in the font.
                    glyph.setKerning(second, amount);
                }

                bitMapCharKerningArray.add(new KerningData(first, second));
            }

            adjustVariationValue();

        } catch (Exception ex) {
            throw new GdxRuntimeException("Error loading font file: " + fontFile, ex);
        } finally {
            StreamUtils.closeQuietly(reader);
        }
    }

    public void adjustVariationValue() {
        float padY = padTop + padBottom;
        BitmapFont.Glyph spaceGlyph = getGlyph(' ');
        if (spaceGlyph == null) {
            spaceGlyph = new BitmapFont.Glyph();
            spaceGlyph.id = (int) ' ';
            BitmapFont.Glyph xadvanceGlyph = getGlyph('l');
            if (xadvanceGlyph == null) xadvanceGlyph = getFirstGlyph();
            spaceGlyph.xadvance = xadvanceGlyph.xadvance;
            setGlyph(' ', spaceGlyph);
        }
        if (spaceGlyph.width == 0) {
            spaceGlyph.width = (int) (padLeft + spaceGlyph.xadvance + padRight);
            spaceGlyph.xoffset = (int) -padLeft;
        }
        spaceXadvance = spaceGlyph.xadvance;

        BitmapFont.Glyph xGlyph = null;
        for (char xChar : xChars) {
            xGlyph = getGlyph(xChar);
            if (xGlyph != null) break;
        }
        if (xGlyph == null) xGlyph = getFirstGlyph();
        xHeight = xGlyph.height - padY;

        BitmapFont.Glyph capGlyph = null;
        for (char capChar : capChars) {
            capGlyph = getGlyph(capChar);
            if (capGlyph != null) break;
        }
        if (capGlyph == null) {
            for (BitmapFont.Glyph[] page : this.glyphs) {
                if (page == null) continue;
                for (BitmapFont.Glyph glyph : page) {
                    if (glyph == null || glyph.height == 0 || glyph.width == 0)
                        continue;
                    capHeight = Math.max(capHeight, glyph.height);
                }
            }
        } else
            capHeight = capGlyph.height;
        capHeight -= padY;

        ascent = baseLine - capHeight;
        down = -lineHeight;
        if (flipped) {
            ascent = -ascent;
            down = -down;
        }
    }

    public void resetDescent() {
        descent = 0;
        for (int ch : bitMapCharArray) {
            BitmapFont.Glyph glyph = getGlyph((char) ch);
            if (glyph.width > 0 && glyph.height > 0) {
                descent = Math.min(baseLine + glyph.yoffset, descent);
            }
        }
        descent += padBottom;
    }

    public void addBitMapChar(int ch, ExtraData extraData) {
        bitMapCharHashMap.put(ch, extraData);
        bitMapCharArray.add(ch);
    }

    public void write(FileHandle writeFileHandle) {
        StringBuffer sb = new StringBuffer();
        sb.append(firstLine);
        //line2
        sb.append("\ncommon lineHeight=");
        sb.append(((int) lineHeight));
        sb.append(" base=");
        sb.append((int) baseLine);
        sb.append(" scaleW=");
        sb.append(scaleW);
        sb.append(" scaleH=");
        sb.append(scaleH);
        sb.append(" pages=");
        sb.append(pageCount);
        sb.append(" packed=0\n");
        //line3
        for (int id = 0; id < pageCount; ++id) {
            sb.append("page id=");
            sb.append(id);
            sb.append(" file=\"");
            sb.append(imagePaths[id].substring(imagePaths[id].lastIndexOf("/")));
            sb.append("\"\n");
        }
        //line 4
        sb.append("chars count=");
        sb.append(bitMapCharArray.size);
        sb.append("\n");
        //line 5
        for (int i = 0; i < bitMapCharArray.size; ++i) {
            sb.append("char id=");
            int id = bitMapCharArray.get(i);
            sb.append(MyString.getEquidistanceString(id + "", 8));
            BitmapFont.Glyph g = getGlyph((char) id);
            sb.append("x=");
            sb.append(MyString.getEquidistanceString(g.srcX + "", 5));
            sb.append("y=");
            sb.append(MyString.getEquidistanceString(g.srcY + "", 5));
            sb.append("width=");
            sb.append(MyString.getEquidistanceString(g.width + "", 5));
            sb.append("height=");
            sb.append(MyString.getEquidistanceString(g.height + "", 5));
            sb.append("xoffset=");
            sb.append(MyString.getEquidistanceString(g.xoffset + "", 5));
            sb.append("yoffset=");
            int yoffset = g.yoffset;
            if (!flipped) {
                yoffset = Math.abs(g.yoffset + g.height);
            }
            sb.append(MyString.getEquidistanceString(yoffset + "", 5));
            sb.append("xadvance=");
            sb.append(MyString.getEquidistanceString(g.xadvance + "", 5));
            sb.append("page=");
            sb.append(MyString.getEquidistanceString(g.page + "", 5));
            sb.append("chnl=0\n"); //默认不使用
        }
        //line 6
        sb.append("kernings count=");
        sb.append(bitMapCharKerningArray.size);
        //line 7
        for (int i = 0; i < bitMapCharKerningArray.size; ++i) {
            KerningData kd = bitMapCharKerningArray.get(i);
            BitmapFont.Glyph g = getGlyph((char) kd.first);
            sb.append("\nkerning first=");
            sb.append(kd.first);
            sb.append(" second=");
            sb.append(kd.second);
            sb.append(" amount=");
            sb.append(g.getKerning((char) kd.second));
        }

        writeFileHandle.writeString(sb.toString(), false);
    }

    public class ExtraData {
        public BitmapFont.Glyph glyph;
        public int offsetY;

        public ExtraData(BitmapFont.Glyph glyph, int offsetY) {
            this.glyph = glyph;
            this.offsetY = offsetY;
        }
    }

    public class KerningData {
        public int first;
        public int second;

        public KerningData(int first, int second) {
            this.first = first;
            this.second = second;
        }
    }
}

//package com.mygdx.game.Logic.Fnt;
///**
// * 添加bitmapfontdata中没存储的变量
// */
//
//import com.badlogic.gdx.files.FileHandle;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.utils.Array;
//import com.badlogic.gdx.utils.GdxRuntimeException;
//import com.badlogic.gdx.utils.StreamUtils;
//import com.mygdx.game.Logic.ToolInterface.WriteStringInterface;
//import com.mygdx.game.Logic.Tools.StringTool;
//import com.mygdx.game.Struct.IntegerVector2;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.util.StringTokenizer;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class MyBitmapFontData extends BitmapFont.BitmapFontData implements WriteStringInterface {
//    protected String face;
//    protected int size;
//    protected int bold;
//    protected int italic;
//    protected String charset;
//    protected int unicode;
//    protected int stretchH;
//    protected int smooth;
//    protected int aa;
//    protected int spacingX;
//    protected int spacingY;
//    protected int scaleW;
//    protected int scaleH;
//    protected int baseLine = 1;
//    protected int pageCount = 1;
//    protected int packed;
//    protected int kerningCount;
//    protected Array<Character> charArrays = new Array<>();
//
//    public MyBitmapFontData() {
//    }
//
//    public MyBitmapFontData(FileHandle fontFile, boolean flip) {
//        this.fontFile = fontFile;
//        this.flipped = flip;
//        load(fontFile, flip);
//    }
//
//    @Override
//    public void load(FileHandle fontFile, boolean flip) {
//        if (imagePaths != null) throw new IllegalStateException("Already loaded.");
//
//        BufferedReader reader = new BufferedReader(new InputStreamReader(fontFile.read()), 512);
//        try {
//            String line = reader.readLine(); // info
//            if (line == null) throw new GdxRuntimeException("File is empty.");
//            //get face
//            {
//                String[] common = line.split(" ", 12);
//                if (!common[1].startsWith("face")) {
//                    throw new GdxRuntimeException("file error: no found face");
//                }
//                face = common[1].substring(5, common[1].length() - 1);
//                if (!common[2].startsWith("size")) {
//                    throw new GdxRuntimeException("file error: no found size");
//                }
//                size = Integer.parseInt(common[2].substring(5));
//                if (!common[3].startsWith("bold")) {
//                    throw new GdxRuntimeException("file error: no found bold");
//                }
//                bold = Integer.parseInt(common[3].substring(5));
//                if (!common[4].startsWith("italic")) {
//                    throw new GdxRuntimeException("file error: no found italic");
//                }
//                italic = Integer.parseInt(common[4].substring(7));
//
//                if (!common[5].startsWith("charset")) {
//                    throw new GdxRuntimeException("file error: no found charset");
//                }
//                charset = common[5].substring(8, common[5].length() - 1);
//
//                if (!common[6].startsWith("unicode")) {
//                    throw new GdxRuntimeException("file error: no found unicode");
//                }
//                unicode = Integer.parseInt(common[6].substring(8));
//
//                if (!common[7].startsWith("stretchH")) {
//                    throw new GdxRuntimeException("file error: no found stretchH");
//                }
//                stretchH = Integer.parseInt(common[7].substring(9));
//
//                if (!common[8].startsWith("smooth")) {
//                    throw new GdxRuntimeException("file error: no found smooth");
//                }
//                smooth = Integer.parseInt(common[8].substring(7));
//
//                if (!common[9].startsWith("aa")) {
//                    throw new GdxRuntimeException("file error: no found aa");
//                }
//                aa = Integer.parseInt(common[9].substring(3));
//
//                if (!common[10].startsWith("padding")) {
//                    throw new GdxRuntimeException("file error: no found padding");
//                }
//                String[] padding = common[10].substring(8).split(",", 4);
//                if (padding.length != 4) throw new GdxRuntimeException("Invalid padding.");
//                padTop = Integer.parseInt(padding[0]);
//                padRight = Integer.parseInt(padding[1]);
//                padBottom = Integer.parseInt(padding[2]);
//                padLeft = Integer.parseInt(padding[3]);
//
//                if (!common[11].startsWith("spacing")) {
//                    throw new GdxRuntimeException("file error: no found spacing");
//                }
//                String[] spacing = common[11].substring(8).split(",", 2);
//                if (spacing.length != 2) throw new GdxRuntimeException("Invalid spacing.");
//                spacingX = Integer.parseInt(spacing[0]);
//                spacingY = Integer.parseInt(spacing[1]);
//            }
//            {
////            line = line.substring(line.indexOf("padding=") + 8);
////            String[] padding = line.substring(0, line.indexOf(' ')).split(",", 4);
////            if (padding.length != 4) throw new GdxRuntimeException("Invalid padding.");
////            padTop = Integer.parseInt(padding[0]);
////            padRight = Integer.parseInt(padding[1]);
////            padBottom = Integer.parseInt(padding[2]);
////            padLeft = Integer.parseInt(padding[3]);
//            }
//            float padY = padTop + padBottom;
//
//            line = reader.readLine();
//            if (line == null) throw new GdxRuntimeException("Missing common header.");
//            String[] common = line.split(" ", 7); // At most we want the 6th element; i.e. "page=N"
//
//            // At least lineHeight and base are required.
//            if (common.length < 3) throw new GdxRuntimeException("Invalid common header.");
//
//            if (!common[1].startsWith("lineHeight="))
//                throw new GdxRuntimeException("Missing: lineHeight");
//            lineHeight = Integer.parseInt(common[1].substring(11));
//
//            if (!common[2].startsWith("base=")) throw new GdxRuntimeException("Missing: base");
//            baseLine = Integer.parseInt(common[2].substring(5));
//
//            {
//                if (!common[3].startsWith("scaleW")) {
//                    throw new GdxRuntimeException("Missing: scaleW");
//                }
//                scaleW = Integer.parseInt(common[3].substring(7));
//
//                if (!common[4].startsWith("scaleH")) {
//                    throw new GdxRuntimeException("Missing: scaleH");
//                }
//                scaleH = Integer.parseInt(common[4].substring(7));
//            }
//
//
//            if (common.length >= 6 && common[5] != null && common[5].startsWith("pages=")) {
//                try {
//                    pageCount = Math.max(1, Integer.parseInt(common[5].substring(6)));
//                } catch (NumberFormatException ignored) { // Use one page.
//                }
//            }
//
//            {
//                if (!common[6].endsWith("packed")) {
//                    throw new GdxRuntimeException("Missing: packed");
//                }
//                packed = Integer.parseInt(common[6].substring(7));
//            }
//
//            imagePaths = new String[pageCount];
//
//            // Read each page definition.
//            for (int p = 0; p < pageCount; p++) {
//                // Read each "page" info line.
//                line = reader.readLine();
//                if (line == null)
//                    throw new GdxRuntimeException("Missing additional page definitions.");
//
//                // Expect ID to mean "index".
//                Matcher matcher = Pattern.compile(".*id=(\\d+)").matcher(line);
//                if (matcher.find()) {
//                    String id = matcher.group(1);
//                    try {
//                        int pageID = Integer.parseInt(id);
//                        if (pageID != p)
//                            throw new GdxRuntimeException("Page IDs must be indices starting at 0: " + id);
//                    } catch (NumberFormatException ex) {
//                        throw new GdxRuntimeException("Invalid page id: " + id, ex);
//                    }
//                }
//
//                matcher = Pattern.compile(".*file=\"?([^\"]+)\"?").matcher(line);
//                if (!matcher.find()) throw new GdxRuntimeException("Missing: file");
//                String fileName = matcher.group(1);
//
//                imagePaths[p] = fontFile.parent().child(fileName).path().replaceAll("\\\\", "/");
//            }
//
//            descent = 0;
//
//            while (true) {
//                line = reader.readLine();
//                if (line == null) break; // EOF
//                if (line.startsWith("kernings ")) break; // Starting kernings block.
//                if (!line.startsWith("char ")) continue;
//
//                BitmapFont.Glyph glyph = new BitmapFont.Glyph();
//
//                StringTokenizer tokens = new StringTokenizer(line, " =");
//                tokens.nextToken();
//                tokens.nextToken();
//
//                int ch = Integer.parseInt(tokens.nextToken());
//                if (ch <= 0)
//                    missingGlyph = glyph;
//                else if (ch <= Character.MAX_VALUE) {
//                    setGlyph(ch, glyph);
//                    charArrays.add((char) ch);
//                } else
//                    continue;
//                glyph.id = ch;
//                tokens.nextToken();
//                glyph.srcX = Integer.parseInt(tokens.nextToken());
//                tokens.nextToken();
//                glyph.srcY = Integer.parseInt(tokens.nextToken());
//                tokens.nextToken();
//                glyph.width = Integer.parseInt(tokens.nextToken());
//                tokens.nextToken();
//                glyph.height = Integer.parseInt(tokens.nextToken());
//                tokens.nextToken();
//                glyph.xoffset = Integer.parseInt(tokens.nextToken());
//                tokens.nextToken();
//                if (flip)
//                    glyph.yoffset = Integer.parseInt(tokens.nextToken());
//                else
//                    glyph.yoffset = -(glyph.height + Integer.parseInt(tokens.nextToken()));
//                tokens.nextToken();
//                glyph.xadvance = Integer.parseInt(tokens.nextToken());
//
//                // Check for page safely, it could be omitted or invalid.
//                if (tokens.hasMoreTokens()) tokens.nextToken();
//                if (tokens.hasMoreTokens()) {
//                    try {
//                        glyph.page = Integer.parseInt(tokens.nextToken());
//                    } catch (NumberFormatException ignored) {
//                    }
//                }
//
//                if (glyph.width > 0 && glyph.height > 0)
//                    descent = Math.min(baseLine + glyph.yoffset, descent);
//
//            }
//            descent += padBottom;
//
//            while (true) {
//                line = reader.readLine();
//                if (line == null) break;
//                if (!line.startsWith("kerning ")) break;
//
//                StringTokenizer tokens = new StringTokenizer(line, " =");
//                tokens.nextToken();
//                tokens.nextToken();
//                int first = Integer.parseInt(tokens.nextToken());
//                tokens.nextToken();
//                int second = Integer.parseInt(tokens.nextToken());
//                if (first < 0 || first > Character.MAX_VALUE || second < 0 || second > Character.MAX_VALUE)
//                    continue;
//                BitmapFont.Glyph glyph = getGlyph((char) first);
//                tokens.nextToken();
//                int amount = Integer.parseInt(tokens.nextToken());
//                if (glyph != null) { // Kernings may exist for glyph pairs not contained in the font.
//                    glyph.setKerning(second, amount);
//
//                }
//                kerningCount++;
//            }
//
//            BitmapFont.Glyph spaceGlyph = getGlyph(' ');
//            if (spaceGlyph == null) {
//                spaceGlyph = new BitmapFont.Glyph();
//                spaceGlyph.id = (int) ' ';
//                BitmapFont.Glyph xadvanceGlyph = getGlyph('l');
//                if (xadvanceGlyph == null) xadvanceGlyph = getFirstGlyph();
//                spaceGlyph.xadvance = xadvanceGlyph.xadvance;
//                setGlyph(' ', spaceGlyph);
//            }
//            if (spaceGlyph.width == 0) {
//                spaceGlyph.width = (int) (padLeft + spaceGlyph.xadvance + padRight);
//                spaceGlyph.xoffset = (int) -padLeft;
//            }
//            spaceXadvance = spaceGlyph.xadvance;
//
//            BitmapFont.Glyph xGlyph = null;
//            for (char xChar : xChars) {
//                xGlyph = getGlyph(xChar);
//                if (xGlyph != null) break;
//            }
//            if (xGlyph == null) xGlyph = getFirstGlyph();
//            xHeight = xGlyph.height - padY;
//
//            BitmapFont.Glyph capGlyph = null;
//            for (char capChar : capChars) {
//                capGlyph = getGlyph(capChar);
//                if (capGlyph != null) break;
//            }
//            if (capGlyph == null) {
//                for (BitmapFont.Glyph[] page : this.glyphs) {
//                    if (page == null) continue;
//                    for (BitmapFont.Glyph glyph : page) {
//                        if (glyph == null || glyph.height == 0 || glyph.width == 0) continue;
//                        capHeight = Math.max(capHeight, glyph.height);
//                    }
//                }
//            } else
//                capHeight = capGlyph.height;
//            capHeight -= padY;
//
//            ascent = baseLine - capHeight;
//            down = -lineHeight;
//            if (flip) {
//                ascent = -ascent;
//                down = -down;
//            }
//        } catch (Exception ex) {
//            throw new GdxRuntimeException("Error loading font file: " + fontFile, ex);
//        } finally {
//            StreamUtils.closeQuietly(reader);
//        }
//    }
//
//    public void setLineHeight(float lineHeight) {
//        this.lineHeight = lineHeight;
//    }
//
//    public float getLineHeight() {
//        return lineHeight;
//    }
//
//    public void setCharSize(BitmapFont.Glyph chGlyph, int width, int height) {
//        if (chGlyph != null) {
//            chGlyph.width = width;
//            chGlyph.height = height;
//        }
//    }
//
//    public IntegerVector2 getCharSize(BitmapFont.Glyph chGlyph) {
//        if (chGlyph != null) {
//            return new IntegerVector2(chGlyph.width, chGlyph.height);
//        }
//        return null;
//    }
//
//    public void setCharPosition(BitmapFont.Glyph chGlyph, int x, int y) {
//        if (chGlyph != null) {
//            chGlyph.srcX = x;
//            chGlyph.srcY = y;
//        }
//    }
//
//    public IntegerVector2 getCharPosition(BitmapFont.Glyph chGlyph) {
//        if (chGlyph != null) {
//            return new IntegerVector2(chGlyph.srcX, chGlyph.srcY);
//        }
//        return null;
//    }
//
//    public void setCharOffset(BitmapFont.Glyph chGlyph, int offsetX, int offsetY) {
//        if (chGlyph != null) {
//            chGlyph.xoffset = offsetX;
//            chGlyph.yoffset = offsetY;
//        }
//    }
//
//
//    public BitmapFont.Glyph getGlyph(char ch) {
//        return getGlyph(ch);
//    }
//
//    @Override
//    public String writeString() {
//        StringBuffer stringBuffer = new StringBuffer();
//        //title:
//        //line 1:
//        stringBuffer.append("info face=\"");
//        stringBuffer.append(face);
//        stringBuffer.append("\" size=");
//        stringBuffer.append(size);
//        stringBuffer.append(" bold=");
//        stringBuffer.append(bold);
//        stringBuffer.append(" italic=");
//        stringBuffer.append(italic);
//        stringBuffer.append(" charset=\"");
//        stringBuffer.append(charset);
//        stringBuffer.append("\" unicode=");
//        stringBuffer.append(unicode);
//        stringBuffer.append(" stretchH=");
//        stringBuffer.append(stretchH);
//        stringBuffer.append(" smooth=");
//        stringBuffer.append(smooth);
//        stringBuffer.append(" aa=");
//        stringBuffer.append(aa);
//        stringBuffer.append(" padding=");
//        stringBuffer.append(padTop + "," + padRight + "," + padBottom + "," + padLeft);
//        stringBuffer.append("\n");
//        //line2:
//        stringBuffer.append("common lineHeight=");
//        stringBuffer.append(lineHeight);
//        stringBuffer.append(" base=");
//        stringBuffer.append(baseLine);
//        stringBuffer.append(" scaleW=");
//        stringBuffer.append(scaleW);
//        stringBuffer.append(" scaleH=");
//        stringBuffer.append(scaleH);
//        stringBuffer.append(" pages=");
//        stringBuffer.append(pageCount);
//        stringBuffer.append(" packed=");
//        stringBuffer.append(packed);
//        stringBuffer.append("\n");
//        //line 3:
//        for (int i = 0; i < imagePaths.length; ++i) {
//            stringBuffer.append("page id=");
//            stringBuffer.append(i);
//            stringBuffer.append(" file=\"");
//            stringBuffer.append(imagePaths[i] + "\"\n");
//        }
//        //line 4:
//        stringBuffer.append("chars count=");
//        stringBuffer.append(charArrays.size);
//        stringBuffer.append("\n");
//
//        //char:
//        //line 5:
//        for (int i = 0, j = 0; i < glyphs.length; ++i) {
//            if (glyphs != null) continue;
//            char c = (char) i;
//            System.out.println("char is: " + c);
//            BitmapFont.Glyph glyph = getGlyph(c);
//            if (glyph != null) continue;
//            j++;
//
//            stringBuffer.append("char id=");
//            stringBuffer.append(StringTool.getAssignmentLengthString(glyph.id + "", 8, false));
//            stringBuffer.append("x=");
//            stringBuffer.append(StringTool.getAssignmentLengthString(glyph.srcX + "", 5, false));
//            stringBuffer.append("y=");
//            stringBuffer.append(StringTool.getAssignmentLengthString(glyph.srcY + "", 5, false));
//            stringBuffer.append("width=");
//            stringBuffer.append(StringTool.getAssignmentLengthString(glyph.width + "", 5, false));
//            stringBuffer.append("height=");
//            stringBuffer.append(StringTool.getAssignmentLengthString(glyph.height + "", 5, false));
//            stringBuffer.append("xoffset=");
//            stringBuffer.append(StringTool.getAssignmentLengthString(glyph.xoffset + "", 5, false));
//            stringBuffer.append("yoffset=");
//            stringBuffer.append(StringTool.getAssignmentLengthString(glyph.yoffset + "", 5, false));
//            stringBuffer.append("xadvance=");
//            stringBuffer.append(StringTool.getAssignmentLengthString(glyph.xadvance + "", 5, false));
//            stringBuffer.append("page=");
//            stringBuffer.append(StringTool.getAssignmentLengthString(glyph.page + "", 5, false));
//            stringBuffer.append("chnl=0\n");
//        }
//        //kernings:
//        //line 6:
//        stringBuffer.append("kernings count=");
//        stringBuffer.append(kerningCount);
//        stringBuffer.append("\n");
//        //line 7:
//        for (int i = 0; i < charArrays.size; ++i) {
//            BitmapFont.Glyph glyph = getGlyph(charArrays.get(i));
//            for (int j = 0; j < charArrays.size; ++j) {
//                if (j == i) continue;
//                int value = glyph.getKerning((char) j);
//                if (value != 0) {
//                    stringBuffer.append("kerning first=");
//                    stringBuffer.append((int) charArrays.get(i));
//                    stringBuffer.append(" secound=");
//                    stringBuffer.append((int) charArrays.get(j));
//                    stringBuffer.append(" amount=");
//                    stringBuffer.append(value);
//                    stringBuffer.append("\n");
//                }
//            }
//        }
//
//        return stringBuffer.toString();
//    }
//
//    @Override
//    public void write(FileHandle fileHandle) {
//
//    }
//}

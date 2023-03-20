package com.mygdx.game.Logic.Function.Label.FntAdjust;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Logic.Function.Label.MyBitmapFontData;
import com.mygdx.game.Logic.ToolInterface.ReadWriteInterface;

public class AdjustLabel implements ReadWriteInterface {
    private MyBitmapFontData data;
    private DebugBitmapFont bitmapFont;

    private void initBitmapFontArray(FileHandle fntFileHandle) {
        data = new MyBitmapFontData(fntFileHandle, false);
        bitmapFont = new DebugBitmapFont(data, (TextureRegion) null, true);
    }

    public void setLineHeight(BitmapFont bitmapFont, float lineHeight) {
        bitmapFont.getData().lineHeight += lineHeight;
        bitmapFont.getData().down = -bitmapFont.getData().lineHeight;
    }

    public void setCharacterWidth(BitmapFont bitmapFont, char c, int width) {
        setCharacterSize(bitmapFont, c, width, 0);
    }

    public void setCharacterHeight(BitmapFont bitmapFont, char c, int height) {
        setCharacterSize(bitmapFont, c, 0, height);
        if (height > 0) {
            ((MyBitmapFontData) bitmapFont.getData()).adjustVariationValue();
        }
    }

    public void setCharacterSize(BitmapFont bitmapFont, char c, int width, int height) {
        BitmapFont.Glyph g = bitmapFont.getData().getGlyph(c);
        g.width = width;
        g.height = height;
        changeSize(g.width, g.height);
    }

    public void setCharacterStringWidth(String s, int offsetX) {
        setCharacterStringSize(s, offsetX, 0);
    }

    public void setCharacterStringHeight(String s, int offsetY) {
        setCharacterStringSize(s, 0, offsetY);
    }

    public void setCharacterStringSize(String s, int offsetX, int offsetY) {
        new AdjustStringRunable(s, offsetX, offsetY) {
            @Override
            public void deal(MyBitmapFontData data, BitmapFont.Glyph glyph, int offsetX, int offsetY) {
                glyph.width += offsetX;
                glyph.height += offsetY;
                bitmapFont.loadFontRegionChar(glyph);
            }
        };

        if (offsetY > 0) {
            data.adjustVariationValue();
        }
        changeSize(offsetX, offsetY);

    }

    public void setCharacterWidthAll(int widthOffset) {
        setCharacterSizeAll(widthOffset, 0);
    }

    public void setCharacterHeightAll(int heightOffset) {
        setCharacterSizeAll(0, heightOffset);
    }

    public void setCharacterSizeAll(int widthOffset, int heightOffset) {
        new AdjustAllRunable(data, widthOffset, heightOffset) {
            @Override
            public void deal(MyBitmapFontData data, BitmapFont.Glyph glyph, int offsetX, int offsetY) {
                glyph.width += offsetX;
                glyph.height += offsetY;
                bitmapFont.loadFontRegionChar(glyph);
            }
        };

        if (heightOffset > 0) {
            data.adjustVariationValue();
        }

        changeSize(widthOffset, heightOffset);
    }

    public void setCharacterX(char c, int x) {
        setCharacterPosition(c, x, 0);
    }

    public void setCharacterY(char c, int y) {
        setCharacterPosition(c, 0, y);
    }

    public void setCharacterPosition(char c, int x, int y) {
        BitmapFont.Glyph g = bitmapFont.getData().getGlyph(c);
        g.srcX = x;
        g.srcY = y;
        changePosition(g.width, g.height);
    }

    public void setCharacterStringX(String s, int offsetX) {
        setCharacterStringPosition(s, offsetX, 0);
    }

    public void setCharacterStringY(String s, int offsetY) {
        setCharacterStringPosition(s, 0, offsetY);
    }

    public void setCharacterStringPosition(String s, int offsetX, int offsetY) {
        new AdjustStringRunable(s, offsetX, offsetY) {
            @Override
            public void deal(MyBitmapFontData data, BitmapFont.Glyph glyph, int offsetX, int offsetY) {
                glyph.srcX += offsetX;
                glyph.srcY += offsetY;
                bitmapFont.loadFontRegionChar(glyph);
            }
        };
        changePosition(offsetX, offsetY);
    }

    public void setCharacterXAll(int offsetX) {
        setCharacterPositionAll(offsetX, 0);
    }

    public void setCharacterYAll(int offsetY) {
        setCharacterPositionAll(0, offsetY);
    }

    public void setCharacterPositionAll(int offsetX, int offsetY) {
        new AdjustAllRunable(data, offsetX, offsetY) {
            @Override
            public void deal(MyBitmapFontData data, BitmapFont.Glyph glyph, int offsetX, int offsetY) {
                glyph.srcX += offsetX;
                glyph.srcY += offsetY;
                bitmapFont.loadFontRegionChar(glyph);
            }
        };
        changePosition(offsetX, offsetY);
    }

    public void setCharacterStringOffsetX(String s, int offsetX) {
        setCharacterStringOffset(s, offsetX, 0);
    }

    public void setCharacterStringOffsetY(String s, int offsetY) {
        setCharacterStringOffset(s, 0, offsetY);
    }

    public void setCharacterStringOffset(String s, int offsetX, int offsetY) {
        new AdjustStringRunable(s, offsetX, offsetY) {
            @Override
            public void deal(MyBitmapFontData data, BitmapFont.Glyph glyph, int offsetX, int offsetY) {
                glyph.xoffset += offsetX;
                glyph.yoffset += offsetY;
            }
        };

        if (offsetY > 0) {
            updataDescent();
        }
    }

    public void setCharacterOffsetXAll(int offsetX) {
        setCharacterOffsetAll(offsetX, 0);
    }

    public void setCharacterOffsetYAll(int offsetY) {
        setCharacterOffsetAll(0, offsetY);
    }

    public void setCharacterOffsetAll(int offsetX, int offsetY) {
        new AdjustAllRunable(data, offsetX, offsetY) {
            @Override
            public void deal(MyBitmapFontData data, BitmapFont.Glyph glyph, int offsetX, int offsetY) {
                glyph.xoffset += offsetX;
                glyph.yoffset += offsetY;
            }
        };
        if (offsetY > 0) {
            updataDescent();
        }
    }

    public void setCharacterOffsetX(char c, int offsetX) {
        data.getGlyph(c).xoffset += offsetX;
    }

    public void setCharacterOffsetY(char c, int offsetY) {
        data.getGlyph(c).yoffset += offsetY;
        updataDescent();
    }

    public void setXAdvance(char c, int advanceWidth) {
        data.getGlyph(c).xadvance = advanceWidth;
    }

    public void setStringXAdvance(String s, int offset) {
        new AdjustStringRunable(s, offset, 0) {
            @Override
            public void deal(MyBitmapFontData data, BitmapFont.Glyph glyph, int offsetX, int offsetY) {
                glyph.xadvance += offsetX;
            }
        };
    }

    public void setXAdvanceAll(int advanceWidthOffset) {
        new AdjustAllRunable(data, advanceWidthOffset, 0) {
            @Override
            public void deal(MyBitmapFontData data, BitmapFont.Glyph glyph, int offsetX, int offsetY) {
                glyph.xadvance += offsetX;
            }
        };
    }

    public void setBaseLine(float baseLine) {
        data.baseLine += baseLine;
        data.resetDescent();
        data.adjustVariationValue();

        System.out.println("ooooooooooooooooooooooooooooooooooooooooooooooooooo:  " + data.baseLine + "  lineHeight: " + data.lineHeight);
//        updataDescent();
    }

    public void updataDescent(BitmapFont.Glyph glyph) {
        if (glyph.width > 0 && glyph.height > 0) {
            data.descent = Math.min(((MyBitmapFontData) data).baseLine + glyph.yoffset, data.descent);
        }
    }

    public void updataDescent() {
        new AdjustAllRunable(data, 0, 0) {
            @Override
            public void deal(MyBitmapFontData data, BitmapFont.Glyph glyph, int offsetX, int offsetY) {
                data.descent = Math.min(data.baseLine + glyph.yoffset, data.descent);
            }
        };

    }


    public BitmapFont.Glyph getGlyPh(BitmapFont bitmapFont, int charId) {
        BitmapFont.Glyph[] page = bitmapFont.getData().glyphs[charId];
        if (page != null) {
            return page[charId];
        }
        return null;
    }

    public DebugBitmapFont getBitMapFont() {
        return bitmapFont;
    }


    public void changeSize(int width, int height) {

    }

    public void changePosition(int x, int y) {

    }

    @Override
    public void read(FileHandle fileHandle) {
        initBitmapFontArray(fileHandle);
    }

    @Override
    public void write(FileHandle fileHandle) {
    }

    //用來修改全体char的回调函数
    public class AdjustAllRunable {
        public AdjustAllRunable(MyBitmapFontData data, int offsetX, int offsetY) {
            adjustAll(data, offsetX, offsetY);
        }

        public AdjustAllRunable adjustAll(MyBitmapFontData data, int offsetX, int offsetY) {
            for (int ch : data.bitMapCharArray) {
                BitmapFont.Glyph g = data.getGlyph((char) ch);
                if (g != null && g.width > 0 && g.height > 0) {
                    deal(data, g, offsetX, offsetY);
                }
            }
            return this;
        }

        public void deal(MyBitmapFontData data, BitmapFont.Glyph glyph, int offsetX, int offsetY) {

        }
    }

    public class AdjustStringRunable {
        public AdjustStringRunable(String string, int offsetX, int offsetY) {
            if (string == null || string.toCharArray().length <= 0) {
                throw new RuntimeException("string is NULL or string.length is 0");
            } else {
                char[] chars = string.toCharArray();
                for (char c : chars) {
                    deal(data, bitmapFont.getData().getGlyph(c), offsetX, offsetY);
                }
            }
        }

        public void deal(MyBitmapFontData data, BitmapFont.Glyph glyph, int offsetX, int offsetY) {

        }
    }

    public MyBitmapFontData getBitmapFontData() {
        return data;
    }

}

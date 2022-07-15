package com.mygdx.game.Logic.Fnt;

public class FntTitleData {
    public String face;
    public int size, bold, italic;
    public String charset;
    public int unicode, stretchH, smooth, aa, spacingX, spacingY;
    public int padTop, padRight, padBottom, padLeft;
    public int lineHeight, base, scaleW, scaleH, pages, packed;
    public String[] files;

    @Override
    public String toString() {
        String s = "info"
                + " face=\"" + face + "\""
                + " size=" + size
                + " bold=" + bold
                + " italic=" + italic
                + " charset=\"" + charset + "\""
                + " unicode=" + unicode
                + " stretchH=" + stretchH
                + " smooth=" + smooth
                + " aa=" + aa
                + " padding=" + padTop + "," + padRight + "," + padBottom + "," + padLeft
                + " spacing=" + spacingX + "," + spacingY
                + "\n"
                + "common"
                + " lineHeight=" + lineHeight
                + " base=" + base
                + " scaleW=" + scaleW
                + " scaleH=" + scaleH
                + " pages=" + pages
                + " packed=" + packed
                + "\n"
                + "page";
        for (int i = 0; i < files.length; ++i) {
            s += " id=" + i + " file=\"" + files[i] + "\"";
        }
        return s;
    }
}

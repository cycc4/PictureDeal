package com.mygdx.game.Logic.Function.Label.GeneratorBitmapFont;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.Logic.ToolInterface.ReadWriteInterface;

public class AtlasToFnt implements ReadWriteInterface {
    private TextureAtlas.TextureAtlasData data;
    private String[] pages;
    private String fontName;
    private int fontSize;
    private int maxHeight;
    private int maxPageW;
    private int maxPageH;

    @Override
    public void read(FileHandle fileHandle) {
        data = new TextureAtlas.TextureAtlasData(
                fileHandle, fileHandle.parent(), false);

    }

    @Override
    public void write(FileHandle fileHandle) {
        StringBuffer sb = new StringBuffer();
        //line1
        sb.append("info face=\"");
        sb.append(fontName);
        sb.append("\" size=");
        sb.append(fontSize);
        sb.append(" bold=0 italic=0 charset=\"\" unicode=0 stretchH=100 smooth=1 aa=1 padding=0,0,0,0 spacing=1,1\n");
        //line2
        sb.append("common lineHeight=");
        sb.append(maxHeight);
        sb.append(" base=");
        sb.append(maxHeight);
        sb.append(" scaleW=");
        sb.append(maxPageW);
        sb.append(" scaleH=");
        sb.append(maxHeight);
        sb.append(" pages=");
        sb.append(data.getPages().size);
        sb.append(" packed=0\n");
        //line3
        for (int i=0; i<data.getPages().size; ++i){
            sb.append("page ");
            sb.append("id=");
        }
    }
}

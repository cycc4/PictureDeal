package com.mygdx.game.Draw.Layout.Label.AdjustBitmapFont;


import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.Res;
import com.qs.utils.actor.ActorFactory;
import com.qs.utils.assets.Assets;

public class CharGroup {
    private Label name;
    public Label x;
    public Label y;
    public Label width;
    public Label height;
    public Label xoffset;
    public Label yoffset;
    public Label xadvance;

    private BitmapFont.Glyph glyph;
    private Group rootGroup;

    public CharGroup(BitmapFont.Glyph glyph) {
        this.glyph = glyph;
        rootGroup = ActorFactory.createCsdJson(Assets.getIns().getAssetManager(), Res.LAYOUT_DIR + "Fnt/charData.json");
        name = rootGroup.findActor("title");
        name.setText(((char) glyph.id) + "");

        x = rootGroup.findActor("x");
        y = rootGroup.findActor("y");
        width = rootGroup.findActor("width");
        height = rootGroup.findActor("height");
        xoffset = rootGroup.findActor("xoffset");
        yoffset = rootGroup.findActor("yoffset");
        xadvance = rootGroup.findActor("xadvance");
    }

    public void updataChar() {
        if (glyph == null) {
            return;
        }
        x.setText(glyph.srcX);
        y.setText(glyph.srcY);
        xoffset.setText(glyph.xoffset);
        yoffset.setText(glyph.yoffset);
        width.setText(glyph.width);
        height.setText(glyph.height);
        xadvance.setText(glyph.xadvance);
    }

    public Group getRootGroup(){
        return rootGroup;
    }
}

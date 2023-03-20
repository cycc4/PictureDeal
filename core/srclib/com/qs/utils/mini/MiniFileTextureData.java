/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.qs.utils.mini;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class MiniFileTextureData implements TextureData {

    final FileHandle file;
    final float scale;
    int rawWidth = 0;
    int rawHeight = 0;
    Format format;
    Pixmap pixmap;
    boolean isPrepared = false;

    public MiniFileTextureData(FileHandle file, Pixmap preloadedPixmap, Format format, float scale) {
        this.file = file;
        this.pixmap = preloadedPixmap;
        this.format = format;
        this.scale = scale;
        if (pixmap != null) {
            pixmap = scalePixmap(pixmap);
            if (format == null)
                this.format = pixmap.getFormat();
        }
    }

    @Override
    public boolean isPrepared() {
        return isPrepared;
    }

    @Override
    public void prepare() {
        if (isPrepared)
            throw new GdxRuntimeException("Already prepared");
        if (pixmap == null) {
            if (file.extension().equals("cim"))
                pixmap = PixmapIO.readCIM(file);
            else
                pixmap = scalePixmap(new Pixmap(file));
            if (format == null)
                format = pixmap.getFormat();
        }
        isPrepared = true;
    }

    private Pixmap scalePixmap(Pixmap rawPixmap) {
        rawWidth = rawPixmap.getWidth();
        rawHeight = rawPixmap.getHeight();
        int scaleWidth = Math.round(rawWidth * scale);
        int scaleHeight = Math.round(rawHeight * scale);

        Pixmap tmp = new Pixmap(scaleWidth, scaleHeight, Format.RGBA4444);
        tmp.setBlending(Pixmap.Blending.None);
        tmp.drawPixmap(rawPixmap, 0, 0, rawWidth, rawHeight, 0, 0, scaleWidth, scaleHeight);
        rawPixmap.dispose();
        return tmp;
    }

    @Override
    public Pixmap consumePixmap() {
        if (!isPrepared)
            throw new GdxRuntimeException("Call prepare() before calling getPixmap()");
        isPrepared = false;
        Pixmap pixmap = this.pixmap;
        this.pixmap = null;
        return pixmap;
    }

    @Override
    public boolean disposePixmap() {
        return true;
    }

    @Override
    public int getWidth() {
        return MathUtils.round(rawWidth);
    }

    @Override
    public int getHeight() {
        return MathUtils.round(rawHeight);
    }

    @Override
    public Format getFormat() {
        return format;
    }

    @Override
    public boolean useMipMaps() {
        return false;
    }

    @Override
    public boolean isManaged() {
        return true;
    }

    public FileHandle getFileHandle() {
        return file;
    }

    @Override
    public TextureDataType getType() {
        return TextureDataType.Pixmap;
    }

    @Override
    public void consumeCustomData(int target) {
        throw new GdxRuntimeException("This TextureData implementation does not upload data itself");
    }
}

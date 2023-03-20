package com.qs.utils.batch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.NumberUtils;

public class CpuPolygonSpriteBatch extends PolygonSpriteBatch {
    static final int VERTEX_SIZE = 2 + 1 + 2;
    static final int SPRITE_SIZE = 4 * VERTEX_SIZE;
    protected final float[] vertices;
    protected final short[] triangles;
    private final Matrix4 transformMatrix = new Matrix4();
    private final Matrix4 projectionMatrix = new Matrix4();
    private final Matrix4 combinedMatrix = new Matrix4();
    private final ShaderProgram shader;
    //-------------------------------------------------------------------------------------------------------------------------
    private final Matrix4 virtualMatrix = new Matrix4();
    private final Affine2 adjustAffine = new Affine2();
    private final Affine2 tmpAffine = new Affine2();
    protected int vertexIndex, triangleIndex;
    protected Texture lastTexture;
    protected float invTexWidth = 0, invTexHeight = 0;
    protected boolean drawing;
    float color = Color.WHITE.toFloatBits();
    private Mesh mesh;
    private Mesh[] buffers;
    private int bufferIndex;
    private boolean blendingDisabled;
    private int blendSrcFunc = GL20.GL_SRC_ALPHA;
    private int blendDstFunc = GL20.GL_ONE_MINUS_SRC_ALPHA;
    private int blendSrcFuncAlpha = GL20.GL_SRC_ALPHA;
    private int blendDstFuncAlpha = GL20.GL_ONE_MINUS_SRC_ALPHA;

//	public int renderCalls = 0;

//	public int totalRenderCalls = 0;

    //	public int maxTrianglesInBatch = 0;
    private ShaderProgram customShader;
    private boolean ownsShader;
    private Color tempColor = new Color(1, 1, 1, 1);
    private boolean adjustNeeded;
    private boolean haveIdentityRealMatrix = true;

    public CpuPolygonSpriteBatch() {
        this(2000, null);
    }

    public CpuPolygonSpriteBatch(int size) {
        this(size, size * 2, 1, null);
    }

    public CpuPolygonSpriteBatch(int size, ShaderProgram defaultShader) {
        this(size, size * 2, 1, defaultShader);
    }

    public CpuPolygonSpriteBatch(int maxVertices, int maxTriangles, int buffers, ShaderProgram defaultShader) {
        // 32767 is max vertex index.
        if (maxVertices > 32767)
            throw new IllegalArgumentException("Can't have more than 32767 vertices per batch: " + maxVertices);

        Mesh.VertexDataType vertexDataType = Mesh.VertexDataType.VertexArray;
        if (Gdx.gl30 != null) {
            vertexDataType = Mesh.VertexDataType.VertexBufferObjectWithVAO;
        }

        this.buffers = new Mesh[buffers];
        for (int i = 0; i < buffers; i++) {
            this.buffers[i] = new Mesh(vertexDataType, false, maxVertices, maxTriangles * 3,
                    new VertexAttribute(VertexAttributes.Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE),
                    new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE),
                    new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));
        }
        mesh = this.buffers[0];

        vertices = new float[maxVertices * VERTEX_SIZE];
        triangles = new short[maxTriangles * 3];

        if (defaultShader == null) {
            shader = SpriteBatch.createDefaultShader();
            ownsShader = true;
        } else
            shader = defaultShader;

        projectionMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private static boolean checkEqual(Matrix4 a, Matrix4 b) {
        if (a == b)
            return true;

        // matrices are assumed to be 2D transformations
        return (a.val[Matrix4.M00] == b.val[Matrix4.M00] && a.val[Matrix4.M10] == b.val[Matrix4.M10]
                && a.val[Matrix4.M01] == b.val[Matrix4.M01] && a.val[Matrix4.M11] == b.val[Matrix4.M11]
                && a.val[Matrix4.M03] == b.val[Matrix4.M03] && a.val[Matrix4.M13] == b.val[Matrix4.M13]);
    }

    private static boolean checkEqual(Matrix4 matrix, Affine2 affine) {
        final float[] val = matrix.getValues();

        // matrix is assumed to be 2D transformation
        return (val[Matrix4.M00] == affine.m00 && val[Matrix4.M10] == affine.m10 && val[Matrix4.M01] == affine.m01
                && val[Matrix4.M11] == affine.m11 && val[Matrix4.M03] == affine.m02 && val[Matrix4.M13] == affine.m12);
    }

    private static boolean checkIdt(Matrix4 matrix) {
        final float[] val = matrix.getValues();

        // matrix is assumed to be 2D transformation
        return (val[Matrix4.M00] == 1 && val[Matrix4.M10] == 0 && val[Matrix4.M01] == 0 && val[Matrix4.M11] == 1
                && val[Matrix4.M03] == 0 && val[Matrix4.M13] == 0);
    }

    public int getTriangleIndex() {
        return triangleIndex;
    }

    public void begin() {
        if (drawing)
            throw new IllegalStateException("PolygonSpriteBatch.end must be called before begin.");
        renderCalls = 0;
        maxTrianglesInBatch = 0;

        Gdx.gl.glDepthMask(false);
        if (customShader != null)
            customShader.begin();
        else
            shader.begin();
        setupMatrices();

        drawing = true;
    }

    public void end() {
        if (!drawing)
            throw new IllegalStateException("PolygonSpriteBatch.begin must be called before end.");
        if (vertexIndex > 0)
            flush();
        lastTexture = null;
        drawing = false;

        Gdx.gl.glDepthMask(true);
        if (isBlendingEnabled())
            Gdx.gl.glDisable(GL20.GL_BLEND);

        if (customShader != null)
            customShader.end();
        else
            shader.end();
    }

    public void setColor(float r, float g, float b, float a) {
        int intBits = (int) (255 * a) << 24 | (int) (255 * b) << 16 | (int) (255 * g) << 8 | (int) (255 * r);
        color = NumberUtils.intToFloatColor(intBits);
    }

    public Color getColor() {
        int intBits = NumberUtils.floatToIntColor(color);
        Color color = this.tempColor;
        color.r = (intBits & 0xff) / 255f;
        color.g = ((intBits >>> 8) & 0xff) / 255f;
        color.b = ((intBits >>> 16) & 0xff) / 255f;
        color.a = ((intBits >>> 24) & 0xff) / 255f;
        return color;
    }

    public void setColor(Color tint) {
        color = tint.toFloatBits();
    }

    public void setColor(float color) {
        this.color = color;
    }

    public float getPackedColor() {
        return color;
    }

    public void flush() {
        if (vertexIndex == 0)
            return;

        renderCalls++;
        totalRenderCalls++;
        int trianglesInBatch = triangleIndex;
        if (trianglesInBatch > maxTrianglesInBatch)
            maxTrianglesInBatch = trianglesInBatch;

        lastTexture.bind();
        Mesh mesh = this.mesh;
        mesh.setVertices(vertices, 0, vertexIndex);
        mesh.setIndices(triangles, 0, triangleIndex);
        if (blendingDisabled) {
            Gdx.gl.glDisable(GL20.GL_BLEND);
        } else {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            if (blendSrcFunc != -1) {
                Gdx.gl20.glBlendFuncSeparate(blendSrcFunc, blendDstFunc, blendSrcFuncAlpha, blendDstFuncAlpha);
            }
        }

        mesh.render(customShader != null ? customShader : shader, GL20.GL_TRIANGLES, 0, trianglesInBatch);

        vertexIndex = 0;
        triangleIndex = 0;
        bufferIndex++;
        if (bufferIndex == buffers.length)
            bufferIndex = 0;
        this.mesh = buffers[bufferIndex];
    }

    public void disableBlending() {
        flush();
        blendingDisabled = true;
    }

    public void enableBlending() {
        flush();
        blendingDisabled = false;
    }

    public void setBlendFunction(int srcFunc, int dstFunc) {
        setBlendFunctionSeparate(srcFunc, dstFunc, srcFunc, dstFunc);
    }

    public void setBlendFunctionSeparate(int srcFuncColor, int dstFuncColor, int srcFuncAlpha, int dstFuncAlpha) {
        if (blendSrcFunc == srcFuncColor && blendDstFunc == dstFuncColor && blendSrcFuncAlpha == srcFuncAlpha
                && blendDstFuncAlpha == dstFuncAlpha)
            return;
        flush();
        blendSrcFunc = srcFuncColor;
        blendDstFunc = dstFuncColor;
        blendSrcFuncAlpha = srcFuncAlpha;
        blendDstFuncAlpha = dstFuncAlpha;
    }

    public int getBlendSrcFunc() {
        return blendSrcFunc;
    }

    public int getBlendDstFunc() {
        return blendDstFunc;
    }

    public int getBlendSrcFuncAlpha() {
        return blendSrcFuncAlpha;
    }

    public int getBlendDstFuncAlpha() {
        return blendDstFuncAlpha;
    }

    public void dispose() {
        for (int i = 0; i < buffers.length; i++)
            buffers[i].dispose();
        if (ownsShader && shader != null)
            shader.dispose();
    }

    public Matrix4 getProjectionMatrix() {
        return projectionMatrix;
    }

    public void setProjectionMatrix(Matrix4 projection) {
        if (drawing)
            flush();
        projectionMatrix.set(projection);
        if (drawing)
            setupMatrices();
    }

    protected void setupMatrices() {
        combinedMatrix.set(projectionMatrix).mul(transformMatrix);
        if (customShader != null) {
            customShader.setUniformMatrix("u_projTrans", combinedMatrix);
            customShader.setUniformi("u_texture", 0);
        } else {
            shader.setUniformMatrix("u_projTrans", combinedMatrix);
            shader.setUniformi("u_texture", 0);
        }
    }

    protected void switchTexture(Texture texture) {
        flush();
        lastTexture = texture;
        invTexWidth = 1.0f / texture.getWidth();
        invTexHeight = 1.0f / texture.getHeight();
    }

    public ShaderProgram getShader() {
        if (customShader == null) {
            return shader;
        }
        return customShader;
    }

    public void setShader(ShaderProgram shader) {
        if (drawing) {
            flush();
            if (customShader != null)
                customShader.end();
            else
                this.shader.end();
        }
        customShader = shader;
        if (drawing) {
            if (customShader != null)
                customShader.begin();
            else
                this.shader.begin();
            setupMatrices();
        }
    }

    public boolean isBlendingEnabled() {
        return !blendingDisabled;
    }

    public boolean isDrawing() {
        return drawing;
    }

    public void flushAndSyncTransformMatrix() {
        flush();

        if (adjustNeeded) {
            // vertices flushed, safe now to replace matrix
            haveIdentityRealMatrix = checkIdt(virtualMatrix);

            if (!haveIdentityRealMatrix && virtualMatrix.det() == 0)
                throw new GdxRuntimeException("Transform matrix is singular, can't sync");

            adjustNeeded = false;
            if (drawing)
                flush();
            transformMatrix.set(virtualMatrix);
            if (drawing)
                setupMatrices();
        }
    }

    @Override
    public Matrix4 getTransformMatrix() {
        return (adjustNeeded ? virtualMatrix : transformMatrix);
    }

    @Override
    public void setTransformMatrix(Matrix4 transform) {
        Matrix4 realMatrix = transformMatrix;

        if (checkEqual(realMatrix, transform)) {
            adjustNeeded = false;
        } else {
            if (isDrawing()) {
                virtualMatrix.setAsAffine(transform);
                adjustNeeded = true;

                // adjust = inverse(real) x virtual
                // real x adjust x vertex = virtual x vertex

                if (haveIdentityRealMatrix) {
                    adjustAffine.set(transform);
                } else {
                    tmpAffine.set(transform);
                    adjustAffine.set(realMatrix).inv().mul(tmpAffine);
                }
            } else {
                realMatrix.setAsAffine(transform);
                haveIdentityRealMatrix = checkIdt(realMatrix);
            }
        }
    }

    public void setTransformMatrix(Affine2 transform) {
        Matrix4 realMatrix = transformMatrix;

        if (checkEqual(realMatrix, transform)) {
            adjustNeeded = false;
        } else {
            virtualMatrix.setAsAffine(transform);

            if (isDrawing()) {
                adjustNeeded = true;

                // adjust = inverse(real) x virtual
                // real x adjust x vertex = virtual x vertex

                if (haveIdentityRealMatrix) {
                    adjustAffine.set(transform);
                } else {
                    adjustAffine.set(realMatrix).inv().mul(transform);
                }
            } else {
                realMatrix.setAsAffine(transform);
                haveIdentityRealMatrix = checkIdt(realMatrix);
            }
        }
    }

    public void draw(PolygonRegion region, float x, float y) {
        if (!adjustNeeded) {
            if (!drawing)
                throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles1 = this.triangles;
            final short[] regionTriangles = region.getTriangles();
            final int regionTrianglesLength = regionTriangles.length;
            final float[] regionVertices = region.getVertices();
            final int regionVerticesLength = regionVertices.length;

            final Texture texture = region.getRegion().getTexture();
            if (texture != lastTexture)
                switchTexture(texture);
            else if (triangleIndex + regionTrianglesLength > triangles1.length
                    || vertexIndex + regionVerticesLength > vertices.length)
                flush();

            int triangleIndex1 = this.triangleIndex;
            int vertexIndex1 = this.vertexIndex;
            final int startVertex = vertexIndex1 / VERTEX_SIZE;

            for (int i = 0; i < regionTrianglesLength; i++)
                triangles1[triangleIndex1++] = (short) (regionTriangles[i] + startVertex);
            this.triangleIndex = triangleIndex1;

            final float[] vertices1 = this.vertices;
            final float color1 = this.color;
            final float[] textureCoords = region.getTextureCoords();

            for (int i = 0; i < regionVerticesLength; i += 2) {
                vertices1[vertexIndex1++] = regionVertices[i] + x;
                vertices1[vertexIndex1++] = regionVertices[i + 1] + y;
                vertices1[vertexIndex1++] = color1;
                vertices1[vertexIndex1++] = textureCoords[i];
                vertices1[vertexIndex1++] = textureCoords[i + 1];
            }
            this.vertexIndex = vertexIndex1;
        } else {
            if (!drawing)
                throw new IllegalStateException("CpuPolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles = this.triangles;
            final short[] regionTriangles = region.getTriangles();
            final int regionTrianglesLength = regionTriangles.length;
            final float[] regionVertices = region.getVertices();
            final int regionVerticesLength = regionVertices.length;

            final Texture texture = region.getRegion().getTexture();
            if (texture != lastTexture)
                switchTexture(texture);
            else if (triangleIndex + regionTrianglesLength > triangles.length
                    || vertexIndex + regionVerticesLength > vertices.length)
                flush();

            int triangleIndex = this.triangleIndex;
            int vertexIndex = this.vertexIndex;
            final int startVertex = vertexIndex / VERTEX_SIZE;

            for (int i = 0; i < regionTrianglesLength; i++)
                triangles[triangleIndex++] = (short) (regionTriangles[i] + startVertex);
            this.triangleIndex = triangleIndex;

            final float[] vertices = this.vertices;
            final float color = this.color;
            final float[] textureCoords = region.getTextureCoords();

            Affine2 t = adjustAffine;
            for (int i = 0; i < regionVerticesLength; i += 2) {
                float x1 = regionVertices[i] + x;
                float y1 = regionVertices[i + 1] + y;
                vertices[vertexIndex++] = t.m00 * x1 + t.m01 * y1 + t.m02;
                vertices[vertexIndex++] = t.m10 * x1 + t.m11 * y1 + t.m12;
                vertices[vertexIndex++] = color;
                vertices[vertexIndex++] = textureCoords[i];
                vertices[vertexIndex++] = textureCoords[i + 1];
            }
            this.vertexIndex = vertexIndex;
        }
    }

    public void draw(PolygonRegion region, float x, float y, float originX, float originY, float width, float height,
                     float scaleX, float scaleY, float rotation) {
        if (!adjustNeeded) {
            if (!drawing)
                throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles1 = this.triangles;
            final short[] regionTriangles = region.getTriangles();
            final int regionTrianglesLength = regionTriangles.length;
            final float[] regionVertices = region.getVertices();
            final int regionVerticesLength = regionVertices.length;
            final TextureRegion textureRegion = region.getRegion();

            Texture texture = textureRegion.getTexture();
            if (texture != lastTexture)
                switchTexture(texture);
            else if (triangleIndex + regionTrianglesLength > triangles1.length
                    || vertexIndex + regionVerticesLength > vertices.length)
                flush();

            int triangleIndex1 = this.triangleIndex;
            int vertexIndex1 = this.vertexIndex;
            final int startVertex = vertexIndex1 / VERTEX_SIZE;

            for (int i = 0; i < regionTrianglesLength; i++)
                triangles1[triangleIndex1++] = (short) (regionTriangles[i] + startVertex);
            this.triangleIndex = triangleIndex1;

            final float[] vertices1 = this.vertices;
            final float color1 = this.color;
            final float[] textureCoords = region.getTextureCoords();

            final float worldOriginX = x + originX;
            final float worldOriginY = y + originY;
            final float sX = width / textureRegion.getRegionWidth();
            final float sY = height / textureRegion.getRegionHeight();
            final float cos = MathUtils.cosDeg(rotation);
            final float sin = MathUtils.sinDeg(rotation);

            float fx, fy;
            for (int i = 0; i < regionVerticesLength; i += 2) {
                fx = (regionVertices[i] * sX - originX) * scaleX;
                fy = (regionVertices[i + 1] * sY - originY) * scaleY;
                vertices1[vertexIndex1++] = cos * fx - sin * fy + worldOriginX;
                vertices1[vertexIndex1++] = sin * fx + cos * fy + worldOriginY;
                vertices1[vertexIndex1++] = color1;
                vertices1[vertexIndex1++] = textureCoords[i];
                vertices1[vertexIndex1++] = textureCoords[i + 1];
            }
            this.vertexIndex = vertexIndex1;
        } else {
            if (!drawing)
                throw new IllegalStateException("CpuPolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles = this.triangles;
            final short[] regionTriangles = region.getTriangles();
            final int regionTrianglesLength = regionTriangles.length;
            final float[] regionVertices = region.getVertices();
            final int regionVerticesLength = regionVertices.length;
            final TextureRegion textureRegion = region.getRegion();

            Texture texture = textureRegion.getTexture();
            if (texture != lastTexture)
                switchTexture(texture);
            else if (triangleIndex + regionTrianglesLength > triangles.length
                    || vertexIndex + regionVerticesLength > vertices.length)
                flush();

            int triangleIndex = this.triangleIndex;
            int vertexIndex = this.vertexIndex;
            final int startVertex = vertexIndex / VERTEX_SIZE;

            for (int i = 0; i < regionTrianglesLength; i++)
                triangles[triangleIndex++] = (short) (regionTriangles[i] + startVertex);
            this.triangleIndex = triangleIndex;

            final float[] vertices = this.vertices;
            final float color = this.color;
            final float[] textureCoords = region.getTextureCoords();

            final float worldOriginX = x + originX;
            final float worldOriginY = y + originY;
            final float sX = width / textureRegion.getRegionWidth();
            final float sY = height / textureRegion.getRegionHeight();
            final float cos = MathUtils.cosDeg(rotation);
            final float sin = MathUtils.sinDeg(rotation);

            float fx, fy;
            Affine2 t = adjustAffine;
            for (int i = 0; i < regionVerticesLength; i += 2) {
                fx = (regionVertices[i] * sX - originX) * scaleX;
                fy = (regionVertices[i + 1] * sY - originY) * scaleY;
                float x1 = cos * fx - sin * fy + worldOriginX;
                float y1 = sin * fx + cos * fy + worldOriginY;
                vertices[vertexIndex++] = t.m00 * x1 + t.m01 * y1 + t.m02;
                vertices[vertexIndex++] = t.m10 * x1 + t.m11 * y1 + t.m12;
                vertices[vertexIndex++] = color;
                vertices[vertexIndex++] = textureCoords[i];
                vertices[vertexIndex++] = textureCoords[i + 1];
            }
            this.vertexIndex = vertexIndex;
        }
    }

    public void draw(PolygonRegion region, float x, float y, float width, float height) {
        if (!adjustNeeded) {
            if (!drawing)
                throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles1 = this.triangles;
            final short[] regionTriangles = region.getTriangles();
            final int regionTrianglesLength = regionTriangles.length;
            final float[] regionVertices = region.getVertices();
            final int regionVerticesLength = regionVertices.length;
            final TextureRegion textureRegion = region.getRegion();

            final Texture texture = textureRegion.getTexture();
            if (texture != lastTexture)
                switchTexture(texture);
            else if (triangleIndex + regionTrianglesLength > triangles1.length
                    || vertexIndex + regionVerticesLength > vertices.length)
                flush();

            int triangleIndex1 = this.triangleIndex;
            int vertexIndex1 = this.vertexIndex;
            final int startVertex = vertexIndex1 / VERTEX_SIZE;

            for (int i = 0, n = regionTriangles.length; i < n; i++)
                triangles1[triangleIndex1++] = (short) (regionTriangles[i] + startVertex);
            this.triangleIndex = triangleIndex1;

            final float[] vertices1 = this.vertices;
            final float color1 = this.color;
            final float[] textureCoords = region.getTextureCoords();
            final float sX = width / textureRegion.getRegionWidth();
            final float sY = height / textureRegion.getRegionHeight();

            for (int i = 0; i < regionVerticesLength; i += 2) {
                vertices1[vertexIndex1++] = regionVertices[i] * sX + x;
                vertices1[vertexIndex1++] = regionVertices[i + 1] * sY + y;
                vertices1[vertexIndex1++] = color1;
                vertices1[vertexIndex1++] = textureCoords[i];
                vertices1[vertexIndex1++] = textureCoords[i + 1];
            }
            this.vertexIndex = vertexIndex1;
        } else {
            if (!drawing)
                throw new IllegalStateException("CpuPolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles = this.triangles;
            final short[] regionTriangles = region.getTriangles();
            final int regionTrianglesLength = regionTriangles.length;
            final float[] regionVertices = region.getVertices();
            final int regionVerticesLength = regionVertices.length;
            final TextureRegion textureRegion = region.getRegion();

            final Texture texture = textureRegion.getTexture();
            if (texture != lastTexture)
                switchTexture(texture);
            else if (triangleIndex + regionTrianglesLength > triangles.length
                    || vertexIndex + regionVerticesLength > vertices.length)
                flush();

            int triangleIndex = this.triangleIndex;
            int vertexIndex = this.vertexIndex;
            final int startVertex = vertexIndex / VERTEX_SIZE;

            for (int i = 0, n = regionTriangles.length; i < n; i++)
                triangles[triangleIndex++] = (short) (regionTriangles[i] + startVertex);
            this.triangleIndex = triangleIndex;

            final float[] vertices = this.vertices;
            final float color = this.color;
            final float[] textureCoords = region.getTextureCoords();
            final float sX = width / textureRegion.getRegionWidth();
            final float sY = height / textureRegion.getRegionHeight();

            Affine2 t = adjustAffine;
            for (int i = 0; i < regionVerticesLength; i += 2) {
                float x1 = regionVertices[i] * sX + x;
                float y1 = regionVertices[i + 1] * sY + y;
                vertices[vertexIndex++] = t.m00 * x1 + t.m01 * y1 + t.m02;
                vertices[vertexIndex++] = t.m10 * x1 + t.m11 * y1 + t.m12;
                vertices[vertexIndex++] = color;
                vertices[vertexIndex++] = textureCoords[i];
                vertices[vertexIndex++] = textureCoords[i + 1];
            }
            this.vertexIndex = vertexIndex;
        }
    }

    @Override
    public void draw(Texture texture, float x, float y) {
        if (!adjustNeeded) {
            if (!drawing)
                throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles1 = this.triangles;
            final float[] vertices1 = this.vertices;

            if (texture != lastTexture)
                switchTexture(texture);
            else if (triangleIndex + 6 > triangles1.length || vertexIndex + SPRITE_SIZE > vertices1.length) //
                flush();

            int triangleIndex1 = this.triangleIndex;
            final int startVertex = vertexIndex / VERTEX_SIZE;
            triangles1[triangleIndex1++] = (short) startVertex;
            triangles1[triangleIndex1++] = (short) (startVertex + 1);
            triangles1[triangleIndex1++] = (short) (startVertex + 2);
            triangles1[triangleIndex1++] = (short) (startVertex + 2);
            triangles1[triangleIndex1++] = (short) (startVertex + 3);
            triangles1[triangleIndex1++] = (short) startVertex;
            this.triangleIndex = triangleIndex1;

            final float fx2 = x + (float) texture.getWidth();
            final float fy2 = y + (float) texture.getHeight();
            final float u = 0;
            final float v = 1;
            final float u2 = 1;
            final float v2 = 0;

            float color1 = this.color;
            int idx = this.vertexIndex;
            vertices1[idx++] = x;
            vertices1[idx++] = y;
            vertices1[idx++] = color1;
            vertices1[idx++] = u;
            vertices1[idx++] = v;

            vertices1[idx++] = x;
            vertices1[idx++] = fy2;
            vertices1[idx++] = color1;
            vertices1[idx++] = u;
            vertices1[idx++] = v2;

            vertices1[idx++] = fx2;
            vertices1[idx++] = fy2;
            vertices1[idx++] = color1;
            vertices1[idx++] = u2;
            vertices1[idx++] = v2;

            vertices1[idx++] = fx2;
            vertices1[idx++] = y;
            vertices1[idx++] = color1;
            vertices1[idx++] = u2;
            vertices1[idx++] = v;
            this.vertexIndex = idx;
        } else {
            float width = texture.getWidth();
            float height = texture.getHeight();
            if (!drawing)
                throw new IllegalStateException("CpuPolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles = this.triangles;
            final float[] vertices = this.vertices;

            if (texture != lastTexture)
                switchTexture(texture);
            else if (triangleIndex + 6 > triangles.length || vertexIndex + SPRITE_SIZE > vertices.length) //
                flush();

            int triangleIndex = this.triangleIndex;
            final int startVertex = vertexIndex / VERTEX_SIZE;
            triangles[triangleIndex++] = (short) startVertex;
            triangles[triangleIndex++] = (short) (startVertex + 1);
            triangles[triangleIndex++] = (short) (startVertex + 2);
            triangles[triangleIndex++] = (short) (startVertex + 2);
            triangles[triangleIndex++] = (short) (startVertex + 3);
            triangles[triangleIndex++] = (short) startVertex;
            this.triangleIndex = triangleIndex;

            final float fx2 = x + width;
            final float fy2 = y + height;
            final float u = 0;
            final float v = 1;
            final float u2 = 1;
            final float v2 = 0;

            float color = this.color;
            int idx = this.vertexIndex;
            Affine2 t = adjustAffine;
            vertices[idx++] = t.m00 * x + t.m01 * y + t.m02;
            vertices[idx++] = t.m10 * x + t.m11 * y + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u;
            vertices[idx++] = v;

            vertices[idx++] = t.m00 * x + t.m01 * fy2 + t.m02;
            vertices[idx++] = t.m10 * x + t.m11 * fy2 + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u;
            vertices[idx++] = v2;

            vertices[idx++] = t.m00 * fx2 + t.m01 * fy2 + t.m02;
            vertices[idx++] = t.m10 * fx2 + t.m11 * fy2 + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u2;
            vertices[idx++] = v2;

            vertices[idx++] = t.m00 * fx2 + t.m01 * y + t.m02;
            vertices[idx++] = t.m10 * fx2 + t.m11 * y + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u2;
            vertices[idx++] = v;
            this.vertexIndex = idx;
        }
    }

    @Override
    public void draw(Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX,
                     float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {
        if (!adjustNeeded) {
            if (!drawing)
                throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles1 = this.triangles;
            final float[] vertices1 = this.vertices;

            if (texture != lastTexture)
                switchTexture(texture);
            else if (triangleIndex + 6 > triangles1.length || vertexIndex + SPRITE_SIZE > vertices1.length) //
                flush();

            int triangleIndex1 = this.triangleIndex;
            final int startVertex = vertexIndex / VERTEX_SIZE;
            triangles1[triangleIndex1++] = (short) startVertex;
            triangles1[triangleIndex1++] = (short) (startVertex + 1);
            triangles1[triangleIndex1++] = (short) (startVertex + 2);
            triangles1[triangleIndex1++] = (short) (startVertex + 2);
            triangles1[triangleIndex1++] = (short) (startVertex + 3);
            triangles1[triangleIndex1++] = (short) startVertex;
            this.triangleIndex = triangleIndex1;

            // bottom left and top right corner points relative to origin
            final float worldOriginX = x + originX;
            final float worldOriginY = y + originY;
            float fx = -originX;
            float fy = -originY;
            float fx2 = width - originX;
            float fy2 = height - originY;

            // scale
            if (scaleX != 1 || scaleY != 1) {
                fx *= scaleX;
                fy *= scaleY;
                fx2 *= scaleX;
                fy2 *= scaleY;
            }

            // construct corner points, start from top left and go counter clockwise
            final float p1x = fx;
            final float p1y = fy;
            final float p2x = fx;
            final float p2y = fy2;
            final float p3x = fx2;
            final float p3y = fy2;
            final float p4x = fx2;
            final float p4y = fy;

            float x1;
            float y1;
            float x2;
            float y2;
            float x3;
            float y3;
            float x4;
            float y4;

            // rotate
            if (rotation != 0) {
                final float cos = MathUtils.cosDeg(rotation);
                final float sin = MathUtils.sinDeg(rotation);

                x1 = cos * p1x - sin * p1y;
                y1 = sin * p1x + cos * p1y;

                x2 = cos * p2x - sin * p2y;
                y2 = sin * p2x + cos * p2y;

                x3 = cos * p3x - sin * p3y;
                y3 = sin * p3x + cos * p3y;

                x4 = x1 + (x3 - x2);
                y4 = y3 - (y2 - y1);
            } else {
                x1 = p1x;
                y1 = p1y;

                x2 = p2x;
                y2 = p2y;

                x3 = p3x;
                y3 = p3y;

                x4 = p4x;
                y4 = p4y;
            }

            x1 += worldOriginX;
            y1 += worldOriginY;
            x2 += worldOriginX;
            y2 += worldOriginY;
            x3 += worldOriginX;
            y3 += worldOriginY;
            x4 += worldOriginX;
            y4 += worldOriginY;

            float u = srcX * invTexWidth;
            float v = (srcY + srcHeight) * invTexHeight;
            float u2 = (srcX + srcWidth) * invTexWidth;
            float v2 = srcY * invTexHeight;

            if (flipX) {
                float tmp = u;
                u = u2;
                u2 = tmp;
            }

            if (flipY) {
                float tmp = v;
                v = v2;
                v2 = tmp;
            }

            float color1 = this.color;
            int idx = this.vertexIndex;
            vertices1[idx++] = x1;
            vertices1[idx++] = y1;
            vertices1[idx++] = color1;
            vertices1[idx++] = u;
            vertices1[idx++] = v;

            vertices1[idx++] = x2;
            vertices1[idx++] = y2;
            vertices1[idx++] = color1;
            vertices1[idx++] = u;
            vertices1[idx++] = v2;

            vertices1[idx++] = x3;
            vertices1[idx++] = y3;
            vertices1[idx++] = color1;
            vertices1[idx++] = u2;
            vertices1[idx++] = v2;

            vertices1[idx++] = x4;
            vertices1[idx++] = y4;
            vertices1[idx++] = color1;
            vertices1[idx++] = u2;
            vertices1[idx++] = v;
            this.vertexIndex = idx;
        } else {
            if (!drawing)
                throw new IllegalStateException("CpuPolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles = this.triangles;
            final float[] vertices = this.vertices;

            if (texture != lastTexture)
                switchTexture(texture);
            else if (triangleIndex + 6 > triangles.length || vertexIndex + SPRITE_SIZE > vertices.length) //
                flush();

            int triangleIndex = this.triangleIndex;
            final int startVertex = vertexIndex / VERTEX_SIZE;
            triangles[triangleIndex++] = (short) startVertex;
            triangles[triangleIndex++] = (short) (startVertex + 1);
            triangles[triangleIndex++] = (short) (startVertex + 2);
            triangles[triangleIndex++] = (short) (startVertex + 2);
            triangles[triangleIndex++] = (short) (startVertex + 3);
            triangles[triangleIndex++] = (short) startVertex;
            this.triangleIndex = triangleIndex;

            // bottom left and top right corner points relative to origin
            final float worldOriginX = x + originX;
            final float worldOriginY = y + originY;
            float fx = -originX;
            float fy = -originY;
            float fx2 = width - originX;
            float fy2 = height - originY;

            // scale
            if (scaleX != 1 || scaleY != 1) {
                fx *= scaleX;
                fy *= scaleY;
                fx2 *= scaleX;
                fy2 *= scaleY;
            }

            // construct corner points, start from top left and go counter clockwise
            final float p1x = fx;
            final float p1y = fy;
            final float p2x = fx;
            final float p2y = fy2;
            final float p3x = fx2;
            final float p3y = fy2;
            final float p4x = fx2;
            final float p4y = fy;

            float x1;
            float y1;
            float x2;
            float y2;
            float x3;
            float y3;
            float x4;
            float y4;

            // rotate
            if (rotation != 0) {
                final float cos = MathUtils.cosDeg(rotation);
                final float sin = MathUtils.sinDeg(rotation);

                x1 = cos * p1x - sin * p1y;
                y1 = sin * p1x + cos * p1y;

                x2 = cos * p2x - sin * p2y;
                y2 = sin * p2x + cos * p2y;

                x3 = cos * p3x - sin * p3y;
                y3 = sin * p3x + cos * p3y;

                x4 = x1 + (x3 - x2);
                y4 = y3 - (y2 - y1);
            } else {
                x1 = p1x;
                y1 = p1y;

                x2 = p2x;
                y2 = p2y;

                x3 = p3x;
                y3 = p3y;

                x4 = p4x;
                y4 = p4y;
            }

            x1 += worldOriginX;
            y1 += worldOriginY;
            x2 += worldOriginX;
            y2 += worldOriginY;
            x3 += worldOriginX;
            y3 += worldOriginY;
            x4 += worldOriginX;
            y4 += worldOriginY;

            float u = srcX * invTexWidth;
            float v = (srcY + srcHeight) * invTexHeight;
            float u2 = (srcX + srcWidth) * invTexWidth;
            float v2 = srcY * invTexHeight;

            if (flipX) {
                float tmp = u;
                u = u2;
                u2 = tmp;
            }

            if (flipY) {
                float tmp = v;
                v = v2;
                v2 = tmp;
            }

            float color = this.color;
            int idx = this.vertexIndex;
            Affine2 t = adjustAffine;
            vertices[idx++] = t.m00 * x1 + t.m01 * y1 + t.m02;
            vertices[idx++] = t.m10 * x1 + t.m11 * y1 + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u;
            vertices[idx++] = v;

            vertices[idx++] = t.m00 * x2 + t.m01 * y2 + t.m02;
            vertices[idx++] = t.m10 * x2 + t.m11 * y2 + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u;
            vertices[idx++] = v2;

            vertices[idx++] = t.m00 * x3 + t.m01 * y3 + t.m02;
            vertices[idx++] = t.m10 * x3 + t.m11 * y3 + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u2;
            vertices[idx++] = v2;

            vertices[idx++] = t.m00 * x4 + t.m01 * y4 + t.m02;
            vertices[idx++] = t.m10 * x4 + t.m11 * y4 + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u2;
            vertices[idx++] = v;
            this.vertexIndex = idx;
        }
    }

    @Override
    public void draw(Texture texture, float x, float y, float width, float height) {
        if (!adjustNeeded) {
            if (!drawing)
                throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles1 = this.triangles;
            final float[] vertices1 = this.vertices;

            if (texture != lastTexture)
                switchTexture(texture);
            else if (triangleIndex + 6 > triangles1.length || vertexIndex + SPRITE_SIZE > vertices1.length) //
                flush();

            int triangleIndex1 = this.triangleIndex;
            final int startVertex = vertexIndex / VERTEX_SIZE;
            triangles1[triangleIndex1++] = (short) startVertex;
            triangles1[triangleIndex1++] = (short) (startVertex + 1);
            triangles1[triangleIndex1++] = (short) (startVertex + 2);
            triangles1[triangleIndex1++] = (short) (startVertex + 2);
            triangles1[triangleIndex1++] = (short) (startVertex + 3);
            triangles1[triangleIndex1++] = (short) startVertex;
            this.triangleIndex = triangleIndex1;

            final float fx2 = x + width;
            final float fy2 = y + height;
            final float u = 0;
            final float v = 1;
            final float u2 = 1;
            final float v2 = 0;

            float color1 = this.color;
            int idx = this.vertexIndex;
            vertices1[idx++] = x;
            vertices1[idx++] = y;
            vertices1[idx++] = color1;
            vertices1[idx++] = u;
            vertices1[idx++] = v;

            vertices1[idx++] = x;
            vertices1[idx++] = fy2;
            vertices1[idx++] = color1;
            vertices1[idx++] = u;
            vertices1[idx++] = v2;

            vertices1[idx++] = fx2;
            vertices1[idx++] = fy2;
            vertices1[idx++] = color1;
            vertices1[idx++] = u2;
            vertices1[idx++] = v2;

            vertices1[idx++] = fx2;
            vertices1[idx++] = y;
            vertices1[idx++] = color1;
            vertices1[idx++] = u2;
            vertices1[idx++] = v;
            this.vertexIndex = idx;
        } else {
            if (!drawing)
                throw new IllegalStateException("CpuPolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles = this.triangles;
            final float[] vertices = this.vertices;

            if (texture != lastTexture)
                switchTexture(texture);
            else if (triangleIndex + 6 > triangles.length || vertexIndex + SPRITE_SIZE > vertices.length) //
                flush();

            int triangleIndex = this.triangleIndex;
            final int startVertex = vertexIndex / VERTEX_SIZE;
            triangles[triangleIndex++] = (short) startVertex;
            triangles[triangleIndex++] = (short) (startVertex + 1);
            triangles[triangleIndex++] = (short) (startVertex + 2);
            triangles[triangleIndex++] = (short) (startVertex + 2);
            triangles[triangleIndex++] = (short) (startVertex + 3);
            triangles[triangleIndex++] = (short) startVertex;
            this.triangleIndex = triangleIndex;

            final float fx2 = x + width;
            final float fy2 = y + height;
            final float u = 0;
            final float v = 1;
            final float u2 = 1;
            final float v2 = 0;

            float color = this.color;
            int idx = this.vertexIndex;
            Affine2 t = adjustAffine;
            vertices[idx++] = t.m00 * x + t.m01 * y + t.m02;
            vertices[idx++] = t.m10 * x + t.m11 * y + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u;
            vertices[idx++] = v;

            vertices[idx++] = t.m00 * x + t.m01 * fy2 + t.m02;
            vertices[idx++] = t.m10 * x + t.m11 * fy2 + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u;
            vertices[idx++] = v2;

            vertices[idx++] = t.m00 * fx2 + t.m01 * fy2 + t.m02;
            vertices[idx++] = t.m10 * fx2 + t.m11 * fy2 + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u2;
            vertices[idx++] = v2;

            vertices[idx++] = t.m00 * fx2 + t.m01 * y + t.m02;
            vertices[idx++] = t.m10 * fx2 + t.m11 * y + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u2;
            vertices[idx++] = v;
            this.vertexIndex = idx;
        }
    }

    @Override
    public void draw(Texture texture, float x, float y, float width, float height, float u, float v, float u2, float v2) {
        if (!adjustNeeded) {
            if (!drawing)
                throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles1 = this.triangles;
            final float[] vertices1 = this.vertices;

            if (texture != lastTexture)
                switchTexture(texture);
            else if (triangleIndex + 6 > triangles1.length || vertexIndex + SPRITE_SIZE > vertices1.length) //
                flush();

            int triangleIndex1 = this.triangleIndex;
            final int startVertex = vertexIndex / VERTEX_SIZE;
            triangles1[triangleIndex1++] = (short) startVertex;
            triangles1[triangleIndex1++] = (short) (startVertex + 1);
            triangles1[triangleIndex1++] = (short) (startVertex + 2);
            triangles1[triangleIndex1++] = (short) (startVertex + 2);
            triangles1[triangleIndex1++] = (short) (startVertex + 3);
            triangles1[triangleIndex1++] = (short) startVertex;
            this.triangleIndex = triangleIndex1;

            final float fx2 = x + width;
            final float fy2 = y + height;

            float color1 = this.color;
            int idx = this.vertexIndex;
            vertices1[idx++] = x;
            vertices1[idx++] = y;
            vertices1[idx++] = color1;
            vertices1[idx++] = u;
            vertices1[idx++] = v;

            vertices1[idx++] = x;
            vertices1[idx++] = fy2;
            vertices1[idx++] = color1;
            vertices1[idx++] = u;
            vertices1[idx++] = v2;

            vertices1[idx++] = fx2;
            vertices1[idx++] = fy2;
            vertices1[idx++] = color1;
            vertices1[idx++] = u2;
            vertices1[idx++] = v2;

            vertices1[idx++] = fx2;
            vertices1[idx++] = y;
            vertices1[idx++] = color1;
            vertices1[idx++] = u2;
            vertices1[idx++] = v;
            this.vertexIndex = idx;
        } else {
            if (!drawing)
                throw new IllegalStateException("CpuPolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles = this.triangles;
            final float[] vertices = this.vertices;

            if (texture != lastTexture)
                switchTexture(texture);
            else if (triangleIndex + 6 > triangles.length || vertexIndex + SPRITE_SIZE > vertices.length) //
                flush();

            int triangleIndex = this.triangleIndex;
            final int startVertex = vertexIndex / VERTEX_SIZE;
            triangles[triangleIndex++] = (short) startVertex;
            triangles[triangleIndex++] = (short) (startVertex + 1);
            triangles[triangleIndex++] = (short) (startVertex + 2);
            triangles[triangleIndex++] = (short) (startVertex + 2);
            triangles[triangleIndex++] = (short) (startVertex + 3);
            triangles[triangleIndex++] = (short) startVertex;
            this.triangleIndex = triangleIndex;

            final float fx2 = x + width;
            final float fy2 = y + height;

            float color = this.color;
            int idx = this.vertexIndex;
            Affine2 t = adjustAffine;
            vertices[idx++] = t.m00 * x + t.m01 * y + t.m02;
            vertices[idx++] = t.m10 * x + t.m11 * y + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u;
            vertices[idx++] = v;

            vertices[idx++] = t.m00 * x + t.m01 * fy2 + t.m02;
            vertices[idx++] = t.m10 * x + t.m11 * fy2 + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u;
            vertices[idx++] = v2;

            vertices[idx++] = t.m00 * fx2 + t.m01 * fy2 + t.m02;
            vertices[idx++] = t.m10 * fx2 + t.m11 * fy2 + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u2;
            vertices[idx++] = v2;

            vertices[idx++] = t.m00 * fx2 + t.m01 * y + t.m02;
            vertices[idx++] = t.m10 * fx2 + t.m11 * y + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u2;
            vertices[idx++] = v;
            this.vertexIndex = idx;
        }
    }

    @Override
    public void draw(Texture texture, float x, float y, float width, float height, int srcX, int srcY, int srcWidth,
                     int srcHeight, boolean flipX, boolean flipY) {
        if (!adjustNeeded) {
            if (!drawing)
                throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles1 = this.triangles;
            final float[] vertices1 = this.vertices;

            if (texture != lastTexture)
                switchTexture(texture);
            else if (triangleIndex + 6 > triangles1.length || vertexIndex + SPRITE_SIZE > vertices1.length) //
                flush();

            int triangleIndex1 = this.triangleIndex;
            final int startVertex = vertexIndex / VERTEX_SIZE;
            triangles1[triangleIndex1++] = (short) startVertex;
            triangles1[triangleIndex1++] = (short) (startVertex + 1);
            triangles1[triangleIndex1++] = (short) (startVertex + 2);
            triangles1[triangleIndex1++] = (short) (startVertex + 2);
            triangles1[triangleIndex1++] = (short) (startVertex + 3);
            triangles1[triangleIndex1++] = (short) startVertex;
            this.triangleIndex = triangleIndex1;

            float u = srcX * invTexWidth;
            float v = (srcY + srcHeight) * invTexHeight;
            float u2 = (srcX + srcWidth) * invTexWidth;
            float v2 = srcY * invTexHeight;
            final float fx2 = x + width;
            final float fy2 = y + height;

            if (flipX) {
                float tmp = u;
                u = u2;
                u2 = tmp;
            }

            if (flipY) {
                float tmp = v;
                v = v2;
                v2 = tmp;
            }

            float color1 = this.color;
            int idx = this.vertexIndex;
            vertices1[idx++] = x;
            vertices1[idx++] = y;
            vertices1[idx++] = color1;
            vertices1[idx++] = u;
            vertices1[idx++] = v;

            vertices1[idx++] = x;
            vertices1[idx++] = fy2;
            vertices1[idx++] = color1;
            vertices1[idx++] = u;
            vertices1[idx++] = v2;

            vertices1[idx++] = fx2;
            vertices1[idx++] = fy2;
            vertices1[idx++] = color1;
            vertices1[idx++] = u2;
            vertices1[idx++] = v2;

            vertices1[idx++] = fx2;
            vertices1[idx++] = y;
            vertices1[idx++] = color1;
            vertices1[idx++] = u2;
            vertices1[idx++] = v;
            this.vertexIndex = idx;
        } else {
            if (!drawing)
                throw new IllegalStateException("CpuPolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles = this.triangles;
            final float[] vertices = this.vertices;

            if (texture != lastTexture)
                switchTexture(texture);
            else if (triangleIndex + 6 > triangles.length || vertexIndex + SPRITE_SIZE > vertices.length) //
                flush();

            int triangleIndex = this.triangleIndex;
            final int startVertex = vertexIndex / VERTEX_SIZE;
            triangles[triangleIndex++] = (short) startVertex;
            triangles[triangleIndex++] = (short) (startVertex + 1);
            triangles[triangleIndex++] = (short) (startVertex + 2);
            triangles[triangleIndex++] = (short) (startVertex + 2);
            triangles[triangleIndex++] = (short) (startVertex + 3);
            triangles[triangleIndex++] = (short) startVertex;
            this.triangleIndex = triangleIndex;

            float u = srcX * invTexWidth;
            float v = (srcY + srcHeight) * invTexHeight;
            float u2 = (srcX + srcWidth) * invTexWidth;
            float v2 = srcY * invTexHeight;
            final float fx2 = x + width;
            final float fy2 = y + height;

            if (flipX) {
                float tmp = u;
                u = u2;
                u2 = tmp;
            }

            if (flipY) {
                float tmp = v;
                v = v2;
                v2 = tmp;
            }

            float color = this.color;
            int idx = this.vertexIndex;
            Affine2 t = adjustAffine;
            vertices[idx++] = t.m00 * x + t.m01 * y + t.m02;
            vertices[idx++] = t.m10 * x + t.m11 * y + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u;
            vertices[idx++] = v;

            vertices[idx++] = t.m00 * x + t.m01 * fy2 + t.m02;
            vertices[idx++] = t.m10 * x + t.m11 * fy2 + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u;
            vertices[idx++] = v2;

            vertices[idx++] = t.m00 * fx2 + t.m01 * fy2 + t.m02;
            vertices[idx++] = t.m10 * fx2 + t.m11 * fy2 + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u2;
            vertices[idx++] = v2;

            vertices[idx++] = t.m00 * fx2 + t.m01 * y + t.m02;
            vertices[idx++] = t.m10 * fx2 + t.m11 * y + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u2;
            vertices[idx++] = v;
            this.vertexIndex = idx;
        }
    }

    @Override
    public void draw(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight) {
        if (!adjustNeeded) {
            if (!drawing)
                throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles1 = this.triangles;
            final float[] vertices1 = this.vertices;

            if (texture != lastTexture)
                switchTexture(texture);
            else if (triangleIndex + 6 > triangles1.length || vertexIndex + SPRITE_SIZE > vertices1.length) //
                flush();

            int triangleIndex1 = this.triangleIndex;
            final int startVertex = vertexIndex / VERTEX_SIZE;
            triangles1[triangleIndex1++] = (short) startVertex;
            triangles1[triangleIndex1++] = (short) (startVertex + 1);
            triangles1[triangleIndex1++] = (short) (startVertex + 2);
            triangles1[triangleIndex1++] = (short) (startVertex + 2);
            triangles1[triangleIndex1++] = (short) (startVertex + 3);
            triangles1[triangleIndex1++] = (short) startVertex;
            this.triangleIndex = triangleIndex1;

            final float u = srcX * invTexWidth;
            final float v = (srcY + srcHeight) * invTexHeight;
            final float u2 = (srcX + srcWidth) * invTexWidth;
            final float v2 = srcY * invTexHeight;
            final float fx2 = x + srcWidth;
            final float fy2 = y + srcHeight;

            float color1 = this.color;
            int idx = this.vertexIndex;
            vertices1[idx++] = x;
            vertices1[idx++] = y;
            vertices1[idx++] = color1;
            vertices1[idx++] = u;
            vertices1[idx++] = v;

            vertices1[idx++] = x;
            vertices1[idx++] = fy2;
            vertices1[idx++] = color1;
            vertices1[idx++] = u;
            vertices1[idx++] = v2;

            vertices1[idx++] = fx2;
            vertices1[idx++] = fy2;
            vertices1[idx++] = color1;
            vertices1[idx++] = u2;
            vertices1[idx++] = v2;

            vertices1[idx++] = fx2;
            vertices1[idx++] = y;
            vertices1[idx++] = color1;
            vertices1[idx++] = u2;
            vertices1[idx++] = v;
            this.vertexIndex = idx;
        } else {
            if (!drawing)
                throw new IllegalStateException("CpuPolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles = this.triangles;
            final float[] vertices = this.vertices;

            if (texture != lastTexture)
                switchTexture(texture);
            else if (triangleIndex + 6 > triangles.length || vertexIndex + SPRITE_SIZE > vertices.length) //
                flush();

            int triangleIndex = this.triangleIndex;
            final int startVertex = vertexIndex / VERTEX_SIZE;
            triangles[triangleIndex++] = (short) startVertex;
            triangles[triangleIndex++] = (short) (startVertex + 1);
            triangles[triangleIndex++] = (short) (startVertex + 2);
            triangles[triangleIndex++] = (short) (startVertex + 2);
            triangles[triangleIndex++] = (short) (startVertex + 3);
            triangles[triangleIndex++] = (short) startVertex;
            this.triangleIndex = triangleIndex;

            final float u = srcX * invTexWidth;
            final float v = (srcY + srcHeight) * invTexHeight;
            final float u2 = (srcX + srcWidth) * invTexWidth;
            final float v2 = srcY * invTexHeight;
            final float fx2 = x + srcWidth;
            final float fy2 = y + srcHeight;

            float color = this.color;
            int idx = this.vertexIndex;
            Affine2 t = adjustAffine;
            vertices[idx++] = t.m00 * x + t.m01 * y + t.m02;
            vertices[idx++] = t.m10 * x + t.m11 * y + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u;
            vertices[idx++] = v;

            vertices[idx++] = t.m00 * x + t.m01 * fy2 + t.m02;
            vertices[idx++] = t.m10 * x + t.m11 * fy2 + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u;
            vertices[idx++] = v2;

            vertices[idx++] = t.m00 * fx2 + t.m01 * fy2 + t.m02;
            vertices[idx++] = t.m10 * fx2 + t.m11 * fy2 + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u2;
            vertices[idx++] = v2;

            vertices[idx++] = t.m00 * fx2 + t.m01 * y + t.m02;
            vertices[idx++] = t.m10 * fx2 + t.m11 * y + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u2;
            vertices[idx++] = v;
            this.vertexIndex = idx;
        }
    }

    public void draw(Texture texture, float[] polygonVertices, int verticesOffset, int verticesCount, short[] polygonTriangles,
                     int trianglesOffset, int trianglesCount) {
        if (!adjustNeeded) {
            if (!drawing)
                throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles1 = this.triangles;
            final float[] vertices1 = this.vertices;

            if (texture != lastTexture)
                switchTexture(texture);
            else if (triangleIndex + trianglesCount > triangles1.length || vertexIndex + verticesCount > vertices1.length) //
                flush();

            int triangleIndex1 = this.triangleIndex;
            final int vertexIndex1 = this.vertexIndex;
            final int startVertex = vertexIndex1 / VERTEX_SIZE;

            for (int i = trianglesOffset, n = i + trianglesCount; i < n; i++)
                triangles1[triangleIndex1++] = (short) (polygonTriangles[i] + startVertex);
            this.triangleIndex = triangleIndex1;

            System.arraycopy(polygonVertices, verticesOffset, vertices1, vertexIndex1, verticesCount);
            this.vertexIndex += verticesCount;
        } else {
            if (!drawing)
                throw new IllegalStateException("CpuPolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles = this.triangles;
            final float[] vertices = this.vertices;

            if (texture != lastTexture)
                switchTexture(texture);
            else if (triangleIndex + trianglesCount > triangles.length || vertexIndex + verticesCount > vertices.length) //
                flush();

            if (triangleIndex + trianglesCount > triangles.length || vertexIndex + verticesCount > vertices.length)
                throw new GdxRuntimeException("Polygon too big");

            int triangleIndex = this.triangleIndex;
            final int vertexIndex = this.vertexIndex;
            final int startVertex = vertexIndex / VERTEX_SIZE;

            for (int i = trianglesOffset, n = i + trianglesCount; i < n; i++)
                triangles[triangleIndex++] = (short) (polygonTriangles[i] + startVertex);
            this.triangleIndex = triangleIndex;

            Affine2 t = adjustAffine;
            int vdin = vertexIndex;
            for (int offsetin = 0; offsetin < verticesCount; offsetin += 5, vdin += 5) {
                float x = polygonVertices[offsetin];
                float y = polygonVertices[offsetin + 1];

                vertices[vdin] = t.m00 * x + t.m01 * y + t.m02; // x
                vertices[vdin + 1] = t.m10 * x + t.m11 * y + t.m12; // y
                vertices[vdin + 2] = polygonVertices[offsetin + 2]; // color
                vertices[vdin + 3] = polygonVertices[offsetin + 3]; // u
                vertices[vdin + 4] = polygonVertices[offsetin + 4]; // v
            }
//			System.arraycopy(polygonVertices, verticesOffset, vertices, vertexIndex, verticesCount);
            this.vertexIndex += verticesCount;
        }
    }

    @Override
    public void draw(Texture texture, float[] spriteVertices, int offset, int count) {
        //传入进来为一串坐标点，从offset起数一共count个
        //正常情况下count是20的倍数 VERTEX_SIZE*4
        if (!adjustNeeded) {
            if (!drawing)
                throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles1 = this.triangles;
            final float[] vertices1 = this.vertices;

            final int triangleCount = count / SPRITE_SIZE * 6;
            if (texture != lastTexture)
                switchTexture(texture);
            else if (triangleIndex + triangleCount > triangles1.length || vertexIndex + count > vertices1.length) //
                flush();

            final int vertexIndex1 = this.vertexIndex;
            int triangleIndex1 = this.triangleIndex;
            short vertex = (short) (vertexIndex1 / VERTEX_SIZE);
            for (int n = triangleIndex1 + triangleCount; triangleIndex1 < n; triangleIndex1 += 6, vertex += 4) {
                triangles1[triangleIndex1] = vertex;
                triangles1[triangleIndex1 + 1] = (short) (vertex + 1);
                triangles1[triangleIndex1 + 2] = (short) (vertex + 2);
                triangles1[triangleIndex1 + 3] = (short) (vertex + 2);
                triangles1[triangleIndex1 + 4] = (short) (vertex + 3);
                triangles1[triangleIndex1 + 5] = vertex;
            }
            this.triangleIndex = triangleIndex1;

            System.arraycopy(spriteVertices, offset, vertices1, vertexIndex1, count);
            this.vertexIndex += count;
        } else {
            if (!drawing)
                throw new IllegalStateException("CpuPolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles = this.triangles;
            final float[] vertices = this.vertices;

//			final int triangleCount = count / SPRITE_SIZE * 6;
            if (texture != lastTexture)
                switchTexture(texture);
//			else if (triangleIndex + triangleCount > triangles.length || vertexIndex + count > vertices.length) //
//				flush();
//
//			final int vertexIndex = this.vertexIndex;
//			int triangleIndex = this.triangleIndex;
//			short vertex = (short)(vertexIndex / VERTEX_SIZE);
//			for (int n = triangleIndex + triangleCount; triangleIndex < n; triangleIndex += 6, vertex += 4) {
//				triangles[triangleIndex] = vertex;
//				triangles[triangleIndex + 1] = (short)(vertex + 1);
//				triangles[triangleIndex + 2] = (short)(vertex + 2);
//				triangles[triangleIndex + 3] = (short)(vertex + 2);
//				triangles[triangleIndex + 4] = (short)(vertex + 3);
//				triangles[triangleIndex + 5] = vertex;
//			}
//			this.triangleIndex = triangleIndex;
//
//			System.arraycopy(spriteVertices, offset, vertices, vertexIndex, count);
//			this.vertexIndex += count;

            //20倍数
            int texturenum = count / 20;
            int offsetin = offset;
            while (texturenum > 0) {
                if (triangleIndex + 6 > triangles.length || vertexIndex + 20 > vertices.length)
                    flush();

                int triangleIndex = this.triangleIndex;
                final int vertexIndex = this.vertexIndex;
                short vertex = (short) (vertexIndex / 5);

                triangles[triangleIndex] = vertex;
                triangles[triangleIndex + 1] = (short) (vertex + 1);
                triangles[triangleIndex + 2] = (short) (vertex + 2);
                triangles[triangleIndex + 3] = (short) (vertex + 2);
                triangles[triangleIndex + 4] = (short) (vertex + 3);
                triangles[triangleIndex + 5] = vertex;
                this.triangleIndex = triangleIndex + 6;

                Affine2 t = adjustAffine;
                float x = spriteVertices[offsetin];
                float y = spriteVertices[offsetin + 1];
                vertices[vertexIndex] = t.m00 * x + t.m01 * y + t.m02;
                vertices[vertexIndex + 1] = t.m10 * x + t.m11 * y + t.m12;
                vertices[vertexIndex + 2] = spriteVertices[offsetin + 2];
                vertices[vertexIndex + 3] = spriteVertices[offsetin + 3];
                vertices[vertexIndex + 4] = spriteVertices[offsetin + 4];

                x = spriteVertices[offsetin + 5];
                y = spriteVertices[offsetin + 6];
                vertices[vertexIndex + 5] = t.m00 * x + t.m01 * y + t.m02;
                vertices[vertexIndex + 6] = t.m10 * x + t.m11 * y + t.m12;
                vertices[vertexIndex + 7] = spriteVertices[offsetin + 7];
                vertices[vertexIndex + 8] = spriteVertices[offsetin + 8];
                vertices[vertexIndex + 9] = spriteVertices[offsetin + 9];

                x = spriteVertices[offsetin + 10];
                y = spriteVertices[offsetin + 11];
                vertices[vertexIndex + 10] = t.m00 * x + t.m01 * y + t.m02;
                vertices[vertexIndex + 11] = t.m10 * x + t.m11 * y + t.m12;
                vertices[vertexIndex + 12] = spriteVertices[offsetin + 12];
                vertices[vertexIndex + 13] = spriteVertices[offsetin + 13];
                vertices[vertexIndex + 14] = spriteVertices[offsetin + 14];

                x = spriteVertices[offsetin + 15];
                y = spriteVertices[offsetin + 16];
                vertices[vertexIndex + 15] = t.m00 * x + t.m01 * y + t.m02;
                vertices[vertexIndex + 16] = t.m10 * x + t.m11 * y + t.m12;
                vertices[vertexIndex + 17] = spriteVertices[offsetin + 17];
                vertices[vertexIndex + 18] = spriteVertices[offsetin + 18];
                vertices[vertexIndex + 19] = spriteVertices[offsetin + 19];
                this.vertexIndex = vertexIndex + 20;
                offsetin += 20;
                texturenum--;
            }
        }
    }

    @Override
    public void draw(TextureRegion region, float width, float height, Affine2 transform) {
        if (!adjustNeeded) {
            if (!drawing)
                throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles1 = this.triangles;
            final float[] vertices1 = this.vertices;

            Texture texture = region.getTexture();
            if (texture != lastTexture)
                switchTexture(texture);
            else if (triangleIndex + 6 > triangles1.length || vertexIndex + SPRITE_SIZE > vertices1.length) //
                flush();

            int triangleIndex1 = this.triangleIndex;
            final int startVertex = vertexIndex / VERTEX_SIZE;
            triangles1[triangleIndex1++] = (short) startVertex;
            triangles1[triangleIndex1++] = (short) (startVertex + 1);
            triangles1[triangleIndex1++] = (short) (startVertex + 2);
            triangles1[triangleIndex1++] = (short) (startVertex + 2);
            triangles1[triangleIndex1++] = (short) (startVertex + 3);
            triangles1[triangleIndex1++] = (short) startVertex;
            this.triangleIndex = triangleIndex1;

            // construct corner points
            float x1 = transform.m02;
            float y1 = transform.m12;
            float x2 = transform.m01 * height + transform.m02;
            float y2 = transform.m11 * height + transform.m12;
            float x3 = transform.m00 * width + transform.m01 * height + transform.m02;
            float y3 = transform.m10 * width + transform.m11 * height + transform.m12;
            float x4 = transform.m00 * width + transform.m02;
            float y4 = transform.m10 * width + transform.m12;

            float u = region.getU();
            float v = region.getV2();
            float u2 = region.getU2();
            float v2 = region.getV();

            float color1 = this.color;
            int idx = vertexIndex;
            vertices1[idx++] = x1;
            vertices1[idx++] = y1;
            vertices1[idx++] = color1;
            vertices1[idx++] = u;
            vertices1[idx++] = v;

            vertices1[idx++] = x2;
            vertices1[idx++] = y2;
            vertices1[idx++] = color1;
            vertices1[idx++] = u;
            vertices1[idx++] = v2;

            vertices1[idx++] = x3;
            vertices1[idx++] = y3;
            vertices1[idx++] = color1;
            vertices1[idx++] = u2;
            vertices1[idx++] = v2;

            vertices1[idx++] = x4;
            vertices1[idx++] = y4;
            vertices1[idx++] = color1;
            vertices1[idx++] = u2;
            vertices1[idx++] = v;
            vertexIndex = idx;
        } else {
            if (!drawing)
                throw new IllegalStateException("CpuPolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles = this.triangles;
            final float[] vertices = this.vertices;

            Texture texture = region.getTexture();
            if (texture != lastTexture)
                switchTexture(texture);
            else if (triangleIndex + 6 > triangles.length || vertexIndex + SPRITE_SIZE > vertices.length) //
                flush();

            int triangleIndex = this.triangleIndex;
            final int startVertex = vertexIndex / VERTEX_SIZE;
            triangles[triangleIndex++] = (short) startVertex;
            triangles[triangleIndex++] = (short) (startVertex + 1);
            triangles[triangleIndex++] = (short) (startVertex + 2);
            triangles[triangleIndex++] = (short) (startVertex + 2);
            triangles[triangleIndex++] = (short) (startVertex + 3);
            triangles[triangleIndex++] = (short) startVertex;
            this.triangleIndex = triangleIndex;

            // construct corner points
            float x1 = transform.m02;
            float y1 = transform.m12;
            float x2 = transform.m01 * height + transform.m02;
            float y2 = transform.m11 * height + transform.m12;
            float x3 = transform.m00 * width + transform.m01 * height + transform.m02;
            float y3 = transform.m10 * width + transform.m11 * height + transform.m12;
            float x4 = transform.m00 * width + transform.m02;
            float y4 = transform.m10 * width + transform.m12;

            float u = region.getU();
            float v = region.getV2();
            float u2 = region.getU2();
            float v2 = region.getV();

            float color = this.color;
            int idx = vertexIndex;
            Affine2 t = adjustAffine;
            vertices[idx++] = t.m00 * x1 + t.m01 * y1 + t.m02;
            vertices[idx++] = t.m10 * x1 + t.m11 * y1 + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u;
            vertices[idx++] = v;

            vertices[idx++] = t.m00 * x2 + t.m01 * y2 + t.m02;
            vertices[idx++] = t.m10 * x2 + t.m11 * y2 + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u;
            vertices[idx++] = v2;

            vertices[idx++] = t.m00 * x3 + t.m01 * y3 + t.m02;
            vertices[idx++] = t.m10 * x3 + t.m11 * y3 + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u2;
            vertices[idx++] = v2;

            vertices[idx++] = t.m00 * x4 + t.m01 * y4 + t.m02;
            vertices[idx++] = t.m10 * x4 + t.m11 * y4 + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u2;
            vertices[idx++] = v;
            vertexIndex = idx;
        }
    }

    @Override
    public void draw(TextureRegion region, float x, float y) {
        if (!adjustNeeded) {
            if (!drawing)
                throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles1 = this.triangles;
            final float[] vertices1 = this.vertices;

            Texture texture = region.getTexture();
            if (texture != lastTexture)
                switchTexture(texture);
            else if (triangleIndex + 6 > triangles1.length || vertexIndex + SPRITE_SIZE > vertices1.length) //
                flush();

            int triangleIndex1 = this.triangleIndex;
            final int startVertex = vertexIndex / VERTEX_SIZE;
            triangles1[triangleIndex1++] = (short) startVertex;
            triangles1[triangleIndex1++] = (short) (startVertex + 1);
            triangles1[triangleIndex1++] = (short) (startVertex + 2);
            triangles1[triangleIndex1++] = (short) (startVertex + 2);
            triangles1[triangleIndex1++] = (short) (startVertex + 3);
            triangles1[triangleIndex1++] = (short) startVertex;
            this.triangleIndex = triangleIndex1;

            final float fx2 = x + (float) region.getRegionWidth();
            final float fy2 = y + (float) region.getRegionHeight();
            final float u = region.getU();
            final float v = region.getV2();
            final float u2 = region.getU2();
            final float v2 = region.getV();

            float color1 = this.color;
            int idx = this.vertexIndex;
            vertices1[idx++] = x;
            vertices1[idx++] = y;
            vertices1[idx++] = color1;
            vertices1[idx++] = u;
            vertices1[idx++] = v;

            vertices1[idx++] = x;
            vertices1[idx++] = fy2;
            vertices1[idx++] = color1;
            vertices1[idx++] = u;
            vertices1[idx++] = v2;

            vertices1[idx++] = fx2;
            vertices1[idx++] = fy2;
            vertices1[idx++] = color1;
            vertices1[idx++] = u2;
            vertices1[idx++] = v2;

            vertices1[idx++] = fx2;
            vertices1[idx++] = y;
            vertices1[idx++] = color1;
            vertices1[idx++] = u2;
            vertices1[idx++] = v;
            this.vertexIndex = idx;
        } else {
            float width = region.getRegionWidth();
            float height = region.getRegionHeight();
            if (!drawing)
                throw new IllegalStateException("CpuPolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles = this.triangles;
            final float[] vertices = this.vertices;

            Texture texture = region.getTexture();
            if (texture != lastTexture)
                switchTexture(texture);
            else if (triangleIndex + 6 > triangles.length || vertexIndex + SPRITE_SIZE > vertices.length) //
                flush();

            int triangleIndex = this.triangleIndex;
            final int startVertex = vertexIndex / VERTEX_SIZE;
            triangles[triangleIndex++] = (short) startVertex;
            triangles[triangleIndex++] = (short) (startVertex + 1);
            triangles[triangleIndex++] = (short) (startVertex + 2);
            triangles[triangleIndex++] = (short) (startVertex + 2);
            triangles[triangleIndex++] = (short) (startVertex + 3);
            triangles[triangleIndex++] = (short) startVertex;
            this.triangleIndex = triangleIndex;

            final float fx2 = x + width;
            final float fy2 = y + height;
            final float u = region.getU();
            final float v = region.getV2();
            final float u2 = region.getU2();
            final float v2 = region.getV();

            float color = this.color;
            int idx = this.vertexIndex;
            Affine2 t = adjustAffine;
            vertices[idx++] = t.m00 * x + t.m01 * y + t.m02;
            vertices[idx++] = t.m10 * x + t.m11 * y + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u;
            vertices[idx++] = v;

            vertices[idx++] = t.m00 * x + t.m01 * fy2 + t.m02;
            vertices[idx++] = t.m10 * x + t.m11 * fy2 + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u;
            vertices[idx++] = v2;

            vertices[idx++] = t.m00 * fx2 + t.m01 * fy2 + t.m02;
            vertices[idx++] = t.m10 * fx2 + t.m11 * fy2 + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u2;
            vertices[idx++] = v2;

            vertices[idx++] = t.m00 * fx2 + t.m01 * y + t.m02;
            vertices[idx++] = t.m10 * fx2 + t.m11 * y + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u2;
            vertices[idx++] = v;
            this.vertexIndex = idx;
        }
    }

    @Override
    public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height,
                     float scaleX, float scaleY, float rotation) {
        if (!adjustNeeded) {
            if (!drawing)
                throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles1 = this.triangles;
            final float[] vertices1 = this.vertices;

            Texture texture = region.getTexture();
            if (texture != lastTexture)
                switchTexture(texture);
            else if (triangleIndex + 6 > triangles1.length || vertexIndex + SPRITE_SIZE > vertices1.length) //
                flush();

            int triangleIndex1 = this.triangleIndex;
            final int startVertex = vertexIndex / VERTEX_SIZE;
            triangles1[triangleIndex1++] = (short) startVertex;
            triangles1[triangleIndex1++] = (short) (startVertex + 1);
            triangles1[triangleIndex1++] = (short) (startVertex + 2);
            triangles1[triangleIndex1++] = (short) (startVertex + 2);
            triangles1[triangleIndex1++] = (short) (startVertex + 3);
            triangles1[triangleIndex1++] = (short) startVertex;
            this.triangleIndex = triangleIndex1;

            // bottom left and top right corner points relative to origin
            final float worldOriginX = x + originX;
            final float worldOriginY = y + originY;
            float fx = -originX;
            float fy = -originY;
            float fx2 = width - originX;
            float fy2 = height - originY;

            // scale
            if (scaleX != 1 || scaleY != 1) {
                fx *= scaleX;
                fy *= scaleY;
                fx2 *= scaleX;
                fy2 *= scaleY;
            }

            // construct corner points, start from top left and go counter clockwise
            final float p1x = fx;
            final float p1y = fy;
            final float p2x = fx;
            final float p2y = fy2;
            final float p3x = fx2;
            final float p3y = fy2;
            final float p4x = fx2;
            final float p4y = fy;

            float x1;
            float y1;
            float x2;
            float y2;
            float x3;
            float y3;
            float x4;
            float y4;

            // rotate
            if (rotation != 0) {
                final float cos = MathUtils.cosDeg(rotation);
                final float sin = MathUtils.sinDeg(rotation);

                x1 = cos * p1x - sin * p1y;
                y1 = sin * p1x + cos * p1y;

                x2 = cos * p2x - sin * p2y;
                y2 = sin * p2x + cos * p2y;

                x3 = cos * p3x - sin * p3y;
                y3 = sin * p3x + cos * p3y;

                x4 = x1 + (x3 - x2);
                y4 = y3 - (y2 - y1);
            } else {
                x1 = p1x;
                y1 = p1y;

                x2 = p2x;
                y2 = p2y;

                x3 = p3x;
                y3 = p3y;

                x4 = p4x;
                y4 = p4y;
            }

            x1 += worldOriginX;
            y1 += worldOriginY;
            x2 += worldOriginX;
            y2 += worldOriginY;
            x3 += worldOriginX;
            y3 += worldOriginY;
            x4 += worldOriginX;
            y4 += worldOriginY;

            final float u = region.getU();
            final float v = region.getV2();
            final float u2 = region.getU2();
            final float v2 = region.getV();

            float color1 = this.color;
            int idx = this.vertexIndex;
            vertices1[idx++] = x1;
            vertices1[idx++] = y1;
            vertices1[idx++] = color1;
            vertices1[idx++] = u;
            vertices1[idx++] = v;

            vertices1[idx++] = x2;
            vertices1[idx++] = y2;
            vertices1[idx++] = color1;
            vertices1[idx++] = u;
            vertices1[idx++] = v2;

            vertices1[idx++] = x3;
            vertices1[idx++] = y3;
            vertices1[idx++] = color1;
            vertices1[idx++] = u2;
            vertices1[idx++] = v2;

            vertices1[idx++] = x4;
            vertices1[idx++] = y4;
            vertices1[idx++] = color1;
            vertices1[idx++] = u2;
            vertices1[idx++] = v;
            this.vertexIndex = idx;
        } else {
            if (!drawing)
                throw new IllegalStateException("CpuPolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles = this.triangles;
            final float[] vertices = this.vertices;

            Texture texture = region.getTexture();
            if (texture != lastTexture)
                switchTexture(texture);
            else if (triangleIndex + 6 > triangles.length || vertexIndex + SPRITE_SIZE > vertices.length) //
                flush();

            int triangleIndex = this.triangleIndex;
            final int startVertex = vertexIndex / VERTEX_SIZE;
            triangles[triangleIndex++] = (short) startVertex;
            triangles[triangleIndex++] = (short) (startVertex + 1);
            triangles[triangleIndex++] = (short) (startVertex + 2);
            triangles[triangleIndex++] = (short) (startVertex + 2);
            triangles[triangleIndex++] = (short) (startVertex + 3);
            triangles[triangleIndex++] = (short) startVertex;
            this.triangleIndex = triangleIndex;

            // bottom left and top right corner points relative to origin
            final float worldOriginX = x + originX;
            final float worldOriginY = y + originY;
            float fx = -originX;
            float fy = -originY;
            float fx2 = width - originX;
            float fy2 = height - originY;

            // scale
            if (scaleX != 1 || scaleY != 1) {
                fx *= scaleX;
                fy *= scaleY;
                fx2 *= scaleX;
                fy2 *= scaleY;
            }

            // construct corner points, start from top left and go counter clockwise
            final float p1x = fx;
            final float p1y = fy;
            final float p2x = fx;
            final float p2y = fy2;
            final float p3x = fx2;
            final float p3y = fy2;
            final float p4x = fx2;
            final float p4y = fy;

            float x1;
            float y1;
            float x2;
            float y2;
            float x3;
            float y3;
            float x4;
            float y4;

            // rotate
            if (rotation != 0) {
                final float cos = MathUtils.cosDeg(rotation);
                final float sin = MathUtils.sinDeg(rotation);

                x1 = cos * p1x - sin * p1y;
                y1 = sin * p1x + cos * p1y;

                x2 = cos * p2x - sin * p2y;
                y2 = sin * p2x + cos * p2y;

                x3 = cos * p3x - sin * p3y;
                y3 = sin * p3x + cos * p3y;

                x4 = x1 + (x3 - x2);
                y4 = y3 - (y2 - y1);
            } else {
                x1 = p1x;
                y1 = p1y;

                x2 = p2x;
                y2 = p2y;

                x3 = p3x;
                y3 = p3y;

                x4 = p4x;
                y4 = p4y;
            }

            x1 += worldOriginX;
            y1 += worldOriginY;
            x2 += worldOriginX;
            y2 += worldOriginY;
            x3 += worldOriginX;
            y3 += worldOriginY;
            x4 += worldOriginX;
            y4 += worldOriginY;

            final float u = region.getU();
            final float v = region.getV2();
            final float u2 = region.getU2();
            final float v2 = region.getV();

            float color = this.color;
            int idx = this.vertexIndex;
            Affine2 t = adjustAffine;
            vertices[idx++] = t.m00 * x1 + t.m01 * y1 + t.m02;
            vertices[idx++] = t.m10 * x1 + t.m11 * y1 + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u;
            vertices[idx++] = v;

            vertices[idx++] = t.m00 * x2 + t.m01 * y2 + t.m02;
            vertices[idx++] = t.m10 * x2 + t.m11 * y2 + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u;
            vertices[idx++] = v2;

            vertices[idx++] = t.m00 * x3 + t.m01 * y3 + t.m02;
            vertices[idx++] = t.m10 * x3 + t.m11 * y3 + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u2;
            vertices[idx++] = v2;

            vertices[idx++] = t.m00 * x4 + t.m01 * y4 + t.m02;
            vertices[idx++] = t.m10 * x4 + t.m11 * y4 + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u2;
            vertices[idx++] = v;
            this.vertexIndex = idx;
        }
    }

    @Override
    public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height,
                     float scaleX, float scaleY, float rotation, boolean clockwise) {
        if (!adjustNeeded) {
            if (!drawing)
                throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles1 = this.triangles;
            final float[] vertices1 = this.vertices;

            Texture texture = region.getTexture();
            if (texture != lastTexture)
                switchTexture(texture);
            else if (triangleIndex + 6 > triangles1.length || vertexIndex + SPRITE_SIZE > vertices1.length) //
                flush();

            int triangleIndex1 = this.triangleIndex;
            final int startVertex = vertexIndex / VERTEX_SIZE;
            triangles1[triangleIndex1++] = (short) startVertex;
            triangles1[triangleIndex1++] = (short) (startVertex + 1);
            triangles1[triangleIndex1++] = (short) (startVertex + 2);
            triangles1[triangleIndex1++] = (short) (startVertex + 2);
            triangles1[triangleIndex1++] = (short) (startVertex + 3);
            triangles1[triangleIndex1++] = (short) startVertex;
            this.triangleIndex = triangleIndex1;

            // bottom left and top right corner points relative to origin
            final float worldOriginX = x + originX;
            final float worldOriginY = y + originY;
            float fx = -originX;
            float fy = -originY;
            float fx2 = width - originX;
            float fy2 = height - originY;

            // scale
            if (scaleX != 1 || scaleY != 1) {
                fx *= scaleX;
                fy *= scaleY;
                fx2 *= scaleX;
                fy2 *= scaleY;
            }

            // construct corner points, start from top left and go counter clockwise
            final float p1x = fx;
            final float p1y = fy;
            final float p2x = fx;
            final float p2y = fy2;
            final float p3x = fx2;
            final float p3y = fy2;
            final float p4x = fx2;
            final float p4y = fy;

            float x1;
            float y1;
            float x2;
            float y2;
            float x3;
            float y3;
            float x4;
            float y4;

            // rotate
            if (rotation != 0) {
                final float cos = MathUtils.cosDeg(rotation);
                final float sin = MathUtils.sinDeg(rotation);

                x1 = cos * p1x - sin * p1y;
                y1 = sin * p1x + cos * p1y;

                x2 = cos * p2x - sin * p2y;
                y2 = sin * p2x + cos * p2y;

                x3 = cos * p3x - sin * p3y;
                y3 = sin * p3x + cos * p3y;

                x4 = x1 + (x3 - x2);
                y4 = y3 - (y2 - y1);
            } else {
                x1 = p1x;
                y1 = p1y;

                x2 = p2x;
                y2 = p2y;

                x3 = p3x;
                y3 = p3y;

                x4 = p4x;
                y4 = p4y;
            }

            x1 += worldOriginX;
            y1 += worldOriginY;
            x2 += worldOriginX;
            y2 += worldOriginY;
            x3 += worldOriginX;
            y3 += worldOriginY;
            x4 += worldOriginX;
            y4 += worldOriginY;

            float u1, v1, u2, v2, u3, v3, u4, v4;
            if (clockwise) {
                u1 = region.getU2();
                v1 = region.getV2();
                u2 = region.getU();
                v2 = region.getV2();
                u3 = region.getU();
                v3 = region.getV();
                u4 = region.getU2();
                v4 = region.getV();
            } else {
                u1 = region.getU();
                v1 = region.getV();
                u2 = region.getU2();
                v2 = region.getV();
                u3 = region.getU2();
                v3 = region.getV2();
                u4 = region.getU();
                v4 = region.getV2();
            }

            float color1 = this.color;
            int idx = this.vertexIndex;
            vertices1[idx++] = x1;
            vertices1[idx++] = y1;
            vertices1[idx++] = color1;
            vertices1[idx++] = u1;
            vertices1[idx++] = v1;

            vertices1[idx++] = x2;
            vertices1[idx++] = y2;
            vertices1[idx++] = color1;
            vertices1[idx++] = u2;
            vertices1[idx++] = v2;

            vertices1[idx++] = x3;
            vertices1[idx++] = y3;
            vertices1[idx++] = color1;
            vertices1[idx++] = u3;
            vertices1[idx++] = v3;

            vertices1[idx++] = x4;
            vertices1[idx++] = y4;
            vertices1[idx++] = color1;
            vertices1[idx++] = u4;
            vertices1[idx++] = v4;
            this.vertexIndex = idx;
        } else {
            if (!drawing)
                throw new IllegalStateException("CpuPolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles = this.triangles;
            final float[] vertices = this.vertices;

            Texture texture = region.getTexture();
            if (texture != lastTexture)
                switchTexture(texture);
            else if (triangleIndex + 6 > triangles.length || vertexIndex + SPRITE_SIZE > vertices.length) //
                flush();

            int triangleIndex = this.triangleIndex;
            final int startVertex = vertexIndex / VERTEX_SIZE;
            triangles[triangleIndex++] = (short) startVertex;
            triangles[triangleIndex++] = (short) (startVertex + 1);
            triangles[triangleIndex++] = (short) (startVertex + 2);
            triangles[triangleIndex++] = (short) (startVertex + 2);
            triangles[triangleIndex++] = (short) (startVertex + 3);
            triangles[triangleIndex++] = (short) startVertex;
            this.triangleIndex = triangleIndex;

            // bottom left and top right corner points relative to origin
            final float worldOriginX = x + originX;
            final float worldOriginY = y + originY;
            float fx = -originX;
            float fy = -originY;
            float fx2 = width - originX;
            float fy2 = height - originY;

            // scale
            if (scaleX != 1 || scaleY != 1) {
                fx *= scaleX;
                fy *= scaleY;
                fx2 *= scaleX;
                fy2 *= scaleY;
            }

            // construct corner points, start from top left and go counter clockwise
            final float p1x = fx;
            final float p1y = fy;
            final float p2x = fx;
            final float p2y = fy2;
            final float p3x = fx2;
            final float p3y = fy2;
            final float p4x = fx2;
            final float p4y = fy;

            float x1;
            float y1;
            float x2;
            float y2;
            float x3;
            float y3;
            float x4;
            float y4;

            // rotate
            if (rotation != 0) {
                final float cos = MathUtils.cosDeg(rotation);
                final float sin = MathUtils.sinDeg(rotation);

                x1 = cos * p1x - sin * p1y;
                y1 = sin * p1x + cos * p1y;

                x2 = cos * p2x - sin * p2y;
                y2 = sin * p2x + cos * p2y;

                x3 = cos * p3x - sin * p3y;
                y3 = sin * p3x + cos * p3y;

                x4 = x1 + (x3 - x2);
                y4 = y3 - (y2 - y1);
            } else {
                x1 = p1x;
                y1 = p1y;

                x2 = p2x;
                y2 = p2y;

                x3 = p3x;
                y3 = p3y;

                x4 = p4x;
                y4 = p4y;
            }

            x1 += worldOriginX;
            y1 += worldOriginY;
            x2 += worldOriginX;
            y2 += worldOriginY;
            x3 += worldOriginX;
            y3 += worldOriginY;
            x4 += worldOriginX;
            y4 += worldOriginY;

            float u1, v1, u2, v2, u3, v3, u4, v4;
            if (clockwise) {
                u1 = region.getU2();
                v1 = region.getV2();
                u2 = region.getU();
                v2 = region.getV2();
                u3 = region.getU();
                v3 = region.getV();
                u4 = region.getU2();
                v4 = region.getV();
            } else {
                u1 = region.getU();
                v1 = region.getV();
                u2 = region.getU2();
                v2 = region.getV();
                u3 = region.getU2();
                v3 = region.getV2();
                u4 = region.getU();
                v4 = region.getV2();
            }

            float color = this.color;
            int idx = this.vertexIndex;
            Affine2 t = adjustAffine;
            vertices[idx++] = t.m00 * x1 + t.m01 * y1 + t.m02;
            vertices[idx++] = t.m10 * x1 + t.m11 * y1 + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u1;
            vertices[idx++] = v1;

            vertices[idx++] = t.m00 * x2 + t.m01 * y2 + t.m02;
            vertices[idx++] = t.m10 * x2 + t.m11 * y2 + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u2;
            vertices[idx++] = v2;

            vertices[idx++] = t.m00 * x3 + t.m01 * y3 + t.m02;
            vertices[idx++] = t.m10 * x3 + t.m11 * y3 + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u3;
            vertices[idx++] = v3;

            vertices[idx++] = t.m00 * x4 + t.m01 * y4 + t.m02;
            vertices[idx++] = t.m10 * x4 + t.m11 * y4 + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u4;
            vertices[idx++] = v4;
            this.vertexIndex = idx;
        }
    }

    @Override
    public void draw(TextureRegion region, float x, float y, float width, float height) {
        if (!adjustNeeded) {
            if (!drawing)
                throw new IllegalStateException("PolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles1 = this.triangles;
            final float[] vertices1 = this.vertices;

            Texture texture = region.getTexture();
            if (texture != lastTexture)
                switchTexture(texture);
            else if (triangleIndex + 6 > triangles1.length || vertexIndex + SPRITE_SIZE > vertices1.length) //
                flush();

            int triangleIndex1 = this.triangleIndex;
            final int startVertex = vertexIndex / VERTEX_SIZE;
            triangles1[triangleIndex1++] = (short) startVertex;
            triangles1[triangleIndex1++] = (short) (startVertex + 1);
            triangles1[triangleIndex1++] = (short) (startVertex + 2);
            triangles1[triangleIndex1++] = (short) (startVertex + 2);
            triangles1[triangleIndex1++] = (short) (startVertex + 3);
            triangles1[triangleIndex1++] = (short) startVertex;
            this.triangleIndex = triangleIndex1;

            final float fx2 = x + width;
            final float fy2 = y + height;
            final float u = region.getU();
            final float v = region.getV2();
            final float u2 = region.getU2();
            final float v2 = region.getV();

            float color1 = this.color;
            int idx = this.vertexIndex;
            vertices1[idx++] = x;
            vertices1[idx++] = y;
            vertices1[idx++] = color1;
            vertices1[idx++] = u;
            vertices1[idx++] = v;

            vertices1[idx++] = x;
            vertices1[idx++] = fy2;
            vertices1[idx++] = color1;
            vertices1[idx++] = u;
            vertices1[idx++] = v2;

            vertices1[idx++] = fx2;
            vertices1[idx++] = fy2;
            vertices1[idx++] = color1;
            vertices1[idx++] = u2;
            vertices1[idx++] = v2;

            vertices1[idx++] = fx2;
            vertices1[idx++] = y;
            vertices1[idx++] = color1;
            vertices1[idx++] = u2;
            vertices1[idx++] = v;
            this.vertexIndex = idx;
        } else {
            if (!drawing)
                throw new IllegalStateException("CpuPolygonSpriteBatch.begin must be called before draw.");

            final short[] triangles = this.triangles;
            final float[] vertices = this.vertices;

            Texture texture = region.getTexture();
            if (texture != lastTexture)
                switchTexture(texture);
            else if (triangleIndex + 6 > triangles.length || vertexIndex + SPRITE_SIZE > vertices.length) //
                flush();

            int triangleIndex = this.triangleIndex;
            final int startVertex = vertexIndex / VERTEX_SIZE;
            triangles[triangleIndex++] = (short) startVertex;
            triangles[triangleIndex++] = (short) (startVertex + 1);
            triangles[triangleIndex++] = (short) (startVertex + 2);
            triangles[triangleIndex++] = (short) (startVertex + 2);
            triangles[triangleIndex++] = (short) (startVertex + 3);
            triangles[triangleIndex++] = (short) startVertex;
            this.triangleIndex = triangleIndex;

            final float fx2 = x + width;
            final float fy2 = y + height;
            final float u = region.getU();
            final float v = region.getV2();
            final float u2 = region.getU2();
            final float v2 = region.getV();

            float color = this.color;
            int idx = this.vertexIndex;
            Affine2 t = adjustAffine;
            vertices[idx++] = t.m00 * x + t.m01 * y + t.m02;
            vertices[idx++] = t.m10 * x + t.m11 * y + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u;
            vertices[idx++] = v;

            vertices[idx++] = t.m00 * x + t.m01 * fy2 + t.m02;
            vertices[idx++] = t.m10 * x + t.m11 * fy2 + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u;
            vertices[idx++] = v2;

            vertices[idx++] = t.m00 * fx2 + t.m01 * fy2 + t.m02;
            vertices[idx++] = t.m10 * fx2 + t.m11 * fy2 + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u2;
            vertices[idx++] = v2;

            vertices[idx++] = t.m00 * fx2 + t.m01 * y + t.m02;
            vertices[idx++] = t.m10 * fx2 + t.m11 * y + t.m12;
            vertices[idx++] = color;
            vertices[idx++] = u2;
            vertices[idx++] = v;
            this.vertexIndex = idx;
        }
    }
}

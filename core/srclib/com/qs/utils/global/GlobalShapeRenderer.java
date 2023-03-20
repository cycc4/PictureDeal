package com.qs.utils.global;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GlobalShapeRenderer {
    private static ShapeRenderer shapeRenderer;

    public static ShapeRenderer getShapeRenderer() {
        if (shapeRenderer == null) {
            System.err.println("生成默认 ShapeRenderer");
            shapeRenderer = new ShapeRenderer();
            shapeRenderer.setAutoShapeType(true);
        }
        return shapeRenderer;
    }

    public static void createShapeRenderer() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
        System.err.println("创建默认 ShapeRenderer");
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
    }


    public static void disposeShapeRenderer() {
        if (shapeRenderer == null) {
            System.err.println("ShapeRenderer 为空");
        } else {
            shapeRenderer.dispose();
            shapeRenderer = null;
        }
    }
}

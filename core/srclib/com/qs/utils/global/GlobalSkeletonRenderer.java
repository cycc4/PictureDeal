package com.qs.utils.global;

import com.esotericsoftware.spine.SkeletonRenderer;

public class GlobalSkeletonRenderer {
    private static SkeletonRenderer shapeRenderer;

    public static SkeletonRenderer getSkeletonRenderer() {
        if (shapeRenderer == null) {
            System.err.println("生成默认 ShapeRenderer");
            shapeRenderer = new SkeletonRenderer();
        }
        return shapeRenderer;
    }

    public static void createShapeRenderer() {
        if (shapeRenderer != null) {
        }
        System.err.println("创建默认 ShapeRenderer");
        shapeRenderer = new SkeletonRenderer();
    }
}

package com.qs.utils.global;

import com.qs.utils.batch.CpuPolygonSpriteBatch;

public class GlobalBatch {
    private static CpuPolygonSpriteBatch cpuPolygonSpriteBatch;

    public static CpuPolygonSpriteBatch getCpuPolygonSpriteBatch() {
        if (cpuPolygonSpriteBatch == null) {
            System.err.println("生成默认 CpuPolygonSpriteBatch");
            cpuPolygonSpriteBatch = new CpuPolygonSpriteBatch(3000);
        }
        return cpuPolygonSpriteBatch;
    }

    public static void createCpuPolygonSpriteBatch() {
        if (cpuPolygonSpriteBatch != null) {
            cpuPolygonSpriteBatch.dispose();
        }
        System.err.println("创建默认 CpuPolygonSpriteBatch");
        cpuPolygonSpriteBatch = new CpuPolygonSpriteBatch();
    }

    public static void disposeCpuPolygonSpriteBatch() {
        if (cpuPolygonSpriteBatch == null) {
            System.err.println("CpuPolygonSpriteBatch 为空");
        } else {
            cpuPolygonSpriteBatch.dispose();
            cpuPolygonSpriteBatch = null;
        }
    }
}

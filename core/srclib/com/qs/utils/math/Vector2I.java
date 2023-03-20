package com.qs.utils.math;

public class Vector2I {
    public int x;
    public int y;

    public Vector2I(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2I(Vector2I v) {
        this.x = v.x;
        this.y = v.y;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

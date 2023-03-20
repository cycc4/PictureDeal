package com.qs.utils.stage;

import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class ShipeiExtendViewport extends ExtendViewport {
    ShipeiStage shipeiStage;
    private float modScale;
    private float modX;
    private float modY;
    private float xMore;
    private float yMore;
    private float modScaleX;
    private float modScaleY;

    public ShipeiExtendViewport(float minWorldWidth, float minWorldHeight) {
        super(minWorldWidth, minWorldHeight);
    }

    @Override
    public void update(int screenWidth, int screenHeight, boolean centerCamera) {
        super.update(screenWidth, screenHeight, centerCamera);
        if (shipeiStage != null)
            shipeiStage.update(getWorldWidth(), getWorldHeight());

        modScaleX = getWorldWidth() / getMinWorldWidth();
        modScaleY = getWorldHeight() / getMinWorldHeight();
        modScale = Math.max(modScaleX, modScaleY);
        modX = (getMinWorldWidth() - getWorldWidth()) / 2;
        modY = (getMinWorldHeight() - getWorldHeight()) / 2;

        xMore = -modX;
        yMore = -modY;
    }

    public void setStage(ShipeiStage shipeiStage) {
        this.shipeiStage = shipeiStage;
    }

    public float getModScale() {
        return modScale;
    }

    public float getModScaleX() {
        return modScaleX;
    }

    public float getModScaleY() {
        return modScaleY;
    }

    public float getModX() {
        return modX;
    }

    public float getModY() {
        return modY;
    }

    public float getXMore() {
        return xMore;
    }

    public float getYMore() {
        return yMore;
    }

    public boolean match(int width, int height) {
        if (Math.signum(width - height) == Math.signum(getMinWorldWidth() - getMinWorldHeight())) {
            return true;
        }
        return false;
    }
}

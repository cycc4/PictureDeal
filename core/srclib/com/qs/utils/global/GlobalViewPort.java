package com.qs.utils.global;

import com.mygdx.game.Constants;
import com.qs.utils.stage.ShipeiExtendViewport;

public class GlobalViewPort {
    private static ShipeiExtendViewport shipeiExtendViewport;

    public static ShipeiExtendViewport getShipeiExtendViewport() {
        if (shipeiExtendViewport == null) {
            System.err.println("生成默认 ShipeiExtendViewport 720 720");
            shipeiExtendViewport = new ShipeiExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);

        }
        return shipeiExtendViewport;
    }

    public static void createShipeiExtendViewport(float width, float height) {
        System.err.println("初始化 ShipeiExtendViewport " + width + " " + height + "");
        shipeiExtendViewport = new ShipeiExtendViewport(width, height);
    }
}

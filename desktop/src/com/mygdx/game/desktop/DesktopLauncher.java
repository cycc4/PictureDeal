package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 1280;
		new LwjglApplication(new MyGdxGame(arg), config);

//        if (arg.length > 0) {
//            if(arg.length > 1) {
//                Common.setTexturePackerSettings(arg[1]);
//            }else{
//                Common.setTexturePackerSettings(null);
//            }
//            System.out.println("path is:  " + arg[0]);
//            if (arg[0].endsWith(".atlas")) {
//                UnPackPictureBat unPackPictureBat = new UnPackPictureBat(arg[0]);
//            } else {
//                PackPictureBat ppb = new PackPictureBat(arg[0]);
//            }
//        }
    }
}

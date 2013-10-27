package ru.cfif11.cosmo;

import com.threed.jpct.Config;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import ru.cfif11.cosmo.control.KeyboardListener;
import ru.cfif11.cosmo.manager.LocationManager;
import ru.cfif11.cosmo.scene.GameWorld;
import ru.cfif11.cosmo.scene.Scene;

/**
 * A simple example of a planet orbiting a star. This example demonstrates the use of physics and libraries jpct
 * @author Galkin Aleksandr
 * 
 */
public class Main {

	private static final long serialVersionUID = -3626482109116766979L;
    static public TextureManager    texMan;
    static public LocationManager   locMan;
	private Scene                   scene           = null;
    private KeyboardListener        keyboardControl = null;



    //счетчик времени
    private int     rate   = 15;
	private Ticker  ticker = new Ticker(rate);
    private int     fps    = 0;
    private long    time   = System.currentTimeMillis();

	public static void main(String[] args) throws Exception {
		Config.glVerbose = true;
		Main cd = new Main();
		cd.init();
		cd.loop();
	}

    //инициализация
	public Main() throws Exception {
		Config.glAvoidTextureCopies = true;
		Config.maxPolysVisible      = 1000;
		Config.glColorDepth         = 24;
		Config.glFullscreen         = false;
		Config.farPlane             = 4000;
		Config.glShadowZBias        = 0.8f;
		Config.lightMul             = 1;
		Config.collideOffset        = 500;
		Config.glTrilinear          = true;
	}

	private void init() throws Exception {
        texMan = TextureManager.getInstance();
        texMan.addTexture("Earth", new Texture("resources/texture/Earth.jpg"));
        texMan.addTexture("Sun", new Texture("resources/texture/Sun.gif"));
        texMan.addTexture("Moon", new Texture("resources/texture/Moon.jpg"));
        texMan.addTexture("Spot", new Texture("resources/texture/spot.jpg"));

        locMan = new LocationManager(ticker);
        //создаем мир
        GameWorld startWorld = locMan.getGameWorld(0);
        scene                = new Scene(texMan, ticker, startWorld);
        keyboardControl      = new KeyboardListener();

	}


    //основной цикл программы
	private void loop() throws Exception {
		while (scene.run()) {
            scene.bufferReset(false);
		}
        scene.close();
		System.exit(0);
	}

}

	
package ru.cfif11.cosmo;

import com.threed.jpct.Config;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
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
    private static LocationManager   locMan;
	private Scene                   scene;
   // static public Settings          settings;



    //счетчик времени
    private int     rate   = 15;
	private Ticker  ticker = new Ticker(rate);
    private int     fps    = 0;
    private long    time   = System.currentTimeMillis();

    static {
        texMan = TextureManager.getInstance();
        texMan.addTexture("SolarSystemPlanet_1", new Texture("resources/texture/starSystem/solarSystem/Earth.jpg"));
        texMan.addTexture("SolarSystemStar", new Texture("resources/texture/starSystem/solarSystem/Sun.gif"));
        texMan.addTexture("SolarSystemSputnik_1", new Texture("resources/texture/starSystem/solarSystem/Moon.jpg"));
        texMan.addTexture("Radar", new Texture("resources/texture/form/radar/radar.jpg"));
        texMan.addTexture("StarRad", new Texture("resources/texture/form/radar/star.jpg"));
        texMan.addTexture("PlanetRad", new Texture("resources/texture/form/radar/planet.jpg"));
        texMan.addTexture("SputnikRad", new Texture("resources/texture/form/radar/sputnik.jpg"));
    }

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

	private void init(){


        locMan = new LocationManager(ticker);
        //создаем мир
        GameWorld startWorld = locMan.getGameWorld(0);
        scene                = new Scene(texMan, ticker, startWorld);
	}


    //основной цикл программы
	private void loop(){
		while (scene.run()) {
            scene.bufferReset(false);
		}
        scene.close();
		System.exit(0);
	}

}

	
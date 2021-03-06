package ru.cfif11.cosmo;

import com.threed.jpct.Config;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import ru.cfif11.cosmo.control1.KeyboardListener;
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
    public static final TextureManager      TEX_MAN;
    public static final KeyboardListener    KEYBOARD_LISTENER;
    private static LocationManager          locMan;
	private Scene                   scene;
   // static public Settings          settings;



    //счетчик времени
    public static int rate   = 15;
	private Ticker  ticker = new Ticker(rate);
    private int     fps    = 0;
    private long    time   = System.currentTimeMillis();

    static {
        KEYBOARD_LISTENER = new KeyboardListener();
        TEX_MAN = TextureManager.getInstance();
        TEX_MAN.addTexture("SolarSystemPlanet_1", new Texture("resources/texture/starSystem/solarSystem/Earth.jpg"));
        TEX_MAN.addTexture("SolarSystemStar", new Texture("resources/texture/starSystem/solarSystem/Sun.gif"));
        TEX_MAN.addTexture("SolarSystemSputnik_1", new Texture("resources/texture/starSystem/solarSystem/Moon.jpg"));
        TEX_MAN.addTexture("Radar", new Texture("resources/texture/form/radar/radar.jpg"));
        TEX_MAN.addTexture("SelectRad", new Texture("resources/texture/form/radar/select.jpg"));
        TEX_MAN.addTexture("StarRad", new Texture("resources/texture/form/radar/star.jpg"));
        TEX_MAN.addTexture("PlanetRad", new Texture("resources/texture/form/radar/planet.jpg"));
        TEX_MAN.addTexture("SputnikRad", new Texture("resources/texture/form/radar/sputnik.jpg"));
        TEX_MAN.addTexture("Information", new Texture("resources/texture/form/information/information.jpg"));
        TEX_MAN.addTexture("StarInf", new Texture("resources/texture/form/information/select/star.jpg"));
        TEX_MAN.addTexture("PlanetInf", new Texture("resources/texture/form/information/select/planet.jpg"));
        TEX_MAN.addTexture("SputnikInf", new Texture("resources/texture/form/information/select/sputnik.jpg"));
        TEX_MAN.addTexture("CameraInf", new Texture("resources/texture/form/information/select/camera.jpg"));
        TEX_MAN.addTexture("LookerInf", new Texture("resources/texture/form/information/select/looker.jpg"));
        TEX_MAN.addTexture("NullInf", new Texture("resources/texture/form/information/select/null.jpg"));
    }

	public static void main(String[] args) throws Exception {
		Config.glVerbose = true;
		Main cd = new Main();
		cd.init();
		cd.loop();

        System.exit(0);
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
        Config.oldStyle3DSLoader    = true;
	}

	private void init(){
        locMan = new LocationManager(ticker);
        //создаем мир
        GameWorld startWorld = locMan.getGameWorld(0);
        scene                = new Scene(ticker, startWorld);
	}


    //основной цикл программы
	private void loop(){
		while (scene.run()) {
            scene.bufferReset(false);
		}
        scene.close();
	}

}

	
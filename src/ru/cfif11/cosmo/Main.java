package ru.cfif11.cosmo;

import com.threed.jpct.*;
import ru.cfif11.cosmo.object.Camera;
import ru.cfif11.cosmo.control.KeyboardListener;
import ru.cfif11.cosmo.adapterphysics.AdapterPhysics;
import ru.cfif11.cosmo.object.physobject.MassAttractObject3D;
import ru.cfif11.cosmo.object.physobject.StarSystemEnum;

import java.util.ArrayList;

/**
 * A simple example of a planet orbiting a star. This example demonstrates the use of physics and libraries jpct
 * @author Galkin Aleksandr
 * 
 */
public class Main implements IPaintListener{

	private static final long serialVersionUID = -3626482109116766979L;

    //буфер для экрана
	private FrameBuffer buffer      = null;

    //для отлавливания событий с клавиатурой и мышкой


    private TextureManager texMan = null;


    //мир и объекты
	private World world                 = null;
    private ArrayList<MassAttractObject3D> starSystem = null;
    private float scalingFactor = 1e-4f;
    private Camera camera = null;
    private KeyboardListener keyboardControl = null;

	private boolean doLoop      = true;



    //счетчик времени
    private int rate        = 15;
	private Ticker ticker   = new Ticker(rate);
    private int fps         = 0;
    private long time       = System.currentTimeMillis();

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

        //содаем буффер
		buffer = new FrameBuffer(800, 600, FrameBuffer.SAMPLINGMODE_NORMAL);
		buffer.disableRenderer(IRenderer.RENDERER_SOFTWARE);
        //используем openGL, а не всякие swingи или awt
		buffer.enableRenderer(IRenderer.RENDERER_OPENGL);
		buffer.setPaintListener(this);

        texMan = TextureManager.getInstance();
        texMan.addTexture("Earth", new Texture("texture/Earth.jpg"));
        texMan.addTexture("Sun", new Texture("texture/Sun.gif"));
        texMan.addTexture("Moon", new Texture("texture/Moon.jpg"));

        //создаем мир
		world = new World();
        world.setAmbientLight(100, 100, 100);

        world.getLights().setRGBScale(Lights.RGB_SCALE_2X);


        keyboardControl = new KeyboardListener();
        camera = new Camera(world, ticker, buffer);
        initializationStarSystem();

        //создаем камеру, перемещаем в заданную точку и направляем ее взор на центр Солнца
		camera.setPosition(1000, 0, 0);
		camera.lookAt(new SimpleVector());
		//cam.setFOV(1.5f);
	}

    //данный метод создает объекты из перечисления StarSystemEnum и инициализирует их начальными значениями
    //все объекты записываются в список starSystem
    private void initializationStarSystem() {
        MassAttractObject3D tempObj;
        SimpleVector tempVec;
        starSystem = new ArrayList<MassAttractObject3D>();
        int i = 0;
        for (StarSystemEnum p : StarSystemEnum.values()) {
            tempVec  = p.getVelocity();
            tempVec.scalarMul(scalingFactor);
            tempObj = new MassAttractObject3D(Primitives.getSphere(100, p.getRadius() * scalingFactor), p.getNameObject()+i,
                    tempVec, p.getMass());
            tempObj.setTexture(tempObj.getName().substring(0,tempObj.getName().length() -1));
            tempObj.setEnvmapped(Object3D.ENVMAP_ENABLED);
            tempVec  = p.getInitialPosition();
            tempVec.scalarMul(scalingFactor);
            tempObj.translate(tempVec);
            starSystem.add(tempObj);
            world.addObject(tempObj);
            tempObj.build();
            tempObj.compileAndStrip();
            i++;
        }
    }

    //основной цикл программы
	private void loop() throws Exception {

        //создаем адаптер
		AdapterPhysics adapter = new AdapterPhysics(world);
		long ticks;

		while (doLoop) {
			ticks = ticker.getTicks();
			if (ticks > 0) {
                //рассчитываем все силы и считаем новые местоположения объектов
				adapter.calcForce();
                for(MassAttractObject3D e : starSystem) {
                    e.calcLocation(0.1f);
                }
                //используем обработчик событий для движения камеры
                doLoop = camera.pollControls();
                camera.move(ticks);
			}
            //очищаем буфер и рисуем заново
			buffer.clear();
			buffer.setPaintListenerState(false);
			world.renderScene(buffer);
			world.draw(buffer);
			buffer.update();
			buffer.displayGLOnly();
            //не используется, а вообще для подсчета fps
			if (System.currentTimeMillis() - time >= 1000) {
				fps = 0;
				time = System.currentTimeMillis();
			}
		}
        //очищаем и вырубаем
		buffer.disableRenderer(IRenderer.RENDERER_OPENGL);
		buffer.dispose();
		System.exit(0);
	}


	@Override
	public void finishedPainting() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startPainting() {
		// TODO Auto-generated method stub
		
	}
}

	
package ru.cfif11.cosmo;

import com.threed.jpct.*;
import com.threed.jpct.util.KeyMapper;
import com.threed.jpct.util.KeyState;
import org.lwjgl.input.Mouse;
import ru.cfif11.cosmo.adapterphysics.AdapterPhysics;
import ru.cfif11.cosmo.physobject.MassAttractObject3D;
import ru.cfif11.cosmo.physobject.StarSystemEnum;

import java.awt.event.KeyEvent;
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
	private KeyMapper keyMapper     = null;
	private MouseMapper mouseMapper = null;

    private TextureManager texMan = null;


    //мир и объекты
	private World world                 = null;
    private ArrayList<MassAttractObject3D> starSystem = null;
    private float scalingFactor = 1e-4f;
    //переменный для определения направления движения камеры
	private boolean forward     = false;
	private boolean backward    = false;
	private boolean up          = false;
	private boolean down        = false;
	private boolean left        = false;
	private boolean right       = false;
    private boolean fast        = false;
    private boolean slow        = false;

    //переменная для работы с основным циклом
	private boolean doLoop      = true;

	private float xAngle    = 0;

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

        //создаем обработчики
		keyMapper   = new KeyMapper();
		mouseMapper = new MouseMapper(buffer);
		mouseMapper.hide();

        initializationStarSystem();

        //создаем камеру, перемещаем в заданную точку и направляем ее взор на центр Солнца
		Camera cam = world.getCamera();
		cam.setPosition(0, 3500,0);
		cam.lookAt(new SimpleVector());
		//cam.setFOV(1.5f);
	}

    //данный метод создает объекты из перечисления StarSystemEnum и инициализирует их начальными значениями
    //все объекты записываются в список starSystem
    private void initializationStarSystem() {
        MassAttractObject3D tempObj = null;
        SimpleVector tempVec = null;
        starSystem = new ArrayList<MassAttractObject3D>();
        for (StarSystemEnum p : StarSystemEnum.values()) {
            tempVec  = p.getVelocity();
            tempVec.scalarMul(scalingFactor);
            System.out.println("before =" + p.getVelocity());
            tempObj = new MassAttractObject3D(Primitives.getSphere(100, p.getRadius() * scalingFactor), p.getNameObject(),
                    tempVec, p.getMass());
            tempObj.setTexture(tempObj.getName());
            tempObj.setEnvmapped(Object3D.ENVMAP_ENABLED);
            tempVec  = p.getInitialPosition();
            tempVec.scalarMul(scalingFactor);
            tempObj.translate(tempVec);
            starSystem.add(tempObj);
            world.addObject(tempObj);
            tempObj.build();
            tempObj.compileAndStrip();
        }
    }

    //основной цикл программы
	private void loop() throws Exception {

        //создаем адаптер
		AdapterPhysics adapter = new AdapterPhysics(world);
		long ticks  = 0;
        long tim    = System.currentTimeMillis();

		while (doLoop) {
			ticks = ticker.getTicks();
			if (ticks > 0) {
                //рассчитываем все силы и считаем новые местоположения объектов
				adapter.calcForce();
                for(int i = 0; i < starSystem.size(); i++) {
                    starSystem.get(i).calcLocation(0.1f);
                }
                //используем обработчик событий для движения камеры
				pollControls();
				move(ticks);
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
				//System.out.println();
				fps = 0;
				time = System.currentTimeMillis();
			}
		}
        //очищаем и вырубаем
		buffer.disableRenderer(IRenderer.RENDERER_OPENGL);
		buffer.dispose();
		System.exit(0);
	}

    //определяем что мы нажали на клаве
	private void pollControls() {
		KeyState ks = null;
		while ((ks = keyMapper.poll()) != KeyState.NONE) {
			if (ks.getKeyCode() == KeyEvent.VK_ESCAPE) {
				doLoop = false;
			}

			if (ks.getKeyCode() == KeyEvent.VK_UP) {
				forward = ks.getState();
			}

			if (ks.getKeyCode() == KeyEvent.VK_DOWN) {
				backward = ks.getState();
			}

			if (ks.getKeyCode() == KeyEvent.VK_LEFT) {
				left = ks.getState();
			}

			if (ks.getKeyCode() == KeyEvent.VK_RIGHT) {
				right = ks.getState();
			}

			if (ks.getKeyCode() == KeyEvent.VK_PAGE_UP) {
				up = ks.getState();
			}

			if (ks.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
				down = ks.getState();
			}

            if (ks.getKeyCode() == KeyEvent.VK_W) {
                fast = ks.getState();
            }

            if (ks.getKeyCode() == KeyEvent.VK_Q) {
                slow = ks.getState();
            }
		}

		if (org.lwjgl.opengl.Display.isCloseRequested()) {
			doLoop = false;
		}
	}

    //двигаем камеру в зависимости от того, куда нажали
	private void move(long ticks) {

		if (ticks == 0) {
			return;
		}

		// Key controls

		SimpleVector ellipsoid = new SimpleVector(5, 5, 5);

		if (forward) {
			world.checkCameraCollisionEllipsoid(Camera.CAMERA_MOVEIN, ellipsoid, ticks, 5);
		}

		if (backward) {
			world.checkCameraCollisionEllipsoid(Camera.CAMERA_MOVEOUT, ellipsoid, ticks, 5);
		}

		if (left) {
			world.checkCameraCollisionEllipsoid(Camera.CAMERA_MOVELEFT, ellipsoid, ticks, 5);
		}

		if (right) {
			world.checkCameraCollisionEllipsoid(Camera.CAMERA_MOVERIGHT, ellipsoid, ticks, 5);
		}

		if (up) {
			world.checkCameraCollisionEllipsoid(Camera.CAMERA_MOVEUP, ellipsoid, ticks, 5);
		}

		if (down) {
			world.checkCameraCollisionEllipsoid(Camera.CAMERA_MOVEDOWN, ellipsoid, ticks, 5);
		}

        if (fast) {
            if(rate > 2) {rate--; ticker.setRate(rate); }
        }

        if (slow) {
            if(rate < 30) {rate++; ticker.setRate(rate); }
        }

		// mouse rotation
		Matrix rot  = world.getCamera().getBack();
		int dx      = mouseMapper.getDeltaX();
		int dy      = mouseMapper.getDeltaY();

		float ts    = 0.2f * ticks;
		float tsy   = ts;

		if (dx != 0) {
			ts = dx / 500f;
		}
		if (dy != 0) {
			tsy = dy / 500f;
		}

		if (dx != 0) {
			rot.rotateAxis(rot.getYAxis(), ts);
		}

		if ((dy > 0 && xAngle < Math.PI / 4.2)
				|| (dy < 0 && xAngle > -Math.PI / 4.2)) {
			rot.rotateX(tsy);
			xAngle += tsy;
		}

	}

    //слушатель мышки
	private static class MouseMapper {

		private boolean hidden  = false;
		private int height      = 0;

		public MouseMapper(FrameBuffer buffer) {
			height = buffer.getOutputHeight();
			init();
		}

		public void hide() {
			if (!hidden) {
				Mouse.setGrabbed(true);
				hidden = true;
			}
		}

		public void show() {
			if (hidden) {
				Mouse.setGrabbed(false);
				hidden = false;
			}
		}

		public boolean isVisible() {
			return !hidden;
		}

		public void destroy() {
			show();
			if (Mouse.isCreated()) {
				Mouse.destroy();
			}
		}

		public boolean buttonDown(int button) {
			return Mouse.isButtonDown(button);
		}

		public int getMouseX() {
			return Mouse.getX();
		}

		public int getMouseY() {
			return height - Mouse.getY();
		}

		public int getDeltaX() {
			if (Mouse.isGrabbed()) {
				return Mouse.getDX();
			} else {
				return 0;
			}
		}

		public int getDeltaY() {
			if (Mouse.isGrabbed()) {
				return Mouse.getDY();
			} else {
				return 0;
			}
		}

		private void init() {
			try {
				if (!Mouse.isCreated()) {
					Mouse.create();
				}

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
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

	
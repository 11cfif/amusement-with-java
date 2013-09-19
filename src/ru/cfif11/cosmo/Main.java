package ru.cfif11.cosmo;

import com.threed.jpct.*;
import com.threed.jpct.util.KeyMapper;
import com.threed.jpct.util.KeyState;
import org.lwjgl.input.Mouse;
import ru.cfif11.cosmo.adapterphysics.AdapterPhysics;
import ru.cfif11.cosmo.physobject.MassAttractObject3D;

import java.awt.event.KeyEvent;

/**
 * A simple example of a planet orbiting a star. This example demonstrates the use of physics and libraries jpct
 * @author Galkin Aleksandr
 * 
 */
public class Main implements IPaintListener{

	private static final long serialVersionUID = -3626482109116766979L;

    //буфер для экрана
	private FrameBuffer buffer = null;

    //для отлавливания событий с клавиатурой и мышкой
	private KeyMapper keyMapper = null;
	private MouseMapper mouseMapper = null;

    //мир и объекты
	private World world = null;
	private MassAttractObject3D sun = null;
	private MassAttractObject3D earth = null;

    //переменный для определения направления движения камеры
	private boolean forward = false;
	private boolean backward = false;
	private boolean up = false;
	private boolean down = false;
	private boolean left = false;
	private boolean right = false;

    //переменная для работы с основным циклом
	private boolean doLoop = true;

	private int fps = 0;
	private long time = System.currentTimeMillis();
	

	private float xAngle = 0;
	

    //счетчик времени
	private Ticker ticker = new Ticker(15);

	public static void main(String[] args) throws Exception {
		Config.glVerbose = true;
		Main cd = new Main();
		cd.init();
		cd.loop();
	}

    //инициализация
	public Main() throws Exception {
		Config.glAvoidTextureCopies = true;
		Config.maxPolysVisible = 1000;
		Config.glColorDepth = 24;
		Config.glFullscreen = false;
		Config.farPlane = 4000;
		Config.glShadowZBias = 0.8f;
		Config.lightMul = 1;
		Config.collideOffset = 500;
		Config.glTrilinear = true;
	}

	private void init() throws Exception {

        //содаем буффер
		buffer = new FrameBuffer(800, 600, FrameBuffer.SAMPLINGMODE_NORMAL);
		buffer.disableRenderer(IRenderer.RENDERER_SOFTWARE);
        //используем openGL, а не всякие swingи или awt
		buffer.enableRenderer(IRenderer.RENDERER_OPENGL);
		buffer.setPaintListener(this);

        //создаем мир
		world = new World();
		world.setAmbientLight(0, 255, 0);

        //создаем обработчики
		keyMapper = new KeyMapper();
		mouseMapper = new MouseMapper(buffer);
		mouseMapper.hide();


        //создаем объекты на основе примитивов
		earth = new MassAttractObject3D(Primitives.getSphere(100, 10), new SimpleVector(19.4,0,0),10);
		sun = new MassAttractObject3D(Primitives.getSphere(100, 10), new SimpleVector(), 2e+15);

        //передвигаем объекты
		sun.translate(0, 0, 0);
		earth.translate(0, -334.7f, 0);

        //добавляем к миру строим и компилируем
		world.addObject(sun);
		world.addObject(earth);
		world.buildAllObjects();
		sun.compileAndStrip();
		earth.compileAndStrip();

        //создаем камеру, перемещаем в заданную точку и направляем ее взор на центр Солнца
		Camera cam = world.getCamera();
		cam.setPosition(600, 0,60);
		cam.lookAt(sun.getTransformedCenter());
		//cam.setFOV(1.5f);
	}

    //основной цикл программы
	private void loop() throws Exception {

        //создаем адаптер
		AdapterPhysics adapter = new AdapterPhysics(world);
		long ticks = 0;

		while (doLoop) {
			ticks = ticker.getTicks();
			if (ticks > 0) {
                //рассчитываем все силы и считаем новые местоположения объектов
				adapter.calcForce();
				earth.calcLocation(0.1f);	
				sun.calcLocation(0.1f);
                //используем обработчик событий для движения камеры
				pollControls();
				move(ticks);
				System.out.println("ticks=" + ticks);
			}
            //очищаем буфер и рисуем заново
			buffer.clear(java.awt.Color.BLUE);
			buffer.setPaintListenerState(false);
			world.renderScene(buffer);
			world.draw(buffer);
			buffer.update();
			buffer.displayGLOnly();
            //не используется, а вообще для подсчета fps
			if (System.currentTimeMillis() - time >= 1000) {
				System.out.println(fps);
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
			world.checkCameraCollisionEllipsoid(Camera.CAMERA_MOVEIN,
					ellipsoid, ticks, 5);
		}

		if (backward) {
			world.checkCameraCollisionEllipsoid(Camera.CAMERA_MOVEOUT,
					ellipsoid, ticks, 5);
		}

		if (left) {
			world.checkCameraCollisionEllipsoid(Camera.CAMERA_MOVELEFT,
					ellipsoid, ticks, 5);
		}

		if (right) {
			world.checkCameraCollisionEllipsoid(Camera.CAMERA_MOVERIGHT,
					ellipsoid, ticks, 5);
		}

		if (up) {
			world.checkCameraCollisionEllipsoid(Camera.CAMERA_MOVEUP,
					ellipsoid, ticks, 5);
		}

		if (down) {
			world.checkCameraCollisionEllipsoid(Camera.CAMERA_MOVEDOWN,
					ellipsoid, ticks, 5);
		}

		// mouse rotation

		Matrix rot = world.getCamera().getBack();
		int dx = mouseMapper.getDeltaX();
		int dy = mouseMapper.getDeltaY();

		float ts = 0.2f * ticks;
		float tsy = ts;

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

		private boolean hidden = false;

		private int height = 0;

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

    //счетчик времени
	private static class Ticker {

		private int rate;
		private long s2;

		public static long getTime() {
			return System.currentTimeMillis();
		}

		public Ticker(int tickrateMS) {
			rate = tickrateMS;
			s2 = Ticker.getTime();
		}

		public int getTicks() {
			long i = Ticker.getTime();
			if (i - s2 > rate) {
				int ticks = (int) ((i - s2) / (long) rate);
				s2 += (long) rate * ticks;
				return ticks;
			}
			return 0;
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

	
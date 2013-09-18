import java.io.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import com.threed.jpct.*;
import com.threed.jpct.util.*;


class CarTest {

   /**
    * A flag that signals a change of the renderer (don't ask about the 35 here....)
    */
   private final static int SWITCH_RENDERER=35;

   /**
    * Should we try to do fullscreen?
    */
   private boolean fullscreen=false;

   /**
    * Are we using OpenGL?
    */
   private boolean openGL=false;

   /**
    * Are we rendering in wireframe mode?
    */
   private boolean wireframe=false;

   /**
    * Some jPCT related stuff
    */
   private FrameBuffer buffer=null;
   private World theWorld=null;
   private TextureManager texMan=null;
   private Camera camera=null;

   /**
    * Some things for the "game logic"
    */
   private Object3D terrain=null;
   private Car car=null;
   private ProjectileManager bulMan=null;
   private PlantManager plantMan=null;
   private SkidMarkManager skidMan=null;

   /**
    * The texture used for blitting the framerate
    */
   private Texture numbers=null;

   /**
    * Default size of the framebuffer
    */
   private int width=640;
   private int height=480;

   /**
    * Some AWT related stuff
    */
   private Frame frame=null;
   private Graphics gFrame=null;
   private BufferStrategy bufferStrategy=null;
   private GraphicsDevice device=null;
   private int titleBarHeight=0;
   private int leftBorderWidth=0;

   private int switchMode=0;

   private int fps;
   private int lastFps;
   private long totalFps;

   private int pps;
   private int lastPps;

   private boolean isIdle=false;
   private boolean exit=false;

   /**
    * Flags for the keys
    */
   private boolean left=false;
   private boolean right=false;
   private boolean forward=false;
   private boolean back=false;
   private boolean fire=false;
   private int fireCount=3;

   private float speed=0;

   private KeyMapper keyMapper=null;

   /**
    * Very complex stuff...impossible to explain...
    */
   public static void main(String[] args) {
      new CarTest(args);
   }

   /**
    * The constructor. Here we are initializing things...
    */
   private CarTest(String[] args) {

     Config.maxPolysVisible=10000;

      /**
       * Evaluate the commandline parameters
       */
      for (int i=0; i<args.length; i++) {
         if (args[i].equals("fullscreen")) {
            fullscreen=true;
            Config.glFullscreen=true;
         }
         if (args[i].equals("mipmap")) {
            Config.glMipmap=true;
         }
         if (args[i].equals("trilinear")) {
            Config.glTrilinear=true;
         }

         if (args[i].equals("16bit")) {
            Config.glColorDepth=16;
         }
         try {
            if (args[i].startsWith("width=")) {
               width=Integer.parseInt(args[i].substring(6));
            }
            if (args[i].startsWith("height=")) {
               height=Integer.parseInt(args[i].substring(7));
            }
            if (args[i].startsWith("refresh=")) {
               Config.glRefresh=Integer.parseInt(args[i].substring(8));
            }
            if (args[i].startsWith("zbuffer=")) {
               Config.glZBufferDepth=Integer.parseInt(args[i].substring(8));
               if (Config.glZBufferDepth==16) {
                  Config.glFixedBlitting=true;
               }
            }

         } catch (Exception e) {
            // We don't care
         }
      }

      isIdle=false;
      switchMode=0;
      totalFps=0;
      fps=0;
      lastFps=0;

      /**
       * Initialize the World instance and get the TextureManager (a singleton)
       */
      theWorld=new World();
      texMan=TextureManager.getInstance();

      /**
       * Setup the lighting. We are not using overbright lighting because the OpenGL
       * renderer can't do it, but we are using RGB-scaling. Some hardware/drivers
       * for OpenGL don't support this (but most do).
       */
      Config.fadeoutLight=false;
      theWorld.getLights().setOverbrightLighting(Lights.OVERBRIGHT_LIGHTING_DISABLED);
      theWorld.getLights().setRGBScale(Lights.RGB_SCALE_2X);
      theWorld.setAmbientLight(25, 30, 30);

      /**
       * Place the lightsources...
       */
      theWorld.addLight(new SimpleVector(0, -150, 0), 25, 22, 19);
      theWorld.addLight(new SimpleVector(-1000, -150, 1000), 22, 5, 4);
      theWorld.addLight(new SimpleVector(1000, -150, -1000), 4, 2, 22);

      /**
       * We are using fog. Please note that the fog implementation is not very well suited for
       * any other fog color than black when using OpenGL's lighting model.
       */
      theWorld.setFogging(World.FOGGING_ENABLED);
      theWorld.setFogParameters(1200, 0, 0, 0);
      Config.farPlane=1200;

      /**
       * Load the textures needed and add them to the TextureManager. We are loading the "numbers"
       * texture for blitting the framerate and the texture for the ground. Textures used by the game
       * entities are loaded by them.
       */
      char c=File.separatorChar;

      numbers=new Texture("textures"+c+"other"+c+"numbers.jpg");
      texMan.addTexture("numbers", numbers);

      Texture rocks=new Texture("textures"+c+"rocks.jpg");
      texMan.addTexture("rocks", rocks);

      /**
       * Load the map (i.e. the terrain or "ground")
       */
      Object3D[] objs=Loader.load3DS("models"+c+"terascene.3ds", 400);
      if (objs.length>0) {
         terrain=objs[0];
         terrain.setTexture("rocks");
      }

      terrain.enableLazyTransformations();
      theWorld.addObject(terrain);

      /**
       * We want to drive around...a car may help
       */
      car=new Car();
      car.addToWorld(theWorld);

      /**
       * The game entities are building themselves, so we only have to build
       * the terrain here
       */
      terrain.build();

      /**
       * The terrain isn't located where we want it to, so we take
       * care of this here:
       */
      SimpleVector pos=terrain.getCenter();
      pos.scalarMul(-1f);
      terrain.translate(pos);
      terrain.rotateX((float)-Math.PI/2f);
      terrain.translateMesh();
      terrain.rotateMesh();
      terrain.setTranslationMatrix(new Matrix());
      terrain.setRotationMatrix(new Matrix());

      /**
       * That won't hurt...
       */
      terrain.createTriangleStrips(2);

      /**
       * The game logic relies on "Managers" in this example. We are
       * instantiating them here.
       */
      bulMan=new ProjectileManager(theWorld);
      plantMan=new PlantManager(theWorld, terrain, 1800);
      skidMan=new SkidMarkManager(theWorld);


      /**
       * Setup the octree and the collision mode/listener for the terrain
       */
      OcTree oc=new OcTree(terrain,50,OcTree.MODE_OPTIMIZED);
      terrain.setOcTree(oc);
      oc.setCollisionUse(OcTree.COLLISION_USE);

      Config.collideOffset=250;

      terrain.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
      terrain.setCollisionOptimization(Object3D.COLLISION_DETECTION_OPTIMIZED);
      terrain.addCollisionListener(new BulletTerrainListener(bulMan));

      /**
       * Place the camera at the starting position.
       */
      camera=theWorld.getCamera();
      camera.setPosition(0,-2500,-1500);
      camera.lookAt(car.getTransformedCenter());

      /**
       * Setup some optimizations for outdoor rendering
       */
      Config.tuneForOutdoor();

      /**
       * Do some AWT setup work
       */
      initializeFrame();

      /**
       * Here we go...!
       */
      gameLoop();
   }

   /**
    * This initializes the AWT frame either in fullscreen or in windowed mode.
    * This is not a waterproof intialization, but i didn't want to do a AWT
    * tutorial here (and i would be the wrong person to do this anyway...:-)).
    * Change whatever you think that needs change here...
    */
   private void initializeFrame() {
      if (fullscreen) {
         GraphicsEnvironment env=GraphicsEnvironment.getLocalGraphicsEnvironment();
         device=env.getDefaultScreenDevice();
         GraphicsConfiguration gc=device.getDefaultConfiguration();
         frame=new Frame(gc);
         frame.setUndecorated(true);
         frame.setIgnoreRepaint(true);
         device.setFullScreenWindow(frame);
         if (device.isDisplayChangeSupported()) {
            device.setDisplayMode(new DisplayMode(width, height, 32, 0));
         }
         frame.createBufferStrategy(2);
         bufferStrategy=frame.getBufferStrategy();
         Graphics g=bufferStrategy.getDrawGraphics();
         bufferStrategy.show();
         g.dispose();
      } else {
         frame=new Frame();
         frame.setTitle("jPCT "+Config.getVersion());
         frame.pack();
         Insets insets = frame.getInsets();
         titleBarHeight=insets.top;
         leftBorderWidth=insets.left;
         frame.setSize(width+leftBorderWidth+insets.right, height+titleBarHeight+insets.bottom);
         frame.setResizable(false);
         frame.show();
         gFrame=frame.getGraphics();
      }

      /**
       * The listeners are bound to the AWT frame...they are useless in OpenGL mode.
       */
      frame.addWindowListener(new WindowEvents());
      keyMapper=new KeyMapper(frame);
   }

   /**
    * The fps counter is blitted into the rendered framebuffer here and the rendering results
    * will be displayed (hence the name of the method...:-))
    */
   private void display() {

      blitNumber((int) totalFps, 5, 2);
      blitNumber((int) lastPps, 5, 12);

      plantMan.drawRadar(buffer, car);

      if (!openGL) {
         if (!fullscreen) {
            buffer.display(gFrame, leftBorderWidth, titleBarHeight);
         } else {
            Graphics g=bufferStrategy.getDrawGraphics();
            g.drawImage(buffer.getOutputBuffer(), 0, 0, null);
            bufferStrategy.show();
            g.dispose();
         }
      } else {
         buffer.displayGLOnly();
      }
   }

   /**
    * A simple method that does the number-blitting.
    */
   private void blitNumber(int number, int x, int y) {
      if (numbers!=null) {
         String sNum=Integer.toString(number);
         for (int i=0; i<sNum.length(); i++) {
            char cNum=sNum.charAt(i);
            int iNum=cNum-48;
            buffer.blit(numbers, iNum*5, 0, x, y, 5, 9, FrameBuffer.TRANSPARENT_BLITTING);
            x+=5;
         }
      }
   }

   /**
    * Does the collision detection and the movement of the car.
    */
   private void moveCar() {

      if (left) {
         car.turnLeft();
      }
      if (right) {
         car.turnRight();
      }

      /**
       * We need to store this matrix for the case that the car can't be placed
       * at the current location.
       */
      Matrix backUpTrans=car.getTranslationMatrix().cloneMatrix();

      if (forward) {
         if (speed<15) {speed+=0.1f;}
         car.setSpeed(speed);
         car.moveForward();
      }
      if (back) {
         if (speed>0) {speed-=0.3f;}
         if (speed<0) {speed=0;
         }
         car.setSpeed(speed);
         car.moveForward();
      }

      if (speed>=0 && !back && !forward) {
         /**
          * If car is still moving but not accelerating anymore,
          * then it should get slower over time.
          */
         speed-=0.075f;
         if (speed<0) {speed=0;}
         car.setSpeed(speed);
         car.moveForward();
      }

      /**
       * Try to place the car at its new location
       */
      boolean ok=car.place(terrain);
      if (!ok) {
         /**
          * Placing the car failed...we'll restore the backup
          * translation matrix in that case (may happen when
          * actually leaving the map).
          */
         car.setTranslationMatrix(backUpTrans);
         speed=0;
         car.setSpeed(speed);
      } else {
         /**
          * The car has been placed, so we can draw some skidmarks if required.
          */
         skidMan.createSkidMarks(car);
      }
   }

   /**
    * Move the camera. The camera will always look at the car from behind and takes
    * the last camera position into account, so that the movement gets a smooth
    * feeling.
    */
   private void moveCamera() {
      SimpleVector oldCamPos=camera.getPosition();
      SimpleVector oldestCamPos=new SimpleVector(oldCamPos);
      oldCamPos.scalarMul(9f);

      SimpleVector carCenter=car.getTransformedCenter();
      SimpleVector camPos=new SimpleVector(carCenter);
      SimpleVector zOffset=car.getZAxis();
      SimpleVector yOffset=new SimpleVector(0, -100, 0);
      zOffset.scalarMul(-250f);

      camPos.add(zOffset);
      camPos.add(yOffset);

      camPos.add(oldCamPos);
      camPos.scalarMul(0.1f);

      SimpleVector delta=camPos.calcSub(oldestCamPos);
      float len=delta.length();

      if (len!=0) {
         /**
          * Do a collision detection between the camera and the ground to prevent the camera from
          * moving into the ground.
          */
         theWorld.checkCameraCollisionEllipsoid(delta.normalize(), new SimpleVector(20, 20, 20), len, 3);
      }

      /**
       * And finally: Look at the car
       */
      camera.lookAt(car.getTransformedCenter());
   }


   /**
    * Generates and/or move existing bullets
    */
   private void processProjectiles() {
      if (fire||fireCount!=3) {
         fireCount--;
         if (fireCount==0) {
            bulMan.createBullet(car);
            fireCount=3;
         }
      }
      bulMan.moveBullets();
   }


   /**
    * This is the game's main loop. We are doing some additional setup work here and
    * rendering the scene in a loop, as well as handling the movement
    * and the collision detection before each frame.
    */
   private void gameLoop() {
      World.setDefaultThread(Thread.currentThread());

      buffer=new FrameBuffer(width, height, FrameBuffer.SAMPLINGMODE_NORMAL);
      buffer.enableRenderer(IRenderer.RENDERER_SOFTWARE);
      buffer.setBoundingBoxMode(FrameBuffer.BOUNDINGBOX_NOT_USED);

      buffer.optimizeBufferAccess();

      Timer timer=new Timer(25);
      timer.start();

      Timer fpsTimer=new Timer(1000);
      fpsTimer.start();

      while (!exit) {
         if (!isIdle) {

            long ticks=timer.getElapsedTicks();

            for (int i=0; i<ticks; i++) {
                moveCar();
                processProjectiles();
                moveCamera();
            }

            poll();

            if (switchMode!=0) {
               switchOptions();
            }

            buffer.clear();
            theWorld.renderScene(buffer);

            if (!wireframe) {
               theWorld.draw(buffer);
            }
            else {
               theWorld.drawWireframe(buffer, Color.white);
            }
            buffer.update();
            display();

            fps++;
            pps+=theWorld.getVisibilityList().getSize();

            if (fpsTimer.getElapsedTicks()>0) {
               totalFps=(fps-lastFps);
               lastFps=fps;
               lastPps=pps;
               pps=0;
            }

            Thread.yield();

         } else {
            try {
               Thread.sleep(500);
            } catch (InterruptedException e) {}
         }
      }

      if (!openGL && fullscreen) {
         device.setFullScreenWindow(null);
      }

      System.exit(0);
   }

   /**
    * This is for switching settings. Currently, only switching from OpenGL to software and
    * back is supported here. This is done to avoid switching modes while polling the keyboard,
    * because it may have undesired side-effects otherwise.
    */
   private void switchOptions() {
      switch (switchMode) {
         case (SWITCH_RENDERER): {
            isIdle=true;
            if (buffer.usesRenderer(IRenderer.RENDERER_OPENGL)) {
               keyMapper.destroy();
               buffer.disableRenderer(IRenderer.RENDERER_OPENGL);
               buffer.enableRenderer(IRenderer.RENDERER_SOFTWARE, IRenderer.MODE_OPENGL);
               openGL=false;
               if (fullscreen) {
                  device.setFullScreenWindow(null);
               }
               frame.hide();
               frame.dispose();
               initializeFrame();
            } else {
               frame.hide();
               buffer.enableRenderer(IRenderer.RENDERER_OPENGL, IRenderer.MODE_OPENGL);
               buffer.disableRenderer(IRenderer.RENDERER_SOFTWARE);
               openGL=true;
               keyMapper.destroy();
               keyMapper=new KeyMapper();
            }
            isIdle=false;
            break;
         }
      }
      switchMode=0;
   }


   /**
    * Use the KeyMapper to poll the keyboard
    */
   private void poll() {
      KeyState state=null;
      do {
         state=keyMapper.poll();
         if (state!=KeyState.NONE) {
            keyAffected(state);
         }
      } while (state!=KeyState.NONE);
   }


   private void keyAffected(KeyState state) {
      int code=state.getKeyCode();
      boolean event=state.getState();

      switch (code) {
         case (KeyEvent.VK_ESCAPE): {
            exit=event;
            break;
         }
         case (KeyEvent.VK_LEFT): {
            left=event;
            break;
         }
         case (KeyEvent.VK_RIGHT): {
            right=event;
            break;
         }
         case (KeyEvent.VK_UP): {
            forward=event;
            break;
         }
         case (KeyEvent.VK_SPACE): {
            fire=event;
            break;
         }
         case (KeyEvent.VK_DOWN): {
            back=event;
            break;
         }
         case (KeyEvent.VK_W): {
            if (event) {
               wireframe=!wireframe;
            }
            break;
         }
         case (KeyEvent.VK_X): {
            if (event) {
               switchMode=SWITCH_RENDERER;
            }
            break;
         }
      }
   }

   /**
    * The WindowApapter used for software mode
    */
   private class WindowEvents extends WindowAdapter {

      public void windowIconified(WindowEvent e) {
         isIdle=true;
      }

      public void windowDeiconified(WindowEvent e) {
         isIdle=false;
      }
   }


   private class Timer {

      private long ticks=0;
      private long granularity=0;

      public Timer(int granularity) {
         this.granularity=granularity;
      }

      public void start() {
         ticks=System.currentTimeMillis();
      }

      public void reset() {
         start();
      }

      public long getElapsedTicks() {
         long cur=System.currentTimeMillis();
         long l=cur-ticks;

         if (l>=granularity) {
            ticks=cur-(l%granularity);
            return l/granularity;
         }
         return 0;
      }
   }

}

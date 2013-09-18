import java.io.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import com.threed.jpct.*;
import com.threed.jpct.util.*;

/**
 * This is a simple demonstration of how a first-person-shooter like application
 * can be implementated using jPCT. It shows fps-like movement and collision detection
 * as well as loading a 3DS-level, some OcTree-stuff etc.
 */
class JPCTDemo {

   /**
    * The starting position of the player
    */
   private final static SimpleVector STARTING_POS=new SimpleVector(800, -120, -400);

   /**
    * The radius of the sphere used for sphere/polygon collision detection
    */
   private final static float COLLISION_SPHERE_RADIUS=8f;

   /**
    * The "height" of the player, i.e. how many units the camera is loacted above the ground.
    */
   private final static float PLAYER_HEIGHT=30f;

   /**
    * The dimensions of the ellipsoid used for collision detection. This represents the "size"
    * of the player
    */
   private final static SimpleVector ELLIPSOID_RADIUS=new SimpleVector(COLLISION_SPHERE_RADIUS,PLAYER_HEIGHT/2f,COLLISION_SPHERE_RADIUS);

   /**
   * The speed with which the player will fall if there's no ground below his feet
   */
   private final static float GRAVITY=4f;

   /**
    * How fast the player will move (in world units)
    */
   private final static float MOVE_SPEED=2.5f;

   /**
    * How fast the player will turn
    */
   private final static float TURN_SPEED=0.06f;

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
    * Some jPCT related stuff...
    */
   private Object3D level=null;
   private Object3D weapon=null;
   private Object3D elevator=null;
   private FrameBuffer buffer=null;
   private World theWorld=null;
   private TextureManager texMan=null;
   private Camera camera=null;

   /**
    * The texture used for blitting the framerate
    */
   private Texture numbers=null;

   /**
    * playerDirection stores the player's current orientation, so that the player's
    * movement is decoupled from the actual camera.
    */
   private Matrix playerDirection=new Matrix();
   private SimpleVector tempVector=new SimpleVector();

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
   private boolean up=false;
   private boolean down=false;
   private boolean forward=false;
   private boolean back=false;

   /**
    * The KeyMapper that offers a uniform way to access
    * the keyboard in hard- and software-mode
    */
   private KeyMapper keyMapper=null;

   /**
    * Some vars to handle the elevator
    */
   float elevatorOffset=-0.8f;
   float elevatorPosition=-90f;
   int elevatorCountdown=50;

   /**
    * Very complex stuff...impossible to explain...
    */
   public static void main(String[] args) {
      JPCTDemo start=new JPCTDemo(args);
   }

   /**
    * The constructor. Here we are initializing things...
    */
   private JPCTDemo(String[] args) {
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
            // We don't care...
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
       * for OpenGL don't support this.
       */
      Config.fadeoutLight=true;
      Config.linearDiv=100;
      Config.lightDiscardDistance=350;
      theWorld.getLights().setOverbrightLighting(Lights.OVERBRIGHT_LIGHTING_DISABLED);
      theWorld.getLights().setRGBScale(Lights.RGB_SCALE_2X);
      theWorld.setAmbientLight(10, 15, 15);

      /**
       * Place the lightsources...
       */
      theWorld.addLight(new SimpleVector(820, -150, -400), 5, 20, 15);
      theWorld.addLight(new SimpleVector(850, -130, -580), 20, 18, 2);
      theWorld.addLight(new SimpleVector(850, -130, -760), 15, 10, 15);
      theWorld.addLight(new SimpleVector(1060, -170, -910), 20, 0, 0);
      theWorld.addLight(new SimpleVector(760, -200, -990), 15, 10, 20);
      theWorld.addLight(new SimpleVector(850, -230, -780), 0, 15, 25);
      theWorld.addLight(new SimpleVector(600, -230, -770), 20, 25, 0);
      theWorld.addLight(new SimpleVector(405, -230, -610), 18, 20, 25);
      theWorld.addLight(new SimpleVector(340, -150, -370), 15, 20, 25);
      theWorld.addLight(new SimpleVector(650, -170, -200), 15, 0, 0);
      theWorld.addLight(new SimpleVector(870, -230, -190), 15, 20, 20);
      theWorld.addLight(new SimpleVector(540, -190, -180), 15, 15, 15);

      /**
       * We are using fog. Please note that the fog implementation is not very well suited for
       * any other fog color than black when using OpenGL's lighting model.
       */
      theWorld.setFogging(World.FOGGING_ENABLED);
      theWorld.setFogParameters(500, 0, 0, 0);
      Config.farPlane=500;

      /**
       * Load the textures needed and add them to the TextureManager. We are loading the "numbers"
       * texture for blitting the framerate as well as the weapon's environment map and all JPGs
       * that can be found in the "textures"-directory. The 3DS file of the level contains
       * materials that are pointing to these textures (identified by name).
       */
      char c=File.separatorChar;
      numbers=new Texture("textures"+c+"other"+c+"numbers.jpg");
      texMan.addTexture("numbers", numbers);
      texMan.addTexture("envmap", new Texture("textures"+c+"other"+c+"envmap.jpg"));

      File dir=new File("textures");
      String[] files=dir.list();
      for (int i=0; i<files.length; i++) {
         String name=files[i];
         if (name.toLowerCase().endsWith(".jpg")) {
            texMan.addTexture(name, new Texture("textures"+c+name));
         }
      }

      /**
       * Load and setup the weapon. To ease the placement of the weapon in the gameloop,
       * we are transforming the mesh here too.
       */
      Object3D[] miss=Loader.load3DS("3ds"+c+"weapon.3ds", 2);
      weapon=miss[0];
      weapon.rotateY(-(float) Math.PI/2f);
      weapon.rotateZ(-(float) Math.PI/2f);
      weapon.rotateX(-(float) Math.PI/7f);

      // Make the rotations permanent
      weapon.rotateMesh();
      weapon.translate(6, 6, 10);

      // Make the translation permanent
      weapon.translateMesh();
      weapon.setRotationMatrix(new Matrix());
      weapon.setTranslationMatrix(new Matrix());

      weapon.setTexture("envmap");
      weapon.setEnvmapped(Object3D.ENVMAP_ENABLED);
      weapon.setEnvmapMode(Object3D.ENVMAP_WORLDSPACE);
      theWorld.addObject(weapon);

      /**
       * Load the level...
       */
      Object3D[] levelParts=Loader.load3DS("3ds"+c+"ql.3ds", 20f);
      level=new Object3D(0);

      for (int i=0; i<levelParts.length; i++) {
         Object3D part=levelParts[i];
         /**
          * The level is not rotated correctly after loading (something all Quake3 levels
          * share when converted to 3DS-format, because Quake3 uses a different coordinate
          * system than jPCT.
          */
         part.setCenter(SimpleVector.ORIGIN);
         part.rotateX((float)-Math.PI/2);
         part.rotateMesh();
         part.setRotationMatrix(new Matrix());

         /**
          * Merge all parts into one object. This is usefull for this level, because the parts
          * don't represent sectors or zones but are widespreaded over the level, which would render
          * an octree useless for this level. By merging them all, the octree can be used much
          * more efficient.
          */
         level=Object3D.mergeObjects(level, part);
      }

      /**
       * Create triangle strips (good for OpenGL performance) and setup the collision
       * detection mode. Furthermore, an octree is created for the level.
       */
      level.createTriangleStrips(2);
      level.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
      level.setCollisionOptimization(Object3D.COLLISION_DETECTION_OPTIMIZED);

      OcTree oc=new OcTree(level, 100, OcTree.MODE_OPTIMIZED);
      oc.setCollisionUse(OcTree.COLLISION_USE);
      level.setOcTree(oc);

      /**
       * The level won't move, so...
       */
      level.enableLazyTransformations();

      /**
       * Done! Now add the result to the world.
       */
      theWorld.addObject(level);

      /**
       * Setup the elevator. The elevator is a more or
       * less hardcoded entity in this level that serves
       * demonstration purposes only. You'll most
       * likely have to implement a more advanced elevator
       * management if you want to use elevators at all.
       */
      elevator=Primitives.getBox(15f,0.1f);
      elevator.rotateY((float)Math.PI/4);
      elevator.setOrigin(new SimpleVector(800,-90,-450));
      elevator.setCollisionOptimization(Object3D.COLLISION_DETECTION_OPTIMIZED);
      elevator.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
      elevator.setTexture("envmap");
      elevator.setEnvmapped(Object3D.ENVMAP_ENABLED);
      elevator.setEnvmapMode(Object3D.ENVMAP_CAMERASPACE);
      theWorld.addObject(elevator);

      /**
       * Place the camera at the starting position.
       */
      camera=theWorld.getCamera();
      camera.setPosition(STARTING_POS);

      /**
       * Now build() all objects.
       */
      theWorld.buildAllObjects();

      /**
       * This is needed for weapon movement, but has to be done after calling build()
       * on the weapon because build() resets the rotation pivot to the object's
       * (calculated) center.
       */
      weapon.setRotationPivot(new SimpleVector(0, 0, 0));

      /**
       * Setup some optimizations for outdoor rendering (the level is not very outdoorish at
       * all, but at least the framebuffer needs clearing because the level is not closed.
       */
      Config.collideOffset=250;
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
    * The fps counter is blitted into the rendered framebuffer here and the results
    * will be displayed (hence the name of the method...:-))
    */
   private void display() {
      blitNumber((int) totalFps, 5, 2);
      blitNumber((int) lastPps, 5, 12);

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
    * Does the collision detection and the movement of the player.
    */
   private void doMovement() {

      /**
       * The first part handles the "physics", i.e. it allows the player to fall down.
       */

      SimpleVector camPos=camera.getPosition();
      camPos.add(new SimpleVector(0, PLAYER_HEIGHT/2f, 0));
      SimpleVector dir=new SimpleVector(0, GRAVITY, 0);
      dir=theWorld.checkCollisionEllipsoid(camPos, dir, ELLIPSOID_RADIUS, 1);
      camPos.add(new SimpleVector(0, -PLAYER_HEIGHT/2f, 0));
      dir.x=0;
      dir.z=0;
      camPos.add(dir);
      camera.setPosition(camPos);

      /**
       * The "falling" part of the player is now finished. Now we care for the horizontal movement.
       * Nothing special here and no problems either. One thing worth mentioning is, that the player is
       * always moving in the same plane regardless of where he's looking. playerDirection takes
       * care of this.
       */

      // forward and back may change during excution (threaded!), so we have to ensure to
      // reset the camera only if has been changed before.
      boolean cameraChanged=false;

      if (forward) {
         camera.moveCamera(new SimpleVector(0,1,0), PLAYER_HEIGHT/2f);
         cameraChanged=true;
         tempVector=playerDirection.getZAxis();
         theWorld.checkCameraCollisionEllipsoid(tempVector, ELLIPSOID_RADIUS, MOVE_SPEED, 5);
      }
      if (back) {
         if (!cameraChanged) {
            camera.moveCamera(new SimpleVector(0,1,0), PLAYER_HEIGHT/2f);
            cameraChanged=true;
         }
         tempVector=playerDirection.getZAxis();
         tempVector.scalarMul(-1f);
         theWorld.checkCameraCollisionEllipsoid(tempVector, ELLIPSOID_RADIUS, MOVE_SPEED, 5);
      }

      if (left) {
         camera.rotateAxis(camera.getBack().getYAxis(), -TURN_SPEED);
         playerDirection.rotateY(-TURN_SPEED);
      }
      if (right) {
         camera.rotateAxis(camera.getBack().getYAxis(), TURN_SPEED);
         playerDirection.rotateY(TURN_SPEED);
      }

      if (up) {
         camera.rotateX(TURN_SPEED);
      }
      if (down) {
         camera.rotateX(-TURN_SPEED);
      }

      if (cameraChanged) {
         camera.moveCamera(new SimpleVector(0, -1, 0), PLAYER_HEIGHT/2f);
      }
   }


   /**
    * Move the elevator up/down
    */
   private void moveElevator() {

      if ((elevator.wasTargetOfLastCollision()&&elevatorCountdown--<=0)||
          (elevatorPosition!=-90f&&elevatorPosition!=-180f)) {

         float tempElevator=elevatorPosition+elevatorOffset;
         float tempOffset=elevatorOffset;

         if (tempElevator<-180f) {
            tempOffset=-180f-elevatorPosition;
            elevatorCountdown=50;
            elevatorOffset*=-1f;
         } else {
            if (tempElevator>-90f) {
               tempOffset=-90f-elevatorPosition;
               elevatorCountdown=50;
               elevatorOffset*=-1f;
            }
         }
         elevatorPosition+=tempOffset;
         elevator.translate(0, tempOffset, 0);
      }
   }

   /**
    * Poll the keyboard using the KeyMapper from com.threed.jpct.util
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

   /**
    * This is the game's main loop. We are doing some additional setup work here and
    * rendering the scene in a loop, as well as calling doMovement() to handle the movement
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

      long timerTicks=0;

      while (!exit) {

         if (!isIdle) {

            long ticks=timer.getElapsedTicks();
            timerTicks+=ticks;

            for (int i=0; i<ticks; i++) {
               /**
                * Do this as often as ticks have passed. This can
                * be improved by calling the method only once and letting
                * the collision detection somehow handle the ticks passed.
                */
                doMovement();
                moveElevator();
            }

            poll();

            if (switchMode!=0) {
               switchOptions();
            }

            buffer.clear();

            weapon.getTranslationMatrix().setIdentity();
            weapon.translate(camera.getPosition());
            weapon.align(camera);
            weapon.rotateAxis(camera.getDirection(), (float) Math.sin(timerTicks/6f)/20f);

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

      buffer.dispose();
      if (!openGL && fullscreen) {
        device.setFullScreenWindow(null);
      }
      System.exit(0);
   }

   /**
    * This is for switching settings. Currently, only switching from OpenGL to software and
    * back is supported here.
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
               keyMapper.destroy();
               buffer.enableRenderer(IRenderer.RENDERER_OPENGL, IRenderer.MODE_OPENGL);
               buffer.disableRenderer(IRenderer.RENDERER_SOFTWARE);
               openGL=true;
               keyMapper=new KeyMapper();
            }
            isIdle=false;
            break;
         }
      }
      switchMode=0;
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
         case (KeyEvent.VK_PAGE_UP): {
            up=event;
            break;
         }
         case (KeyEvent.VK_PAGE_DOWN): {
            down=event;
            break;
         }
         case (KeyEvent.VK_UP): {
            forward=event;
            break;
         }
         case (KeyEvent.VK_DOWN): {
            back=event;
            break;
         }
         case (KeyEvent.VK_1): {
            if (event&&buffer.supports(FrameBuffer.SUPPORT_FOR_RGB_SCALING)) {
               theWorld.getLights().setRGBScale(Lights.RGB_SCALE_DEFAULT);
            }
            break;
         }

         case (KeyEvent.VK_2): { // 2x scaling
            if (event&&buffer.supports(FrameBuffer.SUPPORT_FOR_RGB_SCALING)) {
               theWorld.getLights().setRGBScale(Lights.RGB_SCALE_2X);
            }
            break;
         }

         case (KeyEvent.VK_4): { // 4x scaling
            if (event&&buffer.supports(FrameBuffer.SUPPORT_FOR_RGB_SCALING)) {
               theWorld.getLights().setRGBScale(Lights.RGB_SCALE_4X);
            }
            break;
         }

         case (KeyEvent.VK_W): { // wireframe mode (w)
            if (event) {
               wireframe=!wireframe;
            }
            break;
         }

         case (KeyEvent.VK_X): { // change renderer  (x)
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

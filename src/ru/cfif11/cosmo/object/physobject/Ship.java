package ru.cfif11.cosmo.object.physobject;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import org.lwjgl.opengl.Display;
import ru.cfif11.cosmo.Main;
import ru.cfif11.cosmo.control1.ControllableMKInterface;
import ru.cfif11.cosmo.control1.MouseListener;
import ru.cfif11.cosmo.object.MovableInterface;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public class Ship extends MassObject3D implements ControllableMKInterface, MovableInterface {

    public static final String[] KEYS = new String[] {  "Up", "Down", "Left"    , "Right"   , "W",
            "Q" , "C"   , "Page Up" , "Page Down"};

    private boolean[] keyStates = new boolean[KEYS.length];

    private MouseListener mouseListener;


    /**
     * Create MassObject3D on base Object3D, velocity and mass
     *
     * @param obj      the Object3D
     * @param velocity the velocity
     * @param mass     the mass
     */
    public Ship(Object3D obj, String name, SimpleVector velocity, double mass, int[] characteristicSize) {
        super(obj, name, velocity, mass);
        this.characteristicSize = characteristicSize;
        movable = true;
    }

    public Ship(String model, float scale, String name, SimpleVector velocity, double mass, int[] characteristicSize) {
        super(Loader.load3DS(model, scale)[0], name, velocity, mass);
        this.characteristicSize = characteristicSize;
    }

    @Override
    public boolean pollControls() {
        Main.KEYBOARD_LISTENER.recordPollСontrols(KEYS, keyStates);
        return !Display.isCloseRequested();
    }

    @Override
    public void applyControl(long ticks, FrameBuffer buffer) {
    /*    if(ticks == 0) {
            return;
        }

        // Key control
        SimpleVector ellipsoid = new SimpleVector(5, 5, 5);

        if (keyStates[0]) {
            if(fixedOrientation)
                gameWorld.getWorld().checkCameraCollisionEllipsoid(com.threed.jpct.Camera.CAMERA_MOVEUP, ellipsoid, ticks, 5);
            else
                gameWorld.getWorld().checkCameraCollisionEllipsoid(com.threed.jpct.Camera.CAMERA_MOVEIN, ellipsoid, ticks, 5);
        }

        if (keyStates[1]) {
            if(fixedOrientation)
                gameWorld.getWorld().checkCameraCollisionEllipsoid(com.threed.jpct.Camera.CAMERA_MOVEDOWN, ellipsoid, ticks, 5);
            else
                gameWorld.getWorld().checkCameraCollisionEllipsoid(com.threed.jpct.Camera.CAMERA_MOVEOUT, ellipsoid, ticks, 5);
        }

        if (keyStates[2]) {
            gameWorld.getWorld().checkCameraCollisionEllipsoid(com.threed.jpct.Camera.CAMERA_MOVELEFT, ellipsoid, ticks, 5);
        }

        if (keyStates[3]) {
            gameWorld.getWorld().checkCameraCollisionEllipsoid(com.threed.jpct.Camera.CAMERA_MOVERIGHT, ellipsoid, ticks, 5);
        }

        if(keyStates[6]) {
            if(!firstPress)
                changeFixation();
            firstPress = true;
        } else
            firstPress = false;

        if (keyStates[7]) {
            if(fixedOrientation)
                gameWorld.getWorld().checkCameraCollisionEllipsoid(com.threed.jpct.Camera.CAMERA_MOVEIN, ellipsoid, ticks, 5);
            else
                gameWorld.getWorld().checkCameraCollisionEllipsoid(com.threed.jpct.Camera.CAMERA_MOVEUP, ellipsoid, ticks, 5);
        }

        if (keyStates[8]) {
            if(fixedOrientation)
                gameWorld.getWorld().checkCameraCollisionEllipsoid(com.threed.jpct.Camera.CAMERA_MOVEOUT, ellipsoid, ticks, 5);
            else
                gameWorld.getWorld().checkCameraCollisionEllipsoid(com.threed.jpct.Camera.CAMERA_MOVEDOWN, ellipsoid, ticks, 5);
        }

        if (!fixedOrientation) {
            rotateCamera(ticks);
        } else {
            if(!mouseListener.isInsideWindow())
                mouseListener.setCursorPosition();
            if(mouseListener.getMouseX() < 10)
                gameWorld.getWorld().checkCameraCollisionEllipsoid(com.threed.jpct.Camera.CAMERA_MOVELEFT, ellipsoid, ticks, 5);
            else if(mouseListener.getMouseX() > width - 10)
                gameWorld.getWorld().checkCameraCollisionEllipsoid(com.threed.jpct.Camera.CAMERA_MOVERIGHT, ellipsoid, ticks, 5);
            if(mouseListener.getMouseY() < 10)
                gameWorld.getWorld().checkCameraCollisionEllipsoid(com.threed.jpct.Camera.CAMERA_MOVEUP, ellipsoid, ticks, 5);
            else if(mouseListener.getMouseY() > height - 10)
                gameWorld.getWorld().checkCameraCollisionEllipsoid(com.threed.jpct.Camera.CAMERA_MOVEDOWN, ellipsoid, ticks, 5);

            if(mouseListener.isButtonDown(0)) {
                if(!rotate) {
                    if(!objectSelection(buffer))
                        rotate = true;
                }
                if(rotate)
                    rotateCamera(ticks);
            } else {
                if(fixedOrientation && !mouseListener.isVisible())
                    mouseListener.show();
                rotate = false;
            }
        }    */
    }

    @Override
    public void move(long ticks, FrameBuffer buffer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

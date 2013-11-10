package ru.cfif11.cosmo.object;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Interact2D;
import com.threed.jpct.Matrix;
import com.threed.jpct.SimpleVector;
import org.lwjgl.opengl.Display;
import ru.cfif11.cosmo.Main;
import ru.cfif11.cosmo.Ticker;
import ru.cfif11.cosmo.control1.ControllableMKInterface;
import ru.cfif11.cosmo.control1.MouseListener;
import ru.cfif11.cosmo.object.physobject.PhysObject3D;
import ru.cfif11.cosmo.scene.GameWorld;
import ru.cfif11.cosmo.scene.Scene;

import java.util.Enumeration;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public class Camera implements MovableInterface, ControllableMKInterface {

    private boolean firstPress          = false;
    private boolean fixedOrientation    = false;
    private boolean rotate              = false;

    private GameWorld               gameWorld;
    private com.threed.jpct.Camera  cam;

    private final SimpleVector shiftCam = new SimpleVector(0, -100, 0);

    private int height;
    private int width;


    public static final String[] KEYS = new String[] {  "Up", "Down", "Left", "Right", "C", "Page Up", "Page Down"};

    private boolean[] keyStates = new boolean[KEYS.length];


    public Camera(GameWorld gameWorld, Ticker ticker, FrameBuffer buffer) {
        this.gameWorld  = gameWorld;
        this.cam        = gameWorld.getWorld().getCamera();
        Scene.MOUSE_LISTENER   = new MouseListener(buffer);
        if(!fixedOrientation)
            Scene.MOUSE_LISTENER.hide();
        height = buffer.getOutputHeight();
        width  = buffer.getOutputWidth();
    }

    @Override
    public boolean pollControls() {
        Main.KEYBOARD_LISTENER.recordPollСontrols(KEYS, keyStates);
        return !Display.isCloseRequested();
    }

    private void allFalse() {
        for(boolean bool : keyStates)
            bool = false;
    }

    //двигаем камеру в зависимости от того, куда нажали
    @Override
    public void applyControl(long ticks, FrameBuffer buffer) {

        if(ticks == 0) {
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

        if(keyStates[4]) {
            if(!firstPress)
                changeFixation();
            firstPress = true;
        } else
            firstPress = false;

        if (keyStates[5]) {
            if(fixedOrientation)
                gameWorld.getWorld().checkCameraCollisionEllipsoid(com.threed.jpct.Camera.CAMERA_MOVEIN, ellipsoid, ticks, 5);
            else
                gameWorld.getWorld().checkCameraCollisionEllipsoid(com.threed.jpct.Camera.CAMERA_MOVEUP, ellipsoid, ticks, 5);
        }

        if (keyStates[6]) {
            if(fixedOrientation)
                gameWorld.getWorld().checkCameraCollisionEllipsoid(com.threed.jpct.Camera.CAMERA_MOVEOUT, ellipsoid, ticks, 5);
            else
                gameWorld.getWorld().checkCameraCollisionEllipsoid(com.threed.jpct.Camera.CAMERA_MOVEDOWN, ellipsoid, ticks, 5);
        }

        if (!fixedOrientation) {
            rotateCamera(ticks);
        } else {

            if(Scene.MOUSE_LISTENER.isButtonDown(0)) {
                if(!rotate) {
                    if(!objectSelection(buffer))
                        rotate = true;
                }
                if(rotate)
                    rotateCamera(ticks);
            } else {
                if(fixedOrientation && !Scene.MOUSE_LISTENER.isVisible())
                    Scene.MOUSE_LISTENER.show();
                rotate = false;
            }
        }

    }

    @Override
    public void move(long ticks, FrameBuffer buffer){}

    private void rotateCamera(long ticks) {
        int t = 1;
        if(rotate) {
            Scene.MOUSE_LISTENER.hide();
            t = -1;
        }
        Matrix rot = cam.getBack();
        int dx = Scene.MOUSE_LISTENER.getDeltaX()*t;
        int dy = Scene.MOUSE_LISTENER.getDeltaY()*t;
        float ts = 0.2f * ticks;
        float tsy = ts;

        if (dx != 0) {
            ts = dx / 500f;
        }
        if (dy != 0) {
            tsy = dy / 500f;
        }

        if (dx != 0) {
            //rot.rotateAxis(rot.getXAxis(), ts);
            rot.rotateY(-ts);
        }

        if (dy != 0) {
            rot.rotateX(tsy);
        }
    }

    public void setPosition(float x, float y, float z) {
        cam.setPosition(x, y, z);
    }

    public void setPosition(SimpleVector pos) {
        cam.setPosition(pos);
    }

    public void lookAt(SimpleVector obj) {
        cam.lookAt(obj);
    }

    public void setFOV(float fov) {
        cam.setFOV(fov);
    }

    public SimpleVector getDirection() {
        return cam.getDirection();
    }

    public float getFOV() {
        return cam.getFOV();
    }

    public float convertRADAngleIntoFOV(float angle) {
        return cam.convertRADAngleIntoFOV(angle);
    }

    public SimpleVector getUpVector() {
        return cam.getUpVector();
    }

    public SimpleVector getSideVector() {
        return cam.getSideVector();
    }

    public Matrix getBack() {
        return cam.getBack();
    }

    public SimpleVector getPosition() {
        return cam.getPosition();
    }

    public boolean isFixedOrientation() {
        return fixedOrientation;
    }

    public void setFixedOrientation(boolean fixedOrientation) {
        this.fixedOrientation = fixedOrientation;
        cursorVisibility();
    }


    private boolean objectSelection(FrameBuffer buffer) {
        PhysObject3D                obj;
        SimpleVector                objCenBuf;
        SimpleVector                objBound;
        SimpleVector                shiftCenObj;
        Enumeration<PhysObject3D>   objs = gameWorld.getWorld().getObjects();
        float radius;
        while (objs.hasMoreElements()) {
            obj = objs.nextElement();
            float angle = obj.getTransformedCenter().calcSub(getPosition()).calcAngle(getDirection());
            float fov = convertRADAngleIntoFOV(angle);
            if(Math.abs(angle-Math.PI)>0.0001 && fov <= cam.getMaxFOV()) {
                objCenBuf = Interact2D.projectCenter3D2D(buffer, obj);
                if((objCenBuf.x > 0 && objCenBuf.x < width) && (
                        objCenBuf.y > 0 && objCenBuf.y < height)) {
                    shiftCenObj = cam.getSideVector();
                    shiftCenObj.scalarMul(obj.getCharacteristicSize()[0]);
                    objBound = obj.getTransformedCenter().calcAdd(shiftCenObj);
                    radius = objCenBuf.calcSub(Interact2D.project3D2D(cam, buffer, objBound)).length();
                    if(objCenBuf.calcSub(new SimpleVector(Scene.MOUSE_LISTENER.getMouseX(),
                            Scene.MOUSE_LISTENER.getMouseY(), 0)).length() < radius) {
                        gameWorld.setSelectObject(obj);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void changeFixation() {
        fixedOrientation = !fixedOrientation;
        cursorVisibility();
        allFalse();
    }

    private void cursorVisibility() {
        if(fixedOrientation)
            Scene.MOUSE_LISTENER.show();
        else
            Scene.MOUSE_LISTENER.hide();
    }

    public void lookTo(PhysObject3D selectObject) {
        SimpleVector objPos = selectObject.getTransformedCenter();
        setPosition(objPos.calcAdd(shiftCam));
        cam.setOrientation(objPos.calcSub(cam.getPosition()), new SimpleVector(objPos.x,0,objPos.z));
    }
}

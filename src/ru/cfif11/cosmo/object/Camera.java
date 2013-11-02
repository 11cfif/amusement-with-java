package ru.cfif11.cosmo.object;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Interact2D;
import com.threed.jpct.Matrix;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.util.KeyState;
import org.lwjgl.opengl.Display;
import ru.cfif11.cosmo.Ticker;
import ru.cfif11.cosmo.control1.ControllableMKInterface;
import ru.cfif11.cosmo.control1.KeyboardListener;
import ru.cfif11.cosmo.control1.MouseListener;
import ru.cfif11.cosmo.object.physobject.PhysObject3D;
import ru.cfif11.cosmo.scene.GameWorld;

import java.awt.event.KeyEvent;
import java.util.Enumeration;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public class Camera implements MovableInterface, ControllableMKInterface {

    private boolean forward     = false;
    private boolean backward    = false;
    private boolean up          = false;
    private boolean down        = false;
    private boolean left        = false;
    private boolean right       = false;
    private boolean fast        = false;
    private boolean slow        = false;
    private boolean fixation    = false;

    private boolean firstPress          = false;
    private boolean fixedOrientation    = false;

    private KeyboardListener        keyListener;
    private MouseListener           mouseListener;
    private GameWorld               gameWorld;
    private com.threed.jpct.Camera  cam;
    private Ticker                  ticker;

    private int height;
    private int width;

    private int rate = 15;


    public Camera(GameWorld gameWorld, Ticker ticker, FrameBuffer buffer) {
        this.gameWorld  = gameWorld;
        this.ticker     = ticker;
        this.cam        = gameWorld.getWorld().getCamera();
        keyListener     = new KeyboardListener();
        mouseListener   = new MouseListener(buffer);
        if(!fixedOrientation)
            mouseListener.hide();
        height = buffer.getOutputHeight();
        width  = buffer.getOutputWidth();
    }

    @Override
    public boolean pollControls() {
        KeyState ks = keyListener.pollControls();
        while (ks!= KeyState.NONE) {
            if (ks.getKeyCode() == KeyEvent.VK_ESCAPE)
                return false;

            if (ks.getKeyCode() == KeyEvent.VK_UP) {
                if(fixedOrientation)
                    forward = ks.getState();
                else
                    up = ks.getState();
            }

            if (ks.getKeyCode() == KeyEvent.VK_DOWN) {
                if(fixedOrientation)
                    backward = ks.getState();
                else
                    down = ks.getState();
            }

            if (ks.getKeyCode() == KeyEvent.VK_LEFT)
                left = ks.getState();

            if (ks.getKeyCode() == KeyEvent.VK_RIGHT)
                right = ks.getState();

            if (ks.getKeyCode() == KeyEvent.VK_PAGE_UP) {
                if(fixedOrientation)
                    up = ks.getState();
                else
                    forward = ks.getState();
            }

            if (ks.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
                if(fixedOrientation)
                    down = ks.getState();
                else
                    backward = ks.getState();
            }

            if (ks.getKeyCode() == KeyEvent.VK_W)
                fast = ks.getState();

            if (ks.getKeyCode() == KeyEvent.VK_Q)
                slow = ks.getState();

            if (ks.getKeyCode() == KeyEvent.VK_C) {
                allFalse();
                fixation = ks.getState();
                if(fixation)
                    firstPress = true;
            }

            ks = keyListener.pollControls();
        }

        return !Display.isCloseRequested();
    }

    private void allFalse() {
        forward     = false;
        backward    = false;
        up          = false;
        down        = false;
        left        = false;
        right       = false;
        fast        = false;
        slow        = false;
    }

    //двигаем камеру в зависимости от того, куда нажали
    @Override
    public void move(long ticks, FrameBuffer buffer) {

        if (ticks == 0) {
            return;
        }

        // Key control
        SimpleVector ellipsoid = new SimpleVector(5, 5, 5);

        if (forward) {
            gameWorld.getWorld().checkCameraCollisionEllipsoid(com.threed.jpct.Camera.CAMERA_MOVEUP, ellipsoid, ticks, 5);
        }

        if (backward) {
            gameWorld.getWorld().checkCameraCollisionEllipsoid(com.threed.jpct.Camera.CAMERA_MOVEDOWN, ellipsoid, ticks, 5);
        }

        if (left) {
            gameWorld.getWorld().checkCameraCollisionEllipsoid(com.threed.jpct.Camera.CAMERA_MOVELEFT, ellipsoid, ticks, 5);
        }

        if (right) {
            gameWorld.getWorld().checkCameraCollisionEllipsoid(com.threed.jpct.Camera.CAMERA_MOVERIGHT, ellipsoid, ticks, 5);
        }

        if (up) {
            gameWorld.getWorld().checkCameraCollisionEllipsoid(com.threed.jpct.Camera.CAMERA_MOVEIN, ellipsoid, ticks, 5);
        }

        if (down) {
            gameWorld.getWorld().checkCameraCollisionEllipsoid(com.threed.jpct.Camera.CAMERA_MOVEOUT, ellipsoid, ticks, 5);
        }

        if (fast) {
            if (rate > 2) {
                rate--;
                ticker.setRate(rate);
            }
        }

        if (slow) {
            if (rate < 30) {
                rate++;
                ticker.setRate(rate);
            }
        }

        if(fixation) {
            changeFixation();
        }

        if (!fixedOrientation) {
            Matrix rot = cam.getBack();
            int dx = mouseListener.getDeltaX();
            int dy = mouseListener.getDeltaY();

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

            if(mouseListener.buttonDown(0))
                objectSelection(buffer);
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
        if(fixedOrientation)
            mouseListener.show();
        else
            mouseListener.hide();
    }


    private void objectSelection(FrameBuffer buffer) {
        PhysObject3D                obj;
        SimpleVector                objCenBuf;
        SimpleVector                objBound;
        SimpleVector                shiftCenObj;
        Enumeration<PhysObject3D>   objs = gameWorld.getWorld().getObjects();
        float radius;
        while (objs.hasMoreElements()) {
            obj = objs.nextElement();
            float fov = convertRADAngleIntoFOV(obj.getTransformedCenter().calcSub(getPosition()).calcAngle(getDirection()));
            if(fov <= cam.getMaxFOV()) {
                objCenBuf = Interact2D.projectCenter3D2D(buffer, obj);
                if((objCenBuf.x > 0 && objCenBuf.x < buffer.getOutputWidth()) && (
                        objCenBuf.y > 0 && objCenBuf.y < buffer.getOutputHeight())) {
                    shiftCenObj = cam.getSideVector();
                    shiftCenObj.scalarMul(obj.getCharacteristicSize()[0]);
                    objBound = obj.getTransformedCenter().calcAdd(shiftCenObj);
                    radius = objCenBuf.calcSub(Interact2D.project3D2D(cam, buffer, objBound)).length();
                    if(objCenBuf.calcSub(new SimpleVector(mouseListener.getMouseX(),
                            mouseListener.getMouseY(), 0)).length() < radius)
                        gameWorld.setSelectObject(obj);
                }
            }
        }
    }

    private void changeFixation() {
        if(firstPress) {
            if(fixedOrientation) {
                fixedOrientation = false;
                mouseListener.hide();
            } else {
                fixedOrientation = true;
                mouseListener.show();
            }
            firstPress = false;
        }
    }

}

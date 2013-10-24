package ru.cfif11.cosmo.object;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Matrix;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;
import com.threed.jpct.util.KeyState;
import org.lwjgl.opengl.Display;
import ru.cfif11.cosmo.Ticker;
import ru.cfif11.cosmo.control.ControllableMKInterface;
import ru.cfif11.cosmo.control.KeyboardListener;
import ru.cfif11.cosmo.control.MouseListener;

import java.awt.event.KeyEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public class Camera implements ControllableMKInterface, MovableInterface{

    private boolean forward     = false;
    private boolean backward    = false;
    private boolean up          = false;
    private boolean down        = false;
    private boolean left        = false;
    private boolean right       = false;
    private boolean fast        = false;
    private boolean slow        = false;

    private KeyboardListener        keyListener;
    private MouseListener           mouseListener;
    private World                   world;
    private com.threed.jpct.Camera  cam;
    private Ticker                  ticker;

    private int rate = 15;




    public Camera(World world, Ticker ticker, FrameBuffer buffer) {
        this.world      = world;
        this.ticker     = ticker;
        this.cam        = world.getCamera();
        keyListener     = new KeyboardListener();
        mouseListener   = new MouseListener(buffer);
        mouseListener.hide();
    }

    public void setPosition(float x, float y, float z) {
        cam.setPosition(x,y,z);
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

    @Override
    public boolean pollControls() {
        KeyState ks = keyListener.pollControls();
        while (ks != KeyState.NONE) {
            if (ks.getKeyCode() == KeyEvent.VK_ESCAPE)
                return false;

            if (ks.getKeyCode() == KeyEvent.VK_UP)
                forward = ks.getState();

            if (ks.getKeyCode() == KeyEvent.VK_DOWN)
                backward = ks.getState();

            if (ks.getKeyCode() == KeyEvent.VK_LEFT)
                left = ks.getState();

            if (ks.getKeyCode() == KeyEvent.VK_RIGHT)
                right = ks.getState();

            if (ks.getKeyCode() == KeyEvent.VK_PAGE_UP)
                up = ks.getState();

            if (ks.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
                down = ks.getState();

            if (ks.getKeyCode() == KeyEvent.VK_W)
                fast = ks.getState();

            if (ks.getKeyCode() == KeyEvent.VK_Q)
                slow = ks.getState();

            ks = keyListener.pollControls();
        }

        if (Display.isCloseRequested())
            return false;

        return true;
    }

    //двигаем камеру в зависимости от того, куда нажали
    @Override
    public void move(long ticks) {

        if (ticks == 0) {
            return;
        }

        // Key control
        SimpleVector ellipsoid = new SimpleVector(5, 5, 5);

        if (forward) {
            world.checkCameraCollisionEllipsoid(com.threed.jpct.Camera.CAMERA_MOVEIN, ellipsoid, ticks, 5);
        }

        if (backward) {
            world.checkCameraCollisionEllipsoid(com.threed.jpct.Camera.CAMERA_MOVEOUT, ellipsoid, ticks, 5);
        }

        if (left) {
            world.checkCameraCollisionEllipsoid(com.threed.jpct.Camera.CAMERA_MOVELEFT, ellipsoid, ticks, 5);
        }

        if (right) {
            world.checkCameraCollisionEllipsoid(com.threed.jpct.Camera.CAMERA_MOVERIGHT, ellipsoid, ticks, 5);
        }

        if (up) {
            world.checkCameraCollisionEllipsoid(com.threed.jpct.Camera.CAMERA_MOVEUP, ellipsoid, ticks, 5);
        }

        if (down) {
            world.checkCameraCollisionEllipsoid(com.threed.jpct.Camera.CAMERA_MOVEDOWN, ellipsoid, ticks, 5);
        }

        if (fast) {
            if(rate > 2) {rate--; ticker.setRate(rate); }
        }

        if (slow) {
            if(rate < 30) {rate++; ticker.setRate(rate); }
        }

        // mouse rotation
        Matrix rot  = cam.getBack();
        int dx      = mouseListener.getDeltaX();
        int dy      = mouseListener.getDeltaY();

        float ts    = 0.2f * ticks;
        float tsy   = ts;

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

        if (dy!=0) {
            rot.rotateX(tsy);
        }

    }

}

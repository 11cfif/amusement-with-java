package ru.cfif11.cosmo.Control;

import com.threed.jpct.*;
import com.threed.jpct.util.KeyMapper;
import com.threed.jpct.util.KeyState;
import org.lwjgl.input.Mouse;
import ru.cfif11.cosmo.Ticker;

import java.awt.event.KeyEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public class CameraControl {

    private boolean forward     = false;
    private boolean backward    = false;
    private boolean up          = false;
    private boolean down        = false;
    private boolean left        = false;
    private boolean right       = false;
    private boolean fast        = false;
    private boolean slow        = false;

    private KeyMapper keyMapper;
    private KeyboardControl keyControl= null;
    private MouseMapper mouseMapper = null;
    private World world;
    private int rate        = 15;
    private Camera cam;
    private Ticker ticker;

    public CameraControl(World world, Ticker ticker, FrameBuffer buffer, KeyboardControl keyControl) {
        this.world = world;
        this.ticker = ticker;
        this.cam = world.getCamera();
        keyMapper   = new KeyMapper();
        mouseMapper = new MouseMapper(buffer);
        mouseMapper.hide();
        this.keyControl = keyControl;
    }

    public boolean pollControls() {
        KeyState ks = keyControl.pollControls();
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

            ks = keyControl.pollControls();
        }

        if (org.lwjgl.opengl.Display.isCloseRequested())
            return false;

        return true;
    }

    //двигаем камеру в зависимости от того, куда нажали
    public void move(long ticks) {

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
        Matrix rot  = cam.getBack();
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
            //rot.rotateAxis(rot.getXAxis(), ts);
            rot.rotateY(-ts);
        }

        if (dy!=0) {
            rot.rotateX(tsy);
        }

    }


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
}

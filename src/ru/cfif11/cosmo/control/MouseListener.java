package ru.cfif11.cosmo.control;

import com.threed.jpct.FrameBuffer;
import org.lwjgl.input.Mouse;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public class MouseListener {

    private boolean hidden  = false;
    private int height      = 0;
    private int width       = 0;
    private int lastMouseX;
    private int lastMouseY;

    public MouseListener(FrameBuffer buffer) {

        height = buffer.getOutputHeight();
        width  = buffer.getOutputWidth();
        init();
    }

    public void hide() {
        if (!hidden) {
            Mouse.setGrabbed(true);
            hidden = true;
            setLastCoordMouse();
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

    public boolean isButtonDown(int button) {
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

    public boolean isInsideWindow() {
        boolean result = Mouse.isInsideWindow();
        if(result)
            setLastCoordMouse();
        return  result;
    }

    public void setCursorPosition() {
        Mouse.setCursorPosition(lastMouseX, lastMouseY);
    }

    private void init() {
        try {
            Mouse.setCursorPosition((int)(width/2.0), ((int)(height/2.0) ));
            setLastCoordMouse();
            if (!Mouse.isCreated()) {
                Mouse.create();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setLastCoordMouse() {
        lastMouseX = Mouse.getX();
        lastMouseY = Mouse.getY();
    }

}

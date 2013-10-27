package ru.cfif11.cosmo.scene;

import com.threed.jpct.FrameBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public abstract class GraphicForm {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected double scaleFactor;

    GraphicForm(int x, int y, int width, int height, double scaleFactor) {
        this.x              = x;
        this.y              = y;
        this.width          = width;
        this.height         = height;
        this.scaleFactor    = scaleFactor;
    }

    public abstract void createForm();

    public abstract void blit(FrameBuffer buffer);

}

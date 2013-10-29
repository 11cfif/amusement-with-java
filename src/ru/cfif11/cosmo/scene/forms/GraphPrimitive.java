package ru.cfif11.cosmo.scene.forms;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Texture;
import ru.cfif11.cosmo.Main;
import ru.cfif11.cosmo.object.Camera;
import ru.cfif11.cosmo.object.physobject.PhysObject3D;
import ru.cfif11.cosmo.scene.GraphicForm;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public abstract class GraphPrimitive {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Texture texture;

    protected GraphPrimitive(int x, int y, int width, int height, String texture) {
        this.x          = x;
        this.y          = y;
        this.width      = width;
        this.height     = height;
        this.texture    = Main.texMan.getTexture(texture);
    }

    public abstract void blit(FrameBuffer buffer);


    protected abstract void calcCoordinates(GraphicForm gForm, Camera camera, PhysObject3D obj);
}

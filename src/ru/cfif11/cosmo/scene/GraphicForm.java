package ru.cfif11.cosmo.scene;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Texture;
import ru.cfif11.cosmo.Main;
import ru.cfif11.cosmo.object.Camera;
import ru.cfif11.cosmo.scene.forms.GraphPrimitive;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public abstract class GraphicForm {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Texture texture;
    protected HashMap<String, GraphPrimitive> primitives;

    protected GraphicForm(int x, int y, int width, int height, String texture) {
        this.x          = x;
        this.y          = y;
        this.width      = width;
        this.height     = height;
        this.texture    = Main.texMan.getTexture(texture);
        primitives = new HashMap<String, GraphPrimitive>();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public abstract void refresh(Camera camera);

    public abstract void draw(FrameBuffer buffer);

    protected abstract void createForm();



}

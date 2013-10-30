package ru.cfif11.cosmo.scene;

import com.threed.jpct.FrameBuffer;
import ru.cfif11.cosmo.object.Camera;
import ru.cfif11.cosmo.scene.forms.GraphAbstract;
import ru.cfif11.cosmo.scene.forms.GraphPrimitive;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public abstract class GraphicForm extends GraphAbstract{


    protected HashMap<String, GraphPrimitive> primitives;

    protected GraphicForm(int x, int y, int width, int height, String texture) {
        super(x, y, width, height);
        setTexture(texture);
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

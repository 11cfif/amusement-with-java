package ru.cfif11.cosmo.scene.forms;

import com.threed.jpct.FrameBuffer;
import ru.cfif11.cosmo.object.Camera;
import ru.cfif11.cosmo.object.physobject.PhysObject3D;
import ru.cfif11.cosmo.scene.GraphicForm;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public abstract class GraphPrimitive extends GraphAbstract{

    private String name;

    GraphPrimitive(int x, int y, int width, int height, int widthDest, int heightDest, String name) {
        super(x, y, width, height, widthDest, heightDest);
        this.name = name;
    }

    public String getName() {
        return  name;
    }

    protected void setName(String newName) {
        name = newName;
    }

    public abstract void blit(FrameBuffer buffer);

    protected abstract void calcCoordinates(GraphicForm gForm, Camera camera, PhysObject3D obj);

}

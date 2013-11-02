package ru.cfif11.cosmo.scene;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.World;
import ru.cfif11.cosmo.Ticker;
import ru.cfif11.cosmo.object.Camera;
import ru.cfif11.cosmo.object.physobject.PhysObject3D;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public abstract class GameWorld {


    protected World world;
    protected Ticker ticker;
    protected ManagerGraphicForm manGraphForm;
    protected PhysObject3D selectObject = null;


    protected GameWorld(Ticker ticker) {
        world = new World();
        this.ticker = ticker;
    }

    public abstract void tunePositionCamera(Camera camera);

    public abstract boolean run(Camera camera, FrameBuffer buffer);

    public World getWorld() {
        return world;
    }

    public void renderScene(FrameBuffer buffer) {
        world.renderScene(buffer);
    }

    public void draw(FrameBuffer buffer) {
        world.draw(buffer);
    }

    public PhysObject3D getSelectObject() {
        return selectObject;
    }

    public void setSelectObject(PhysObject3D selectObject) {
        this.selectObject = selectObject;
    }

    protected abstract void initializationLevel();

    protected abstract void initializationManagerGraphForm();

    public abstract void drawGraphForm(FrameBuffer buffer, Camera camera);
}

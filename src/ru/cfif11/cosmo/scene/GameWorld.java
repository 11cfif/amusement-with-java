package ru.cfif11.cosmo.scene;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.World;
import org.lwjgl.opengl.Display;
import ru.cfif11.cosmo.Main;
import ru.cfif11.cosmo.Ticker;
import ru.cfif11.cosmo.control1.ControllableMKInterface;
import ru.cfif11.cosmo.object.Camera;
import ru.cfif11.cosmo.object.physobject.PhysObject3D;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public abstract class GameWorld implements ControllableMKInterface{


    protected World                 world;
    protected Ticker                ticker;
    protected ManagerGraphicForm    manGraphForm;
    protected PhysObject3D selectObject = null;

    public static final String[] KEYS = new String[] {"Minus", "Equals", "NumPad -", "NumPad +", "Escape"};
    private boolean[] keyStates = new boolean[KEYS.length];


    protected GameWorld(Ticker ticker) {
        world = new World();
        this.ticker = ticker;
    }

    @Override
    public boolean pollControls(){
        Main.KEYBOARD_LISTENER.recordPollСontrols(KEYS, keyStates);
        if(keyStates[keyStates.length-1] == true)
            return false;

        return !Display.isCloseRequested();
    }

    @Override
    public void applyControl(long ticks, FrameBuffer buffer) {
        if(keyStates == null || ticks == 0) {
            return;
        }

        if (keyStates[1] || keyStates[3]) {
            if (Main.rate > 2) {
                Main.rate--;
                ticker.setRate(Main.rate);
            }
        }

        if (keyStates[0] || keyStates[2]) {
            if (Main.rate < 30) {
                Main.rate++;
                ticker.setRate(Main.rate);
            }
        }

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

package ru.cfif11.cosmo.scene;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.World;
import ru.cfif11.cosmo.Ticker;
import ru.cfif11.cosmo.object.Camera;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public abstract class GameWorld {


    protected World world;
    protected Ticker ticker;


    public GameWorld(Ticker ticker) {
        world = new World();
        this.ticker = ticker;
    }

    public abstract void tunePositionCamera(Camera camera);

    public abstract boolean run(Camera camera);

    public World getWorld() {
        return world;
    }

    public void renderScene(FrameBuffer buffer) {
        world.renderScene(buffer);
    }

    public void draw(FrameBuffer buffer) {
        world.draw(buffer);
    }

    protected abstract void initializationLevel();

}

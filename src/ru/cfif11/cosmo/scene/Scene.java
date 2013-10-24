package ru.cfif11.cosmo.scene;

import com.threed.jpct.*;
import ru.cfif11.cosmo.Ticker;
import ru.cfif11.cosmo.object.Camera;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public class Scene implements IPaintListener {

    private GameWorld           gameWorld;
    private Cursor              cur;
    private ArrayList<Table>    listTable;
    private FrameBuffer         buffer;
    private Camera              camera;

    public Scene(TextureManager tm, Ticker ticker, GameWorld gameWorld) {
        buffer = new FrameBuffer(800, 600, FrameBuffer.SAMPLINGMODE_NORMAL);
        buffer.disableRenderer(IRenderer.RENDERER_SOFTWARE);
        buffer.enableRenderer(IRenderer.RENDERER_OPENGL);
        buffer.setPaintListener(this);


        this.gameWorld  = gameWorld;
        camera          = new Camera(gameWorld.getWorld(), ticker, buffer);
        cur             = new Cursor(tm);
        gameWorld.tunePositionCamera(camera);
    }

    @Override
    public void startPainting() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void finishedPainting() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void bufferReset(boolean state) {
        buffer.clear();
        buffer.setPaintListenerState(state);
        gameWorld.renderScene(buffer);
        gameWorld.draw(buffer);
        buffer.update();
        buffer.displayGLOnly();
    }

    public void close() {
        buffer.disableRenderer(IRenderer.RENDERER_OPENGL);
        buffer.dispose();
    }

    public boolean run() {
        return gameWorld.run(camera);
    }

}

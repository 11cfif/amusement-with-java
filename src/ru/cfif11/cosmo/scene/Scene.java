package ru.cfif11.cosmo.scene;

import com.threed.jpct.*;
import ru.cfif11.cosmo.Ticker;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public class Scene implements IPaintListener {

    private GameWorld gameWorld;
    private Cursor cur;
    private ArrayList<Table> listTable;
    private FrameBuffer buffer;

    public Scene(TextureManager tm, Ticker ticker, int level) {
        buffer = new FrameBuffer(800, 600, FrameBuffer.SAMPLINGMODE_NORMAL);
        buffer.disableRenderer(IRenderer.RENDERER_SOFTWARE);
        buffer.enableRenderer(IRenderer.RENDERER_OPENGL);
        buffer.setPaintListener(this);


        gameWorld = new GameWorld(this, tm, buffer, ticker, level);
        cur = new Cursor(tm);
        switch (level) {

        }
    }

    @Override
    public void startPainting() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void finishedPainting() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public GameWorld getGameWorld() {
        return gameWorld;
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
}

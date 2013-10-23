package ru.cfif11.cosmo.scene;

import com.threed.jpct.*;

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

    public Scene(World world, TextureManager tm, int local) {
        buffer = new FrameBuffer(800, 600, FrameBuffer.SAMPLINGMODE_NORMAL);
        buffer.disableRenderer(IRenderer.RENDERER_SOFTWARE);
        buffer.enableRenderer(IRenderer.RENDERER_OPENGL);
        buffer.setPaintListener(this);


        gameWorld = new GameWorld(world, tm);
        cur = new Cursor(tm);
        switch (local) {

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
}

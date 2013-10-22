package ru.cfif11.cosmo.scene;

import com.threed.jpct.*;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Администратор
 * Date: 22.10.13
 * Time: 19:33
 * To change this template use File | Settings | File Templates.
 */
public class Scene implements IPaintListener {

    private GameWorld gameWorld = null;
    private Cursor cur = null;
    private ArrayList<Table> listTable = null;
    private FrameBuffer buffer = null;

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

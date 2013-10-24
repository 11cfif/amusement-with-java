package ru.cfif11.cosmo.scene;

import com.threed.jpct.*;
import ru.cfif11.cosmo.Ticker;
import ru.cfif11.cosmo.adapterphysics.AdapterPhysics;
import ru.cfif11.cosmo.object.Camera;
import ru.cfif11.cosmo.object.physobject.MassAttractObject3D;
import ru.cfif11.cosmo.object.physobject.StarSystemEnum;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public class GameWorld {

    private Camera camera;
    private World world;
    private ArrayList<MassAttractObject3D> starSystem;
    private float                          scalingFactor   = 1e-4f;
    private Ticker ticker;
    private AdapterPhysics adapter;
    private Scene scene;

    public GameWorld(Scene scene, TextureManager tm, FrameBuffer buffer, Ticker ticker, int level) {
        world = new World();
        this.scene = scene;
        world.setAmbientLight(100, 100, 100);
        world.getLights().setRGBScale(Lights.RGB_SCALE_2X);
        camera = new Camera(world, ticker, buffer);
        this.ticker = ticker;

        adapter = new AdapterPhysics(world);
        initializationLevel(level);

        //создаем камеру, перемещаем в заданную точку и направляем ее взор на центр Солнца
        camera.setPosition(1000, 0, 0);
        camera.lookAt(new SimpleVector());
        camera.setFOV(1.5f);

    }

    private void initializationLevel(int level) {
        switch (level) {
            case 1:
                initializationStarSystem();
        }
    }

    private void initializationStarSystem() {
        MassAttractObject3D tempObj;
        SimpleVector tempVec;
        starSystem = new ArrayList<MassAttractObject3D>();
        int i = 0;
        for (StarSystemEnum p : StarSystemEnum.values()) {
            tempVec  = p.getVelocity();
            tempVec.scalarMul(scalingFactor);
            tempObj = new MassAttractObject3D(Primitives.getSphere(100, p.getRadius() * scalingFactor), p.getNameObject()+i,
                    tempVec, p.getMass());
            tempObj.setTexture(tempObj.getName().substring(0,tempObj.getName().length() -1));
            tempObj.setEnvmapped(Object3D.ENVMAP_ENABLED);
            tempVec  = p.getInitialPosition();
            tempVec.scalarMul(scalingFactor);
            tempObj.translate(tempVec);
            starSystem.add(tempObj);
            world.addObject(tempObj);
            tempObj.build();
            tempObj.compileAndStrip();
            i++;
        }
    }

    public World getWorld() {
        return world;
    }

    public void renderScene(FrameBuffer buffer) {
        world.renderScene(buffer);
    }

    public void draw(FrameBuffer buffer) {
        world.draw(buffer);
    }

    public boolean run(){
        boolean doLoop;
        doLoop = true;
        adapter = new AdapterPhysics(world);
        long ticks = ticker.getTicks();
        if (ticks > 0) {
            //рассчитываем все силы и считаем новые местоположения объектов
            adapter.calcForce();
            for(MassAttractObject3D e : starSystem) {
                e.calcLocation(0.1f);
            }
            //используем обработчик событий для движения камеры
            doLoop = camera.pollControls();
            camera.move(ticks);
        }
        //очищаем буфер и рисуем заново
        scene.bufferReset(false);

        //не используется, а вообще для подсчета fps
        /*if (System.currentTimeMillis() - time >= 1000) {
            fps = 0;
            time = System.currentTimeMillis();
        } */
        return doLoop;

    }
}

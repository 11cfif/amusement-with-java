package ru.cfif11.cosmo.locations;

import com.threed.jpct.*;
import ru.cfif11.cosmo.Ticker;
import ru.cfif11.cosmo.adapterphysics.AdapterPhysics;
import ru.cfif11.cosmo.object.Camera;
import ru.cfif11.cosmo.object.physobject.MassAttractObject3D;
import ru.cfif11.cosmo.scene.*;
import ru.cfif11.cosmo.scene.forms.Radar;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public class SolarSystemLoc extends GameWorld {

    private ArrayList<MassAttractObject3D>  starSystem;
    private float                           scalingFactor = 1e-4f;
    private AdapterPhysics                  adapter;



    public SolarSystemLoc(Ticker ticker) {
        super(ticker);
        world.setAmbientLight(100, 100, 100);
        world.getLights().setRGBScale(Lights.RGB_SCALE_2X);
        adapter = new AdapterPhysics(world);
        initializationLevel();
    }

    @Override
    protected void initializationLevel() {
        MassAttractObject3D tempObj;
        SimpleVector        tempVec;
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
        initializationManagerGraphForm();
    }

    @Override
    protected void initializationManagerGraphForm() {
        ArrayList<GraphicForm> graphicForms = new ArrayList<GraphicForm>();
        graphicForms.add(new Radar(256,0,256,205, "Radar", world));
        manGraphForm = new ManagerGraphicForm(graphicForms);
    }

    @Override
    public void drawGraphForm(FrameBuffer buffer, Camera camera) {
        manGraphForm.refresh(camera);
        manGraphForm.drawGraphForm(buffer);
    }

    @Override
    public void tunePositionCamera(Camera camera){
        camera.setPosition(1000, 0, 0);
        camera.lookAt(new SimpleVector());
        camera.setFOV(1.5f);
    }

    @Override
    public boolean run(Camera camera){
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


        //не используется, а вообще для подсчета fps
        /*if (System.currentTimeMillis() - time >= 1000) {
            fps = 0;
            time = System.currentTimeMillis();
        } */
        return doLoop;

    }

}

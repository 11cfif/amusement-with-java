package ru.cfif11.cosmo.locations;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Lights;
import com.threed.jpct.SimpleVector;
import ru.cfif11.cosmo.Ticker;
import ru.cfif11.cosmo.adapterphysics.AdapterPhysics;
import ru.cfif11.cosmo.object.Camera;
import ru.cfif11.cosmo.object.physobject.MassAttractObject3D;
import ru.cfif11.cosmo.scene.GameWorld;
import ru.cfif11.cosmo.scene.GraphicForm;
import ru.cfif11.cosmo.scene.ManagerGraphicForm;
import ru.cfif11.cosmo.scene.forms.Radar;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public abstract class StarSystem extends GameWorld{

    protected ArrayList<MassAttractObject3D>    system;
    protected float                             scalingFactor = 1e-4f;
    private AdapterPhysics                      adapter;

    public StarSystem(Ticker ticker) {
        super(ticker);
        world.setAmbientLight(100, 100, 100);
        world.getLights().setRGBScale(Lights.RGB_SCALE_2X);
        adapter = new AdapterPhysics(world);
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
        doLoop      = true;
        adapter     = new AdapterPhysics(world);
        long ticks  = ticker.getTicks();
        if (ticks > 0) {
            //рассчитываем все силы и считаем новые местоположения объектов
            adapter.calcForce();
            for(MassAttractObject3D e : system)
                e.calcLocation(0.1f);
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

    public ArrayList<MassAttractObject3D> getSystem() {
        return system;
    }

    @Override
    protected void initializationManagerGraphForm() {
        ArrayList<GraphicForm> graphicForms = new ArrayList<GraphicForm>();
        graphicForms.add(new Radar(256,0,256,205, "Radar", this));
        manGraphForm = new ManagerGraphicForm(graphicForms);
    }


}

package ru.cfif11.cosmo.locations;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Lights;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.util.KeyState;
import ru.cfif11.cosmo.Main;
import ru.cfif11.cosmo.Ticker;
import ru.cfif11.cosmo.adapterphysics.AdapterPhysics;
import ru.cfif11.cosmo.object.Camera;
import ru.cfif11.cosmo.object.physobject.MassAttractObject3D;
import ru.cfif11.cosmo.object.physobject.MassObject3D;
import ru.cfif11.cosmo.object.physobject.Ship;
import ru.cfif11.cosmo.scene.GameWorld;
import ru.cfif11.cosmo.scene.GraphicForm;
import ru.cfif11.cosmo.scene.ManagerGraphicForm;
import ru.cfif11.cosmo.scene.forms.InformationForm;
import ru.cfif11.cosmo.scene.forms.Radar;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public abstract class StarSystem extends GameWorld{

    ArrayList<MassAttractObject3D>  system;
    float                           scalingFactor = 1e-4f;
    private AdapterPhysics          adapter;
    ArrayList<Ship>                 ships;

    StarSystem(Ticker ticker) {
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
        camera.lookAt(new SimpleVector(2000,0,0));
        camera.setFOV(1.5f);
    }

    @Override
    public boolean run(Camera camera, FrameBuffer buffer){
        boolean doLoop;
        doLoop      = true;
        adapter     = new AdapterPhysics(world);
        long ticks  = ticker.getTicks();
        if (ticks > 0) {
            //рассчитываем все силы и считаем новые местоположения объектов
            adapter.calcForce();
            for(MassObject3D e : system)
                e.calcLocation(0.1f);

            for (MassObject3D e : ships)
                e.calcLocation(0.1f);

            Main.KEYBOARD_LISTENER.setMainState();
            while(Main.KEYBOARD_LISTENER.getMainState() != KeyState.NONE) {
                Main.KEYBOARD_LISTENER.pollControls();

            }
            if((doLoop = pollControls())) {
                applyControl(ticks, buffer);
                camera.pollControls();
                camera.applyControl(ticks, buffer);
            }
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

    public void addShip(Ship ship) {
        ships.add(ship);
    }

    @Override
    protected void initializationManagerGraphForm() {
        ArrayList<GraphicForm> graphicForms = new ArrayList<GraphicForm>();
        graphicForms.add(new InformationForm(900, 568, 1500, 600, 800, 200,  "Information", this));
        graphicForms.add(new Radar(900-800+256, 568, 500, 400, 256,200, "Radar", this));
        manGraphForm = new ManagerGraphicForm(graphicForms);
    }


}

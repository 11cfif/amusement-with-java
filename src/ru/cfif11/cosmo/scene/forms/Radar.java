package ru.cfif11.cosmo.scene.forms;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.World;
import ru.cfif11.cosmo.object.Camera;
import ru.cfif11.cosmo.object.physobject.PhysObject3D;
import ru.cfif11.cosmo.scene.GraphicForm;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public class Radar extends GraphicForm {

    private String[] names = {"Star", "Planet", "Sputnik"};
    private World world;
    public static float fovX = 0.8f;
    public static float fovY = 1f;

    public Radar(int x, int y, int width, int height, String texture, World world) {
        super(x, y, width, height, texture);
        this.world = world;
        createForm();
    }

    @Override
    public void createForm() {
        RadarGraphPrimitive prim = null;
        for(int i = 0; i<names.length; i++) {
            prim = new RadarGraphPrimitive(0,0,4,4,names[i]);
            primitives.put(names[i], prim);
        }
    }

    @Override
    public void draw(FrameBuffer buffer) {
        buffer.blit(texture, 0, 0, buffer.getOutputWidth() - x, y, width, height, FrameBuffer.OPAQUE_BLITTING);
        for(int i = 0; i<names.length; i++) {
            primitives.get(names[i]).blit(buffer);
        }
    }


    @Override
    public void refresh(Camera camera) {
        calcPrimitives(camera);
    }

    public void setFovX(float fovX){
        this.fovX = fovX;
    }

    public void setFovY(float fovY){
        this.fovY = fovY;
    }

    private void calcPrimitives(Camera camera) {
        PhysObject3D[] obj = {(PhysObject3D)world.getObjectByName("Sun0"), (PhysObject3D)world.getObjectByName("Earth1"),
                (PhysObject3D)world.getObjectByName("Moon2")};
        for(int i = 0; i<names.length; i++) {
            primitives.get(names[i]).calcCoordinates(this, camera, obj[i]);
        }
    }

}

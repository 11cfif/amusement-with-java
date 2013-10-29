package ru.cfif11.cosmo.scene.forms;

import com.threed.jpct.FrameBuffer;
import ru.cfif11.cosmo.locations.StarSystem;
import ru.cfif11.cosmo.object.Camera;
import ru.cfif11.cosmo.object.physobject.MassAttractObject3D;
import ru.cfif11.cosmo.scene.GraphicForm;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public class Radar extends GraphicForm {

    private StarSystem world;
    private String nameGameWorld;
    public static float fovX = 0.8f;
    public static float fovY = 1f;

    public Radar(int x, int y, int width, int height, String texture, StarSystem world) {
        super(x, y, width, height, texture);
        this.world = world;
        setNameGameWorld(world.toString());
        createForm();
    }

    @Override
    public void createForm() {
        RadarGraphPrimitive prim;
        for (MassAttractObject3D obj : world.getSystem()) {
            prim = new RadarGraphPrimitive(0, 0, 4, 4, getNameTypeObj(obj.getName()));
            primitives.put(prim.getName(), prim);
        }
    }

    @Override
    public void draw(FrameBuffer buffer) {
        buffer.blit(texture, 0, 0, buffer.getOutputWidth() - x, y, width, height, FrameBuffer.OPAQUE_BLITTING);
        for (MassAttractObject3D obj : world.getSystem())
            primitives.get(getNameTypeObj(obj.getName())).blit(buffer);
    }


    @Override
    public void refresh(Camera camera) {
        calcPrimitives(camera);
    }

    public void setFovX(float fovX) {
        this.fovX = fovX;
    }

    public void setFovY(float fovY) {
        this.fovY = fovY;
    }

    private void calcPrimitives(Camera camera) {
        for (MassAttractObject3D obj : world.getSystem())
            primitives.get(getNameTypeObj(obj.getName())).calcCoordinates(this, camera, obj);
    }

    private String getNameTypeObj(String objName) {
        return objName.substring(nameGameWorld.length());
    }

    private void setNameGameWorld(String name) {
        nameGameWorld = name.substring(name.lastIndexOf(".") + 1, name.indexOf("Loc"));
    }

}

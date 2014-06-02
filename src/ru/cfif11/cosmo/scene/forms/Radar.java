package ru.cfif11.cosmo.scene.forms;

import java.awt.*;

import com.threed.jpct.FrameBuffer;
import ru.cfif11.cosmo.object.Camera;
import ru.cfif11.cosmo.scene.GraphicForm;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public class Radar extends GraphicForm {

	// private StarSystem world;
	private String nameGameWorld;
	private static float fovX = 0.98f;
	private static float fovY = 1.25f;
	private static float MaxDestToRad = 5000;
	private static float MinDestToRad = 100;

	public Radar(int x, int y, int width, int height, int widthDest, int heightDest, String texture/*, StarSystem world*/) {
		super(x, y, width, height, widthDest, heightDest, texture);
		//    this.world = world;
		//  setNameGameWorld(world.toString());
		createForm();
	}

	@Override
	protected void createForm() {
		RadarGraphPrimitive prim;
		prim = new RadarGraphPrimitive(x - widthDest, y, 8, 8, 0, 0, "Select");
		primitives.put(prim.getName(), prim);
 /*       for (MassAttractObject3D obj : world.getSystem()) {
            prim = new RadarGraphPrimitive(x-widthDest, y, 8, 8, 4, 4, getNameTypeObj(obj.getName()));
            primitives.put(prim.getName(), prim);
        }
*/
	}

	@Override
	public void draw(FrameBuffer buffer) {
		buffer.blit(texture, 0, 0, buffer.getOutputWidth() - x, y, width, width, widthDest, heightDest, 0, false, new Color(165, 100, 50));
		primitives.get("Select").blit(buffer);
/*		for (MassAttractObject3D obj : world.getSystem())
			primitives.get(getNameTypeObj(obj.getName())).blit(buffer);
*/
	}


	@Override
	public void refresh(Camera camera) {
		calcPrimitives(camera);
	}

	public void setFovX(float fovX) {
		Radar.fovX = fovX;
	}

	public void setFovY(float fovY) {
		Radar.fovY = fovY;
	}

	public static float getFovX() {
		return fovX;
	}

	public static float getFovY() {
		return fovY;
	}

	public static float getMaxDestToRad() {
		return MaxDestToRad;
	}

	public static float getMinDestToRad() {
		return MinDestToRad;
	}

	public static void setMaxDestToRad(float maxDestToRad) {
		MaxDestToRad = maxDestToRad;
	}

	public static void setMinDestToRad(float minDestToRad) {
		MinDestToRad = minDestToRad;
	}

	private void calcPrimitives(Camera camera) {
/*		for (MassAttractObject3D obj : world.getSystem()) {
			primitives.get(getNameTypeObj(obj.getName())).calcCoordinates(this, camera, obj);
			if (obj.equals(world.getSelectObject()))
				primitives.get("Select").calcCoordinates(this, camera, obj);
		}
*/
	}

	private String getNameTypeObj(String objName) {
		return objName.substring(nameGameWorld.length());
	}

	private void setNameGameWorld(String name) {
		nameGameWorld = name.substring(name.lastIndexOf(".") + 1, name.indexOf("Loc"));
	}

}

package ru.cfif11.cosmo.scene.forms;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.SimpleVector;
import ru.cfif11.cosmo.object.Camera;
import ru.cfif11.cosmo.object.physobject.PhysObject3D;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public class RadarGraphPrimitive extends GraphPrimitive {

	private static final String subName = "Rad";
	private static final int maxSize = 10;
	private static final int minSize = 1;

	public RadarGraphPrimitive(int x, int y, int width, int height, int widthDest, int heightDest, String name) {
		super(x, y, width, height, widthDest, heightDest, name);
		if (name.contains("_"))
			setTexture(name.substring(0, name.indexOf("_")) + subName);
		else
			setTexture(name + subName);
	}

	@Override
	public void blit(FrameBuffer buffer) {
		buffer.blit(texture, 0, 0, buffer.getOutputWidth() - x, y, width, height, widthDest, heightDest, 0, false);
	}

	@Override
	protected void calcCoordinates(GraphicForm gForm, Camera camera, PhysObject3D obj) {
		SimpleVector camToObj = obj.getTransformedCenter().calcSub(camera.getPosition());
		SimpleVector radToObj = camToObj.reflect(camera.getDirection()).calcAdd(camToObj);
		float angle = camToObj.calcAngle(camera.getDirection());
		radToObj.scalarMul(0.5f);
		float angleRadX = radToObj.calcAngle(camera.getSideVector());
		float angleRadY = radToObj.calcAngle(camera.getUpVector());
		float fovX = (float)Math.cos(angleRadX) * camera.convertRADAngleIntoFOV(angle);
		float fovY = (float)Math.cos(angleRadY) * camera.convertRADAngleIntoFOV(angle);
		if (Math.abs(fovY) < Radar.getFovY() && Math.abs(fovX) < Radar.getFovX()) {
			x = (int)((gForm.widthDest / 2.0) * (1 - fovX / (double)Radar.getFovX())) + gForm.x - gForm.widthDest;
			y = (int)((gForm.heightDest / 2.0) * (1 - fovY / (double)Radar.getFovY())) + gForm.y;
			if (getName().equals("Select")) {
				x += 1;
				y -= 1;
			}
			getSizeTexture(camToObj);
		} else {
			x = 0;
			y = 0;
		}

	}

	private void getSizeTexture(SimpleVector camToObj) {
		if (camToObj.length() < Radar.getMinDestToRad()) {
			System.out.println();
			widthDest = maxSize;
		} else if (camToObj.length() > Radar.getMaxDestToRad())
			widthDest = minSize;
		else
			widthDest = (int)(maxSize - camToObj.length() / ((Radar.getMaxDestToRad() - Radar.getMinDestToRad()) / (double)(maxSize - minSize)));
		if (getName().equals("Select"))
			widthDest += 2;
		heightDest = widthDest;
	}
}

package ru.cfif11.cosmo.scene.forms;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.SimpleVector;
import ru.cfif11.cosmo.object.Camera;
import ru.cfif11.cosmo.object.physobject.PhysObject3D;
import ru.cfif11.cosmo.scene.GraphicForm;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public class RadarGraphPrimitive extends GraphPrimitive {

    private static final String subName = "Rad";

    public RadarGraphPrimitive(int x, int y, int width, int height, String texture) {
        super(x, y, width, height, (texture + subName));
    }

    @Override
    protected void calcCoordinates(GraphicForm gForm, Camera camera, PhysObject3D obj) {
        SimpleVector camToObj = obj.getTransformedCenter().calcSub(camera.getPosition());
        float angle = camToObj.calcAngle(camera.getDirection());
        SimpleVector radToObj = camToObj.reflect(camera.getDirection()).calcAdd(camToObj);
        radToObj.scalarMul(0.5f);
        float angleRadX = radToObj.calcAngle(camera.getSideVector());
        float angleRadY = radToObj.calcAngle(camera.getUpVector());
        float fovX = (float) Math.cos(angleRadX) * camera.convertRADAngleIntoFOV(angle);
        float fovY = (float) Math.cos(angleRadY) * camera.convertRADAngleIntoFOV(angle);
        if (Math.abs(fovY) < Radar.fovY && Math.abs(fovX) < Radar.fovX) {
            y = (int) ((gForm.getHeight() / 2) * (1 - fovY / Radar.fovY));
            x = (int) ((gForm.getWidth() / 2) * (1 - fovX / Radar.fovX));
        } else {
            x = 0;
            y = 0;
        }

    }

    @Override
    public void blit(FrameBuffer buffer) {
        buffer.blit(texture, 0, 0, buffer.getOutputWidth() - x, y, width, height, FrameBuffer.OPAQUE_BLITTING);
    }
}

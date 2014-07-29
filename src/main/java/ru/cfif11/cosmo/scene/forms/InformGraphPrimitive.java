package ru.cfif11.cosmo.scene.forms;

import com.threed.jpct.FrameBuffer;
import ru.cfif11.cosmo.object.Camera;
import ru.cfif11.cosmo.object.physobject.PhysObject3D;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public class InformGraphPrimitive extends GraphPrimitive{
    static final String subName = "Inf";

    InformGraphPrimitive(int x, int y, int width, int height, int widthDest, int heightDest, String name) {
        super(x, y, width, height, widthDest, heightDest, name);
        if(name.contains("_"))
            setTexture(name.substring(0,name.indexOf("_")) + subName);
        else
            setTexture(name + subName);
    }

    @Override
    public void blit(FrameBuffer buffer) {
        buffer.blit(texture, 0, 0, buffer.getOutputWidth() - x, y, width, height, widthDest, heightDest, 0, false, new Color(200, 190, 150));
    }

    @Override
    protected void calcCoordinates(GraphicForm gForm, Camera camera, PhysObject3D obj) {
        if(camera.isFixedOrientation() && !getName().equals("Looker"))
            setName("Looker");
        else if(!camera.isFixedOrientation() && !getName().equals("Camera"))
            setName("Camera");
        setTexture(getName() + subName);
    }
}

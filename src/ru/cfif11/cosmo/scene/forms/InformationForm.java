package ru.cfif11.cosmo.scene.forms;

import com.threed.jpct.FrameBuffer;
import ru.cfif11.cosmo.object.Camera;
import ru.cfif11.cosmo.scene.GameWorld;
import ru.cfif11.cosmo.scene.GraphicForm;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public class InformationForm extends GraphicForm {

	private String selectObjectName;
	private String nameGameWorld;
	//  private StarSystem world;
	private final String[] namePrimitives = new String[] {"selectObj", "mode"};

	public InformationForm(int x, int y, int width, int height, int widthDest, int heightDest, String texture, GameWorld world) {
		super(x, y, width, height, widthDest, heightDest, texture);
	 /*   setNameGameWorld(world.toString());
        this.world = world;*/
		createForm();
	}

	@Override
	public void refresh(Camera camera) {
		if (!VerifySelectedObject())
			setSelectObject();
		primitives.get(namePrimitives[1]).calcCoordinates(this, camera, null);
	}

	@Override
	public void draw(FrameBuffer buffer) {
		buffer.blit(texture, 0, 0, buffer.getOutputWidth() - x, y, width, height, widthDest, heightDest, 0, false);
		for (String str : namePrimitives)
			primitives.get(str).blit(buffer);
	}

	@Override
	protected void createForm() {
		setSelectObject();
		primitives.put(namePrimitives[1], new InformGraphPrimitive(x - 430, y + 70, 500, 500, 120, 60, "Looker"));
	}

	private void setNameGameWorld(String name) {
		nameGameWorld = name.substring(name.lastIndexOf(".") + 1, name.indexOf("Loc"));
	}

	private void setSelectObject() {
		InformGraphPrimitive prim;
      /*  if( world.getSelectObject() == null) {
            prim = new InformGraphPrimitive(x-220, y + 50, 500, 500, 100, 100, "Null");
            selectObjectName = "null";
        } else {
            prim = new InformGraphPrimitive(x-220, y + 50, 500, 500, 100, 100, getNameTypeObj(world.getSelectObject().getName()));
            selectObjectName = world.getSelectObject().getName();
        }
        primitives.put(namePrimitives[0], prim);*/
	}

	private boolean VerifySelectedObject() {
    /*    if(world.getSelectObject() == null)
            return selectObjectName.equals("null");
        else
            return selectObjectName.equals(world.getSelectObject().getName());*/
		return false;
	}

	private String getNameTypeObj(String objName) {
		return objName.substring(nameGameWorld.length());
	}


}

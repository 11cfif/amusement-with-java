package ru.cfif11.cosmo.scene.forms;

import java.util.ArrayList;

import com.threed.jpct.FrameBuffer;
import ru.cfif11.cosmo.object.Camera;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public class ManagerGraphicForm {


	private ArrayList<GraphicForm> graphForms;

	public ManagerGraphicForm(ArrayList<GraphicForm> graphForms) {
		this.graphForms = graphForms;
	}

	public void refresh(Camera camera) {
		for (GraphicForm gForm : graphForms)
			gForm.refresh(camera);
	}

	public void drawGraphForm(FrameBuffer buffer) {
		for (GraphicForm gForm : graphForms)
			gForm.draw(buffer);
	}
}

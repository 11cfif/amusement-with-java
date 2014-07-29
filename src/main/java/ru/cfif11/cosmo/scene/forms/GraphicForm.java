package ru.cfif11.cosmo.scene.forms;

import java.util.HashMap;

import com.threed.jpct.FrameBuffer;
import ru.cfif11.cosmo.object.Camera;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public abstract class GraphicForm extends GraphAbstract {


	protected HashMap<String, GraphPrimitive> primitives;

	protected GraphicForm(int x, int y, int width, int height, int widthDest, int heightDest, String texture) {
		super(x, y, width, height, widthDest, heightDest);
		setTexture(texture);
		primitives = new HashMap<>();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public abstract void refresh(Camera camera);

	public abstract void draw(FrameBuffer buffer);

	protected abstract void createForm();


}

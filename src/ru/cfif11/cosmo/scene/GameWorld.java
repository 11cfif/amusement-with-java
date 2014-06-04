package ru.cfif11.cosmo.scene;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.World;
import org.lwjgl.opengl.Display;
import ru.cfif11.cosmo.Main;
import ru.cfif11.cosmo.Ticker;
import ru.cfif11.cosmo.control.ControllableMKInterface;
import ru.cfif11.cosmo.object.Camera;
import ru.cfif11.cosmo.object.physobject.PhysObject3D;
import ru.cfif11.cosmo.physics.PhysEngine;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public abstract class GameWorld implements ControllableMKInterface {


	protected World world;
	protected Ticker ticker;
	protected PhysEngine engine;
	protected ManagerGraphicForm manGraphForm;

	public static final String[] KEYS = new String[] {"W", "Q", "Escape"};
	private boolean[] keyStates = new boolean[KEYS.length];

	private Location location;

	protected GameWorld(Ticker ticker) {
		world = new World();
		this.ticker = ticker;
	}

	@Override
	public boolean pollControls() {
		Main.KEYBOARD_LISTENER.recordPollСontrols(KEYS, keyStates);
		return !keyStates[keyStates.length - 1] && !Display.isCloseRequested();

	}

	@Override
	public void applyControl(long ticks, FrameBuffer buffer) {
		if (keyStates == null || ticks == 0) {
			return;
		}

		if (keyStates[0]) {
			if (Main.rate > 2) {
				Main.rate--;
				ticker.setRate(Main.rate);
			}
		}

		if (keyStates[1]) {
			if (Main.rate < 30) {
				Main.rate++;
				ticker.setRate(Main.rate);
			}
		}

	}

	public Location getLocation() {
		return location;
	}

	public void addPhysObject(PhysObject3D physObject3D) {
		location.addPhysObject(physObject3D);
		engine.addPhysObject(physObject3D);
	}

	public void setLocation(Location location) {
		this.location = location;
		location.init();
		if(engine == null)
			engine = new PhysEngine(location);
		else
			engine.init(location);
	}

	public World getWorld() {
		return world;
	}

	public void renderScene(FrameBuffer buffer) {
		world.renderScene(buffer);
	}

	public void draw(FrameBuffer buffer) {
		world.draw(buffer);
	}

	public PhysObject3D getSelectObject() {
		return location.getSelectObject();
	}

	public void setSelectObject(PhysObject3D selectObject) {
		location.setSelectObject(selectObject);
	}

	public abstract boolean run(Camera camera, FrameBuffer buffer);

	public abstract void drawGraphForm(FrameBuffer buffer, Camera camera);

	public abstract void tunePositionCamera(Camera camera);

	protected abstract void initializationLocation();

	protected abstract void initializationManagerGraphForm();

}

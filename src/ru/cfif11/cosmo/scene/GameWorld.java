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

	public static final String[] KEYS = new String[] {"W", "Q", "Escape"};
	private boolean[] keyStates = new boolean[KEYS.length];


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
			if (Main.timestamp > 2) {
				Main.timestamp--;
				ticker.setTimestamp(Main.timestamp);
			}
		}

		if (keyStates[1]) {
			if (Main.timestamp < 30) {
				Main.timestamp++;
				ticker.setTimestamp(Main.timestamp);
			}
		}

	}

	public Location getLocation() {
		return engine.getLocation();
	}

	public void addPhysObject(PhysObject3D physObject3D) {
		engine.getLocation().addPhysObject(physObject3D);
	}

	public void setLocation(Location location) {
		engine = new PhysEngine(location);
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
		return engine.getLocation().getSelectObject();
	}

	public void setSelectObject(PhysObject3D selectObject) {
		engine.getLocation().setSelectObject(selectObject);
	}

	public abstract boolean run(Camera camera, FrameBuffer buffer);

	public abstract void tunePositionCamera(Camera camera);

	protected abstract void initializationLocation();
}

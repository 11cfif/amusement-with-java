package ru.cfif11.cosmo.scene;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.util.KeyState;
import ru.cfif11.cosmo.Main;
import ru.cfif11.cosmo.Ticker;
import ru.cfif11.cosmo.object.Camera;

public class ConsoleGameWorld extends GameWorld {

	public ConsoleGameWorld(Ticker ticker, Location location) {
		super(ticker);
		setLocation(location);
	}

	@Override
	public boolean run(Camera camera, FrameBuffer buffer) {
		boolean doLoop;
		doLoop = true;
		if(!ticker.isStarted())
			ticker.start();
		else
			ticker.waitTimestamp();
		if (ticker.isNotOver()) {
			//рассчитываем все силы и считаем новые местоположения объектов
			engine.calculate(ticker.getPhysTimestamp());

			//используем обработчик событий для движения камеры

			Main.KEYBOARD_LISTENER.setMainState();
			while(Main.KEYBOARD_LISTENER.getMainState() != KeyState.NONE) {
				Main.KEYBOARD_LISTENER.pollControls();

			}
			doLoop = pollControls();
		}
		return doLoop && ticker.isNotOver();
	}


	@Override
	public void tunePositionCamera(Camera camera) {

	}

	@Override
	protected void initializationLocation() {

	}

}

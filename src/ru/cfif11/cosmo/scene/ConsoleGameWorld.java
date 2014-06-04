package ru.cfif11.cosmo.scene;

import java.util.ArrayList;

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
		long ticks  = ticker.getTicks();
		if (ticks > 0) {
			//рассчитываем все силы и считаем новые местоположения объектов
			engine.calculate(0.1f);

			//используем обработчик событий для движения камеры

			Main.KEYBOARD_LISTENER.setMainState();
			while(Main.KEYBOARD_LISTENER.getMainState() != KeyState.NONE) {
				Main.KEYBOARD_LISTENER.pollControls();

			}
			doLoop = pollControls();
		}
		return doLoop;
	}

	@Override
	public void drawGraphForm(FrameBuffer buffer, Camera camera) {
		if(ticker.getTicks() > 0)
			System.out.println(getLocation().toString());
	}

	@Override
	public void tunePositionCamera(Camera camera) {

	}

	@Override
	protected void initializationLocation() {
		getLocation().init();
		engine.init(getLocation());
	}

	@Override
	protected void initializationManagerGraphForm() {
		manGraphForm = new ManagerGraphicForm(new ArrayList<GraphicForm>());
	}
}

package ru.cfif11.cosmo.manager;

import java.util.ArrayList;

import ru.cfif11.cosmo.Ticker;
import ru.cfif11.cosmo.scene.GameWorld;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public class LocationManager {

	private ArrayList<GameWorld> locations;
	private Ticker ticker;

	public LocationManager(Ticker ticker) {
		locations = new ArrayList<>();
		this.ticker = ticker;
		init();
	}

	private void init() {
		// GameWorld temp = new SolarSystemLoc(ticker);
//        locations.add(temp);
	}

	public GameWorld getGameWorld(int i) {
		return locations.get(i);
	}
}

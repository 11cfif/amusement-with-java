package ru.cfif11.cosmo.manager;

import ru.cfif11.cosmo.Ticker;
import ru.cfif11.cosmo.locations.SolarSystemLoc;
import ru.cfif11.cosmo.scene.GameWorld;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public class LocationManager {

    private ArrayList<GameWorld> locations;
    private Ticker ticker;

    public LocationManager(Ticker ticker) {
        locations = new ArrayList<GameWorld>();
        this.ticker = ticker;
        init();
    }

    private void init() {
        GameWorld temp = new SolarSystemLoc(ticker);
        locations.add(temp);
    }

    public GameWorld getGameWorld(int i) {
        return locations.get(i);
    }
}

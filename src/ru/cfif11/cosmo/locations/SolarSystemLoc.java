package ru.cfif11.cosmo.locations;

import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;
import ru.cfif11.cosmo.Ticker;
import ru.cfif11.cosmo.locations.enumeration.StarSystemEnum;
import ru.cfif11.cosmo.object.physobject.MassAttractObject3D;
import ru.cfif11.cosmo.object.physobject.MassObject3D;
import ru.cfif11.cosmo.object.physobject.Ship;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public class SolarSystemLoc extends StarSystem {

    Ship ship;


    public SolarSystemLoc(Ticker ticker) {
        super(ticker);
        initializationLevel();
    }

    @Override
    protected void initializationLevel() {
        MassAttractObject3D tempObj;
        SimpleVector        tempVec;
        system  = new ArrayList<MassAttractObject3D>();
        ships   = new ArrayList<Ship>();
        for (StarSystemEnum p : StarSystemEnum.values()) {
            tempVec  = p.getVelocity();
            tempVec.scalarMul(scalingFactor);
            tempObj = new MassAttractObject3D(Primitives.getSphere(100, p.getRadius() * scalingFactor), p.getNameObject(),
                    tempVec, p.getMass(), (int)(p.getRadius() * scalingFactor));
            tempObj.setTexture(tempObj.getName());
            tempObj.setEnvmapped(Object3D.ENVMAP_ENABLED);
            tempVec  = p.getInitialPosition();
            tempVec.scalarMul(scalingFactor);
            tempObj.translate(tempVec);
            system.add(tempObj);
            world.addObject(tempObj);
            tempObj.build();
            tempObj.compileAndStrip();
        }
        initializationManagerGraphForm();

        ship = new Ship(Loader.load3DS("resources/3ds/sts.3ds", 1f)[0], "SolarSystemSheep", new SimpleVector(0,0,0), 1e10, new int[] {3,3,3});
        ship.translate(1950, 0, 0);
        ship.setOrientation(new SimpleVector(1, 0, 0), new SimpleVector(0, 1, 0));
        ships.add(ship);
        world.addObject(ship);
        ship.build();
        ship.compileAndStrip();
    }


}

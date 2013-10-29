package ru.cfif11.cosmo.locations;

import com.threed.jpct.*;
import ru.cfif11.cosmo.Ticker;
import ru.cfif11.cosmo.adapterphysics.AdapterPhysics;
import ru.cfif11.cosmo.locations.enumeration.StarSystemEnum;
import ru.cfif11.cosmo.object.Camera;
import ru.cfif11.cosmo.object.physobject.MassAttractObject3D;
import ru.cfif11.cosmo.scene.*;
import ru.cfif11.cosmo.scene.forms.Radar;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public class SolarSystemLoc extends StarSystem {


    public SolarSystemLoc(Ticker ticker) {
        super(ticker);
        initializationLevel();
    }

    @Override
    protected void initializationLevel() {
        MassAttractObject3D tempObj;
        SimpleVector        tempVec;
        system = new ArrayList<MassAttractObject3D>();
        int i = 0;
        for (StarSystemEnum p : StarSystemEnum.values()) {
            tempVec  = p.getVelocity();
            tempVec.scalarMul(scalingFactor);
            tempObj = new MassAttractObject3D(Primitives.getSphere(100, p.getRadius() * scalingFactor), p.getNameObject(),
                    tempVec, p.getMass());
            tempObj.setTexture(tempObj.getName());
            tempObj.setEnvmapped(Object3D.ENVMAP_ENABLED);
            tempVec  = p.getInitialPosition();
            tempVec.scalarMul(scalingFactor);
            tempObj.translate(tempVec);
            system.add(tempObj);
            world.addObject(tempObj);
            tempObj.build();
            tempObj.compileAndStrip();
            i++;
        }
        initializationManagerGraphForm();
    }

}

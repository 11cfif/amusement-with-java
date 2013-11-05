package ru.cfif11.cosmo.object.physobject;

import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public class Ship extends MassObject3D {

    /**
     * Create MassObject3D on base Object3D, velocity and mass
     *
     * @param obj      the Object3D
     * @param velocity the velocity
     * @param mass     the mass
     */
    public Ship(Object3D obj, String name, SimpleVector velocity, double mass, int[] characteristicSize) {
        super(obj, name, velocity, mass);
        this.characteristicSize = characteristicSize;
    }

    public Ship(String model, float scale, String name, SimpleVector velocity, double mass, int[] characteristicSize) {
        super(Loader.load3DS(model, scale)[0], name, velocity, mass);
        this.characteristicSize = characteristicSize;
    }
}

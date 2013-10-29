package ru.cfif11.cosmo.locations.enumeration;

import com.threed.jpct.SimpleVector;


/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr.
 *
 * In this enumeration establishes the basic physical parameters for the bodies of the star system
 */
public enum StarSystemEnum {
    sun     ("SolarSystemStar", 3.65e+14, 1e+6f, new SimpleVector(0,0,0), new SimpleVector(0,0,0)),
    earth   ("SolarSystemPlanet_1",9.44e+11,15e+4f,new SimpleVector(2e+7, 0,0), new SimpleVector(0,0,-35e+3)),
    moon    ("SolarSystemSputnik_1", 9e+10, 5e+4f, earth.initialPosition.calcAdd(new SimpleVector(5e+5,0,0)),
            earth.getVelocity().calcAdd(new SimpleVector(0,0,-11.2e+3)));

    private String          nameObject;
    private double          mass;
    private float           radius;
    private SimpleVector    initialPosition;
    private SimpleVector    velocity;

    /**
     * Creates the bodies of the star system
     * @param nameObject the name of the body
     * @param mass the mass of the body
     * @param radius the radius of the body
     * @param initialPosition the initial position of the body
     * @param velocity the initial velocity of the body
     */
    StarSystemEnum(String nameObject, double mass, float radius, SimpleVector initialPosition, SimpleVector velocity) {
        this.nameObject         = nameObject;
        this.mass               = mass;
        this.radius             = radius;
        this.initialPosition    = initialPosition;
        this.velocity           = velocity;
    }

    /**
     * Returns the name of the body
     * @return the name of the body
     */
    public String getNameObject()            { return nameObject; }

    /**
     * Returns the mass of the body
     * @return the mass of the body
     */
    public double getMass()                  { return mass; }

    /**
     * Returns the radius of the body
     * @return the radius of the body
     */
    public float getRadius()                 { return radius; }

    /**
     * Returns the initial position of the body
     * @return the initial position of the body
     */
    public SimpleVector getInitialPosition() { return initialPosition; }

    /**
     * Returns the initial velocity of the body
     * @return the initial velocity of the body
     */
    public SimpleVector getVelocity()        { return velocity; }

}

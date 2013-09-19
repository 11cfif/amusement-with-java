package ru.cfif11.cosmo.physics;

import com.threed.jpct.SimpleVector;

/**
 * Abstract class that is the superclass for the simulation of different physical laws
 */
public abstract class Physics {
	
	private SimpleVector totalForce;
	private SimpleVector location;

    /**
     * The simulator is initialized with zero values
     */
	public Physics() {
		this.totalForce = new SimpleVector();
		this.location = new SimpleVector();
	}

    /**
     * Returns the total active force
     * @return the total force
     */
    public SimpleVector getTotalForce() {
        return totalForce;
    }

    /**
     * Calculates total force
     */
	public abstract void calcTotalForce();

    /**
     * Sets the basic parameters for the simulator
     * @param a characteristic of the PhysObject3D to be calculated
     * @param s characteristics of other PhysObject3D
     * @param v location of other PhysObject3D
     * @param loc location of the PhysObject3D to be calculated
     */
	public abstract void setParam(double a, double[] s, SimpleVector[] v, SimpleVector loc);

    /**
     * Reset all settings simulator
     */
	public void reset() {
		totalForce.set(0, 0, 0);
		location.set(0, 0, 0);
	}

    /**
     * Set location of the PhysObject3D to be calculated
     * @param location the location
     */
    protected void setLocation(SimpleVector location) {
        this.location = location;
    }

    /**
     * Sets the total active force
     * @param totalForce the total force
     */
	protected void setTotalForce(SimpleVector totalForce) {
		this.totalForce = totalForce;
	}

    /**
     * Returns location of the PhysObject3D to be calculated
     * @return the location
     */
	protected SimpleVector getLocation() {
		return location;
	}

}

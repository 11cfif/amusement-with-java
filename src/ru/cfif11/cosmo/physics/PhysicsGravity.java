package ru.cfif11.cosmo.physics;

import com.threed.jpct.SimpleVector;

/**
 * Simulator to model the gravitational forces
 */
public class PhysicsGravity extends Physics {
	
	private double weight;
	private double[] mass;
	private SimpleVector[] distances;
	private SimpleVector gravityForce;

    /**
     * The simulator is initialized
     * @param n the number of objects
     */
	public PhysicsGravity(int n) {
		super();
		this.mass = new double[n];
		this.distances = new SimpleVector[n];
		this.gravityForce = new SimpleVector();
	}


    @Override
	public void setParam(double a, double[] s, SimpleVector[] v, SimpleVector loc) {
        /**
         * Sets the basic parameters for the simulator
         * @param a mass of the PhysObject3D to be calculated
         * @param s mass of other PhysObject3D
         * @param v location of other PhysObject3D
         * @param loc location of the PhysObject3D to be calculated
         */
		setWeight(a);
		setMass(s);
		setLocation(loc);
		setDistances(v);
	}
	
	@Override
	public void calcTotalForce() {
		calcGravityForce();
		setTotalForce(gravityForce);
	}
	
	@Override
	public void reset() {
		super.reset();
		weight = 0;
		gravityForce.set(0, 0, 0);
		//setTotalForce(gravityForce);
	}

    /**
     * Calculates the force of gravity
     */
	protected void calcGravityForce() {
		double scalar;
		double dist;
		for(int i = 0; i < mass.length; i++) {
			dist = distances[i].length();
			if(Math.abs(dist) > 0.001) {			
				scalar = PhysConstantEnum.gravitationalConstant.value() * weight * mass[i] / 
						(dist * dist * dist);
				distances[i].scalarMul((float)scalar);
				gravityForce.add(distances[i]);
			}
		}
	}

    /**
     * Sets mass of the PhysObject3D to be calculated
     * @param m the mass
     */
    private void setWeight(double m) {
        this.weight = m;
    }

    /**
     * Sets mass of other PhysObject3D
     * @param m the array of weights
     */
    private void setMass(double[] m) {
        this.mass = m.clone();
    }

    /**
     * Calculates the distance to PhysObject3D to be calculated and from other PhysObject3D
     * @param dist the array of distances
     */
    private void setDistances(SimpleVector[] dist) {
        for(int i = 0; i < dist.length; i++) {
            distances[i] = dist[i].calcSub(getLocation());
        }
    }

}

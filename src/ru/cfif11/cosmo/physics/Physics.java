package ru.cfif11.cosmo.physics;

import com.threed.jpct.SimpleVector;

public abstract class Physics {
	
	private SimpleVector totalForce;
	private SimpleVector location;
	
	public Physics(int n) {
		this.totalForce = new SimpleVector();
		this.location = new SimpleVector();
	}

    public SimpleVector getTotalForce() {
        return totalForce;
    }
	
	public abstract void calcTotalForce();
	
	public abstract void setParam(double a, double[] s, SimpleVector[] v, SimpleVector loc);
	
	public void reset() {
		totalForce.set(0, 0, 0);
		location.set(0, 0, 0);
	}

    protected void setLocation(SimpleVector location) {
        this.location = location;
    }
	
	protected void setTotalForce(SimpleVector totalForce) {
		this.totalForce = totalForce;
	}
	
	protected SimpleVector getLoacation() {
		return location;
	}

}

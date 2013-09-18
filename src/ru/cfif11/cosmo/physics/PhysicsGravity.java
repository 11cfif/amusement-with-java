package ru.cfif11.cosmo.physics;

import com.threed.jpct.SimpleVector;

public class PhysicsGravity extends Physics {
	
	private double weight;
	private double[] mass;
	private SimpleVector[] distances;
	private SimpleVector gravityForce;
	
	public PhysicsGravity(int n) {
		super(n);
		this.mass = new double[n];
		this.distances = new SimpleVector[n];
		this.gravityForce = new SimpleVector();
	}
	
	public void setParam(double a, double[] s, SimpleVector[] v, SimpleVector loc) {
		setWeight(a);
		setMass(s);
		setLocation(loc);
		setDistances(v);
		loc = null;
	}
	
	private void setWeight(double m) {
		this.weight = m;
	}
	
	private void setMass(double[] m) {
		this.mass = m.clone();
	}
	
	private void setDistances(SimpleVector[] dist) {
		for(int i = 0; i < dist.length; i++) {
			distances[i] = dist[i].calcSub(getLoacation());
		}
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


}

package ru.cfif11.cosmo.adapterphysics;

import java.util.ArrayList;
import java.util.Enumeration;

import ru.cfif11.cosmo.physics.Physics;
import ru.cfif11.cosmo.physics.PhysicsGravity;
import ru.cfif11.cosmo.physobject.CanAttractInterface;
import ru.cfif11.cosmo.physobject.HasChargeInterface;
import ru.cfif11.cosmo.physobject.HasMassInterface;
import ru.cfif11.cosmo.physobject.MassObject3D;
import ru.cfif11.cosmo.physobject.PhysObject3D;

import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

public class AdapterPhysics {
	
	private World world;
	private int numObjs;
	private int[] gravityInteraction;
	private int[] sourceGravity;
	private int[] electromagneticInteraction;
	private ArrayList<Physics> simulators;
	private ArrayList<PhysObject3D> objects;
	private double[] arguments;
	private SimpleVector[] dist;
	private double ownParam;	
	
	public AdapterPhysics(World world) {
		this.world = world;
		this.numObjs = world.getSize();
		this.gravityInteraction = new int[numObjs + 1];
		this.sourceGravity = new int[numObjs + 1];
		this.electromagneticInteraction = new int[numObjs + 1];
		this.simulators = new ArrayList<Physics>();
		this.objects = new ArrayList<PhysObject3D>();
		analysisInteraction();
		createSimulator();
	}

	public void calcForce() {
		Physics p;
		for(int i = 0; i < simulators.size(); i++) {
			p = simulators.get(i);
			if(p instanceof PhysicsGravity) {
				MassObject3D mo;
				arguments = new double[sourceGravity[numObjs]];
				dist = new SimpleVector[sourceGravity[numObjs]];
				for(int j = 0; j < sourceGravity[numObjs]; j++) {
					mo = (MassObject3D)objects.get(sourceGravity[j]);
					arguments[j] = mo.getMass();
					dist[j] = mo.getTransformedCenter();
				}
				for(int k = 0; k < gravityInteraction[numObjs]; k++) {
					mo = (MassObject3D)objects.get(gravityInteraction[k]);
					ownParam = mo.getMass();
					p.setParam(ownParam, arguments, dist, mo.getTransformedCenter());
					p.calcTotalForce();
					mo.calcAcceleration(p.getTotalForce());
				}
			}
			p.reset();
		}
		
	}
	
	private void createSimulator() {
		if(gravityInteraction[numObjs] != 0) {
			PhysicsGravity pGravity = new PhysicsGravity(sourceGravity[numObjs]);
			simulators.add(pGravity);
		}
		return;
		
	}
	
	private void analysisInteraction() {
		Enumeration<PhysObject3D> e = world.getObjects();
		int i = 0;
		PhysObject3D temp;
		while(e.hasMoreElements()) {
			temp = e.nextElement();
			if(temp instanceof HasMassInterface) {
				gravityInteraction[gravityInteraction[numObjs]] = i;
				gravityInteraction[numObjs]++;
				if(temp instanceof CanAttractInterface) {
					sourceGravity[sourceGravity[numObjs]] = i;
					sourceGravity[numObjs]++;
				}
			} else if(temp instanceof HasChargeInterface){
				electromagneticInteraction[electromagneticInteraction[numObjs]] = i;
				electromagneticInteraction[numObjs]++;
			}
			i++;
			objects.add(temp);
		}
	}

}

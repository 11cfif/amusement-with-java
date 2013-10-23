package ru.cfif11.cosmo.adapterphysics;

import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;
import ru.cfif11.cosmo.physics.Physics;
import ru.cfif11.cosmo.physics.PhysicsGravity;
import ru.cfif11.cosmo.object.physobject.*;

import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Class linking interfaces of PhysObject3D and simulators
 */
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

    /**
     *  Creates a new AdapterPhysics.
     * @param world the World
     */
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

    /**
     * calculates the force acting on the PhysObject3D
     */
	public void calcForce() {
		for(Physics p: simulators) {
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
                    p.reset();
				}
			}
			p.reset();
		}
		
	}

    /**
     * Creates create an initialized AdapterPhysics.
     */
	private void createSimulator() {
		if(gravityInteraction[numObjs] != 0) {
			PhysicsGravity pGravity = new PhysicsGravity(sourceGravity[numObjs]);
			simulators.add(pGravity);
		}
	}

    /**
     * The analysis of all interactions in the World
     */
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

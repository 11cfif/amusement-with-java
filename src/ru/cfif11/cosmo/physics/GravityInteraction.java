package ru.cfif11.cosmo.physics;

import com.threed.jpct.SimpleVector;
import ru.cfif11.cosmo.object.physobject.PhysObject3D;

public class GravityInteraction extends InteractionWithObject {

	public GravityInteraction(InteractionType type) {
		super(type);
	}

	@Override
	public void interactWithObject(PhysObject3D mainObject, PhysObject3D minorObject) {
		SimpleVector direction = minorObject.getTransformedCenter().calcSub(mainObject.getTransformedCenter());
		double distance = direction.length();
		direction.scalarMul((float)(PhysConstantEnum.gravitationalConstant.value() * minorObject.getMass() /
			(distance * distance * distance)));
		mainObject.setAcceleration(direction.calcAdd(mainObject.getAcceleration()));
	}
}

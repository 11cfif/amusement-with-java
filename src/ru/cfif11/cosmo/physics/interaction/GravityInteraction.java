package ru.cfif11.cosmo.physics.interaction;

import com.threed.jpct.SimpleVector;
import ru.cfif11.cosmo.object.physobject.PhysObject3D;
import ru.cfif11.cosmo.physics.PhysConstantEnum;

class GravityInteraction extends DescriberInteraction {

	static final GravityInteraction INSTANCE = new GravityInteraction();

	private GravityInteraction() {	}

	@Override
	public void describe(PhysObject3D mainObject, PhysObject3D minorObject) {
		SimpleVector direction = minorObject.getTransformedCenter().calcSub(mainObject.getTransformedCenter());
		double distance = direction.length();
		direction.scalarMul((float)(PhysConstantEnum.gravitationalConstant.value() * minorObject.getMass() /
			(distance * distance * distance)));
		mainObject.setAcceleration(direction.calcAdd(mainObject.getAcceleration()));
	}

	@Override
	public String toString() {
		return "Describer gravity interaction";
	}
}

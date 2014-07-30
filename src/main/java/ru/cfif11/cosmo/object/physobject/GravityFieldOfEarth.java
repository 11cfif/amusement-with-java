package ru.cfif11.cosmo.object.physobject;

import com.threed.jpct.SimpleVector;
import ru.cfif11.cosmo.physics.PhysConstant;
import ru.cfif11.cosmo.physics.interaction.InteractionType;

public class GravityFieldOfEarth extends Field {

	public static final String DIRECTION_FORCE = "DirectForce";
	public static final String INITIAL_POSITION = "InitPos";
	public static final String FREE_FALL_ACCELERATION = "g";
	public static final String FREE_FALL_ACCELERATION_V = "g_v";

	public GravityFieldOfEarth() {
		this(SimpleVector.ORIGIN, new SimpleVector(0, 0, -1));
	}

	public GravityFieldOfEarth(SimpleVector directForce) {
		this(SimpleVector.ORIGIN, directForce);
	}

	public GravityFieldOfEarth(SimpleVector initPos, SimpleVector directForce) {
		super(InteractionType.GRAVITATIONAL);
		addCharacteristic(FREE_FALL_ACCELERATION, PhysConstant.FREE_FALL_ACCELERATION.value());
		addCharacteristic(INITIAL_POSITION, initPos);
		addCharacteristic(DIRECTION_FORCE, directForce.normalize());
		SimpleVector accel = new SimpleVector(directForce);
		accel.scalarMul((float)PhysConstant.FREE_FALL_ACCELERATION.value());
		addCharacteristic(FREE_FALL_ACCELERATION_V, accel);
	}
}

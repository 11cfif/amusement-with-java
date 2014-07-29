package ru.cfif11.cosmo.object.physobject;

import com.threed.jpct.SimpleVector;
import ru.cfif11.cosmo.physics.PhysConstant;
import ru.cfif11.cosmo.physics.interaction.InteractionType;

public class GravitationalFieldOfEarth extends Field {


	public static final String DIRECTION_FORCE = "DirectForce";
	public static final String INITIAL_POSITION = "InitPos";
	public static final String FREE_FALL_ACCELERATION = "g";
	public static final String FREE_FALL_ACCELERATION_V = "g_v";

	private static GravitationalFieldOfEarth INSTANCE;

	public static GravitationalFieldOfEarth getGravitationalField(
		SimpleVector initPos, SimpleVector directForce)
	{
		if (INSTANCE == null)
			throw new IllegalStateException("GravitationalFieldOfEarth has already been created");
		INSTANCE = new GravitationalFieldOfEarth();
		INSTANCE.addCharacteristic(INITIAL_POSITION, initPos);
		INSTANCE.addCharacteristic(DIRECTION_FORCE, directForce.normalize());
		SimpleVector accel = new SimpleVector(directForce);
		accel.scalarMul((float)PhysConstant.FREE_FALL_ACCELERATION.value());
		INSTANCE.addCharacteristic(FREE_FALL_ACCELERATION_V, accel);
		return INSTANCE;
	}

	private GravitationalFieldOfEarth() {
		super(InteractionType.GRAVITATIONAL);
		addCharacteristic(FREE_FALL_ACCELERATION, PhysConstant.FREE_FALL_ACCELERATION.value());
	}
}

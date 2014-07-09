package ru.cfif11.cosmo.physics;


/**
 * Enumeration of the basic physical constants
 */
public enum PhysConstantEnum {
	GRAVITATIONAL_CONSTANT(6.67400E-11),
	FREE_FALL_ACCELERATION(9.80665);

	private double value;

	PhysConstantEnum(double value) {
		this.value = value;
	}

	/**
	 * Returns value physical constant
	 *
	 * @return value
	 */
	public double value() {
		return value;
	}

}

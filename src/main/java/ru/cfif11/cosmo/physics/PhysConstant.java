package ru.cfif11.cosmo.physics;


/**
 * Enumeration of the basic physical constants
 */
public enum PhysConstant {
	GRAVITATIONAL_CONSTANT(6.67400E-11),
	FREE_FALL_ACCELERATION(9.80665),
	EARTH_RADIUS(6.371e+6),
	EARTH_MASS(5.973e+24);

	private double value;

	PhysConstant(double value) {
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

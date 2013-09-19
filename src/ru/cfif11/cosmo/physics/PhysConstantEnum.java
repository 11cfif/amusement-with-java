package ru.cfif11.cosmo.physics;


/**
 * Enumeration of the basic physical constants
 */
public enum PhysConstantEnum {
	gravitationalConstant(6.67300E-11);
	
	private double value;

	PhysConstantEnum(double value) {
        this.value = value;
    }

    /**
     * Returns value physical constant
     * @return value
     */
	public double value() {return value;}

}

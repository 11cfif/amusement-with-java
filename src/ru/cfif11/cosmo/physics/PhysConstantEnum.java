package ru.cfif11.cosmo.physics;

public enum PhysConstantEnum {
	gravitationalConstant(6.67300E-11);
	
	private double value;
	
	PhysConstantEnum(double value) {
        this.value = value;
    }
	
	public double value() {return value;}

}

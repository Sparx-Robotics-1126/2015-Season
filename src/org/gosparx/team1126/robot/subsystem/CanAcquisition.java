package org.gosparx.team1126.robot.subsystem;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * @author Reizwan, Raza
 * Stores the state of the can acquisition and controls the arm and claw
 * version 1.0 Season 2015
 */
public class CanAcquisition{
	/**
	 * Position for which the arms to drop
	 */
	private static final double DROP_RELEASE_POSITION = 1.0; //TODO find out
	/**
	 * Position for which the arms raise
	 */
	private static final double RAISE_RELEASE_POSITION = 1.0; //TODO find out
	/**
	 * This is the sensors that tells us the right claw is inside can
	 */
	private DigitalInput rightArmInCan;
	/**
	 * This is the sensors that tells us the left claw is inside can
	 */
	private DigitalInput leftArmInCan;
	/**
	 * This is the servo that releases both the arms
	 */
	private Servo releasingArmsServo;
	/**
	 * this is the servo to raise both arms
	 */
	private Servo raisingArmsServo; 
	/**
	 * This is the solenoid for the right arm
	 */
	private Solenoid actuateRightArm;
	/** 
	 * This is the solenoid for the left arm
	 */
	private Solenoid actuateLeftArm;
	/**
	 * The can acquisition for the singleton model
	 */
	private static CanAcquisition acq;
	
	/**
	 * Returns an instance of the can acquisition
	 */
	public static CanAcquisition getInstance(){
		if(acq == null){
			acq = new CanAcquisition();
		}
		return acq;
	} 
	
	/**
	 * Creates new CanAqcuisition
	 */
	private CanAcquisition() {
		// TODO get all IO from IO class
		rightArmInCan = new DigitalInput(0);
		leftArmInCan = new DigitalInput(0);
		releasingArmsServo = new Servo(0);
		raisingArmsServo = new Servo(0);
		actuateRightArm = new Solenoid(0);
		actuateLeftArm = new Solenoid(0);
	}
	
	/**
	 * Drops both arms
	 */
	public void armsDrop() {
		releasingArmsServo.set(DROP_RELEASE_POSITION);
	}
	/**
	 * Raises both arms
	 */
	public void armsRaise(){
		raisingArmsServo.set(RAISE_RELEASE_POSITION);
	}
	/**
	 * Returns TRUE if right hand is in the can
	 */
	public boolean rightHandInCan() {
		return(rightArmInCan.get());
	}
	
	/**
	 * Returns FALSE if left hand is in the can
	 */
	public boolean leftHandInCan() {
		return(leftArmInCan.get());
	}	

	/**
	 * Opens both arms
	 */
	public void openArms() {
		if (rightArmInCan.get()) {
			actuateRightArm.set(true);
		}
		if (leftArmInCan.get()) {
			actuateLeftArm.set(true);
		}
	}
	/**
	 * Closes both arms 	
	 */
	public void closeArms() {
		actuateRightArm.set(false); 
		actuateLeftArm.set(false);
	}
		
}

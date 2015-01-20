package org.gosparx.team1126.robot.subsystem;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * @author Reizwan, Raza
 * Stores the state of the can acquisition and controls the arm and claw
 * version 1.0 Season 2015
 */
public class CanAcquisition extends GenericSubsystem{
	/**
	 * Position for which the arms to drop
	 */
	private static final double DROP_RELEASE_POSITION = 1.0;
	/**
	 * This is the sensors that tells us the right claw is inside can
	 */
	private DigitalInput rightClawInCan;
	/**
	 * This is the sensors that tells us the left claw is inside can
	 */
	private DigitalInput leftClawInCan;
	/**
	 * This is the servo that releases both the arms
	 */
	private Servo releasingArmsServo;
	/**
	 * This is the servo to lift right arm
	 */
	private Servo rightLiftArm;
	/**
	 * This is the servo to lift left arm
	 */
	private Servo leftLiftArm;
	/**
	 * This is the solenoid for the right claw
	 */
	private Solenoid actuateRightClaw;
	/** 
	 * This is the solenoid for the left claw
	 */
	private Solenoid actuateLeftClaw;
	
	/**
	 * The current state of the can acquisition
	 */
	private int canAcquisitionState;
	
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
		// TODO get the name of this subsystem from the logger in GenericSubsystem when it gets implemented
		super("CanAcqui", Thread.NORM_PRIORITY);
	}
		
		
	/**
	 * Initializes everything
	 */
	protected boolean init() {
		// TODO get all IO from IO class
		rightClawInCan = new DigitalInput(0);
		leftClawInCan = new DigitalInput(0);
		releasingArmsServo = new Servo(0);
		rightLiftArm = new Servo(0);
		leftLiftArm = new Servo(0);
		actuateRightClaw = new Solenoid(0);
		actuateLeftClaw = new Solenoid(0);
		return false;
	}

	/**
	 * Loops.
	 */
	protected boolean execute() {
		switch(canAcquisitionState){
			case State.ARMS_DROP:
				releasingArmsServo.set(DROP_RELEASE_POSITION);
				canAcquisitionState = State.ARMS_RELEASED; 
				break;
			case State.ARMS_RELEASED:
				if (releasingArmsServo.get() == DROP_RELEASE_POSITION) {
					canAcquisitionState = State.IDLE;
				}
			case State.IDLE:
				break;
			default:
		}
		// TODO what is this return?
		return true;
	}

	@Override
	protected long sleepTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void writeLog() {
		// TODO Auto-generated method stub
	
	}
	/**
	 * Drops both arms and goes to armsDone when the servo reaches the release position
	 */
	public void armsDrop() {
		canAcquisitionState = State.ARMS_DROP;
	}
	
	/**
	 * Returns if the last command is done
	 */
	public boolean isLastCommandDone() {
		return(canAcquisitionState == State.IDLE);
	}
	
	private static class State{
		static final int IDLE = 0;
		static final int ARMS_DROP = 1;
		static final int ARMS_RELEASED = 2;
		public static String getState(int state){
			switch(state){
				case IDLE:
					return "Idle";
				case ARMS_DROP:
					return "Dropping Arms";
				case ARMS_RELEASED:
					return "Arms Released";
			}
			return "Unknown state";
		}
	}

		
}

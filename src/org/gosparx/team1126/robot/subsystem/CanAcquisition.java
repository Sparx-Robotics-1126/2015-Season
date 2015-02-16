package org.gosparx.team1126.robot.subsystem;

import org.gosparx.team1126.robot.IO;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * Stores the state of the can acquisition and controls the arm and claw
 * version 1.0 Season 2015
 * @author Raza Ahmed
 * @author Reizwan Chowdhury
 * @author Andrew Thompson
 */

public class CanAcquisition extends GenericSubsystem{
	
	/**
	 * The position to drop arms
	 */
	private static final Value DROP_AMRS = Value.kOn;
	
	/**
	 * The position to raise arms
	 */
	private static final Value RAISE_ARMS = Value.kOff;
	
	/**
	 * The "grab" position of the pnu
	 */
	private static final boolean GRAB = true;
	
	/**
	 * This is the sensors that tells us the right claw is inside can
	 */
	private DigitalInput rightArmInCan;

	/**
	 * This is the sensors that tells us the left claw is inside can
	 */
	private DigitalInput leftArmInCan;

	/**
	 * The solenoid for arm controls 
	 */
	private Relay arms;
	
	/**
	 * This is the solenoid for the right arm
	 */
	private Solenoid rightArm;

	/**
	 * This is the solenoid for the left arm
	 */
	private Solenoid leftArm;
	
	/**
	 * The can acquisition for the singleton model
	 */
	private static CanAcquisition acq;

	/**
	 * The current state of the AUTO CAN subsystem
	 */
	private State currentState = State.STANDBY;

	/**
	 * True if the right arm sensor has been triggered
	 */
	private boolean hasRight = false;
	
	/**
	 * True if the left arm sensor has been triggered
	 */
	private boolean hasLeft = false;
	
	/**
	 * Returns an instance of the can acquisition
	 */
	public static synchronized CanAcquisition getInstance(){
		if(acq == null){
			acq = new CanAcquisition();
		}
		return acq;
	} 

	/**
	 * Creates new CanAqcuisition
	 */
	private CanAcquisition() {
		super("Can Acq", Thread.NORM_PRIORITY);
	}

	/**
	 * Set auto function
	 */
	public void setAutoFunction(State wantedAutoState) {
		currentState = wantedAutoState;
		LOG.logMessage("Auto Can State changing to: "+ State.getName(wantedAutoState));
	}
	
	/**
	 * @return is Acquisition done last auto command
	 */
	public boolean isDone() {
		return (currentState == State.STANDBY);
	}

	/**
	 * Initializes the magic
	 */
	@Override
	protected boolean init() {
		// TODO get all IO from IO class
		rightArmInCan = new DigitalInput(IO.DIO_CAN_AUTO_RIGHT);
		leftArmInCan = new DigitalInput(IO.DIO_CAN_AUTO_LEFT);
		arms = new Relay(0);
		arms.set(RAISE_ARMS);
		rightArm = new Solenoid(IO.PNU_ACQ_CAN_RIGHT);
		leftArm = new Solenoid(IO.PNU_ACQ_CAN_LEFT);
		return true;
	}

	/**
	 * Sends controllers/sensors to livewindow
	 */
	@Override
	protected void liveWindow() {
		LiveWindow.addActuator(getName(), "Arms", arms);
		LiveWindow.addActuator(getName(), "Right Arm", rightArm);
		LiveWindow.addActuator(getName(), "Left Arm", leftArm);
		LiveWindow.addSensor(getName(), "Right Arm Touch", rightArmInCan);
		LiveWindow.addSensor(getName(), "Left Arm Touch", leftArmInCan);
	}

	/**
	 * Makes the magic work
	 */
	@Override
	protected boolean execute() {
		switch(currentState){
		case STANDBY:
			hasLeft = false;
			hasRight = false;
			break;
		case DROP_ARMS:
			arms.set(DROP_AMRS);
			currentState = State.ATTEMPT_TO_GRAB;
			break;
		case ATTEMPT_TO_GRAB:
			//RIGHT
			if(!rightArmInCan.get() && !hasRight){
				rightArm.set(GRAB);
				hasRight = true;
				LOG.logMessage("Right grabbed");
			}
			
			//LEFT
			if(!leftArmInCan.get() && !hasLeft){
				leftArm.set(GRAB);
				hasLeft = true;
				LOG.logMessage("Left grabbed");
			}
			
			if(hasRight && hasLeft){
				currentState = State.STANDBY;
				LOG.logMessage("Cans have been grabbed");
			}
			break;
		case RELEASE:
			rightArm.set(!GRAB);
			leftArm.set(!GRAB);
			currentState = State.STANDBY;
			break;
		case DISABLE:
			arms.set(RAISE_ARMS);
			currentState = State.STANDBY;
			break;
		default:
			LOG.logMessage("INVALID STATE: " + currentState);
		}
		return false;
	}

	/**
	 * The amount of time we are sleeping for
	 */
	protected long sleepTime() {
		return 10;
	}

	/**
	 * Logs the current state
	 */
	protected void writeLog() {
		LOG.logMessage("Current state: " + State.getName(currentState));
		LOG.logMessage("LEFT: "  + leftArmInCan.get() + " RIGHT: " + rightArmInCan.get());
	}

	/**
	 * Enum that represents the current sate for CanAuto CanAcquisition 
	 */
	public enum State{
		STANDBY,
		DROP_ARMS,	
		ATTEMPT_TO_GRAB,
		RELEASE,
		DISABLE;

		public static String getName(State state){
			switch(state){
			case STANDBY: 			return "Standby";
			case DROP_ARMS: 		return "Drop Arms";
			case ATTEMPT_TO_GRAB: 	return "Attempting to grab";
			case RELEASE: 			return "Releasing cans";
			case DISABLE: 			return "Disabled";
			default: 				return "Unknown State: " + state;
			}
		}
	}		
}

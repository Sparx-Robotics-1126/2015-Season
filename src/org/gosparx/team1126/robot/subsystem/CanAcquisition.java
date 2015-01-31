package org.gosparx.team1126.robot.subsystem;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * @author Raza Ahmed
 * @author Reizwan Chowdhury
 * @author Andrew Thompson
 * Stores the state of the can acquisition and controls the arm and claw
 * version 1.0 Season 2015
 */

public class CanAcquisition extends GenericSubsystem{

	
	/**
	 * Position for which the arms to drop
	 */
	private static final double DROP_RELEASE_POSITION = 1.0; //TODO find out

	/**
	 * Position for which the arms start
	 */
	private static final double DROP_DEFAULT_POSITION = 0.0; //TODO find value 
	
	/**
	 * Position for which the arms raise
	 */
	private static final double RAISE_RELEASE_POSITION = 1.0; //TODO find out

	/**
	 * Position for which the arms start
	 */
	private static final double RAISE_DEFAULT_POSITION = 0.0;
	
	/**
	 * The "grab" position of the pnu
	 */
	private static final boolean GRAB = false;
	
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
		super("Can Acq", Thread.NORM_PRIORITY);
	}

	/**
	 * Set auto function
	 */
	public void setAutoFunction(State wantedAutoState) {
		currentState = wantedAutoState;
	}
	
	/**
	 * @return is Acquisition done last auto command
	 */
	public boolean isDone() {
		return (currentState == State.STANDBY);
	}
	/**
	 * Drops both arms
	 */
	private void armsDrop() {
		releasingArmsServo.set(DROP_RELEASE_POSITION);
	}

	/**
	 * Raises both arms
	 */
	private void armsRaise(){
		raisingArmsServo.set(RAISE_RELEASE_POSITION);
	}
	
	/**
	 * Resets the servo
	 */
	private void resetServo(){
		releasingArmsServo.set(DROP_DEFAULT_POSITION);
		raisingArmsServo.set(RAISE_DEFAULT_POSITION);
	}

	@Override
	protected boolean init() {
		// TODO get all IO from IO class
		rightArmInCan = new DigitalInput(0);
		leftArmInCan = new DigitalInput(0);
		releasingArmsServo = new Servo(0);
		raisingArmsServo = new Servo(0);
		rightArm = new Solenoid(0);
		leftArm = new Solenoid(0);
		return false;
	}

	@Override
	protected void liveWindow() {
		LiveWindow.addActuator(getName(), "Release Arm", releasingArmsServo);
		LiveWindow.addActuator(getName(), "Raise Arms", raisingArmsServo);
		LiveWindow.addActuator(getName(), "Right Arm", rightArm);
		LiveWindow.addActuator(getName(), "Left Arm", leftArm);
		LiveWindow.addSensor(getName(), "Right Arm Touch", rightArmInCan);
		LiveWindow.addSensor(getName(), "Left Arm Touch", leftArmInCan);

	}

	@Override
	protected boolean execute() {
		if(DriverStation.getInstance().isOperatorControl() && 
				DriverStation.getInstance().isEnabled()){
			currentState = State.RESET_SERVO;
		}
		switch(currentState){
		case STANDBY:
			break;
		case DROP_ARMS:
			armsDrop();
			currentState = State.ATTEMPT_TO_GRAB;
			break;
		case ATTEMPT_TO_GRAB:
			//RIGHT
			if(rightArmInCan.get()){
				rightArm.set(GRAB);
				hasRight = true;
			}
			
			//LEFT
			if(leftArmInCan.get()){
				leftArm.set(GRAB);
				hasLeft = true;
			}
			
			if(hasRight && hasLeft){
				currentState = State.STANDBY;
			}
			break;
		case GRABBED:
			break;
		case RELEASE:
			rightArm.set(!GRAB);
			leftArm.set(!GRAB);
			currentState = State.STANDBY;
			break;
		case DISABLE:
			armsRaise();
			currentState = State.STANDBY;
			break;
		case RESET_SERVO:
			resetServo();
			break;
		default:
//			log.logMessage("INVALID STATE: " + currentState);
		}
		return false;
	}

	@Override
	protected long sleepTime() {
		return 10;
	}

	@Override
	protected void writeLog() {
		LOG.logMessage("Current state: " + State.getName(currentState));
	}

	public enum State{
		STANDBY,
		DROP_ARMS,
		ATTEMPT_TO_GRAB,
		GRABBED,
		RELEASE,
		DISABLE,
		RESET_SERVO;

		public static String getName(State state){
			switch(state){
			case STANDBY: 			return "Standby";
			case DROP_ARMS: 		return "Drop Arms";
			case ATTEMPT_TO_GRAB: 	return "Attempting to grab";
			case GRABBED: 			return "Both can's aquired";
			case RELEASE: 			return "Releasing cans";
			case DISABLE: 			return "Disabled";
			default: 				return "Unknown State: " + state;
			}
		}
	}		
}


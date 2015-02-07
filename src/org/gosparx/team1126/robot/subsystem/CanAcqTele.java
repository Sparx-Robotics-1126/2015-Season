package org.gosparx.team1126.robot.subsystem;

import org.gosparx.team1126.robot.IO;
import org.gosparx.team1126.robot.sensors.EncoderData;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * Controls the can tele acq
 * @author Alex Mechler {amechler1998@gmail.com}
 * @author Mike the Camel {Michaele789@gmail.com}
 */
public class CanAcqTele extends GenericSubsystem{

	/************************Objects**********************/

	/**
	 * Supports singleton
	 */
	private static CanAcqTele canAcq;

	/**
	 * The motor that rotates the arms
	 */
	private Talon rotateMotor;

	/**
	 * The motor that controls the hook
	 */
	private Talon hookMotor;

	/**
	 * The encoder that tracks the rotation
	 */
	private Encoder rotateEnc;

	/**	
	 * The encoder data for the rotation
	 */
	private EncoderData rotateEncData;

	/**
	 * The encoder for the hook
	 */
	private Encoder hookEnc;

	/**
	 * The encoder data for the hook
	 */
	private EncoderData hookEncData;

	/***********************Constants*********************/

	/**
	 * The distance the hook will travel per tick 
	 */
	private static final double DISTANCE_PER_TICK_HOOK = .0040614375;

	/**
	 * The distance the arms will rotate per tick 
	 */
	private static final double DISTANCE_PER_TICK_ROTATE = (1/112) / 256;

	/**
	 * The distance the can must go up per tote
	 */
	private static final double DISTANCE_PER_TOTE = 13.0;

	/**
	 * The minimum power for the motors to get when we are rotating up
	 */
	private static final double MIN_ROTATE_UP_SPEED = 0.4;

	/**
	 * the minimum power for the motors when we are rotating down
	 */
	private static final double MIN_ROTATE_DOWN_SPEED = 0.2;

	/**
	 * The minimum hook speed
	 */
	private static final double MIN_HOOK_SPEED = 0.4;
	
	/**
	 * The max position for the rotation
	 */
	private static final double MAX_ROTATION = 90;
	
	/**
	 * The max position for the hook
	 */
	private static final double MAX_HOOK_POS = 48.25;
	
	/***********************Variables*********************/

	/**
	 * the wanted angle of the arms
	 */
	private double wantedAngle;

	/**
	 * the wanted position of the hook
	 */
	private double wantedHookPos;

	/**
	 * the current state hook is in
	 */
	private HookState currentHookState;

	/**
	 * The current state rotate is in
	 */
	private RotateState currentRotateState;
	
	/**
	 * @return a CanAcqTele
	 */
	public static synchronized CanAcqTele getInstance(){
		if(canAcq == null){
			canAcq = new CanAcqTele();
		}
		return canAcq;
	}

	/**
	 * creates a new CanAcqTele
	 */
	private CanAcqTele() {
		super("CanAcqTele", Thread.NORM_PRIORITY);
	}

	/**
	 * Initializes things
	 */
	@Override
	protected boolean init() {
		rotateMotor = new Talon(IO.PWM_CAN_ROTATE);
		rotateEnc = new Encoder(IO.DIO_CAN_ROTATE_A, IO.DIO_CAN_ROTATE_B);
		rotateEncData = new EncoderData(rotateEnc, DISTANCE_PER_TICK_ROTATE);
		hookMotor = new Talon(IO.PWM_CAN_HOOK);
		hookEnc = new Encoder(IO.DIO_CAN_HOOK_A, IO.DIO_CAN_HOOK_B);
		hookEncData = new EncoderData(hookEnc, DISTANCE_PER_TICK_HOOK);
		currentHookState = HookState.STANDBY;
		currentRotateState = RotateState.STANDBY;
		return true;
	}

	/**
	 *Overrides in genericSubsytems, does things
	 */
	@Override
	protected void liveWindow() {
		LiveWindow.addActuator(getName(), "Rotating Victor", rotateMotor);
		LiveWindow.addActuator(getName(), "Hooking Victor", hookMotor);
		LiveWindow.addActuator(getName(), "Rotate Encoder", rotateEnc);
		LiveWindow.addActuator(getName(), "Hook Encoder", hookEnc);
	}

	/**
	 * Loops
	 */
	@Override
	protected boolean execute() {
		double wantedHookSpeed = 0;
		double wantedRotateSpeed = 0;
		switch(currentRotateState){
		case STANDBY:
			
			break;
		case ROTATING:
			double calculatedRotateSpeed = (wantedAngle - rotateEncData.getDistance()) / 45;
			if(calculatedRotateSpeed > 0){
				wantedRotateSpeed = ((Math.abs(calculatedRotateSpeed) > MIN_ROTATE_UP_SPEED) ? calculatedRotateSpeed : MIN_ROTATE_UP_SPEED);
			}else{
				wantedRotateSpeed = ((Math.abs(calculatedRotateSpeed) > MIN_ROTATE_DOWN_SPEED) ? calculatedRotateSpeed : -MIN_ROTATE_DOWN_SPEED);
			}
			if((rotateEncData.getDistance() >= wantedAngle && calculatedRotateSpeed < 0) || (rotateEncData.getDistance() <= wantedAngle && calculatedRotateSpeed > 0)){
				currentRotateState = RotateState.STANDBY;
				wantedRotateSpeed = 0;
				LOG.logMessage("Done rotating");
			}
			break;
		}
		switch(currentHookState){
		case STANDBY:
			
			break;
		case MOVING:
			double calculatedMovingSpeed = (wantedHookPos - hookEncData.getDistance()) / 15;
			if(calculatedMovingSpeed > 0){
				wantedRotateSpeed = (Math.abs(calculatedMovingSpeed) > MIN_HOOK_SPEED) ? calculatedMovingSpeed : MIN_HOOK_SPEED; 
			}else{
				wantedRotateSpeed = (Math.abs(calculatedMovingSpeed) > MIN_HOOK_SPEED) ? calculatedMovingSpeed : -MIN_HOOK_SPEED;
			}
			if((hookEncData.getDistance() >= wantedHookPos && calculatedMovingSpeed > 0) || (hookEncData.getDistance() <= wantedHookPos && calculatedMovingSpeed < 0)){
				wantedHookSpeed = 0;
				currentHookState = HookState.STANDBY;
				LOG.logMessage("Done moving hook");
			}
			break;
		}
		rotateMotor.set(wantedRotateSpeed);
		hookMotor.set(wantedHookSpeed);
		return false;
	}
	
	/**
	 * Goes to acquiring mode
	 */
	public void goToAcquire(){
		wantedHookPos = MAX_HOOK_POS;
		wantedAngle = MAX_ROTATION;
		currentHookState = HookState.MOVING;
		currentRotateState = RotateState.ROTATING;
	}
	
	/**
	 * Brings the can in
	 */
	public void acquireCan(){
		wantedHookPos = 0;
		wantedAngle = 0;
		currentHookState = HookState.MOVING;
		currentRotateState = RotateState.ROTATING;
	}
	
	/**
	 * Moves the can up DISTANCE_PER_TOTE
	 */
	public void acquiredTote(){
		wantedHookPos = hookEncData.getDistance() + DISTANCE_PER_TOTE;
		currentHookState = HookState.MOVING;
	}

	/**
	 * How long to sleep between loops
	 */
	@Override
	protected long sleepTime() {
		return 20;
	}

	/**
	 * Writes the info about the subsystem.
	 */
	@Override
	protected void writeLog() {
		LOG.logMessage("Current Hook State: " + currentHookState.toString());
		LOG.logMessage("Current Rotate State: " + currentRotateState.toString());
		LOG.logMessage("Wanted Angle: " + wantedAngle + " Current Angle: " + rotateEncData.getDistance());
		LOG.logMessage("Wanted Hook Pos: " + wantedHookPos + " Current Hook Pos: " + hookEncData.getDistance());
	}
	
	/**
	 * The states for the hook
	 * @author Mike
	 */
	public enum HookState{
		STANDBY,
		MOVING;

		public String toString(){
			switch(this){
			case STANDBY:
				return "In standby";
			case MOVING:
				return "Moving";
			default:
				return "Error" + this;
			}
		}
	}
	
	/**
	 * The states for rotating
	 * @author Mike
	 */
	public enum RotateState{
		STANDBY,
		ROTATING;

		public String toString(){
			switch(this){
			case STANDBY:
				return "In standby";
			case ROTATING:
				return "Rotating";
			default:
				return "Error" + this;
			}
		}
	}
}

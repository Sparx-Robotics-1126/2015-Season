package org.gosparx.team1126.robot.subsystem;

import org.gosparx.team1126.robot.IO;
import org.gosparx.team1126.robot.sensors.EncoderData;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * The class for controlling the acquisition
 * @author Mike the camel
 */
public class CanAcqTele extends GenericSubsystem{

	/**
	 * An instance of CanAcq to be used for the CanAcq class
	 */
	private CanAcqTele canAcq;
	
	/**
	 * The victor used to rotate the arms
	 */
	private Talon rotateTal;

	/**
	 * the victor used to control the hook
	 */
	private Talon hookTal;

	/**
	 * The encoder used for the hook acquisition
	 */
	private Encoder acqChanHook;

	/**
	 * the encoder used for the rotate acquisition
	 */
	private Encoder acqChanRotate;

	/**
	 * The encoder data for the acqChanHook encoder
	 */
	private EncoderData acqHookED;

	/**
	 * the encoder data for the acqChanRotate encoder
	 */
	private EncoderData acqRotateED;

	/**
	 * The distance the hook will travel per tick NOT FOR SURE - TODO find
	 */
	private static final double DISTANCE_PER_TICK_HOOK = 0.0;

	/**
	 * The distance the arms will rotate per tick NOT FOR SURE - TODO find
	 */
	private static final double DISTANCE_PER_TICK_ROTATE = 0.0;

	/**
	 * The rate the arms will lower and raise NOT FOR SURE
	 */
	private static final double MOTOR_SPEED = 0.3;

	

	/**
	 * The current state canAcq is in
	 */
	private State canAcqState = State.STANDBY;

	/**
	 * The distance the encoder traveled for the rotate
	 */
	private double rotateDistTravel = 0;

	/**
	 * The distance the encoder traveled for the hook
	 */
	private double hookDistTravel = 0;

	/**
	 * stops the motors
	 */
	private static final double STOP_MOTOR = 0.0;

	/**
	 * The distance left to travel for the hook
	 */
	private double hookDistLeft = 0;

	/**
	 * The distance left to travel for the rotate
	 */
	private double rotateDistLeft = 0;

	/**
	 * true if we acquired a tote false, not
	 */
	private boolean acqTote = false;

	/**
	 * The distance the arms will raise for every tote collected;
	 */
	private static final int DISTANCE_PER_TOTE = 13;

	/**
	 * if canAcq == null, make a canAcq
	 * @return the new drives
	 */
	public synchronized CanAcqTele getInstance(){
		if(canAcq == null){
			canAcq = new CanAcqTele();
		}
		return canAcq;
	}

	/**
	 * The constructor for the CanAcqTele class, creates a new canAcq with Thread.NORM_PRIORITY
	 */
	public CanAcqTele() {
		super("CanAcq", Thread.NORM_PRIORITY);
	}

	/**
	 * instantiates all the objects
	 * @return if false, keep looping, true loop ends
	 */
	@Override
	protected boolean init() {
		rotateTal = new Talon(IO.PWM_CAN_ROTATE);
		hookTal = new Talon(IO.PWM_CAN_HOOK);
		acqChanRotate = new Encoder(IO.DIO_CAN_ROTATE_A, IO.DIO_CAN_ROTATE_B);
		acqChanHook = new Encoder(IO.DIO_CAN_HOOK_A, IO.DIO_CAN_HOOK_B);
		acqRotateED = new EncoderData(acqChanRotate, DISTANCE_PER_TICK_ROTATE);
		acqHookED = new EncoderData(acqChanHook, DISTANCE_PER_TICK_HOOK);

		return true;
	}
	/**
	 *Overrides in genericSubsytems, does things
	 */
	@Override
	protected void liveWindow() {
		String subsystem = "CanAcq";
		LiveWindow.addActuator(subsystem, "Rotating Victor", rotateTal);
		LiveWindow.addActuator(subsystem, "Hooking Victor", hookTal);
	}
	/**
	 * determines if the can arms are being used
	 * @return if false, keep looping, true end loop
	 */
	@Override
	protected boolean execute() {
		switch(canAcqState){
		case LOWERING_ARMS:
			rotateTal.set(MOTOR_SPEED);
			rotateDistTravel = acqRotateED.getDistance();
			canAcqState = State.LOWERING_HOOK;
			break;
		case RAISING_ARMS:
			if(acqTote){
				if(rotateDistLeft < DISTANCE_PER_TOTE){
					rotateTal.set(-MOTOR_SPEED);
				}else {
					rotateTal.set(STOP_MOTOR);
					canAcqState = State.STANDBY;
					reset(false);
				}
				}else if(rotateDistLeft < rotateDistTravel){
				rotateTal.set(-MOTOR_SPEED);
				rotateDistTravel = acqRotateED.getDistance();
			}else{
				rotateTal.set(STOP_MOTOR);
				canAcqState = State.STANDBY;
				reset(false);
			}
			break;
		case LOWERING_HOOK:
			hookTal.set(MOTOR_SPEED);
			hookDistTravel = acqHookED.getDistance();
			canAcqState = State.STANDBY;
			break;
		case RAISING_HOOK:
			if(hookDistLeft <= hookDistTravel ){
				hookTal.set(-MOTOR_SPEED);
			}else {
				rotateTal.set(STOP_MOTOR);
				canAcqState = State.RAISING_ARMS;
				reset(true);
			}
			break;
		case STANDBY:
			LOG.logMessage("In Standby");
			break;
		default:
			LOG.logError("Error");
		}
		return false;
	}
	/**
	 * Time rested, in milliseconds
	 */
	@Override
	protected long sleepTime() {
		return 15;
	}

	/**
	 * logs data to the rio
	 */
	@Override
	protected void writeLog() {
		LOG.logMessage("Current State: " + canAcqState);
	}

	/**
	 * Sets the wanted state to the acutal state
	 * @param wantedAcqState
	 */
	public void setAcqState(State wantedAcqState){
		canAcqState = wantedAcqState;
	}

	/**
	 * every time a tote is collected it sets the case to tote collected
	 */
	public void aquiredNewTote(){
		acqTote = true;
		canAcqState = State.RAISING_ARMS;
	}

	/**
	 * @param hook if the hook or the rotater is getting reset
	 * resets all the encoders, and distances
	 */
	public void reset(boolean hook){
		if(hook){
			acqChanHook.reset();
			hookDistLeft = 0;
		}else {
			rotateDistLeft = 0;
			acqChanRotate.reset();
		}
	}

	/**
	 * The states for CanAcq
	 *@author Mike the camel
	 */
	public enum State{
		RAISING_ARMS,
		LOWERING_ARMS,
		RAISING_HOOK,
		LOWERING_HOOK,
		STANDBY;

		/**
		 * Gets the name of the state
		 * @return the correct state
		 */
		@Override
		public String toString(){
			switch(this){
			case RAISING_ARMS:
				return "Raising arms";
			case LOWERING_ARMS:
				return "Lowering arms";
			case RAISING_HOOK:
				return "Raising hook";
			case LOWERING_HOOK:
				return "Lowering hook";
			case STANDBY:
				return "Standby";
			default:
				return "Error";
			}
		}
	}

}
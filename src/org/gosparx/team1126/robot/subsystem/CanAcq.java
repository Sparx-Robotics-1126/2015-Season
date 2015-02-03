package org.gosparx.team1126.robot.subsystem;

import org.gosparx.team1126.robot.IO;
import org.gosparx.team1126.robot.sensors.EncoderData;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;

/**
 * The class for controlling the aqusition
 * @author Mike the camel
 */
public class CanAcq extends GenericSubsystem{

	/**
	 * The victor used to rotate the arms
	 */
	private Victor rotateVic;
	
	/**
	 * the victor used to control the hook
	 */
	private Victor hookVic;

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
	 * The distance the hook will travel per tick NOT FOR SURE
	 */
	private static final double DISTANCE_PER_TICK_HOOK = 0.0;

	/**
	 * The distance the arms will rotate per tick NOT FOR SURE
	 */
	private static final double DISTANCE_PER_TICK_ROTATE = 0.0;

	/**
	 * The rate the arms will lower and raise NOT FOR SURE
	 */
	private static final double MOTOR_SPEED = 0.3;

	/**
	 * An instance of CanAcq to be used for the CanAqc class
	 */
	private CanAcq canAcq;

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

	public synchronized CanAcq getInstance(){
		if(canAcq == null){
			canAcq = new CanAcq();
		}
		return canAcq;
	}

	public CanAcq() {
		super("CanAcq", Thread.NORM_PRIORITY);
	}

	@Override
	protected boolean init() {
		rotateVic = new Victor(IO.PWM_CAN_ROTATE);
		hookVic = new Victor(IO.PWM_CAN_HOOK);
		acqChanRotate = new Encoder(IO.DIO_CAN_ROTATE_A, IO.DIO_CAN_ROTATE_B);
		acqChanHook = new Encoder(IO.DIO_CAN_HOOK_A, IO.DIO_CAN_HOOK_B);
		acqRotateED = new EncoderData(acqChanRotate, DISTANCE_PER_TICK_ROTATE);
		acqHookED = new EncoderData(acqChanHook, DISTANCE_PER_TICK_HOOK);

		return true;
	}
	/**
	 * Displays stuff to make alex cool
	 */
	@Override
	protected void liveWindow() {
		// TODO Auto-generated method stub

	}
	/**
	 * determines if the can arms are being used
	 * @return if false, keep looping, true end loop
	 */
	@Override
	protected boolean execute() {
		switch(canAcqState){
		case LOWERING_ARMS:
			rotateVic.set(MOTOR_SPEED);
			rotateDistTravel = acqRotateED.getDistance();
			break;
		case RAISING_ARMS:
			if(rotateDistLeft <= rotateDistTravel){
				rotateVic.set(-MOTOR_SPEED);
			}else{
				rotateVic.set(STOP_MOTOR);
				canAcqState = State.RAISING_ARMS;
			}
			break;
		case LOWERING_HOOK:
			hookVic.set(MOTOR_SPEED);
			hookDistTravel = acqHookED.getDistance();
			break;
		case RAISING_HOOK:
			if(hookDistLeft <= hookDistTravel ){
				hookVic.set(MOTOR_SPEED);
			}else {
				rotateVic.set(STOP_MOTOR);
			}
		case STANDBY:
			LOG.logMessage("In Standby");
			break;
		default:
			LOG.logError("Error");
		}
		return true;
	}
	/**
	 * Time rested, in milliseconds
	 */
	@Override
	protected long sleepTime() {
		// TODO Auto-generated method stub
		return 0;
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
	public void SetAcqState(State wantedAcqState){
		canAcqState = wantedAcqState;
	}
	/**
	 * @author Mike the camel
	 * The states for CanAcq
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

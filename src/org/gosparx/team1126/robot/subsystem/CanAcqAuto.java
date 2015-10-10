package org.gosparx.team1126.robot.subsystem;

import org.gosparx.team1126.robot.IO;
import org.gosparx.team1126.robot.sensors.MagnetSensor;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * A class for controlling the can auto arms we are adding
 * @author Alex Mechler {amechler1998@gmail.com}
 */
public class CanAcqAuto extends GenericSubsystem{

	/**
	 * A solenoid that holds the arms
	 */
	private Solenoid armHolder;

	/**
	 * Bob likes to determine when to stop moving the arms
	 */
	private MagnetSensor limitSwitch;

	/**
	 * The victor that moves the arm up
	 */
	private Victor armController;

	/**
	 * The current state the arms are in
	 */
	private State currentState;

	/**
	 * The time that we started to move the arms back up, in seconds
	 */
	private double raiseTime;
	
	/**
	 * An instance of this class to support the singleton model 
	 */
	private static CanAcqAuto canAcqAuto;

	/**
	 * The power to raise the can auto arms at
	 */
	private static final double RAISE_POWER = 1.0;

	/**
	 * The time in seconds from when we start raising the arms to when we assume the limit switch has failed or motor has stalled
	 */
	private static final double TIMEOUT = 5.0;

	/**
	 * Singleton
	 * @return the only instance of CanAcqAuto ever
	 */
	public static CanAcqAuto getInstance(){
		if(canAcqAuto == null){
			canAcqAuto = new CanAcqAuto();
		}
		return canAcqAuto;
	}

	/**
	 * Creates a new CanAcqAuto
	 */
	private CanAcqAuto() {
		super("CanAcqAuto", Thread.NORM_PRIORITY);
	}

	/**
	 * Initializes everything
	 */
	@Override
	protected boolean init() {
		armHolder = new Solenoid(IO.PNU_CAN_ARM_CONTROLLER);
		limitSwitch = new MagnetSensor(IO.DIO_CAN_ARM_CONTROLLER, false);
		armController = new Victor(IO.PWM_CAN_ARM_CONTROLLER);
		currentState = State.STANDBY;
		return false;
	}
	
	/**
	 * Loops
	 */
	@Override
	protected boolean execute() {
		switch(currentState){
		case ARMS_DROPPING:
			armHolder.set(false);
			currentState = State.STANDBY;
			break;
		case ARMS_HELD:
			armHolder.set(true);
			currentState = State.STANDBY;
			break;
		case ARMS_RAISING:
			if(!limitSwitch.isTripped()){
				armController.set(RAISE_POWER);
				if(Timer.getFPGATimestamp() - TIMEOUT >= raiseTime){
					armController.set(0);
					currentState = State.ARMS_HELD;		
					LOG.logError("Timeout reached on arms, firing pnus");
				}
			}else{
				armController.set(0);
				currentState = State.ARMS_HELD;		
				LOG.logMessage("Limit Switch tripped, firing pnus");
			}
			break;
		case STANDBY:
			break;
		default:
			LOG.logError("Unknown State: " + currentState);
			break;
		}
		return false;
	}

	/**
	 * Drops the CanAcqAuto arms into the cans
	 */
	public void dropArms(){
		currentState = State.ARMS_DROPPING;
		LOG.logMessage("Attempting to drop arms");
	}

	/**
	 * Raises the arms back to the upper position
	 */
	public void raiseArms(){
		currentState = State.ARMS_RAISING;
		raiseTime = Timer.getFPGATimestamp();
		LOG.logMessage("Raising Arms");
	}

	/**
	 * How long to wait in ms between execute() calls
	 */
	@Override
	protected long sleepTime() {
		return 20;
	}

	/**
	 * Logs info every 5 seconds
	 */
	@Override
	protected void writeLog() {
		LOG.logMessage("Current State: " + currentState);
	}
	
	/**
	 * Add things to the LiveWindow
	 */
	@Override
	protected void liveWindow() {
		LiveWindow.addActuator(getName(), "Holder", armHolder);
		LiveWindow.addActuator(getName(), "Window Motor", armController);
	}

	/**
	 * Any possible state of the subsystem is stored here
	 */
	private enum State{
		ARMS_HELD,
		ARMS_RAISING,
		ARMS_DROPPING,
		STANDBY;

		/**
		 * @return a friendly name of the state
		 */
		@Override
		public String toString(){
			switch(this){
			case ARMS_HELD:			return("Arms Held");
			case ARMS_RAISING: 		return("Arms Raising");
			case ARMS_DROPPING: 	return("Arms Dropped");
			case STANDBY: 			return("Standby");
			default: 				return("Error not in a state");
			}
		}
	}
}

package org.gosparx.team1126.robot.subsystem;

import org.gosparx.team1126.robot.IO;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * A class for controlling the can auto arms we are adding
 * @author Mike Edwards {michaele789@gmail.com}
 * @author Alex Mechler {amechler1998@gmail.com}
 */
public class CanAcqAuto extends GenericSubsystem{

	/**
	 * A solenoid that holds the arms
	 */
	private Solenoid armHolder;
	
	/**
	 * The current state the arms are in
	 */
	private State currentState;
	
	/**
	 * An instance of this class to support the singleton model 
	 */
	private static CanAcqAuto canAcqAuto;

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
		currentState = State.ARMS_UP;
		return false;
	}
	
	/**
	 * Loops
	 */
	@Override
	protected boolean execute() {
		switch(currentState){
		case ARMS_UP:
			armHolder.set(false);
			break;
		case ARMS_DOWN:
			armHolder.set(true);
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
		currentState = State.ARMS_DOWN;
		LOG.logMessage("Attempting to drop arms");
	}

	/**
	 * Raises the arms back to the upper position
	 */
	public void raiseArms(){
		currentState = State.ARMS_UP;
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
		LiveWindow.addActuator(getName(), "Pnus", armHolder);
	}

	/**
	 * Any possible state of the subsystem is stored here
	 */
	private enum State{
		ARMS_UP,
		ARMS_DOWN;

		/**
		 * @return a friendly name of the state
		 */
		@Override
		public String toString(){
			switch(this){
			case ARMS_UP:			return("Arms Up");
			case ARMS_DOWN: 		return("Arms Down");
			default: 				return("Error not in a state");
			}
		}
	}
}

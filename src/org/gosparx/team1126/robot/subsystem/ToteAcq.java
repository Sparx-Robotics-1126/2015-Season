package org.gosparx.team1126.robot.subsystem;

import org.gosparx.team1126.robot.IO;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * A class for manipulating the tote acquisition system. 
 * @author Alex
 */
public class ToteAcq extends GenericSubsystem{

	/**
	 * Supports singleton
	 */
	private static ToteAcq acq;

	/**
	 * The solenoid that controls if the rollers are on the ground or at the human player
	 */
	private Solenoid longCylinder;

	/**
	 * The second solenoid that controls the rollers
	 */
	private Solenoid shortCylinder;

	/**
	 * The solenoid that controls the clutch
	 */
	private Solenoid clutch;

	/**
	 * The solenoid that stops the totes from shooting out the back
	 */
	private Solenoid stopper;

	/**
	 * The current position of the rollers
	 */
	private RollerPosition currPos;

	/**
	 * The current state of the clutch
	 */
	private ClutchState currState;

	/**
	 * The current state of the stopper
	 */
	private StopState currStop;

	/**
	 * the value of the solenoid if the rollers are up
	 */
	private static final boolean IN = false;

	/**
	 * the value of the solenoid if the rollers are down
	 */
	private static final boolean OUT = !IN;

	/**
	 * The value of the solenoid when the clutch is engaged
	 */
	private static final boolean CLUTCH_ENGAGED = true;

	/**
	 * the value of the solenoid when the clutch is disengaged 
	 */
	private static final boolean CLUTCH_DISENGAGED = !CLUTCH_ENGAGED;

	/**
	 * The value of the solenoid when the stopper is engaged
	 */
	private static final boolean STOPPER_ENGAGED = false;

	/**
	 * the value of the solenoid when the stopper is disengaged 
	 */
	private static final boolean STOPPER_DISENGAGED = !STOPPER_ENGAGED;

	/**
	 * Supports singleton
	 * @return The only instance of ToteAcq ever
	 */
	public static synchronized ToteAcq getInstance(){
		if(acq == null)
			acq = new ToteAcq();
		return acq;
	}

	/**
	 * Creates a new ToteAcq
	 */
	private ToteAcq() {
		super("ToteAcq", Thread.NORM_PRIORITY);
	}

	/**
	 * Initiates things
	 */
	@Override
	protected boolean init() {
		longCylinder = new Solenoid(IO.PNU_ACQ_TRAVEL);
		shortCylinder = new Solenoid(IO.PNU_ACQ_TOTE);
		stopper = new Solenoid(IO.PNU_TOTE_STOP);
		clutch = new Solenoid(IO.PNU_ACQ_CLUTCH);
		currPos = RollerPosition.TRAVEL;
		currState = ClutchState.OFF;
		currStop = StopState.ON;
		return true;
	}

	/**
	 * loops
	 */
	@Override
	protected boolean execute() {
		switch (currPos) {
		case FLOOR:
			longCylinder.set(OUT);
			shortCylinder.set(OUT);
			break;
		case HUMAN_PLAYER:
			shortCylinder.set(OUT);
			longCylinder.set(IN);
			break;
		case TRAVEL:
			longCylinder.set(IN);
			shortCylinder.set(IN);
			break;
		}
		switch (currState){
		case ON:
			clutch.set(CLUTCH_ENGAGED);
			break;
		case OFF:
			clutch.set(CLUTCH_DISENGAGED);
			break;
		}
		switch (currStop) {
		case ON:
			stopper.set(STOPPER_ENGAGED);
			break;
		case OFF:
			stopper.set(STOPPER_DISENGAGED);
			break;
		}
		return false;

	}

	/**
	 * How long to sleep in ms
	 */
	@Override
	protected long sleepTime() {
		return 20;
	}

	/**
	 * Writes info about the subsystem
	 */
	@Override
	protected void writeLog() {
		LOG.logMessage("Current State: " + currState);
	}

	/**
	 * Set the Clutch on or off
	 * @param state the desired clutchState
	 */
	public void setClutch(ClutchState state){
		currState = state;
	}

	/**
	 * Set the stopper on or off
	 * @param state the desired StopState
	 */
	public void setStopper(StopState state){
		currStop = state;
	}

	/**
	 * Sets the roller position
	 * @param pos the desired position
	 */
	public void setRollerPos(RollerPosition pos){
		currPos = pos;
	}

	/**
	 * A enum of all of the posible roller positions 
	 */
	public enum RollerPosition{
		TRAVEL,
		HUMAN_PLAYER,
		FLOOR;

		/**
		 * @return a string name of the enum
		 */
		public String getName(){
			switch (this) {
			case TRAVEL:
				return "Travel";
			case HUMAN_PLAYER:
				return "Human Player";
			case FLOOR:
				return "Floor";
			default:
				return "Unknown Roller Position";
			}
		}
	}

	/**
	 * A enum for if the rollers are on or off
	 */
	public enum ClutchState{
		ON,
		OFF;

		/**
		 * @return a string name of the enum
		 */
		public String getName(){
			switch (this) {
			case ON:
				return "Rollers On";
			case OFF:
				return "Rollers Off";
			default:
				return "Unknown Roller State";
			}
		}
	}

	/**
	 * The possible states for the stop
	 */
	public enum StopState{
		ON,
		OFF;

		/**
		 * @return a string name of the enum
		 */
		public String getName(){
			switch(this){
			case ON:
				return "Totes stopped";
			case OFF:
				return "Totes not stopped";
			default:
				return "Unknown State";
			}
		}
	}

	@Override
	protected void liveWindow() {
		LiveWindow.addActuator(getName(), "Long Cylinder", longCylinder);
		LiveWindow.addActuator(getName(), "Short Cylinder", shortCylinder);
		LiveWindow.addActuator(getName(), "Clutch", clutch);
		LiveWindow.addActuator(getName(), "Stopper", stopper);
	}
}
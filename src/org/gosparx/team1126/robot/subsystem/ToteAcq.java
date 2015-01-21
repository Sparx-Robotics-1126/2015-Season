package org.gosparx.team1126.robot.subsystem;

import org.gosparx.team1126.robot.IO;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;

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
	private Solenoid rollerRaiser;
	
	/**
	 * The victor that controls the left roller motor
	 */
	private Victor leftRollers;
	
	/**
	 * The victor that controls the right roller motor
	 */
	private Victor rightRollers;
	
	/**
	 * The current position of the rollers
	 */
	private RollerPosition currPos;
	
	/**
	 * If the rollers are on or off
	 */
	private RollerState currState;
	
	/**
	 * the value of the solenoid if the rollers are up
	 */
	private static final boolean ROLLERS_UP = true;
	
	/**
	 * the value of the solenoid if the rollers are down
	 */
	private static final boolean ROLLERS_DOWN = false;
	
	/**
	 * The value to send to the roller victors if they are acquiring
	 */
	private static final double ROLLERS_ON = 1.0;
	
	/**
	 * the value to send to the roller victors if they are off
	 */
	private static final double ROLLERS_OFF = 0.0;

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
		rollerRaiser = new Solenoid(IO.PNU_ACQ_TOTE);
		leftRollers = new Victor(IO.PWM_ACQ_TOTES_LEFT);
		rightRollers = new Victor(IO.PWM_ACQ_TOTES_RIGHT);
		currPos = RollerPosition.POS_UP;
		currState = RollerState.ON;
		return true;
	}

	/**
	 * loops
	 */
	@Override
	protected boolean execute() {
		double wantedRollerSpeed = ROLLERS_OFF;
		switch (currPos) {
		case POS_UP:
			break;
		case POS_DOWN:
			break;
		case GOING_UP:
			rollerRaiser.set(ROLLERS_UP);
			currPos = RollerPosition.POS_UP;
			break;
		case GOING_DOWN:
			rollerRaiser.set(ROLLERS_DOWN);
			currPos = RollerPosition.POS_DOWN;
			break;
		}
		switch (currState) {
		case ON:
			wantedRollerSpeed = ROLLERS_ON;
			break;
		case OFF: 
			wantedRollerSpeed = ROLLERS_OFF;
			break;
		}
		leftRollers.set(wantedRollerSpeed);
		rightRollers.set(wantedRollerSpeed);
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

	}

	/**
	 * Set the rollers on or off
	 * @param on If the rollers should be on
	 */
	public void setRollers(boolean on){
		if(on){
			currState = RollerState.ON;
		}else{
			currState = RollerState.OFF;
		}
	}

	/**
	 * Moves the rollers up if they already are not
	 */
	public void raiseRollers(){
		if(currPos != RollerPosition.POS_UP && currPos != RollerPosition.GOING_UP){
			currPos = RollerPosition.GOING_UP;
		}
	}
	
	/**
	 * Moves the rollers down if they already are not
	 */
	public void lowerRollers(){
		if(currPos != RollerPosition.POS_DOWN && currPos != RollerPosition.GOING_DOWN){
			currPos = RollerPosition.GOING_DOWN;
		}
	}
	
	/**
	 * A enum of all of the posible roller positions 
	 */
	public enum RollerPosition{
		POS_UP,
		POS_DOWN,
		GOING_UP,
		GOING_DOWN;

		/**
		 * @return a string name of the enum
		 */
		public String getName(){
			switch (this) {
			case POS_UP:
				return "Rollers Up";
			case POS_DOWN:
				return "Rollers Down";
			case GOING_UP:
				return "Going Up";
			case GOING_DOWN:
				return "Going Down";
			default:
				return "Unknown Roller Position";
			}
		}
	}
	
	/**
	 * A enum for if the rollers are on or off
	 */
	public enum RollerState{
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
}
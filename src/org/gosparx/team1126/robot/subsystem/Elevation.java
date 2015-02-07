package org.gosparx.team1126.robot.subsystem;

import org.gosparx.team1126.robot.IO;
import org.gosparx.sensors.EncoderData;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * Version 1.0 Season 2015
 * @author Andrew Thompson {andrewt015@gmail.com}
 * @author Reizwan Chowdhury
 * @author Raza Ahmed
 */

public class Elevation extends GenericSubsystem{
	//***************************Instants***************************
	
	/**
	 * This is the elevation single instance
	 */
	private static Elevation elevation;
	
	//***************************Constants***************************
	
	/**
	 * the speed of the elevation up
	 */
	private static final double UP_SPEED = 0.5; //TODO find best speed
	
	/**
	 * One revolution travels .125 inches. There is total of 256 ticks per revolution.
	 */
	private static final double DIST_PER_TICK = 0.125/256;
	
	/**
	 * Threshold for tote distance 
	 */
	private static final double TOTE_THRESHOLD = 0.1; //TODO Test if this is enough clearance
	
	/**
	 * The holding position shouldn't go above or below .5 inch
	 */
	private static final double HOLDING_THRESHOLD = 0.5;
	
	/**
	 * this is the holding current
	 */
	private static final double DOWN_SPEED = -0.5; //TODO find best speed

	/**
	 * distance in inches to raise elevation to acquire tote
	 */
	private static final double TOTE_CLEARANCE_DISTANCE = 13;

	/**
	 * Distance where tote is acquired
	 */
	private static final double TOTE_LIP_ACQUIRED = 11;

	/**
	 * distance the elevator moves if it has one tote
	 */
	private static final double HAS_ONE_TOTE_DISTANCE = 24;

	/**
	 * Little bit of extra distance to clear the tote
	 */
	private static final double TOTE_CLEARANCE_THRESHOLD = .5; //TODO find out for adjustment
	
	//***************************Talons***************************
	
	/**
	 * this is the left motors
	 */
	private Talon leftElevation;
	
	/**
	 * this is the right motors
	 */
	private Talon rightElevation;
	
	//***************************Sensors***************************
	
	/**
	 * Encoder for the elevation mechanism
	 */
	private Encoder elevationEncoder;
	
	/**
	 * This is the encoder data to translate the encoder into distance.
	 */
	private EncoderData elevationEncoderData; 
	
	/**
	 * This represents the current state of Elevation
	 */
	private State elevationState; 
	
	/**
	 * sensor for detecting the staring position for acquiring a tote
	 */
	private DigitalInput homeSwitch;
	
	//***************************Variables***************************
	
	/**
	 * This is the distance we are going to lift or drop
	 */
	private double targetDistance; 
	
	/**
	 * threshold for encoder
	 */
	private double encoderThreshold; 
	
	/**
	 * variable set to true when lifting a tote
	 */
	private boolean liftingTote;
	
	/**
	 * Right Elevation power
	 */
	private double rightPower;
	
	/**
	 * LEft Elevation power;
	 */
	private double leftPower;
	
	/**
	 * this is the constructor of the Elevation
	 */
	private Elevation() {
		super("Elevation", Thread.NORM_PRIORITY);
	}
	
	/**
	 * Checks to see if standby or holding position
	 */
	public boolean isDone () {
		return (elevationState == State.STANDBY || elevationState == State.HOLD_POSITION);
	}
	
	/**
	 * When scoring sets the elevator to tote clearance
	 */
	public void goToScoring() {
		moveElevator(TOTE_CLEARANCE_DISTANCE, 0);
	}
	
	/**
	 * gets 1 instance of elevation
	 * @return the instance of elevation
	 */
	public static synchronized Elevation getInstance(){
		if(elevation == null){
			elevation = new Elevation();
		}
		return elevation;
	}
	
	/**
	 * Called by Generic Subsystem to initialize
	 */
	protected boolean init() {
		leftElevation = new Talon(IO.PWM_LEFT_ELEVATION);
		rightElevation = new Talon(IO.PWM_RIGHT_ELEVATION);
		elevationEncoder = new Encoder(IO.DIO_ELEVATIONS_A, IO.DIO_ELEVATIONS_B);
		elevationEncoderData = new EncoderData(elevationEncoder, DIST_PER_TICK);
		homeSwitch = new DigitalInput(IO.DIO_ELEVATIONS_ORIGIN);
		elevationState = State.STANDBY;
		targetDistance = 0;
		encoderThreshold = 0;
		if (!homeSwitch.get()){
			findHome();
		}
		return true;
	}
	
	/**
	 * Put things on the live window
	 */
	@Override
	protected void liveWindow() {
		LiveWindow.addActuator(getName(), "Right Elevation", rightElevation);
		LiveWindow.addActuator(getName(), "Left Elevation", leftElevation);
		LiveWindow.addActuator(getName(), "Elevation Encoder", elevationEncoder);	
	}

	/**
	 * Main loop
	 */
	protected boolean execute() {     
		switch(elevationState){
			case STANDBY:
				leftPower = 0;
				rightPower = 0;
				break;
			case LIFTING:  
				if (elevationEncoderData.getDistance() >= targetDistance + encoderThreshold) {
					leftElevation.set(0);
					rightElevation.set(0);
					if (liftingTote) { 
						liftingTote = false;
						moveElevator(HAS_ONE_TOTE_DISTANCE, TOTE_CLEARANCE_THRESHOLD);
					}
					else {
						elevationState = State.HOLD_POSITION;
					}
				}
				break;
			case LOWERING:
				if (elevationEncoderData.getDistance() <= targetDistance + encoderThreshold) {
					leftElevation.set(0);
					rightElevation.set(0);
					elevationState = State.STANDBY;
				}
				break;
			case HOLD_POSITION:
				if ((elevationEncoderData.getDistance() >= targetDistance + HOLDING_THRESHOLD)||
					(elevationEncoderData.getDistance() <= targetDistance - HOLDING_THRESHOLD)){
					moveElevator(targetDistance, TOTE_THRESHOLD);
				}
				break;
			case RETURNING_HOME:
				if (homeSwitch.get()) {
					leftElevation.set(0);
					rightElevation.set(0);
					moveElevator(TOTE_CLEARANCE_DISTANCE, 0);
				}
				break;
			default:
				LOG.logError("Elevation Error");
				break;
		}
		return true;
	}
	@Override
	protected long sleepTime() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	protected void writeLog() {   
		LOG.logMessage("Elevation State:" + elevationState);
		LOG.logMessage("Lift Distance" + elevationEncoderData);
		LOG.logMessage("Hold Distance" + targetDistance);
		LOG.logMessage("Elevation Starting Position" + homeSwitch);
	}
	
	/**
	 * This lifts the tote enough that another tote fits under it
	 */
	public void liftTote(){
		liftingTote = true;
		moveElevator(TOTE_LIP_ACQUIRED, 0);
	}
	
	/**
	 * Find home
	 */
	private void findHome() {
		leftElevation.set(DOWN_SPEED);
		rightElevation.set(DOWN_SPEED);
		elevationState = State.RETURNING_HOME;
	}
	
	/**
	 * Sends the elevation mechanism up or down to the holding distance
	 */
	private void moveElevator(double distance, double threshold) {
		if (elevationEncoderData.getDistance() <= targetDistance) {
			leftElevation.set(UP_SPEED);
			rightElevation.set(UP_SPEED);
			elevationState = State.LIFTING;
		}
		else {
			leftElevation.set(DOWN_SPEED);
			rightElevation.set(DOWN_SPEED);
			elevationState = State.LOWERING;
		}

		targetDistance = distance;
		encoderThreshold = threshold;
	}
	
	/**
	 * This lowers the totes
	 */	
	public void lowerTote(){
		if (!homeSwitch.get()){
			leftElevation.set(DOWN_SPEED);
			rightElevation.set(DOWN_SPEED);
			elevationState = State.RETURNING_HOME;
		}
	}
	
	/**
	 *Makes the states for elevation
	 */
	public enum State{
		STANDBY,
		LIFTING,
		LOWERING,
		HOLD_POSITION,
		RETURNING_HOME;
		
		/**
		 * Gets the name of the state
		 * @return the correct state 
		 */
		public String toString(){
			switch(this){
			case STANDBY:
				return "In standby";
			case LIFTING:
				return "Lifting"; 
			case LOWERING:
				return "Lowering";
			case HOLD_POSITION:
				return "Holding Position";
			case RETURNING_HOME:
				return "Returning Home";
			default:
				return "Error";
			}
		}
	}
	
}
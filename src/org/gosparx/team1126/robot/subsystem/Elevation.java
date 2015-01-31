package org.gosparx.team1126.robot.subsystem;

import org.gosparx.team1126.robot.IO;
import org.gosparx.sensors.EncoderData;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * @author Andrew Thompson {andrewt015@gmail.com}
 * @author Reizwan Chowdhury
 * @author Raza Ahmed
 * Version 1.0 Season 2015
 */

public class Elevation extends GenericSubsystem{
	//***************************Instants***************************
	
	/**
	 * This is the elevation single instance
	 */
	private static Elevation elevation;
	
	/**
	 * When scoring sets the elvator to tote clearence
	 */
	public void goToScoring() {
		moveElevator(TOTE_CLEARANCE_DISTANCE, 0);
	}
	
	//***************************Constants***************************
	
	/**
	 * the speed of the elevation up
	 */
	private static final double UP_SPEED = 0.5; //TODO find best speed
	
	/**
	 * the holding speed
	 */
	private static final double HOLDING_SPEED = 0.2; //TODO validate this speed with 5 totes
	
	/**
	 * One revolution travels .125 inches. There is total of 256 ticks per revolution.
	 */
	private static final double DIST_PER_TICK = 0.125/256;
	
	/**
	 * Checks to see if idle or holding position
	 */
	public boolean isDone () {
		return (elevationState == State.IDLE || elevationState == State.HOLD_POSITION);
	}
	
	/**
	 * the tote is 12 inches in height
	 */
	private static final double TOTE_DISTANCE_CLEARED = 12;
	
	/**
	 * Threshhold for tote distance 
	 */
	private static final double TOTE_THRESHOLD = 0.1; //TODO Test if this is enough clearance
	
	/**
	 * variable set to true when lifting a tote
	 */
	boolean liftingTote;
	
	/**
	 * The holding position shouldn't go above or below .5 inch
	 */
	private static final double HOLDING_THRESHHOLD = 0.5;
	
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
	private static final double TOTE_CLEARANCE_THRESHOLD = 0;
	
	//***************************Victors***************************
	
	/**
	 * this is the left motors
	 */
	private Victor leftElevation;
	
	/**
	 * this is the right motors
	 */
	private Victor rightElevation;
	
	/**
	 * this is the encoder for elevations 
	 */
	
	//***************************Sensors***************************
	
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
	private double holdDistance;
	
	/**
	 * threshold for encoder
	 */
	private double encoderThreshold;
	
	/**
	 * this is the constructor of the Elevation
	 */
	private Elevation() {
		super("Elevation", Thread.NORM_PRIORITY);
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
		leftElevation = new Victor(IO.PWM_LEFT_ELEVATION);
		rightElevation = new Victor(IO.PWM_RIGHT_ELEVATION);
		elevationEncoder = new Encoder(IO.ENCODER_ELEVATION_A, IO.ENCODER_ELEVATION_B);
		elevationEncoderData = new EncoderData(elevationEncoder, DIST_PER_TICK);
		elevationState = State.IDLE;
		holdDistance = 0;
		encoderThreshold = 0;
		homeSwitch = new DigitalInput(IO.SWITCH_ELEVATIONS_RIGHT);
		if (homeSwitch.get()) {
		}
		else {
			findHome();
		}
		return true;
	}
	/**
	 * Put things on the live window
	 */
	@Override
	protected void liveWindow() {
		String subsystemName = "Elevation";
		LiveWindow.addActuator(subsystemName, "rightElevation", rightElevation);
		LiveWindow.addActuator(subsystemName, "leftElevation", leftElevation);
		LiveWindow.addActuator(subsystemName, "elevationEncoder", elevationEncoder);
		
	}

	/**
	 * Main loop
	 */
	
	protected boolean execute() {
		switch(elevationState){
			case IDLE:
				break;
			case MOVING:
				if (elevationEncoderData.getDistance() >= holdDistance + encoderThreshold) {
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
			case HOLD_POSITION:
				if ((elevationEncoderData.getDistance() >= holdDistance + HOLDING_THRESHHOLD)||
					(elevationEncoderData.getDistance() <= holdDistance - HOLDING_THRESHHOLD)){
					moveElevator(TOTE_DISTANCE_CLEARED, TOTE_THRESHOLD);
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
		// TODO Auto-generated method stub
		
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
	private void moveElevator(double targetDistance, double threshold) {
		if (elevationEncoderData.getDistance() <= targetDistance) {
			leftElevation.set(UP_SPEED);
			rightElevation.set(UP_SPEED);
		}
		else {
			leftElevation.set(DOWN_SPEED);
			rightElevation.set(DOWN_SPEED);
		}
	
		elevationState = State.MOVING;
		holdDistance = targetDistance;
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
	 * resets the encoder
	 */
	private void resetEncoder() {
		elevationEncoder.reset();
		elevationEncoderData.reset();	
	}
	
	/**
	 *Makes the states for elevation
	 */
	public enum State{
		IDLE,
		MOVING,
		HOLD_POSITION,
		RETURNING_HOME;
		
		/**
		 * Gets the name of the state
		 * @return the correct state 
		 */
		public String toString(){
			switch(this){
			case IDLE:
				return "In idle";
			case MOVING:
				return "Lifting";
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

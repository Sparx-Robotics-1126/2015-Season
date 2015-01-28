package org.gosparx.team1126.robot.subsystem;

import org.gosparx.team1126.robot.IO;
import org.gosparx.sensors.EncoderData;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;

/**
 * @author Reizwan & Raza
 * Version 1.0 Season 2015
 */
public class Elevation extends GenericSubsystem{
	
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
	 * the tote is 12 inches in height
	 */
	private static final double TOTE_DISTANCE_CLEARED = 12;
	
	private static final double TOTE_THRESHOLD = 0.1; //TODO Test if this is enough clearance
	
	/**
	 * The holding position cannot be bigger than the tote clearance threshold
	 */
	private static final double HOLDING_THRESHHOLD = -1 * TOTE_THRESHOLD * 0.9;
	
	/**
	 * this is the holding current
	 */
	private static final double DOWN_SPEED = -0.5; //TODO find best speed
	
	/**
	 * this is the left motors
	 */
	private Victor leftElevation;
	
	/**
	 * this is the right motors
	 */
	private Victor rightElevation;
	
	/**
	 * This is the elevation single instance
	 */
	private static Elevation elevation;
	
	/**
	 * this is the encoder for elevations 
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
	 * This is the distance we are going to lift or drop
	 */
	private double distanceToMove;
	
	/**
	 * threshold for encoder
	 */
	private double encoderThreshold;
	
	/**
	 * sensor for detecting the staring position for acquiring a tote
	 */
	private DigitalInput homeSwitch;
	
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
		distanceToMove = 0;
		encoderThreshold = 0;
		homeSwitch = new DigitalInput(IO.SWITCH_ELEVATIONS_RIGHT);
		return true;
	}
	@Override
	protected void liveWindow() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Main loop
	 */
	
	protected boolean execute() {
		switch(elevationState){
			case IDLE:
				break;
			case LIFTING:
				if (elevationEncoderData.getDistance() >= distanceToMove + encoderThreshold) {
					elevationState = State.HOLD_POSITION;
					leftElevation.set(0);
					rightElevation.set(0);
					/**
					 * reseting encoder to hold 0 position
					 */
					resetEncoder();
				}
				break;
			case HOLD_POSITION:
				if (elevationEncoderData.getDistance() <= HOLDING_THRESHHOLD){
					resetEncoder();
					leftElevation.set(HOLDING_SPEED);
					rightElevation.set(HOLDING_SPEED);
					elevationState = State.LIFTING;
					/**
					 * Move back threshold distance
					 */
					distanceToMove = -1 * HOLDING_THRESHHOLD;
					encoderThreshold = 0;
				}
				break;
			case RETURNING_HOME:
				if (homeSwitch.get()) {
					leftElevation.set(0);
					rightElevation.set(0);
					elevationState = State.IDLE;
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
		if (homeSwitch.get()){
			resetEncoder();
			leftElevation.set(UP_SPEED);
			rightElevation.set(UP_SPEED);
			elevationState = State.LIFTING;
			distanceToMove = TOTE_DISTANCE_CLEARED;
			encoderThreshold = TOTE_THRESHOLD;	
		}
		else {
			leftElevation.set(DOWN_SPEED);
			rightElevation.set(DOWN_SPEED);
			elevationState = State.RETURNING_HOME;
		}
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
		LIFTING,
		HOLD_POSITION,
		RETURNING_HOME;
		/**
		 * Gets the name of the state
		 * @return the correct state 
		 */
		
		public String toString(){
			switch(this){
			case IDLE:
				return "Idle";
			case LIFTING:
				return "Lifting";
			case HOLD_POSITION:
				return "Hold Position";
			case RETURNING_HOME:
				return "Returning Home";
			default:
				return "Error";
			}
		}
	}
	
}

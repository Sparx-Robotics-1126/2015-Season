package org.gosparx.team1126.robot.subsystem;

import org.gosparx.sensors.EncoderData;
import org.gosparx.team1126.robot.IO;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class Elevations2 extends GenericSubsystem{

	//******************OBJECTS**********************
	
	/**
	 * The only instance of Elevatios2
	 */
	private static Elevations2 elevations;
	
	/**
	 * Right Elevations motor
	 */
	private Talon rightElevationMotor;
	
	/**
	 * Left Elevations motor
	 */
	private Talon leftElevationMotor;
	
	/**
	 * Elevations encoder - tells distance elevations has traveled
	 */
	private Encoder elevationEncoder;
	
	/**
	 * Elevations encoder data - returns accurate values of elevations position
	 */
	private EncoderData elevationEncoderData;
	
	/**
	 * The home position switch
	 */
	private DigitalInput homeSwitch;
	
	
	private DigitalInput newToteSensor;
	
	//******************CONSTANTS********************
	
	/**
	 * Converts encoder rotation to distance elevation travel in inches
	 * calculated: lead screw pitch / enocder ticks
	 */
	private static final double DISTANCE_PER_TICK = 0.126/256;
	
	/**
	 * How fast to move the motors while moving
	 */
	private static final double MOVE_SPEED = 0.5;
	
	/**
	 * The distance we must be off by to give the motors full power
	 */
	private static final double MAX_OFF = 10;
	
	/**
	 * How far we need to lift the tote for clearance.
	 */
	private static final double TOTE_LIFT_DIST = 13;
	
	//******************VARIABLES********************
	
	/**
	 * The wanted speed of the motors
	 */
	private double wantedSpeed;
	
	/**
	 * The wanted position of the elevations system
	 */
	private double wantedPosition;
	
	/**
	 * The current state of the system
	 */
	private State currState;
	
	/**
	 * Are we going up or down?
	 */
	private boolean goingUp;
	
	/**
	 * Returns the only instance of elevations
	 */
	public static synchronized Elevations2 getInstance(){
		if(elevations == null){
			elevations = new Elevations2();
		}
		return elevations;
	}
	
	/**
	 * Creates a new elevations
	 */
	private Elevations2() {
		super("Elevations", Thread.NORM_PRIORITY);
	}

	/**
	 * Initializes things
	 */
	@Override
	protected boolean init() {
		rightElevationMotor = new Talon(IO.PWM_RIGHT_ELEVATION);
		leftElevationMotor = new Talon(IO.PWM_LEFT_ELEVATION);
		elevationEncoder = new Encoder(IO.DIO_ELEVATIONS_A, IO.DIO_ELEVATIONS_B);
		elevationEncoderData = new EncoderData(elevationEncoder, DISTANCE_PER_TICK);
		homeSwitch = new DigitalInput(IO.DIO_ELEVATIONS_ORIGIN);
		return false;
	}

	/**
	 * Adds things to the live window
	 */
	@Override
	protected void liveWindow() {
		LiveWindow.addActuator(getName(), "Right Elevator", rightElevationMotor);
		LiveWindow.addActuator(getName(), "Left Elevations", leftElevationMotor);
		LiveWindow.addSensor(getName(), "Home Switch", homeSwitch);
	}

	/**
	 * Loops and move the elevator accordingly 
	 */
	@Override
	protected boolean execute() {
		wantedSpeed = 0;
		elevationEncoderData.calculateSpeed();
		if(currState == State.STANDBY && newToteSensor.get()){
			lowerTote();
			LOG.logMessage("New tote acquired, starting lift sequence");
		}
		switch(currState){
		case STANDBY:
			wantedSpeed = (wantedPosition - elevationEncoderData.getDistance()) / MAX_OFF;
			break;
		case MOVE:
			if(goingUp && (elevationEncoderData.getDistance() < wantedPosition)){
				wantedSpeed = MOVE_SPEED;
			} else if(!goingUp && (elevationEncoderData.getDistance() > wantedPosition)) {
				wantedSpeed = -MOVE_SPEED;
			} else {
				wantedSpeed = 0;
				currState = State.STANDBY;
			}
			break;
		case SETTING_HOME:
			wantedSpeed = -MOVE_SPEED;
			if(homeSwitch.get()){
				LOG.logMessage("Home set");
				wantedSpeed = 0;
				goingUp = true;
				wantedPosition = TOTE_LIFT_DIST;
				elevationEncoderData.reset();
				elevationEncoder.reset();
				currState = State.MOVE;
			}
			break;
		}
		rightElevationMotor.set(wantedSpeed);
		leftElevationMotor.set(wantedSpeed);
		return false;
	}
	
	/**
	 * @return if we are done moving the totes
	 */
	public boolean isDone(){
		return currState == State.STANDBY;
	}
	
	/**
	 * Lowers the totes
	 */
	public void lowerTote(){
		goingUp = false;
		setHome();
	}
	
	/**
	 * Sets the home position
	 */
	public void setHome(){
		currState = State.SETTING_HOME;
	}

	/**
	 * Lifts the tote;
	 */
	public void liftTote(){
		goingUp = true;
		wantedPosition = TOTE_LIFT_DIST;
		currState = State.MOVE;
	}
	
	/**
	 * @return How long to sleep for
	 */
	@Override
	protected long sleepTime() {
		return 20;
	}

	/**
	 * Writes info about the subsystem to the log
	 */
	@Override
	protected void writeLog() {
		LOG.logMessage("Current State: " + currState.toString(currState));
		LOG.logMessage("Wanted Position: " + wantedPosition);
		LOG.logMessage("Current Position: " + elevationEncoderData.getDistance());
	}
	
	/**
	 * Stores all possible states.
	 */
	public enum State{
		STANDBY,
		MOVE,
		SETTING_HOME;
		
		/**
		 * @param state The current state
		 * @return the name of the state
		 */
		public String toString(State state){
			switch(state){
			case STANDBY: 		return "Standby";
			case MOVE: 			return "Moving";
			case SETTING_HOME: 	return "Setting Home";
			default: 		return "Unknown State";
			}
		}
	}
}

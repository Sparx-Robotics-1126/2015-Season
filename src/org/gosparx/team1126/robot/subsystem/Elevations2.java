package org.gosparx.team1126.robot.subsystem;

import org.gosparx.sensors.EncoderData;
import org.gosparx.team1126.robot.IO;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * Controls the elevations system.
 * @author Alex Mechler {amechler1998@gmail.com}
 * @author Nate
 */
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
	private Encoder elevationRightEncoder;

	/**
	 * Elevations encoder data - returns accurate values of elevations position
	 */
	private EncoderData elevationRightEncoderData;

	/**
	 * Elevations encoder - tells distance elevations has traveled
	 */
	private Encoder elevationLeftEncoder;

	/**
	 * Elevations encoder data - returns accurate values of elevations position
	 */
	private EncoderData elevationLeftEncoderData;

	/**
	 * The home position switch
	 */
	private DigitalInput homeSwitch;

	/**
	 * Used to detected the presence of a new Tote
	 */
	private DigitalInput newToteSensor;

	//******************CONSTANTS********************

	/**
	 * Converts encoder rotation to distance elevation travel in inches
	 * calculated: lead screw pitch / enocder ticks
	 */
	private static final double DISTANCE_PER_TICK = 0.126/256;

	/**
	 * The slowest we will run the motors if we are moving up
	 */
	private static final double MOVE_UP_SPEED = 0.5;

	/**
	 * The slowest we will run the motors if we are moving down
	 */
	private static final double MOVE_DOWN_SPEED = 0.25;

	/**
	 * The distance we must be off by to give the motors full power
	 */
	private static final double MAX_OFF = 10;

	/**
	 * How far we need to lift the tote for clearance.
	 */
	private static final double TOTE_LIFT_DIST = 13;

	/**
	 * Max offset between te two sides
	 */
	private static final double MAX_OFFSET = 0.25;

	//******************VARIABLES********************

	/**
	 * The wanted speed of the motors
	 */
	private double rightWantedSpeed = 0;

	/**
	 * The wanted speed of the motors
	 */
	private double leftWantedSpeed = 0;

	/**
	 * USed if both motors should be set to the same value
	 */
	private double wantedSpeed = 0;

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
		rightElevationMotor = new Talon(IO.PWM_ELEVATIONS_RIGHT);
		leftElevationMotor = new Talon(IO.PWM_ELEVATIONS_LEFT);
		elevationRightEncoder = new Encoder(IO.DIO_ELEVATIONS_A, IO.DIO_ELEVATIONS_B);
		elevationRightEncoderData = new EncoderData(elevationRightEncoder, DISTANCE_PER_TICK);
		elevationLeftEncoder = new Encoder(IO.DIO_ELEVATIONS_A, IO.DIO_ELEVATIONS_B);
		elevationLeftEncoderData = new EncoderData(elevationLeftEncoder, DISTANCE_PER_TICK);
		homeSwitch = new DigitalInput(IO.DIO_ELEVATIONS_ORIGIN);
		newToteSensor = new DigitalInput(IO.DIO_TOTE_SENSOR);
		currState = State.SETTING_HOME;
		return false;
	}

	/**
	 * Loops and move the elevator accordingly 
	 */
	@Override
	protected boolean execute() {
		wantedSpeed = 0;
		elevationRightEncoderData.calculateSpeed();
		elevationLeftEncoderData.calculateSpeed();
		if(currState == State.STANDBY && newToteSensor.get()){
			lowerTote();
			LOG.logMessage("New tote acquired, starting lifting sequence");
		}
		switch(currState){
		case STANDBY:
			wantedSpeed = (wantedPosition - elevationRightEncoderData.getDistance()) / MAX_OFF;
			break;
		case MOVE:
			double calculatedWantedSpeedUp = (wantedPosition - elevationRightEncoderData.getDistance()) / (MAX_OFF);
			double calculatedWantedSpeedDown = (wantedPosition - elevationRightEncoderData.getDistance()) / (MAX_OFF * 2);
			if(goingUp && (elevationRightEncoderData.getDistance() < wantedPosition)){
				wantedSpeed = (calculatedWantedSpeedUp > MOVE_UP_SPEED) ? calculatedWantedSpeedUp : MOVE_UP_SPEED;
			} else if(!goingUp && (elevationRightEncoderData.getDistance() > wantedPosition)) {
				wantedSpeed = (calculatedWantedSpeedDown > MOVE_DOWN_SPEED) ? calculatedWantedSpeedDown : MOVE_DOWN_SPEED;
			} else {
				wantedSpeed = 0;
				currState = State.STANDBY;
			}
			rightWantedSpeed = wantedSpeed;
			leftWantedSpeed = wantedSpeed;
			break;
		case COMPLEX_MOVE:
			double rightDistance = elevationRightEncoderData.getDistance();
			double leftDistance = elevationLeftEncoderData.getDistance();

			if(rightDistance > (leftDistance + MAX_OFFSET)){
				rightWantedSpeed -= 0.05;
				leftWantedSpeed += 0.05;
			}else if(leftDistance > (rightDistance + MAX_OFFSET)){
				rightWantedSpeed += 0.05;
				leftWantedSpeed -= 0.05;
			}else{
				if(rightDistance > wantedPosition){
					rightWantedSpeed = -0.5;
					leftWantedSpeed = -0.5;
				}else{
					rightWantedSpeed = 0.5;
					leftWantedSpeed = 0.5;
				}
			}
			
			//DONE
			if(Math.abs(rightDistance - wantedPosition) < MAX_OFFSET){
				LOG.logMessage("POSITION HAS BEEN FOUND at: " + wantedPosition);
				currState = State.STANDBY;
			}
			break;
		case SETTING_HOME:
			wantedSpeed = -MOVE_DOWN_SPEED;
			if(homeSwitch.get()){
				LOG.logMessage("Home set");
				wantedSpeed = 0;
				goingUp = true;
				wantedPosition = TOTE_LIFT_DIST;
				elevationRightEncoderData.reset();
				elevationLeftEncoderData.reset();
				currState = State.MOVE;
			}
			break;
		}
		rightElevationMotor.set(rightWantedSpeed);
		leftElevationMotor.set(leftWantedSpeed);
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
	 * Adds things to the live window
	 */
	@Override
	protected void liveWindow() {
		LiveWindow.addActuator(getName(), "Right Elevator", rightElevationMotor);
		LiveWindow.addActuator(getName(), "Left Elevations", leftElevationMotor);
		LiveWindow.addSensor(getName(), "Home Switch", homeSwitch);
		LiveWindow.addSensor(getName(), "Tote Detect", newToteSensor);
	}

	/**
	 * Stores all possible states.
	 */
	public enum State{
		STANDBY,
		MOVE,
		COMPLEX_MOVE,
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

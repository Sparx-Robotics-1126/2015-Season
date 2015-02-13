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
	 * The home position for the right side
	 */
	private DigitalInput rightHomeSwitch;

	/**
	 * The home position for the left side
	 */
	private DigitalInput leftHomeSwitch;

	/**
	 * Used to detected the presence of a new Tote
	 */
	private DigitalInput newToteSensor;

	//******************CONSTANTS********************

	/**
	 * Converts encoder rotation to distance elevation travel in inches
	 * calculated: lead screw pitch / enocder ticks
	 */
	private static final double DISTANCE_PER_TICK = 0.00048828;

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
	private static final double TOTE_LIFT_DIST = 15;

	/**
	 * Max offset between te two sides
	 */
	private static final double MAX_OFFSET = 0.25;

	private static final double MIN_SPEED = 0.2;
	
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

	private boolean rightDone = false;
	private boolean leftDone = false;

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
		elevationRightEncoder = new Encoder(10, 11);
		elevationRightEncoder.setDistancePerPulse(DISTANCE_PER_TICK);
		elevationRightEncoderData = new EncoderData(elevationRightEncoder, DISTANCE_PER_TICK);
		elevationLeftEncoder = new Encoder(4, 5);
		elevationLeftEncoder.setDistancePerPulse(DISTANCE_PER_TICK);
		elevationLeftEncoderData = new EncoderData(elevationLeftEncoder, DISTANCE_PER_TICK);
		rightHomeSwitch = new DigitalInput(21);
		leftHomeSwitch = new DigitalInput(20);
		newToteSensor = new DigitalInput(IO.DIO_TOTE_SENSOR);
		currState = State.STANDBY;
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
		//		if(currState == State.STANDBY && newToteSensor.get()){
		//			lowerTote();
		//			LOG.logMessage("New tote acquired, starting lifting sequence");
		//		}
		switch(currState){
		case STANDBY:
			//			wantedSpeed = (wantedPosition - elevationRightEncoderData.getDistance()) / MAX_OFF;
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
			double rightSpeed = (wantedPosition - rightDistance)/5.0;
			double leftSpeed = (wantedPosition - leftDistance)/5.0;
			
			if(rightDistance > (leftDistance + MAX_OFFSET)){
				rightWantedSpeed -= 0.02;
				leftWantedSpeed += 0.02;
			}else if(leftDistance > (rightDistance + MAX_OFFSET)){
				rightWantedSpeed += 0.02;
				leftWantedSpeed -= 0.02;
			}else{
				if(goingUp){
					rightWantedSpeed = Math.abs(rightSpeed) < MIN_SPEED ? MIN_SPEED: rightSpeed;
					leftWantedSpeed = Math.abs(leftSpeed) < MIN_SPEED ? MIN_SPEED: leftSpeed;
				}else{
					rightWantedSpeed = Math.abs(rightSpeed) < MIN_SPEED ? -MIN_SPEED: rightSpeed;
					leftWantedSpeed = Math.abs(leftSpeed) < MIN_SPEED ? -MIN_SPEED: leftSpeed;
				}
			}
			

			//DONE
			if(((rightDistance > wantedPosition - 0.25 && goingUp) || (rightDistance < wantedPosition + 0.25 && !goingUp)) && !rightDone){
				LOG.logMessage("RIGHT POSITION HAS BEEN FOUND at: " + wantedPosition);
				rightDone = true;
				rightWantedSpeed = 0;
			}

			if(((leftDistance > wantedPosition - 0.25 && goingUp) || (leftDistance < wantedPosition + 0.25 && !goingUp)) && !leftDone){
				LOG.logMessage("LEFT POSITION HAS BEEN FOUND at: " + wantedPosition);
				leftDone = true;
				leftWantedSpeed = 0;
			}

			if(rightDone){
				rightWantedSpeed = 0;
			}
			if(leftDone){
				leftWantedSpeed = 0;
			}

			if(leftDone && rightDone){
				leftDone = false;
				rightDone = false;
				currState = State.STANDBY;
			}

			if(!rightHomeSwitch.get() && !goingUp){
				rightWantedSpeed = 0;
			}
			if(!leftHomeSwitch.get() && !goingUp){
				leftWantedSpeed = 0;
			}
			if(!rightHomeSwitch.get() && !leftHomeSwitch.get() && !goingUp){
				LOG.logMessage("LIMIT SWITCHES HAVE BEEN TRIGGERED***");
				elevationLeftEncoderData.reset();
				elevationRightEncoderData.reset();
				currState = State.STANDBY;
			}
			
			break;
		case SETTING_HOME:
			if(!rightHomeSwitch.get()){
				LOG.logMessage("Right Home set");	
				elevationRightEncoderData.reset();
				rightWantedSpeed = 0;
			}else{
				rightWantedSpeed = -0.3;
			}

			if(!leftHomeSwitch.get()){
				LOG.logMessage("Left Home set");
				elevationLeftEncoderData.reset();
				leftWantedSpeed = 0;
			}else{
				leftWantedSpeed = -0.3;
			}

			if(!rightHomeSwitch.get() && !leftHomeSwitch.get()){
				currState = State.STANDBY;
				goingUp = true;
				wantedPosition = TOTE_LIFT_DIST;
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

	public void lowerTotes(){
		goingUp = false;
		wantedPosition = 0;
		currState = State.COMPLEX_MOVE;
	}

	/**
	 * Lifts the tote;
	 */
	public void liftTote(){
		goingUp = true;
		wantedPosition = TOTE_LIFT_DIST;
		currState = State.COMPLEX_MOVE;
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
		LOG.logMessage("Current Position: Right: " + elevationRightEncoderData.getDistance() + " Left: " + elevationLeftEncoderData.getDistance());
		LOG.logMessage("Right Sensor: " + rightHomeSwitch.get() + " Left: " + leftHomeSwitch.get());
		LOG.logMessage("New Tote: " + newToteSensor.get());
	}

	/**
	 * Adds things to the live window
	 */
	@Override
	protected void liveWindow() {
		LiveWindow.addActuator(getName(), "Right Elevator", rightElevationMotor);
		LiveWindow.addActuator(getName(), "Left Elevations", leftElevationMotor);
		LiveWindow.addActuator(getName(), "Right Encoder", elevationRightEncoder);
		LiveWindow.addActuator(getName(), "Left Encoder", elevationLeftEncoder);
		LiveWindow.addSensor(getName(), "Right Home Switch", rightHomeSwitch);
		LiveWindow.addSensor(getName(), "LEft Home Switch", leftHomeSwitch);
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
			case COMPLEX_MOVE: 	return "Complex Moving";
			case SETTING_HOME: 	return "Setting Home";
			default: 		return "Unknown State";
			}
		}
	}
}

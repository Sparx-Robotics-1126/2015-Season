package org.gosparx.team1126.robot.subsystem;

import org.gosparx.sensors.EncoderData;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;

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
	
	
	private Encoder elevationEncoder;
	
	
	private EncoderData elevationEncoderData;
	
	
	private DigitalInput homeSwitch;
	
	//******************CONSTANTS********************
	
	/**
	 * Converts encoder rotation to distance elevation travel in inches
	 * calculated: lead screw pitch / enocder ticks
	 */
	private static final double DISTANCE_PER_TICK = 0.126/256;
	
	private static final double MOVE_SPEED = 0.5;
	
	private static final double MAX_OFF = 10;
	
	private static final double TOTE_LIFT_DIST = 13;
	
	//******************VARIABLES********************
	private double wantedSpeed;
	private double wantedPosition;
	private State currState;
	private boolean goingUp;
	
	public Elevations2() {
		super("Elevations", Thread.NORM_PRIORITY);
	}

	@Override
	protected boolean init() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void liveWindow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean execute() {
		wantedSpeed = 0;
		elevationEncoderData.calculateSpeed();
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
				currState = State.STANDBY;
			}
			break;
		case SETTING_HOME:
			wantedSpeed = -MOVE_SPEED;
			if(homeSwitch.get()){
				wantedSpeed = 0;
				wantedPosition = 0;
				elevationEncoderData.reset();
				elevationEncoder.reset();
				currState = State.STANDBY;
			}
			break;
		}
		rightElevationMotor.set(wantedSpeed);
		leftElevationMotor.set(wantedSpeed);
		return false;
	}
	
	public boolean isDone(){
		return currState == State.STANDBY;
	}
	
	public void lowerTote(){
		setHome();
	}
	
	public void setHome(){
		currState = State.SETTING_HOME;
	}

	public void liftTote(){
		currState = State.MOVE;
		wantedPosition = TOTE_LIFT_DIST;
	}
	
	@Override
	protected long sleepTime() {
		return 10;
	}

	@Override
	protected void writeLog() {
	
	}
	
	public enum State{
		STANDBY,
		MOVE,
		SETTING_HOME;
		
		public String toString(State num){
			switch(num){
			case STANDBY: 		return "Standby";
			case MOVE: 			return "Moving";
			case SETTING_HOME: 	return "Setting Home";
			default: 		return "Unknown State";
			}
		}
	}

}

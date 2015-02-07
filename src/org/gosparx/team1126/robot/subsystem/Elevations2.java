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
	
	//******************VARIABLES********************
	
	public Elevations2() {
		super("Elevations", Thread.NORM_PRIORITY);
	}

	@Override
	protected boolean init() {
		rightElevationMotor = new Talon(IO.PWM_ELEVATIONS_RIGHT);
		leftElevationMotor = new Talon(IO.PWM_ELEVATIONS_LEFT);
		elevationEncoder = new Encoder(IO.DIO_ELEVATIONS_A, IO.DIO_ELEVATIONS_B);
		elevationEncoderData = new EncoderData(elevationEncoder, DISTANCE_PER_TICK);
		homeSwitch = new DigitalInput(IO.DIO_ELEVATIONS_ORIGIN);
		newToteSensor = new DigitalInput(IO.DIO):
		return false;
	}

	@Override
	protected void liveWindow() {
		LiveWindow.addActuator(getName(), "Right Elevator", rightElevationMotor);
		LiveWindow.addActuator(getName(), "Left Elevations", leftElevationMotor);
		LiveWindow.addSensor(getName(), "Home Switch", homeSwitch);
	}

	@Override
	protected boolean execute() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected long sleepTime() {
		return 20;
	}

	@Override
	protected void writeLog() {
		
	}
	
	public enum State{
		Standby;
		
		public String toString(State num){
			switch(num){
			case Standby: 	return "Standby";
			default: 		return "Unknow State";
			}
		}
	}
}

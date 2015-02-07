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
	
	//******************VARIABLES********************
	
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
		// TODO Auto-generated method stub
		return false;
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

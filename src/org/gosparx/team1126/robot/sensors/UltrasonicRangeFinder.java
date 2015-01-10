package org.gosparx.team1126.robot.sensors;

import edu.wpi.first.wpilibj.AnalogInput;

/**
 * A class to interpret data from the UltrasonicRangeFinder
 * @author Alex_Fixed
 */
public class UltrasonicRangeFinder {
	
	private AnalogInput sensor;
	
	private double voltsPerInch;
	
	private double zeroPos;
	
	public static final double MAX_SONAR_EZ1_VPI = 5.0/512.0; 
	
	/**
	 * Creates a new UltrasonicRangeFinder 
	 * @param device - the analog input the range finder is in
	 * @param voltsPerInch - the volts the analog reading changes per inch
	 */
	public UltrasonicRangeFinder(AnalogInput device, double voltsPerInch){
		sensor = device;
		this.voltsPerInch = voltsPerInch;
		zeroPos = 0;
	}
	
	public void zero(){
		zeroPos = sensor.getVoltage();
	}
	
	public void resetZero(){
		zeroPos = 0;
	}
	
	public double getRange(){
		return (sensor.getVoltage() - zeroPos) / voltsPerInch;
	}
}

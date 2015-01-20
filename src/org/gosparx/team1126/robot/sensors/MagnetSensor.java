package org.gosparx.team1126.robot.sensors;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 * A class for interpreting the data from Magnetic Limit Switch
 * @author Alex
 */
public class MagnetSensor {

	/**
	 * The digital input for the Magnetic Sensor
	 */
	private DigitalInput in;
	
	/**
	 * Creates a new magnetic sensor
	 * @param dio - The digitalinput the sensor is in
	 */
	public MagnetSensor(DigitalInput dio){
		in = dio;
	}
	
	/**
	 * Creates a new magnetic sensor
	 * @param port - the port the sensor is in
	 */
	public MagnetSensor(int port){
		in = new DigitalInput(port);
	}
	
	/**
	 * @return if the sensor is tripped.
	 */
	public boolean isTripped(){
		return !in.get();
	}
}

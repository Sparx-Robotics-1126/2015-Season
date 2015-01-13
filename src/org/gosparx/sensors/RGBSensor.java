package org.gosparx.sensors;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalOutput;


public class RGBSensor{

	/**
	 * Red input
	 */
	private AnalogInput redAnalogInput;
	
	/**
	 * Green input
	 */
	private AnalogInput greenAnalogInput;
	
	/**
	 * Blue input
	 */
	private AnalogInput blueAnalogInput;

	/**
	 * Creates an RGBSensor
	 * @param redChannel - red channel
	 * @param greenChannel - green channel
	 * @param blueChannel - blue channel
	 * @param ledChannel - LED channel
	 */
	public RGBSensor(int redChannel, int greenChannel, int blueChannel, int ledChannel){
		redAnalogInput = new AnalogInput(redChannel);
		greenAnalogInput = new AnalogInput(greenChannel);
		blueAnalogInput = new AnalogInput(blueChannel);
		new DigitalOutput (ledChannel).set(true);
	}

	/**
	 * @return value of red
	 */
	public int getRed(){
		return redAnalogInput.getValue();
	}
	
	/**
	 * @return value of green
	 */
	public int getGreen(){
		return greenAnalogInput.getValue();
	}
	
	/**
	 * @return value of blue
	 */
	public int getBlue(){
		return blueAnalogInput.getValue();
	}
}


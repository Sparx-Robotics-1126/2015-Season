package org.gosparx.sensors;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalOutput;

/**
 * Used for creating an RGB sensor used for detecting color 
 */
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
	 * @return value of red (0 - 255)
	 */
	public int getRed(){
		return redAnalogInput.getValue();
	}
	
	/**
	 * @return value of green (0 - 255)
	 */
	public int getGreen(){
		return greenAnalogInput.getValue();
	}
	
	/**
	 * @return value of blue (0 - 255)
	 */
	public int getBlue(){
		return blueAnalogInput.getValue();
	}
}


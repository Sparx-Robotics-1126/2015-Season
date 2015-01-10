package org.gosparx.sensors;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalOutput;

/* Implement Sparkfun type RGB sensor
 * Author: Raza Ahmed
 * version 1.0 season 2015
 */

public class SparkfunRGBSensor implements RGBSensorIF{

	private AnalogInput redAnalogInput;
	private AnalogInput greenAnalogInput;
	private AnalogInput blueAnalogInput; 
	private DigitalOutput ledDigitalOutput;

	public SparkfunRGBSensor(int redChannel, int greenChannel, int blueChannel, int ledChannel){
		redAnalogInput = new AnalogInput(redChannel);
		greenAnalogInput = new AnalogInput(greenChannel);
		blueAnalogInput = new AnalogInput(blueChannel);
		ledDigitalOutput = new DigitalOutput (ledChannel);
	}

	public int getRed(){
		return redAnalogInput.getValue();
		// return redAnalogInput.getValue() * 10;
	}
	public int getGreen(){
		return greenAnalogInput.getValue();
		// return greenAnalogInput.getValue() * 17;
	}
	public int getBlue(){
		return blueAnalogInput.getValue();
		// return blueAnalogInput.getValue() * 14;
	}
	public void setLED(boolean on){
			ledDigitalOutput.set(on);	
	}
}


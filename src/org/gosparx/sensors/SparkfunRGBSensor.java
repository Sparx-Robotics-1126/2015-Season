package org.gosparx.sensors;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogOutput;

/* Implement Sparkfun type RGB sensor
 * Author: Raza Ahmed
 * version 1.0 season 2015
 */

public class SparkfunRGBSensor implements RGBSensorIF{

	private AnalogInput redAnalogInput;
	private AnalogInput blueAnalogInput; 
	private AnalogInput greenAnalogInput;
	private AnalogOutput ledAnalogOutput;

	public SparkfunRGBSensor(int redChannel, int blueChannel, int greenChannel, int ledChannel){
		redAnalogInput = new AnalogInput(redChannel);
		blueAnalogInput = new AnalogInput(blueChannel);
		greenAnalogInput = new AnalogInput(greenChannel);
		ledAnalogOutput = new AnalogOutput (ledChannel);
	}

	public int getRed(){
		return redAnalogInput.getValue();
		// return redAnalogInput.getValue() * 10;
	}
	public int getBlue(){
		return blueAnalogInput.getValue();
		// return blueAnalogInput.getValue() * 14;
	}
	public int getGreen(){
		return greenAnalogInput.getValue();
		// return greenAnalogInput.getValue() * 17;
	}
	public void setLED(boolean on){
		if(on == true){
			ledAnalogOutput.setVoltage(5.0);	
		}
		else {
			ledAnalogOutput.setVoltage(0);
		}
	
	}
}


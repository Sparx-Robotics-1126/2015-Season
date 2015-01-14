package org.gosparx.team1126.robot.sensors;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalOutput;

/* This returns the color 
 * Version 1.0 Season 2015
 */

public class ColorSensor {
	
	/**
	 * State machine for Color
	 *
	 */
	public enum Color {
		UNKNOWN,
		WHITE,
		BLACK,
		RED,
		GREEN,
		BLUE 
	}

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
	 * LED ouput
	 */
	private DigitalOutput lightLED;
	
	/**
	 * we treat white as 80% of the addition of all of the colors and above
	 */
	static final double WHITE_THRESHOLD = (255 * 3) * .8; 
	
	/**
	 * we treat black as 20% of the addition of all of the colors and below
	 */
	static final double BLACK_THRESHOLD = (255 * 3) * .2;

	public ColorSensor(int redChannel, int greenChannel, int blueChannel, int ledChannel){
		redAnalogInput = new AnalogInput(redChannel);
		greenAnalogInput = new AnalogInput(greenChannel);
		blueAnalogInput = new AnalogInput(blueChannel);
		lightLED = new DigitalOutput(ledChannel);
		lightLED.set(true);
	}

	/**
	 * @return value of red (0 - 255) values may be bigger than 255
	 */
	private int getRed(){
		return redAnalogInput.getValue();
	}
	
	/**
	 * @return value of green (0 - 255) values may be bigger than 255
	 */
	private int getGreen(){
		return greenAnalogInput.getValue();
	}
	
	/**
	 * @return value of blue (0 - 255) values may be bigger than 255
	 */
	private int getBlue(){
		return blueAnalogInput.getValue();
	}
	
	/**
	 * @param on - true if on, false is off
	 */
	public void setLED(boolean on){
		lightLED.set(on);
	}
	
	/**
	 * returns the color ID
	 */
	public Color getColor(){
		int redValue = getRed();
		int greenValue = getGreen();
		int blueValue = getBlue();
		int totalValue = redValue + greenValue + blueValue;
		
		if (totalValue >= WHITE_THRESHHOLD){
			return Color.WHITE;
		}
		else if (totalValue <= BLACK_THRESHHOLD){
			return Color.BLACK;
		}
		else if (blueValue >= redValue && blueValue >= greenValue){
			return Color.BLUE;
		}
		else if (greenValue >= redValue && greenValue >= blueValue){
			return Color.GREEN;
		}
		else if (redValue >= blueValue && redValue >= greenValue){
			return Color.RED;
		}
		return Color.UNKNOWN;
	}
	
	/**
	 * 
	 * @param color - color wanted from Color.
	 * @return the string value for the color
	 */
	public String colorToString(Color color){
		switch (color){
			case WHITE:
				return "White";
			case BLACK:
				return "Black";
			case RED:
				return "Red";
			case GREEN:
				return "Green";
			case BLUE:
				return "Blue";
			case UNKNOWN:
				return "Unknown";
			default:
				return "Invalid";
			
		}
	}
	
	/**
	 * 
	 * @param color - color wanted to see
	 * @return - if the color seen by the sensor is the same as the color wanted
	 */
	public boolean isColor(Color color) {
		return(getColor() == color);
	}
}

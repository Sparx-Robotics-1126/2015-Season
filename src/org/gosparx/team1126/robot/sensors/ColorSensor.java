package org.gosparx.team1126.robot.sensors;

/* This returns the color 
 * Author:Raza Ahmed, Reizwan Chowdhury, Andrew Thompson
 * Version 1.0 Season 2015
 */

public class ColorSensor {
	
	public enum Color {
		UNKNOWN,
		WHITE,
		BLACK,
		RED,
		GREEN,
		BLUE 
	}
	
	// this is the sensor we are using currently to read RGB values
	private RGBSensor rgbSensor;
	// we treat white as 80% of the addition of all of the colors and above
	static final double WHITE_THRESHHOLD = (255 * 3) * .8; 
	// we treat black as 20% of the addition of all of the colors and below
	static final double BLACK_THRESHHOLD = (255 * 3) * .2;

	public ColorSensor(){
		rgbSensor = new RGBSensor(0, 1, 2, 0);
	}
	
	//returns the color ID
	public Color getColor(){
		int redValue = rgbSensor.getRed();
		int greenValue = rgbSensor.getGreen();
		int blueValue = rgbSensor.getBlue();
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
	
	public boolean isColor(Color color) {
		return(getColor() == color);
	}
}

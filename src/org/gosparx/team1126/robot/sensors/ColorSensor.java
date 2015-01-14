package org.gosparx.team1126.robot.sensors;

/* This returns the colour 
 * Author:Raza Ahmed, Reizwan Chowdhury, Andrew Thompson
 * Version 1.0 Season 2015
 */

public class ColorSensor {
	
	public enum COLOR {
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
	public COLOR getColor(){
		int redValue = rgbSensor.getRed();
		int greenValue = rgbSensor.getGreen();
		int blueValue = rgbSensor.getBlue();
		int totalValue = redValue + greenValue + blueValue;
		
		if (totalValue >= WHITE_THRESHHOLD){
			return COLOR.White;
		}
		else if (totalValue <= BLACK_THRESHHOLD){
			return COLOR.Black;
		}
		else if (blueValue >= redValue && blueValue >= greenValue){
			return COLOR.Blue;
		}
		else if (greenValue >= redValue && greenValue >= blueValue){
			return COLOR.Green;
		}
		else if (redValue >= blueValue && redValue >= greenValue){
			return COLOR.Red;
		}
		return COLOR.Unknown;
	}
	
	public String colorToString(COLOR _color){
		switch (_color){
			case White:
				return "White";
			case Black:
				return "Black";
			case Red:
				return "Red";
			case Green:
				return "Green";
			case Blue:
				return "Blue";
			case Unknown:
				return "Unknown";
			default:
				return "Invalid";
			
		}
	}
	
	public boolean isColor(COLOR color) {
		return(getColor() == color);
	}
}

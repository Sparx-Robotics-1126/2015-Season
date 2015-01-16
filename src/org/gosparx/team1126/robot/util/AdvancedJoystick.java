package org.gosparx.team1126.robot.util;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;

/**
 * A class for more advanced data collection from the joysticks.
 * @author Alex Mechler
 */
public class AdvancedJoystick{

	/**
	 * The Joystick to get the data from
	 */
	private Joystick stick;

	/**
	 * The number of buttons on the joystick
	 */
	private int numButtons;

	/**
	 * The number of Axis on the joystick
	 */
	private int numAxis;

	/**
	 * A map of all of the previous button values, used for rising/falling edge detection
	 */
	private boolean[] prevButton;

	/**
	 * A map of all of the current button values, used for rising/falling edge detection
	 */
	private boolean[] currButton;

	/**
	 * A map of all of the previous axis values, currently unused
	 */
	private double[] prevAxis;

	/**
	 * A map of all of the current axis values
	 */
	private double[] currAxis;

	/**
	 * An instance of driverstation
	 */
	private DriverStation ds;

	/**
	 * Creates a new AdvancedJoystick
	 * @param joy The Joystick to collect data from
	 * @param numButtons The number of buttons on the Joystick
	 * @param numAxis The number of Axis on the Joystick
	 */
	public AdvancedJoystick(Joystick joy, int numButtons, int numAxis){
		stick = joy;
		this.numButtons = numButtons;
		this.numAxis = numAxis;
		prevButton = new boolean[numButtons];
		currButton = new boolean[numButtons];
		prevAxis = new double[numAxis];
		currAxis = new double[numAxis];
		ds = DriverStation.getInstance();
	}
	
	/**
	 * Creates a new AdvancedJoystick
	 * @param port The port of the Joystick to collect data from
	 * @param numButtons The number of buttons on the Joystick
	 * @param numAxis The number of Axis on the Joystick
	 */
	public AdvancedJoystick(int port, int numButtons, int numAxis){
		stick = new Joystick(port);
		this.numButtons = numButtons;
		this.numAxis = numAxis;
		prevButton = new boolean[numButtons];
		currButton = new boolean[numButtons];
		prevAxis = new double[numAxis];
		currAxis = new double[numAxis];
		ds = DriverStation.getInstance();
	}

	/**
	 * Call this before any other method using the values. Updates all of the maps
	 */
	public void updateValues(){
		if(ds.isEnabled() && ds.isOperatorControl()){
			prevButton = currButton.clone();
			for(int i = 1; i <= numButtons; i++){
				currButton[i-1] = stick.getRawButton(i);
			}
			prevAxis = currAxis.clone();
			for (int i = 1; i <= numAxis; i++){
				currAxis[i-1] = stick.getRawAxis(i);
			}
		}
	}

	/**
	 * returns the current value of the axis
	 * @param id the id of the Axis
	 * @return the current value of the axis
	 */
	public double getAxis(int id){
		return currAxis[id-1];
	}

	/**
	 * Gets the current state of the button
	 * @param id the id of the button to collect
	 * @return true if pressed, false if not
	 */
	public boolean getButton(int id){
		return currButton[id-1];
	}

	/**
	 * Returns if the button is on a rising edge
	 * @param id the buttons id
	 * @return true if it is a rising edge
	 */
	public boolean getRisingEdge(int id){
		return currButton[id-1] && !prevButton[id-1];
	}

	/**
	 * Returns if the button is on a falling edge
	 * @param id the id of the button
	 * @return returns if it is on a falling edge
	 */
	public boolean getFallingEdge(int id){
		return !currButton[id-1] && prevButton[id-1];
	}
}

package org.gosparx.team1126.robot.subsystem;

import org.gosparx.team1126.robot.IO;
import edu.wpi.first.wpilibj.Victor;

/**
 * @author Reizwan & Raza
 * Version 1.0 Season 2015
 */
public class Elevation {
	/**
	 * the speed of the elevation up
	 */
	private static final double UP_SPEED = 0.5; //TODO find elevation elevation up
	/**
	 * this is the left motors
	 */
	private Victor leftElevation;
	/**
	 * this is the right motors
	 */
	private Victor rightElevation;
	/**
	 * This is the elevation single instance
	 */
	private static Elevation elevation;
	/**
	 * this is the constructor of the Elevation
	 */
	private Elevation() {
		leftElevation = new Victor(IO.PWM_LEFT_ELEVATION);
		rightElevation = new Victor(IO.PWM_RIGHT_ELEVATION);
	}
	/**
	 * gets 1 instance of elevation
	 * @return the instance of elevation
	 */
	public static Elevation getInstance(){
		if(elevation == null){
			elevation = new Elevation();
		}
		return elevation;
	}
	/**
	 * This lifts the tote enough that another tote fits under it
	 */
	public void liftTote(){
		leftElevation.set(UP_SPEED);
		rightElevation.set(UP_SPEED);
		
	}
	
}

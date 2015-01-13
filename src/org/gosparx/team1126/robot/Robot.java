package org.gosparx.team1126.robot;

import org.gosparx.team1126.robot.subsystem.GenericSubsystem;
import org.gosparx.sensors.ColorSensor;
import edu.wpi.first.wpilibj.SampleRobot;

/**
 * The entrypoint for the robot. The constructor is called once the robot is turned on.
 * @author Alex
 */
public class Robot extends SampleRobot{
	private ColorSensor colorSensor;

	/**
	 * An array of all of the subsystems on the robot
	 */
	private GenericSubsystem[] subsystems;

	/**
	 * Called once every time the robot is powered on
	 */
	public Robot() {
		subsystems = new GenericSubsystem[]{

		};
		colorSensor = new ColorSensor();
		for(GenericSubsystem system: subsystems){
		}
	}

	/**
	 *  Called one time when the robot enters autonomous
	 */
	public void autonomous() {

	}

	/**
	 *  Called one time when the robot enters teleop
	 */
	public void operatorControl() {
		
				theDriverStation.reportError(colorSensor.colorToString(colorSensor.getColor()) + "\n", false);
	}

	/**
	 *  Called one time when the robot enters test
	 */
	public void test() {
	}
}

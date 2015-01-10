package org.gosparx.team1126.robot;

import java.sql.Time;

import org.gosparx.team1126.robot.sensors.UltrasonicRangeFinder;
import org.gosparx.team1126.robot.subsystem.GenericSubsystem;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;

/**
 * The entrypoint for the robot. The constructor is called once the robot is turned on.
 * @author Alex
 */
public class Robot extends SampleRobot {

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

		for(GenericSubsystem system: subsystems){
			system.start();
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
		UltrasonicRangeFinder usrf = new UltrasonicRangeFinder(new AnalogInput(0), UltrasonicRangeFinder.MAX_SONAR_EZ1_VPI);
		while(DriverStation.getInstance().isEnabled() && DriverStation.getInstance().isOperatorControl()){
			DriverStation.getInstance().reportError("Range: " + usrf.getRange() + "\n", false);
			Timer.delay(1);
		}
	}

	/**
	 *  Called one time when the robot enters test
	 */
	public void test() {
	}
}

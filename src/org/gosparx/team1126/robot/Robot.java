
package org.gosparx.team1126.robot;


import org.gosparx.sensors.SparkfunRGBSensor;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

/**
 * This is a demo program showing the use of the RobotDrive class.
 * The SampleRobot class is the base of a robot application that will automatically call your
 * Autonomous and OperatorControl methods at the right time as controlled by the switches on
 * the driver station or the field controls.
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 * WARNING: While it may look like a good choice to use for your code if you're inexperienced,
 * don't. Unless you know what you are doing, complex code will be much more difficult under
 * this system. Use IterativeRobot or Command-Based instead if you're new.
 */
public class Robot extends SampleRobot {
	RobotDrive myRobot;
	Joystick stick;
	private SparkfunRGBSensor rgbSensor;
	private DriverStation theDriverStation;

	public Robot() {
		myRobot = new RobotDrive(0, 1);
		myRobot.setExpiration(0.1);
		stick = new Joystick(0);
		rgbSensor = new SparkfunRGBSensor(1, 2, 3, 0);
		theDriverStation = DriverStation.getInstance();
		rgbSensor.setLED(true);
	}

	/**
	 * Drive left & right motors for 2 seconds then stop
	 */
	public void autonomous() {
		myRobot.setSafetyEnabled(false);
		myRobot.drive(-0.5, 0.0);	// drive forwards half speed
		Timer.delay(2.0);		//    for 2 seconds
		myRobot.drive(0.0, 0.0);	// stop robot
	}

	/**
	 * Runs the motors with arcade steering.
	 */
	public void operatorControl() {
		myRobot.setSafetyEnabled(true);
		int counter = 0;
		while (isOperatorControl() && isEnabled()) {
			myRobot.arcadeDrive(stick); // drive with arcade style (use right stick)
			Timer.delay(0.005);		// wait for a motor update time
			int redValue = rgbSensor.getRed();
			int greenValue = rgbSensor.getGreen();
			int blueValue = rgbSensor.getBlue();
			String redString = "red Value = " + redValue;
			String greenString = "green Value = " + greenValue;
			String blueString = "blue Value = " + blueValue;
			if(counter%100 == 0){
				theDriverStation.reportError(redString + "\n", false);
				theDriverStation.reportError(greenString + "\n", false);
				theDriverStation.reportError(blueString + "\n", false);
			}
			counter ++; 
		}
	}

	/**
	 * Runs during test mode
	 */
	public void test() {
	}
}

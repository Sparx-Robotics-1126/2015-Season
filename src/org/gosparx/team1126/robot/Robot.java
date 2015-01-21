package org.gosparx.team1126.robot;

import org.gosparx.team1126.robot.subsystem.Controls;
import org.gosparx.team1126.robot.subsystem.Drives;
import org.gosparx.team1126.robot.subsystem.GenericSubsystem;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Servo;

/**
 * The entry point for the robot. The constructor is called once the robot is turned on.
 */
public class Robot extends SampleRobot{
	/**
	 * An array of all of the subsystems on the robot
	 */
	private GenericSubsystem[] subsystems;

	/**
	 * Called once every time the robot is powered on
	 */
	public Robot() {
		subsystems = new GenericSubsystem[]{
        	Controls.getInstance(),
        	Drives.getInstance()
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
		Servo ser = new Servo(7);
		while(true){
			for(double i = 0; i < 1; i+=0.01){
				ser.set(i);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 *  Called one time when the robot enters test
	 */
	public void test() {
		THIS.WILL.NOT.COMPILE!!!!!!!
	}
}

package org.gosparx.team1126.robot;

import org.gosparx.team1126.robot.subsystem.CanAcqTele;
import org.gosparx.team1126.robot.subsystem.CanAcquisition;
import org.gosparx.team1126.robot.subsystem.Controls;
import org.gosparx.team1126.robot.subsystem.Drives;
import org.gosparx.team1126.robot.subsystem.Elevations;
import org.gosparx.team1126.robot.subsystem.GenericSubsystem;
import org.gosparx.team1126.robot.subsystem.ToteAcq;
import org.gosparx.team1126.robot.util.LogWriter;

import edu.wpi.first.wpilibj.SampleRobot;

/**
 * The entry point for the robot. The constructor is called once the robot is turned on.
 */
public class Robot extends SampleRobot{
	/**
	 * An array of all of the subsystems on the robot
	 */
	private GenericSubsystem[] subsystems;
	
	private Autonomous auto;

	/**
	 * Called once every time the robot is powered on
	 */
	public Robot() {
		auto = Autonomous.getInstance();
		
		subsystems = new GenericSubsystem[]{	
        	LogWriter.getInstance(),
        	Elevations.getInstance(),
        	Drives.getInstance(),
        	CanAcquisition.getInstance(),
        	CanAcqTele.getInstance(),
        	ToteAcq.getInstance(),
        	Controls.getInstance(),
        	auto
		};
		
		for(GenericSubsystem system: subsystems){
			system.start();
		}
	}

	/**
	 *  Called one time when the robot enters autonomous
	 */
	public void autonomous() {
		auto.runAuto(true);
	}

	/**
	 *  Called one time when the robot enters teleop
	 */
	public void operatorControl() {
		auto.runAuto(false);
	}

	/**
	 *  Called one time when the robot enters test
	 */
	public void test() {
		
	}
}

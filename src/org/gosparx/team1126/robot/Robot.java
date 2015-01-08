package org.gosparx.team1126.robot;

import org.gosparx.team1126.robot.subsystem.GenericSubsystem;
import edu.wpi.first.wpilibj.SampleRobot;

public class Robot extends SampleRobot {

	private GenericSubsystem[] subsystems;
	
    public Robot() {
        subsystems = new GenericSubsystem[]{
        		
        };
        
        for(GenericSubsystem system: subsystems){
        	system.start();
        }
    }

    public void autonomous() {

    }

    public void operatorControl() {
        
    }

    public void test() {
    }
}

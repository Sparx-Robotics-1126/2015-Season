package org.gosparx.team1126.robot;

import org.gosparx.team1126.robot.subsystem.GenericSubsystem;
import org.gosparx.team1126.robot.util.LogWriter;

import edu.wpi.first.wpilibj.SampleRobot;

public class Robot extends SampleRobot {

	private GenericSubsystem[] subsystems;
	
    public Robot() {
        subsystems = new GenericSubsystem[]{	
        		LogWriter.getInstance()
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

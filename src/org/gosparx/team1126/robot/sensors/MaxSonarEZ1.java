package org.gosparx.team1126.robot.sensors;

import edu.wpi.first.wpilibj.AnalogInput;

public class MaxSonarEZ1 extends UltrasonicRangeFinder{
	
	/**
	 * The volts per inch drop for the MaxSonar EZ1 range finder.
	 */
	public static final double MAX_SONAR_EZ1_VPI = 5.0/512.0; 
	
	/**
	 * Creates a new UltraSonic range finder with a VPI of MAX_SONAR_EZ1_VPI
	 * @param device - The AnalogInput of the RangeFinder
	 */
	public MaxSonarEZ1(AnalogInput device) {
		super(device, MAX_SONAR_EZ1_VPI);
	}	
}

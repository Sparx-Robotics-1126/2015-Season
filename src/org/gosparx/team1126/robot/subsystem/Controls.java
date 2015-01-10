package org.gosparx.team1126.robot.subsystem;

import org.gosparx.team1126.robot.IO;

import edu.wpi.first.wpilibj.Joystick;

public class Controls extends GenericSubsystem{
 private Joystick driverJoy;
 
	private static Controls controls;
	private Drives drives;
	public static Controls getInstance(){
		if(controls == null){
			controls = new Controls("Controls", Thread.NORM_PRIORITY);
		}
		return controls;
	}
	
	private Controls(String name, int priority) {
		super(name, priority);
	}

	@Override
	protected boolean init() {
		driverJoy = new Joystick(IO.DRIVERJOYPORT);
		drives = Drives.getInstance(); 
		return true;
	}

	@Override
	protected boolean execute() {
		drives.setSpeed(driverJoy.getX(), driverJoy.getY());
		return false;
	}

	@Override
	protected long sleepTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void writeLog() {
		// TODO Auto-generated method stub
		
	}

}

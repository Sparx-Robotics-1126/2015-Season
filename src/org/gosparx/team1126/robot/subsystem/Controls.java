package org.gosparx.team1126.robot.subsystem;

import org.gosparx.team1126.robot.IO;
import edu.wpi.first.wpilibj.Joystick;
/**
 * This is how the controller is able to work with drives
 *
 */
public class Controls extends GenericSubsystem{
 private Joystick driverJoy;
 
	private static Controls controls;
	private Drives drives;
	public static synchronized Controls getInstance(){
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
		driverJoy = new Joystick(IO.DRIVER_JOYSTICK_PORT);
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
		return 20; //not sure if this is correct time needed
	}

	@Override
	protected void writeLog() {
		System.out.println("Enabling");
		
	}

}

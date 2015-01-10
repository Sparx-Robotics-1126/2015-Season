package org.gosparx.team1126.robot.subsystem;

import org.gosparx.team1126.robot.IO;
import edu.wpi.first.wpilibj.Joystick;
/**
 * This is how the controller is able to work with drives
 *
 */
public class Controls extends GenericSubsystem{
	/**
	 * declares a Joystick object named driverJoy
	 */
	private Joystick driverJoy;
	/**
	 * declares a Controls object named controls
	 */
	private static Controls controls;
	/**
	 * declares a Drives object named drives
	 */
	private Drives drives;
	/**
	 * if controls == null, make a new controls
	 * @return the new controls
	 */
	public static synchronized Controls getInstance(){
		if(controls == null){
			controls = new Controls("Controls", Thread.NORM_PRIORITY);
		}
		return controls;
	}
	/**
	 * @param name the name of the control
	 * @param priority the priority of the control
	 * constructor for the Controls
	 */
	private Controls(String name, int priority) {
		super(name, priority);
	}
	/**@return false ~ keeps looping true ~ stops loop
	 * instantiates a Joystick and Drives
	 */
	@Override
	protected boolean init() {
		driverJoy = new Joystick(IO.DRIVER_JOYSTICK_PORT);
		drives = Drives.getInstance(); 
		return true;
	}
	/**@return false ~ keeping looping true ~ end loop
	 * sets the speed of the control axis to drives
	 */
	@Override
	protected boolean execute() {
		drives.setSpeed(driverJoy.getX(), driverJoy.getY());
		return false;
	}
	/** *FIXED*
	 * The amount of time you want to sleep for after a cycle.
	 * 
	 * @return the number of milliseconds you want to sleep after a cycle.
	 */
	@Override
	protected long sleepTime() {
		return 20; 
	}
	/**
	 * Where all the logged info goes
	 */
	@Override
	protected void writeLog() {
		System.out.println("Enabling");

	}

}

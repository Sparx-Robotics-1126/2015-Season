package org.gosparx.team1126.robot.subsystem;

import org.gosparx.team1126.robot.IO;
import edu.wpi.first.wpilibj.Joystick;
/**
 * This is how the controller is able to work with drives
 * @author Mike the camel
 */
public class Controls extends GenericSubsystem{
	/**
	 * declares a Joystick object named driverJoyLeft
	 */
	private Joystick driverJoyLeft;
	/**
	 * declares a Joystick object named driverJoyRight
	 */
	private Joystick driverJoyRight;
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
			controls = new Controls();
		}
		return controls;
	}
	/**
	 * @param name the name of the control
	 * @param priority the priority of the control
	 * constructor for the Controls
	 */
	private Controls() {
		super("controls", Thread.NORM_PRIORITY);
	}
	/**
	 * instantiates a Joystick and Drives
	 * @return false ~ keeps looping true ~ stops loop
	 */
	@Override
	protected boolean init() {
		driverJoyLeft = new Joystick(IO.DRIVER_JOYSTICK_PORT1);
		driverJoyRight = new Joystick(IO.DRIVER_JOYSTICK_PORT2);
		drives = Drives.getInstance(); 
		return true;
	}
	/**
	 * sets the speed of the control axis to drives
	 * @return false ~ keeping looping true ~ end loop
	 */
	@Override
	protected boolean execute() {
		drives.setPower(driverJoyLeft.getY(), driverJoyRight.getY());
		return false;
	}
	/** 
	 * The amount of time you want to sleep for after a cycle.
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
	}

}

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
	
	/*/********************************************
	 *****************Logitech 1.0*****************
	 **********************************************
	 */
	private static final int LOGITECH_1_BUTTON_1		= 1;
	private static final int LOGITECH_1_BUTTON_2		= 2;
	private static final int LOGITECH_1_Y_AXIS 			= 1;
	private static final int LOGITECH_1_X_AXIS			= 0;
	private static final int LOGITECH_1_Z_AXIS			= 2;
	
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
		driverJoyLeft = new Joystick(IO.DRIVER_JOYSTICK_LEFT);
		driverJoyRight = new Joystick(IO.DRIVER_JOYSTICK_RIGHT);
		drives = Drives.getInstance(); 
		return true;
	}
	
	/**
	 * sets the speed of the control axis to drives
	 * @return false ~ keeping looping true ~ end loop
	 */
	@Override
	protected boolean execute() {
		drives.setPower(-driverJoyLeft.getRawAxis(LOGITECH_1_Y_AXIS),
						-driverJoyRight.getRawAxis(LOGITECH_1_Y_AXIS));
		if(driverJoyRight.getRawButton(LOGITECH_1_BUTTON_1)){
//			drives.setAutoFunction(Drives.State.AUTO_STEP_LINEUP);
			drives.driveStraight(36, 0);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
	
	@Override
	protected void liveWindow() {
		// TODO Auto-generated method stub
		
	}

}

package org.gosparx.team1126.robot.subsystem;

import org.gosparx.team1126.robot.IO;
import org.gosparx.team1126.robot.util.AdvancedJoystick;
import org.gosparx.team1126.robot.util.AdvancedJoystick.ButtonEvent;
import org.gosparx.team1126.robot.util.AdvancedJoystick.JoystickListener;

import org.gosparx.team1126.robot.util.AdvancedJoystick.MultibuttonEvent;

/**
 * This is how the controller is able to work with drives
 * @author Mike the camel
 * @author Alex Mechler {amechler1998@gmail.com}
 */
public class Controls extends GenericSubsystem implements JoystickListener{

	/**
	 * declares a Joystick object named driverJoyLeft
	 */
	private AdvancedJoystick driverJoyLeft;

	/**
	 * declares a Joystick object named driverJoyRight
	 */
	private AdvancedJoystick driverJoyRight;
	
	/**
	 * The Advanced joystick for the operator
	 */
	private AdvancedJoystick operatorJoy;
	
	/**
	 * declares a Controls object named controls
	 */
	private static Controls controls;

	/**
	 * declares a Drives object named drives
	 */
	private Drives drives;

	/**
	 * instance for CanAcquisition
	 */
	private CanAcquisition canAcq;
	
	//**************************************************************************
	//*****************************Logitech f310 mapping************************
	//**************************************************************************
	private static final int LOGI_LEFT_X_AXIS = 1;
	private static final int LOGI_LEFT_Y_AXIS = 2;
	private static final int LOGI_RIGHT_X_AXIS = 3;
	private static final int LOGI_RIGHT_Y_AXIS = 4;
	/**
	 * right = 1, left = -1
	 */
	private static final int LOGI_DPAD_X_AXIS = 5;
	/**
	 * up = -1, down = 1
	 */
	private static final int LOGI_DPAD_Y_AXIS = 6;
	private static final int LOGI_X = 1;
	private static final int LOGI_A = 2;
	private static final int LOGI_B = 3;
	private static final int LOGI_Y = 4;
	private static final int LOGI_L1 = 5;
	private static final int LOGI_R1 = 6;
	private static final int LOGI_L2 = 7;
	private static final int LOGI_R2 = 8;
	private static final int LOGI_BACK = 9;
	private static final int LOGI_START = 10;
	private static final int LOGI_L3 = 11;
	private static final int LOGI_R3 = 12;

	//********************************************************************
	//*******************Driver Controller Mapping**********************
	//********************************************************************
	private static final int ATTACK3_Y_AXIS = 2;
	private static final int ATTACK3_X_AXIS = 2;
	private static final int ATTACK3_Z_AXIS = 3;
	private static final int ATTACK3_TRIGGER = 1;
	private static final int ATTACK3_TOP_BUTTON = 2;

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
		driverJoyLeft = new AdvancedJoystick("Left Driver", IO.DRIVER_JOYSTICK_LEFT);
		driverJoyLeft.addActionListener(this);
		driverJoyLeft.addButton(ATTACK3_TOP_BUTTON);
		driverJoyLeft.addButton(ATTACK3_TRIGGER);
		driverJoyLeft.addMultibutton(ATTACK3_TRIGGER, ATTACK3_TOP_BUTTON);
		driverJoyLeft.start();
		driverJoyRight = new AdvancedJoystick("Right Driver", IO.DRIVER_JOYSTICK_RIGHT);
		driverJoyRight.addActionListener(this);
		driverJoyRight.addButton(ATTACK3_TOP_BUTTON);
		driverJoyRight.addButton(ATTACK3_TRIGGER);
		driverJoyRight.start();
		operatorJoy = new AdvancedJoystick("Operator Joy", IO.OPERATOR_JOYSTICK);
		operatorJoy.addActionListener(this);
		operatorJoy.addButton(LOGI_A);
		operatorJoy.addButton(LOGI_B);
		operatorJoy.addButton(LOGI_Y);
		operatorJoy.start();
		drives = Drives.getInstance();
		canAcq = CanAcquisition.getInstance();
		return true;
	}

	/**
	 * sets the speed of the control axis to drives
	 * @return false ~ keeping looping true ~ end loop
	 */
	@Override
	protected boolean execute() {
		
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

	/**
	 * Uses Livewindow 
	 */
	@Override
	protected void liveWindow() {
		
	}

	/**
	 * Responds to button events
	 * @param e - the buttonevent
	 */
	@Override
	public void actionPerformed(ButtonEvent e) {
		if(!(e instanceof MultibuttonEvent)){
			switch(e.getPort()){
			case IO.DRIVER_JOYSTICK_LEFT:
				switch(e.getID()){
				case ATTACK3_TOP_BUTTON:
					//TODO: Up Shift
					break;
				case ATTACK3_TRIGGER:
					//TODO: Down Shift
					break;
				}
				break;
			case IO.DRIVER_JOYSTICK_RIGHT:
				switch(e.getID()){
				case ATTACK3_TOP_BUTTON:
					//TODO: auto line up 
					break;
				case ATTACK3_TRIGGER:
					//TODO: force low gear
					break;
				}
				break;
				
			case IO.OPERATOR_JOYSTICK:
				switch(e.getID()){
				case LOGI_A:
					//TODO: Floor Mode
					break;
				case LOGI_B:
					//TODO: HP Mode
					break;
				case LOGI_Y:
					//TODO: Safe Mode
					break;
				}
				break;
			}
		}
		else{
			switch (e.getPort()) {
			case IO.DRIVER_JOYSTICK_LEFT:
				if(e.isRising()){
					//TODO trigger manual
				}
				break;
			}
		}
	}
}

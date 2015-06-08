package org.gosparx.team1126.robot.subsystem;

import org.gosparx.team1126.robot.Autonomous;
import org.gosparx.team1126.robot.IO;
import org.gosparx.team1126.robot.subsystem.ToteAcq.ClutchState;
import org.gosparx.team1126.robot.subsystem.ToteAcq.RollerPosition;
import org.gosparx.team1126.robot.subsystem.ToteAcq.StopState;
import org.gosparx.team1126.robot.util.AdvancedJoystick;
import org.gosparx.team1126.robot.util.AdvancedJoystick.ButtonEvent;
import org.gosparx.team1126.robot.util.AdvancedJoystick.JoystickListener;
import org.gosparx.team1126.robot.util.AdvancedJoystick.MultibuttonEvent;

import sun.rmi.runtime.Log;
import edu.wpi.first.wpilibj.Timer;

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

	/**
	 * Instance for ToteAcq
	private CanAcqTele canAcqTele
	 */
	private ToteAcq toteAcq;

	private Autonomous auto;

	/**
	 * Manual shifting on or off
	 */
	private boolean manualShifting = false;

	/**
	 * The operator wants controls over the PTO
	 */
	//private boolean operatorWantsControl = false;

	/**
	 * The speed at which the operator wants to power the PTO
	 */
	private double operatorWantedPower = 0;

	/**
	 * The wait for the tote stop to remove and the totes to score
	 */
	private double scoreWait = 0;

	private boolean useAuto = false;

	/**
	 * Instance for Elevations
	 */
	private Elevations elevations;

	/**
	 * Instance for CanAcqTele
	 */
	private CanAcqTele canAcqTele;

	//**************************************************************************
	//*****************************Logitech f310 mapping************************
	//**************************************************************************
	private static final int LOGI_LEFT_X_AXIS = 0;
	private static final int LOGI_LEFT_Y_AXIS = 1;
	private static final int LOGI_RIGHT_X_AXIS = 2;
	private static final int LOGI_RIGHT_Y_AXIS = 3;
	private static final int LOGI_POV = 0;
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
	private static final int ATTACK3_Y_AXIS = 1;
	private static final int ATTACK3_X_AXIS = 0;
	private static final int ATTACK3_Z_AXIS = 2;
	private static final int ATTACK3_TRIGGER = 1;
	private static final int ATTACK3_TOP_BUTTON = 2;

	private static final int NEW_JOY_Y_AXIS = 1;
	private static final int NEW_JOY_X_AXIS = 0;
	private static final int NEW_JOY_TRIGGER = 1;//TRIGGEr
	private static final int NEW_JOY_LEFT = 2;//LEFT
	private static final int NEW_JOY_RIGHT = 3;//RIGHT
	private static final int NEW_JOY_MIDDLE = 4;

	//***************************************************************************
	//***************************XBOX360*****************************************
	//***************************************************************************
	private static final int XBOX_A = 1;
	private static final int XBOX_B = 2;
	private static final int XBOX_X = 3;
	private static final int XBOX_Y = 4;
	private static final int XBOX_L1 = 5;
	private static final int XBOX_R1 = 6;
	private static final int XBOX_BACK = 7;
	private static final int XBOX_START = 8;
	private static final int XBOX_L3 = 9;
	private static final int XBOX_R3 = 10;
	private static final int XBOX_LEFT_X = 0;
	private static final int XBOX_LEFT_Y = 1;
	private static final int XBOX_L2 = 2;
	private static final int XBOX_R2 = 3;
	private static final int XBOX_RIGHT_X = 4;
	private static final int XBOX_RIGHT_Y = 5;
	private static final int XBOX_POV = 0;

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
		super("Controls", Thread.NORM_PRIORITY);
	}

	/**
	 * instantiates a Joystick and Drives
	 * @return false ~ keeps looping true ~ stops loop
	 */
	@Override
	protected boolean init() {
		driverJoyLeft = new AdvancedJoystick("Left Driver", IO.DRIVER_JOYSTICK_LEFT, 4, 0.05);
		driverJoyLeft.addActionListener(this);
		driverJoyLeft.addButton(NEW_JOY_LEFT);
		driverJoyLeft.addButton(NEW_JOY_TRIGGER);
		driverJoyLeft.addMultibutton(NEW_JOY_LEFT, NEW_JOY_TRIGGER);
		driverJoyLeft.start();
		driverJoyRight = new AdvancedJoystick("Right Driver", IO.DRIVER_JOYSTICK_RIGHT, 4, 0.05);
		driverJoyRight.addActionListener(this);
		driverJoyRight.addButton(NEW_JOY_LEFT);
		driverJoyRight.addButton(NEW_JOY_TRIGGER);
		driverJoyRight.addButton(NEW_JOY_MIDDLE);
		driverJoyRight.start();
		operatorJoy = new AdvancedJoystick("Operator Joy", IO.OPERATOR_JOYSTICK, 10, 0.25);
		operatorJoy.addActionListener(this);
		operatorJoy.addButton(XBOX_Y);
		operatorJoy.addButton(XBOX_R1);
		operatorJoy.addButton(XBOX_BACK);
		operatorJoy.addButton(XBOX_L1);
		operatorJoy.addButton(XBOX_L2);
		operatorJoy.addButton(XBOX_START);
		operatorJoy.addButton(XBOX_B);
		operatorJoy.addButton(XBOX_A);
		operatorJoy.addButton(XBOX_X);
		operatorJoy.start();
		drives = Drives.getInstance();
		canAcq = CanAcquisition.getInstance();
		canAcqTele = CanAcqTele.getInstance();
		toteAcq = ToteAcq.getInstance();
		elevations = Elevations.getInstance();
		auto = Autonomous.getInstance();
		return true;
	}

	/**
	 * sets the speed of the control axis to drives
	 * @return false ~ keeping looping true ~ end loop
	 */
	@Override
	protected boolean execute() {
		if(ds.isOperatorControl()){
			double left = -driverJoyLeft.getAxis(NEW_JOY_Y_AXIS);
			double right = -driverJoyRight.getAxis(NEW_JOY_Y_AXIS);
			//TRIMS
			double hookOveride = operatorJoy.getAxis(XBOX_LEFT_X);
			double rotateOveride = -operatorJoy.getAxis(XBOX_RIGHT_Y);
			if(Math.abs(rotateOveride) > 0){
				System.out.println("**************************************ROTATE");
				elevations.scoreTotes();
				canAcqTele.setState(CanAcqTele.RotateState.STANDBY);
				canAcqTele.overriding(true);
				canAcqTele.manualRotateOverride(rotateOveride);
			}else if(Math.abs(hookOveride) > 0){
				System.out.println("**************************************HOOK");
				canAcqTele.setState(CanAcqTele.HookState.STANDBY);
				canAcqTele.overriding(true);
				canAcqTele.manualHookOverride(hookOveride);
			}else{
				canAcqTele.overriding(false);
			}


			//Driver vs Operator
			drives.setPower(left, right, true);
			if(scoreWait == 0 || Timer.getFPGATimestamp() > scoreWait + 0.1){
				drives.setPTO(operatorWantedPower);
			}
			if(left == 0 && right == 0 && useAuto){
				auto.runAuto(true);
			}else{
				useAuto = false;
				auto.runAuto(false);
			}

			//OPERATOR CONTORLS
			if(operatorJoy.getPOV(XBOX_POV) == 90){
				//Human Feed Mode
				elevations.liftTote();
				toteAcq.setClutch(ClutchState.ON);
				toteAcq.setRollerPos(RollerPosition.HUMAN_PLAYER);
				toteAcq.setStopper(StopState.ON);
				//operatorWantsControl = true;
				operatorWantedPower = -0.8;
				LOG.logMessage("OP Button: Human Feed Mode");
			}else if(operatorJoy.getPOV(XBOX_POV) == 180){
				//Floor Mode
				elevations.liftTote();
				toteAcq.setClutch(ClutchState.ON);
				toteAcq.setRollerPos(RollerPosition.FLOOR);
				toteAcq.setStopper(StopState.ON);
				//operatorWantsControl = true;
				operatorWantedPower = -0.8;
				LOG.logMessage("OP Button: Floor Mode");
			}else if(operatorJoy.getPOV(XBOX_POV) == 0){
				//TODO: OFF Mode
				toteAcq.setClutch(ClutchState.OFF);
				toteAcq.setStopper(StopState.ON);
				toteAcq.setRollerPos(RollerPosition.TRAVEL);
				scoreWait = 0;
				//operatorWantsControl = false;
				LOG.logMessage("OP Button: OFF Mode");
			}else if(operatorJoy.getPOV(XBOX_POV) == 270){
				//EJECT
				toteAcq.setClutch(ClutchState.ON);
				toteAcq.setStopper(StopState.ON);
				toteAcq.setRollerPos(RollerPosition.FLOOR);
				//operatorWantsControl = true;
				operatorWantedPower = 0.8;
				LOG.logMessage("OP Button: Eject");
			}
			if(operatorJoy.getAxis(XBOX_L2) > .5){
				canAcqTele.acquireCan(false);
				LOG.logMessage("Acquiring Can");
			}
			if(operatorJoy.getAxis(XBOX_R2) > .5){
				canAcqTele.setAutoPosition(40);
				LOG.logMessage("Capping Stack");
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
		LOG.logMessage("DR Left axis: " + -driverJoyLeft.getAxis(ATTACK3_Y_AXIS));
		LOG.logMessage("DR Right axis: " + -driverJoyRight.getAxis(ATTACK3_Y_AXIS));
		//LOG.logMessage("Operator wants control: " + operatorWantsControl);
		LOG.logMessage("Operator Wanted Power: " + operatorWantedPower);
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
		if(ds.isOperatorControl()){
			//		useAuto = false;
			if(!(e instanceof MultibuttonEvent)){
				switch(e.getPort()){
				case IO.DRIVER_JOYSTICK_LEFT:
					switch(e.getID()){
					case NEW_JOY_LEFT:
						drives.setManualShifting(true);
						LOG.logMessage("DR Button: HIGH GEAR");
						break;
					case NEW_JOY_TRIGGER:
						drives.setManualShifting(false);
						LOG.logMessage("DR Button: LOW GEAR");
						break;
					}
					break;

				case IO.DRIVER_JOYSTICK_RIGHT:
					switch(e.getID()){
					case NEW_JOY_TRIGGER:
						drives.driveStraight(10, 100);
						LOG.logMessage("DR Button: Auto Drive Straight");
						break;
					case NEW_JOY_MIDDLE:
						useAuto = true;
						LOG.logMessage("USING AUTO");
						break;
					}
					break;

				case IO.OPERATOR_JOYSTICK:
					switch(e.getID()){
					case XBOX_Y:
						//Reset Elevator
						elevations.setHome();					
						LOG.logMessage("OP Button: Elevations reset");
						break;
					case XBOX_R1:
						//SCORE
						elevations.scoreTotes();
						toteAcq.setClutch(ClutchState.ON);
						toteAcq.setStopper(StopState.OFF);
						scoreWait = Timer.getFPGATimestamp();
						//operatorWantsControl = true;
						operatorWantedPower = -0.75;
						LOG.logMessage("OP Button: Score");
						break;
					case XBOX_START:
						//STOP CAN TELE
						canAcqTele.setState(CanAcqTele.HookState.STANDBY);
						canAcqTele.setState(CanAcqTele.RotateState.STANDBY);
						LOG.logMessage("OP Button: Stop Can Tele");
						break;
					case XBOX_B:
						//STOP
						elevations.stopElevator();
						LOG.logMessage("OP Button: Stop");
						break;
					case XBOX_X:
						canAcqTele.setAutoPosition(85);
						elevations.scoreTotes();
						LOG.logMessage("Floor pickup");
						break;
					case XBOX_L1:
						elevations.scoreTotes();
						canAcqTele.goToAcquire();
						LOG.logMessage("OP Button: Dropping Can Tele");
						break;
						//				case XBOX_B:
						//					if(e.isRising()){
						//						canAcq.setAutoFunction(CanAcquisition.State.ATTEMPT_TO_GRAB);
						//						LOG.logMessage("OP Button: Can Auto Arms OPEN");
						//					}else{
						//						canAcq.setAutoFunction(CanAcquisition.State.DISABLE);
						//						LOG.logMessage("OP Button: Can Auto Arms CLOSE");
						//					}
						//					break;
					case XBOX_A:
						elevations.lowerTotes();
						LOG.logMessage("OP Button: Lowering Elevations");
						break;
					case XBOX_L3:
						elevations.scoreTotes();
						toteAcq.setClutch(ClutchState.OFF);
						toteAcq.setRollerPos(RollerPosition.HUMAN_PLAYER);
						toteAcq.setStopper(StopState.ON);
						break;
					case XBOX_BACK:
						if(e.isRising())
							canAcqTele.toggleFloorGrabbers();
					}
				}
			}else{
				switch (e.getPort()) {
				case IO.DRIVER_JOYSTICK_LEFT:
					if(e.isRising()){
						manualShifting = !manualShifting;
						drives.isManualShifting(manualShifting);
						LOG.logMessage("DR Button: Manual Shifting is enabled? " + manualShifting);
					}
					break;
				}
			}
		}
	}
}

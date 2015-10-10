package org.gosparx.team1126.robot.subsystem;

import org.gosparx.team1126.robot.IO;
import org.gosparx.team1126.robot.sensors.MagnetSensor;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class CanAcqAuto extends GenericSubsystem{
	
	/**
	 * A solenoid that holds the arms
	 */
	private Solenoid armHolder;
	
	/**
	 * Bob likes to determine when to stop moving the arms
	 */
	private MagnetSensor limitSwitch;
	
	/**
	 * The victor that moves the arm up
	 */
	private Victor armController;
	
	/**
	 * The current state the arms are in
	 */
	private State currentState;
	
	private static final double RAISE_POWER = 1.0;
	
	private static CanAcqAuto canAcqAuto;
	
	public static CanAcqAuto getInstance(){
		if(canAcqAuto == null){
			canAcqAuto = new CanAcqAuto();
		}
		return canAcqAuto;
	}
	
	private CanAcqAuto() {
		super("CanAcqAuto", Thread.NORM_PRIORITY);
	}

	
	@Override
	protected boolean init() {
		armHolder = new Solenoid(IO.PNU_CAN_ARM_CONTROLLER);
		limitSwitch = new MagnetSensor(IO.DIO_CAN_ARM_CONTROLLER, false);
		armController = new Victor(IO.PWM_CAN_ARM_CONTROLLER);
		currentState = State.STANDBY;
		return false;
	}

	@Override
	protected void liveWindow() {
		LiveWindow.addActuator(getName(), "Holder", armHolder);
		LiveWindow.addActuator(getName(), "Window Motor", armController);
	}

	@Override
	protected boolean execute() {
		switch(currentState){
		case ARMS_DROPPING:
			armHolder.set(false);
			currentState = State.STANDBY;
			break;
		case ARMS_HELD:
			armHolder.set(true);
			currentState = State.STANDBY;
			break;
		case ARMS_RAISING:
			if(!limitSwitch.isTripped()){
				armController.set(RAISE_POWER);
			}else{
				armController.set(0);
				currentState = State.ARMS_HELD;				
			}
			break;
		case STANDBY:
			break;
		default:
			LOG.logError("Unknown State: " + currentState);
			break;
		}
		return false;
	}

	@Override
	protected long sleepTime() {
		return 20;
	}

	@Override
	protected void writeLog() {
		LOG.logMessage("Current State: " + currentState);
	}


	private enum State{
		ARMS_HELD,
		ARMS_RAISING,
		ARMS_DROPPING,
		STANDBY;
		
		@Override
		public String toString(){
			switch(this){
			case ARMS_HELD:
				return("Arms are being held");
			case ARMS_RAISING: 
				return("Arms are being raised");
			case ARMS_DROPPING:
				return("Arms are dropped");
			case STANDBY:
				return("In standby");
				default: return("Error not in a state");
			}
		}
	}

}

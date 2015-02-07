package org.gosparx.team1126.robot.subsystem;

import org.gosparx.team1126.robot.IO;
import org.gosparx.team1126.robot.sensors.EncoderData;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class CanAcqTele2 extends GenericSubsystem{

	/************************Objects**********************/

	private static CanAcqTele2 canAcq;
	
	private Talon rotateMotor;
	
	private Talon hookMotor;
	
	private Encoder rotateEnc;
	
	private EncoderData rotateEncData;
	
	private Encoder hookEnc;
	
	private EncoderData hookEncData;
	
	/***********************Constants*********************/
	
	/**
	 * The distance the hook will travel per tick 
	 */
	private static final double DISTANCE_PER_TICK_HOOK = .0040614375;

	/**
	 * The distance the arms will rotate per tick 
	 */
	private static final double DISTANCE_PER_TICK_ROTATE = (1/112) / 256;

	private static final double DISTANCE_PER_TOTE = 13.0;
	
	private static final double MIN_ROTATE_UP_SPEED = 0.4;
	
	private static final double MIN_ROTATE_DOWN_SPEED = 0.2;
	
	private static final double MIN_HOOK_SPEED = 0.4;
	
	/***********************Variables*********************/
	
	private double currAngle;
	
	private double currHookPos;
	
	private double wantedAngle;
	
	private double wantedHookPos;
	
	private HookState currentHookState;
	
	private RotateState currentRotateState;
	
	public static synchronized CanAcqTele2 getInstance(){
		if(canAcq == null){
			canAcq = new CanAcqTele2();
		}
		return canAcq;
	}
	
	private CanAcqTele2() {
		super("CanAcqTele", Thread.NORM_PRIORITY);
	}

	@Override
	protected boolean init() {
		rotateMotor = new Talon(IO.PWM_CAN_ROTATE);
		rotateEnc = new Encoder(IO.DIO_CAN_ROTATE_A, IO.DIO_CAN_ROTATE_B);
		rotateEncData = new EncoderData(rotateEnc, DISTANCE_PER_TICK_ROTATE);
		hookMotor = new Talon(IO.PWM_CAN_HOOK);
		hookEnc = new Encoder(IO.DIO_CAN_HOOK_A, IO.DIO_CAN_HOOK_B);
		hookEncData = new EncoderData(hookEnc, DISTANCE_PER_TICK_HOOK);
		return true;
	}
	
	/**
	 *Overrides in genericSubsytems, does things
	 */
	@Override
	protected void liveWindow() {
		LiveWindow.addActuator(getName(), "Rotating Victor", rotateMotor);
		LiveWindow.addActuator(getName(), "Hooking Victor", hookMotor);
	}

	@Override
	protected boolean execute() {
		return false;
	}

	@Override
	protected long sleepTime() {
		return 20;
	}

	@Override
	protected void writeLog() {

	}

	public enum HookState{
		STANDBY,
		MOVING;

		public String toString(){
			switch(this){
			case STANDBY:
				return "In standby";
			case MOVING:
				return "Moving";
			default:
				return "Error" + this;
			}
		}
	}
	public enum RotateState{
		STANDBY,
		ROTATING;
		
		public String toString(){
			switch(this){
			case STANDBY:
				return "In standby";
			case ROTATING:
				return "Rotating";
			default:
				return "Error" + this;
			}
		}
	}
}

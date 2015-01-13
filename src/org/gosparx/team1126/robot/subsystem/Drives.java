package org.gosparx.team1126.robot.subsystem;

import org.gosparx.sensors.EncoderData;
import org.gosparx.team1126.robot.IO;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
/**
 * Makes the drives system to have the robot move.
 *
 */
public class Drives extends GenericSubsystem{
	/**
	 * Makes a Drives object called drives
	 */
	private static Drives drives;
	/**
	 * makes the left front victor
	 */
	private Victor leftFront;
	/**
	 * makes the left back victor
	 */
	private Victor leftBack;
	/**
	 * makes the right front victor
	 */
	private Victor rightFront;
	/**
	 * makes the right back victor
	 */
	private Victor rightBack;
	/**
	 * makes the left encoder
	 */
	private Encoder encoderLeft;
	/**
	 * makes the right encoder
	 */
	private Encoder encoderRight;
	/**
	 * makes the left encoder data
	 */
	private EncoderData encoderDataLeft;
	/**
	 * makes the right encoder data
	 */
	private EncoderData encoderDataRight;
	/**
	 * the solenoid used for shifting
	 */
	private Solenoid shiftingSol;
	/**
	 * the amount of distance the shortbot will make per tick
	 */
	private final double DISTANCE_PER_TICK = 0.04908738;
	/**
	 * the wanted speed for the left motors
	 */
	private double wantedLeftPower;
	/**
	 * the wanted speed for the right motors
	 */
	private double wantedRightPower;
	/**
	 * the current state the drives is in
	 */
	private State currentDriveState;
	/**
	 * the current average speed between the left and right motors
	 */
	private double currentSpeed;
	/**
	 * the speed required to shift down
	 */
	private static final double LOWERSHIFTSPEED = 30;
	/**
	 * the speed required to shift up
	 */
	private static final double UPPERSHIFTSPEED = 70;
	/**
	 * the time required to shift
	 */
	private static final double SHIFTING_TIME = 0.5;
	/**
	 * actual time it took to shift
	 */
	private double shiftTime;
	/**
	 * the speed required to shift
	 */
	private static final double SHIFTINGSPEED = 0.25;
	/**
	 * determines if it's in high or low gear
	 */
	private static final boolean LOW_GEAR = false;

	/**
	 * if drives == null, make a new drives
	 * @return the new drives
	 */
	public static synchronized Drives getInstance(){
		if(drives == null){
			drives = new Drives("Drives", Thread.NORM_PRIORITY);
		}
		return drives;
	}
	
	/**
	 * constructor for drives
	 * @param name drives name
	 * @param priority drives priority for execute
	 */
	private Drives(String name, int priority) {
		super(name, priority);
	}
	
	/**
	 * instantiates all the objects
	 * @return if false, keep looping, true loop ends
	 */
	@Override
	protected boolean init() {
		leftFront = new Victor(IO.PWM_LEFT_FRONT_DRIVES);
		leftBack = new Victor(IO.PWM_LEFT_BACK_DRIVES);
		rightFront = new Victor(IO.PWM_RIGHT_FRONT_DRIVES);
		rightBack = new Victor(IO.PWM_RIGHT_BACK_DRIVES);
		encoderLeft = new Encoder(IO.PWM_LEFT_DRIVES_ENCODERA, IO.PWM_LEFT_DRIVES_ENCODERB);
		encoderDataLeft = new EncoderData(encoderLeft, DISTANCE_PER_TICK);
		encoderRight = new Encoder(IO.PWM_RIGHT_DRIVES_ENCODERA, IO.PWM_RIGHT_DRIVES_ENCODERB);
		encoderDataRight = new EncoderData(encoderRight, DISTANCE_PER_TICK);
		shiftingSol = new Solenoid(IO.PNU_SHIFTING);
		wantedLeftPower = 0;
		wantedRightPower = 0;
		currentDriveState = State.IN_LOW_GEAR;
		currentSpeed = 0;
		shiftTime = 0;
		return true;
	}
	
	/**
	 * determines if it needs to be shifted
	 * @return if false, keep looping, true end loop
	 */
	@Override
	protected boolean execute() {
		encoderDataRight.calculateSpeed();
		encoderDataLeft.calculateSpeed();
		currentSpeed = (encoderDataRight.getSpeed() + encoderDataLeft.getSpeed()) / 2;
		switch(currentDriveState){
		case IN_LOW_GEAR:
			if(currentSpeed >= LOWERSHIFTSPEED){
				shiftingSol.set(!LOW_GEAR);
				shiftTime = Timer.getFPGATimestamp();
				currentDriveState = State.SHIFTING_HIGH;
			}
			break;
		case SHIFTING_LOW:
			if(Timer.getFPGATimestamp() >= shiftTime + SHIFTING_TIME){
				currentDriveState = State.IN_LOW_GEAR;
			}
			if(currentSpeed < 0){
				wantedRightPower = SHIFTINGSPEED * - 1;
				wantedLeftPower = SHIFTINGSPEED * - 1;
			}else wantedRightPower = SHIFTINGSPEED; wantedLeftPower = SHIFTINGSPEED;
			break;
		case IN_HIGH_GEAR:
			if(currentSpeed >= UPPERSHIFTSPEED){
				shiftingSol.set(LOW_GEAR);
				shiftTime = Timer.getFPGATimestamp();
				currentDriveState = State.SHIFTING_LOW;
			}
			break;
		case SHIFTING_HIGH:
			if(Timer.getFPGATimestamp() >= shiftTime + SHIFTING_TIME){
				currentDriveState = State.IN_HIGH_GEAR;
			}
			if(currentSpeed < 0){
				wantedRightPower = SHIFTINGSPEED * - 1;
				wantedLeftPower = SHIFTINGSPEED * - 1;
			}else wantedRightPower = SHIFTINGSPEED; wantedLeftPower = SHIFTINGSPEED;
			break;
		default:
			System.out.println("Error currentDriveState = " + currentDriveState);
		}
		leftFront.set(wantedLeftPower);
		leftBack.set(wantedLeftPower);
		rightFront.set(wantedRightPower);
		rightBack.set(wantedRightPower);
		return false;
	}

	/**
	 * The amount of time you want to sleep for after a cycle.
	 * 
	 * @return the number of milliseconds you want to sleep after a cycle.
	 */
	@Override
	protected long sleepTime() {
		return 10;
	}

	/**
	 * Where all the logged info goes
	 */
	@Override
	protected void writeLog() {
		System.out.println("Enabling");
	}
	
	/**
	 * sets the wanted left and right speed to the speed sent in inches
	 * @param left left motor speed
	 * @param right right motor speed
	 */
	public void setSpeed(double left, double right) {
		wantedLeftPower = left;
		wantedRightPower = right;
	}
	/**
	 *Makes the states for drives
	 */
	public enum State{
		IN_LOW_GEAR,
		SHIFTING_LOW,
		IN_HIGH_GEAR,
		SHIFTING_HIGH;

		/**
		 * Gets the name of the state
		 * @return the correct state 
		 */
		@Override
		public String toString(){
			switch(this){
			case IN_LOW_GEAR:
				return "In low gear";
			case SHIFTING_LOW:
				return "Shifting Low";
			case IN_HIGH_GEAR:
				return "In high gear";
			case SHIFTING_HIGH:
				return "Shifting high";
			default:
				return "Error";
			}
		}
	}
}

package org.gosparx.team1126.robot.subsystem;

import org.gosparx.sensors.EncoderData;
import org.gosparx.team1126.robot.IO;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
/**
 * 
 * @author Mike
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
	private Encoder encodeLeft;
	/**
	 * makes the right encoder
	 */
	private Encoder encodeRight;
	/**
	 * makes the left encoder data
	 */
	private EncoderData encodeDataLeft;
	/**
	 * makes the right encoder data
	 */
	private EncoderData encodeDataRight;
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
	private double wantedLeftSpeed = 0;
	/**
	 * the wanted speed for the right motors
	 */
	private double wantedRightSpeed = 0;
	/**
	 * the current state the drives is in
	 */
	private int currentDriveState = State.IN_LOW_GEAR;
	/**
	 * the current average speed between the left and right motors
	 */
	private double currentSpeed = 0;
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
	private double shiftTime = 0;
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
	public static Drives getInstance(){
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
		leftFront = new Victor(IO.LEFTFRONTDRIVES);
		leftBack = new Victor(IO.LEFTBACKDRIVES);
		rightFront = new Victor(IO.RIGHTFRONTDRIVES);
		rightBack = new Victor(IO.RIGHTBACKDRIVES);
		encodeLeft = new Encoder(IO.LEFTDRIVESENCODERA, IO.LEFTDRIVESENCODERB);
		encodeDataLeft = new EncoderData(encodeLeft, DISTANCE_PER_TICK);
		encodeRight = new Encoder(IO.RIGHTDRIVESENCODERA, IO.RIGHTDRIVESENCODERB);
		encodeDataRight = new EncoderData(encodeRight, DISTANCE_PER_TICK);
		shiftingSol = new Solenoid(IO.SHIFTINGPNU);
		return true;
	}
	/**
	 * determines if it needs to be shifted
	 * @return if false, keep looping, true end loop
	 */
	@Override
	protected boolean execute() {
		encodeDataRight.calculateSpeed();
		encodeDataLeft.calculateSpeed();
		currentSpeed = (encodeDataRight.getSpeed() + encodeDataLeft.getSpeed()) / 2;

		switch(currentDriveState){
		case State.IN_LOW_GEAR:
			if(currentSpeed >= LOWERSHIFTSPEED){
				shiftingSol.set(!LOW_GEAR);
				shiftTime = Timer.getFPGATimestamp();
				currentDriveState = State.SHIFTING_HIGH;
			}
			break;
		case State.SHIFTING_LOW:
			if(Timer.getFPGATimestamp() >= shiftTime + SHIFTING_TIME){
				currentDriveState = State.IN_LOW_GEAR;
			}
			wantedRightSpeed = SHIFTINGSPEED;
			wantedLeftSpeed = SHIFTINGSPEED;
			break;
		case State.IN_HIGH_GEAR:
			if(currentSpeed >= UPPERSHIFTSPEED){
				shiftingSol.set(LOW_GEAR);
				shiftTime = Timer.getFPGATimestamp();
				currentDriveState = State.SHIFTING_LOW;
			}
			break;
		case State.SHIFTING_HIGH:
			if(Timer.getFPGATimestamp() >= shiftTime + SHIFTING_TIME){
				currentDriveState = State.IN_HIGH_GEAR;
			}
			wantedRightSpeed = SHIFTINGSPEED;
			wantedLeftSpeed = SHIFTINGSPEED;
			break;
		default:
			System.out.println("Error currentDriveState = " + currentDriveState);
		}
		leftFront.set(wantedLeftSpeed);
		leftBack.set(wantedLeftSpeed);
		rightFront.set(wantedRightSpeed);
		rightBack.set(wantedRightSpeed);
		return false;
	}
	/**
	 * The amount of time you want to sleep for after a cycle.
	 * 
	 * @return the number of milliseconds you want to sleep after a cycle.
	 */
	@Override
	protected long sleepTime() {
		return 0;
	}
	
	/**
	 * Where all the logged info goes
	 */
	@Override
	protected void writeLog() {

	}
	/**
	 * sets the wanted left and right speed to the speed sent in
	 * @param left left motor speed
	 * @param right right motor speed
	 */
	public void setSpeed(double left, double right) {
		wantedLeftSpeed = left;
		wantedRightSpeed = right;
	}
	/**
	 * 
	 * @author Mike
	 *
	 */
	private static final class State{
		public static final int IN_LOW_GEAR		= 0;
		public static final int SHIFTING_LOW	= 1;
		public static final int IN_HIGH_GEAR	= 2;
		public static final int SHIFTING_HIGH	= 3;

		/**
		 * Gets the name of the state
		 * @param num is used to get the correct State
		 * @return the correct state 
		 */
		public String getStateName(int num){
			switch(num){
			case IN_LOW_GEAR:
				return "In low gear";
			case SHIFTING_LOW:
				return "Shifting into low gear";
			case IN_HIGH_GEAR:
				return "In high gear";
			case SHIFTING_HIGH:
				return "Shifting into high gear";
			default: return "Error";
			}
		}

	}
}

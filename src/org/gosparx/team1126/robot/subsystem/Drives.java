package org.gosparx.team1126.robot.subsystem;

import org.gosparx.sensors.EncoderData;
import org.gosparx.team1126.robot.IO;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

public class Drives extends GenericSubsystem{

	private static Drives drives;
	private Victor leftFront;
	private Victor leftBack;
	private Victor rightFront;
	private Victor rightBack;
	private Encoder encodeLeft;
	private Encoder encodeRight;
	private EncoderData encodeDataLeft;
	private EncoderData encodeDataRight;
	private Solenoid shiftingSol;
	private final double DISTANCE_PER_TICK = 0.04908738;
	private double wantedLeftSpeed = 0;
	private double wantedRightSpeed = 0;
	private int currentDriveState = State.IN_LOW_GEAR;
	private double currentSpeed = 0;
	private static final double LOWERSHIFTSPEED = 30;
	private static final double UPPERSHIFTSPEED = 70;
	private static final double SHIFTING_TIME = 0.5;
	private double shiftTime = 0;
	private static final double SHIFTINGSPEED = 0.25;
	private static final boolean LOW_GEAR = false;

	public static Drives getInstance(){
		if(drives == null){
			drives = new Drives("Drives", Thread.NORM_PRIORITY);
		}
		return drives;
	}

	private Drives(String name, int priority) {
		super(name, priority);
	}

	@Override
	protected boolean init() {
		leftFront = new Victor(IO.leftFrontDrives);
		leftBack = new Victor(IO.leftBackDrives);
		rightFront = new Victor(IO.rightFrontDrives);
		rightBack = new Victor(IO.rightBackDrives);
		encodeLeft = new Encoder(IO.leftDrivesEncoderA, IO.leftDrivesEncoderB);
		encodeDataLeft = new EncoderData(encodeLeft, DISTANCE_PER_TICK);
		encodeRight = new Encoder(IO.rightDrivesEncoderA, IO.rightDrivesEncoderB);
		encodeDataRight = new EncoderData(encodeRight, DISTANCE_PER_TICK);
		shiftingSol = new Solenoid(IO.shiftingPnu);
		return true;
	}

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

	@Override
	protected long sleepTime() {
		return 0;
	}

	@Override
	protected void writeLog() {

	}

	public void setSpeed(double left, double right) {
		wantedLeftSpeed = left;
		wantedRightSpeed = right;
	}
	private static final class State{
		public static final int IN_LOW_GEAR		= 0;
		public static final int SHIFTING_LOW	= 1;
		public static final int IN_HIGH_GEAR	= 2;
		public static final int SHIFTING_HIGH	= 3;

	}
}

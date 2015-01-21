package org.gosparx.team1126.robot.subsystem;

import org.gosparx.sensors.EncoderData;
import org.gosparx.team1126.robot.IO;
import org.gosparx.team1126.robot.sensors.ColorSensor;
import org.gosparx.team1126.robot.sensors.ColorSensor.Color;
import org.gosparx.team1126.robot.sensors.PID;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
/**
 * Makes the drives system to have the robot move.
 *@author Mike the camel
 */
public class Drives extends GenericSubsystem{
	/**
	 * Makes a drives object that will be called to use the drives class
	 */
	private static Drives drives;
	/**
	 * Object used to control the left front motor
	 */
	private Victor leftFront;
	/**
	 * Object used to control the left back motor
	 */
	private Victor leftBack;
	/**
	 * Object used to control the right front motor
	 */
	private Victor rightFront;
	/**
	 * Object used to control the right back motor
	 */
	private Victor rightBack;
	/**
	 * Used to get the distance the robot has traveled 
	 */
	private Encoder encoderLeft;
	/**
	 * Used to get the distance the robot has traveled
	 */
	private Encoder encoderRight;
	/**
	 * makes the left encoder data which calculates how far the robot traveled in inches
	 */
	private EncoderData encoderDataLeft;
	/**
	 * makes the right encoder data which calculates how far the robot traveled in inches
	 */
	private EncoderData encoderDataRight;
	/**
	 * the solenoid used for shifting
	 */
	private Solenoid shiftingSol;
	/**
	 * the left color sensor for detecting the colors in front of the robot
	 */
	private ColorSensor colorSensorLeft;
	/**
	 * the right color sensor for detecting the colors in front of the robot
	 */
	private ColorSensor colorSensorRight;
	
	private DigitalInput rightTouch;
	private DigitalInput leftTouch;
	
	private PID rightPID;
	private PID leftPID;
	
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
	 * the speed required to shift down, not accurate yet
	 */
	private static final double LOWERSHIFTSPEED = 20;
	/**
	 * the speed required to shift up, not accurate yet
	 */
	private static final double UPPERSHIFTSPEED = 40;
	/**
	 * the time required to shift, not accurate yet, in seconds
	 */
	private static final double SHIFTING_TIME = 0.15;
	/**
	 * actual time it took to shift
	 */
	private double shiftTime;
	/**
	 * 
	 */
	public static final double DEAD_ZONE = 0.05;
	/**
	 * the speed required to shift
	 */
	private static final double SHIFTINGSPEED = 0.35;
	/**
	 * determines if it's in high or low gear
	 */
	private static final boolean LOW_GEAR = false;
	/**
	 * Variable for determining which state the color sensor
	 */
	private State autoFunctions;

	private double rightPower;
	private double leftPower;

	/**
	 * stops the motors for auto
	 */
	private static final int STOP_MOTOR = 0;
	/**
	 * if drives == null, make a new drives
	 * @return the new drives
	 */
	public static synchronized Drives getInstance(){
		if(drives == null){
			drives = new Drives();
		}
		return drives;
	}

	/**
	 * constructor for drives
	 * @param name drives name
	 * @param priority drives priority for execute
	 */
	private Drives() {
		super("Drives", Thread.NORM_PRIORITY);
	}

	/**
	 * instantiates all the objects
	 * @return if false, keep looping, true loop ends
	 */
	@Override
	protected boolean init() {
		rightFront = new Victor(IO.PWM_RIGHT_FRONT_DRIVES);
		rightBack = new Victor(IO.PWM_RIGHT_BACK_DRIVES);
		leftFront = new Victor(IO.PWM_LEFT_FRONT_DRIVES);
		leftBack = new Victor(IO.PWM_LEFT_BACK_DRIVES);
		encoderLeft = new Encoder(IO.ENCODER_LEFT_DRIVES_A, IO.ENCODER_LEFT_DRIVES_B);
		encoderDataLeft = new EncoderData(encoderLeft, DISTANCE_PER_TICK);
		leftPID = new PID(0.026, 0.02, 1, 0.00, true, false);
		encoderRight = new Encoder(IO.ENCODER_RIGHT_DRIVES_A, IO.ENCODER_RIGHT_DRIVES_B);
		encoderDataRight = new EncoderData(encoderRight, DISTANCE_PER_TICK);
		rightPID = new PID(0.026, 0.02, 1, 0.00, true, false);
		shiftingSol = new Solenoid(IO.PNU_SHIFTING);
		colorSensorLeft = new ColorSensor(IO.COLOR_LEFT_RED, IO.COLOR_LEFT_BLUE, IO.COLOR_LEFT_LED);
		colorSensorRight = new ColorSensor(IO.COLOR_RIGHT_RED, IO.COLOR_RIGHT_BLUE, IO.COLOR_RIGHT_LED);
		rightTouch = new DigitalInput(7);
		leftTouch = new DigitalInput(6);
		leftPower = 0;
		rightPower = 0;
		currentDriveState = State.IN_LOW_GEAR;
		currentSpeed = 0;
		shiftTime = 0;
		autoFunctions = State.AUTO_STAND_BY;
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
			if(Math.abs(currentSpeed) >= UPPERSHIFTSPEED){
				System.out.println("SHIFTING HIGH");
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
				rightPower = SHIFTINGSPEED * - 1;
				leftPower = SHIFTINGSPEED * - 1;
			}else{
				rightPower = SHIFTINGSPEED;

				leftPower = SHIFTINGSPEED;
			}
			break;
		case IN_HIGH_GEAR:
			if(Math.abs(currentSpeed) <= LOWERSHIFTSPEED){
				System.out.println("SHIFTING LOW");
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
				rightPower = SHIFTINGSPEED * - 1;
				leftPower = SHIFTINGSPEED * - 1;
			}else{
				rightPower = SHIFTINGSPEED;
				leftPower = SHIFTINGSPEED;
			}
			break;
		default:
			System.out.println("Error currentDriveState = " + currentDriveState);
		}

		switch(autoFunctions){
		case AUTO_STAND_BY:
			if(currentDriveState == State.IN_HIGH_GEAR || currentDriveState == State.IN_LOW_GEAR){
				rightPower = wantedRightPower;
				leftPower = wantedLeftPower;
			}
			break;
		case AUTO_LIGHT_LINE_UP:
			boolean rightWhite = colorSensorRight.isColor(Color.WHITE);
			boolean leftWhite = colorSensorLeft.isColor(Color.WHITE);
			if(leftWhite){
				leftPower = -0.5;
			}else{
				leftPower = 0.4;
			}
			if(rightWhite){
				rightPower = -0.5;
			}else{
				rightPower = 0.4;
			}
			if(rightWhite && leftWhite){
				autoFunctions = State.AUTO_STAND_BY;
				leftPower = 0;
				rightPower = 0;
			}
			break;
		case AUTO_STEP_LINEUP:
			boolean right = rightTouch.get();
			boolean left = leftTouch.get();
			if(right){
				rightPower = 0;
			}else{
				rightPower = 0.2;
			}
			if(left){
				leftPower = 0;
			}else{
				leftPower = 0.2;
			}
			if(left && right){
				autoFunctions = State.AUTO_STAND_BY;
			}
			break;
		default: System.out.println("Error autoFunctions = " + autoFunctions);
		}

//		rightPower = rightPID.update(encoderDataRight.getSpeed());
//		leftPower = leftPID.update(encoderDataLeft.getSpeed());
		
		leftFront.set(leftPower);
		leftBack.set(-leftPower);
		rightFront.set(rightPower);
		rightBack.set(-rightPower);
		return false;
	}

	/**
	 * The amount of time you want to sleep for after a cycle.
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
		System.out.println("Current speed: " + currentSpeed);
		System.out.println("Current drive state: " + currentDriveState);
		System.out.println("Auto State: " + autoFunctions);
//				System.out.println("Left: " + colorSensorLeft.colorToString(colorSensorLeft.getColor()) +
//									"  Right: " + colorSensorRight.colorToString(colorSensorRight.getColor()));
//				System.out.println("Left Red: " + colorSensorLeft.getRed() + " Left Blue:" + colorSensorLeft.getBlue());
//				System.out.println("Right Red: " + colorSensorRight.getRed() + " Right Blue:" + colorSensorRight.getBlue());
//		System.out.println("Left Encoder: " + encoderDataLeft.getSpeed() +
//				" Right Encoder: " +encoderDataRight.getSpeed());
		System.out.println("Left Touch: " + leftTouch.get() + " Right: " + rightTouch.get());
	}

	/**
	 * sets the wanted left and right speed to the speed sent in inches
	 * @param left left motor speed
	 * @param right right motor speed
	 */
	public void setPower(double left, double right) {
		if(Math.abs(left) < DEAD_ZONE){
			left = 0;
		}
		if(Math.abs(right) < DEAD_ZONE){
			right = 0;
		}
		if(left > 0){
			wantedLeftPower = (5/4)*Math.sqrt(left);
		}else{
			wantedLeftPower = -(5/4)*Math.sqrt(-left);
		}
		if(right > 0){
			wantedRightPower = (5/4)*Math.sqrt(right);
		}else{
			wantedRightPower = -(5/4)*Math.sqrt(-right);
		}
//		rightPID.setGoal(right*100);
//		leftPID.setGoal(left*100);
	}

	public void setAutoFunction(State wantedAutoState){
		autoFunctions = wantedAutoState;
	}
	/**
	 *Makes the states for drives
	 */
	public enum State{
		IN_LOW_GEAR,
		SHIFTING_LOW,
		IN_HIGH_GEAR,
		SHIFTING_HIGH,
		AUTO_STAND_BY,
		AUTO_LIGHT_LINE_UP,
		AUTO_STEP_LINEUP;


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
			case AUTO_STAND_BY:
				return "In auto stand by";
			case AUTO_LIGHT_LINE_UP:
				return "In auto light line up";
			default:
				return "Error";
			}
		}
	}

	@Override
	protected void liveWindow() {
		String subsytemName = "Drives";
		LiveWindow.addActuator(subsytemName, "Shifting", shiftingSol);
		LiveWindow.addActuator(subsytemName, "Right Encoder", encoderRight);
		LiveWindow.addActuator(subsytemName, "Right Front Motor", rightFront);
		LiveWindow.addActuator(subsytemName, "Right Rear Motor", rightBack);
		LiveWindow.addActuator(subsytemName, "Left Front Motor", leftFront);
		LiveWindow.addActuator(subsytemName, "Left Front Motor", leftBack);
		LiveWindow.addActuator(subsytemName, "Left Encoder", encoderLeft);	
	}
}

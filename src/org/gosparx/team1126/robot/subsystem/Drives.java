package org.gosparx.team1126.robot.subsystem;

import org.gosparx.team1126.robot.IO;
import org.gosparx.team1126.robot.sensors.ColorSensor;
import org.gosparx.team1126.robot.sensors.EncoderData;
import org.gosparx.team1126.robot.sensors.ColorSensor.Color;
import org.gosparx.team1126.robot.sensors.PID;
import org.gosparx.team1126.robot.util.Logger;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
/**
 * Makes the drives system to have the robot move.
 *@author Mike the camel
 */
public class Drives extends GenericSubsystem{

	//***************************INSTANCES****************************
	/**
	 * Makes a drives object that will be called to use the drives class
	 */
	private static Drives drives;

	/**
	 * Access to the File logger
	 */
	private Logger log;

	/**
	 * Right drive PID loop
	 */
	private PID rightPID;

	/**
	 * Left drive PID loop
	 */
	private PID leftPID;

	/**
	 * the current state the drives is in
	 */
	private State currentDriveState;

	/**
	 * Variable for determining which state the color sensor
	 */
	private State autoFunctions;

	//*********************************MOTOR VICTORS*************************
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

	//****************************PNU**************************
	/**
	 * the solenoid used for shifting
	 */
	private Solenoid shiftingSol;

	//****************************SENSORS**********************
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
	 * the left color sensor for detecting the colors in front of the robot
	 */
	private ColorSensor colorSensorLeft;

	/**
	 * the right color sensor for detecting the colors in front of the robot
	 */
	private ColorSensor colorSensorRight;

	/**
	 * Right touch sensor
	 */
	private DigitalInput rightTouch;

	/**
	 * Left touch sensor
	 */
	private DigitalInput leftTouch;


	private Gyro gyro;

	//********************************CONSTANTS****************************
	/**
	 * The P value in the drives left/right PID loop
	 */
	private static final double P_LEFT = 0.01;

	/**
	 * The I value in the drives left/right PID loop
	 */
	private static final double I_LEFT = 0.04;

	/**
	 * The D
	 */
	private static final double D_LEFT = 0;

	/**
	 * The P value in the drives left/right PID loop
	 */
	private static final double P_RIGHT = 0.021;

	/**
	 * The I value in the drives left/right PID loop
	 */
	private static final double I_RIGHT = 0.055;

	/**
	 * The D
	 */
	private static final double D_RIGHT = 0;

	/**
	 * the amount of distance the shortbot will make per tick
	 */
	private final double DISTANCE_PER_TICK = 0.04908738;

	/**
	 * the speed required to shift down, not accurate yet
	 */
	private static final double LOWERSHIFTSPEED = 60;

	/**
	 * the speed required to shift up, not accurate yet
	 */
	private static final double UPPERSHIFTSPEED = 80;

	/**
	 * the time required to shift, not accurate yet, in seconds
	 */
	private static final double SHIFTING_TIME = 0.15;

	/**
	 * Joystick deadzone, +/-
	 */
	public static final double DEAD_ZONE = 0.05;

	/**
	 * the speed required to shift
	 */
	private static final double SHIFTINGSPEED = 0.35;

	/**
	 * Alex said I didn't need comments (=
	 */
	private static final double LINEUP_SPEED = 0.45;

	/**
	 * determines if it's in high or low gear
	 */
	private static final boolean LOW_GEAR = false;

	/**
	 * stops the motors for auto
	 */
	private static final int STOP_MOTOR = 0;

	/**
	 * Use PID Debugger (DRIVES WILL NOT WORK WITH JOYSTICK INPUT)
	 */
	private static final boolean USE_PID_DEBUG = false;

	//*********************************VARIBLES****************************
	/**
	 * the wanted speed for the left motors
	 */
	private double wantedLeftPower;

	/**
	 * the wanted speed for the right motors
	 */
	private double wantedRightPower;

	/**
	 * the current average speed between the left and right motors
	 */
	private double currentSpeed;

	/**
	 * actual time it took to shift
	 */
	private double shiftTime;

	/**
	 * The wanted power of the right drives (-1 - 1)
	 */
	private double rightPower;

	/**
	 * The wanted power of the left drives (-1 - 1)
	 */
	private double leftPower;

	/**
	 * The wanted auto turn angle in degrees, (- left) (+ right)
	 */
	private double autoWantedTurn = 0;

	/**
	 * The wanted distance to travel in inches
	 */
	private double autoDistance = 0;

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
		//LEFT
		leftFront = new Victor(IO.PWM_LEFT_FRONT_DRIVES);
		leftBack = new Victor(IO.PWM_LEFT_BACK_DRIVES);
		encoderLeft = new Encoder(IO.ENCODER_LEFT_DRIVES_A, IO.ENCODER_LEFT_DRIVES_B);
		encoderDataLeft = new EncoderData(encoderLeft, DISTANCE_PER_TICK);
		leftPID = new PID(P_LEFT, I_LEFT, 1, D_LEFT, true, false);
		//		colorSensorLeft = new ColorSensor(IO.COLOR_LEFT_RED, IO.COLOR_LEFT_BLUE, IO.COLOR_LEFT_LED);
		leftTouch = new DigitalInput(IO.SWITCH_LEFT_DRIVES);
		leftPower = 0;

		//RIGHT
		rightFront = new Victor(IO.PWM_RIGHT_FRONT_DRIVES);
		rightBack = new Victor(IO.PWM_RIGHT_BACK_DRIVES);
		encoderRight = new Encoder(IO.ENCODER_RIGHT_DRIVES_A, IO.ENCODER_RIGHT_DRIVES_B);
		encoderDataRight = new EncoderData(encoderRight, DISTANCE_PER_TICK);
		rightPID = new PID(P_RIGHT, I_RIGHT, 1, D_RIGHT, true, false);
		//		colorSensorRight = new ColorSensor(IO.COLOR_RIGHT_RED, IO.COLOR_RIGHT_BLUE, IO.COLOR_RIGHT_LED);
		rightTouch = new DigitalInput(IO.SWTICH_RIGHT_DRIVES);
		rightPower = 0;

		//OTHER
		gyro = new Gyro(0);
		log = new Logger(getName());
		shiftingSol = new Solenoid(IO.PNU_SHIFTING);	
		currentDriveState = State.IN_LOW_GEAR;
		currentSpeed = 0;
		shiftTime = 0;
		autoFunctions = State.AUTO_STAND_BY;

		if(USE_PID_DEBUG){
			debugPID();
		}
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
				rightPower = -LINEUP_SPEED;
			}else{
				rightPower = LINEUP_SPEED;
			}
			if(left){
				leftPower = -LINEUP_SPEED;
			}else{
				leftPower = LINEUP_SPEED;
			}
			if(left && right){
				autoFunctions = State.AUTO_STAND_BY;
				rightPower = 0;
				leftPower = 0;
			}
			break;
		case AUTO_TURN:
			double currentAngle = gyro.getAngle();
			double angleDiff = autoWantedTurn - currentAngle;
			double speed = (1.0/16) * (angleDiff > 0 ? Math.sqrt(angleDiff) : -Math.sqrt(-angleDiff));
			speed = speed < Math.PI/8 ? Math.PI/8 : speed;
			log.logMessage(speed + "");
			if(currentAngle < autoWantedTurn){
				rightPower = -speed;
				leftPower = speed;
			}else{
				rightPower = speed;
				leftPower = -speed;
			}
			if(currentAngle > (autoWantedTurn - 0.5) && currentAngle < (autoWantedTurn + 0.5)){
				rightPower = 0;
				leftPower = 0;
				autoFunctions = State.AUTO_STAND_BY;
			}
			break;
		case AUTO_DRIVE:
			double currentDistance = (encoderDataRight.getDistance() + encoderDataLeft.getDistance())/2;
			double driveSpeed = (1.0/10)*(Math.sqrt(autoDistance - currentDistance));
			driveSpeed = driveSpeed < Math.PI/16 ? Math.PI/16: driveSpeed;
			driveSpeed = 0.4;
			
			if(currentDistance < autoDistance){
				rightPower = driveSpeed + gyroOffset();
				leftPower = driveSpeed - gyroOffset();
			}else{
				rightPower = -driveSpeed + gyroOffset();
				leftPower = -driveSpeed - gyroOffset();
			}
			if(autoDistance - currentDistance < 0.5 && autoDistance - currentDistance > -0.5){
				rightPower = 0;
				leftPower = 0;
				autoFunctions = State.AUTO_STAND_BY;
			}
		break;
	default: System.out.println("Error autoFunctions = " + autoFunctions);
	}

	//PID DEBUG
	if(USE_PID_DEBUG){
		updatePIDEncoder();
		rightPID.setGains(getP(true), getI(true), getD(true));
		leftPID.setGains(getP(false), getI(false), getD(false));
		rightPID.setGoal(getGoal(true));
		leftPID.setGoal(getGoal(false));
		rightPower = rightPID.update(encoderDataRight.getSpeed());
		leftPower = leftPID.update(encoderDataLeft.getSpeed());
	}

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
	//		log.logMessage("Current speed: " + currentSpeed);
	//		log.logMessage("Current drive state: " + currentDriveState);
	//		log.logMessage("Auto State: " + autoFunctions);
	//				log.logMessage("Left: " + colorSensorLeft.colorToString(colorSensorLeft.getColor()) +
	//									"  Right: " + colorSensorRight.colorToString(colorSensorRight.getColor()));
	//				log.logMessage("Left Red: " + colorSensorLeft.getRed() + " Left Blue:" + colorSensorLeft.getBlue());
	//				log.logMessage("Right Red: " + colorSensorRight.getRed() + " Right Blue:" + colorSensorRight.getBlue());
	//				log.logMessage("Left Encoder: " + encoderDataLeft.getSpeed() +
	//				" Right Encoder: " +encoderDataRight.getSpeed());
	//		log.logMessage("Left Touch: " + leftTouch.get() + " Right: " + rightTouch.get());
	log.logMessage("Gyro: " + gyro.getAngle());
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

/**
 * Set auto function
 * @param wantedAutoState - State from drives
 */
public void setAutoFunction(State wantedAutoState){
	autoFunctions = wantedAutoState;
}

/**
 * Force drive to stop moving
 */
public void autoForceStop(){
	setAutoFunction(State.AUTO_STAND_BY);
}

/**
 * Turns to robot
 * @param degrees - positive(right) || negative(left)
 */
public void autoTurn(int degrees){
	setAutoFunction(State.AUTO_TURN);
	autoWantedTurn = degrees;
	gyro.reset();
}

/**
 * Drives robot forward set distance
 * @param inchDistance - distance to travel in inches
 * @param speed - desired speed(0 - 1)
 */
public void driveStraight(int inchDistance, int speed){
	setAutoFunction(State.AUTO_DRIVE);
	autoDistance = inchDistance;
	gyro.reset();
	encoderDataRight.reset();
	encoderDataLeft.reset();
	autoWantedTurn = 0;
}

/**
 * @return is drives is done with last auto command
 */
public boolean isDone(){
	return (autoFunctions == State.AUTO_STAND_BY);
}

/**
 * 
 * @return (-1 - 1)
 */
private double gyroOffset(){
	double currentAngle = gyro.getAngle();
	double position = autoWantedTurn - currentAngle;
	return position > 0 ? -(1.0/16)*(Math.sqrt(position)) : (1.0/16)*(Math.sqrt(-position));
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
	AUTO_TURN,
	AUTO_DRIVE,
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

//PID
private void debugPID(){
	SmartDashboard.putNumber("Left Goal", 0);
	SmartDashboard.putNumber("Left P", 0);
	SmartDashboard.putNumber("Left I", 0);
	SmartDashboard.putNumber("Left D", 0);

	SmartDashboard.putNumber("Right Goal", 0);
	SmartDashboard.putNumber("Right P", 0);
	SmartDashboard.putNumber("Right I", 0);
	SmartDashboard.putNumber("Right D", 0);
}

private void updatePIDEncoder(){
	SmartDashboard.putNumber("Right Encoder", encoderDataRight.getSpeed());
	SmartDashboard.putNumber("Left Encoder", encoderDataLeft.getSpeed());
}

private double getGoal(boolean right){
	return right ? SmartDashboard.getNumber("Right Goal") : SmartDashboard.getNumber("Left Goal");
}

private double getP(boolean right){
	return right ? SmartDashboard.getNumber("Right P") : SmartDashboard.getNumber("Left P");
}

private double getI(boolean right){
	return right ? SmartDashboard.getNumber("Right I") : SmartDashboard.getNumber("Left I");
}

private double getD(boolean right){
	return right ? SmartDashboard.getNumber("Right D") : SmartDashboard.getNumber("Left D");
}

}

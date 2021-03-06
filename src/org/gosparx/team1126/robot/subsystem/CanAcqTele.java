package org.gosparx.team1126.robot.subsystem;

import org.gosparx.team1126.robot.IO;
import org.gosparx.team1126.robot.sensors.EncoderData;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * Controls the can tele acq
 * @author Alex Mechler {amechler1998@gmail.com}
 * @author Mike the Camel {Michaele789@gmail.com}
 */
public class CanAcqTele extends GenericSubsystem{

	/************************Objects**********************/

	/**
	 * Supports singleton
	 */
	private static CanAcqTele canAcq;

	/**
	 * Instance of elevations
	 */
	private Elevations elevations;

	/**
	 * The motor that rotates the arms
	 */
	private Talon rotateMotor;

	/**
	 * The motor that controls the hook
	 */
	//private Talon hookMotor;

	/**
	 * The encoder that tracks the rotation
	 */
	private Encoder rotateEnc;

	/**	
	 * The encoder data for the rotation
	 */
	private EncoderData rotateEncData;

	/**
	 * The encoder for the hook
	 */
	private Encoder hookEnc;

	/**
	 * The encoder data for the hook
	 */
	private EncoderData hookEncData;

	/**
	 * Rotate home sensor
	 */
	private DigitalInput rotateHome;

	/**
	 * Hook home sensor
	 */
	private DigitalInput hookHome;

	/**
	 * The power distribution board instance
	 */
	private PowerDistributionPanel pdp;
	
	/**
	 * The floor grabber thingy
	 */
	private Solenoid floorGrabbers;

	/***********************Constants*********************/

	/**
	 * The distance the hook will travel per tick 
	 */
	private static final double DISTANCE_PER_TICK_HOOK = .00812/2;

	/**
	 * The distance the arms will rotate per tick 
	 */
	private static final double DISTANCE_PER_TICK_ROTATE = 75/256.0;

	/**
	 * The minimum power for the motors to get when we are rotating up
	 */
	private static final double MIN_ROTATE_UP_SPEED = 0.4;

	/**
	 * the minimum power for the motors when we are rotating down
	 */
	private static final double MIN_ROTATE_DOWN_SPEED = 0.05;

	/**
	 * The minimum hook speed
	 */
	private static final double MIN_HOOK_SPEED = 1.2;

	/**
	 * The max position for the rotation
	 */
	private static final double ACQ_ROTATION = 77;//86.5;
	
	private static final double MAX_ROTATION = 86.5;

	/**
	 * The position for the hook to acq
	 */
	private static final double ACQ_CAN_DIST = 27.5;

	/**
	 * The max pos for the hook
	 */
	private static final double MAX_HOOK_POS = 43;
	
	/**
	 * Acquire speed for grabbing a RC
	 */
	private static final double ACQUIRE_SPEED = 30;//490;
	
	/**
	 * Rotation speed for rotating down
	 */
	private static final double ROTATION_SPEED = 20;//200;

	/***********************Variables*********************/

	/**
	 * the wanted angle of the arms
	 */
	private double wantedAngle;

	/**
	 * the wanted position of the hook
	 */
	private double wantedHookPos;

	/**
	 * the current state hook is in
	 */
	private HookState currentHookState;

	/**
	 * The current state rotate is in
	 */
	private RotateState currentRotateState;

	/**
	 * The wanted speed of the hooks
	 */
	private double wantedHookSpeed = 0;

	/**
	 * The wanted speed of rotation
	 */
	private double wantedRotateSpeed = 0;

	/**
	 * True if auto controls are used
	 */
	private boolean useAutoFunctions = true;
	
	/**
	 * The divider for the rotation speed
	 */
	private double rotationDivider;
	
	private boolean isAcquiring = false;
	
	private double maxRotateUp = 1;
	
	private boolean hookInitialized = true;//false;
	private boolean rotateInitialized = false;
	
	private boolean currentFloorGrabbers = false;

	/**
	 * @return a CanAcqTele
	 */
	public static synchronized CanAcqTele getInstance(){
		if(canAcq == null){
			canAcq = new CanAcqTele();
		}
		return canAcq;
	}

	/**
	 * creates a new CanAcqTele
	 */
	private CanAcqTele() {
		super("CanAcqTele", Thread.NORM_PRIORITY);
	}

	/**
	 * Initializes things
	 */
	@Override
	protected boolean init() {
		rotateMotor = new Talon(IO.PWM_CAN_ROTATE);
		rotateEnc = new Encoder(IO.DIO_CAN_ROTATE_A, IO.DIO_CAN_ROTATE_B);
		rotateEnc.setDistancePerPulse(DISTANCE_PER_TICK_ROTATE);
		rotateEncData = new EncoderData(rotateEnc, DISTANCE_PER_TICK_ROTATE);
		//hookMotor = new Talon(IO.PWM_CAN_HOOK);
		hookEnc = new Encoder(IO.DIO_CAN_HOOK_A, IO.DIO_CAN_HOOK_B);
		hookEncData = new EncoderData(hookEnc, DISTANCE_PER_TICK_HOOK);
		hookHome = new DigitalInput(18);
		rotateHome = new DigitalInput(19);
		currentHookState = HookState.STANDBY;
		currentRotateState = RotateState.STANDBY;
		pdp = new PowerDistributionPanel();
		elevations = Elevations.getInstance();
		currentHookState = HookState.STANDBY;//HOOK_FINDING_HOME;
		currentRotateState = RotateState.ROTATE_FINDING_HOME;
		floorGrabbers = new Solenoid(IO.PNU_FLOOR_GRABBER);
		return true;
	}

	/**
	 *Overrides in genericSubsytems, does things
	 */
	@Override
	protected void liveWindow() {
		LiveWindow.addActuator(getName(), "Rotating Victor", rotateMotor);
		//LiveWindow.addActuator(getName(), "Hooking Victor", hookMotor);
		LiveWindow.addSensor(getName(), "Hook Home", hookHome);
		LiveWindow.addSensor(getName(), "Rotate Home", rotateHome);
		LiveWindow.addActuator(getName(), "Rotate Encoder", rotateEnc);
		LiveWindow.addActuator(getName(), "Hook Encoder", hookEnc);
	}

	/**
	 * Loops
	 */
	@Override
	protected boolean execute() {
		isWorking = true;//USE TO DEBUG
		rotateEncData.calculateSpeed();
		hookEncData.calculateSpeed();
		
		if(!rotateInitialized){
			currentRotateState = RotateState.ROTATE_FINDING_HOME;
			isWorking = false;
		}//else if(!hookInitialized){
		//	currentHookState = HookState.HOOK_FINDING_HOME;
		//}
		
		if(useAutoFunctions || !rotateInitialized || !hookInitialized){
			switch(currentRotateState){
			case STANDBY:
				if(rotateEncData.getDistance() < 10){
					wantedRotateSpeed = 0.06;
					if(rotateEncData.getDistance() < -0.01){
						rotateEncData.reset();
						LOG.logMessage("Encoder Home has been reset");
					}
				}else{
					wantedRotateSpeed = 0;
				}
				break;
			case ROTATING:
				double calculatedRotateSpeed = -(wantedAngle - rotateEncData.getDistance()) / rotationDivider;
				if(calculatedRotateSpeed > 0){
					wantedRotateSpeed = ((Math.abs(calculatedRotateSpeed) > MIN_ROTATE_UP_SPEED) ? calculatedRotateSpeed : MIN_ROTATE_UP_SPEED);
					wantedRotateSpeed = ((Math.abs(calculatedRotateSpeed) > maxRotateUp) ? maxRotateUp : calculatedRotateSpeed);
					
				}else{
					wantedRotateSpeed = ((Math.abs(calculatedRotateSpeed) > MIN_ROTATE_DOWN_SPEED) ? calculatedRotateSpeed : -MIN_ROTATE_DOWN_SPEED);
					wantedRotateSpeed = ((Math.abs(calculatedRotateSpeed) > 0.25) ? -0.15 : calculatedRotateSpeed);
				}

				if(calculatedRotateSpeed > 0 && rotateEncData.getDistance() <=  50  && rotateEncData.getDistance() >= 40 && isAcquiring){
					elevations.moveElevator(15, 1, true);
				}
				
//				if(hookEncData.getDistance() > 24 && wantedRotateSpeed > 0 && isAcquiring){
//					wantedRotateSpeed = 0;
//				}

				if((rotateEncData.getDistance() >= wantedAngle - 2 && calculatedRotateSpeed < 0) || (rotateEncData.getDistance() <= wantedAngle + 1 && calculatedRotateSpeed > 0)){
					currentRotateState = RotateState.STANDBY;
					wantedRotateSpeed = 0;
					LOG.logMessage("Done rotating");
				}
				break;
			case ROTATE_FINDING_HOME:
				wantedRotateSpeed = 0.2;
				if(!rotateHome.get()){
					wantedRotateSpeed = 0;
					currentRotateState = RotateState.STANDBY;
					currentHookState = HookState.HOOK_FINDING_HOME;//Finding Home
					LOG.logMessage("Rotate has found home*********");
					rotateEncData.reset();
					rotateInitialized = true;
				}
				break;
			}

			switch(currentHookState){
			case STANDBY:
				wantedHookSpeed= 0;
				break;
			case MOVING:
				double calculatedMovingSpeed = -((wantedHookPos - hookEncData.getDistance()) / 0.25);
				if(calculatedMovingSpeed > 0){
					wantedHookSpeed = (Math.abs(calculatedMovingSpeed) > 1) ? calculatedMovingSpeed : 1;
					wantedHookSpeed = (Math.abs(calculatedMovingSpeed) > 2) ? 2 : calculatedMovingSpeed;
					if(hookEncData.getDistance() > 27 && isAcquiring){
						wantedHookSpeed = (Math.abs(calculatedMovingSpeed) > 1.0) ? 1.0 : calculatedMovingSpeed;
					}else if(hookEncData.getDistance() < 9 && isAcquiring){
						wantedHookSpeed = (Math.abs(calculatedMovingSpeed) > 1.4) ? 1.4 : calculatedMovingSpeed;
					}
				}else{
					wantedHookSpeed = (Math.abs(calculatedMovingSpeed) > MIN_HOOK_SPEED) ? calculatedMovingSpeed : -MIN_HOOK_SPEED;
				}
				if((hookEncData.getDistance() >= wantedHookPos - 0.5 && calculatedMovingSpeed < 0) || (hookEncData.getDistance() <= wantedHookPos + 0.5 && calculatedMovingSpeed > 0)){
					wantedHookSpeed = 0;
					currentHookState = HookState.STANDBY;
					LOG.logMessage("Done moving hook");
				}
				break;
			case HOOK_FINDING_HOME:
				wantedHookSpeed = 1.2;
				if(!hookHome.get()){
					hookEncData.reset();
					hookEnc.reset();
					currentHookState = HookState.STANDBY;
					wantedHookSpeed = 0;
					LOG.logMessage("Hook has found home *********************");
					hookInitialized = true;
				}
				break;
			}
		}

		floorGrabbers.set(currentFloorGrabbers);
		rotateMotor.set(wantedRotateSpeed*3);
		//hookMotor.set(-wantedHookSpeed/2.0); //Needs to be flipped if we return to AM motor
		return false;
	}

	/**
	 * Set the rotate manual speed directly
	 * @param speed - (-1 - 1)
	 */
	public void manualRotateOverride(double speed){
		if((rotateEncData.getDistance() > 0 && speed > 0) || (speed < 0 && rotateEncData.getDistance() < MAX_ROTATION)){
			wantedRotateSpeed = speed/4;
		}else{
			wantedRotateSpeed = 0;
		}
	}

	/**
	 * Set the hook manual speed directly
	 * @param speed - (-1 - 1)
	 */
	public void manualHookOverride(double speed){
		if((hookEncData.getDistance() > 0 && speed > 0) || (speed < 0 && hookEncData.getDistance() < MAX_HOOK_POS)){
			wantedHookSpeed = speed*1.25;
		}else{
			wantedHookSpeed = 0;
		}
	}
	
	public void setAutoPosition(double angle){
		wantedAngle = angle;
		currentRotateState = RotateState.ROTATING;
		rotationDivider = ROTATION_SPEED;
		isAcquiring = false;
	}

	/**
	 * Used to set weather to use auto controls or manual
	 * @param override - true if using manual controls
	 */
	public void overriding(boolean override){
		if(!override != useAutoFunctions){
			LOG.logMessage("Auto controls are being overrode");
		}
		useAutoFunctions = !override;
	}

	/**
	 * Goes to acquiring mode
	 */
	public void goToAcquire(){
		wantedHookPos = ACQ_CAN_DIST;
		wantedAngle = ACQ_ROTATION;
		rotationDivider = ROTATION_SPEED;
		currentHookState = HookState.MOVING;
		currentRotateState = RotateState.ROTATING;
		isAcquiring = false;
	}

	/**
	 * Initial Setup
	 */
	public void initalizedPositions(){
		currentHookState = HookState.HOOK_FINDING_HOME;
		currentRotateState = RotateState.ROTATE_FINDING_HOME;
	}

	/**
	 * Brings the can in
	 */
	public void acquireCan(boolean isSlow){
		rotationDivider = ACQUIRE_SPEED;
		wantedHookPos = 0;
		wantedAngle = 0;
		currentHookState = HookState.MOVING;
		currentRotateState = RotateState.ROTATING;
		isAcquiring = true;
		if(isSlow){
			maxRotateUp = 0.35;
		}else{
			maxRotateUp = 0.6;
		}
	}

	/**
	 * Moves the can up DISTANCE_PER_TOTE
	 */
	public void acquiredTote(double distance){
		currentHookState = HookState.MOVING;
		wantedHookPos = Math.min(hookEncData.getDistance() + distance, MAX_HOOK_POS);	
	}
	
	public boolean isDone(){
		return currentRotateState == RotateState.STANDBY;//(currentHookState == HookState.STANDBY && currentRotateState == RotateState.STANDBY);
	}

	/**
	 * The desired new state of Rotate
	 * @param state
	 */
	public void setState(RotateState state){
		currentRotateState = state;
	}

	/**
	 * The desired new state of Hook
	 * @param state
	 */
	public void setState(HookState state){
		currentHookState = state;
	}
	
	public void toggleFloorGrabbers(){
		currentFloorGrabbers = !currentFloorGrabbers;
	}
	
	/**
	 * How long to sleep between loops
	 */
	@Override
	protected long sleepTime() {
		return 25;
	}

	/**
	 * Writes the info about the subsystem.
	 */
	@Override
	protected void writeLog() {
		LOG.logMessage("Current State Hook: " + currentHookState.toString());
		LOG.logMessage("Current State Rotate: " + currentRotateState.toString());
		LOG.logMessage("Current Rotate Location:" + rotateEncData.getDistance());
		LOG.logMessage("Wanted Angle: " + wantedAngle + " Current Angle: " + rotateEncData.getDistance());
		LOG.logMessage("Wanted Hook Pos: " + wantedHookPos + " Current Hook Pos: " + hookEncData.getDistance());
		LOG.logMessage("Hook Home: " + !hookHome.get() + " Rotate Home: " + !rotateHome.get());
		LOG.logMessage("Hook Current: " + pdp.getCurrent(IO.PWM_CAN_HOOK));
	}

	/**
	 * The states for the hook
	 * @author Mike
	 */
	public enum HookState{
		STANDBY,
		MOVING,
		HOOK_FINDING_HOME;

		public String toString(){
			switch(this){
			case STANDBY:
				return "In standby";
			case MOVING:
				return "Moving";
			case HOOK_FINDING_HOME:
				return "Hook Finding Home";
			default:
				return "Error";
			}
		}
	}

	/**
	 * The states for rotating
	 * @author Mike
	 */
	public enum RotateState{
		STANDBY,
		ROTATING,
		ROTATE_FINDING_HOME;

		public String toString(){
			switch(this){
			case STANDBY:
				return "In standby";
			case ROTATING:
				return "Rotating";
			case ROTATE_FINDING_HOME:
				return "Rotate Finding Home";
			default:
				return "Error";
			}
		}
	}
}

package org.gosparx.team1126.robot;

import org.gosparx.team1126.robot.subsystem.CanAcquisition;
import org.gosparx.team1126.robot.subsystem.GenericSubsystem;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

/**
 * A class to control Autonomous
 * @author Alex y Andrew
 */
public class Autonomous extends GenericSubsystem{
	
	/**
	 * True if we are running auto
	 */
	private boolean runAuto;

	/**
	 * Supports singleton
	 */
	private static Autonomous auto;
	
	/**
	 * Instance of the can acquisition 
	 */
	private CanAcquisition canAcq;
	
	/**
	 * The Physical selector switch for auto modes
	 */
	private AnalogInput selectorSwitch;
	
	/**
	 * The channel that the selector switch is in 
	 */
	private final int SELECTOR_SWITCH_CHANNEL = 0;
	
	/**
	 * An instance of a driverstation
	 */
	private DriverStation ds;
	
	/**
	 * The current autonomous 
	 */
	private int[][] currentAuto;
	
	/**
	 * The current step of the autonomous we are on
	 */
	private int currentStep;
	
	/**
	 * Should we go to the next step?
	 */
	private boolean increaseStep;
	
	/**
	 * Should we check the if the time is critical
	 */
	private boolean checkTime;
	
	/**
	 * What action to do when time is critical
	 */
	private int criticalStep;
	
	/**
	 * What time left is considered critical
	 */
	private double criticalTime;
	
	/**
	 * Time auto starts
	 */
	private double autoStart;
	
	/**
	 * The voltages of the different choices on the selection switch
	 */
	private final double SELECTION_0 = 4.80;
	private final double SELECTION_1 = 4.27;
	private final double SELECTION_2 = 3.74;
	private final double SELECTION_3 = 3.20;
	private final double SELECTION_4 = 2.67;
	private final double SELECTION_5 = 2.14;
	private final double SELECTION_6 = 1.60;
	private final double SELECTION_7 = 1.07;
	private final double SELECTION_8 = 0.54;
	private final double SELECTION_9 = 0.00;
	/*********************************************************************************************************************************
	 **********************************************AUTO COMMANDS**********************************************************************
	 *********************************************************************************************************************************/
	
	private static final int DRIVES_GO_FORWARD = 1;
	private static final int DRIVES_GO_REVERSE = 2;
	private static final int DRIVES_TURN_RIGHT = 3;
	private static final int DRIVES_TURN_LEFT = 4;
	private static final int DRIVES_STOP = 5;
	private static final int DRIVE_TILL_STEP = 6;
	private static final int DRIVES_DANCE = 7;
	private static final int DRIVES_DONE = 9;
	
	private static final int ARMS_DROP = 10;
	private static final int ARMS_RAISE = 11;
	private static final int ARMS_EXPAND = 12;
	private static final int ARMS_CONTRACT = 13;
	private static final int ARMS_STOP = 14;
	private static final int ARMS_IN_CAN = 15;
	private static final int ARMS_DONE = 19;
	
	
	private static final int ACQ_LOWER = 20;
	private static final int ACQ_RAISE = 21;
	private static final int ACQ_ROLLERS_ON = 22;
	private static final int ACQ_ROLLERS_OFF = 23;
	private static final int ACQ_STOP = 24;
	private static final int ACQ_DONE = 29;
	
	private static final int TOTES_RAISE = 30;
	private static final int TOTES_LOWER = 31;
	private static final int TOTES_EJECT = 32;
	private static final int TOTES_STOP = 33;
	private static final int TOTES_DONE = 39;
	
	private static final int CHECK_TIME = 97; //{CHECK_TIME, criticalStep, criticalTime}
	private static final int WAIT = 98;
	private static final int END = 99; 
	
	/**
	 * Move 2 containers from step into Auto zone
	 */
	private static final int [][] twoCanStep = {
		{DRIVE_TILL_STEP},
		{DRIVES_DONE},
		{ARMS_DROP},
		{WAIT, 500}, // TODO find the actual time it takes the arms to drop and touch the can.
		{DRIVES_DANCE},
		{CHECK_TIME, 7, 5},
		{ARMS_IN_CAN},
		{DRIVES_STOP},
	};
	
	/**
	 * Singleton
	 * @return the only instance of Autonomous ever
	 */
	public static synchronized Autonomous getInstance(){
		if(auto == null){
			auto = new Autonomous();
		}
		return auto;
	}
	
	/**
	 * Creates a new Autonomous
	 */
	private Autonomous() {
		super("Autonomous", Thread.NORM_PRIORITY);
	}

	/**
	 * runs one time
	 */
	@Override
	protected boolean init() {
		selectorSwitch = new AnalogInput(SELECTOR_SWITCH_CHANNEL);
		ds = DriverStation.getInstance();
		return false;
	}

	/**
	 * Loops
	 */
	@Override
	protected boolean execute() {
		if(runAuto){
			runAuto();
		}else{
			getAutoMode();
			currentStep = 0;
			autoStart = Timer.getFPGATimestamp();
		}
		return false;
	}

	/**
	 * How long do we sleep for in ms.
	 */
	@Override
	protected long sleepTime() {
		return 20;
	}

	/**
	 * Writes info about what its currently doing
	 */
	@Override
	protected void writeLog() {
		System.out.println("Current Step:" + currentStep);
	}
	
	/**
	 * Gets the current automode based on the voltages
	 */
	private void getAutoMode(){
		double voltage = selectorSwitch.getVoltage();
		if (voltage >= SELECTION_0){
		
		}else if(voltage >= SELECTION_1){
			
		}else if(voltage >= SELECTION_2){
			
		}else if(voltage >= SELECTION_3){
			
		}else if(voltage >= SELECTION_4){
		
		}else if(voltage >= SELECTION_5){
			
		}else if(voltage >= SELECTION_6){
		
		}else if(voltage >= SELECTION_7){
			
		}else if(voltage >= SELECTION_8){
			
		}else if(voltage >= SELECTION_9){ 
		
			
		}else{
			
		}
	}
	
	/**
	 * Executes commands
	 */
	private void runAuto(){
		increaseStep = true;
		if(ds.isAutonomous() && ds.isEnabled()){
			switch(currentAuto[currentStep][0]){  //add automodes later when we have them
			case DRIVES_GO_FORWARD:
				break;
			case DRIVES_GO_REVERSE:
				break;
			case DRIVES_TURN_RIGHT:
				break;
			case DRIVES_TURN_LEFT:
				break;
			case DRIVES_STOP:
				break;
			case DRIVE_TILL_STEP:
				// TODO implement drive till step in drives
				// drives.driveTillStep();
				break;
			case DRIVES_DANCE:
				// TODO Implement drives dance
				// drives.dance();
				break;
			case DRIVES_DONE:
				// TODO Implement drive last command Done
				// TODO Implement run next statement
				// runNextStatement(drives.isLastCommandDone());
				break;
			case ARMS_DROP:
				canAcq.armsDrop();
				break;
			case ARMS_RAISE:
				break;
			case ARMS_EXPAND:
				break;
			case ARMS_CONTRACT:
				break;
			case ARMS_STOP:
				break;
			case ARMS_IN_CAN:
				// Keep looping into this case until both hands are in can
				if (!canAcq.rightHandInCan() || !canAcq.leftHandInCan())
				{
					increaseStep = false;
				}
				break;
			case ARMS_DONE:
				break;
			case ACQ_LOWER:
				break;
			case ACQ_RAISE:
				break;
			case ACQ_ROLLERS_ON:
				break;
			case ACQ_ROLLERS_OFF:
				break;
			case ACQ_STOP:
				break;
			case ACQ_DONE:
				break;
			case TOTES_RAISE:
				break;
			case TOTES_LOWER:
				break;
			case TOTES_EJECT:
				break;
			case TOTES_STOP:
				break;
			case TOTES_DONE:
				break;
			case CHECK_TIME:
				checkTime = true;
				criticalStep = currentAuto[currentStep][1];
				criticalTime = currentAuto[currentStep][2];
				break;
			case WAIT:
				break;
			case END:
				break;
			}
			if(increaseStep)
				currentStep++;
			if(checkTime && Timer.getFPGATimestamp() - autoStart <= criticalTime && currentStep < criticalStep){
				currentStep = criticalStep;
				checkTime = false;
			}
		}
	}
	
}

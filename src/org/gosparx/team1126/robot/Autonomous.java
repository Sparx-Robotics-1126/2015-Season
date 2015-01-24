package org.gosparx.team1126.robot;

import org.gosparx.team1126.robot.subsystem.Drives;
import org.gosparx.team1126.robot.subsystem.GenericSubsystem;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * A class to control Autonomous
 * @author Alex Mechler {amechler1998@gmail.com}
 * @author Andrew Thompson {andrewt015@gmail.com}
 */
public class Autonomous extends GenericSubsystem{

	/**
	 * An Instance of Drives
	 */
	private Drives drives;
	
	/**
	 * True if we are running auto
	 */
	private boolean runAuto;

	/**
	 * Supports singleton
	 */
	private static Autonomous auto;

	/**
	 * The Physical selector switch for auto modes
	 */
	private AnalogInput selectorSwitch;

	/**
	 * An instance of a driver station
	 */
	private DriverStation ds;

	/**
	 * The current autonomous 
	 */
	private AutoCommands[] currentAuto;

	/**
	 * An array of the parameters
	 */
	private int[][] autoCommands;

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
	private double autoStartTime;

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

	public enum AutoCommands {
		/**
		 * Makes the drives go forward
		 * {Inches, Inches per second}
		 */
		DRIVES_GO_FORWARD,
		
		/**
		 * Makes the drives go in reverse
		 * {Inches, inches per second}
		 */
		DRIVES_GO_REVERSE,
		
		/**
		 * Turns right
		 * {Degrees}
		 */
		DRIVES_TURN_RIGHT,
		
		/**
		 * Turns left
		 * {Degrees}
		 */
		DRIVES_TURN_LEFT,
		
		/**
		 * Stops the drives in case of emergency
		 * {}
		 */
		DRIVES_STOP,
		
		/**
		 * Waits until drives is done with its last command
		 * {}
		 */
		DRIVES_DONE,
		
		/**
		 * Lowers the arms to container level
		 * {}
		 */
		ARMS_DROP,
		
		/**
		 * Raises the arms back up from container level
		 * {}
		 */
		ARMS_RAISE,
		
		/**
		 * Expands the claws that grab the container
		 * {}
		 */
		ARMS_EXPAND,
		
		/**
		 * Contracts the claws that grab the container
		 * {}
		 */
		ARMS_CONTRACT,
		
		/**
		 * Stops arms in case of emergency
		 * {} 
		 */
		ARMS_STOP,
		
		/**
		 * Waits until arms is done with its last command
		 * {}
		 */
		ARMS_DONE,
		
		/**
		 * Lowers the acquisition mechanism
		 * {}
		 */
		ACQ_LOWER,
		
		/**
		 * Raises the acquisition mechanism
		 * {} 
		 */
		ACQ_RAISE,
		
		/**
		 * Turns acquisition rollers on
		 * {}
		 */
		ACQ_ROLLERS_ON,
		
		/**
		 * Turns roller acquisition rollers off
		 * {}
		 */
		ACQ_ROLLERS_OFF,
		
		/**
		 * Stops the acquisitions
		 * {}
		 */
		ACQ_STOP,
		
		/**
		 * Waits until the acquisitions is done
		 * {}
		 */
		ACQ_DONE,
		
		/**
		 * Raise the stack of totes
		 * {}
		 */
		TOTES_RAISE,
		
		/**
		 * Lower the stack of totes
		 * {}
		 */
		TOTES_LOWER,
		
		/**
		 * Ejects the stack totes
		 * {}
		 */
		TOTES_EJECT,
		
		/**
		 * E-stop the tote system
		 * {}
		 */
		TOTES_STOP,
		
		/**
		 * Waits until the tote acq is done with its previous commands
		 * {}
		 */
		TOTES_DONE,
		
		/**
		 * Sets a critical action
		 * {critical time, critical step}
		 */
		CHECK_TIME,
		
		/**
		 * Sleeps
		 * {time in ms}
		 */
		WAIT,
		
		/**
		 * Signals the end of the auto mode 
		 * {}
		 */
		END,
	}
	
	/*********************************************************************************************************************************
	 **************************************************AUTO MODES*********************************************************************
	 *********************************************************************************************************************************/
	
	/**
	 * Drives from the alliance wall to the center of the auto zone 
	 */
	private static final AutoCommands[] DRIVE_TO_AUTOZONE_FROM_STAGING = {
			AutoCommands.DRIVES_GO_FORWARD,
			AutoCommands.DRIVES_DONE,
			AutoCommands.DRIVES_STOP,
			AutoCommands.END
	};
	
	private int[][] DRIVES_TO_AUTOZONE_FROM_STAGING_PARAMS = {
		{163, 24},
		{},
		{},
		{}
	};
	
	private static final String DRIVES_TO_AUTOZONE_FROM_STAGING_NAME = "Drive to Autozone from wall";

	/**
	 * Drives from edge of Autozone into Autozone
	 */
	private static final AutoCommands[] DRIVE_TO_AUTOZONE_FROM_EDGE = {
			AutoCommands.DRIVES_GO_FORWARD,
			AutoCommands.DRIVES_DONE,
			AutoCommands.DRIVES_STOP,
			AutoCommands.END	
	};
	
	private int[][] DRIVES_TO_AUTOZONE_FROM_EDGE_PARAMS = {
			{48, 24},
			{},
			{},
			{}
	};
	
	private static final String DRIVES_TO_AUTOZONE_FROM_EDGE_NAME = "Into Autozone from edge of Autozone";
	
	/**
	 * Moves one yellow tote from the staging zone to the Autozone, robot starts at alliance wall
	 */
	private static final AutoCommands[] ONE_YELLOW_TOTE_FROM_STAGING = {
			AutoCommands.DRIVES_GO_FORWARD,
			AutoCommands.DRIVES_GO_REVERSE,
			AutoCommands.DRIVES_DONE,
			AutoCommands.DRIVES_STOP,
			AutoCommands.END
	};
	
	private int[][] ONE_YELLOW_TOTE_FROM_STAGING_PARAMS = {
			{175, 24}, //TEST POWER!
			{6, 24}, //TEST POWER!
			{},
			{},
			{}
	};
	
	private static final String ONE_YELLOW_TOTE_FROM_STAGING_NAME = "One yellow tote from staging to Sutozone";
	
	
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
		selectorSwitch = new AnalogInput(IO.SELECTOR_SWITCH_CHANNEL);
		ds = DriverStation.getInstance();
		drives = Drives.getInstance();
		SendableChooser chooser = new SendableChooser();
		chooser.addDefault("NO_AUTO", new Integer(0));
		chooser.addObject("AUTO_1", new Integer(1));
		chooser.addObject("AUTO_2", new Integer(2));
		chooser.addObject("AUTO_3", new Integer(3));
		chooser.addObject("AUTO_4", new Integer(4));
		chooser.addObject("AUTO_5", new Integer(5));
		chooser.addObject("AUTO_6", new Integer(6));
		chooser.addObject("AUTO_7", new Integer(7));
		chooser.addObject("AUTO_8", new Integer(8));
		chooser.addObject("AUTO_9", new Integer(9));
		SmartDashboard.putData("Auto Mode", chooser);
		return false;

	}

	/**
	 * Gets the automode if autonomous is not running, otherwise runs auto.
	 */
	@Override
	protected boolean execute() {
		if(runAuto){
			runAuto();
		}else{
			getAutoMode();
			currentStep = 0;
			autoStartTime = Timer.getFPGATimestamp();
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
			switch(currentAuto[currentStep]){
			case DRIVES_GO_FORWARD:
				drives.driveStraight(autoCommands[currentStep][0], autoCommands[currentStep][1]);
				break;
			case DRIVES_GO_REVERSE:
				drives.driveStraight(-autoCommands[currentStep][0], autoCommands[currentStep][1]);
				break;
			case DRIVES_TURN_RIGHT:
				drives.autoTurn(autoCommands[currentStep][0]);
				break;
			case DRIVES_TURN_LEFT:
				drives.autoTurn(-autoCommands[currentStep][0]);
				break;
			case DRIVES_STOP:
				//drives.forceStop();
				break;
			case DRIVES_DONE:
				increaseStep = drives.isDone();
				break;
			case ARMS_DROP:
				break;
			case ARMS_RAISE:
				break;
			case ARMS_EXPAND:
				break;
			case ARMS_CONTRACT:
				break;
			case ARMS_STOP:
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
				criticalStep =  autoCommands[currentStep][0];
				criticalTime = autoCommands[currentStep][1];
				break;
			case WAIT:
				break;
			case END:
				break;
			default:
				runAuto = false;
				LOG.logError("Unknown autocommand: " + currentAuto[currentStep]);
			}
			if(increaseStep){
				currentStep++;
				StringBuilder sb = new StringBuilder();
				sb.append(currentAuto[currentStep]).append("(");
				String prefix = "";
				for(int i:autoCommands[currentStep]){
					sb.append(prefix);
					prefix = ", ";
					sb.append(autoCommands[currentStep][i]);
				}
				sb.append(")");
				LOG.logMessage(sb.toString());
			}
			if(checkTime && Timer.getFPGATimestamp() - autoStartTime >= criticalTime && currentStep < criticalStep){
				currentStep = criticalStep;
				checkTime = false;
			}
		}
	}

	@Override
	protected void liveWindow() {
	
	}
}

package org.gosparx.team1126.robot;

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
	 * An instance of a driverstation
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
		DRIVES_GO_FORWARD,
		DRIVES_GO_REVERSE,
		DRIVES_TURN_RIGHT,
		DRIVES_TURN_LEFT,
		DRIVES_STOP,
		DRIVES_DONE,
		ARMS_DROP,
		ARMS_RAISE,
		ARMS_EXPAND,
		ARMS_CONTRACT,
		ARMS_STOP,
		ARMS_DONE,
		ACQ_LOWER,
		ACQ_RAISE,
		ACQ_ROLLERS_ON,
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
				break;
			case DRIVES_GO_REVERSE:
				break;
			case DRIVES_TURN_RIGHT:
				break;
			case DRIVES_TURN_LEFT:
				break;
			case DRIVES_STOP:
				break;
			case DRIVES_DONE:
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
}

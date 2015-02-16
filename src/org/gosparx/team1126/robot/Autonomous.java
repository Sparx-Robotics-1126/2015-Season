package org.gosparx.team1126.robot;

import org.gosparx.team1126.robot.subsystem.CanAcquisition;
import org.gosparx.team1126.robot.subsystem.Drives;
import org.gosparx.team1126.robot.subsystem.GenericSubsystem;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
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
	 * An instance of CanAcq
	 */
	private CanAcquisition canAcq;

	/**
	 * Supports singleton
	 */
	private static Autonomous auto;

	/**
	 * Used for selecting the smartdashboard
	 */
	private SendableChooser chooser;

	/**
	 * The String name of 
	 */
	private static final String SD_AUTO_NAME = "Auto Mode Selector Switch";

	/**
	 * The string name for the SmartDashboard button
	 */
	private static final String SD_USE_SMART_AUTO = "Use Smart Auto";

	/**
	 * The current autonomous selected
	 */
	private static final String SD_CURRENT_AUTO_MODE = "Current Auto Selected";

	/**
	 * The Physical selector switch for auto modes
	 */
	private AnalogInput selectorSwitch;

	/**
	 * The current autonomous 
	 */
	private int[][] currentAuto;

	/**
	 * True if we are running auto
	 */
	private boolean runAuto = false;

	/**
	 * The name of the current selected auto
	 */
	private String currentAutoName = "";

	/**
	 * The current step of the autonomous we are on
	 */
	private int currentStep = 0;

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
	 * The actual time wanted to wait
	 */
	private double waitTime;

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
	/*********************************************************************************************************************************
	 **********************************************AUTO COMMANDS**********************************************************************
	 *********************************************************************************************************************************/

	public enum AutoCommands {
		/**
		 * Makes the drives go forward
		 * {Inches, Inches per second}
		 */
		DRIVES_GO_FORWARD(0),

		/**
		 * Makes the drives go in reverse
		 * {Inches, inches per second}
		 */
		DRIVES_GO_REVERSE(1),

		/**
		 * Turns right
		 * {Degrees}
		 */
		DRIVES_TURN_RIGHT(2),

		/**
		 * Turns left
		 * {Degrees}
		 */
		DRIVES_TURN_LEFT(3),

		/**
		 * "Dances" to jiggle the arms into the cans
		 * {}
		 */
		DRIVES_DANCE(4),

		/**
		 * Stops the drives in case of emergency
		 * {}
		 */
		DRIVES_STOP(5),

		/**
		 * Waits until drives is done with its last command
		 * {}
		 */
		DRIVES_DONE(6),

		/**
		 * Uses touch sensor to detect the step and stop
		 */
		DRIVES_STEP_LINUP(1126),

		/**
		 * Lowers the arms to container level
		 * {}
		 */
		ARMS_DROP(7),

		/**
		 * Raises the arms back up from container level
		 * {}
		 */
		ARMS_RAISE(8),

		/**
		 * Expands the claws that grab the container
		 * {}
		 */
		ARMS_GRAB(9),

		/**
		 * Contracts the claws that grab the container
		 * {}
		 */
		ARMS_RELEASE(10),

		/**
		 * Stops arms in case of emergency
		 * {} 
		 */
		ARMS_STOP(11),

		/**
		 * Waits until arms is done with its last command
		 * {}
		 */
		ARMS_DONE(12),

		/**
		 * Lowers the acquisition mechanism
		 * {}
		 */
		ACQ_LOWER(13),

		/**
		 * Raises the acquisition mechanism
		 * {} 
		 */
		ACQ_RAISE(14),

		/**
		 * Turns acquisition rollers on
		 * {}
		 */
		ACQ_ROLLERS_ON(15),

		/**
		 * Turns roller acquisition rollers off
		 * {}
		 */
		ACQ_ROLLERS_OFF(16),

		/**
		 * Stops the acquisitions
		 * {}
		 */
		ACQ_STOP(17),

		/**
		 * Waits until the acquisitions is done
		 * {}
		 */
		ACQ_DONE(18),

		/**
		 * Raise the stack of totes
		 * {}
		 */
		TOTES_RAISE(19),

		/**
		 * Lower the stack of totes
		 * {}
		 */
		TOTES_LOWER(20),

		/**
		 * Ejects the stack totes
		 * {}
		 */
		TOTES_EJECT(21),

		/**
		 * E-stop the tote system
		 * {}
		 */
		TOTES_STOP(22),

		/**
		 * Waits until the tote acq is done with its previous commands
		 * {}
		 */
		TOTES_DONE(23),

		/**
		 * Sets a critical action
		 * {critical time, critical step}
		 */
		CHECK_TIME(24),

		/**
		 * Sleeps
		 * {time in ms}
		 */
		WAIT(25),

		/**
		 * Signals the end of the auto mode 
		 * {}
		 */
		END(26);

		private int id;
		private AutoCommands(int id){
			this.id = id;
		}

		public int toId(){
			return id;
		}

		public static AutoCommands fromId(int id){
			for(AutoCommands ac : AutoCommands.values())
				if(ac.id == id)
					return ac;
			throw new RuntimeException("Invalid Id for AutoCommands");
		}

		public static String getName(AutoCommands auto){
			switch(auto){
			case ACQ_DONE: 			return "ACQ has finished";
			case ACQ_LOWER: 		return "ACQ lowered";
			case ACQ_RAISE: 		return "ACQ raised";
			case ACQ_ROLLERS_OFF: 	return "ACQ rollers off";
			case ACQ_ROLLERS_ON: 	return "ACQ on";
			case ACQ_STOP: 			return "ACQ stop";
			case ARMS_RELEASE: 		return "ARMS contracted";
			case ARMS_DONE: 		return "ARMS done";
			case ARMS_DROP: 		return "ARMS dropped";
			case ARMS_GRAB: 		return "ARMS expanded";
			case ARMS_RAISE: 		return "ARMS raised";
			case ARMS_STOP: 		return "AMRS stop";
			case CHECK_TIME: 		return "AUTO Checking time";
			case DRIVES_DANCE: 		return "DRIVES dancing";
			case DRIVES_DONE: 		return "DRIVES done";
			case DRIVES_GO_FORWARD: return "DRIVES moving forward";
			case DRIVES_GO_REVERSE: return "DRIVES moving backwards";
			case DRIVES_STOP: 		return "DRIVES has raged quit (STOPPED)";
			case DRIVES_TURN_LEFT: 	return "DRIVES is turning left";
			case DRIVES_TURN_RIGHT: return "DRIVES is turning right";
			case DRIVES_STEP_LINUP: return "Lined up on step";
			case END: 				return "AUTO has ended";
			case TOTES_DONE: 		return "TOTES done";
			case TOTES_EJECT: 		return "TOTES ejected";
			case TOTES_LOWER: 		return "TOTES lowered";
			case TOTES_RAISE: 		return "Totes rasied";
			case TOTES_STOP: 		return "TOTES ragged quit (STOPPED)";
			case WAIT: 				return "AUTO WAITING....";
			default:				return auto.toId() + "";
			}
		}

	}

	/*********************************************************************************************************************************
	 **************************************************AUTO MODES*********************************************************************
	 *********************************************************************************************************************************/

	/**
	 * NO AUTO
	 */
	private static final String NO_AUTO_NAME = "No Auto";
	private static final int[][] NO_AUTO = {
		{AutoCommands.END.toId()}
	};

	/**
	 * Drives from the alliance wall to the center of the auto zone 
	 */
	private static final String DRIVES_TO_AUTOZONE_FROM_STAGING_NAME = "Into Autozone from wall";
	private int[][] DRIVES_TO_AUTOZONE_FROM_STAGING = {
			{AutoCommands.DRIVES_GO_FORWARD.toId(), 135, 50},
			{AutoCommands.DRIVES_DONE.toId()},
			{AutoCommands.END.toId()}
	};


	/**
	 * Moves one yellow tote from the staging zone to the Autozone, robot starts at alliance wall
	 */
	private static final String ONE_YELLOW_TOTE_FROM_STAGING_NAME = "One yellow tote into Autozone";
	private int[][] ONE_YELLOW_TOTE_FROM_STAGING = {
			{AutoCommands.DRIVES_GO_FORWARD.toId(), 135, 75},
			{AutoCommands.DRIVES_DONE.toId()},
			{AutoCommands.DRIVES_GO_REVERSE.toId(), 6, 100}, 
			{AutoCommands.DRIVES_DONE.toId()},
			{AutoCommands.END.toId()}
	};


	/**
	 * Acquires 2 cans from the step and brings them to the auto zone
	 */
	private static final String TWO_CANS_LEFT_STEP_NAME = "Two Cans from Step to left";
	private static final int[][] TWO_CANS_LEFT_STEP= {
		{AutoCommands.DRIVES_GO_FORWARD.toId(), 58, 100},
		{AutoCommands.DRIVES_DONE.toId()},
		//{AutoCommands.DRIVES_STEP_LINUP.toId()},
		//{AutoCommands.DRIVES_DONE.toId()},
		{AutoCommands.ARMS_DROP.toId()},
		{AutoCommands.CHECK_TIME.toId(), 5, 8},
		{AutoCommands.DRIVES_DANCE.toId()},
		{AutoCommands.ARMS_DONE.toId()},
		//{AutoCommands.DRIVES_STEP_LINUP.toId()},
		//{AutoCommands.DRIVES_DONE.toId()},
		{AutoCommands.DRIVES_GO_REVERSE.toId(), 36, 100},
		{AutoCommands.DRIVES_DONE.toId()},
		{AutoCommands.DRIVES_TURN_RIGHT.toId(), 45},
		{AutoCommands.DRIVES_DONE.toId()},
		{AutoCommands.DRIVES_GO_REVERSE.toId(), 140, 75},
		{AutoCommands.DRIVES_DONE.toId()},
		{AutoCommands.DRIVES_TURN_LEFT.toId(), 35},
		{AutoCommands.DRIVES_DONE.toId()},
		{AutoCommands.DRIVES_GO_REVERSE.toId(), 10, 100},
		{AutoCommands.DRIVES_DONE.toId()},
		{AutoCommands.ARMS_RELEASE.toId()},
		{AutoCommands.ARMS_RAISE.toId()},
		{AutoCommands.END.toId()}
	};

	
	/**
	 * Acquires 2 cans from the step and brings them to the auto zone
	 */
	private static final String TWO_CANS_RIGHT_STEP_NAME = "Two Cans from Step to right";
	private static final int[][] TWO_CANS_RIGHT_STEP= {
		{AutoCommands.DRIVES_GO_FORWARD.toId(), 55, 75},
		{AutoCommands.DRIVES_DONE.toId()},
		{AutoCommands.DRIVES_STEP_LINUP.toId()},
		{AutoCommands.DRIVES_DONE.toId()},
		{AutoCommands.ARMS_DROP.toId()},
		{AutoCommands.CHECK_TIME.toId(), 5, 8},
		{AutoCommands.DRIVES_DANCE.toId()},
		{AutoCommands.ARMS_DONE.toId()},
		{AutoCommands.DRIVES_STEP_LINUP.toId()},
		{AutoCommands.DRIVES_DONE.toId()},
		{AutoCommands.DRIVES_GO_REVERSE.toId(), 36, 50},
		{AutoCommands.DRIVES_DONE.toId()},
		{AutoCommands.DRIVES_TURN_LEFT.toId(), 45},
		{AutoCommands.DRIVES_DONE.toId()},
		{AutoCommands.DRIVES_GO_REVERSE.toId(), 140, 50},
		{AutoCommands.DRIVES_DONE.toId()},
		{AutoCommands.DRIVES_TURN_RIGHT.toId(), 35},
		{AutoCommands.DRIVES_DONE.toId()},
		{AutoCommands.DRIVES_GO_REVERSE.toId(), 10, 100},
		{AutoCommands.DRIVES_DONE.toId()},
		{AutoCommands.ARMS_RELEASE.toId()},
		{AutoCommands.ARMS_RAISE.toId()},
		{AutoCommands.END.toId()}
	};

	private static final int[][] TEST_AUTO = {
		{AutoCommands.DRIVES_GO_FORWARD.toId(), 58, 100},
		{AutoCommands.DRIVES_DONE.toId()},
		//{AutoCommands.DRIVES_STEP_LINUP.toId()},
		//{AutoCommands.DRIVES_DONE.toId()},
		{AutoCommands.ARMS_DROP.toId()},
		{AutoCommands.CHECK_TIME.toId(), 5, 8},
		{AutoCommands.DRIVES_DANCE.toId()},
		{AutoCommands.ARMS_DONE.toId()},
		//{AutoCommands.DRIVES_STEP_LINUP.toId()},
		//{AutoCommands.DRIVES_DONE.toId()},
		{AutoCommands.DRIVES_GO_REVERSE.toId(), 140, 100},
		{AutoCommands.DRIVES_DONE.toId()},
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
		selectorSwitch = new AnalogInput(IO.ANA_AUTOSWITCH);
		drives = Drives.getInstance();
		canAcq = CanAcquisition.getInstance();
		runAuto = false;

		//Auto current selected
		SmartDashboard.putBoolean(SD_USE_SMART_AUTO, false);
		SmartDashboard.putString(SD_CURRENT_AUTO_MODE, "TEST1");
		//Auto Selector
		chooser = new SendableChooser();
		chooser.addDefault(NO_AUTO_NAME, new Integer(1));
		chooser.addObject(DRIVES_TO_AUTOZONE_FROM_STAGING_NAME, new Integer(2));
		chooser.addObject(ONE_YELLOW_TOTE_FROM_STAGING_NAME, new Integer(3));
		chooser.addObject(TWO_CANS_LEFT_STEP_NAME, new Integer(4));
		chooser.addObject(TWO_CANS_RIGHT_STEP_NAME, new Integer(5));
		SmartDashboard.putData(SD_AUTO_NAME, chooser);

		return false;
	}

	/**
	 * Gets the automode if autonomous is not running, otherwise runs auto.
	 */
	@Override
	protected boolean execute() {
		if(runAuto && ds.isEnabled()){
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
		LOG.logMessage("Current Auto Selected: " + currentAutoName);
	}

	/**
	 * Gets the current automode based on the voltages
	 */
	private void getAutoMode(){
		double voltage = selectorSwitch.getVoltage();
		int wantedAuto = -1;
		if(!SmartDashboard.getBoolean(SD_USE_SMART_AUTO)){
			if (voltage >= SELECTION_0){
				wantedAuto = 1;
			}else if(voltage >= SELECTION_1){
				wantedAuto = 2;
			}else if(voltage >= SELECTION_2){
				wantedAuto = 3;
			}else if(voltage >= SELECTION_3){
				wantedAuto = 4;
			}else if(voltage >= SELECTION_4){
				wantedAuto = 5;
			}else if(voltage >= SELECTION_5){
				wantedAuto = 6;
			}else if(voltage >= SELECTION_6){
				wantedAuto = 7;
			}else if(voltage >= SELECTION_7){
				wantedAuto = 8;
			}else if(voltage >= SELECTION_8){
				wantedAuto = 9;
			}else{
				wantedAuto = 10;
			}
		}else{
			wantedAuto = (Integer)chooser.getSelected();
		}
		//SET AUTO;
		switch(wantedAuto){
		case 1:
			currentAutoName = NO_AUTO_NAME;
			currentAuto = NO_AUTO;
			break;
		case 2:
			currentAutoName = DRIVES_TO_AUTOZONE_FROM_STAGING_NAME;
			currentAuto = DRIVES_TO_AUTOZONE_FROM_STAGING;
			break;
		case 3:
			currentAutoName = ONE_YELLOW_TOTE_FROM_STAGING_NAME;
			currentAuto = ONE_YELLOW_TOTE_FROM_STAGING;
			break;
		case 4:
			currentAutoName = TWO_CANS_LEFT_STEP_NAME;
			currentAuto = TWO_CANS_LEFT_STEP;
			break;
		case 5:
			currentAutoName = TWO_CANS_RIGHT_STEP_NAME;
			currentAuto = TWO_CANS_RIGHT_STEP;
			break;
		default:
			currentAutoName = "Test";
			currentAuto = TEST_AUTO;
		}
		SmartDashboard.putString(SD_CURRENT_AUTO_MODE, currentAutoName);
	}

	/**
	 * Executes commands
	 */
	private void runAuto(){
		increaseStep = true;
		if(ds.isAutonomous() && ds.isEnabled() && currentStep < currentAuto.length){
			switch(AutoCommands.fromId(currentAuto[currentStep][0])){
			case DRIVES_GO_FORWARD:
				drives.driveStraight(currentAuto[currentStep][1], (currentAuto[currentStep][2]/100.0));
				break;
			case DRIVES_GO_REVERSE:
				drives.driveStraight(-currentAuto[currentStep][1], (currentAuto[currentStep][2]/100.0));
				break;
			case DRIVES_TURN_RIGHT:
				drives.autoTurn(currentAuto[currentStep][1]);
				break;
			case DRIVES_TURN_LEFT:
				drives.autoTurn(-currentAuto[currentStep][1]);
				break;
			case DRIVES_STEP_LINUP:
				drives.setAutoFunction(Drives.State.AUTO_STEP_LINEUP);
				break;
			case DRIVES_STOP:
				drives.autoForceStop();
				break;
			case DRIVES_DANCE:
				drives.autoDance();
				break;
			case DRIVES_DONE:
				increaseStep = drives.isDone();
				break;
			case ARMS_DROP:
				canAcq.setAutoFunction(CanAcquisition.State.DROP_ARMS);
				break;
			case ARMS_RAISE:
				canAcq.setAutoFunction(CanAcquisition.State.DISABLE);
				break;
			case ARMS_GRAB:
				canAcq.setAutoFunction(CanAcquisition.State.DROP_ARMS);
				break;
			case ARMS_RELEASE:
				canAcq.setAutoFunction(CanAcquisition.State.RELEASE);
				break;
			case ARMS_STOP:
				canAcq.setAutoFunction(CanAcquisition.State.STANDBY);
				break;
			case ARMS_DONE:
				increaseStep = canAcq.isDone();
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
				criticalStep =  currentAuto[currentStep][2];
				criticalTime = currentAuto[currentStep][1];
				break;
			case WAIT:
				waitTime = Timer.getFPGATimestamp() + currentAuto[currentStep][1];
			case END:
				break;
			default:
				runAuto = false;
				LOG.logError("Unknown autocommand: " + currentAuto[currentStep]);
			}
			//WAIT
			if(waitTime < Timer.getFPGATimestamp()){
				increaseStep = true;
				waitTime = Double.MAX_VALUE;
			}
			
			if(increaseStep){
				StringBuilder sb = new StringBuilder();
				sb.append(AutoCommands.getName(AutoCommands.fromId(currentAuto[currentStep][0]))).append("(");
				String prefix = "";
				for(int i = 1; i < currentAuto[currentStep].length; i++){
					sb.append(prefix);
					prefix = ", ";
					sb.append(currentAuto[currentStep][i]);
				}
				sb.append(")");
				LOG.logMessage(sb.toString());
				currentStep++;
			}
			if(checkTime && Timer.getFPGATimestamp() - autoStartTime >= criticalTime && currentStep < criticalStep){
				currentStep = criticalStep;
				checkTime = false;
				LOG.logMessage("TIMEOUT: skipping to: " + AutoCommands.fromId(currentAuto[criticalStep][0]));
			}
		}
	}

	@Override
	protected void liveWindow() {
		LiveWindow.addSensor(getName(), "Auto Switch", selectorSwitch);
	}

	public void runAuto(boolean run){
		runAuto = run;
		LOG.logMessage("****************Auto has been switch to: " + run + "********************");
	}
}

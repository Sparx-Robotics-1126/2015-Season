package org.gosparx.team1126.robot.util;

import edu.wpi.first.wpilibj.DriverStation;

/**
 * @author Alex
 * @date 1/8/15
 * Used to log messages to files. This is the non singleton Logger that communicates with the singleton LogWriter.
 */
public class Logger{
	
	/**
	 * A LogWriter to log our messages
	 */
	private LogWriter writer;
	
	/**
	 * The amount of digits after the decimal in the time
	 */
	private static final int PERCISION = 4;
	
	/**
	 * The total amount of digits in the time
	 */
	private static final int DIGITS_IN_TIME = 8;

	/**
	 * Used to get field times and robot status
	 */
	private DriverStation ds;
	
	/**
	 * The name of the subsytem
	 */
	private String subsystemName;
	
	public Logger(String subsystem){
		subsystemName = subsystem;
		ds = DriverStation.getInstance();
		writer = LogWriter.getInstance();
	}
	
	/**
	 * Sends a log message to the Writer with the format DEBUG[status]{subsystem}(time):message 
	 * @param message - the message to send
	 */
	public void logMessage(String message){
		String status = "";
		if(ds.isDisabled()){
			status = "Dis";
		}else if(ds.isAutonomous()){
			status = "Aut";
		}else if(ds.isEnabled()){
			status = "Tel";
		}
		String semiformatted = Integer.toString(((int)(ds.getMatchTime() * Math.pow(10, PERCISION))));
		String time = ("00000000000".substring(0,DIGITS_IN_TIME - semiformatted.length()) + semiformatted + "").substring(0, DIGITS_IN_TIME);
		String formatted = time.substring(0, time.length() - PERCISION) + "." + time.substring(time.length() - PERCISION);
		String toLog = "DEBUG[" + status + "]{" + subsystemName + "}(" + formatted + "):" + message;
		writer.logString(toLog);
	}
	
	/**
	 * Sends a log message to the Writer with the format ERROR[status]{subsystem}(time):message 
	 * @param message - the message to send
	 */
	public void logError(String message){
		String status = "";
		if(ds.isDisabled()){
			status = "Dis";
		}else if(ds.isAutonomous()){
			status = "Aut";
		}else if(ds.isEnabled()){
			status = "Tel";
		}
		String semiformatted = Integer.toString(((int)(ds.getMatchTime() * Math.pow(10, PERCISION))));
		String time = ("00000000000".substring(0,DIGITS_IN_TIME - semiformatted.length()) + semiformatted + "").substring(0, DIGITS_IN_TIME);
		String formatted = time.substring(0, time.length() - PERCISION) + "." + time.substring(time.length() - PERCISION);
		String toLog = "ERROR[" + status + "]{" + subsystemName + "}(" + formatted + "):" + message;
		writer.logString(toLog);
	}
}

package org.gosparx.team1126.robot.util;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

/**
 * Used to log messages to files. This is the non singleton Logger that communicates with the singleton LogWriter.
 * @author Alex
 * @date 1/8/15
 */
public class Logger{
	
	/**
	 * A LogWriter to log our messages
	 */
	private LogWriter writer;
	
	/**
	 * The amount of digits after the decimal in the time
	 */
	private static final int PRECISION = 4;
	
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
		double time = Timer.getFPGATimestamp();
        time *= Math.pow(10, PRECISION);
        int timeInt = (int)time;
        String timeToFormat = "" + timeInt;
        String timeFormatted = timeToFormat;
        if(timeToFormat.length()<= DIGITS_IN_TIME) {
            timeFormatted = "0000000000000000".substring(0, DIGITS_IN_TIME - timeToFormat.length()) + timeInt;
        }
        timeFormatted = timeFormatted.substring(0,timeFormatted.length() - PRECISION) + "." + timeFormatted.substring(timeFormatted.length() - PRECISION);
		String toLog = "DEBUG[" + status + "]{" + subsystemName + "}(" + timeFormatted + "):" + message;
		writer.logString(toLog + "\n");
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
		double time = ds.getMatchTime();
        time *= Math.pow(10, PRECISION);
        int timeInt = (int)time;
        String timeToFormat = "" + timeInt;
        String timeFormatted = timeToFormat;
        if(timeToFormat.length()<= DIGITS_IN_TIME) {
            timeFormatted = "0000000000000000".substring(0, DIGITS_IN_TIME - timeToFormat.length()) + timeInt;
        }
        timeFormatted = timeFormatted.substring(0,timeFormatted.length() - PRECISION) + "." + timeFormatted.substring(timeFormatted.length() - PRECISION);
		String toLog = "DEBUG[" + status + "]{" + subsystemName + "}(" + timeFormatted + "):" + message;
		writer.logString(toLog);
	}
}

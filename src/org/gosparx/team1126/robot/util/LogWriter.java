package org.gosparx.team1126.robot.util;

import org.gosparx.team1126.robot.subsystem.GenericSubsystem;

import edu.wpi.first.wpilibj.networktables2.util.List;

/**
 * @author Alex
 * @date 1/8/15
 * Used to log messages to files. This is the singleton LogWriter that writes to the files.
 */
public class LogWriter extends GenericSubsystem{
	
	/**
	 * Support for the singleton model
	 */
	private static LogWriter lw;
	
	/**
	 * A list of log messages we need to write so that they always appear in chronological order.
	 */
	private List toLog;
	
	/**
	 * Supports singleton model.
	 * @return - THE ONLY LOGWRITER EVER CREATED
	 */
	public static LogWriter getInstance(){
		if(lw == null){
			lw = new LogWriter();
		}
		return lw;
	}
	
	/**
	 * Creates a LogWriter
	 */
	private LogWriter(){
		super("LogWriter", Thread.NORM_PRIORITY);
		toLog = new List();
	}

	/**
	 * MAKES THINGS IN THE START
	 */
	@Override
	protected boolean init() {
		return true;
	}

	/**
	 * LOOPS AND MAKES MESSAGES APPEAR IN FILES
	 */
	@Override
	protected boolean execute(){
		String toWrite;
		synchronized (toLog) {
			if(toLog.isEmpty())
				try{
					toLog.wait();
				}catch(Exception e){
					e.printStackTrace();
				}
			toWrite = (String) toLog.get(0);
			toLog.remove(0);
		}
		write(toWrite.getBytes());
		System.out.println(toWrite);
		return false;
	}

	/**
	 * THIS THREAD NEEDS SOME ZZZ
	 */
	@Override
	protected long sleepTime() {
		return 50;
	}

	/**
	 * WHY WOULD I LOG WHAT THE LOGWRITER IS DOING JUSTIN
	 */
	@Override
	protected void writeLog() {
		
	}
	
	//TODO IMPLEMENT
	private void write(byte[] bytes){
		
	}
	
	/**
	 * Adds a message to the queue
	 * @param message - the message to add to the queue
	 */
	public void logString(String message){
		toLog.add(message);
		toLog.notify();
	}
}

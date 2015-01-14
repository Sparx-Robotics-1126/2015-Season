package org.gosparx.team1126.robot.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.gosparx.team1126.robot.subsystem.GenericSubsystem;

/**
 * Used to log messages to files. This is the singleton LogWriter that writes to the files.
 * @author Alex
 * @date 1/8/15
 */
public class LogWriter extends GenericSubsystem{

	/**
	 * The file path to store the logs in. /mnt/sda1 is the USB port.
	 */
	private static final String FILE_PATH = "/mnt/sda1/";

	/**
	 * The name of the log
	 */
	private String logName;

	/**
	 * A file for checking if the current log exists
	 */
	private File file;

	/**
	 * The FileOutputStream for accessing the log
	 */
	private FileOutputStream dos;

	/**
	 * Support for the singleton model
	 */
	private static LogWriter lw;

	/**
	 * A queue of log messages we need to write so that they always appear in chronological order.
	 */
	private Queue<String> toLog;

	/**
	 * Supports singleton model.
	 * @return - the long writer
	 */
	public static synchronized LogWriter getInstance(){
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
		toLog = new ConcurrentLinkedQueue<String>();
	}

	/**
	 * Makes sure the file exists and if the directories for it exist
	 */
	@Override
	protected boolean init() {
		try {
			logName = "log" + Calendar.getInstance().get(Calendar.MONTH) + "-" + Calendar.getInstance().get(Calendar.DATE) + "-" + Calendar.getInstance().get(Calendar.YEAR) + "(" + Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + "-" + Calendar.getInstance().get(Calendar.MINUTE) + ").txt";
			file = new File(FILE_PATH + logName);
			file.mkdirs();
			if(file.exists()){
				file.delete();
			}

			file.createNewFile();
		} catch (Exception e) {
		}
		return true;
	}

	/**
	 * Loops and sleeps until toLog is no longer empty, and then writes the information to the log file.
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
			toWrite = toLog.remove();
			toLog.remove(0);
		}
		write(toWrite.getBytes());
		System.out.println(toWrite);
		return false;
	}

	/**
	 * The amount of time for the sleeping between loops of execute()
	 */
	@Override
	protected long sleepTime() {
		return 50;
	}

	/**
	 *	Logs info about the subsystem
	 */
	@Override
	protected void writeLog() {

	}

	/**
	 * writes the passed byte array to the file and then closes the output stream
	 * @param bytes - the array of bytes to write
	 */
	private void write(byte[] bytes){
		try {
			dos = new FileOutputStream(file);
			dos.write(bytes);
			dos.flush();
			dos.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
	}

	/**
	 * Adds a message to the queue
	 * @param message - the message to add to the queue
	 */
	public void logString(String message){
		synchronized (toLog) {
			toLog.add(message);
			toLog.notify();
		}
	}
}
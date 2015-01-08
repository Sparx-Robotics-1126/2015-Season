package org.gosparx.team1126.robot.subsystem;

public class Drives extends GenericSubsystem{

	private static Drives drives;
	
	public static Drives getInstance(){
		if(drives == null){
			drives = new Drives("Drives", Thread.NORM_PRIORITY);
		}
		return drives;
	}
	
	private Drives(String name, int priority) {
		super(name, priority);
	}

	@Override
	protected boolean init() {	
		return false;
	}

	@Override
	protected boolean execute() {
		return false;
	}

	@Override
	protected long sleepTime() {
		return 0;
	}

	@Override
	protected void writeLog() {
		
	}

}

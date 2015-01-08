package org.gosparx.team1126.robot.subsystem;

public class Controls extends GenericSubsystem{

	private static Controls controls;
	
	public static Controls getInstance(){
		if(controls == null){
			controls = new Controls("Controls", Thread.NORM_PRIORITY);
		}
		return controls;
	}
	
	private Controls(String name, int priority) {
		super(name, priority);
	}

	@Override
	protected boolean init() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean execute() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected long sleepTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void writeLog() {
		// TODO Auto-generated method stub
		
	}

}

package org.gosparx.team1126.robot.subsystem;

public class Elevations2 extends GenericSubsystem{

	//******************OBJECTS**********************
	
	
	//******************CONSTANTS********************
	
	
	//******************VARIABLES********************
	
	public Elevations2() {
		super("Elevations", Thread.NORM_PRIORITY);
	}

	@Override
	protected boolean init() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void liveWindow() {
		// TODO Auto-generated method stub
		
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
	
	public enum State{
		Standby;
		
		public String toString(State num){
			switch(num){
			case Standby: 	return "Standby";
			default: 		return "Unknow State";
			}
		}
	}

}

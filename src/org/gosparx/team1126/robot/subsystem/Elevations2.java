package org.gosparx.team1126.robot.subsystem;

public class Elevations2 extends GenericSubsystem{

	//******************OBJECTS**********************
	
	
	//******************CONSTANTS********************
	
	
	//******************VARIABLES********************
	private double wantedSpeed;
	private double wantedPosition;
	private State currState;
	
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
		switch(currState){
		case STANDBY:
			break;
		case MOVE:
			break;
		case SETTING_HOME:
			break;
		}
		return false;
	}

	@Override
	protected long sleepTime() {
		return 10;
	}

	@Override
	protected void writeLog() {
		// TODO Auto-generated method stub
		
	}
	
	public enum State{
		STANDBY,
		MOVE,
		SETTING_HOME;
		
		public String toString(State num){
			switch(num){
			case STANDBY: 		return "Standby";
			case MOVE: 			return "Moving";
			case SETTING_HOME: 	return "Setting Home";
			default: 		return "Unknown State";
			}
		}
	}

}

package org.gosparx.team1126.robot.subsystem;

public class CanAcqTele2 extends GenericSubsystem{

	public CanAcqTele2() {
		super("CanAcqTele", Thread.NORM_PRIORITY);
	}

	@Override
	protected boolean init() {
		return true;
	}

	@Override
	protected void liveWindow() {
	
	}

	@Override
	protected boolean execute() {
		return false;
	}

	@Override
	protected long sleepTime() {
		return 20;
	}

	@Override
	protected void writeLog() {

	}

}

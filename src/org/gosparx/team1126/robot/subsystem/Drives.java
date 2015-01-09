package org.gosparx.team1126.robot.subsystem;

import org.gosparx.team1126.robot.IO;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;

public class Drives extends GenericSubsystem{

	private static Drives drives;
	private Victor v1LeftFront;
	private Victor v2LeftBack;
	private Victor v3RightFront;
	private Victor v4RightBack;
	private Encoder encodeLeft;
	private Encoder encodeRight;
	private Solenoid sol;
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
		v1LeftFront = new Victor(IO.leftFrontDrives);
		v2LeftBack = new Victor(IO.leftBackDrives);
		v3RightFront = new Victor(IO.rightFrontDrives);
		v4RightBack = new Victor(IO.rightBackDrives);
		encodeLeft = new Encoder(IO.leftDrivesEncoderA, IO.leftDrivesEncoderB);
		encodeRight = new Encoder(IO.rightDrivesEncoderA, IO.rightDrivesEncoderB);
		sol = new Solenoid(IO.shiftingPnu);
		return true;
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

package org.gosparx.team1126.robot;

public class IO {
	
	//PWM
	private static final int PWM_DRIVES_LEFT_FRONT		= 0;
	private static final int PWM_DRIVES_LEFT_REAR		= 1;
	private static final int PWM_DRIVES_RIGHT_FRONT		= 2;
	private static final int PWM_DRIVES_RIGHT_REAR		= 3;
	private static final int PWM_ELEVATIONS_LEFT		= 4;
	private static final int PWM_ELEVATIONS_RIGHT 		= 5;
	private static final int PWM_CAN_ROTATE				= 6;
	private static final int PWM_CAN_HOOK				= 7;
	private static final int PWM_ARM_DOWN				= 8;
	private static final int PWM_ARM_UP					= 9;
	
	//Analog
	private static final int ANA_GYRO					= 0;
	private static final int ANA_AUTOSWITCH				= 1;
	private static final int ANA_COLOR_LEFT_RED			= 4;
	private static final int ANA_COLOR_LEFT_BLUE		= 5;
	private static final int ANA_COLOR_RIGHT_RED		= 6;
	private static final int ANA_COLOR_RIGHT_BLUE		= 7;
	
	//DIO
	private static final int DIO_DRIVES_LEFT_ENC_A		= 0;
	private static final int DIO_DRIVES_LEFT_ENC_B		= 1;
	private static final int DIO_DRIVES_RIGHT_ENC_A		= 2;
	private static final int DIO_DRIVES_RIGHT_ENC_B		= 3;
	private static final int DIO_ELEVATIONS_A			= 4;
	private static final int DIO_ELEVATIONS_B			= 5;
	private static final int DIO_CAN_HOOK_A				= 6;
	private static final int DIO_CAN_HOOK_B				= 7;
	private static final int DIO_CAN_ROTATE_A			= 8;
	private static final int DIO_CAN_ROTATE_B			= 9;
	private static final int DIG_RIGHT_IN_CAN                                                                            = 8;
	private static final int DIG_LEFT_IN_CAN                                                                             = 9;
	private static final int DIG_RELEASE_ARMS_SERVO                                                                      = 10;
	private static final int DIG_RAISING_ARMS_SERVO                                                                      = 11;
	private static final int DIG_RIGHT_ARM_SOLO                                                                          = 12;
	private static final int DIG_LEFT_ARM_SOLO                                                                           = 13;
	
	private static final int DIO_LEFT_STEP				= 14;
	private static final int DIO_RIGHT_STEP 			= 15;
	private static final int DIO_CAN_AUTO_RIGHT_GRAB	= 16;
	private static final int DIO_CAN_AUTO_LEFT_GRAB		= 17;
	
	private static final int DIO_ELEVATIONS_ORIGIN		= 21;
	private static final int DIO_TOTE_HOME_RIGHT		= 22;
	private static final int DIO_TOTE_HOME_left 		= 23;
	private static final int DIG_COLOR_LEFT				= 24;
	private static final int DIG_COLOR_RIGHT			= 25;
	
	//PNU
	private static final int PNU_SHIFT					= 0;
	private static final int PNU_ACQ_TOTE				= 1;
	private static final int PNU_ACQ_TRAVEL				= 2;
	private static final int PNU_ACQ_CAN_LEFT			= 3;
	private static final int PNU_ACQ_CAN_RIGHT			= 4;
	private static final int PNU_DISENGAGE_DRIVES		= 5;
	private static final int PNU_TOTE_STOP				= 6;
	private static final int PNU_ACQ_CLUTCH				= 9;
}

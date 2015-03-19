package org.gosparx.team1126.robot;

public final class IO {
	//JOYSTICKS
	public static final int DRIVER_JOYSTICK_LEFT		= 0;
	public static final int DRIVER_JOYSTICK_RIGHT		= 1;
	public static final int OPERATOR_JOYSTICK 			= 2;
	
	//PWM
	public static final int PWM_DRIVES_LEFT_FRONT		= 0;
	public static final int PWM_DRIVES_LEFT_REAR		= 1;
	public static final int PWM_DRIVES_RIGHT_FRONT		= 3;
	public static final int PWM_DRIVES_RIGHT_REAR		= 2;
	public static final int PWM_ELEVATIONS_LEFT			= 4;
	public static final int PWM_ELEVATIONS_RIGHT 		= 5;
	public static final int PWM_CAN_ROTATE				= 6;
	public static final int PWM_CAN_HOOK				= 7;
	
	//Analog
	public static final int ANA_GYRO					= 0;
	public static final int ANA_AUTOSWITCH				= 1;
	
	public static final int ANA_COLOR_LEFT_RED			= 4;
	public static final int ANA_COLOR_LEFT_BLUE			= 5;
	public static final int ANA_COLOR_RIGHT_RED			= 6;
	public static final int ANA_COLOR_RIGHT_BLUE		= 7;
	
	//DIO
	public static final int DIO_DRIVES_LEFT_ENC_A		= 1;
	public static final int DIO_DRIVES_LEFT_ENC_B		= 0;
	public static final int DIO_DRIVES_RIGHT_ENC_A		= 3;
	public static final int DIO_DRIVES_RIGHT_ENC_B		= 2;
	public static final int DIO_ELEVATIONS_LEFT_A		= 5;
	public static final int DIO_ELEVATIONS_LEFT_B		= 4;
	public static final int DIO_CAN_HOOK_A				= 6;
	public static final int DIO_CAN_HOOK_B				= 7;
	public static final int DIO_CAN_ROTATE_A			= 8;
	public static final int DIO_CAN_ROTATE_B			= 9;
	public static final int DIO_ELEVATIONS_RIGHT_A		= 10;
	public static final int DIO_ELEVATIONS_RIGHT_B		= 11;
	
	public static final int DIO_TOTE_SENSOR				= 14;
	public static final int DIO_CAN_AUTO_LEFT			= 16;
	public static final int DIO_CAN_AUTO_RIGHT			= 17;
	
	public static final int DIO_CAN_TELE_HOOK_HOME		= 18;
	public static final int DIO_CAN_TELE_ROTATE_HOME	= 19;
	public static final int DIO_ELEVATIONS_LEFT_ORIGIN	= 20;
	public static final int DIO_ELEVATIONS_RIGHT_ORIGIN	= 21;
	
	public static final int DIO_COLOR_LED_LEFT			= 24;
	public static final int DIO_COLOR_LED_RIGHT			= 25;
	
	//PNU
	public static final int PNU_SHIFT					= 3;
	public static final int PNU_ACQ_TOTE				= 1;
	public static final int PNU_ACQ_TRAVEL				= 2;
	public static final int PNU_ACQ_CAN_LEFT			= 5;
	public static final int PNU_ACQ_CAN_RIGHT			= 4;
	public static final int PNU_DISENGAGE_DRIVES		= 0;
	public static final int PNU_TOTE_STOP				= 6;
	public static final int PNU_ACQ_CLUTCH				= 7;
}
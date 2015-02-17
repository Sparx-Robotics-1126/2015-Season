package org.gosparx.team1126.robot;

public final class IO {
	//JOYSTICKS
	public static final int DRIVER_JOYSTICK_LEFT		= 0;
	public static final int DRIVER_JOYSTICK_RIGHT		= 1;
	public static final int OPERATOR_JOYSTICK 			= 2;
	
	//Motor
	public static final int PWM_LEFT_FRONT_DRIVES 		= 0;
	public static final int PWM_LEFT_BACK_DRIVES		= 1;
	public static final int PWM_RIGHT_FRONT_DRIVES		= 3;
	public static final int PWM_RIGHT_BACK_DRIVES		= 2;
	
	//PWM
	public static final int PWM_DRIVES_LEFT_FRONT		= 0;
	public static final int PWM_DRIVES_LEFT_REAR		= 1;
	public static final int PWM_DRIVES_RIGHT_FRONT		= 2;
	public static final int PWM_DRIVES_RIGHT_REAR		= 3;
	public static final int PWM_ELEVATIONS_LEFT			= 4;
	public static final int PWM_ELEVATIONS_RIGHT 		= 5;
	public static final int PWM_CAN_ROTATE				= 6;
	public static final int PWM_CAN_HOOK				= 7;
	public static final int PWM_ARM_DOWN				= 10;
	public static final int PWM_RIGHT_ARM_UP			= 9;
	public static final int PWM_LEFT_ARM_UP				= 8;
	
	//Analog
	public static final int ANA_GYRO					= 0;
	public static final int ANA_AUTOSWITCH				= 1;
	public static final int ANA_COLOR_LEFT_RED			= 4;

	public static final int ANA_COLOR_LEFT_BLUE			= 5;
	public static final int ANA_COLOR_RIGHT_RED			= 6;
	public static final int ANA_COLOR_RIGHT_BLUE		= 7;
	
	//DIO
	public static final int DIO_DRIVES_LEFT_ENC_A		= 0;
	public static final int DIO_DRIVES_LEFT_ENC_B		= 1;
	public static final int DIO_DRIVES_RIGHT_ENC_A		= 2;
	public static final int DIO_DRIVES_RIGHT_ENC_B		= 3;
	public static final int DIO_ELEVATIONS_A			= 4;
	public static final int DIO_ELEVATIONS_B			= 5;
	public static final int DIO_CAN_HOOK_A				= 6;
	public static final int DIO_CAN_HOOK_B				= 7;
	public static final int DIO_CAN_ROTATE_A			= 8;
	public static final int DIO_CAN_ROTATE_B			= 9;
	
	public static final int DIO_LEFT_STEP				= 14;
	public static final int DIO_RIGHT_STEP 				= 15;
	public static final int DIO_CAN_AUTO_RIGHT_GRAB		= 17;
	public static final int DIO_CAN_AUTO_LEFT_GRAB		= 16;
	public static final int SELECTOR_SWITCH_CHANNEL     = 1;
	
	public static final int DIO_ELEVATIONS_ORIGIN		= 21;
	public static final int DIO_TOTE_HOME_RIGHT			= 22;
	public static final int DIO_CAN_HOOK_HOME			= 22;
	public static final int DIO_CAN_ROTATE_HOME 		= 23;
	public static final int DIO_COLOR_LED_LEFT			= 24;
	public static final int DIO_COLOR_LED_RIGHT			= 25;
	public static final int DIG_DRIVES_ELE_ENC_B		= 5;
	//PNU
	public static final int PNU_SHIFT					= 0;
	public static final int PNU_ACQ_TOTE				= 1;
	public static final int PNU_ACQ_TRAVEL				= 2;
	public static final int PNU_ACQ_CAN_LEFT			= 4;
	public static final int PNU_ACQ_CAN_RIGHT			= 5;
	public static final int PNU_DISENGAGE_DRIVES		= 3;
	public static final int PNU_TOTE_STOP				= 6;
	public static final int PNU_ACQ_CLUTCH				= 7;
}

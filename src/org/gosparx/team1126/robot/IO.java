package org.gosparx.team1126.robot;

public final class IO {
	//Motor
	public static final int PWM_LEFT_FRONT_DRIVES 		= 0;
	public static final int PWM_LEFT_BACK_DRIVES		= 1;
	public static final int PWM_RIGHT_FRONT_DRIVES		= 3;
	public static final int PWM_RIGHT_BACK_DRIVES		= 2;
	
	//PNU
	public static final int PNU_SHIFTING 				= 0;
	
	public static final int PWM_DRIVES_LEFT_FRONT																		 = 0;
	public static final int PWM_DRIVES_LEFT_REAR																		 = 1;
	public static final int PWM_DRIVES_RIGHT_FRONT																	     = 2;
	public static final int PWM_DRIVES_RIGHT_REAR																		 = 3;
	public static final int PWM_ELEVATIONS																				 = 4;
	public static final int PWM_ACQ_TOTES_LEFT																			 = 5;
	public static final int PWM_ACQ_TOTES_RIGHT																		 = 6;
	public static final int PWM_ACQ_CAN_LEFT																			 = 7;
	public static final int PWM_ACQ_CAN_RIGHT																			 = 8;
	//Encoders
	public static final int ENCODER_RIGHT_DRIVES_A 		= 0;
	public static final int ENCODER_RIGHT_DRIVES_B 		= 1;
	public static final int ENCODER_LEFT_DRIVES_A 		= 2;
	public static final int ENCODER_LEFT_DRIVES_B		= 3;
	
	//Joystick
	public static final int DRIVER_JOYSTICK_LEFT		= 0;
	public static final int DRIVER_JOYSTICK_RIGHT		= 1;
	
	//Sensors 
	public static final int COLOR_RIGHT_RED				= 2;
	public static final int COLOR_RIGHT_BLUE			= 3;
	public static final int COLOR_RIGHT_GREEN			= 9;//NOT USED
	public static final int COLOR_RIGHT_LED				= 5;
	public static final int COLOR_LEFT_RED				= 4;//TODO: CHECK
	public static final int COLOR_LEFT_BLUE				= 5;//TODO: CHECK
	public static final int COLOR_LEFT_GREEN			= 8;//NOT USED
	public static final int COLOR_LEFT_LED				= 4;
	public static final int SWTICH_RIGHT_DRIVES 		= 6;
	public static final int SWITCH_LEFT_DRIVES 			= 7;
	
	public static final int ANA_GYRO																					 = 0;
	public static final int ANA_AUTOSWITCH																				 = 1;
	public static final int ANA_COLOR_LEFT_RED																			 = 4;
	public static final int ANA_COLOR_LEFT_BLUE																		 = 5;
	public static final int ANA_COLOR_RIGHT_RED																		 = 6;
	public static final int ANA_COLOR_RIGHT_BLUE																		 = 7;
	
	
	public static final int PNU_SHIFT																					 = 0;
	public static final int PNU_ACQ_TOTE																				 = 1;
	public static final int PNU_ACQ_CAN_LEFT																			 = 2;
	public static final int PNU_ACQ_CAN_RIGHT																			 = 3;
}

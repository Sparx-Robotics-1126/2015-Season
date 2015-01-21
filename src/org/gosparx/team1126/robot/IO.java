package org.gosparx.team1126.robot;

public class IO {
	
	/**
	 * PWM
	 */
	public static final int PWM_DRIVES_LEFT_FRONT																		 = 0;
	public static final int PWM_DRIVES_LEFT_REAR																		 = 1;
	public static final int PWM_DRIVES_RIGHT_FRONT																	     = 2;
	public static final int PWM_DRIVES_RIGHT_REAR																		 = 3;
	public static final int PWM_ELEVATIONS																				 = 4;
	public static final int PWM_ACQ_TOTES_LEFT																			 = 5;
	public static final int PWM_ACQ_TOTES_RIGHT																		 = 6;
	public static final int PWM_ACQ_CAN_LEFT																			 = 7;
	public static final int PWM_ACQ_CAN_RIGHT																			 = 8;
	
	/**
	 * Analog
	 */
	public static final int ANA_GYRO																					 = 0;
	public static final int ANA_AUTOSWITCH																				 = 1;
	public static final int ANA_COLOR_LEFT_RED																			 = 4;
	public static final int ANA_COLOR_LEFT_BLUE																		 = 5;
	public static final int ANA_COLOR_RIGHT_RED																		 = 6;
	public static final int ANA_COLOR_RIGHT_BLUE																		 = 7;
	public static final int SELECTOR_SWITCH_CHANNEL                                                                      =8;
	/**
	 * DIO
	 */
	public static final int DIG_DRIVES_LEFT_ENC_A																		 = 0;
	public static final int DIG_DRIVES_LEFT_ENC_B																		 = 1;
	public static final int DIG_DRIVES_RIGHT_ENC_A																		 = 2;
	public static final int DIG_DRIVES_RIGHT_ENC_B																		 = 3;
	public static final int DIG_DRIVES_ELE_ENC_A																		 = 4;
	public static final int DIG_DRIVES_ELE_ENC_B																		 = 5;
	public static final int DIG_COLOR_LEFT																				 = 6;
	public static final int DIG_COLOR_RIGHT																			 = 7;
	
	/**
	 * PNU
	 */
	public static final int PNU_SHIFT																					 = 0;
	public static final int PNU_ACQ_TOTE																				 = 1;
	public static final int PNU_ACQ_CAN_LEFT																			 = 2;
	public static final int PNU_ACQ_CAN_RIGHT																			 = 3;
}

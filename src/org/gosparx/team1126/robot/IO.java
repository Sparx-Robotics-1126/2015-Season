package org.gosparx.team1126.robot;

public class IO {
	
	/**
	 * PWM
	 */
	private static final int PWM_DRIVES_LEFT_FRONT																		 = 0;
	private static final int PWM_DRIVES_LEFT_REAR																		 = 1;
	private static final int PWM_DRIVES_RIGHT_FRONT																	     = 2;
	private static final int PWM_DRIVES_RIGHT_REAR																		 = 3;
	private static final int PWM_ELEVATIONS																				 = 4;
	private static final int PWM_ACQ_TOTES_LEFT																			 = 5;
	private static final int PWM_ACQ_TOTES_RIGHT																		 = 6;
	private static final int PWM_ACQ_CAN_LEFT																			 = 7;
	private static final int PWM_ACQ_CAN_RIGHT																			 = 8;
	
	/**
	 * Analog
	 */
	private static final int ANA_GYRO																					 = 0;
	private static final int ANA_AUTOSWITCH																				 = 1;
	private static final int ANA_COLOR_LEFT_RED																			 = 4;
	private static final int ANA_COLOR_LEFT_BLUE																		 = 5;
	private static final int ANA_COLOR_RIGHT_RED																		 = 6;
	private static final int ANA_COLOR_RIGHT_BLUE																		 = 7;
	
	/**
	 * DIO
	 */
	private static final int DIG_DRIVES_LEFT_ENC_A																		 = 0;
	private static final int DIG_DRIVES_LEFT_ENC_B																		 = 1;
	private static final int DIG_DRIVES_RIGHT_ENC_A																		 = 2;
	private static final int DIG_DRIVES_RIGHT_ENC_B																		 = 3;
	private static final int DIG_DRIVES_ELE_ENC_A																		 = 4;
	private static final int DIG_DRIVES_ELE_ENC_B																		 = 5;
	private static final int DIG_COLOR_LEFT																				 = 6;
	private static final int DIG_COLOR_RIGHT																			 = 7;
	private static final int DIG_RIGHT_IN_CAN                                                                            = 8;
	private static final int DIG_LEFT_IN_CAN                                                                             = 9;
	private static final int DIG_RELEASE_ARMS_SERVO                                                                      = 10;
	private static final int DIG_RAISING_ARMS_SERVO                                                                      = 11;
	private static final int DIG_RIGHT_ARM_SOLO                                                                          = 12;
	private static final int DIG_LEFT_ARM_SOLO                                                                           = 13;
	
	/**
	 * PNU
	 */
	private static final int PNU_SHIFT																					 = 0;
	private static final int PNU_ACQ_TOTE																				 = 1;
	private static final int PNU_ACQ_CAN_LEFT																			 = 2;
	private static final int PNU_ACQ_CAN_RIGHT																			 = 3;
}

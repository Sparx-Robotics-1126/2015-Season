
package org.gosparx.team1126.robot;


import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

/**
 * This is a demo program showing the use of the RobotDrive class.
 * The SampleRobot class is the base of a robot application that will automatically call your
 * Autonomous and OperatorControl methods at the right time as controlled by the switches on
 * the driver station or the field controls.
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 * WARNING: While it may look like a good choice to use for your code if you're inexperienced,
 * don't. Unless you know what you are doing, complex code will be much more difficult under
 * this system. Use IterativeRobot or Command-Based instead if you're new.
 */
public class Robot extends SampleRobot {
    private Joystick leftStick;
    private Joystick rightStick;
    private Victor v1LeftFront;
    private Victor v2LeftBack;
    private Victor v3RightFront;
    private Victor v4RightBack;

    public Robot() {
        leftStick = new Joystick(0);
        rightStick = new Joystick(1);
        v1LeftFront = new Victor(0);
        v2LeftBack = new Victor(1);
        v3RightFront = new Victor(2);
        v4RightBack = new Victor(3);
    }

    /**
     * Drive left & right motors for 2 seconds then stop
     */
    public void autonomous() {
        
        Timer.delay(2.0);		//    for 2 seconds
       
    }

    /**
     * Runs the motors with arcade steering.
     */
    public void operatorControl() {
       
        while (isOperatorControl() && isEnabled()) {
           v1LeftFront.set(leftStick.getY());
           v2LeftBack.set(leftStick.getY());
           v3RightFront.set(rightStick.getY());
           v4RightBack.set(rightStick.getY());
            Timer.delay(0.005);		// wait for a motor update time
        }
    }

    /**
     * Runs during test mode
     */
    public void test() {
    }
}

package org.gosparx.team1126.robot.sensors;

import edu.wpi.first.wpilibj.DriverStation;
/**
 * Logic of a Proportional Integral Derivative loop. Must be constructed first,
 * then it must receive continual updates in order to receive accurate output
 * values.
 */
public class PID {
	
	/*
	 * P, I, D 
	 */
    private double error, integral, derivative, lastInput;
    
    /**
     * Scaling factors
     */
    private double pGain, iGain, dGain;
    
    /**
     * Integral limit
     */
    private double iMax;
    
    /**
     * Output limit
     */
    private double lowerLimit, upperLimit;
    
    /**
     * Booleans to set correct output
     */
    private boolean updated_FLAG, reverse_FLAG, brake_FLAG;
    
    /**
     *SetPoint
     */
    private double goal;
    
    /**
     * Output value
     */
    private double output;
    
    /**
     * Last time in MS when PID loop ran
     */
    private long lasttime;
    
    /**
     * The time since the last run of the PID loop
     */
    private double elapsedtime;

    /**
     * Limitless Constructor - initializes PID
     * @param pScale - the modifier for the Proportional calculation
     * @param iScale - the modifier for the integral calculation
     * @param dScale - the modifier for the derivative calculation, give the 
     * dScale as 0 if you wish to ignore the derivative
     * @param iMax - the maximum absolute value the integral should rise to.
     * @param fastBrake - whether or not to set output to 0 when goal is 0.
     * @param reversed - whether or not the output should be inverted: 
     * true if invert, false if not
     * **NOTE** the modifiers are all multiplicative. Use fractions for divisors.
     */
    public PID(double pScale, double iScale, double iMax, double dScale, 
                                        boolean fastBrake, boolean reversed){
        error = 0;
        integral = 0;
        derivative = 0;
        output = 0;
        lastInput = 0;
        pGain = pScale;
        iGain = iScale;
        dGain = dScale;
        upperLimit = 1.0;
        lowerLimit = -1.0;
        this.iMax = iMax;

        brake_FLAG = fastBrake;
        reverse_FLAG = reversed;
        lasttime = System.currentTimeMillis();
    }
    
    /**
     * Limited Constructor
     * @param pScale - the modifier for the Proportional calculation
     * @param iScale - the modifier for the integral calculation
     * @param iMax - the maximum absolute value the integral should rise to.
     * @param dScale - the modifier for the derivative calculation, give the 
     * dScale as 0 if you wish to ignore the derivative
     * @param upperLimit - the upper limit of the output.
     * @param lowerLimit - the lower limit of the output.
     * @param fastBrake - whether or not to set output to 0 when goal is 0.
     * @param reversed - whether or not the output should be inverted: 
     * true if invert, false if not
     * **NOTE** the modifiers are all multiplicative. Use fractions for divisors.
     */
    public PID(double pScale, double iScale, double iMax, double dScale, 
                              double upperLimit, double lowerLimit, 
                              boolean fastBrake, boolean reversed){
        error = 0;
        integral = 0;
        derivative = 0;
        lastInput = 0;
        output = 0;
        pGain = pScale;
        iGain = iScale;
        dGain = dScale;
        this.iMax = iMax;
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
        brake_FLAG = fastBrake;
        reverse_FLAG = reversed;
        lasttime = System.currentTimeMillis();
    }
    
    /**
     * Runs through update calculations. Drives the PID.
     * @param input - the current input value for the loop
     * @return the output generated by this update.
     */
    public double update(double input){
        long currenttime;

        // Calculate the last time the update routine was run and store this
        // in a variable as seconds (or fraction of seconds).  This will be
        // used as a multiplier to convert each of the PID components into
        // change/second and make the it less sensitive to variations in
        // interval (either through delays in processing or changes in Sleep
        // time between executions).
        
        currenttime = System.currentTimeMillis();
        elapsedtime = ((double) (currenttime - lasttime)) / 1000.0;

        // The brake flag automatically sets the output to zero when the goal
        // is zero.  This allows the victor/jaguar to provide the breaking
        // by shorting the motor leads.  (in brake mode, not coast mode)
        
        if (brake_FLAG && goal == 0) {
            lasttime = currenttime;
            error = 0;
            integral = 0;
            derivative = 0;
            lastInput = input;
            updated_FLAG = true;
            return (output = 0);
        }

        // If more than 1/2 second has passed since the last execution (which
        // typically will mean something unexpected happened within the cRIO or
        // is the first update after bootup), assume a base elaped time of
        // 40 milliseconds and ignore the derivative.
        
        if (elapsedtime > .5) {
            elapsedtime = 0.04;
            lastInput = input;
        }
        
        // If 1 millisecond or less has passed since last execution, just
        // return the current output value.
        
        if (elapsedtime <= 0.0011)
            return (output);

        lasttime = currenttime;

        // Calculate the offset between the current value and the goal and
        // add this amount (x iGain) to the integral (cumulative error)
        
        error = (goal - input);
        integral += (error * iGain * elapsedtime);
        error *= pGain;

        // Check the integral against the absolute integral maximum.
        
        if (integral > iMax)
            integral = iMax;
        else if (integral < -iMax)
            integral = -iMax;

        // If derivative is required, calculate the D component - rate of
        // change in the input.  The division by elapsedtime is done in order
        // to convert the change in input into a rate per second.
        
        if (dGain != 0.0)
            derivative = (lastInput - input) * dGain / elapsedtime;
        else
            derivative = 0.0;

        lastInput = input;
        
        // Calculates the raw output of the 3 components
        
        output = error + integral + derivative;

        // Checks the limits for the output and integral against the
        // upper and lower specified limits and adjusts the two values
        // accordingly.
        
        if (output > upperLimit) {
            if (integral > 0)
                integral -= (output - upperLimit);

            output = upperLimit;
            
            if (integral < 0)
                integral = 0;
        }
        else if (output < lowerLimit) {
            if (integral < 0)
                integral -= (output - lowerLimit);

            output = lowerLimit;
            
            if (integral > 0)
                integral = 0;
        }

        // If the motor needs to work opposite normal way (increasing motor
        // output increases the input), then negate the value.
        
        if (reverse_FLAG)
            output *= -1;

        // Set up updated flag to true.
        
        updated_FLAG = true;

        // Check to see if the robot is disabled.  If so, then zero out the
        // intgral and output.
        
        if (!DriverStation.getInstance().isEnabled()) {
            integral = 0;
            output = 0;
        }

        // Return the new motor output speed.
        
        return output;
    }
    
    /**
     * Sets the goal to work towards. Drives the entire PID loop.
     * @param goal - the requested goal to move toward.
     */
    public void setGoal(double goal){
        this.goal = goal;
    }

    /**
     * Changes the pGain to a different value to change the responsiveness of the PID.
     * @param pScale - the requested pGain
     * @param iScale - the requested iGain
     * @param dScale - the requested dGain
     */
    public void setGains(double pScale, double iScale, double dScale){
        pGain = pScale;
        iGain = iScale;
        dGain = dScale;        
    }
    
    /**
     * Sets the lower and upper limits of the integral value.
     * @param upperLimit - the requested upper limit
     * @param lowerLimit - the requested lower limit
     */
    public void setLimits(double upperLimit, double lowerLimit){
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
    }
    
    /**
     * Getter for the output of the loop.
     * @return the currently calculated output of the PID loop.
     */
    public double getOutput(){
        if (!updated_FLAG)
            System.out.println("Old PID output returned! Has not been updated!");
        updated_FLAG = false;
        return output;
    }
    
    /**
     * Sets the output manually.
     * **NOTE** Output will be overwritten by update(double) if it is called.
     * @param value 
     */
    public void setOutput(double value){
        output = value;
    }
    
    /**
     * Resets the PID to initial state.
     */
    public void reset(){
        error = 0;
        integral = 0;
        derivative = 0;
        lastInput = 0;
        goal = 0;
        output = 0;
    }
    
    /**
     * Getter for the goal value.
     * @return 
     */
    public double getGoal(){
        return goal;
    }
    
    /**
     * Getter for the current error.
     * @return 
     */
    public double getInternalP(){
        return error;
    }
   
    /**
     * Getter for the current integral.
     * @return 
     */
    public double getInternalI(){
        return integral;
    }

    /**
     * Getter for the current derivative.
     * @return 
     */
    public double getInternalD(){
        return derivative;
    }
    
    /**
     * Getter for the elapsed time between current and previous iterations.
     * @return 
     */
    public double getInternalET(){
        return elapsedtime;
    }
    
    /**
     * Getter for the current proportional gain.
     * @return 
     */
    public double getP(){
        return pGain;
    }

    /**
     * Getter for the current integral gain.
     * @return 
     */
    public double getI(){
        return iGain;
    }
    
    /**
     * Getter for the current derivative gain.
     * @return 
     */
    public double getD(){
        return dGain;
    }
}

package org.gosparx.team1126.robot.util;

import org.gosparx.team1126.robot.subsystem.GenericSubsystem;

import edu.wpi.first.wpilibj.Joystick;

/**
 * A class for more advanced data collection from the joysticks.
 * @author Alex Mechler {amechler1998@gmail.com}
 */
public class AdvancedJoystick extends GenericSubsystem{

	/**
	 * The joystick we are gathering data from
	 */
	private Joystick joy;

	/**
	 * The previous value of the joystick buttons
	 */
	private boolean[] prevValues;

	/**
	 * The listener that is listening for the rising/falling edges
	 */
	private JoystickListener listener;

	/**
	 * the port the joystick is in
	 */
	private int port;

	/**
	 * Any axis value under this will be treated as 0
	 */
	private static final double DEADBAND = .04;

	/**
	 * Creates a new advanced joystick
	 * @param name - The name of the thread
	 * @param joyPort - the port the joystick is in
	 */
	public AdvancedJoystick(String name, int joyPort) {
		super(name, Thread.NORM_PRIORITY);
		joy = new Joystick(joyPort);
		port = joyPort;
		prevValues = new boolean[joy.getButtonCount() + 1];
	}

	/**
	 * Initializes things.
	 */
	@Override
	protected boolean init() {
		return true;
	}

	/**
	 * Uses livewindow
	 */
	@Override
	protected void liveWindow() {

	}

	/**
	 * loops, updates and compares values
	 */
	@Override
	protected boolean execute() {
		for(int i = 1; i <= joy.getButtonCount(); i++){
			hasChanged(i);
			prevValues[i] = joy.getRawButton(i);
		}
		return false;
	}

	/**
	 * How long to sleep in ms
	 */
	@Override
	protected long sleepTime() {
		return 20;
	}

	/**
	 * writes log info
	 */
	@Override
	protected void writeLog() {

	}

	/**
	 * checks to see if the button has had a rising/falling edge and will notify the listener if it is
	 * @param button - the id of the button
	 */
	private void hasChanged(int button){
		if(joy.getRawButton(button) != prevValues[button]){
			if(listener != null){
				listener.actionPerformed(new ButtonEvent(port, button, joy.getRawButton(button)));
				LOG.logMessage("ButtonEvent( " + port + ", " + button + ", " + joy.getRawButton(button));
			}
		}
	}

	/**
	 * Get the axis value accounting for the deadband
	 * @param axis - the int of the axis to get
	 * @return the modified axis value
	 */
	public double getAxis(int axis){
		return (joy.getRawAxis(axis) > DEADBAND) ? joy.getRawAxis(axis) : 0;
	}

	/**
	 * Adds a new ActionListener
	 * @param listener - the listener
	 */
	public void addActionListener(JoystickListener listener){
		this.listener = listener;
	}

	/**
	 * A class that represents all of the button events that could happen
	 * @author Alex Mechler {amechler1998@gmail.com}
	 */
	public class ButtonEvent{

		/**
		 * True if this is a rising edge, false if its a falling edge
		 */
		private boolean risingEdge;
		
		/**
		 * The button id that triggered the event
		 */
		private int buttonID;
		
		/**
		 * The port of the joystick that triggered the event
		 */
		private int joy;
		
		/**
		 * Creates a new ButtonEvent
		 * @param port - the port of the joystick
		 * @param buttonID - the button that created the event
		 * @param risingEdge - if this is a rising edge or falling edge
		 */
		public ButtonEvent(int port, int buttonID, boolean risingEdge){
			this.buttonID = buttonID;
			this.risingEdge = risingEdge;
			joy = port;
		}

		/**
		 * @return true - if rising edge
		 * 		   false - if falling edge
		 */
		public boolean isRising(){
			return risingEdge;
		}

		/**
		 * @return the button id that the event was triggered from 
		 */
		public int getID(){
			return buttonID;
		}

		/**
		 * @return The port of the joystick that triggered the event
		 */
		public int getPort(){
			return joy;
		}
	}

	/**
	 * An interface that allows for listening to the advanced joystick.
	 * @author Alex Mechler {amechler1998@gmail.com}
	 */
	public interface JoystickListener{
		public void actionPerformed(ButtonEvent e);
	}
}

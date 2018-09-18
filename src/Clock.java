import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * A Clock class that tracks the player's time.
 * 
 * @author Spencer Hedeen
 */

public class Clock extends JLabel implements ActionListener {

  /**
   * Serial id to make the compiler happy.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Timer that increments every second.
   */
  private static Timer timer;

  /**
   * The current time displayed on the clock.
   */
  private static long elapsedTime;

  /**
   * The font.
   */
  private Font font = new Font("Lucida Sans Regular", Font.BOLD, 22);

  /**
   * The initial time the clock is reset to.
   */
  private static long initialTime;

  /**
   * True if the timer is stopped, false otherwise.
   */
  private static boolean timerStopped = true;

  /**
   * Returns the current time as in hh:mm:ss.
   * 
   * @return the current time
   */
  public static String getTime() {
    // Get the elapsed time and divide it by 10
    // to get the seconds
    long newElapsedTime = elapsedTime / 10;

    // Calculate the display values of hours
    // minutes and seconds
    long hours = (newElapsedTime / 3600);
    long minutes = (newElapsedTime / 60) % 60;
    long seconds = newElapsedTime % 60;

    // Declare some strings for displaying values
    String hoursString;
    String minutesString;
    String secondsString;

    // Add a 0 if any of the times are only one
    // digit long
    if (hours < 10) {
      hoursString = "0" + String.valueOf(hours);
    } else {
      hoursString = String.valueOf(hours);
    }
    if (minutes < 10) {
      minutesString = "0" + String.valueOf(minutes);
    } else {
      minutesString = String.valueOf(minutes);
    }
    if (seconds < 10) {
      secondsString = "0" + String.valueOf(seconds);
    } else {
      secondsString = String.valueOf(seconds);
    }

    // Finally display the string
    return hoursString + ":" + minutesString + ":" + secondsString;
  }

  /**
   * Returns the current time.
   * 
   * @return the current time
   */
  public static long getTimeLong() {
    return elapsedTime / 10;
  }

  /**
   * Resets the clock to 0.
   */
  public static void reset() {
    elapsedTime = initialTime;
    stop();
  }

  /**
   * Starts the timer.
   */
  public static void start() {
    timerStopped = false;
    timer.start();
  }

  /**
   * Stop incrementing the clock.
   */
  public static void stop() {
    timerStopped = true;
  }

  /**
   * Runs the timer and displays the time on the screen.
   */
  public Clock() {
    super(" ");
    Clock.initialTime = elapsedTime;

    // Fix the size to be 160 x 26 (pixels)
    setPreferredSize(new Dimension(160, 26));

    // Set the new panel to be transparent
    setOpaque(false);

    // Text formatting
    setFont(font);
    setForeground(Color.WHITE);
    setHorizontalAlignment(CENTER);

    // Create the timer of 100 milliseconds (0.1 seconds)
    // It needs to be small so the update time is quick
    // when resetting the game.
    timer = new Timer(100, this);
  }

  /**
   * Adds the action performed function which increments the time.
   */
  @Override
  public void actionPerformed(ActionEvent event) {
    if (!timerStopped) {
      elapsedTime++;
    }
    repaint();
  }

  /**
   * Displays the time.
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    // Display the string
    setText(getTime());
  }

  /**
   * Sets the time for the clock.
   * 
   * @param savedTime
   *          new time to set
   */
  public void setTime(long savedTime) {
    // Multiply by 10 to save the correct time, since we increment every .1 seconds
    elapsedTime = savedTime * 10;
    initialTime = savedTime * 10;
  }
}

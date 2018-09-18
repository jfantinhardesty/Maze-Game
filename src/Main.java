import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * A starting point for the COSC 3011 programming assignment Probably need to
 * fix a bunch of stuff, but this compiles and runs.
 *
 * <p>This COULD be part of a package but I choose to make the starting point
 * NOT a package. However all other add elements can certainly be sub-packages,
 * and probably should be.
 * 
 * @author Kim Buckner Date: Feb 01, 2017
 */

public class Main {
  /**
   * Main function for the program.
   * 
   * @param args
   *          generic main arguments, do nothing
   */
  public static void main(String[] args) {
    // This is the play area
    GameWindow game = new GameWindow("Gemini aMaze");

    // Set up the window
    game.initializeWindow();
    game.setUp();

    try {
      // The 4 that are installed on Linux here
      // May have to test on Windows boxes to see what is there.
      UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

      // This is the "Java" or CrossPlatform version and the default
      // UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
      // Linux only
      // UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
      // really old style Motif
      // UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
    } catch (UnsupportedLookAndFeelException e) {
      // handle possible exception
    } catch (ClassNotFoundException e) {
      // handle possible exception
    } catch (InstantiationException e) {
      // handle possible exception
    } catch (IllegalAccessException e) {
      // handle possible exception
    }

  }
}

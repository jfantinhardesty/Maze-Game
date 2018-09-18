import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 * A Button class that stores three buttons. It holds the new game, reset and
 * quit buttons. This class is in charge of adding action listeners to all of
 * the buttons.
 * 
 * @author James Fantin
 */
public class Button extends JPanel implements ActionListener {
  /**
   * Serial id to make the compiler happy.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Pop-up menu that displays when the file button is clicked.
   */
  private JPopupMenu menu;

  /**
   * Tile size in pixels.
   */
  private int tileDims;

  /**
   * Creates the new game, reset, and quit buttons.
   * 
   * @param tileDims
   *          the dimension of the tiles
   */
  public Button(int tileDims) {
    super();

    this.tileDims = tileDims;

    // Makes the JPanel transparent
    setOpaque(false);

    // This will be the layout for the buttons
    FlowLayout flow = new FlowLayout();
    setLayout(flow);

    // create buttons
    createFile(this);
    createReset(this);
    createQuit(this);

    // Needs to update the frame to see the buttons.
    revalidate();
  }

  /**
   * Handles all the buttons actions when they are pressed.
   * 
   * @param event
   *          Event on button press
   */
  @Override
  public void actionPerformed(ActionEvent event) {
    Component pressed = (Component) event.getSource();
    String name = pressed.getName();

    if (name == "File") {
      // File button pressed
      showPopup(event);
    } else if (name == "Reset") {
      // Reset button pressed
      TileMover.moveTilesHome();
      Clock.reset();
    } else if (name == "Quit") {
      // Quit button pressed

      // Prompt the user to save the game if it is modified
      if (TileController.isPlayed()) {
        showSave(false);
      }
      System.exit(0);
    } else if (name == "Load") {
      // File button pressed
      showLoad();
    } else if (name == "Save") {
      // Save button pressed
      showSave(true);
    }
  }

  /**
   * (WIP) On click, displays a pop-up menu with load and save game.
   * 
   * @param panel
   *          Panel where the button will be added
   */
  private void createFile(Container panel) {
    JButton file = new JButton("File");
    file.setName("File");
    file.addActionListener(this);

    // Create the popup item
    menu = new JPopupMenu();

    // Create JMenuItems
    JMenuItem save = new JMenuItem("Save");
    save.setName("Save");
    save.addActionListener(this);

    JMenuItem load = new JMenuItem("Load");
    load.setName("Load");
    load.addActionListener(this);

    // Add JMenuItems to JPopupMenu
    menu.add(save);

    menu.add(load);

    panel.add(file);
  }

  /**
   * (WIP) When pressed, quits the game and closes window. Adds button to the
   * panel.
   * 
   * @param panel
   *          Panel where the button will be added
   */
  private void createQuit(Container panel) {
    JButton quit = new JButton("Quit");
    quit.setName("Quit");
    quit.addActionListener(this);

    panel.add(quit);
  }

  /**
   * (WIP) When pressed, resets game with the same tiles. Adds button to the
   * panel.
   * 
   * @param panel
   *          Panel where the button will be added
   */
  private void createReset(Container panel) {
    JButton reset = new JButton("Reset");
    reset.setName("Reset");
    reset.addActionListener(this);

    panel.add(reset);
  }

  /**
   * Displays the pop-up window for the user to load a file.
   */
  public void showLoad() {
    boolean loop = true;

    // Stop the timer
    Clock.stop();

    // Instantiate the file
    File newFile = null;

    // Loop until the user enters a valid file or cancels.
    while (loop) {
      String input = JOptionPane.showInputDialog(null, "Load file from location.", "Load",
          JOptionPane.QUESTION_MESSAGE);

      // If the user pressed cancel then close the window.
      if (input == null) {
        Clock.start();
        return;
      }

      newFile = new File(input);

      // If the file doesn't exist, prompt the user. If it does then continue to read
      // from file
      if (!newFile.isFile()) {
        JOptionPane.showMessageDialog(null, "File not found.", "Error", JOptionPane.ERROR_MESSAGE);
      } else {
        loop = false;
      }
    }

    if (TileController.isPlayed()) {
      showSave(false);
    }

    long time = 0;

    // Try to load the new file
    try {
      FileReader fileReader = new FileReader(newFile);
      HashMap<Integer, Lines> data = fileReader.convertBytesToFloats(tileDims);
      int numTiles = data.size();
      int[] order = fileReader.getOrder();
      boolean played = fileReader.getPlayed();

      ((TileMover) getParent()).removeTiles();

      if (played) {
        time = fileReader.getTimePassed();
        TileController.loadOldTiles(numTiles, tileDims, data, order, time);
      } else {
        TileController.createNewTiles(numTiles, tileDims, data, order);
      }

      fileReader.close();
      revalidate();
      getParent().revalidate();

    } catch (IOException e) {
      // Display an error to the user
      JOptionPane.showMessageDialog(null, "Could not read the file.", "Error",
          JOptionPane.ERROR_MESSAGE);
    }

    // Validate the window again so the tiles display correctly
    revalidate();
  }

  /**
   * Displays the pop-up menu under the file button.
   * 
   * @param event
   *          Event on button press
   */
  private void showPopup(ActionEvent event) {
    // Get the event source
    Component pressed = (Component) event.getSource();

    // Get the location of the click
    Point location = pressed.getLocationOnScreen();

    // 0,0 is the coordinate where the pop-up is shown
    menu.show(this, 0, 0);

    // Now set the location of the menu relative to the screen
    menu.setLocation(location.x, location.y + pressed.getHeight());
  }

  /**
   * Displays the pop-up window for the user to save a file.
   * 
   * @param saveButton
   *          True if the function calling save was the save button, false
   *          otherwise
   */
  public void showSave(boolean saveButton) {
    boolean loop = true;

    // Stop the timer
    Clock.stop();

    if (TileController.isPlayed() == true && saveButton == false) {
      int confirm = JOptionPane.showConfirmDialog(null,
          "Maze has been modified. Do you want to save?", "Save", JOptionPane.ERROR_MESSAGE);
      // If the user wants to overwrite the file, then pass the file to save it, if
      // not then prompt the user for a file.
      if (confirm == JOptionPane.YES_OPTION) {
        loop = true;
      } else {
        loop = false;
      }
    }

    if (loop == true) {

      File newFile = null;

      // Check to see if there are tiles loaded in the game, if not then nothing to
      // save
      if (TileController.getShuffledTiles() != null) {

        // Loop until the user enters a valid file or cancels.
        while (loop) {
          String input = JOptionPane.showInputDialog(null, "Location to save file", "Save",
              JOptionPane.QUESTION_MESSAGE);

          // If the user pressed cancel then close the window.
          if (input == null) {
            Clock.start();
            return;
          }

          int begin = input.lastIndexOf(".");

          // Checks if there is any extension, if not then add the .mze extension
          if (begin == -1) {
            input += ".mze";
          }

          newFile = new File(input);

          try {
            // Attempt to create a file, if it exists then prompt the user
            if (!newFile.createNewFile()) {
              int confirm = JOptionPane.showConfirmDialog(null,
                  "File already exists. Overwrite file?", "Save", JOptionPane.ERROR_MESSAGE);
              // If the user wants to overwrite the file, then pass the file to save it, if
              // not then prompt the user for a file.
              if (confirm == JOptionPane.YES_OPTION) {
                Clock.start();
                loop = false;
              }
            } else {
              loop = false;
            }

            // Error occurred so display message to user.
          } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Not a valid file.", "Error",
                JOptionPane.ERROR_MESSAGE);
          }
        }

        // Try to write the new file
        try {
          FileWriter outFile = new FileWriter(newFile);
          outFile.save();
          outFile.close();
        } catch (IOException e) {
          JOptionPane.showMessageDialog(null, "Could not save the file.", "Error",
              JOptionPane.ERROR_MESSAGE);
        }
      } else {
        // If there were no tiles, then prompt a message saying that
        JOptionPane.showMessageDialog(null, "No valid tiles to save.", "Error",
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }
}

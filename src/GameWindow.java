import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;

/**
 * Responsible for initializing and setting up main window game.
 * 
 * @author Kim Buckner
 * @author Batyr Nuryyev
 * @author James Fantin
 * @author Chandler Haukap
 * @author Spencer Hedeen
 */
public class GameWindow extends JFrame {

  /**
   * Serial id to make the compiler happy.
   */
  public static final long serialVersionUID = 1;

  /**
   * Set the default Tile dimension to be 100 x 100 pixels. Could be potentially
   * chosen by user if he/she prefers other Tile size.
   */
  private int tileDims = 100;

  /**
   * Constructor sets the window name using super(), changes the layout, which you
   * really need to read up on, and maybe you can see why I chose this one.
   *
   * @param string
   *          This sets the window name/title.
   */
  public GameWindow(String string) {
    // Set window name
    super(string);
  }

  /**
   * Initializes window, sets the dimension and background for it, and makes it
   * display on a screen. Then, adds tiles and buttons to window.
   */
  public void initializeWindow() {
    // have to override the default layout to reposition things
    this.setSize(new Dimension(900, 1000));
    setMinimumSize(new Dimension(900, 1000));
    setResizable(false);

    // Exits when you click X button
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Sets background color to be blue-ish.
    getContentPane().setBackground(new Color(0, 0, 200));

    // Makes window visible
    setVisible(true);
  }

  /**
   * Establishes the initial board.
   */
  public void setUp() {

    // Initialize tileMover and add to the frame
    TileMover tileMover = new TileMover();
    this.add(tileMover);

    // Set up the constrains for the components.
    GridBagConstraints gbConstraints = new GridBagConstraints();
    gbConstraints.gridx = 0;
    gbConstraints.gridy = 0;
    gbConstraints.gridwidth = 1;
    gbConstraints.gridheight = 1;
    gbConstraints.weightx = 1;
    gbConstraints.weighty = 1;

    // Initialize and add the game grid
    gbConstraints.anchor = GridBagConstraints.CENTER;
    GameGrid grid = new GameGrid(4, tileDims);

    tileMover.add(grid, gbConstraints, JLayeredPane.DEFAULT_LAYER);

    // Number of tiles
    int numTiles = 0;

    long time = 0;
    boolean played = false;
    HashMap<Integer, Lines> data = null;
    int[] order = null;

    Button buttons = new Button(tileDims);

    // Add the tile areas
    try {
      File newGame = new File("default.mze");
      FileReader fileReader = new FileReader(newGame);
      data = fileReader.convertBytesToFloats(tileDims);
      numTiles = data.size();
      played = fileReader.getPlayed();
      order = fileReader.getOrder();
      time = fileReader.getTimePassed();
      fileReader.close();
    } catch (IOException exception) {
      // If file is not found or corrupt, prompt user for a file
      buttons.showLoad();
    }

    // Add the game clock
    Clock clock = new Clock();
    gbConstraints.anchor = GridBagConstraints.PAGE_START;
    tileMover.add(clock, gbConstraints, JLayeredPane.DEFAULT_LAYER);

    // Add the buttons to the panel and place them accordingly
    gbConstraints.insets = new Insets(26, 0, 0, 0);
    gbConstraints.anchor = GridBagConstraints.PAGE_START;
    tileMover.add(buttons, gbConstraints, JLayeredPane.DEFAULT_LAYER);

    TileController tiles = new TileController(16, tileDims, clock);

    if (played) {
      TileController.loadOldTiles(numTiles, tileDims, data, order, time);
    } else {
      TileController.createNewTiles(numTiles, tileDims, data, order);
    }

    gbConstraints.anchor = GridBagConstraints.LINE_START;
    tileMover.add(tiles.getLeftTiles(), gbConstraints, JLayeredPane.DEFAULT_LAYER);

    gbConstraints.anchor = GridBagConstraints.LINE_END;
    tileMover.add(tiles.getRightTiles(), gbConstraints, JLayeredPane.DEFAULT_LAYER);

    tileMover.revalidate();

    // Refresh the window so the tiles show up
    revalidate();
  }
}

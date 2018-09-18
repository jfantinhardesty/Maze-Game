import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * This object is responsible for creating left and right JPanels that hold cell
 * objects.
 * 
 * @author Spencer Hedeen
 * @author Chandler Haukap
 * @author James Fantin
 * @author Batyr Nuryyev
 */
public class TileController {

  /**
   * Cells on the sides.
   */
  private static Cell[] cells;

  /**
   * The clock.
   */
  private static Clock clock;

  /**
   * The panels to be displayed on the left of the screen.
   */
  private final JPanel leftTiles;

  /**
   * The panels to be displayed on the right of the screen.
   */
  private final JPanel rightTiles;

  /**
   * Contains all of the tiles in a shuffled order.
   */
  private static ArrayList<Tile> shuffledTiles;

  /**
   * Contains all of the tiles in the order that they are read in from the file.
   * This corresponds with a winning tile order.
   */
  private static ArrayList<Tile> orderedTiles;

  /**
   * Clears the tile array.
   */
  public static void clearTiles() {
    orderedTiles = null;
    shuffledTiles = null;
  }

  /**
   * Adds tiles for a new game that has not been played.
   * 
   * @param n
   *          the number of tiles
   * @param tileSize
   *          the size of the tiles in pixels
   * @param data
   *          the hash map of data containing all of the lines and rotations
   * @param order
   *          array that has the order the titles were read in
   */
  public static void createNewTiles(int n, int tileSize, HashMap<Integer, Lines> data,
      int[] order) {
    // Storage for points
    List<Float> points;

    // Get random rotations
    List<Integer> randomRotations = new ArrayList<>(n);
    for (int i = 0; i < n; i++) {
      randomRotations.add(i % 4);
    }

    // "Shuffle" the hash map (if played)
    Collections.shuffle(randomRotations);

    // Initializes all of the tiles
    orderedTiles = new ArrayList<>();
    shuffledTiles = new ArrayList<>();

    // Create tiles
    for (int i = 0; i < order.length; ++i) {
      // Get the list of points for the tile and pass it to the tile
      points = data.get(order[i]).getLines();

      Tile tile = new Tile(tileSize, points, randomRotations.get(i));

      // Add tile to the array of tiles
      orderedTiles.add(tile);
    }

    // Copy the values to the shuffled array
    for (int j = 0; j < orderedTiles.size(); j++) {
      shuffledTiles.add(orderedTiles.get(j));
    }

    // Shuffle the order of the tiles so they are placed randomly
    Collections.shuffle(shuffledTiles);

    // Place all of the tiles in the cells
    for (int j = 0; j < cells.length; j++) {
      Tile tile = shuffledTiles.get(j);
      Cell cell = cells[j];

      tile.setCurrentCell(cell);
      tile.setHomeCell(cell);

      cell.add(tile);
      cell.removeBorder();
    }
    clock.setTime(0);
    clock.repaint();
  }

  /**
   * Gets the array list of ordered tiles.
   * 
   * @return an array list of the tiles in the order read in
   */
  public static ArrayList<Tile> getOrderedTiles() {
    return orderedTiles;
  }

  /**
   * Gets the array list of shuffled tiles.
   * 
   * @return an array list of the tiles in shuffled order
   */
  public static ArrayList<Tile> getShuffledTiles() {
    return shuffledTiles;
  }

  /**
   * Checks to see if a game has been played or not.
   * 
   * @return returns true if the game has been played, false otherwise
   */
  public static boolean isPlayed() {
    if (shuffledTiles == null) {
      return false;
    }

    for (Tile thisTile : shuffledTiles) {
      if (thisTile.getCurrentCell() != thisTile.getHomeCell()
          || thisTile.getRotations() % 4 != thisTile.getHomeRotation()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Adds tiles for a game that has been played.
   * 
   * @param n
   *          the number of tiles
   * @param tileSize
   *          the size of the tiles in pixels
   * @param data
   *          the hash map of data containing all of the lines and rotations
   * @param order
   *          array that has the order the titles were read in
   * @param time
   *          the played time to display
   */
  public static void loadOldTiles(int n, int tileSize, HashMap<Integer, Lines> data, int[] order,
      long time) {
    // Storage for points
    List<Float> points;

    Cell[] gridCells = GameGrid.getCells();

    // Set the time
    Clock.stop();

    // Initializes all of the tiles
    orderedTiles = new ArrayList<>();
    shuffledTiles = new ArrayList<>();

    // Init cell
    Cell cell;

    for (int i = 0; i < order.length; ++i) {
      // Get the list of points for the tile and pass it to the tile
      points = data.get(order[i]).getLines();

      // Init cell
      if (order[i] >= 16) {
        cell = gridCells[order[i] - 16];
      } else {
        cell = cells[order[i]];
      }

      // Initialize tile
      Tile tile = new Tile(cell, tileSize, points, data.get(order[i]).getRotations());

      cell.add(tile);
      cell.removeBorder();

      // Add tile to the array of tiles
      orderedTiles.add(tile);
      shuffledTiles.add(tile);
    }
    clock.setTime(time);
    clock.repaint();
  }

  /**
   * Creates the left and right panels to hold tiles. defaults to 16 tiles if n is
   * negative or 0.
   * 
   * @param n
   *          specifies the number of tiles
   * 
   * @param tileSize
   *          size in pixels of the cells and tiles. minimum tile size is 60. max
   *          is 200.
   * @param clock
   *          The clock for the screen
   */
  public TileController(int n, int tileSize, Clock clock) {
    TileController.clock = clock;
    // CH- error checking.
    // since the default for GameGrid is a 4 * 4 grid, the default for
    // TileController
    // should be 4 * 4 = 16 tiles.

    // we can't have a negative number of tiles, and 0 would be a boring game
    if (n <= 0) {
      n = 16;
    }

    cells = new Cell[n];

    // CH- more error checking
    // Ensures that the tiles are a reasonable size for desktops.
    if (tileSize < 60) {
      tileSize = 60;
    } else if (tileSize > 200) {
      tileSize = 200;
    }

    // These two panels will hold the tiles
    leftTiles = new JPanel();
    rightTiles = new JPanel();

    leftTiles.setPreferredSize(
        new Dimension(tileSize + 6, tileSize * (n / 2) + (5 * (n / 2)) + 10 + tileSize));
    rightTiles.setPreferredSize(
        new Dimension(tileSize + 6, tileSize * (n / 2) + (5 * (n / 2)) + 10 + tileSize));

    // Makes the JPanel transparent
    leftTiles.setOpaque(false);
    rightTiles.setOpaque(false);

    // These will be the layouts for the tiles
    leftTiles.setLayout(new BoxLayout(leftTiles, BoxLayout.Y_AXIS));
    rightTiles.setLayout(new BoxLayout(rightTiles, BoxLayout.Y_AXIS));

    // Add a border to separate the Cells
    leftTiles.add(Box.createRigidArea(new Dimension(tileSize + 6, 5)));
    rightTiles.add(Box.createRigidArea(new Dimension(tileSize + 6, 5)));

    // Create cells
    for (int i = 0; i < n; ++i) {
      Cell cell = new Cell(tileSize);
      cell.setId(i);
      cells[i] = cell;

      if (i < (n / 2)) {
        leftTiles.add(cell);

        // Add a border to separate the Cells
        leftTiles.add(Box.createRigidArea(new Dimension(tileSize, 5)));
      } else {
        rightTiles.add(cell);

        // Add a border to separate the Cells
        rightTiles.add(Box.createRigidArea(new Dimension(tileSize, 5)));
      }
    }
  }

  /**
   * Gets the left titles JPanel.
   * 
   * @return the left JPanel
   */
  public JPanel getLeftTiles() {
    return leftTiles;
  }

  /**
   * Gets the right tiles JPanel.
   * 
   * @return the right JPanel
   */
  public JPanel getRightTiles() {
    return rightTiles;
  }
}

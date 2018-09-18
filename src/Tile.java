import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * Contains the maze tile value (now - number, later - lines) for a tile.
 * 
 * @author Chandler Haukap
 * @author Spencer Hedeen
 * @author Batyr Nuryyev
 */
public class Tile extends JLabel implements ActionListener {

  /**
   * Serial id to make the compiler happy.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Initial cell place.
   */
  private Cell homeCell;

  /**
   * Current cell place.
   */
  private Cell currentCell;

  /**
   * Current rotation of the cell.
   */
  private int rotations;

  /**
   * home rotation is the rotation that the tile began the game at.
   */
  private int homeRotation;

  /**
   * List of points for line drawing.
   */
  private List<Float> points;

  /**
   * Stores the original points without any rotations.
   */
  private List<Float> originalPoints;

  /**
   * size in pixels of the tile. (tile is a square)
   */
  private int size;

  /**
   * Timer that waits to display a warning.
   */
  private Timer timer;

  /**
   * Creates a tile object that extends JLabel. This will hold the actual maze
   * tile to be dragged in the game. This constructor is used for creating a Tile
   * for a played Game.
   * 
   * @param homeCell
   *          The Home location for the tile
   * @param tileSize
   *          The number of pixels in the tile
   * @param points
   *          The list of the points for the lines in the tile
   * @param rotations
   *          The number of times to rotate the tile
   */
  public Tile(Cell homeCell, int tileSize, List<Float> points, int rotations) {
    this.homeCell = homeCell;
    currentCell = homeCell;
    this.points = points;

    // Store the original points without rotations
    originalPoints = new ArrayList<>();
    for (int i = 0; i < points.size(); i++) {
      originalPoints.add(points.get(i));
    }
    size = tileSize;
    homeRotation = rotations;

    // Set dimension to be 80 x 80 (pixels)
    setPreferredSize(new Dimension(size, size));

    // Set purple-ish background color
    setBackground(new Color(177, 59, 253));

    setOpaque(true);

    // Create the timer that displays a warning
    timer = new Timer(500, this);

    // Must set repeats to false so the delay is the same everytime
    timer.setRepeats(false);

    for (int i = 0; i < rotations; ++i) {
      rotate();
    }
  }

  /**
   * Creates a tile object that extends JLabel. This will hold the actual maze
   * tile to be dragged in the game. This constructor is used for creating a Tile
   * for a new Game.
   * 
   * @param tileSize
   *          The number of pixels in the tile
   * @param points
   *          The list of the points for the lines in the tile
   * @param rotations
   *          The number of times to rotate the tile
   */
  public Tile(int tileSize, List<Float> points, int rotations) {
    this.points = points;

    // Store the original points without rotations
    originalPoints = new ArrayList<>();
    for (int i = 0; i < points.size(); i++) {
      originalPoints.add(points.get(i));
    }
    size = tileSize;
    homeRotation = rotations;

    // Set dimension to be 80 x 80 (pixels)
    setPreferredSize(new Dimension(size, size));

    // Set purple-ish background color
    setBackground(new Color(177, 59, 253));

    setOpaque(true);

    // Create the timer that displays a warning
    timer = new Timer(500, this);

    // Must set repeats to false so the delay is the same everytime
    timer.setRepeats(false);

    for (int i = 0; i < rotations; ++i) {
      rotate();
    }
  }

  /**
   * Overload action listener for the timer object. This changes the tile back to
   * the default color.
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    setBackground(new Color(177, 59, 253));
  }

  /**
   * Changes background color of tile for half a second. Used to be a warning when
   * the user drags a tile on top of another tile.
   */
  public void displayWarning() {
    setBackground(Color.red);
    timer.start();
  }

  /**
   * Draws a single line.
   * 
   * @param g2
   *          for rendering 2-dimensional shapes
   * @param x1
   *          starting coordinate
   * @param y1
   *          starting coordinate
   * @param x2
   *          ending coordinate
   * @param y2
   *          ending coordinate
   */
  private void drawLine(Graphics2D g2, int x1, int y1, int x2, int y2) {
    g2.setStroke(new BasicStroke(3));
    g2.drawLine(x1, y1, x2, y2);
  }

  /**
   * Returns current cell.
   * 
   * @return the current Cell
   */
  public Cell getCurrentCell() {
    return currentCell;
  }

  /**
   * Returns home cell.
   * 
   * @return homeCell
   */
  public Cell getHomeCell() {
    return homeCell;
  }

  /**
   * returns the rotation that the tile began the game at.
   * 
   * @return the number of rotations at the start of the game
   */
  public int getHomeRotation() {
    return homeRotation;
  }

  /**
   * Returns the original points with no rotations.
   * 
   * @return List of points with no rotations
   */
  public List<Float> getOriginalPoints() {
    return originalPoints;
  }

  /**
   * Returns list of points.
   * 
   * @return List of points
   */
  public List<Float> getPoints() {
    return points;
  }

  /**
   * Returns the rotations of the tile.
   * 
   * @return the number of current rotations
   */
  public int getRotations() {
    return rotations;
  }

  /**
   * Draws lines on the Tile based on points.
   * 
   * @param g
   *          Graphics
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setColor(Color.GREEN);

    // Loop through each point and draw it
    for (int i = 0; i < points.size(); i += 4) {
      int x1 = points.get(i).intValue();
      int y1 = points.get(i + 1).intValue();
      int x2 = points.get(i + 2).intValue();
      int y2 = points.get(i + 3).intValue();
      drawLine((Graphics2D) g, x1, y1, x2, y2);
    }
  }

  /**
   * Removes the border.
   */
  public void removeBackground() {
    setBackground(new Color(177, 59, 253));
  }

  /**
   * rotates the lines on the tile by 90 degrees using matrix algebra. The points
   * are first rotated 90 degrees: x' = y*sin(90) = -y y' = -x*sin(90) = x Then
   * translated back into the first quadrant: x = x + size
   */
  public void rotate() {
    rotations++;
    float temp = 0; // temporarily stores the x value since we use is second.
    // for every point
    for (int i = 0; i < points.size(); i++) {
      // for the x values
      if (i % 2 == 0) {
        temp = points.get(i); // store is so we can assign the x
        points.add(i, (-1) * points.get(i + 1) + size); // x = -y + size
      } else {
        points.add(i, temp); // y = x
      }
      points.remove(i + 1); // since we are using a list we have to remove the old value.
    }
  }

  /**
   * Add the border to a cell when a Tile is removed.
   */
  public void setBackground() {
    setBackground(Color.darkGray);
  }

  /**
   * Sets current cell.
   * 
   * @param currentCell
   *          New Cell
   */
  public void setCurrentCell(Cell currentCell) {
    this.currentCell = currentCell;
  }

  /**
   * Sets the home cell.
   * 
   * @param homeCell
   *          New Home Cell
   */
  public void setHomeCell(Cell homeCell) {
    this.homeCell = homeCell;
  }
}

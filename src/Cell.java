import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * Cell class for holding the Tile class.
 * 
 * @author Chandler Haukap
 * @author James Fantin
 * @author Spencer Hedeen
 *
 */
public class Cell extends JPanel {
  /**
   * Serial id to make the compiler happy.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The location of the cell on the gameboard (0 - 31).
   */
  private int id;

  /**
   * Creates a cell object corresponding to a color. If the number is even it is
   * white else it is light blue. This sets the size of the cell object as well.
   * 
   * @param cellSize
   *          size in pixels of the cell
   */
  public Cell(int cellSize) {

    // Set the layout configuration
    super(new BorderLayout());

    // Set white background color
    setBackground(Color.WHITE);

    // Fix the size to be 80 x 80 (pixels)
    setPreferredSize(new Dimension(cellSize, cellSize));
    setMaximumSize(new Dimension(cellSize, cellSize));
    setMinimumSize(new Dimension(cellSize, cellSize));
    // Set the new panel to be transparent
    setOpaque(true);
  }

  /**
   * Add the border to a cell when a Tile is removed.
   */
  public void createBorder() {
    setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.blue));
  }

  /**
   * Gets the cell ID.
   * 
   * @return the ID of the cell
   */
  public int getId() {
    return id;
  }

  /**
   * Removes the border.
   */
  public void removeBorder() {
    setBorder(null);
  }

  /**
   * Sets the ID for the cell.
   * 
   * @param input
   *          the new ID
   */
  public void setId(int input) {
    id = input;
  }
}

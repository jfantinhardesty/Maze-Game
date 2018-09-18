import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Creates the buttons that represent the game space. places the buttons in one
 * container then adds them to frame. Default grid size is 4 x 4.
 * 
 * @author Chandler Haukap
 * @author James Fantin
 * @author Spencer Hedeen
 * @author Batyr Nuryyev
 */
public class GameGrid extends JPanel {

  /**
   * Serial id to make the compiler happy.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The array of the cells on the grid.
   */
  private static Cell[] cells;

  /**
   * Gets the array of cells.
   * 
   * @return an array of cells in the Game Grid
   */
  public static Cell[] getCells() {
    return cells;
  }

  /**
   * Automatically solves the maze. For testing purposes only.
   */
  public static void solve() {
    ArrayList<Tile> tiles = TileController.getOrderedTiles();

    for (int i = 0; i < tiles.size(); i++) {
      cells[i].add(tiles.get(i));
      cells[i].removeBorder();
      tiles.get(i).getCurrentCell().createBorder();
      while ((tiles.get(i).getRotations() % 4) != 0) {
        tiles.get(i).rotate();
      }
      tiles.get(i).getCurrentCell().revalidate();
      tiles.get(i).setCurrentCell(cells[i]);
      cells[i].revalidate();
      tiles.get(i).revalidate();
    }
  }

  /**
   * Tests for a solved maze. Displays a winning message if solved.
   */
  public static void victory() {
    ArrayList<Tile> tiles = TileController.getOrderedTiles();
    int i = 0;
    for (Cell thisCell : cells) {
      // If the tile is not in the correct cell or not rotated back to its original
      // position then we can return since it is not solved
      if (tiles.get(i).getCurrentCell() != thisCell || (tiles.get(i).getRotations() % 4) != 0) {
        return;
      }
      ++i;
    }

    // if all tiles are in the correct spot, then display a winning message
    Clock.stop();
    JOptionPane.showMessageDialog(null, "You won in a time of " + Clock.getTime());
  }

  /**
   * Creates the left and right panels to hold tiles.
   * 
   * @param dimSize
   *          Dimension of the grid
   * @param cellSize
   *          size in pixels of the cell
   */
  public GameGrid(int dimSize, int cellSize) {
    super();
    super.setBackground(Color.BLACK);

    // Checks for invalid dimensions and defaults to a 4 x 4 grid.
    if (dimSize <= 0) {
      dimSize = 4;
    }

    GridLayout lay = new GridLayout(dimSize, dimSize);
    setLayout(lay);

    cells = new Cell[dimSize * dimSize];
    for (int i = 0; i < (dimSize * dimSize); i++) {
      Cell cell = new Cell(cellSize);
      cell.setId(i + 16);
      cell.createBorder();
      cells[i] = cell;

      // Add button to the panel
      add(cell);
    }

    // Needs to update the frame to see the buttons.
    revalidate();
  }
}

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JLayeredPane;

/**
 * The TileMover is responsible for all of the mouse listeners to drag and drop
 * tiles. It must correctly place tiles into cells and handle when they are not
 * dropped on a valid cell. Adds borders to empty cells and highlights the cell
 * currently under consideration.
 * 
 * @author James Fantin
 * @author Chandler Haukap
 */
public final class TileMover extends JLayeredPane implements MouseListener {

  /**
   * Serial id to make the compiler happy.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Stores the tile that is currently being moved.
   */
  private static Tile tile;

  /**
   * Moves all of the tiles back to their original starting cell.
   */
  public static void moveTilesHome() {
    Cell currentCell;
    Cell homeCell;

    List<Tile> tiles = TileController.getShuffledTiles();

    // If there are tiles, then we move them homeF
    if (tiles != null) {

      for (Tile tile : tiles) {
        // CH- rotates the tiles back to their original position
        while (tile.getRotations() % 4 != tile.getHomeRotation()) {
          tile.setVisible(false);
          tile.rotate();
          tile.setVisible(true);
        }
        currentCell = tile.getCurrentCell();
        homeCell = tile.getHomeCell();

        // If we are not already in the home cell, we need to move the cell home
        if (currentCell != homeCell) {
          // Must change visibility to move
          tile.setVisible(false);

          // Remove the border before we add the cell
          homeCell.removeBorder();
          homeCell.add(tile);
          tile.setCurrentCell(homeCell);

          // Add border on the old cell
          currentCell.createBorder();

          tile.setVisible(true);
        }
      }
    }
  }

  /**
   * Tile mover uses a JLayeredPane to hold every object on the screen. We use
   * this because we can utilize the drag layer of the JLayeredPane to be
   * responsible for all dragging and dropping events.
   * 
   * <p>This class is responsible for all mouse listeners for dragging and
   * dropping Tiles into the correct JPanel containers.
   */
  public TileMover() {
    super();

    setMinimumSize(new Dimension(780, 780));
    setPreferredSize(new Dimension(780, 780));

    // Add the mouse listeners
    addMouseListener(this);

    setOpaque(false);

    // Use GridBagLayout for all objects
    setLayout(new GridBagLayout());
    tile = null;
  }

  /**
   * Does nothing. Must override for mouse listener.
   * 
   * @param event
   *          Mouse event
   */
  @Override
  public void mouseClicked(MouseEvent event) {
  }

  /**
   * Does nothing. Must override for mouse listener.
   * 
   * @param event
   *          Mouse event
   */
  @Override
  public void mouseEntered(MouseEvent event) {
  }

  /**
   * Does nothing. Must override for mouse listener.
   * 
   * @param event
   *          Mouse event
   */
  @Override
  public void mouseExited(MouseEvent event) {
  }

  /**
   * Grabs a tile object when the mouse is pressed on a tile. Places the tile into
   * a container if when released. If it is not a valid container, then move it
   * back to where it came from.
   * 
   * @param event
   *          Mouse event
   */
  @Override
  public void mousePressed(MouseEvent event) {
    // Set the tile to null so that mouseRelease works properly.
    // Otherwise we could click Cells and tiles would just move there.

    // Right click rotate the tile
    if (event.getButton() == MouseEvent.BUTTON3) {
      Component c = findComponentAt(event.getX(), event.getY());

      if (c instanceof Tile) {
        Tile temp = (Tile) c;
        temp.rotate();
        temp.repaint();
        Clock.start();
        GameGrid.victory();
      }
    } else if (event.getButton() == MouseEvent.BUTTON1) {
      // Check that the left mouse button was clicked to drag the tile.
      if (tile == null) {
        // Finds the object at the lowest level of a container
        Component c = findComponentAt(event.getX(), event.getY());

        // We only want to move the tile objects
        if (c instanceof Tile) {
          // If the parent isn't a cell then we need to return or else we will have an
          // error
          if (!(c.getParent() instanceof Cell)) {
            return;
          }
          tile = (Tile) c;
          tile.setBackground();
        }
      } else {
        Cell currentCell = tile.getCurrentCell();

        // Finds the container at the lowest level to add the tile to.
        Component c = findComponentAt(event.getX(), event.getY());

        if (c.equals(tile)) {
          tile.removeBackground();
          tile = null;
        } else if (c instanceof Cell) { // If the container is a cell, then we can move it there.
          Cell parent = (Cell) c;
          parent.add(tile);
          parent.setBorder(null);
          tile.setCurrentCell(parent);

          // CH- if the tile is released on the same cell that it was taken from:
          if (c.equals(currentCell)) {
            // The cell does not need a border
            currentCell.removeBorder();
          } else {
            // if the old cell is now vacant

            // make a blue border that is 1 pixel wide and blue.
            currentCell.createBorder();
            tile.removeBackground();
            tile = null;
            Clock.start();
            GameGrid.victory();
          }
        } else if (c instanceof Tile) {
          ((Tile) c).displayWarning();
          // If it is not a cell, we move it to where it originally came from.
          currentCell.add(tile);
          currentCell.setBorder(null);
        } else {
          tile.removeBackground();
          tile = null;
        }
      }
    }
  }

  /**
   * Does nothing. Must override for mouse listener.
   * 
   * @param event
   *          Mouse event
   */
  @Override
  public void mouseReleased(MouseEvent event) {
  }

  /**
   * Removes all of the tiles from the screen. Also resets the array of tiles in
   * tile controller so all instances of tile are deleted.
   */
  public void removeTiles() {
    List<Tile> tiles = TileController.getShuffledTiles();
    if (tiles != null) {
      for (int i = 0; i < tiles.size(); i++) {
        tiles.get(i).setVisible(false);
        this.remove(tiles.get(i));
      }
      TileController.clearTiles();
    }

    Cell[] cells = GameGrid.getCells();
    for (int i = 0; i < cells.length; ++i) {
      cells[i].createBorder();
    }
  }
}

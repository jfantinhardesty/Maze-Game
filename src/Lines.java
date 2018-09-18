import java.util.ArrayList;
import java.util.List;

/**
 * Class that acts as a tuple of the number of rotations and a list of lines.
 * 
 * @author Batyr Nuryyev
 *
 */
public class Lines {
  /**
   * Number of rotations for the lines.
   */
  private int rotations;

  /**
   * The lines for a tile.
   */
  private List<Float> lines = new ArrayList<>();

  /**
   * Sets the rotations and list of floats.
   * 
   * @param rotations
   *          The rotations for the line
   * @param points
   *          The points for the line
   */
  public Lines(int rotations, List<Float> points) {
    this.rotations = rotations;

    for (Float point : points) {
      lines.add(point);
    }
  }

  /**
   * Gets the list of lines.
   * 
   * @return the list of points for a line
   */
  public List<Float> getLines() {
    return lines;
  }

  /**
   * Gets the rotations.
   * 
   * @return the rotations of the tile
   */
  public int getRotations() {
    return rotations;
  }

  /**
   * Sets the list of lines.
   * 
   * @param lines
   *          List containing the points for a line
   */
  public void setLines(List<Float> lines) {
    this.lines = lines;
  }

  /**
   * Sets the rotations.
   * 
   * @param rotations
   *          the number of rotations for a tile
   */
  public void setRotations(int rotations) {
    this.rotations = rotations;
  }
}

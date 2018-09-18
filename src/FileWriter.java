import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Writes current maze state to a file using the specified format.
 * 
 * @author Chandler Haukap
 */
public class FileWriter extends FileOutputStream {

  /**
   * Constructor for FileWriter.
   * 
   * @param file
   *          The File object to write to.
   * @throws IOException
   *           IOException if the file can't be created
   */
  public FileWriter(File file) throws IOException {
    super(file);
  }

  /**
   * Saves the current game to a file.
   * 
   * @throws IOException
   *           IOException if can't write to the file
   */
  public void save() throws IOException {
    // Check if the file has been played

    boolean isPlayed = TileController.isPlayed();

    // First two bytes are the same for all files
    int first = 202;
    int second = 254;
    byte i = (byte) first;
    byte j = (byte) second;
    this.write(i);
    this.write(j);

    if (isPlayed) {
      // Bytes for a played file
      int third = 222;
      int fourth = 237;
      i = (byte) third;
      j = (byte) fourth;
      this.write(i);
      this.write(j);
    } else {
      // Bytes for a new game
      int third = 190;
      int fourth = 239;
      i = (byte) third;
      j = (byte) fourth;
      this.write(i);
      this.write(j);
    }

    // Defaults use 16 tiles
    int tileNum = 16;
    byte[] b = new byte[4];
    b[0] = (byte) tileNum;
    b[1] = (byte) (tileNum >>> 8);
    b[2] = (byte) (tileNum >>> 16);
    b[3] = (byte) (tileNum >>> 24);
    write(b[3]);
    write(b[2]);
    write(b[1]);
    write(b[0]);

    // save time
    long time = Clock.getTimeLong();
    byte[] a = new byte[8];
    a[0] = (byte) time;
    a[1] = (byte) (time >>> 8);
    a[2] = (byte) (time >>> 16);
    a[3] = (byte) (time >>> 24);
    a[4] = (byte) (time >>> 32);
    a[5] = (byte) (time >>> 40);
    a[6] = (byte) (time >>> 48);
    a[7] = (byte) (time >>> 56);
    write(a[7]);
    write(a[6]);
    write(a[5]);
    write(a[4]);
    write(a[3]);
    write(a[2]);
    write(a[1]);
    write(a[0]);

    int id;
    int currentRotation;
    int nullSpace = 0;
    List<Tile> tiles = TileController.getOrderedTiles();

    // Iterate over every tile and save its information
    for (Tile thisTile : tiles) {

      // Get the ID or the location of the cell on the gameboard the tile is in
      id = thisTile.getCurrentCell().getId();
      b[0] = (byte) id;
      b[1] = (byte) (id >>> 8);
      b[2] = (byte) (id >>> 16);
      b[3] = (byte) (id >>> 24);
      write(b[3]);
      write(b[2]);
      write(b[1]);
      write(b[0]);

      if (isPlayed) {
        // Get the current rotation of the played tiles
        currentRotation = thisTile.getRotations() % 4;
        b[0] = (byte) currentRotation;
        b[1] = (byte) (currentRotation >>> 8);
        b[2] = (byte) (currentRotation >>> 16);
        b[3] = (byte) (currentRotation >>> 24);
        write(b[3]);
        write(b[2]);
        write(b[1]);
        write(b[0]);
      } else {
        // If it is a new game we can just use 0 for the tile rotations
        b[0] = (byte) nullSpace;
        b[1] = (byte) (nullSpace >>> 8);
        b[2] = (byte) (nullSpace >>> 16);
        b[3] = (byte) (nullSpace >>> 24);
        write(b[3]);
        write(b[2]);
        write(b[1]);
        write(b[0]);
      }

      // Get the points for the current tile with no rotations
      List<Float> points = thisTile.getOriginalPoints();
      int size = points.size() / 4;
      b[0] = (byte) size;
      b[1] = (byte) (size >>> 8);
      b[2] = (byte) (size >>> 16);
      b[3] = (byte) (size >>> 24);
      write(b[3]);
      write(b[2]);
      write(b[1]);
      write(b[0]);

      // Iterate over all of the lines for the tile
      for (int k = 0; k < points.size(); k += 4) {

        // Get the first point
        int val = Float.floatToIntBits(points.get(k));
        b[0] = (byte) val;
        b[1] = (byte) (val >>> 8);
        b[2] = (byte) (val >>> 16);
        b[3] = (byte) (val >>> 24);
        write(b[3]);
        write(b[2]);
        write(b[1]);
        write(b[0]);

        // Get the second point
        val = Float.floatToIntBits(points.get(k + 1));
        b[0] = (byte) val;
        b[1] = (byte) (val >>> 8);
        b[2] = (byte) (val >>> 16);
        b[3] = (byte) (val >>> 24);
        write(b[3]);
        write(b[2]);
        write(b[1]);
        write(b[0]);

        // Get the third point
        val = Float.floatToIntBits(points.get(k + 2));
        b[0] = (byte) val;
        b[1] = (byte) (val >>> 8);
        b[2] = (byte) (val >>> 16);
        b[3] = (byte) (val >>> 24);
        write(b[3]);
        write(b[2]);
        write(b[1]);
        write(b[0]);

        // Get the fourth point
        val = Float.floatToIntBits(points.get(k + 3));
        b[0] = (byte) val;
        b[1] = (byte) (val >>> 8);
        b[2] = (byte) (val >>> 16);
        b[3] = (byte) (val >>> 24);
        write(b[3]);
        write(b[2]);
        write(b[1]);
        write(b[0]);
      }
    }
  }

}

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Responsible for reading in the data from new files.
 * 
 * @author Batyr Nuryyev
 *
 */
public class FileReader extends FileInputStream {

  /**
   * The file to be read from.
   */
  private File file;

  /**
   * Flag for whether it is new or played game.
   */
  private boolean ifPlayed;

  /**
   * Stores the order in which the tiles were read.
   */
  private int[] order;

  /**
   * Stores all bytes read from file.
   */
  private byte[] allMazeBytes;

  /**
   * Stores integer or float in form of 4 bytes.
   */
  private byte[] nextTempFourBytes = new byte[4];

  /**
   * Stores integer or float in form of 8 bytes.
   */
  private byte[] nextTempEightBytes = new byte[8];

  /**
   * Store the time passed from loaded game.
   */
  private long timePassed;

  /**
   * Gets the file and reads all bytes from it.
   * 
   * @param file
   *          File that the game will be loaded from
   * @throws FileNotFoundException
   *           Generic file not found exception
   * @throws IOException
   *           Generic IO exception
   */
  public FileReader(File file) throws FileNotFoundException, IOException {
    super(file);
    this.file = file;

    readAllByte();
  }

  /**
   * Checks whether the file is played, new, or corrupt. Then, passes the function
   * call accordingly (based on flag).
   * 
   * @param tileDims
   *          The number of tiles in the game
   * @return map from integers to Lines class
   * @throws IOException
   *           Generic IO exception
   */
  public HashMap<Integer, Lines> convertBytesToFloats(int tileDims) throws IOException {
    // Get the 1st, 2nd, 3rd and 4th bytes
    byte firstByte = allMazeBytes[0];
    byte secondByte = allMazeBytes[1];
    byte thirdByte = allMazeBytes[2];
    byte fourthByte = allMazeBytes[3];

    // Chop the array up to 4 bytes in front
    allMazeBytes = Arrays.copyOfRange(allMazeBytes, 4, allMazeBytes.length);

    // Check if the game has been played or not
    if (firstByte != (byte) 0xca || secondByte != (byte) 0xfe) {
      throw new IOException("Error: Corrupt File");
      // Corrupt file
    } else if (thirdByte == (byte) 0xbe && fourthByte == (byte) 0xef) {
      // New game
      ifPlayed = false;
    } else if (thirdByte == (byte) 0xde && fourthByte == (byte) 0xed) {
      // Played game
      ifPlayed = true;
    } else {
      // Corrupt file
      throw new IOException("Error: Corrupt File");
    }

    return this.convertBytesToFloats(tileDims, ifPlayed);
  }

  /**
   * Converts all file bytes to indexed floats. Indexed by tile id. For the game
   * that has been played. IDs are NOT sequential.
   * 
   * @param tileDims
   *          The number of tiles in the game
   * @param played
   *          If a game has been played or not
   * @return list of lists containing the indexed floats
   */
  private HashMap<Integer, Lines> convertBytesToFloats(int tileDims, boolean played) {
    // read first integer (number of tiles)
    setNextFour(0);
    int numTiles = convertToInt(nextTempFourBytes);

    // read the "time passed" from the game
    setNextEight(4);
    long time = convertToLong(nextTempEightBytes);
    timePassed = time;

    // initialize hash map, mapping from tileID to list of floats
    HashMap<Integer, Lines> listOfFloats = new HashMap<>();

    // variables to store info
    int tid;
    int trotations;
    int numLines;

    // container for floats
    List<Float> container = new ArrayList<>();

    // push the numbers in
    int position = 12;
    int bytePosition = 0;
    order = new int[numTiles];

    for (int i = 0; i < numTiles; ++i) {
      // read tile id
      setNextFour(position);
      tid = convertToInt(nextTempFourBytes);
      order[i] = tid;

      // read tile rotations
      position += 4;
      setNextFour(position);

      // set rotations if played. Otherwise, set to 0
      trotations = played ? convertToInt(nextTempFourBytes) : 0;

      // read number of lines of a tile
      position += 4;
      setNextFour(position);
      numLines = convertToInt(nextTempFourBytes);

      // set the bytePosition to be start of the byte 0 of tile i
      bytePosition = position + 4;
      for (int j = 0; j < numLines; ++j) {
        for (int k = 0; k < 4; ++k) {
          setNextFour(bytePosition + k * 4);
          container.add(Float.valueOf(convertToFloat(nextTempFourBytes)));
        }
        bytePosition += 16;
      }

      // insert the rotations and array of lines
      listOfFloats.put(tid, new Lines(trotations, container));
      container.clear();

      // If last tile, then do not move further.
      // Just stop here and increment the position.
      if ((i + 1) == numTiles) {
        position += 16 * numLines - 4;
      } else {
        position += 16 * numLines + 4;
      }
    }

    // lines float normalization
    Float x;
    for (Integer key : listOfFloats.keySet()) {
      for (int j = 0; j < listOfFloats.get(key).getLines().size(); ++j) {
        x = listOfFloats.get(key).getLines().get(j);
        x = x * ((float) tileDims / 100);
        listOfFloats.get(key).getLines().set(j, Float.valueOf(x));
      }
    }

    return listOfFloats;
  }

  /**
   * Converts a byte array into a float. Typically, 4-byte array is passed as an
   * argument.
   * 
   * @param array
   *          Byte array
   * @return float
   */
  private float convertToFloat(byte[] array) {
    ByteBuffer buffer = ByteBuffer.wrap(array);
    return buffer.getFloat();
  }

  /**
   * Converts byte array into an integer. Typically, 4-byte array is passed as an
   * argument.
   * 
   * @param array
   *          Byte array
   * @return integer converted from 4-byte array
   */
  private int convertToInt(byte[] array) {
    ByteBuffer buffer = ByteBuffer.wrap(array);
    return buffer.getInt();
  }

  /**
   * Converts a byte array into a long. Typically, 8-byte array is passed as an
   * argument.
   * 
   * @param array
   *          Byte array
   * @return float
   */
  private long convertToLong(byte[] array) {
    ByteBuffer buffer = ByteBuffer.wrap(array);
    return buffer.getLong();
  }

  /**
   * Returns the array that contains the order the tiles were read in.
   * 
   * @return an array containing the order the tiles were read in
   */
  public int[] getOrder() {
    return order;
  }

  /**
   * Returns if a game has been played or not.
   * 
   * @return true if the game is a played game, false otherwise
   */
  public boolean getPlayed() {
    return ifPlayed;
  }

  /**
   * Returns time passed.
   * 
   * @return time
   */
  public long getTimePassed() {
    return timePassed;
  }

  /**
   * Reads all bytes from file and stores inside byte array.
   * 
   * @throws IOException
   *           Generic IO exception
   */
  private void readAllByte() throws IOException {
    allMazeBytes = Files.readAllBytes(file.toPath());
  }

  /**
   * Store 8 bytes from position i from an array containing all file bytes into a
   * temporary 8-byte array.
   * 
   * @param i
   *          position for the next bytes
   */
  private void setNextEight(int i) {
    for (int j = 0; j < 8; ++j) {
      nextTempEightBytes[j] = allMazeBytes[i + j];
    }
  }

  /**
   * Copies 4 bytes from position i from an array containing all file bytes into a
   * temporary 4-byte array.
   * 
   * @param i
   *          position for the next bytes
   */
  private void setNextFour(int i) {
    for (int j = 0; j < 4; ++j) {
      nextTempFourBytes[j] = allMazeBytes[i + j];
    }
  }
}

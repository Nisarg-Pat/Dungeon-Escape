import dungeoncontroller.DungeonController;
import dungeoncontroller.DungeonControllerImpl;
import dungeonmodel.DungeonModel;
import dungeonmodel.DungeonModelImpl;

import java.io.InputStreamReader;

/**
 * A Driver class to create one run of program.
 * It creates a Model and gives control to Controller.
 * It takes input from System.in and gives output at System.out
 */
public class CommandLineDriver {
  /**
   * Main() method to run the driver class.
   *
   * @param args arguments for main method. Should be 6 arguments.
   *             rows: Number of rows in the dungeon. Should be at least 6.
   *             columns: Number of Columns in the dungeon. Should be at least 6.
   *             isWrapped: Whether the dungeon is wrapped around its end
   *             degreeOfInterconnectivity: The degree of interconnectivity for the dungeon.
   *             percentageItems: The percentage of locations having items.
   *             numOtyugh: Number of Otyughs in the dungeon.
   */
  public static void main(String[] args) {
    int rows;
    int columns;
    int degreeOfConnectivity;
    int percentageCavesWithTreasure;
    int numOtyugh;
    boolean isWrapped;
    try {
      rows = Integer.parseInt(args[0]);
      columns = Integer.parseInt(args[1]);
      isWrapped = Boolean.parseBoolean(args[2]);
      degreeOfConnectivity = Integer.parseInt(args[3]);
      percentageCavesWithTreasure = Integer.parseInt(args[4]);
      numOtyugh = Integer.parseInt(args[5]);
    } catch (Exception e) {
      System.out.println("The command line arguments are not valid.");
      return;
    }

    DungeonModel dungeonModel = new DungeonModelImpl(rows, columns,
            isWrapped, degreeOfConnectivity, percentageCavesWithTreasure, numOtyugh, 0, 0);
    DungeonController dungeonController = new DungeonControllerImpl(
            new InputStreamReader(System.in), System.out, dungeonModel);
    try {
      dungeonController.start();
    } catch (IllegalStateException e) {
      System.out.println(e.getMessage());
    }
  }
}

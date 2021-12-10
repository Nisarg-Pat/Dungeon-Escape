import java.io.InputStreamReader;

import dungeoncontroller.DungeonAsyncController;
import dungeoncontroller.DungeonController;
import dungeoncontroller.DungeonConsoleController;
import dungeonmodel.DungeonModel;
import dungeonmodel.DungeonModelImpl;
import dungeonview.DungeonSwingView;
import dungeonview.DungeonView;

/**
 * A Driver class to create one run of program.
 * If command line arguments are present:
 * It creates a Model and gives control to Controller.
 * It takes input from System.in and gives output at System.out
 * <p>
 * If command line arguments are not present:
 * It creates a view and gives control to Controller.
 * It works as an interactive Game using MVC pattern.
 */
public class SpringDriver {

  /**
   * Main() method to run the driver class.
   *
   * @param args arguments for main method. Should be 6 arguments if using Console based game, 0 otherwise.
   *             rows: Number of rows in the dungeon. Should be at least 6.
   *             columns: Number of Columns in the dungeon. Should be at least 6.
   *             isWrapped: Whether the dungeon is wrapped around its end
   *             degreeOfInterconnectivity: The degree of interconnectivity for the dungeon.
   *             percentageItems: The percentage of locations having items.
   *             numOtyugh: Number of Otyughs in the dungeon.
   */
  public static void main(String[] args) {
    if (args.length != 0) {
      startCommandLineController(args);
    } else {
      startSpringController();
    }
  }

  private static void startSpringController() {
    DungeonView dungeonView = new DungeonSwingView();
    DungeonController dungeonController = new DungeonAsyncController(dungeonView);
    dungeonController.start();
  }

  private static void startCommandLineController(String[] args) {
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
            isWrapped, degreeOfConnectivity, percentageCavesWithTreasure, numOtyugh);
    DungeonController dungeonController = new DungeonConsoleController(
            new InputStreamReader(System.in), System.out, dungeonModel);
    try {
      dungeonController.start();
    } catch (IllegalStateException e) {
      System.out.println(e.getMessage());
    }
  }
}

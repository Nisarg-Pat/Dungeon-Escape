import java.io.InputStreamReader;

import dungeoncontroller.DungeonAsyncControllerImpl;
import dungeoncontroller.DungeonController;
import dungeoncontroller.DungeonControllerImpl;
import dungeonmodel.DungeonModel;
import dungeonmodel.DungeonModelImpl;
import dungeonview.DungeonSpringView;
import dungeonview.DungeonView;

public class SpringDriver {
  public static void main(String[] args) {
    if (args.length != 0) {
      startCommandLineController(args);
    } else {
      startSpringController();
    }
  }

  private static void startSpringController() {
    DungeonView dungeonView = new DungeonSpringView();
    DungeonController dungeonController = new DungeonAsyncControllerImpl(dungeonView);
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

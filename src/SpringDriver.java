import dungeoncontroller.DungeonAsyncControllerImpl;
import dungeoncontroller.DungeonController;
import dungeonmodel.DungeonModel;
import dungeonmodel.DungeonModelImpl;
import dungeonview.DungeonSpringView;
import dungeonview.DungeonView;

public class SpringDriver {
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
            isWrapped, degreeOfConnectivity, percentageCavesWithTreasure, numOtyugh);
    DungeonView dungeonView = new DungeonSpringView(dungeonModel);
    DungeonController dungeonController = new DungeonAsyncControllerImpl(dungeonModel, dungeonView);
    dungeonController.start();
  }
}

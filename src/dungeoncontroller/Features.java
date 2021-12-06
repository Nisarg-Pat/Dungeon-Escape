package dungeoncontroller;

import dungeonmodel.Direction;
import dungeonmodel.Item;

public interface Features {
  void movePlayer(Direction north);

  void pickItem(Item item);

  void shootArrow(Direction direction, int distance);

  void exitProgram();

  void createModel(int rows, int columns, boolean isWrapped,
                  int degreeOfInterconnectivity, int percentageItems,
                  int numOtyugh);

  void resetModel();
}

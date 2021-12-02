package dungeoncontroller;

import dungeonmodel.Direction;

public interface Features {
  void movePlayer(Direction north);

  void exitProgram();

  void resetModel(int rows, int columns, boolean isWrapped,
                  int degreeOfInterconnectivity, int percentageItems,
                  int numOtyugh);
}

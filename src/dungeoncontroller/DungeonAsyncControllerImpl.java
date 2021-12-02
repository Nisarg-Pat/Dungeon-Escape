package dungeoncontroller;

import dungeonmodel.Direction;
import dungeonmodel.DungeonModel;
import dungeonmodel.DungeonModelImpl;
import dungeonview.DungeonView;

public class DungeonAsyncControllerImpl implements DungeonAsyncController, Features {

  DungeonModel model;
  DungeonView view;

  public DungeonAsyncControllerImpl(DungeonModel model, DungeonView view) {
    this.model = model;
    this.view = view;
  }

  @Override
  public void start() {
    view.setFeatures(this);
    view.makeVisible();
  }

  @Override
  public void movePlayer(Direction direction) {
    try {
      model.movePlayer(direction);
      view.refresh();
    } catch (IllegalArgumentException | IllegalStateException e) {
      view.showErrorMessage(e.getMessage());
    }

  }

  @Override
  public void exitProgram() {
    System.exit(0);
  }

  @Override
  public void resetModel(int rows, int columns, boolean isWrapped,
                         int degreeOfInterconnectivity, int percentageItems,
                         int numOtyugh) {
    this.model = new DungeonModelImpl(rows, columns, isWrapped,
            degreeOfInterconnectivity, percentageItems, numOtyugh);
    view.setModel(model);
    view.refresh();
  }
}

package dungeoncontroller;

import dungeonmodel.Direction;
import dungeonmodel.DungeonModel;
import dungeonview.DungeonView;

public class DungeonAsyncControllerImpl implements DungeonAsyncController, Features{

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
    model.movePlayer(direction);
    view.refresh();
  }
}

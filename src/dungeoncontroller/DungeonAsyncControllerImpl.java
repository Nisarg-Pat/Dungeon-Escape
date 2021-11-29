package dungeoncontroller;

import dungeonmodel.DungeonModel;
import dungeonview.DungeonView;

public class DungeonAsyncControllerImpl implements DungeonAsyncController{

  DungeonModel model;
  DungeonView view;

  public DungeonAsyncControllerImpl(DungeonModel model, DungeonView view) {
    this.model = model;
    this.view = view;
  }

  @Override
  public void start() {
    view.addClickListener(this);
    view.makeVisible();
  }
}

package dungeoncontroller;

import dungeonmodel.Arrow;
import dungeonmodel.Direction;
import dungeonmodel.DungeonModel;
import dungeonmodel.DungeonModelImpl;
import dungeonmodel.HitStatus;
import dungeonmodel.Item;
import dungeonview.DungeonView;

public class DungeonAsyncControllerImpl implements DungeonAsyncController, Features {

  DungeonModel model;
  DungeonView view;

  public DungeonAsyncControllerImpl(DungeonView view) {
    this.model = null;
    this.view = view;
  }

  @Override
  public void start() {
    view.setFeatures(this);
    view.makeVisible();
//    view.playSound("dungeonSounds\\mario.wav");
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
  public void pickItem(Item item) {
    try {
      model.pickItem(item);
      if(item == Arrow.CROOKED_ARROW) {
        view.playSound("dungeonSounds\\arrow_pick.wav");
      } else {
        view.playSound("dungeonSounds\\treasure_pick.wav");
      }
      view.refresh();
    } catch (IllegalArgumentException | IllegalStateException e) {
      view.showErrorMessage(e.getMessage());
    }
  }

  @Override
  public void shootArrow(Direction direction, int distance) {
    try {
      HitStatus status = model.shoot(direction, distance);
      if(status == HitStatus.HIT || status == HitStatus.KILLED) {
        view.playSound("dungeonSounds\\otyugh_echo2.wav");
      }
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

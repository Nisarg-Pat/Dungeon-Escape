package dungeoncontroller;

import dungeonmodel.Arrow;
import dungeonmodel.Direction;
import dungeonmodel.DungeonModel;
import dungeonmodel.DungeonModelImpl;
import dungeonmodel.GameStatus;
import dungeonmodel.HitStatus;
import dungeonmodel.Item;
import dungeonview.DungeonView;
import random.RandomImpl;

public class DungeonAsyncControllerImpl implements DungeonAsyncController, Features {

  DungeonModel model;
  DungeonView view;

  MoveAbolethThread thread;

  public DungeonAsyncControllerImpl(DungeonView view) {
    this.model = null;
    this.view = view;
    this.thread = new MoveAbolethThread(model, view);
  }

  @Override
  public void start() {
    view.setFeatures(this);
    view.makeVisible();
//    view.playSound("dungeonSounds\\mario_1.wav");
  }

  @Override
  public void movePlayer(Direction direction) {
    try {
      model.movePlayer(direction);
      if (model.getGameStatus() == GameStatus.GAME_CONTINUE) {
        if (model.getCurrentLocation().containsOtyugh()) {
          view.showString("The Otyugh in the cave is too weak to attack!\nGet out of here ASAP!!\n");
        } else {
          view.showString("");
        }
      } else if (model.getGameStatus() == GameStatus.GAME_OVER_KILLED) {
        view.playSound("dungeonSounds\\monstereat.wav");
        view.showString("Chomp, chomp, you are eaten by an Otyugh!!");
      }
      view.refresh();
    } catch (IllegalArgumentException | IllegalStateException e) {
//      view.showErrorMessage(e.getMessage());
      //Ignore the message
    }

  }

  @Override
  public void pickItem(Item item) {
    try {
      model.pickItem(item);
      if (item == Arrow.CROOKED_ARROW) {
        view.playSound("dungeonSounds\\arrow_pick.wav");
      } else {
        view.playSound("dungeonSounds\\treasure_pick.wav");
      }
      view.showString(String.format("Picked %s.", item.getSingular()));
      view.refresh();
    } catch (IllegalArgumentException | IllegalStateException e) {
      view.showErrorMessage(e.getMessage());
    }
  }

  @Override
  public void shootArrow(Direction direction, int distance) {
    try {
      HitStatus status = model.shoot(direction, distance);
      if (status == HitStatus.HIT || status == HitStatus.KILLED) {
        view.playSound("dungeonSounds\\otyugh_echo3.wav");
      }
      if (status == HitStatus.HIT) {
        view.showString("You hear a great howl in the distance.");
      } else if (status == HitStatus.KILLED) {
        view.showString("The howl ended as it was slayed.");
      } else if (status == HitStatus.MISS) {
        view.showString("You shoot an arrow into the darkness.");
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
  public void createNewModel(int rows, int columns, boolean isWrapped,
                             int degree, int percentageItems, int numOtyugh) {
    RandomImpl.setSeed(RandomImpl.getIntInRange(0, 1000));
    createModel(rows, columns, isWrapped, degree, percentageItems, numOtyugh);
  }

  @Override
  public void resetModel() {
    RandomImpl.setSeed(RandomImpl.getSeed());
    createModel(model.getRows(), model.getColumns(), model.getWrapped(),
            model.getDegree(), model.getPercentageItems(), model.countOtyughs());
  }

  @Override
  public void killMonster() {
    try {
      model.killMonster();
      thread.stop();
      view.showString("Aboleth was killed.");
      view.refresh();
    } catch (IllegalArgumentException | IllegalStateException e) {
      view.showErrorMessage(e.getMessage());
    }
  }

  @Override
  public void exitDungeon() {
    try {
      model.exitDungeon();
      view.playSound("dungeonSounds\\win.wav");
      view.showString("Congrats, you reached the end location.");
      view.refresh();
    } catch (IllegalArgumentException | IllegalStateException e) {
      view.showErrorMessage(e.getMessage());
    }
  }

  private void createModel(int rows, int columns, boolean isWrapped, int degree, int percentageItems, int numOtyugh) {
    if (thread.isRunning()) {
      thread.stop();
    }
    this.model = new DungeonModelImpl(rows, columns, isWrapped,
            degree, percentageItems, numOtyugh);
    view.setModel(model);
    thread = new MoveAbolethThread(model, view);
    thread.start();
    view.refresh();
  }
}

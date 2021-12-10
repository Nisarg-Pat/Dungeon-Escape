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

/**
 * An MVC controller representation of {@link DungeonController}.
 * Takes in a DungeonModel and a DungeonView. All the controls of the game is present.
 * Also implements the Features that can be called by a view.
 */
public class DungeonAsyncController implements DungeonController, Features {

  private DungeonModel model;
  private final DungeonView view;

  private MoveAbolethThread thread;

  /**
   * Constructor for {@link DungeonAsyncController}.
   *
   * @param view The DungeonView for the controller.
   */
  public DungeonAsyncController(DungeonView view) {
    if (view == null) {
      throw new IllegalArgumentException("View cannot be null.");
    }
    this.model = null; //Intentional
    this.thread = null; //Intentional
    this.view = view;
  }

  /**
   * Constructor for {@link DungeonAsyncController}.
   *
   * @param model The DungeonModel for the controller.
   * @param view  The DungeonView for the controller.
   */
  public DungeonAsyncController(DungeonModel model, DungeonView view) {
    if (view == null || model == null) {
      throw new IllegalArgumentException("View or Model cannot be null.");
    }
    this.model = model;
    this.thread = null; //Intentional
    this.view = view;
  }

  @Override
  public void start() {
    view.setFeatures(this);
    view.makeVisible();
  }

  @Override
  public void movePlayer(Direction direction) {
    if (model == null) {
      throw new IllegalStateException("Model needs to be initialized.");
    }
    try {
      model.movePlayer(direction);
      if (model.getGameStatus() == GameStatus.GAME_CONTINUE) {
        view.showString("");
        if (model.stealTreasure()) {
          view.showString("The thief in tunnel stole some treasure and ran away.");
        }
        if (model.getCurrentLocation().containsOtyugh()) {
          view.showString(
                  "The Otyugh in the cave is too weak to attack!\nGet out of here ASAP!!\n");
        }
        if (model.getCurrentLocation().hasPit()) {
          Thread pitThread = new Thread(() -> {
            try {
              model.setPlayerinPit(true);
              view.showString("You are in a pit. It will take 5 seconds to come out.");
              view.refresh();
              Thread.sleep(5000);
              model.setPlayerinPit(false);
              if (model.getGameStatus() == GameStatus.GAME_CONTINUE) {
                view.showString("You are out of pit and can move freely.");
              }
              view.refresh();
            } catch (InterruptedException e) {
              //Ignore catch
            }
          });
          pitThread.start();
        }
      } else if (model.getGameStatus() == GameStatus.GAME_OVER_KILLED) {
        view.playSound("res\\dungeonSounds\\monstereat.wav");
        view.showString("Chomp, chomp, you are eaten by an Otyugh!!");
      }
      view.refresh();
    } catch (IllegalArgumentException | IllegalStateException e) {
      //Ignore the message for move command thus ignoring invalid directions
    }

  }

  @Override
  public void pickItem(Item item) {
    if (model == null) {
      throw new IllegalStateException("Model needs to be initialized.");
    }
    try {
      model.pickItem(item);
      if (item == Arrow.CROOKED_ARROW) {
        view.playSound("res\\dungeonSounds\\arrow_pick.wav");
      } else {
        view.playSound("res\\dungeonSounds\\treasure_pick.wav");
      }
      view.showString(String.format("Picked %s.", item.getSingular()));
      view.refresh();
    } catch (IllegalArgumentException | IllegalStateException e) {
      view.showErrorMessage(e.getMessage());
    }
  }

  @Override
  public void shootArrow(Direction direction, int distance) {
    if (model == null) {
      throw new IllegalStateException("Model needs to be initialized.");
    }
    try {
      HitStatus status = model.shoot(direction, distance);
      if (status == HitStatus.HIT || status == HitStatus.KILLED) {
        view.playSound("res\\dungeonSounds\\otyugh_echo3.wav");
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
  public void killMonster() {
    if (model == null) {
      throw new IllegalStateException("Model needs to be initialized.");
    }
    try {
      model.killMonster();
      view.showString("Aboleth was killed.");
      view.refresh();
    } catch (IllegalArgumentException | IllegalStateException e) {
      view.showErrorMessage(e.getMessage());
    }
  }

  @Override
  public void exitDungeon() {
    if (model == null) {
      throw new IllegalStateException("Model needs to be initialized.");
    }
    try {
      model.exitDungeon();
      view.playSound("res\\dungeonSounds\\win.wav");
      view.showString("Congrats, you reached the end location.");
      view.refresh();
    } catch (IllegalArgumentException | IllegalStateException e) {
      view.showErrorMessage(e.getMessage());
    }
  }

  @Override
  public void createNewModel(int rows, int columns, boolean isWrapped,
                             int degree, int percentageItems, int numOtyugh,
                             int numAboleth, int numThief, int numPits) {
    RandomImpl.setSeed(RandomImpl.getIntInRange(0, 1000));
    createModel(rows, columns, isWrapped, degree, percentageItems, numOtyugh,
            numAboleth, numThief, numPits);
  }

  @Override
  public void resetModel() {
    RandomImpl.setSeed(RandomImpl.getSeed());
    createModel(model.getRows(), model.getColumns(), model.getWrapped(),
            model.getDegree(), model.getPercentageItems(), model.countOtyughs(),
            model.countAboleth(), model.countThief(), model.countPits());
  }

  private void createModel(int rows, int columns, boolean isWrapped, int degree,
                           int percentageItems, int numOtyugh, int numAboleth,
                           int numThief, int numPits) {
    if (thread != null) {
      thread.stop();
    }
    this.model = new DungeonModelImpl(rows, columns, isWrapped,
            degree, percentageItems, numOtyugh, numAboleth, numThief, numPits, true);
    view.setModel(model);
    thread = new MoveAbolethThread(model, view);
    thread.start();
    view.refresh();
  }
}

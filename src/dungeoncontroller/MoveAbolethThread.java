package dungeoncontroller;

import dungeonmodel.DungeonModel;
import dungeonmodel.GameStatus;
import dungeonview.DungeonView;

/**
 * A new thread to move Aboleths independent of player movement.
 * Aboleths will move every 2 seconds.
 */
class MoveAbolethThread implements Runnable {

  private boolean exit;

  private final Thread thread;
  private final DungeonModel model;
  private final DungeonView view;

  protected MoveAbolethThread(DungeonModel model, DungeonView view) {
    if (model == null || view == null) {
      throw new IllegalArgumentException("Model or view cannot be null");
    }
    thread = new Thread(this);
    this.model = model;
    this.view = view;
    exit = false;
  }

  @Override
  public void run() {
    try {
      Thread.sleep(2000);
      while (!exit && model.getGameStatus() == GameStatus.GAME_CONTINUE) {
        model.moveAboleth();
        if (model.getGameStatus() == GameStatus.GAME_OVER_KILLED) {
          view.playSound("dungeonSounds\\monstereat.wav");
          view.showString("Chomp, chomp, you are eaten by an Aboleth!!");
        }
        view.refresh();
        Thread.sleep(2000);
      }
      exit = true;
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  protected void start() {
    thread.start();
  }

  protected void stop() {
    exit = true;
  }

  protected boolean isRunning() {
    return !exit;
  }
}

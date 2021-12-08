package dungeoncontroller;

import dungeonmodel.DungeonModel;
import dungeonmodel.GameStatus;
import dungeonview.DungeonView;

//TODO javadoc
class MoveAbolethThread implements Runnable {

  private boolean exit;

  private final Thread thread;
  DungeonModel model;
  DungeonView view;

  MoveAbolethThread(DungeonModel model, DungeonView view) {
    thread = new Thread(this);
    this.model = model;
    this.view = view;
    exit = false;
  }

  @Override
  public void run() {
    try {
      Thread.sleep(5000);
      while (!exit && model.getGameStatus() == GameStatus.GAME_CONTINUE) {
        model.moveAboleth();
        if(model.getGameStatus() == GameStatus.GAME_OVER_KILLED) {
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

  public void start() {
    thread.start();
  }

  // for stopping the thread
  public void stop() {
    exit = true;
  }

  public boolean isRunning() {
    return !exit;
  }
}

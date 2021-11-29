package dungeoncontroller;

/**
 * Represents a Controller for Dungeon Model
 * Handles user moves by executing them using the model.
 * Conveys move outcomes to the user in some form.
 */
public interface DungeonController {
  /**
   * Execute a single adventure game of exploring dungeon given a dungeon Model.
   * When the game is over, the start method ends.
   */
  void start();
}

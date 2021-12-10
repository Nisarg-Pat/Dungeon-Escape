package dungeoncontroller;

import dungeonmodel.Direction;
import dungeonmodel.HitStatus;
import dungeonmodel.Item;

/**
 * Features of the Dungeon Model.
 * Includes all the features that a view can call to interact with model.
 */
public interface Features {
  /**
   * Moves the player to the mentioned direction.
   *
   * @param direction direction to move the player.
   * @throws IllegalArgumentException if player cannot move in this direction.
   */
  void movePlayer(Direction direction);

  /**
   * Picks the mentioned item.
   *
   * @param item Item to be picked.
   * @throws IllegalArgumentException if item cannot be picked.
   */
  void pickItem(Item item);

  /**
   * Shoots an arrow in the mentioned direction and distance.
   *
   * @param direction direction to shoot the arrow.
   * @param distance  distance to shoot the arrow.
   */
  void shootArrow(Direction direction, int distance);

  /**
   * Exits the game.
   */
  void exitProgram();

  /**
   * Creates a new dungeon with properties mentioned by the user.
   *
   * @param rows                      Number of rows in the dungeon. Should be at least 6.
   * @param columns                   Number of Columns in the dungeon. Should be at least 6.
   * @param isWrapped                 Whether the dungeon is wrapped around its end
   * @param degreeOfInterconnectivity The degree of interconnectivity for the dungeon.
   * @param percentageItems           The percentage of locations having items
   * @param numOtyugh                 Number of otyughs in the dungeon
   * @param numAboleth                Number of aboleths in the dungeon
   * @param numThief                  Number of thieves in the dungeon
   * @throws IllegalArgumentException if rows, columns, degree of Interconnectivity
   *                                  or percentage of caves with treasure is invalid.
   */
  void createNewModel(int rows, int columns, boolean isWrapped,
                      int degreeOfInterconnectivity, int percentageItems,
                      int numOtyugh, int numAboleth, int numThief);

  /**
   * Resets the current dungeon model.
   */
  void resetModel();

  /**
   * Kills the monster present in the current location.
   */
  void killMonster();

  /**
   * Exits the dungeon and win the game
   */
  void exitDungeon();
}

package dungeonmodel;

import structureddata.Position;

/**
 * The Dungeon Model for the adventure game.
 * The dungeon contains a network of tunnels and caves that are interconnected so that
 * player can explore the entire world by traveling
 * from cave to cave through the tunnels that connect them.
 * The dungeon is generated randomly.
 * Player is spawned in a random Cave.
 * Player can visit the locations connected by his current location.
 * A location can be connected with maximum 4 other locations,
 * one in each direction(North, South, East, West).
 * Player can collect treasures from the caves.
 * The goal of the player is to reach an end position cave.
 */
public interface DungeonModel extends ReadOnlyDungeonModel {

  /**
   * Moves the player to the location present at the specified direction from his current cave.
   *
   * @param direction The direction in which to move.
   * @throws IllegalArgumentException if direction is null or particular direction
   *                                  is not possible from the current location.
   * @throws IllegalStateException    if game is already over.
   */
  void movePlayer(Direction direction);

  /**
   * Player picks an Item available in the current location.
   *
   * @param item The item to pick
   * @throws IllegalArgumentException if item is not valid or not present in the location.
   */
  void pickItem(Item item);

  /**
   * Shoots a {@link Arrow#CROOKED_ARROW} in the mentioned direction and distance.
   *
   * @param direction The direction to shoot the arrow.
   * @param distance  The distance at which to throw.
   * @return {@link HitStatus#HIT} if the arrow hits an Otyugh else {@link HitStatus#MISS}
   * @throws IllegalArgumentException if direction is null or distance is < 0 or > 5.
   */
  HitStatus shoot(Direction direction, int distance);

  /**
   * Steals the treasure from the player present in the same location.
   *
   * @return true if treasure is stolen.
   */
  boolean stealTreasure();

  /**
   * Gives the distance between two positions in the dungeon.
   *
   * @param first  the first position
   * @param second the second position
   * @return the distance between the two positions in the dungeon
   */
  int getDistance(Position first, Position second);

  /**
   * Move all the aboleths in the dungeons.
   */
  void moveAboleth();

  /**
   * Kill the aboleth in the current location.
   */
  void killMonster();

  /**
   * Set or unset the player in a pit if the current location has a pit.
   *
   * @param pit Whether to set player in a pit
   */
  void setPlayerinPit(boolean pit);

  /**
   * Exit the dungeon.
   */
  void exitDungeon();

  /**
   * Prints the current state of the dungeon.
   *
   * @return the String representation of the state of the dungeon.
   */
  String printDungeon();
}


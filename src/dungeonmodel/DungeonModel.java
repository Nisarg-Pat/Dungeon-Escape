package dungeonmodel;

import structureddata.LocationDescription;
import structureddata.PlayerDescription;
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
public interface DungeonModel {


  /**
   * Gives the number of rows in the dungeon.
   *
   * @return The number of rows in the dungeon
   */
  int getRows();

  /**
   * Gives the number of columns in the dungeon.
   *
   * @return The number of columns in the dungeon
   */
  int getColumns();

  /**
   * Tells whether the dungeon is wrapped or not.
   *
   * @return whether the dungeon is wrapped or not.
   */
  boolean getWrapped();

  /**
   * Gives the number of otyughs in the dungeon.
   *
   * @return the number of otyughs in the dungeon.
   */
  int countOtyughs();

  /**
   * Gets the description of starting Cave of the dungeon.
   *
   * @return the description of starting cave
   */
  LocationDescription getStartCave();

  /**
   * Gets the description of ending Cave of the dungeon.
   *
   * @return the description of ending cave
   */
  LocationDescription getEndCave();

  /**
   * Gets the description of the current location of the player.
   * Contains information about possible directions,
   * player can move from this location and the available treasures in it.
   *
   * @return the description of current location of the person.
   */
  LocationDescription getCurrentLocation();

  /**
   * Gets the description of a specific location in the dungeon.
   *
   * @param position the position of the location whose description is required.
   * @return the description of the location.
   * @throws IllegalArgumentException if position is null
   */
  LocationDescription getLocation(Position position);

  /**
   * Gets the description of the player in the dungeon.
   * Contains information about its current location, and the treasures collected by the player.
   *
   * @return the description of the player
   */
  PlayerDescription getPlayerDescription();

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
   * Gives the status of the game as over or not.
   * Game is over when player reaches the end position or is eaten by an Otyugh.
   *
   * @return the status of the game
   */
  GameStatus getGameStatus();

  /**
   * Prints the current state of the dungeon.
   *
   * @return the String representation of the state of the dungeon.
   */
  String printDungeon();

  /**
   * Gives the distance between two positions in the dungeon.
   *
   * @param first  the first position
   * @param second the second position
   * @return the distance between the two positions in the dungeon
   */
  int getDistance(Position first, Position second);

  /**
   * Gives the smellLevel detected by the Player.
   *
   * @return the smellLevel detected by the Player.
   */
  SmellLevel detectSmell();
}


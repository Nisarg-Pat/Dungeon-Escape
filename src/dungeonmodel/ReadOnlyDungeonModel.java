package dungeonmodel;

import structureddata.LocationDescription;
import structureddata.PlayerDescription;
import structureddata.Position;

/**
 * Read Only version of DungeonModel.
 * Contains all the methods that could be read from a DungeonModel.
 */
public interface ReadOnlyDungeonModel {
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
   * Gives the degree of Interconnectivity in the dungeon.
   *
   * @return the degree of Interconnectivity in the dungeon.
   */
  int getDegree();

  /**
   * Gives the percentage of caves with items in the dungeon.
   *
   * @return the percentage of caves with items in the dungeon.
   */
  int getPercentageItems();


  /**
   * Gives the number of aboleths in the dungeon.
   *
   * @return the number of aboleths in the dungeon.
   */
  int countAboleth();

  /**
   * Gives the number of thieves in the dungeon.
   *
   * @return the number of thieves in the dungeon.
   */
  int countThief();

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
   * Gives the status of the game as over or not.
   * Game is over when player reaches the end position or is eaten by an Otyugh.
   *
   * @return the status of the game
   */
  GameStatus getGameStatus();

  /**
   * Gives the smellLevel detected by the Player.
   *
   * @return the smellLevel detected by the Player.
   */
  SmellLevel detectSmell();

  /**
   * Gives the number of pits in the dungeon
   *
   * @return the number of pits in the dungeon
   */
  int countPits();
}

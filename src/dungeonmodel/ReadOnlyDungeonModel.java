package dungeonmodel;

import structureddata.LocationDescription;
import structureddata.PlayerDescription;
import structureddata.Position;

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

  //TODO: Generate javaDoc
  int getDegree();

  //TODO: Generate javaDoc
  int getPercentageItems();

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
}

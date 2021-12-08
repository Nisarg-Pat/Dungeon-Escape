package structureddata;

import dungeonmodel.Treasure;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Structured Data to store the description of the player which can be used
 * to print the details of the player.
 */
public class PlayerDescription {
  private final Map<Treasure, Integer> collectedTreasures;
  private final Position position;
  private final int numArrows;
  private final boolean hasKey;

  /**
   * Constructor for PlayerDescription class.
   *
   * @param collectedTreasures the collected treasures of the player
   * @param numArrows          The number of arrows collected by the player.
   * @param location           The current position of the player in the dungeon
   * @param hasKey             True if player has Key.
   * @throws IllegalArgumentException if collectedTreasure or location is null
   *                                  or number of arrows is less than 0.
   */
  public PlayerDescription(Map<Treasure, Integer> collectedTreasures,
                           int numArrows, Position location, boolean hasKey) {
    if (collectedTreasures == null || location == null || numArrows < 0) {
      throw new IllegalArgumentException("Invalid arguments.");
    }
    this.collectedTreasures = collectedTreasures;
    this.position = location;
    this.numArrows = numArrows;
    this.hasKey = hasKey;
  }

  /**
   * Gives a map of treasure and its amount collected by the player.
   *
   * @return the map of treasure and its amount collected by the player.
   */
  public Map<Treasure, Integer> getCollectedTreasures() {
    return new TreeMap<>(collectedTreasures);
  }

  /**
   * Gives the position of the player.
   *
   * @return the position of the player.
   */
  public Position getPosition() {
    return new Position(position.getRow(), position.getColumn());
  }

  /**
   * Gives the number of arrows collected by player.
   *
   * @return the number of arrows collected by player.
   */
  public int countArrows() {
    return numArrows;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PlayerDescription)) {
      return false;
    }
    PlayerDescription that = (PlayerDescription) o;
    return Objects.equals(collectedTreasures, that.collectedTreasures)
            && Objects.equals(position, that.position);
  }

  @Override
  public int hashCode() {
    return Objects.hash(collectedTreasures, position);
  }

  @Override
  public String toString() {
    return String.format("Player Description:\nPosition: %s\n"
                    + "Treasures collected: %s\nArrows collected: %d",
            position, collectedTreasures, numArrows);
  }

  public boolean hasKey() {
    return hasKey;
  }
}

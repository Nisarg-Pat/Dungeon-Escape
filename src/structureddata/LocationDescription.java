package structureddata;

import dungeonmodel.Direction;
import dungeonmodel.Treasure;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Structured Data to store the description of the location
 * which can be used to print the details of the player.
 */
public class LocationDescription {
  private final Set<Direction> possibleDirections;
  private final Map<Treasure, Integer> treasureMap;
  private final int numArrows;
  private final Position position;
  private final boolean isCave;
  private final boolean hasOtyugh;

  /**
   * Constructor for LocationDescription class.
   *
   * @param possibleDirections Set of valid directions from this location
   * @param treasureMap        Treasures currently present in the location
   * @param position           The position of the location
   * @param numArrows          The numer of arrows in the location
   * @param isCave             whether the location is a cave
   * @param hasOtyugh          whether the location has otyugh
   * @throws IllegalArgumentException if any of the argument is null
   */
  public LocationDescription(Set<Direction> possibleDirections,
                             Map<Treasure, Integer> treasureMap, Position position,
                             int numArrows, boolean isCave, boolean hasOtyugh) {
    if (possibleDirections == null || treasureMap == null
            || position == null) {
      throw new IllegalArgumentException("Invalid arguments.");
    }
    this.possibleDirections = possibleDirections;
    this.treasureMap = treasureMap;
    this.position = position;
    this.numArrows = numArrows;
    this.isCave = isCave;
    this.hasOtyugh = hasOtyugh;
  }

  /**
   * Gives the set of directions possible from this location.
   *
   * @return the set of directions possible from this location.
   */
  public Set<Direction> getPossibleDirections() {
    return new TreeSet<>(possibleDirections);
  }

  /**
   * Gives a map of treasures present in the location.
   *
   * @return map of treasures present in the location.
   */
  public Map<Treasure, Integer> getTreasureMap() {
    return new TreeMap<>(treasureMap);
  }

  /**
   * Gives the position of the location.
   *
   * @return the position of the location.
   */
  public Position getPosition() {
    return position;
  }

  /**
   * Gives the number of arrows in the location.
   *
   * @return the number of arrows in the location.
   */
  public int countArrows() {
    return numArrows;
  }

  /**
   * Return true if the location is a cave.
   *
   * @return true if the location is a cave.
   */
  public boolean isCave() {
    return isCave;
  }

  /**
   * Return true if the location contains Otyugh.
   *
   * @return true if location contains Otyugh.
   */
  public boolean containsOtyugh() {
    return hasOtyugh;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof LocationDescription)) {
      return false;
    }
    LocationDescription that = (LocationDescription) o;
    return Objects.equals(possibleDirections, that.possibleDirections)
            && Objects.equals(treasureMap, that.treasureMap)
            && Objects.equals(position, that.position);
  }

  @Override
  public int hashCode() {
    return Objects.hash(possibleDirections, treasureMap, position);
  }

  @Override
  public String toString() {
    return String.format("Location Description:\n"
                    + "Possible directions: %s\nTreasures: %s\nNumber of Arrows: %s",
            possibleDirections, treasureMap, numArrows);
  }
}

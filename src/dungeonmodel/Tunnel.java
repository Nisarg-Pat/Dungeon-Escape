package dungeonmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Tunnel representation of the location.
 * A tunnel will have only 2 connections.
 * A Tunnel cannot contain treasures and Otyughs.
 * A Tunnel can contain arrows.
 * Visibility: Package - private
 */
class Tunnel extends AbstractLocation {

  private boolean hasThief;

  protected Tunnel(int row, int column, Map<Direction, Location> connectedMap) {
    super(row, column, connectedMap);
    hasThief = false;
  }

  @Override
  public Map<Treasure, Integer> getTreasureMap() {
    return new TreeMap<>();
  }

  @Override
  public boolean isCave() {
    return false;
  }

  @Override
  public Location addPath(Location other) {
    if (other == null) {
      throw new IllegalArgumentException("Invalid argument.");
    }
    Direction direction = getDirection(other.getPosition());
    connectedMap.put(direction, other);
    Cave newLocation = new Cave(position.getRow(), position.getColumn(), connectedMap);
    for (Location location : connectedMap.values()) {
      location.updatePath(newLocation);
    }
    return newLocation;
  }

  @Override
  public void setTreasure() {
    throw new IllegalStateException("Cannot set/remove treasure in a tunnel");
  }

  @Override
  public void addOtyugh() {
    throw new IllegalStateException("Cannot put Otyugh in a tunnel.");
  }

  @Override
  public void removeTreasure(Treasure treasure, int amount) {
    throw new IllegalStateException("Cannot set/remove treasure in a tunnel");
  }

  @Override
  public HitStatus shootArrowHelper(Direction direction, int distance) {
    if (direction == null) {
      throw new IllegalArgumentException("Direction cannot be null");
    }
    if (distance > 0 && connectedMap.containsKey(direction.opposite())) {
      List<Direction> directions = new ArrayList<>(connectedMap.keySet());
      directions.remove(direction.opposite());
      Direction newDirection = directions.get(0);
      return connectedMap.get(newDirection).shootArrowHelper(newDirection, distance - 1);
    }
    return HitStatus.MISS;
  }

  @Override
  public void setThief(boolean hasThief) {
    this.hasThief = hasThief;
  }

  @Override
  public boolean hasThief() {
    return hasThief;
  }
}

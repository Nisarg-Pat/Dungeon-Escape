package dungeonmodel;

import random.RandomImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Cave representation of the location.
 * A cave can have 1, 3 or 4 connections.
 * Cave can contain treasures, arrows and Otyughs.
 * Visibility: Package - private
 */
class Cave extends AbstractLocation {
  private final Map<Treasure, Integer> treasureMap;
  private Monster otyugh;

  protected Cave(int row, int column) {
    super(row, column, new TreeMap<>());
    treasureMap = new TreeMap<>();
    otyugh = null;
  }

  protected Cave(int row, int column, Map<Direction, Location> connectedMap) {
    this(row, column);
    this.connectedMap.putAll(connectedMap);
    otyugh = null;
  }

  @Override
  public Map<Treasure, Integer> getTreasureMap() {
    return new TreeMap<>(treasureMap);
  }

  @Override
  public boolean isCave() {
    return true;
  }

  @Override
  public boolean containsOtyugh() {
    return this.otyugh != null && otyugh.isAlive();
  }

  @Override
  public Monster getOtyugh() {
    return otyugh;
  }

  @Override
  public Location addPath(Location other) {
    if (other == null) {
      throw new IllegalArgumentException("Invalid location.");
    }
    Direction direction = getDirection(other.getPosition());
    connectedMap.put(direction, other);
    if (connectedMap.size() == 2) {
      Tunnel newLocation = new Tunnel(position.getRow(), position.getColumn(), connectedMap);
      for (Location location : connectedMap.values()) {
        location.updatePath(newLocation);
      }
      return newLocation;
    }
    return this;
  }

  @Override
  public void setTreasure() {
    List<Treasure> treasures = new ArrayList<>(List.of(Treasure.values()));
    int index = RandomImpl.getIntInRange(0, treasures.size() - 1);
    treasureMap.put(treasures.get(index), RandomImpl.getIntInRange(1, 10));
    treasures.remove(index);
    int randomNumber = RandomImpl.getIntInRange(1, 100);
    if (randomNumber <= 50) {
      index = RandomImpl.getIntInRange(0, treasures.size() - 1);
      treasureMap.put(treasures.get(index), RandomImpl.getIntInRange(1, 10));
      treasures.remove(index);
      randomNumber = RandomImpl.getIntInRange(1, 100);
      if (randomNumber <= 40) {
        index = RandomImpl.getIntInRange(0, treasures.size() - 1);
        treasureMap.put(treasures.get(index), RandomImpl.getIntInRange(1, 10));
        treasures.remove(index);
      }
    }
    for (Treasure treasure : treasures) {
      int number = RandomImpl.getIntInRange(0, 5);
      if (number == 0) {
        treasureMap.put(treasure, RandomImpl.getIntInRange(1, 10));
      }
    }
  }

  @Override
  public void addOtyugh() {
    otyugh = new Otyugh(this);
  }

  @Override
  public void removeTreasure(Treasure treasure, int amount) {
    if (treasure == null) {
      throw new IllegalArgumentException("Invalid treasure.");
    }
    int newAmount = treasureMap.get(treasure) - amount;
    if (newAmount < 0) {
      throw new IllegalArgumentException("Does not contain enough treasure to be removed.");
    } else if (newAmount == 0) {
      treasureMap.remove(treasure);
    } else {
      treasureMap.put(treasure, newAmount);
    }

  }

  @Override
  public HitStatus shootArrowHelper(Direction direction, int distance) {
    if (distance == 0) {
      if (containsOtyugh()) {
        otyugh.damage();
        if (otyugh.isAlive()) {
          return HitStatus.HIT;
        } else {
          return HitStatus.KILLED;
        }
      }
    } else if (connectedMap.containsKey(direction)) {
      return connectedMap.get(direction).shootArrowHelper(direction, distance - 1);
    }
    return HitStatus.MISS;
  }

  @Override
  public void setThief(boolean hasThief) {
    throw new IllegalStateException("Cave should not have any theif.");
  }

  @Override
  public boolean hasThief() {
    return false;
  }
}

package dungeonmodel;

import random.RandomImpl;
import structureddata.DistanceData;
import structureddata.Position;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;

/**
 * Abstract representation of a location.
 * Contains implementation of similar methods of locations.
 * Visibility: Package - private
 */
abstract class AbstractLocation implements Location {
  protected final Map<Direction, Location> connectedMap;
  protected final Position position;
  protected int numCrookedArrows;

  protected AbstractLocation(int row, int column, Map<Direction, Location> connectedMap) {
    if (row < 0 || column < 0) {
      throw new IllegalArgumentException("Invalid argument.");
    }
    this.position = new Position(row, column);
    this.connectedMap = connectedMap;
    this.numCrookedArrows = 0;
  }

  @Override
  public Position getPosition() {
    return position;
  }

  @Override
  public Map<Direction, Location> getConnections() {
    return new TreeMap<>(connectedMap);
  }

  @Override
  public int countArrows() {
    return numCrookedArrows;
  }

  @Override
  public boolean isCave() {
    return false;
  }

  @Override
  public boolean containsOtyugh() {
    return false;
  }

  @Override
  public Monster getOtyugh() {
    return null;
  }

  @Override
  public boolean hasEdge(Location location) {
    if (location == null) {
      throw new IllegalArgumentException("Invalid location.");
    }
    return connectedMap.containsValue(location);
  }

  @Override
  public void updatePath(Location other) {
    if (other == null) {
      throw new IllegalArgumentException("Invalid location.");
    }
    Direction direction = getDirection(other.getPosition());
    connectedMap.put(direction, other);
  }

  @Override
  public void setArrow() {
    int numArrows = RandomImpl.getIntInRange(1, 3);
    this.numCrookedArrows += numArrows;
  }

  @Override
  public void removeArrow(int amount) {
    if (numCrookedArrows == 0) {
      throw new IllegalStateException("Location does not have any arrows.");
    }
    int newAmount = numCrookedArrows - amount;
    if (newAmount < 0) {
      throw new IllegalArgumentException("Does not contain enough arrows to be removed.");
    } else {
      numCrookedArrows = newAmount;
    }
  }

  @Override
  public SmellLevel getSmellLevel() {
    Set<Location> visited = new HashSet<>();
    Queue<DistanceData<Location>> q = new LinkedList<>();
    visited.add(this);
    q.add(new DistanceData<>(this, this, 0));
    int[] otyughAtDistance = new int[3];
    while (!q.isEmpty()) {
      DistanceData<Location> distanceData = q.poll();
      Location current = distanceData.getEnd();
      int distance = distanceData.getDistance();
      if (current.containsOtyugh()) {
        otyughAtDistance[distance] += 1;
      }
      for (Location location : current.getConnections().values()) {
        if (!visited.contains(location) && distance < 2) {
          visited.add(location);
          q.add(new DistanceData<>(this, location, distance + 1));
        }
      }
    }
    if (otyughAtDistance[1] == 0 && otyughAtDistance[2] == 1) {
      return SmellLevel.LESS_PUNGENT;
    } else if (otyughAtDistance[1] + otyughAtDistance[2] > 0) {
      return SmellLevel.MORE_PUNGENT;
    }
    return SmellLevel.NO_SMELL;
  }

  @Override
  public HitStatus shootArrow(Direction direction, int distance) {
    if (direction == null) {
      throw new IllegalArgumentException("Direction cannot be null.");
    } else if (distance < 1 || distance > 5) {
      throw new IllegalArgumentException("Distance should be between 1 and 5.");
    } else if (connectedMap.containsKey(direction)) {
      return connectedMap.get(direction).shootArrowHelper(direction, distance - 1);
    } else {
      return HitStatus.MISS;
    }
  }

  @Override
  public Direction getDirection(Position otherPosition) {
    if (otherPosition == null) {
      throw new IllegalArgumentException("Invalid Position");
    }
    int rowChange = position.getRow() - otherPosition.getRow();
    int columnChange = position.getColumn() - otherPosition.getColumn();
    if (columnChange == 0) {
      if (rowChange == 1) {
        return Direction.NORTH;
      } else if (rowChange == -1) {
        return Direction.SOUTH;
      } else if (position.getRow() == 0) {
        return Direction.NORTH;
      } else if (otherPosition.getRow() == 0) {
        return Direction.SOUTH;
      }
    } else if (rowChange == 0) {
      if (columnChange == 1) {
        return Direction.WEST;
      } else if (columnChange == -1) {
        return Direction.EAST;
      } else if (position.getColumn() == 0) {
        return Direction.WEST;
      } else if (otherPosition.getColumn() == 0) {
        return Direction.EAST;
      }
    }
    throw new IllegalArgumentException("The Locations cannot be connected");
  }
}

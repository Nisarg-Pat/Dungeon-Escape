package dungeonmodel;

/**
 * Enum representation of possible directions from a location: NORTH, SOUTH, EAST, WEST.
 */
public enum Direction {
  NORTH,
  EAST,
  SOUTH,
  WEST;

  /**
   * Gives the direction opposite to the current direction.
   *
   * @return the opposite direction from the current
   */
  public Direction opposite() {
    switch (this) {
      case NORTH:
        return SOUTH;
      case SOUTH:
        return NORTH;
      case EAST:
        return WEST;
      case WEST:
        return EAST;
      default:
        return null;
    }
  }

  @Override
  public String toString() {
    switch (this) {
      case NORTH:
        return "North";
      case EAST:
        return "East";
      case SOUTH:
        return "South";
      case WEST:
        return "West";
      default:
        return super.toString();
    }
  }
}

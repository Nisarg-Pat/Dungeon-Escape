package dungeonview;

import java.awt.*;
import java.util.Map;
import java.util.Set;

import dungeonmodel.Direction;
import structureddata.LocationDescription;
import structureddata.Position;

class Utilities {

  static int X_SPACE = 50;
  static int Y_SPACE = 50;

  static int IMAGE_WIDTH = 64;
  static int IMAGE_HEIGHT = 64;

  protected static String getImageName(Set<Direction> directionMap) {
    StringBuilder sb = new StringBuilder("dungeonImages\\directions\\");
    if (directionMap.contains(Direction.NORTH)) {
      sb.append("N");
    }
    if (directionMap.contains(Direction.EAST)) {
      sb.append("E");
    }
    if (directionMap.contains(Direction.SOUTH)) {
      sb.append("S");
    }
    if (directionMap.contains(Direction.WEST)) {
      sb.append("W");
    }
    sb.append(".png");
    return sb.toString();
  }

  protected static Position getLocationPosition(int row, int column) {
    return new Position(row * IMAGE_HEIGHT + Y_SPACE, column * IMAGE_WIDTH + X_SPACE);
  }

  protected static Position getPointPosition(LocationDescription location) {
    int row = location.getPosition().getRow() * IMAGE_HEIGHT + Y_SPACE + 32 - 8;
    int column = location.getPosition().getColumn() * IMAGE_WIDTH + X_SPACE + 32 - 8;
    if (!location.isCave()) {
      Set<Direction> directionSet = location.getPossibleDirections();
      if (directionSet.contains(Direction.NORTH) && directionSet.contains(Direction.EAST)) {
        row -= 9;
        column += 9;
      }
      if (directionSet.contains(Direction.NORTH) && directionSet.contains(Direction.WEST)) {
        row -= 9;
        column -= 9;
      }
      if (directionSet.contains(Direction.EAST) && directionSet.contains(Direction.SOUTH)) {
        row += 9;
        column += 9;
      }
      if (directionSet.contains(Direction.SOUTH) && directionSet.contains(Direction.WEST)) {
        row += 9;
        column -= 9;
      }
    }
    return new Position(row, column);
  }
}

package dungeonview;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import dungeonmodel.Arrow;
import dungeonmodel.Direction;
import dungeonmodel.Item;
import dungeonmodel.Key;
import dungeonmodel.SmellLevel;
import dungeonmodel.Treasure;
import structureddata.LocationDescription;
import structureddata.PlayerDescription;
import structureddata.Position;


/**
 * A utility function for different components of View.
 * Common code among different components is written to reduce code length in each files.
 * Visibility: package-private.
 */
class Utilities {

  private final static int X_SPACE = 50;
  private final static int Y_SPACE = 50;

  private final static int IMAGE_WIDTH = 64;
  private final static int IMAGE_HEIGHT = 64;

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

  protected static String getImageName(Item item) {
    if (item == Treasure.RUBY) {
      return "dungeonImages\\items\\ruby.png";
    } else if (item == Treasure.SAPPHIRE) {
      return "dungeonImages\\items\\sapphire.png";
    } else if (item == Treasure.DIAMOND) {
      return "dungeonImages\\items\\diamond.png";
    } else if (item == Arrow.CROOKED_ARROW) {
      return "dungeonImages\\items\\arrow.png";
    } else if (item == Key.DOOR_KEY) {
      return "dungeonImages\\items\\key.png";
    }
    return "";
  }

  protected static Position getLocationPosition(int row, int column) {
    return new Position(row * IMAGE_HEIGHT + Y_SPACE, column * IMAGE_WIDTH + X_SPACE);
  }

  protected static Position getPointPosition(LocationDescription location) {
    int row = location.getPosition().getRow() * IMAGE_HEIGHT + Y_SPACE + 32 - 8;
    int column = location.getPosition().getColumn() * IMAGE_WIDTH + X_SPACE + 32 - 8;

    if (!location.isCave()) {
      int[] changes = getChanges(location.getPossibleDirections());
      row += changes[0];
      column += changes[1];
    }
    return new Position(row, column);
  }

  protected static int[] getChanges(Set<Direction> directionSet) {
    int[] changes = new int[2];
    if (directionSet.contains(Direction.NORTH) && directionSet.contains(Direction.EAST)) {
      changes[0] -= 9;
      changes[1] += 9;
    }
    if (directionSet.contains(Direction.NORTH) && directionSet.contains(Direction.WEST)) {
      changes[0] -= 9;
      changes[1] -= 9;
    }
    if (directionSet.contains(Direction.EAST) && directionSet.contains(Direction.SOUTH)) {
      changes[0] += 9;
      changes[1] += 9;
    }
    if (directionSet.contains(Direction.SOUTH) && directionSet.contains(Direction.WEST)) {
      changes[0] += 9;
      changes[1] -= 9;
    }
    return changes;
  }
}

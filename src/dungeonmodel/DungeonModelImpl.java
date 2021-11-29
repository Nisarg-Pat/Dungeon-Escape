package dungeonmodel;

import structureddata.LocationDescription;
import structureddata.PlayerDescription;
import structureddata.Position;

import java.util.Arrays;
import java.util.List;

/**
 * Representation of the Dungeon specific to the adventure game.
 * The dungeon is represented as a 2-D grid.
 * There is a path from every cave in the dungeon to every other cave in the dungeon.
 * Each dungeon can be constructed with a degree of interconnectivity.
 * interconnectivity is 0 when there is exactly one path from every cave in the dungeon
 * to every other cave in the dungeon.
 * Increasing the degree of interconnectivity increases the number of paths between caves.
 * Dungeons can wrap from one side to the other
 * One cave is randomly selected as the start and one cave is randomly selected to be the end.
 * The path between the start and the end locations should be at least of length 5.
 * Dungeon contains Otyugh Monsters present in a cave and giving away smell.
 * Otyughs can be killed with Crooked Arrows which can be picked and shot by the player.
 * Player initially has 3 arrows.
 * Game is over when Otyugh kills the player or player reaches end location successfully.
 */
public class DungeonModelImpl implements DungeonModel {
  private final LocationGraph locationGraph;
  private final Location startLocation;
  private final Location endLocation;
  private final Player player;

  private GameStatus status;

  /**
   * Creates a new dungeon with properties mentioned by the user.
   *
   * @param rows                      Number of rows in the dungeon. Should be at least 6.
   * @param columns                   Number of Columns in the dungeon. Should be at least 6.
   * @param isWrapped                 Whether the dungeon is wrapped around its end
   * @param degreeOfInterconnectivity The degree of interconnectivity for the dungeon.
   * @param percentageItems           The percentage of locations having items
   * @throws IllegalArgumentException if rows, columns, degree of Interconnectivity
   *                                  or percentage of caves with treasure is invalid.
   */
  public DungeonModelImpl(int rows, int columns, boolean isWrapped,
                          int degreeOfInterconnectivity, int percentageItems,
                          int numOtyugh) {
    if (numOtyugh <= 0) {
      throw new IllegalArgumentException("Number of Otyugh should be atleast 1.");
    }
    locationGraph = new LocationGraphImpl(rows, columns, isWrapped,
            degreeOfInterconnectivity, percentageItems);

    List<Location> startEndPositions = locationGraph.getStartEndPositions();

    startLocation = startEndPositions.get(0);
    endLocation = startEndPositions.get(1);

    locationGraph.addOtyughToCaves(numOtyugh, startLocation, endLocation);

    player = new PlayerImpl(startLocation);
    status = GameStatus.GAME_CONTINUE;
  }

  @Override
  public int getRows() {
    return locationGraph.getRows();
  }

  @Override
  public int getColumns() {
    return locationGraph.getColumns();
  }

  @Override
  public boolean getWrapped() {
    return locationGraph.isWrapped();
  }

  @Override
  public int countOtyughs() {
    return locationGraph.countOtyughs();
  }

  @Override
  public LocationDescription getStartCave() {
    return getLocation(startLocation.getPosition());
  }

  @Override
  public LocationDescription getEndCave() {
    return getLocation(endLocation.getPosition());
  }

  @Override
  public LocationDescription getCurrentLocation() {
    return getLocation(player.getLocation().getPosition());
  }

  @Override
  public LocationDescription getLocation(Position position) {
    if (position == null) {
      throw new IllegalArgumentException("Position cannot be null.");
    }
    Location location = locationGraph.getLocation(position);
    return new LocationDescription(location.getConnections().keySet(),
            location.getTreasureMap(), location.getPosition(),
            location.countArrows(), location.isCave(), location.containsOtyugh());
  }

  @Override
  public PlayerDescription getPlayerDescription() {
    return new PlayerDescription(player.getCollectedTreasures(), player.countArrows(),
            player.getLocation().getPosition());
  }

  @Override
  public void movePlayer(Direction direction) {
    checkGameStatus();
    if (direction == null) {
      throw new IllegalArgumentException("Direction cannot be null");
    }
    Location newLocation = player.movePlayer(direction);
    if (newLocation.containsOtyugh()) {
      status = newLocation.getOtyugh().killPlayer();
    } else if (newLocation == endLocation) {
      status = GameStatus.GAME_OVER_WIN;
    }
  }

  @Override
  public void pickItem(Item item) {
    checkGameStatus();
    if (item == Arrow.CROOKED_ARROW) {
      player.pickOneArrow();
    } else if (Arrays.asList((Item[]) Treasure.values()).contains(item)) {
      player.pickOneTreasure((Treasure) item);
    } else {
      throw new IllegalArgumentException(String.format("%s item is not valid.", item));
    }
  }

  @Override
  public HitStatus shoot(Direction direction, int distance) {
    checkGameStatus();
    return player.shootArrow(direction, distance);
  }

  private void checkGameStatus() {
    if (status != GameStatus.GAME_CONTINUE) {
      throw new IllegalStateException("Game is already over");
    }
  }

  @Override
  public GameStatus getGameStatus() {
    return status;
  }

  @Override
  public String printDungeon() {
    StringBuilder sb = new StringBuilder();
    for (int j = 0; j < locationGraph.getColumns(); j++) {
      if (locationGraph.hasEdge(new Position(locationGraph.getRows() - 1, j), new Position(0, j))) {
        sb.append("     | ");
      } else {
        sb.append("       ");
      }
    }
    sb.append("    \n");
    for (int i = 0; i < locationGraph.getRows(); i++) {
      if (locationGraph.hasEdge(new Position(
              i, locationGraph.getColumns() - 1), new Position(i, 0))) {
        sb.append(" —— ");
      } else {
        sb.append("    ");
      }
      for (int j = 0; j < locationGraph.getColumns(); j++) {
        sb.append("(").append(checkSpecial(i, j)).append(")");
        if (locationGraph.hasEdge(new Position(i, j),
                new Position(i, (j + 1) % locationGraph.getColumns()))) {
          sb.append(" —— ");
        } else {
          sb.append("    ");
        }
      }
      sb.append("\n");
      for (int j = 0; j < locationGraph.getColumns(); j++) {
        if (locationGraph.hasEdge(new Position(i, j),
                new Position((i + 1) % locationGraph.getRows(), j))) {
          sb.append("     | ");
        } else {
          sb.append("       ");
        }
      }
      sb.append("    \n");
    }
    return sb.toString();
  }

  @Override
  public int getDistance(Position first, Position second) {
    if (first == null || second == null) {
      throw new IllegalArgumentException("Invalid positions.");
    }
    return locationGraph.getDistance(first, second);
  }

  @Override
  public SmellLevel detectSmell() {
    return player.detectSmell();
  }

  private char checkSpecial(int row, int column) {
    Location playerLocation = player.getLocation();
    if (playerLocation.getPosition().equals(new Position(row, column))) {
      return 'P';
    } else if (locationGraph.getLocation(new Position(row, column)).containsOtyugh()) {
      if (endLocation.getPosition().equals(new Position(row, column))) {
        return '&';
      } else {
        return 'M';
      }
    } else if (startLocation.getPosition().equals(new Position(row, column))) {
      return '#';
    }
    return ' ';
  }
}


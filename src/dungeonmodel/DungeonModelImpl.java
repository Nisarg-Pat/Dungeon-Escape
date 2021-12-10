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
 * Dungeon contains Aboleths which are moving monsters in the dungeon.
 * Player needs to kill the aboleth before it sees you or else aboleth will kill the player.
 * Tunnels contain thieves that can steal treasure from player.
 * Thieves then relocate themselves to a different location after stealing the treasure.
 * Player requires a key to open the door in end location.
 * Key can be found randomly at any location in the dungeon.
 * Game ends if player gets killed or player opens the door.
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
   * @param numOtyugh                 Number of otyughs in the dungeon
   * @param numAboleth                Number of aboleths in the dungeon
   * @param numThief                  Number of thieves in the dungeon
   * @param requireKey                Whether player requires a key to open door at end cave
   * @throws IllegalArgumentException if rows, columns, degree of Interconnectivity
   *                                  or percentage of caves with treasure is invalid.
   */
  public DungeonModelImpl(int rows, int columns, boolean isWrapped,
                          int degreeOfInterconnectivity, int percentageItems,
                          int numOtyugh, int numAboleth, int numThief, boolean requireKey) {
    if (numOtyugh <= 0) {
      throw new IllegalArgumentException("Number of Otyugh should be atleast 1.");
    }
    locationGraph = new LocationGraphImpl(rows, columns, isWrapped,
            degreeOfInterconnectivity, percentageItems, numAboleth, numThief, requireKey);

    List<Location> startEndPositions = locationGraph.getStartEndPositions();

    startLocation = startEndPositions.get(0);
    endLocation = startEndPositions.get(1);

    locationGraph.addOtyughToCaves(numOtyugh, startLocation, endLocation);
    locationGraph.addAbolethToRandomLocation(startLocation);

    player = new PlayerImpl(startLocation);
    startLocation.setVisited(true);
    status = GameStatus.GAME_CONTINUE;
  }

  /**
   * Creates a new dungeon with properties mentioned by the user.
   *
   * @param rows                      Number of rows in the dungeon. Should be at least 6.
   * @param columns                   Number of Columns in the dungeon. Should be at least 6.
   * @param isWrapped                 Whether the dungeon is wrapped around its end
   * @param degreeOfInterconnectivity The degree of interconnectivity for the dungeon.
   * @param percentageItems           The percentage of locations having items
   * @param numOtyugh                 Number of otyughs in the dungeon
   * @throws IllegalArgumentException if rows, columns, degree of Interconnectivity
   *                                  or percentage of caves with treasure is invalid.
   */
  public DungeonModelImpl(int rows, int columns, boolean isWrapped,
                          int degreeOfInterconnectivity, int percentageItems,
                          int numOtyugh) {
    this(rows, columns, isWrapped, degreeOfInterconnectivity, percentageItems, numOtyugh, 0, 0, false);
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
  public int getDegree() {
    return locationGraph.getDegree();
  }

  @Override
  public int getPercentageItems() {
    return locationGraph.getPercentageItems();
  }

  @Override
  public int countAboleth() {
    return locationGraph.countAboleth();
  }

  @Override
  public int countThief() {
    return locationGraph.countThief();
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
            location.countArrows(), location.isCave(), location.containsOtyugh(),
            location.isVisited(), location.hasAboleth(), location.hasKey(), location.hasThief());
  }

  @Override
  public PlayerDescription getPlayerDescription() {
    return new PlayerDescription(player.getCollectedTreasures(), player.countArrows(),
            player.getLocation().getPosition(), player.hasKey());
  }

  @Override
  public void movePlayer(Direction direction) {
    checkGameStatus();
    if (direction == null) {
      throw new IllegalArgumentException("Direction cannot be null");
    }
    Location newLocation = player.movePlayer(direction);
    if (!newLocation.isVisited()) {
      newLocation.setVisited(true);
    }
    if (newLocation.containsOtyugh()) {
      status = newLocation.getOtyugh().killPlayer();
    } else if (!locationGraph.requireKey() && newLocation == endLocation) {
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
    } else if (item == Key.DOOR_KEY) {
      player.pickKey();
    } else {
      throw new IllegalArgumentException(String.format("%s item is not valid.", item));
    }
  }

  @Override
  public boolean stealTreasure() {
    checkGameStatus();
    return locationGraph.stealTreasure(player);
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
  public void moveAboleth() {
    Monster currentAboleth = locationGraph.getAboleth(player.getLocation());
    if (currentAboleth != null) {
      status = currentAboleth.killPlayer();
    } else {
      locationGraph.moveAboleth();
    }
  }

  @Override
  public void killMonster() {
    checkGameStatus();
    player.killMonster(locationGraph.getAboleth(player.getLocation()));
  }

  @Override
  public void exitDungeon() {
    if (player.hasKey()) {
      status = GameStatus.GAME_OVER_WIN;
    } else {
      throw new IllegalStateException("Player does not have the key.");
    }
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


import java.util.HashMap;
import java.util.HashSet;

import dungeonmodel.Direction;
import dungeonmodel.DungeonModel;
import dungeonmodel.GameStatus;
import dungeonmodel.HitStatus;
import dungeonmodel.Item;
import dungeonmodel.SmellLevel;
import structureddata.LocationDescription;
import structureddata.PlayerDescription;
import structureddata.Position;

/**
 * A Mock Logging DungeonModel to check if the input from controller is sent correctly to a model.
 * Only purpose is for testing
 */
public class MockModelLogging implements DungeonModel {

  private final StringBuilder log;

  /**
   * Constructor for MockModelLogging.
   * @param log The StringBuilder for Logging
   */
  public MockModelLogging(StringBuilder log) {
    if (log == null) {
      throw new IllegalArgumentException("Log cannot be null");
    }
    this.log = log;
  }

  @Override
  public LocationDescription getStartCave() {
    return new LocationDescription(new HashSet<>(), new HashMap<>(),
            new Position(0, 0), 0, false, false, false, false, false, false, false, false);
  }

  @Override
  public LocationDescription getEndCave() {
    return new LocationDescription(new HashSet<>(), new HashMap<>(),
            new Position(0, 0), 0, false, false, false, false, false, false, false, false);
  }

  @Override
  public PlayerDescription getPlayerDescription() {
    return new PlayerDescription(new HashMap<>(), 0, new Position(0, 0), false, false);
  }

  @Override
  public LocationDescription getCurrentLocation() {
    return new LocationDescription(new HashSet<>(), new HashMap<>(),
            new Position(0, 0), 0, false, false, false, false, false, false, false, false);
  }

  @Override
  public LocationDescription getLocation(Position position) {
    return new LocationDescription(new HashSet<>(), new HashMap<>(),
            new Position(0, 0), 0, false, false, false, false, false, false, false, false);
  }

  @Override
  public void movePlayer(Direction direction) {
    log.append("Move: ").append(direction).append("\n");
  }

  @Override
  public void pickItem(Item item) {
    log.append("Pick: ").append(item).append("\n");
  }

  @Override
  public GameStatus getGameStatus() {
    return GameStatus.GAME_CONTINUE;
  }

  @Override
  public String printDungeon() {
    return "";
  }

  @Override
  public int getDistance(Position first, Position second) {
    return 0;
  }

  @Override
  public void moveAboleth() {
    log.append("Move Aboleth called.\n");
  }

  @Override
  public void killMonster() {
    log.append("Kill Monster called.\n");
  }

  @Override
  public void setPlayerinPit(boolean pit) {
    log.append("Set Player in Pit called: ").append(pit).append("\n");
  }

  @Override
  public void exitDungeon() {
    log.append("Exit Dungeon called.\n");
  }

  @Override
  public SmellLevel detectSmell() {
    return SmellLevel.NO_SMELL;
  }

  @Override
  public int countPits() {
    return 0;
  }

  @Override
  public HitStatus shoot(Direction direction, int distance) {
    log.append("Shoot: Direction = ").append(direction)
            .append(", Distance = ").append(distance).append("\n");
    return HitStatus.MISS;
  }

  @Override
  public boolean stealTreasure() {
    return false;
  }

  @Override
  public int getRows() {
    return 0;
  }

  @Override
  public int getColumns() {
    return 0;
  }

  @Override
  public boolean getWrapped() {
    return false;
  }

  @Override
  public int countOtyughs() {
    return 0;
  }

  @Override
  public int getDegree() {
    return 0;
  }

  @Override
  public int getPercentageItems() {
    return 0;
  }

  @Override
  public int countAboleth() {
    return 0;
  }

  @Override
  public int countThief() {
    return 0;
  }
}

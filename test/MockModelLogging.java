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

  public MockModelLogging(StringBuilder log) {
    this.log = log;
  }

  @Override
  public LocationDescription getStartCave() {
    return new LocationDescription(new HashSet<>(), new HashMap<>(),
            new Position(0, 0), 0, false, false, false, false, false, false);
  }

  @Override
  public LocationDescription getEndCave() {
    return new LocationDescription(new HashSet<>(), new HashMap<>(),
            new Position(0, 0), 0, false, false, false, false, false, false);
  }

  @Override
  public PlayerDescription getPlayerDescription() {
    return new PlayerDescription(new HashMap<>(), 0, new Position(0, 0), false);
  }

  @Override
  public LocationDescription getCurrentLocation() {
    return new LocationDescription(new HashSet<>(), new HashMap<>(),
            new Position(0, 0), 0, false, false, false, false, false, false);
  }

  @Override
  public LocationDescription getLocation(Position position) {
    return new LocationDescription(new HashSet<>(), new HashMap<>(),
            new Position(0, 0), 0, false, false, false, false, false, false);
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

  }

  @Override
  public void killMonster() {

  }

  @Override
  public void exitDungeon() {

  }

  @Override
  public SmellLevel detectSmell() {
    return SmellLevel.NO_SMELL;
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

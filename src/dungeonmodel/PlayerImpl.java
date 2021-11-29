package dungeonmodel;

import java.util.Map;
import java.util.TreeMap;

/**
 * Representation of a player in the dungeon.
 * Implements method required for the player to carry out in a dungeon.
 * Visibility: Package - private
 */
class PlayerImpl implements Player {
  private final Map<Treasure, Integer> collectedTreasures;
  private Location currentLocation;
  private int numCrookedArrows;

  private static final int INITIAL_CROOKED_ARROWS = 3;

  protected PlayerImpl(Location currentLocation) {
    if (currentLocation == null) {
      throw new IllegalArgumentException("Start Position cannot be null.");
    } else if (!currentLocation.isCave()) {
      throw new IllegalArgumentException("Start Position cannot be a tunnel");
    }
    this.collectedTreasures = new TreeMap<>();
    this.currentLocation = currentLocation;
    this.numCrookedArrows = INITIAL_CROOKED_ARROWS;
  }

  @Override
  public Map<Treasure, Integer> getCollectedTreasures() {
    return new TreeMap<>(collectedTreasures);
  }

  @Override
  public int countArrows() {
    return numCrookedArrows;
  }

  @Override
  public Location getLocation() {
    return currentLocation;
  }

  @Override
  public Location movePlayer(Direction direction) {
    if (direction == null) {
      throw new IllegalArgumentException("Direction cannot be null");
    }
    if (!currentLocation.getConnections().containsKey(direction)) {
      throw new IllegalArgumentException(
              String.format("Direction %s is not possible from here.", direction));
    }
    currentLocation = currentLocation.getConnections().get(direction);
    return currentLocation;
  }

  @Override
  public void pickOneTreasure(Treasure treasure) {
    Map<Treasure, Integer> locationTreasure = currentLocation.getTreasureMap();
    if (!locationTreasure.containsKey(treasure)) {
      throw new IllegalArgumentException("Location does not contain treasure: " + treasure);
    }
    collectedTreasures.put(treasure, collectedTreasures.getOrDefault(treasure, 0) + 1);
    currentLocation.removeTreasure(treasure, 1);
  }

  @Override
  public void pickOneArrow() {
    int locationArrows = currentLocation.countArrows();
    if (locationArrows == 0) {
      throw new IllegalArgumentException("No arrows to Pick.");
    } else {
      numCrookedArrows++;
      currentLocation.removeArrow(1);
    }
  }

  @Override
  public HitStatus shootArrow(Direction direction, int distance) {
    if (numCrookedArrows == 0) {
      throw new IllegalArgumentException("Player does not have any arrows.");
    }
    HitStatus status = currentLocation.shootArrow(direction, distance);
    numCrookedArrows--;
    return status;
  }

  @Override
  public SmellLevel detectSmell() {
    return currentLocation.getSmellLevel();
  }
}

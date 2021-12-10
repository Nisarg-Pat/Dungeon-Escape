package dungeonmodel;

import java.util.ArrayList;
import java.util.List;

import random.RandomImpl;
import structureddata.Position;

/**
 * Aboleth Monster that resides in dungeon.
 * Aboleth is able to move to any of the nearby location from its current location.
 * Aboleth will kill player 100% of times when it sees the player.
 * Visibility: Package - private
 */
class Aboleth implements Monster {
  private Location location;
  private int health;

  protected Aboleth(Location location) {
    if(location == null) {
      throw new IllegalArgumentException("Location cannot be null");
    }
    this.location = location;
    location.setAboleth(true);
    this.health = 1;
  }

  @Override
  public Position getPosition() {
    return location.getPosition();
  }

  @Override
  public void damage() {
    health--;
    if (health <= 0) {
      location.setAboleth(false);
    }
  }

  @Override
  public boolean isAlive() {
    return health > 0;
  }

  @Override
  public GameStatus killPlayer() {
    return GameStatus.GAME_OVER_KILLED;
  }

  @Override
  public void move() {
    List<Direction> possibleDirections = new ArrayList<>(location.getConnections().keySet());
    int random = RandomImpl.getIntInRange(0, possibleDirections.size() - 1);
    location.setAboleth(false);
    location = location.getConnections().get(possibleDirections.get(random));
    location.setAboleth(true);
  }
}

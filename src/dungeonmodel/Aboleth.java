package dungeonmodel;

import java.util.ArrayList;
import java.util.List;

import random.RandomImpl;
import structureddata.Position;

public class Aboleth implements Monster {
  private Location location;
  private int health;

  public Aboleth(Location location) {
    this.location = location;
    location.setAboleth(true);
    this.health = 1;
  }

  protected void move() {

    List<Direction> possibleDirections = new ArrayList<>(location.getConnections().keySet());
    int random = RandomImpl.getIntInRange(0, possibleDirections.size() - 1);
    location.setAboleth(false);
    location = location.getConnections().get(possibleDirections.get(random));
    location.setAboleth(true);
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
}

package dungeonmodel;

import random.RandomImpl;
import structureddata.Position;

/**
 * Otyugh Monster that resides in caves of dungeon.
 * It throws a smell to nearby caves.
 * It requires 2 arrows to kill an Otyugh.
 * Player entering a cave having injured Otyugh can be saved 50% of times.
 * Visibility: Package - private
 */
class Otyugh implements Monster {
  private final Location location;
  private int health;

  protected Otyugh(Location location) {
    if (!location.isCave()) {
      throw new IllegalArgumentException("Otyugh cannot reside outside of Cave.");
    }
    this.location = location;
    this.health = 2;
  }

  @Override
  public Position getPosition() {
    return location.getPosition();
  }

  @Override
  public void damage() {
    if (isAlive()) {
      health--;
    }
  }

  @Override
  public boolean isAlive() {
    return health > 0;
  }

  @Override
  public GameStatus killPlayer() {
    int probability = RandomImpl.getIntInRange(1, 2);
    if (probability <= health) {
      return GameStatus.GAME_OVER_KILLED;
    }
    return GameStatus.GAME_CONTINUE;
  }
}

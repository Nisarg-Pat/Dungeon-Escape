package dungeonmodel;

import random.RandomImpl;
import structureddata.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Tunnel thief representation in which thief resides in tunnels of the dungeon.
 * After stealing from the player, thief can relocate to a different tunnel.
 * visibility: package-private
 */
class TunnelThief implements Thief {
  private Location location;

  private final Map<Treasure, Integer> stolenTreasure;

  protected TunnelThief(Location location) {
    if (location == null || location.isCave()) {
      throw new IllegalArgumentException("Location cannot be null or cave.");
    }
    this.location = location;
    location.setThief(true);
    this.stolenTreasure = new TreeMap<>();
  }

  @Override
  public void steal(Player player) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null.");
    }
    List<Treasure> treasures = new ArrayList<>(player.getCollectedTreasures().keySet());
    if (treasures.size() > 0) {
      int index = RandomImpl.getIntInRange(0, treasures.size() - 1);
      Treasure treasure = treasures.get(index);
      int removedTreasure = player.removeTreasure(treasure, 5);
      stolenTreasure.put(treasures.get(index),
              stolenTreasure.getOrDefault(treasure, 0) + removedTreasure);
    }
  }

  @Override
  public void relocate(Location location) {
    if (location == null || location.isCave()) {
      throw new IllegalArgumentException("Location cannot be null or cave.");
    }
    this.location.setThief(false);
    this.location = location;
    location.setThief(true);
  }

  @Override
  public Position getPosition() {
    return location.getPosition();
  }
}

package dungeonmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import random.RandomImpl;

class TunnelThief implements Thief {
  Location location;

  Map<Treasure, Integer> stolenTreasure;

  TunnelThief(Location location) {
    this.location = location;
    location.setThief(true);
    this.stolenTreasure = new TreeMap<>();
  }

  @Override
  public void steal(Player player) {
    List<Treasure> treasures = new ArrayList<>(player.getCollectedTreasures().keySet());
    if (treasures.size() > 0) {
      int index = RandomImpl.getIntInRange(0, treasures.size()-1);
      Treasure treasure = treasures.get(index);
      int removedTreasure = player.removeTreasure(treasure, 5);
      stolenTreasure.put(treasures.get(index), stolenTreasure.getOrDefault(treasure, 0) + removedTreasure);
    } else {
      throw new IllegalStateException("Player does not have any treasure to steal.");
    }
  }

  @Override
  public void relocate(Location location) {
    this.location.setThief(false);
    this.location = location;
    location.setThief(true);
  }
}

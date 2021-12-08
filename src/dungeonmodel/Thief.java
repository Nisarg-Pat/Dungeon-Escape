package dungeonmodel;

import structureddata.Position;

interface Thief {
  void steal(Player player);

  void relocate(Location location);

  Position getPosition();
}

package dungeonmodel;

import structureddata.Position;

/**
 * A thief representation in the dungeon.
 * Thief can steal treasure from Player.
 * Thief can relocate to a different location in the dungeon.
 *
 * Visibility: package private
 */
interface Thief {
  void steal(Player player);

  void relocate(Location location);

  Position getPosition();
}

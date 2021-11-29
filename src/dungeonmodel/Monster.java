package dungeonmodel;

import structureddata.Position;

/**
 * Represents a Monster in the Dungeon.
 * Monster can be killed by Player with arrows.
 * Visibility: Package - private
 */
interface Monster {
  Position getPosition();

  void damage();

  boolean isAlive();

  GameStatus killPlayer();
}

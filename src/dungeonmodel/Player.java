package dungeonmodel;

import java.util.Map;

/**
 * A player representation in the dungeon model.
 * Player can pick the Treasures and Arrows present at the current location.
 * Player can also move to the locations which are connected to the current location.
 * Player can Shoot the arrows in a particular direction and distance.
 * Visibility: Package - private
 */
interface Player {
  Map<Treasure, Integer> getCollectedTreasures();

  int countArrows();

  Location getLocation();

  Location movePlayer(Direction direction);

  void pickOneTreasure(Treasure treasure);

  void pickOneArrow();

  HitStatus shootArrow(Direction direction, int distance);

  SmellLevel detectSmell();

  void killMonster(Aboleth aboleth);
}

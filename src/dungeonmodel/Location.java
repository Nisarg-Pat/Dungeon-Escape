package dungeonmodel;

import structureddata.Position;

import java.util.Map;

/**
 * Represents a location in the dungeon.
 * Each location can be connected to 4 other locations.
 * One in each direction.
 * Visibility: Package - private
 */
interface Location {
  Position getPosition();

  Map<Direction, Location> getConnections();

  Map<Treasure, Integer> getTreasureMap();

  int countArrows();

  boolean isCave();

  boolean containsOtyugh();

  boolean isVisited();

  void setVisited(boolean visited);

  Monster getOtyugh();

  boolean hasEdge(Location location);

  Location addPath(Location other);

  void updatePath(Location other);

  void setTreasure();

  void setArrow();

  void addOtyugh();

  void removeTreasure(Treasure treasure, int amount);

  void removeArrow(int amount);

  SmellLevel getSmellLevel();

  HitStatus shootArrow(Direction direction, int distance);

  HitStatus shootArrowHelper(Direction direction, int distance);

  Direction getDirection(Position otherPosition);

  void setKey(boolean key);

  boolean hasKey();

  void setThief(boolean hasThief);

  boolean hasThief();

  void setAboleth(boolean hasAboleth);

  boolean hasAboleth();

  boolean hasPit();

  void setPit(boolean pit);

  boolean hasPitNearby();
}

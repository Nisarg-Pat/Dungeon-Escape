package dungeonmodel;

import structureddata.Position;

import java.util.List;

/**
 * Representation of a Graph of locations in the dungeon.
 * Each location in the graph can have 4 connections.
 * Visibility: Package - private
 */
interface LocationGraph {
  int getRows();

  int getColumns();

  boolean isWrapped();

  int countOtyughs();

  int getDegree();

  int getPercentageItems();

  int countAboleth();

  int countThief();

  Location getLocation(Position position);

  boolean hasEdge(Position first, Position second);

  List<Location> getStartEndPositions();

  void addOtyughToCaves(int numOtyugh, Location startLocation, Location endLocation);

  void addAbolethToRandomLocation(Location startLocation);

  int getDistance(Position first, Position second);

  void moveAboleth();

  Monster getAboleth(Location location);

  Thief getThief(Location newLocation);

  boolean stealTreasure(Player player);

  boolean requireKey();

  int countPits();
}

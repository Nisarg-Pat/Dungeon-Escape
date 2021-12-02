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

  Location getLocation(Position position);

  boolean hasEdge(Position first, Position second);

  List<Location> getStartEndPositions();

  void addOtyughToCaves(int numOtyugh, Location startLocation, Location endLocation);

  int getDistance(Position first, Position second);
}

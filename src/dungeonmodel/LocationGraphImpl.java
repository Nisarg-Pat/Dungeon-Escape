package dungeonmodel;

import random.RandomImpl;
import structureddata.DistanceData;
import structureddata.Edge;
import structureddata.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * The representation of LocationGraph specific to the requirements of the dungeon.
 * Random edges are selected from the possible edges such that all locations are connected
 * and the graph has the mentioned degree of interconnectivity.
 * Random caves are chosen to contain treasures by mentioned percentage of caves.
 * Random locations are chosen to contain arrows.
 * Random caves are chosen to contain Otyughs.
 * An Otyugh will be present in the end cave.
 * Visibility: Package - private
 */
class LocationGraphImpl implements LocationGraph {

  private final Location[][] location;
  private final int rows;
  private final int columns;
  private final boolean isWrapped;
  private final int degreeOfInterconnectivity;
  private final int percentageItems;
  private int numOtyughs;

  protected LocationGraphImpl(int rows, int columns, boolean isWrapped,
                              int degree, int percentageItems) {
    checkArguments(rows, columns, degree, percentageItems);
    this.rows = rows;
    this.columns = columns;
    this.isWrapped = isWrapped;
    this.degreeOfInterconnectivity = degree;
    this.percentageItems = percentageItems;
    location = addLocations();
    List<Edge> allEdges = addEdges();
    allEdges = shuffle(allEdges);
    selectEdges(allEdges);
    addTreasureToCaves();
    addArrowsToLocations();
  }

  private void checkArguments(int rows, int columns,
                              int degreeOfInterconnectivity, int percentageCavesWithTreasure) {
    if (rows < 6 || columns < 6) {
      throw new IllegalArgumentException("Rows and columns should be minimum 6.");
    } else if (degreeOfInterconnectivity < 0) {
      throw new IllegalArgumentException("Degree of interconnectivity should be >= 0.");
    } else if (percentageCavesWithTreasure < 0 || percentageCavesWithTreasure > 100) {
      throw new IllegalArgumentException(
              "Percentage of caves with treasure should be between 0 and 100.");
    }
  }

  private Location[][] addLocations() {
    Location[][] location = new Location[rows][columns];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        location[i][j] = new Cave(i, j);
      }
    }
    return location;
  }

  private List<Edge> addEdges() {
    List<Edge> edges = new ArrayList<>();
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        addEdge(edges, i, j, i, j + 1);
        addEdge(edges, i, j, i + 1, j);
      }
    }
    return edges;
  }

  private void addEdge(List<Edge> edges, int row1, int column1, int row2, int column2) {
    if (isWrapped) {
      edges.add(new Edge(row1, column1, (row2 + rows) % rows, (column2 + columns) % columns));
    } else if (row2 >= 0 && row2 < rows && column2 >= 0 && column2 < columns) {
      edges.add(new Edge(row1, column1, row2, column2));
    }
  }

  private List<Edge> shuffle(List<Edge> allEdges) {
    int totalEdges = allEdges.size();
    List<Edge> shuffledEdges = new ArrayList<>();
    while (!allEdges.isEmpty()) {
      int index = RandomImpl.getIntInRange(0, totalEdges - 1);
      totalEdges--;
      shuffledEdges.add(allEdges.remove(index));
    }
    return shuffledEdges;
  }

  private void selectEdges(List<Edge> allEdges) {
    List<List<Position>> parent = new ArrayList<>();
    for (int i = 0; i < rows; i++) {
      parent.add(new ArrayList<>());
      for (int j = 0; j < columns; j++) {
        parent.get(i).add(new Position(i, j));
      }
    }
    List<Edge> remainingEdges = new ArrayList<>();
    for (Edge edge : allEdges) {
      Position x = find(parent, edge.getFirst());
      Position y = find(parent, edge.getSecond());
      if (x.equals(y)) {
        remainingEdges.add(edge);
      } else {
        connectLocations(edge.getFirst(), edge.getSecond());
        union(parent, x, y);
      }
    }
    int max_degree = Math.min(degreeOfInterconnectivity, remainingEdges.size());
    for (int i = 0; i < max_degree; i++) {
      Edge edge = remainingEdges.get(i);
      connectLocations(edge.getFirst(), edge.getSecond());
    }
  }

  private void union(List<List<Position>> parent, Position x, Position y) {
    Position parent_a = find(parent, x);
    Position parent_b = find(parent, y);
    if (!parent_a.equals(parent_b)) {
      parent.get(parent_a.getRow()).set(parent_a.getColumn(), parent_b);
    }
  }

  private Position find(List<List<Position>> parent, Position a) {
    if (!parent.get(a.getRow()).get(a.getColumn()).equals(a)) {
      parent.get(a.getRow()).set(a.getColumn(),
              find(parent, parent.get(a.getRow()).get(a.getColumn())));
    }
    return parent.get(a.getRow()).get(a.getColumn());
  }

  private void connectLocations(Position a, Position b) {
    location[a.getRow()][a.getColumn()] =
            location[a.getRow()][a.getColumn()].addPath(location[b.getRow()][b.getColumn()]);
    location[b.getRow()][b.getColumn()] =
            location[b.getRow()][b.getColumn()].addPath(location[a.getRow()][a.getColumn()]);
  }

  @Override
  public int getRows() {
    return rows;
  }

  @Override
  public int getColumns() {
    return columns;
  }

  @Override
  public boolean isWrapped() {
    return isWrapped;
  }

  @Override
  public int countOtyughs() {
    return numOtyughs;
  }

  @Override
  public Location getLocation(Position position) {
    if (position == null) {
      throw new IllegalArgumentException("Invalid Position.");
    }
    return location[position.getRow()][position.getColumn()];
  }

  @Override
  public boolean hasEdge(Position first, Position second) {
    if (first == null || second == null) {
      throw new IllegalArgumentException("Invalid location.");
    }
    return location[first.getRow()][first.getColumn()].hasEdge(
            location[second.getRow()][second.getColumn()]);
  }

  @Override
  public List<Location> getStartEndPositions() {
    List<Location> caves = getListOfCaves();
    Position startPosition = caves.get(RandomImpl.getIntInRange(0, caves.size() - 1)).getPosition();
    List<Location> possibleEndPositions = possibleEndPositions(startPosition);
    if (possibleEndPositions.isEmpty()) {
      throw new IllegalStateException("Cannot find a distance of 5 between two positions.");
    }
    Location endPosition = possibleEndPositions.get(
            RandomImpl.getIntInRange(0, possibleEndPositions.size() - 1));
    List<Location> startEndPositions = new ArrayList<>();
    startEndPositions.add(location[startPosition.getRow()][startPosition.getColumn()]);
    startEndPositions.add(
            location[endPosition.getPosition().getRow()][endPosition.getPosition().getColumn()]);
    return startEndPositions;
  }

  private List<Location> possibleEndPositions(Position startPosition) {
    if (startPosition == null) {
      throw new IllegalArgumentException("Invalid Position");
    }
    List<Location> locationsWithDistanceGreaterThanEqualTo5 = new ArrayList<>();
    boolean[][] visited = new boolean[rows][columns];
    Queue<DistanceData<Position>> q = new LinkedList<>();
    q.add(new DistanceData<>(startPosition, startPosition, 0));
    visited[startPosition.getRow()][startPosition.getColumn()] = true;
    while (!q.isEmpty()) {
      DistanceData<Position> distanceData = q.poll();
      Position endPosition = distanceData.getEnd();
      if (distanceData.getDistance() >= 5
              && location[endPosition.getRow()][endPosition.getColumn()].isCave()) {
        locationsWithDistanceGreaterThanEqualTo5.add(
                location[endPosition.getRow()][endPosition.getColumn()]);
      }
      visitConnections(startPosition, visited, q, distanceData, endPosition);
    }
    return locationsWithDistanceGreaterThanEqualTo5;
  }


  private void visitConnections(Position startPosition, boolean[][] visited,
                                Queue<DistanceData<Position>> q,
                                DistanceData<Position> distanceData, Position endPosition) {
    for (Location location :
            location[endPosition.getRow()][endPosition.getColumn()].getConnections().values()) {
      if (!visited[location.getPosition().getRow()][location.getPosition().getColumn()]) {
        visited[location.getPosition().getRow()][location.getPosition().getColumn()] = true;
        q.add(new DistanceData<>(startPosition,
                location.getPosition(), distanceData.getDistance() + 1));
      }
    }
  }

  private void addTreasureToCaves() {
    List<Location> caves = getListOfCaves();
    int totalCaves = caves.size();
    int totalTreasureCaves =
            (int) Math.ceil((percentageItems * totalCaves) / 100.0);
    for (int i = 0; i < totalTreasureCaves; i++) {
      int index = RandomImpl.getIntInRange(0, totalCaves - 1);
      totalCaves--;
      caves.get(index).setTreasure();
      caves.remove(index);
    }
  }

  private void addArrowsToLocations() {
    List<Location> locations = getListOfLocations();
    int totalLocations = locations.size();
    int totalArrowLocations =
            (int) Math.ceil((percentageItems * totalLocations) / 100.0);
    for (int i = 0; i < totalArrowLocations; i++) {
      int index = RandomImpl.getIntInRange(0, totalLocations - 1);
      totalLocations--;
      locations.get(index).setArrow();
      locations.remove(index);
    }
  }

  @Override
  public void addOtyughToCaves(int numOtyugh, Location startLocation, Location endLocation) {
    List<Location> caves = getListOfCaves();
    caves.remove(endLocation);
    caves.remove(startLocation);

    int totalCaves = caves.size();
    numOtyughs = Math.min(totalCaves, numOtyugh);

    endLocation.addOtyugh();
    for (int i = 0; i < numOtyughs - 1; i++) {
      int index = RandomImpl.getIntInRange(0, totalCaves - 1);
      totalCaves--;
      caves.get(index).addOtyugh();
      caves.remove(index);
    }
  }

  private List<Location> getListOfCaves() {
    List<Location> caves = new ArrayList<>();
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        if (location[i][j].isCave()) {
          caves.add(location[i][j]);
        }
      }
    }
    return caves;
  }

  private List<Location> getListOfLocations() {
    List<Location> locations = new ArrayList<>();
    for (int i = 0; i < rows; i++) {
      locations.addAll(Arrays.asList(location[i]).subList(0, columns));
    }
    return locations;
  }

  @Override
  public int getDistance(Position first, Position second) {
    boolean[][] visited = new boolean[rows][columns];
    Queue<DistanceData<Position>> q = new LinkedList<>();
    q.add(new DistanceData<>(first, first, 0));
    visited[first.getRow()][first.getColumn()] = true;
    while (!q.isEmpty()) {
      DistanceData<Position> distanceData = q.poll();
      Position endPosition = distanceData.getEnd();
      if (second.equals(endPosition)) {
        return distanceData.getDistance();
      }
      visitConnections(first, visited, q, distanceData, endPosition);
    }
    return -1;
  }
}

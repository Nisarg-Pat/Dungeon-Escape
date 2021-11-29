package structureddata;

/**
 * Structured data to store an Edge between two locations.
 */
public class Edge {
  private final Position first;
  private final Position second;

  /**
   * Constructor for Edge class.
   *
   * @param firstRow    row for first location
   * @param firstColumn column for first location
   * @param secondRow    row for second location
   * @param secondColumn column for second location
   */
  public Edge(int firstRow, int firstColumn, int secondRow, int secondColumn) {
    if (firstRow < 0 || firstColumn < 0 || secondRow < 0 || secondColumn < 0) {
      throw new IllegalArgumentException("Invalid arguments.");
    }
    this.first = new Position(firstRow, firstColumn);
    this.second = new Position(secondRow, secondColumn);
  }

  /**
   * Gives the first position of the edge.
   *
   * @return the first position of the edge.
   */
  public Position getFirst() {
    return first;
  }

  /**
   * Gives the second position of the edge.
   *
   * @return the second position of the edge.
   */
  public Position getSecond() {
    return second;
  }

  @Override
  public String toString() {
    return String.format("(%d, %d) - (%d, %d)",
            first.getRow(), first.getColumn(), second.getRow(), second.getColumn());
  }
}

package structureddata;

import java.util.Objects;

/**
 * Data structure to store a position. Contains 2 integers: row and column.
 */
public class Position {
  private final int row;
  private final int column;

  /**
   * Constructor for Position class.
   *
   * @param row    row of the position
   * @param column column of the position
   * @throws IllegalArgumentException if rows or columns are < 0.
   */
  public Position(int row, int column) {
    if (row < 0 || column < 0) {
      throw new IllegalArgumentException("Invalid arguments.");
    }
    this.row = row;
    this.column = column;
  }

  /**
   * Gives the row of the position.
   *
   * @return the row of the position.
   */
  public int getRow() {
    return row;
  }

  /**
   * Gives the column of the position.
   *
   * @return the column of the position.
   */
  public int getColumn() {
    return column;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Position)) {
      return false;
    }
    Position position = (Position) o;
    return row == position.row && column == position.column;
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, column);
  }

  @Override
  public String toString() {
    return String.format("(%d, %d)", row, column);
  }
}

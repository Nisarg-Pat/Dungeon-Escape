package structureddata;

import java.util.Objects;

/**
 * Structured Data to store the distance between 2 positions.
 */
public class DistanceData<T> {
  private final T start;
  private final T end;
  private final int distance;

  /**
   * Constructor for Distance data class.
   *
   * @param start    starting point
   * @param end      ending point
   * @param distance distance between the two position/
   */
  public DistanceData(T start, T end, int distance) {
    if (start == null || end == null) {
      throw new IllegalArgumentException("Invalid arguments");
    }
    this.start = start;
    this.end = end;
    this.distance = distance;
  }

  /**
   * Gives the start point.
   *
   * @return the start point.
   */
  public T getStart() {
    return start;
  }

  /**
   * Gives the end point.
   *
   * @return the end point.
   */
  public T getEnd() {
    return end;
  }

  /**
   * Gives the distance between the two points.
   *
   * @return the distance between the two points.
   */
  public int getDistance() {
    return distance;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DistanceData<?>)) {
      return false;
    }
    DistanceData<?> that = (DistanceData<?>) o;
    return distance == that.distance && start.equals(that.start) && end.equals(that.end);
  }

  @Override
  public int hashCode() {
    return Objects.hash(start, end, distance);
  }
}

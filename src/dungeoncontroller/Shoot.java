package dungeoncontroller;

import dungeonmodel.Direction;
import dungeonmodel.DungeonModel;
import dungeonmodel.HitStatus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * A Shoot {@link Command} to let player shoot a crooked arrow
 * in the mentioned direction and distance.
 * Visibility: Package - private
 */
class Shoot implements Command {
  private final Scanner sc;
  private final Appendable out;
  private Direction direction;
  private int distance;

  protected Shoot(Scanner sc, Appendable out) throws IOException {
    this.sc = sc;
    this.out = out;
    takeInput();
  }

  private void takeInput() throws IOException {
    Map<String, Direction> directionMap = new HashMap<>();
    directionMap.put("N", Direction.NORTH);
    directionMap.put("S", Direction.SOUTH);
    directionMap.put("E", Direction.EAST);
    directionMap.put("W", Direction.WEST);

    out.append("Which direction ( N | E | S | W ) : ");
    String direction = sc.next().toUpperCase();
    while (!directionMap.containsKey(direction)) {
      out.append(String.format("\nDirection: %s is not a valid direction\n", direction));
      out.append("Which direction ( N | E | S | W ) : ");
      direction = sc.next().toUpperCase();
    }
    this.direction = directionMap.get(direction);
    out.append("Enter distance (1-5) : ");
    String distanceString = sc.next();
    distance = Integer.MIN_VALUE;
    while (distance == Integer.MIN_VALUE) {
      try {
        distance = Integer.parseInt(distanceString);
      } catch (IllegalArgumentException e) {
        out.append("\n").append(distanceString).append(" is not a valid integer.\n");
        out.append("Enter distance (1-5) : ");
        distanceString = sc.next();
      }
    }
  }

  @Override
  public void execute(DungeonModel model) {
    HitStatus status = model.shoot(direction, distance);
    try {
      if (status == HitStatus.KILLED) {
        out.append("You hear a great howl in the distance.\n");
        out.append("The howl ended as it was slayed.\n");
      } else if (status == HitStatus.HIT) {
        out.append("You hear a great howl in the distance.\n");
      } else if (status == HitStatus.MISS) {
        out.append("You shoot an arrow into the darkness.\n");
      }
      if (model.getPlayerDescription().countArrows() == 0) {
        out.append("You are out of arrows, explore to find more\n");
      }
    } catch (IOException e) {
      throw new IllegalStateException("Append Failed", e);
    }
  }
}

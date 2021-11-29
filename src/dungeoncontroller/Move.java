package dungeoncontroller;

import dungeonmodel.Direction;
import dungeonmodel.DungeonModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * A Move {@link Command} to let player move to a connected location from his current location.
 * Visibility: Package - private
 */
class Move implements Command {
  private final Scanner sc;
  private final Appendable out;
  private Direction direction;

  protected Move(Scanner sc, Appendable out) throws IOException {
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

    out.append("Enter a direction ( N | E | S | W ) : ");
    String direction = sc.next().toUpperCase();
    while (!directionMap.containsKey(direction)) {
      out.append(String.format("\nDirection: %s is not a valid direction\n", direction));
      out.append("Enter a direction ( N | E | S | W ) : ");
      direction = sc.next().toUpperCase();
    }
    this.direction = directionMap.get(direction);
  }

  @Override
  public void execute(DungeonModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Invalid model for Command!");
    }
    model.movePlayer(direction);
  }
}

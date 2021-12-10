package dungeoncontroller;

import dungeonmodel.Arrow;
import dungeonmodel.DungeonModel;
import dungeonmodel.Item;
import dungeonmodel.Treasure;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * A Pick {@link Command} to let player pick an item from the current location.
 * Visibility: Package - private
 */
class Pick implements Command {
  private final Scanner sc;
  private final Appendable out;
  private Item item;

  protected Pick(Scanner sc, Appendable out) throws IOException {
    if (sc == null || out == null) {
      throw new IllegalArgumentException("Scanner or Appendable cannot be null.");
    }
    this.sc = sc;
    this.out = out;
    takeInput();
  }

  private void takeInput() throws IOException {
    Map<String, Item> possibleItems = new HashMap<>();
    possibleItems.put("D", Treasure.DIAMOND);
    possibleItems.put("R", Treasure.RUBY);
    possibleItems.put("S", Treasure.SAPPHIRE);
    possibleItems.put("A", Arrow.CROOKED_ARROW);

    out.append("What to pick : ( D | R | S | A ) : ");
    String item = sc.next().toUpperCase();
    while (!possibleItems.containsKey(item)) {
      out.append(String.format("\nItem %s is not recognised\n", item));
      out.append("What to pick : ( D | R | S | A ) : ");
      item = sc.next().toUpperCase();
    }
    this.item = possibleItems.get(item);
  }

  @Override
  public void execute(DungeonModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Invalid model for Command!");
    }
    model.pickItem(item);
  }
}

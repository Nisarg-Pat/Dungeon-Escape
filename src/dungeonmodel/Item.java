package dungeonmodel;

/**
 * General Item representation that can be picked by player.
 */
public interface Item {
  /**
   * The string representation of 1 item.
   *
   * @return the singular string.
   */
  String getSingular();

  /**
   * The string representation of more than 1 items.
   *
   * @return the plural string.
   */
  String getPlural();

  /**
   * Get the string representation depending on number of items.
   * @param number number of items.
   * @return the string representation depending on number of items.
   */
  String getStringFromNumber(int number);
}

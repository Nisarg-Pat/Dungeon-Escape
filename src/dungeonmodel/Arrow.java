package dungeonmodel;

/**
 * Enum representation of Arrows. Currently, only one type of arrow: CROOKED_ARROW.
 * Arrows are {@link Item}s that can be picked by player.
 */
public enum Arrow implements Item {
  CROOKED_ARROW;

  @Override
  public String getSingular() {
    return "Arrow";
  }

  @Override
  public String getPlural() {
    return "Arrows";
  }

  @Override
  public String getStringFromNumber(int number) {
    if (number == 1) {
      return getSingular();
    } else {
      return getPlural();
    }
  }

  @Override
  public String toString() {
    return getSingular();
  }
}

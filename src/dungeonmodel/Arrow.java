package dungeonmodel;

/**
 * Enum representation of Arrows. Currenlty only one type of arrow: CROOKED_ARROW.
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
  public String toString() {
    return getSingular();
  }
}

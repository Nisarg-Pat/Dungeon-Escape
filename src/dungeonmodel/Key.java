package dungeonmodel;

/**
 * Enum representation of Key. Currently, only one type of key: DOOR_KEY.
 * Keys are {@link Item}s that can be picked by player.
 */
public enum Key implements Item {
  DOOR_KEY;

  @Override
  public String getSingular() {
    return "Key";
  }

  @Override
  public String getPlural() {
    return "Keys";
  }

  @Override
  public String getStringFromNumber(int number) {
    if (number == 1) {
      return getSingular();
    } else {
      return getPlural();
    }
  }
}

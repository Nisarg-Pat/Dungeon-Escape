package dungeonmodel;

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

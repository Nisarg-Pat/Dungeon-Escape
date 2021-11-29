package dungeonmodel;

/**
 * The Enum for Smell Level of a location detected by the player.
 * Can be: LESS_PUNGENT, MORE_PUNGENT or NO_SMELL
 */
public enum SmellLevel {
  LESS_PUNGENT, MORE_PUNGENT, NO_SMELL;

  @Override
  public String toString() {
    switch (this) {
      case LESS_PUNGENT:
        return "Less pungent smell.";
      case MORE_PUNGENT:
        return "More pungent smell.";
      default:
        return "No smell.";
    }
  }
}

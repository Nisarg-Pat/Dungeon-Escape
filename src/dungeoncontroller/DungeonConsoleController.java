package dungeoncontroller;

import dungeonmodel.Arrow;
import dungeonmodel.Direction;
import dungeonmodel.DungeonModel;
import dungeonmodel.GameStatus;
import dungeonmodel.SmellLevel;
import dungeonmodel.Treasure;
import structureddata.LocationDescription;
import structureddata.PlayerDescription;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

/**
 * A Console representation of {@link DungeonController}.
 * The input of this controller is a {@link Readable}.
 * The output of this controller is a {@link Appendable}.
 */
public class DungeonConsoleController implements DungeonController {

  private final Readable in;
  private final Appendable out;
  private final DungeonModel model;

  /**
   * Creates an Object of DungeonControllerImpl which can be used to play the dungeon game.
   *
   * @param in    The Readable from which to take the input.
   * @param out   The Appendable to which to give the output.
   * @param model The {@link DungeonModel} to play the game.
   * @throws IllegalArgumentException if any of the arguments is null.
   */
  public DungeonConsoleController(Readable in, Appendable out, DungeonModel model) {
    if (in == null || out == null || model == null) {
      throw new IllegalArgumentException("The arguments to controller cannot be null.");
    }
    this.in = in;
    this.out = out;
    this.model = model;
  }

  @Override
  public void start() {
    try {
      Scanner sc = new Scanner(this.in);
      boolean appendLocation = true;
      GameStatus status = model.getGameStatus();
      describeDungeon();
      while (status == GameStatus.GAME_CONTINUE) {
        if (appendLocation) {
          describeLocation();
          appendSmell(model.detectSmell());
          if (model.getCurrentLocation().containsOtyugh()
                  && model.getGameStatus() == GameStatus.GAME_CONTINUE) {
            out.append("The Otyugh in the cave is too weak to attack!\nGet out of here ASAP!!\n");
          }
        }
        appendLocation = true;
        out.append("\nMove, Pick, Shoot, Describe ( M | P | S | D ) : ");
        String command = sc.next().toUpperCase();
        try {
          switch (command) {
            case "M":
              new Move(sc, out).execute(model);
              break;
            case "P":
              new Pick(sc, out).execute(model);
              break;
            case "S":
              new Shoot(sc, out).execute(model);
              break;
            case "D":
              appendPlayerDescription();
              appendLocation = false;
              break;
            case "PRINT":
              //A cheat command to print the dungeon.
              //Normal game does not contain any information about it.
              out.append(model.printDungeon());
              break;
            case "Q":
              status = GameStatus.GAME_OVER_QUIT;
              break;
            default:
              out.append("\n").append("Invalid command: ").append(command).append("\n");
              appendLocation = false;
              break;
          }
        } catch (IllegalArgumentException | IllegalStateException e) {
          out.append("\n").append(e.getMessage()).append("\n");
        }
        if (status != GameStatus.GAME_OVER_QUIT) {
          status = model.getGameStatus();
        }
      }
      if (status == GameStatus.GAME_OVER_QUIT) {
        out.append("\nGame over.\nYou quit the game. You Lose.\n");
      } else if (status == GameStatus.GAME_OVER_KILLED) {
        out.append("\nChomp, chomp, chomp, you are eaten by an Otyugh!\nYou Lose.\n");
      } else if (status == GameStatus.GAME_OVER_WIN) {
        out.append("\nYou reached the end location.\nCongratulations You Win!!\n");
        out.append("You collected ");
        appendTreasures(model.getPlayerDescription().getCollectedTreasures());
        out.append(" from your adventure!!\n");
      }
    } catch (IOException e) {
      throw new IllegalStateException("Append Failed.");
    } catch (NoSuchElementException e) {
      throw new IllegalStateException("Read Failed.");
    }
  }

  private void describeDungeon() throws IOException {
    out.append("Welcome to the Dungeon Game\n");
    out.append("Following dungeon is created for you: \n");
    out.append("Rows: ").append(String.valueOf(model.getRows())).append("\n");
    out.append("Columns: ").append(String.valueOf(model.getColumns())).append("\n");
    out.append("IsWrapped: ").append(String.valueOf(model.getWrapped())).append("\n");
    out.append("Number of Oyughs: ").append(String.valueOf(model.countOtyughs())).append("\n\n");
    out.append("Commands:\n");
    out.append("M: Move the player in the dungeon.\n");
    out.append("P: Pick an item in the dungeon.\n");
    out.append("S: Shoot a crooked arrow.\n");
    out.append("D: Description of the player.\n");
    out.append("Q: Quit the game.\n");
    out.append("\n");
  }

  private void describeLocation() throws IOException {
    LocationDescription description = model.getCurrentLocation();
    out.append("\nYou are in a ").append(description.isCave() ? "Cave" : "Tunnel").append("\n");
    appendDirections(description.getPossibleDirections());
    appendLocationTreasures(description.getTreasureMap());
    appendArrows(description.countArrows());
  }

  private void appendDirections(Set<Direction> directions) throws IOException {
    if (directions == null) {
      throw new IllegalArgumentException("Directions cannot be null.");
    }
    List<Direction> possibleDirections = new ArrayList<>(directions);
    int totaldirections = possibleDirections.size();
    if (!directions.isEmpty()) {
      out.append("Doors lead to : ");
      out.append(possibleDirections.get(0).toString());
      for (int i = 1; i < totaldirections; i++) {
        out.append(", ").append(possibleDirections.get(i).toString());
      }
      out.append("\n");
    }
  }

  private void appendLocationTreasures(Map<Treasure, Integer> treasureMap) throws IOException {
    if (treasureMap == null) {
      throw new IllegalArgumentException("treasureMap cannot be null.");
    }
    if (!treasureMap.isEmpty()) {
      out.append("You find ");
      appendTreasures(treasureMap);
      out.append(" here.\n");
    }
  }

  private void appendTreasures(Map<Treasure, Integer> treasureMap) throws IOException {
    if (treasureMap == null) {
      throw new IllegalArgumentException("treasureMap cannot be null.");
    }
    List<Treasure> treasures = new ArrayList<>(treasureMap.keySet());
    for (int i = 0; i < treasures.size(); i++) {
      Treasure treasure = treasures.get(i);
      int value = treasureMap.get(treasure);
      if (i != 0) {
        out.append(", ");
      }
      out.append(String.format("%d %s", value,
              value == 1 ? treasure.getSingular()
                      : treasure.getPlural()));
    }
  }

  private void appendSmell(SmellLevel smellLevel) throws IOException {
    if (smellLevel == SmellLevel.LESS_PUNGENT) {
      out.append("You smell something Less Pungent.\n");
    } else if (smellLevel == SmellLevel.MORE_PUNGENT) {
      out.append("You smell something More Pungent.\n");
    }
  }


  private void appendArrows(int numArrows) throws IOException {
    if (numArrows > 0) {
      out.append(String.format("You find %d %s\n", numArrows,
              numArrows == 1 ? Arrow.CROOKED_ARROW.getSingular() :
                      Arrow.CROOKED_ARROW.getPlural()));
    }
  }

  private void appendPlayerDescription() throws IOException {
    PlayerDescription player = model.getPlayerDescription();
    out.append("\nPlayer Name is PacMan.\n");

    appendPlayerTreasures(player.getCollectedTreasures());
    appendPlayerArrows(player.countArrows());
  }

  private void appendPlayerArrows(int numArrows) throws IOException {
    if (numArrows == 0) {
      out.append("Player has no arrows left.\n");
    } else {
      out.append(String.format("Player has %d %s.\n", numArrows,
              numArrows == 1 ? Arrow.CROOKED_ARROW.getSingular()
                      : Arrow.CROOKED_ARROW.getPlural()));
    }
  }

  private void appendPlayerTreasures(Map<Treasure, Integer> collectedTreasures) throws IOException {
    if (collectedTreasures == null) {
      throw new IllegalArgumentException("collectedTreasures cannot be null.");
    }
    if (collectedTreasures.isEmpty()) {
      out.append("Player has not collected any treasures.\n");
    } else {
      out.append("Player has collected ");
      appendTreasures(collectedTreasures);
      out.append(".\n");
    }
  }
}

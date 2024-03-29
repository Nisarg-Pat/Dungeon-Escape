import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

import dungeoncontroller.DungeonController;
import dungeoncontroller.DungeonConsoleController;
import dungeonmodel.DungeonModel;
import dungeonmodel.DungeonModelImpl;
import random.RandomImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests to check the methods of {@link DungeonController}. Covers all the different types
 * of scenarios that could occur in a dungeon controller.
 */
public class DungeonConsoleControllerTest {

  private DungeonModel model;

  @Before
  public void setup() {
    RandomImpl.setSeed(0);
    model = new DungeonModelImpl(6, 8, false, 10, 50, 10);
  }

  @Test
  public void testConstructorFail() {
    Readable in = new StringReader(" ");
    Appendable out = new StringBuilder();


    try {
      new DungeonConsoleController(null, out, model);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("The arguments to controller cannot be null.", e.getMessage());
    }

    try {
      new DungeonConsoleController(in, null, model);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("The arguments to controller cannot be null.", e.getMessage());
    }

    try {
      new DungeonConsoleController(in, out, null);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("The arguments to controller cannot be null.", e.getMessage());
    }
  }

  @Test
  public void testFailingAppendable() {
    Readable in = new StringReader("");
    Appendable failing = new FailingAppendable();
    DungeonController controller = new DungeonConsoleController(in, failing, model);
    try {
      controller.start();
      fail();
    } catch (IllegalStateException e) {
      assertEquals("Append Failed.", e.getMessage());
    }
  }

  @Test
  public void testFailingReadable() {
    Readable failing = new StringReader("");
    Appendable out = new StringBuilder();
    DungeonController controller = new DungeonConsoleController(failing, out, model);
    try {
      controller.start();
      fail();
    } catch (IllegalStateException e) {
      assertEquals("Read Failed.", e.getMessage());
    }
  }

  @Test
  public void testMockModelLogging() {
    //Assures model gets proper values from the controller.
    Readable in =
            new StringReader("M N M E M S M W P D P R P S P A S N 1 S E 2 S S 3 S W 4 S N 5 Q");
    Appendable out = new StringBuilder();
    StringBuilder log = new StringBuilder();
    DungeonModel loggingModel = new MockModelLogging(log);
    DungeonController controller = new DungeonConsoleController(in, out, loggingModel);
    controller.start();
    assertEquals("Move: North\n"
                    + "Move: East\n"
                    + "Move: South\n"
                    + "Move: West\n"
                    + "Pick: Diamond\n"
                    + "Pick: Ruby\n"
                    + "Pick: Sapphire\n"
                    + "Pick: Arrow\n"
                    + "Shoot: Direction = North, Distance = 1\n"
                    + "Shoot: Direction = East, Distance = 2\n"
                    + "Shoot: Direction = South, Distance = 3\n"
                    + "Shoot: Direction = West, Distance = 4\n"
                    + "Shoot: Direction = North, Distance = 5\n",
            log.toString());
  }

  @Test
  public void testInvalidInput() {
    Readable in = new StringReader("abc P carrot C D P A M abc1 1 E M W P D S "
            + "anyDirection 12345 S qwerty 123abc -5 S S 0 S S 10 S S 1 S S 1 S S 1 S S 1 Q");
    Appendable out = new StringBuilder();
    DungeonController controller = new DungeonConsoleController(in, out, model);
    controller.start();
    assertEquals("Welcome to the Dungeon Game\n"
                    + "Following dungeon is created for you: \n"
                    + "Rows: 6\n"
                    + "Columns: 8\n"
                    + "IsWrapped: false\n"
                    + "Number of Oyughs: 10\n"
                    + "\n"
                    + "Commands:\n"
                    + "M: Move the player in the dungeon.\n"
                    + "P: Pick an item in the dungeon.\n"
                    + "S: Shoot a crooked arrow.\n"
                    + "D: Description of the player.\n"
                    + "Q: Quit the game.\n"
                    + "\n"
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : West\n"
                    + "You find 1 Ruby, 5 Sapphires here.\n"
                    + "You smell something Less Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : \n"
                    + "Invalid command: ABC\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "What to pick : ( D | R | S | A ) : \n"
                    + "Item CARROT is not recognised\n"
                    + "What to pick : ( D | R | S | A ) : \n"
                    + "Item C is not recognised\n"
                    + "What to pick : ( D | R | S | A ) : \n"
                    + "Location does not contain treasure: Diamond\n"
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : West\n"
                    + "You find 1 Ruby, 5 Sapphires here.\n"
                    + "You smell something Less Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "What to pick : ( D | R | S | A ) : \n"
                    + "No arrows to Pick.\n"
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : West\n"
                    + "You find 1 Ruby, 5 Sapphires here.\n"
                    + "You smell something Less Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Enter a direction ( N | E | S | W ) : \n"
                    + "Direction: ABC1 is not a valid direction\n"
                    + "Enter a direction ( N | E | S | W ) : \n"
                    + "Direction: 1 is not a valid direction\n"
                    + "Enter a direction ( N | E | S | W ) : \n"
                    + "Direction East is not possible from here.\n"
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : West\n"
                    + "You find 1 Ruby, 5 Sapphires here.\n"
                    + "You smell something Less Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Enter a direction ( N | E | S | W ) : \n"
                    + "You are in a Tunnel\n"
                    + "Doors lead to : East, South\n"
                    + "You find 2 Arrows\n"
                    + "You smell something More Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "What to pick : ( D | R | S | A ) : \n"
                    + "Location does not contain treasure: Diamond\n"
                    + "\n"
                    + "You are in a Tunnel\n"
                    + "Doors lead to : East, South\n"
                    + "You find 2 Arrows\n"
                    + "You smell something More Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Which direction ( N | E | S | W ) : \n"
                    + "Direction: ANYDIRECTION is not a valid direction\n"
                    + "Which direction ( N | E | S | W ) : \n"
                    + "Direction: 12345 is not a valid direction\n"
                    + "Which direction ( N | E | S | W ) : "
                    + "Enter distance (1-5) : \n"
                    + "qwerty is not a valid integer.\n"
                    + "Enter distance (1-5) : \n"
                    + "123abc is not a valid integer.\n"
                    + "Enter distance (1-5) : \n"
                    + "Distance should be between 1 and 5.\n"
                    + "\n"
                    + "You are in a Tunnel\n"
                    + "Doors lead to : East, South\n"
                    + "You find 2 Arrows\n"
                    + "You smell something More Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Which direction ( N | E | S | W ) : "
                    + "Enter distance (1-5) : \n"
                    + "Distance should be between 1 and 5.\n"
                    + "\n"
                    + "You are in a Tunnel\n"
                    + "Doors lead to : East, South\n"
                    + "You find 2 Arrows\n"
                    + "You smell something More Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Which direction ( N | E | S | W ) : "
                    + "Enter distance (1-5) : \n"
                    + "Distance should be between 1 and 5.\n"
                    + "\n"
                    + "You are in a Tunnel\n"
                    + "Doors lead to : East, South\n"
                    + "You find 2 Arrows\n"
                    + "You smell something More Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Which direction ( N | E | S | W ) : "
                    + "Enter distance (1-5) : "
                    + "You hear a great howl in the distance.\n"
                    + "\n"
                    + "You are in a Tunnel\n"
                    + "Doors lead to : East, South\n"
                    + "You find 2 Arrows\n"
                    + "You smell something More Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Which direction ( N | E | S | W ) : "
                    + "Enter distance (1-5) : "
                    + "You hear a great howl in the distance.\n"
                    + "The howl ended as it was slayed.\n"
                    + "\n"
                    + "You are in a Tunnel\n"
                    + "Doors lead to : East, South\n"
                    + "You find 2 Arrows\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Which direction ( N | E | S | W ) : "
                    + "Enter distance (1-5) : "
                    + "You shoot an arrow into the darkness.\n"
                    + "You are out of arrows, explore to find more\n"
                    + "\n"
                    + "You are in a Tunnel\n"
                    + "Doors lead to : East, South\n"
                    + "You find 2 Arrows\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Which direction ( N | E | S | W ) : "
                    + "Enter distance (1-5) : \n"
                    + "Player does not have any arrows.\n"
                    + "\n"
                    + "You are in a Tunnel\n"
                    + "Doors lead to : East, South\n"
                    + "You find 2 Arrows\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : \n"
                    + "Game over.\n"
                    + "You quit the game. You Lose.\n",
            out.toString());
  }

  @Test
  public void testEatenByOtyugh() {
    Readable in = new StringReader("M W M S");
    Appendable out = new StringBuilder();
    DungeonController controller = new DungeonConsoleController(in, out, model);
    controller.start();
    assertEquals("Welcome to the Dungeon Game\n"
                    + "Following dungeon is created for you: \n"
                    + "Rows: 6\n"
                    + "Columns: 8\n"
                    + "IsWrapped: false\n"
                    + "Number of Oyughs: 10\n"
                    + "\n"
                    + "Commands:\n"
                    + "M: Move the player in the dungeon.\n"
                    + "P: Pick an item in the dungeon.\n"
                    + "S: Shoot a crooked arrow.\n"
                    + "D: Description of the player.\n"
                    + "Q: Quit the game.\n"
                    + "\n"
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : West\n"
                    + "You find 1 Ruby, 5 Sapphires here.\n"
                    + "You smell something Less Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Enter a direction ( N | E | S | W ) : "
                    + "\n"
                    + "You are in a Tunnel\n"
                    + "Doors lead to : East, South\n"
                    + "You find 2 Arrows\n"
                    + "You smell something More Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Enter a direction ( N | E | S | W ) : "
                    + "\n"
                    + "Chomp, chomp, chomp, you are eaten by an Otyugh!\n"
                    + "You Lose.\n",
            out.toString());
  }

  @Test
  public void testSavedAndThenEatenByInjuredOtyugh() {
    Readable in = new StringReader("S W 2 M W M S M N M S");
    Appendable out = new StringBuilder();
    DungeonController controller = new DungeonConsoleController(in, out, model);
    controller.start();
    assertEquals("Welcome to the Dungeon Game\n"
                    + "Following dungeon is created for you: \n"
                    + "Rows: 6\n"
                    + "Columns: 8\n"
                    + "IsWrapped: false\n"
                    + "Number of Oyughs: 10\n"
                    + "\n"
                    + "Commands:\n"
                    + "M: Move the player in the dungeon.\n"
                    + "P: Pick an item in the dungeon.\n"
                    + "S: Shoot a crooked arrow.\n"
                    + "D: Description of the player.\n"
                    + "Q: Quit the game.\n"
                    + "\n"
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : West\n"
                    + "You find 1 Ruby, 5 Sapphires here.\n"
                    + "You smell something Less Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Which direction ( N | E | S | W ) : "
                    + "Enter distance (1-5) : "
                    + "You hear a great howl in the distance.\n"
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : West\n"
                    + "You find 1 Ruby, 5 Sapphires here.\n"
                    + "You smell something Less Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Enter a direction ( N | E | S | W ) : "
                    + "\n"
                    + "You are in a Tunnel\n"
                    + "Doors lead to : East, South\n"
                    + "You find 2 Arrows\n"
                    + "You smell something More Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Enter a direction ( N | E | S | W ) : "
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : North, East, West\n"
                    + "You find 6 Rubies here.\n"
                    + "The Otyugh in the cave is too weak to attack!\n"
                    + "Get out of here ASAP!!\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Enter a direction ( N | E | S | W ) : "
                    + "\n"
                    + "You are in a Tunnel\n"
                    + "Doors lead to : East, South\n"
                    + "You find 2 Arrows\n"
                    + "You smell something More Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Enter a direction ( N | E | S | W ) : "
                    + "\n"
                    + "Chomp, chomp, chomp, you are eaten by an Otyugh!\n"
                    + "You Lose.\n",
            out.toString());
  }

  @Test
  public void testMoveThroughDungeon() {
    Readable in = new StringReader("Print D P R P S P S P S P S P S M W S S 1 S S 1 P A P A "
            + "M S M E P D P D M W M W M S M S S W 1 S W 2 M W M S S W 1 M S P A P A P A D "
            + "S W 2 S W 2 M W P A M N M W P R P R P S P S P S P S P A P A S W 1 S W 1 M W");
    Appendable out = new StringBuilder();
    DungeonController controller = new DungeonConsoleController(in, out, model);
    controller.start();
    assertEquals("Welcome to the Dungeon Game\n"
                    + "Following dungeon is created for you: \n"
                    + "Rows: 6\n"
                    + "Columns: 8\n"
                    + "IsWrapped: false\n"
                    + "Number of Oyughs: 10\n"
                    + "\n"
                    + "Commands:\n"
                    + "M: Move the player in the dungeon.\n"
                    + "P: Pick an item in the dungeon.\n"
                    + "S: Shoot a crooked arrow.\n"
                    + "D: Description of the player.\n"
                    + "Q: Quit the game.\n"
                    + "\n"
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : West\n"
                    + "You find 1 Ruby, 5 Sapphires here.\n"
                    + "You smell something Less Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "                                                            \n"
                    + "    (M)    ( )    ( ) —— ( ) —— ( ) —— (M)    ( ) —— (P)    \n"
                    + "     |      |      |      |                    |            \n"
                    + "    ( ) —— ( ) —— ( )    ( )    ( ) —— ( ) —— (M) —— ( )    \n"
                    + "            |             |      |      |                   \n"
                    + "    (M)    ( ) —— (M) —— ( )    ( ) —— ( ) —— ( ) —— ( )    \n"
                    + "     |             |                    |             |     \n"
                    + "    ( )    ( ) —— (M) —— ( )    ( ) —— ( ) —— (M) —— ( )    \n"
                    + "     |      |             |      |      |      |      |     \n"
                    + "    ( ) —— (&) —— ( ) —— (M) —— ( )    ( ) —— ( ) —— ( )    \n"
                    + "     |      |      |      |      |      |      |            \n"
                    + "    ( ) —— (M) —— ( )    ( ) —— ( )    ( ) —— ( ) —— ( )    \n"
                    + "                                                            \n"
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : West\n"
                    + "You find 1 Ruby, 5 Sapphires here.\n"
                    + "You smell something Less Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : \n"
                    + "Player Name is PacMan.\n"
                    + "Player has not collected any treasures.\n"
                    + "Player has 3 Arrows.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "What to pick : ( D | R | S | A ) : "
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : West\n"
                    + "You find 5 Sapphires here.\n"
                    + "You smell something Less Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "What to pick : ( D | R | S | A ) : "
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : West\n"
                    + "You find 4 Sapphires here.\n"
                    + "You smell something Less Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "What to pick : ( D | R | S | A ) : "
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : West\n"
                    + "You find 3 Sapphires here.\n"
                    + "You smell something Less Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "What to pick : ( D | R | S | A ) : "
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : West\n"
                    + "You find 2 Sapphires here.\n"
                    + "You smell something Less Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "What to pick : ( D | R | S | A ) : "
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : West\n"
                    + "You find 1 Sapphire here.\n"
                    + "You smell something Less Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "What to pick : ( D | R | S | A ) : "
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : West\n"
                    + "You smell something Less Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Enter a direction ( N | E | S | W ) : "
                    + "\n"
                    + "You are in a Tunnel\n"
                    + "Doors lead to : East, South\n"
                    + "You find 2 Arrows\n"
                    + "You smell something More Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Which direction ( N | E | S | W ) : "
                    + "Enter distance (1-5) : "
                    + "You hear a great howl in the distance.\n"
                    + "\n"
                    + "You are in a Tunnel\n"
                    + "Doors lead to : East, South\n"
                    + "You find 2 Arrows\n"
                    + "You smell something More Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Which direction ( N | E | S | W ) : "
                    + "Enter distance (1-5) : "
                    + "You hear a great howl in the distance.\n"
                    + "The howl ended as it was slayed.\n"
                    + "\n"
                    + "You are in a Tunnel\n"
                    + "Doors lead to : East, South\n"
                    + "You find 2 Arrows\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "What to pick : ( D | R | S | A ) : "
                    + "\n"
                    + "You are in a Tunnel\n"
                    + "Doors lead to : East, South\n"
                    + "You find 1 Arrow\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "What to pick : ( D | R | S | A ) : "
                    + "\n"
                    + "You are in a Tunnel\n"
                    + "Doors lead to : East, South\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Enter a direction ( N | E | S | W ) : "
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : North, East, West\n"
                    + "You find 6 Rubies here.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Enter a direction ( N | E | S | W ) : "
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : West\n"
                    + "You find 2 Diamonds, 6 Sapphires here.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "What to pick : ( D | R | S | A ) : "
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : West\n"
                    + "You find 1 Diamond, 6 Sapphires here.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "What to pick : ( D | R | S | A ) : "
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : West\n"
                    + "You find 6 Sapphires here.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Enter a direction ( N | E | S | W ) : "
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : North, East, West\n"
                    + "You find 6 Rubies here.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Enter a direction ( N | E | S | W ) : "
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : East, South, West\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Enter a direction ( N | E | S | W ) : "
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : North, East, South, West\n"
                    + "You find 8 Diamonds, 9 Sapphires here.\n"
                    + "You smell something Less Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Enter a direction ( N | E | S | W ) : "
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : North, East, South, West\n"
                    + "You smell something More Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Which direction ( N | E | S | W ) : "
                    + "Enter distance (1-5) : "
                    + "You shoot an arrow into the darkness.\n"
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : North, East, South, West\n"
                    + "You smell something More Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Which direction ( N | E | S | W ) : "
                    + "Enter distance (1-5) : "
                    + "You shoot an arrow into the darkness.\n"
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : North, East, South, West\n"
                    + "You smell something More Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Enter a direction ( N | E | S | W ) : "
                    + "\n"
                    + "You are in a Tunnel\n"
                    + "Doors lead to : East, South\n"
                    + "You smell something More Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Enter a direction ( N | E | S | W ) : "
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : North, South, West\n"
                    + "You find 7 Diamonds here.\n"
                    + "You smell something More Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Which direction ( N | E | S | W ) : "
                    + "Enter distance (1-5) : "
                    + "You hear a great howl in the distance.\n"
                    + "You are out of arrows, explore to find more\n"
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : North, South, West\n"
                    + "You find 7 Diamonds here.\n"
                    + "You smell something More Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Enter a direction ( N | E | S | W ) : "
                    + "\n"
                    + "You are in a Tunnel\n"
                    + "Doors lead to : North, West\n"
                    + "You find 3 Arrows\n"
                    + "You smell something Less Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "What to pick : ( D | R | S | A ) : "
                    + "\n"
                    + "You are in a Tunnel\n"
                    + "Doors lead to : North, West\n"
                    + "You find 2 Arrows\n"
                    + "You smell something Less Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "What to pick : ( D | R | S | A ) : "
                    + "\n"
                    + "You are in a Tunnel\n"
                    + "Doors lead to : North, West\n"
                    + "You find 1 Arrow\n"
                    + "You smell something Less Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "What to pick : ( D | R | S | A ) : "
                    + "\n"
                    + "You are in a Tunnel\n"
                    + "Doors lead to : North, West\n"
                    + "You smell something Less Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "\n"
                    + "Player Name is PacMan.\n"
                    + "Player has collected 2 Diamonds, 1 Ruby, 5 Sapphires.\n"
                    + "Player has 3 Arrows.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Which direction ( N | E | S | W ) : "
                    + "Enter distance (1-5) : "
                    + "You hear a great howl in the distance.\n"
                    + "The howl ended as it was slayed.\n"
                    + "\n"
                    + "You are in a Tunnel\n"
                    + "Doors lead to : North, West\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Which direction ( N | E | S | W ) : "
                    + "Enter distance (1-5) : "
                    + "You shoot an arrow into the darkness.\n"
                    + "\n"
                    + "You are in a Tunnel\n"
                    + "Doors lead to : North, West\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Enter a direction ( N | E | S | W ) : "
                    + "\n"
                    + "You are in a Tunnel\n"
                    + "Doors lead to : North, East\n"
                    + "You find 1 Arrow\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "What to pick : ( D | R | S | A ) : "
                    + "\n"
                    + "You are in a Tunnel\n"
                    + "Doors lead to : North, East\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Enter a direction ( N | E | S | W ) : "
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : North, East, South, West\n"
                    + "You smell something More Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Enter a direction ( N | E | S | W ) : "
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : East, South, West\n"
                    + "You find 2 Rubies, 4 Sapphires here.\n"
                    + "You find 2 Arrows\n"
                    + "You smell something More Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "What to pick : ( D | R | S | A ) : "
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : East, South, West\n"
                    + "You find 1 Ruby, 4 Sapphires here.\n"
                    + "You find 2 Arrows\n"
                    + "You smell something More Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "What to pick : ( D | R | S | A ) : "
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : East, South, West\n"
                    + "You find 4 Sapphires here.\n"
                    + "You find 2 Arrows\n"
                    + "You smell something More Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "What to pick : ( D | R | S | A ) : "
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : East, South, West\n"
                    + "You find 3 Sapphires here.\n"
                    + "You find 2 Arrows\n"
                    + "You smell something More Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "What to pick : ( D | R | S | A ) : "
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : East, South, West\n"
                    + "You find 2 Sapphires here.\n"
                    + "You find 2 Arrows\n"
                    + "You smell something More Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "What to pick : ( D | R | S | A ) : "
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : East, South, West\n"
                    + "You find 1 Sapphire here.\n"
                    + "You find 2 Arrows\n"
                    + "You smell something More Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "What to pick : ( D | R | S | A ) : "
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : East, South, West\n"
                    + "You find 2 Arrows\n"
                    + "You smell something More Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "What to pick : ( D | R | S | A ) : "
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : East, South, West\n"
                    + "You find 1 Arrow\n"
                    + "You smell something More Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "What to pick : ( D | R | S | A ) : "
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : East, South, West\n"
                    + "You smell something More Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Which direction ( N | E | S | W ) : "
                    + "Enter distance (1-5) : "
                    + "You hear a great howl in the distance.\n"
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : East, South, West\n"
                    + "You smell something More Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Which direction ( N | E | S | W ) : "
                    + "Enter distance (1-5) : "
                    + "You hear a great howl in the distance.\n"
                    + "The howl ended as it was slayed.\n"
                    + "\n"
                    + "You are in a Cave\n"
                    + "Doors lead to : East, South, West\n"
                    + "You smell something Less Pungent.\n"
                    + "\n"
                    + "Move, Pick, Shoot, Describe ( M | P | S | D ) : "
                    + "Enter a direction ( N | E | S | W ) : "
                    + "\n"
                    + "You reached the end location.\n"
                    + "Congratulations You Win!!\n"
                    + "You collected 2 Diamonds, 3 Rubies, 9 Sapphires from your adventure!!\n",
            out.toString());
  }
}


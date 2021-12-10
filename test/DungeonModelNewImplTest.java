import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

import dungeonmodel.Direction;
import dungeonmodel.DungeonModel;
import dungeonmodel.DungeonModelImpl;
import dungeonmodel.GameStatus;
import dungeonmodel.Key;
import dungeonmodel.Treasure;
import random.RandomImpl;
import structureddata.Position;

import static org.junit.Assert.*;

public class DungeonModelNewImplTest {

  private DungeonModel model;

  @Before
  public void setup() {
    RandomImpl.setSeed(0);
    model = new DungeonModelImpl(6, 8, false, 10, 50, 1, 1, 1, 0, true);
  }

  @Test
  public void testConstructor() {
    try {
      new DungeonModelImpl(6, 8, true, 20, 50, 1, -1, 1, 0, true);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Number of Aboleths should be > 0",
              e.getMessage());
    }

    try {
      new DungeonModelImpl(6, 8, true, 20, 50, 1, 1, -5, 0, true);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Number of Thieves should be > 0",
              e.getMessage());
    }
  }

  @Test
  public void testStealTreasure() {
    assertEquals("                                                            \n" +
            "    ( )    ( )    ( ) —— ( ) —— ( ) —— ( )    ( ) —— ( )    \n" +
            "     |      |      |      |                    |            \n" +
            "    ( ) —— ( ) —— ( )    ( )    ( ) —— ( ) —— ( ) —— ( )    \n" +
            "            |             |      |      |                   \n" +
            "    ( )    ( ) —— (P) —— ( )    ( ) —— ( ) —— ( ) —— ( )    \n" +
            "     |             |                    |             |     \n" +
            "    ( )    ( ) —— ( ) —— ( )    ( ) —— ( ) —— (&) —— ( )    \n" +
            "     |      |             |      |      |      |      |     \n" +
            "    ( ) —— ( ) —— ( ) —— ( ) —— ( )    ( ) —— ( ) —— ( )    \n" +
            "     |      |      |      |      |      |      |            \n" +
            "    ( ) —— ( ) —— ( )    ( ) —— ( )    ( ) —— ( ) —— ( )    \n" +
            "                                                            \n", model.printDungeon());


    model.movePlayer(Direction.SOUTH);
    model.pickItem(Treasure.DIAMOND);
    model.pickItem(Treasure.DIAMOND);
    model.pickItem(Treasure.DIAMOND);
    model.pickItem(Treasure.DIAMOND);
    model.pickItem(Treasure.DIAMOND);
    model.pickItem(Treasure.DIAMOND);
    model.pickItem(Treasure.DIAMOND);
    model.pickItem(Treasure.DIAMOND);
    model.pickItem(Treasure.RUBY);
    model.pickItem(Treasure.RUBY);
    model.pickItem(Treasure.RUBY);
    model.pickItem(Treasure.RUBY);
    model.pickItem(Treasure.RUBY);
    model.pickItem(Treasure.RUBY);
    model.pickItem(Treasure.SAPPHIRE);
    model.pickItem(Treasure.SAPPHIRE);

    Map<Treasure, Integer> expectedMap = new TreeMap<>();
    expectedMap.put(Treasure.DIAMOND, 8);
    expectedMap.put(Treasure.RUBY, 6);
    expectedMap.put(Treasure.SAPPHIRE, 2);
    assertEquals(expectedMap, model.getPlayerDescription().getCollectedTreasures());

    model.movePlayer(Direction.EAST);
    model.movePlayer(Direction.SOUTH);
    model.movePlayer(Direction.EAST);
    model.movePlayer(Direction.NORTH);
    model.movePlayer(Direction.EAST);
    model.movePlayer(Direction.NORTH);
    model.movePlayer(Direction.NORTH);
    model.movePlayer(Direction.EAST);
    assertFalse(model.getCurrentLocation().containsThief());
    assertFalse(model.stealTreasure());
    assertEquals(expectedMap, model.getPlayerDescription().getCollectedTreasures());
    model.movePlayer(Direction.NORTH);

    assertTrue(model.getCurrentLocation().containsThief());
    assertTrue(model.stealTreasure());
    assertFalse(model.getCurrentLocation().containsThief());


    expectedMap.put(Treasure.DIAMOND, 8);
    expectedMap.put(Treasure.RUBY, 1);
    expectedMap.put(Treasure.SAPPHIRE, 2);
    assertEquals(expectedMap, model.getPlayerDescription().getCollectedTreasures());
  }

  @Test
  public void testStealNoTreasure() {
    assertEquals("                                                            \n" +
            "    ( )    ( )    ( ) —— ( ) —— ( ) —— ( )    ( ) —— ( )    \n" +
            "     |      |      |      |                    |            \n" +
            "    ( ) —— ( ) —— ( )    ( )    ( ) —— ( ) —— ( ) —— ( )    \n" +
            "            |             |      |      |                   \n" +
            "    ( )    ( ) —— (P) —— ( )    ( ) —— ( ) —— ( ) —— ( )    \n" +
            "     |             |                    |             |     \n" +
            "    ( )    ( ) —— ( ) —— ( )    ( ) —— ( ) —— (&) —— ( )    \n" +
            "     |      |             |      |      |      |      |     \n" +
            "    ( ) —— ( ) —— ( ) —— ( ) —— ( )    ( ) —— ( ) —— ( )    \n" +
            "     |      |      |      |      |      |      |            \n" +
            "    ( ) —— ( ) —— ( )    ( ) —— ( )    ( ) —— ( ) —— ( )    \n" +
            "                                                            \n", model.printDungeon());


    model.movePlayer(Direction.SOUTH);

    Map<Treasure, Integer> expectedMap = new TreeMap<>();
    assertEquals(expectedMap, model.getPlayerDescription().getCollectedTreasures());

    model.movePlayer(Direction.EAST);
    model.movePlayer(Direction.SOUTH);
    model.movePlayer(Direction.EAST);
    model.movePlayer(Direction.NORTH);
    model.movePlayer(Direction.EAST);
    model.movePlayer(Direction.NORTH);
    model.movePlayer(Direction.NORTH);
    model.movePlayer(Direction.EAST);
    assertFalse(model.getCurrentLocation().containsThief());
    assertFalse(model.stealTreasure());
    assertEquals(expectedMap, model.getPlayerDescription().getCollectedTreasures());
    model.movePlayer(Direction.NORTH);

    assertTrue(model.getCurrentLocation().containsThief());
    assertTrue(model.stealTreasure());
    assertFalse(model.getCurrentLocation().containsThief());

    assertEquals(expectedMap, model.getPlayerDescription().getCollectedTreasures());
  }

  @Test
  public void moveAboleth() {
    assertEquals("                                                            \n" +
            "    ( )    ( )    ( ) —— ( ) —— ( ) —— ( )    ( ) —— ( )    \n" +
            "     |      |      |      |                    |            \n" +
            "    ( ) —— ( ) —— ( )    ( )    ( ) —— ( ) —— ( ) —— ( )    \n" +
            "            |             |      |      |                   \n" +
            "    ( )    ( ) —— (P) —— ( )    ( ) —— ( ) —— ( ) —— ( )    \n" +
            "     |             |                    |             |     \n" +
            "    ( )    ( ) —— ( ) —— ( )    ( ) —— ( ) —— (&) —— ( )    \n" +
            "     |      |             |      |      |      |      |     \n" +
            "    ( ) —— ( ) —— ( ) —— ( ) —— ( )    ( ) —— ( ) —— ( )    \n" +
            "     |      |      |      |      |      |      |            \n" +
            "    ( ) —— ( ) —— ( )    ( ) —— ( )    ( ) —— ( ) —— ( )    \n" +
            "                                                            \n", model.printDungeon());

    assertTrue(model.getLocation(new Position(3, 5)).containsAboleth());
    model.moveAboleth();
    assertTrue(model.getLocation(new Position(3, 4)).containsAboleth());
    model.moveAboleth();
    assertTrue(model.getLocation(new Position(4, 4)).containsAboleth());
    model.moveAboleth();
    assertTrue(model.getLocation(new Position(4, 3)).containsAboleth());
    model.moveAboleth();

    assertTrue(model.getLocation(new Position(3, 3)).containsAboleth());

    model.movePlayer(Direction.SOUTH);
    model.movePlayer(Direction.EAST);

    assertEquals(new Position(3, 3), model.getCurrentLocation().getPosition());
    model.moveAboleth();
    assertEquals(GameStatus.GAME_OVER_KILLED, model.getGameStatus());
  }


  @Test
  public void killMonster() {
    assertEquals("                                                            \n" +
            "    ( )    ( )    ( ) —— ( ) —— ( ) —— ( )    ( ) —— ( )    \n" +
            "     |      |      |      |                    |            \n" +
            "    ( ) —— ( ) —— ( )    ( )    ( ) —— ( ) —— ( ) —— ( )    \n" +
            "            |             |      |      |                   \n" +
            "    ( )    ( ) —— (P) —— ( )    ( ) —— ( ) —— ( ) —— ( )    \n" +
            "     |             |                    |             |     \n" +
            "    ( )    ( ) —— ( ) —— ( )    ( ) —— ( ) —— (&) —— ( )    \n" +
            "     |      |             |      |      |      |      |     \n" +
            "    ( ) —— ( ) —— ( ) —— ( ) —— ( )    ( ) —— ( ) —— ( )    \n" +
            "     |      |      |      |      |      |      |            \n" +
            "    ( ) —— ( ) —— ( )    ( ) —— ( )    ( ) —— ( ) —— ( )    \n" +
            "                                                            \n", model.printDungeon());

    assertTrue(model.getLocation(new Position(3, 5)).containsAboleth());
    model.moveAboleth();
    assertTrue(model.getLocation(new Position(3, 4)).containsAboleth());
    model.moveAboleth();
    assertTrue(model.getLocation(new Position(4, 4)).containsAboleth());
    model.moveAboleth();
    assertTrue(model.getLocation(new Position(4, 3)).containsAboleth());
    model.moveAboleth();

    assertTrue(model.getLocation(new Position(3, 3)).containsAboleth());

    model.movePlayer(Direction.SOUTH);
    model.movePlayer(Direction.EAST);

    assertEquals(new Position(3, 3), model.getCurrentLocation().getPosition());
    model.killMonster();
    assertFalse(model.getLocation(new Position(3, 3)).containsAboleth());
    assertEquals(GameStatus.GAME_CONTINUE, model.getGameStatus());
  }

  @Test
  public void openDoorWithoutKey() {
    assertEquals("                                                            \n" +
            "    ( )    ( )    ( ) —— ( ) —— ( ) —— ( )    ( ) —— ( )    \n" +
            "     |      |      |      |                    |            \n" +
            "    ( ) —— ( ) —— ( )    ( )    ( ) —— ( ) —— ( ) —— ( )    \n" +
            "            |             |      |      |                   \n" +
            "    ( )    ( ) —— (P) —— ( )    ( ) —— ( ) —— ( ) —— ( )    \n" +
            "     |             |                    |             |     \n" +
            "    ( )    ( ) —— ( ) —— ( )    ( ) —— ( ) —— (&) —— ( )    \n" +
            "     |      |             |      |      |      |      |     \n" +
            "    ( ) —— ( ) —— ( ) —— ( ) —— ( )    ( ) —— ( ) —— ( )    \n" +
            "     |      |      |      |      |      |      |            \n" +
            "    ( ) —— ( ) —— ( )    ( ) —— ( )    ( ) —— ( ) —— ( )    \n" +
            "                                                            \n", model.printDungeon());

    model.movePlayer(Direction.SOUTH);
    model.movePlayer(Direction.EAST);
    model.movePlayer(Direction.SOUTH);
    model.movePlayer(Direction.EAST);
    model.movePlayer(Direction.NORTH);
    model.movePlayer(Direction.EAST);
    model.shoot(Direction.EAST, 1);
    model.shoot(Direction.EAST, 1);
    model.movePlayer(Direction.EAST);

    assertEquals(model.getEndCave().getPosition(), model.getCurrentLocation().getPosition());
    assertEquals(GameStatus.GAME_CONTINUE, model.getGameStatus());
    try {
      model.exitDungeon();
      fail();
    } catch (IllegalStateException e) {
      assertEquals("Player does not have the key.", e.getMessage());
    }
    assertEquals(GameStatus.GAME_CONTINUE, model.getGameStatus());
  }

  @Test
  public void openDoorWithKey() {
    assertEquals("                                                            \n" +
            "    ( )    ( )    ( ) —— ( ) —— ( ) —— ( )    ( ) —— ( )    \n" +
            "     |      |      |      |                    |            \n" +
            "    ( ) —— ( ) —— ( )    ( )    ( ) —— ( ) —— ( ) —— ( )    \n" +
            "            |             |      |      |                   \n" +
            "    ( )    ( ) —— (P) —— ( )    ( ) —— ( ) —— ( ) —— ( )    \n" +
            "     |             |                    |             |     \n" +
            "    ( )    ( ) —— ( ) —— ( )    ( ) —— ( ) —— (&) —— ( )    \n" +
            "     |      |             |      |      |      |      |     \n" +
            "    ( ) —— ( ) —— ( ) —— ( ) —— ( )    ( ) —— ( ) —— ( )    \n" +
            "     |      |      |      |      |      |      |            \n" +
            "    ( ) —— ( ) —— ( )    ( ) —— ( )    ( ) —— ( ) —— ( )    \n" +
            "                                                            \n", model.printDungeon());

    model.movePlayer(Direction.WEST);
    model.movePlayer(Direction.NORTH);
    model.movePlayer(Direction.WEST);

    assertTrue(model.getCurrentLocation().hasKey());
    assertFalse(model.getPlayerDescription().hasKey());
    model.pickItem(Key.DOOR_KEY);
    assertFalse(model.getCurrentLocation().hasKey());
    assertTrue(model.getPlayerDescription().hasKey());

    model.movePlayer(Direction.EAST);
    model.movePlayer(Direction.SOUTH);
    model.movePlayer(Direction.EAST);
    model.movePlayer(Direction.SOUTH);
    model.movePlayer(Direction.EAST);
    model.movePlayer(Direction.SOUTH);
    model.movePlayer(Direction.EAST);
    model.movePlayer(Direction.NORTH);
    model.movePlayer(Direction.EAST);
    model.shoot(Direction.EAST, 1);
    model.shoot(Direction.EAST, 1);
    model.movePlayer(Direction.EAST);

    assertEquals(model.getEndCave().getPosition(), model.getCurrentLocation().getPosition());
    assertEquals(GameStatus.GAME_CONTINUE, model.getGameStatus());
    model.exitDungeon();
    assertEquals(GameStatus.GAME_OVER_WIN, model.getGameStatus());
  }

  @Test
  public void testPit() {
    RandomImpl.setSeed(0);
    model = new DungeonModelImpl(6, 8, false, 10, 50, 1, 1, 1, 1, true);

    assertEquals("                                                            \n" +
            "    ( )    ( )    ( ) —— ( ) —— ( ) —— ( )    ( ) —— ( )    \n" +
            "     |      |      |      |                    |            \n" +
            "    ( ) —— ( ) —— ( )    ( )    ( ) —— ( ) —— ( ) —— ( )    \n" +
            "            |             |      |      |                   \n" +
            "    ( )    ( ) —— (P) —— ( )    ( ) —— ( ) —— ( ) —— ( )    \n" +
            "     |             |                    |             |     \n" +
            "    ( )    ( ) —— ( ) —— ( )    ( ) —— ( ) —— ( ) —— ( )    \n" +
            "     |      |             |      |      |      |      |     \n" +
            "    ( ) —— ( ) —— ( ) —— ( ) —— ( )    (&) —— ( ) —— ( )    \n" +
            "     |      |      |      |      |      |      |            \n" +
            "    ( ) —— ( ) —— ( )    ( ) —— ( )    ( ) —— ( ) —— ( )    \n" +
            "                                                            \n", model.printDungeon());

    model.movePlayer(Direction.WEST);
    model.movePlayer(Direction.NORTH);
    model.movePlayer(Direction.WEST);
    assertFalse(model.getCurrentLocation().hasPit());
    assertTrue(model.getCurrentLocation().hasPitNearby());
    model.setPlayerinPit(true);
    assertFalse(model.getPlayerDescription().fallenInPit());
    model.movePlayer(Direction.NORTH);

    assertEquals(new Position(0, 0), model.getCurrentLocation().getPosition());
    assertTrue(model.getCurrentLocation().hasPit());
    assertFalse(model.getCurrentLocation().hasPitNearby());
    model.setPlayerinPit(true);
    assertTrue(model.getPlayerDescription().fallenInPit());

    try {
      model.movePlayer(Direction.NORTH);
      fail();
    } catch (IllegalStateException e) {
      assertEquals("You are in a pit. It will take 5 seconds to come out.", e.getMessage());
    }

    try {
      model.exitDungeon();
      fail();
    } catch (IllegalStateException e) {
      assertEquals("You are in a pit. It will take 5 seconds to come out.", e.getMessage());
    }

    try {
      model.killMonster();
      fail();
    } catch (IllegalStateException e) {
      assertEquals("You are in a pit. It will take 5 seconds to come out.", e.getMessage());
    }

    try {
      model.pickItem(Key.DOOR_KEY);
      fail();
    } catch (IllegalStateException e) {
      assertEquals("You are in a pit. It will take 5 seconds to come out.", e.getMessage());
    }
    try {
      model.shoot(Direction.NORTH, 2);
      fail();
    } catch (IllegalStateException e) {
      assertEquals("You are in a pit. It will take 5 seconds to come out.", e.getMessage());
    }

    model.moveAboleth();
    model.stealTreasure();

    model.setPlayerinPit(false);
    assertFalse(model.getPlayerDescription().fallenInPit());
    model.movePlayer(Direction.SOUTH);
    assertEquals(new Position(1, 0), model.getCurrentLocation().getPosition());
    assertTrue(model.getCurrentLocation().hasPitNearby());
  }
}
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

import dungeonmodel.Arrow;
import dungeonmodel.Direction;
import dungeonmodel.DungeonModel;
import dungeonmodel.DungeonModelImpl;
import dungeonmodel.GameStatus;
import dungeonmodel.HitStatus;
import dungeonmodel.SmellLevel;
import dungeonmodel.Treasure;
import random.RandomImpl;
import structureddata.LocationDescription;
import structureddata.Position;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


/**
 * Tests to check the methods of {@link DungeonModel}. Covers all the different types of
 * scenarios that could occur in a dungeon.
 */
public class DungeonModelImplTest {

  private DungeonModel nonWrappingDungeon;
  private DungeonModel nonWrappingDungeonFull;
  private DungeonModel nonWrappingDungeonNoTreasure;
  private DungeonModel nonWrappingDungeonNoDegree;
  private DungeonModel wrappingDungeon;
  private DungeonModel wrappingDungeonFull;
  private DungeonModel wrappingDungeonNoTreasure;
  private DungeonModel wrappingDungeonNoDegree;

  private final int rows = 6;
  private final int columns = 8;

  @Before
  public void setup() {
    RandomImpl.setSeed(0);
    nonWrappingDungeon = new DungeonModelImpl(rows, columns, false, 10, 50, 5);
    RandomImpl.setSeed(1);
    nonWrappingDungeonFull = new DungeonModelImpl(rows, columns, false, 1000, 50, 1);
    RandomImpl.setSeed(2);
    nonWrappingDungeonNoTreasure = new DungeonModelImpl(rows, columns, false, 10, 0, 1);
    RandomImpl.setSeed(3);
    nonWrappingDungeonNoDegree = new DungeonModelImpl(rows, columns, false, 0, 50, 1);

    RandomImpl.setSeed(4);
    wrappingDungeon = new DungeonModelImpl(rows, columns, true, 20, 50, 5);
    RandomImpl.setSeed(5);
    wrappingDungeonFull = new DungeonModelImpl(rows, columns, true, 1000, 50, 1);
    RandomImpl.setSeed(6);
    wrappingDungeonNoTreasure = new DungeonModelImpl(rows, columns, true, 20, 0, 1);
    RandomImpl.setSeed(7);
    wrappingDungeonNoDegree = new DungeonModelImpl(rows, columns, true, 0, 50, 1);

  }

  @Test
  public void testConstructorFail() {
    try {
      new DungeonModelImpl(-6, 8, true, 20, 50, 1);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Rows and columns should be minimum 6.",
              e.getMessage());
    }

    try {
      new DungeonModelImpl(6, -8, true, 20, 50, 1);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Rows and columns should be minimum 6.",
              e.getMessage());
    }

    try {
      new DungeonModelImpl(6, 8, true, -20, 50, 1);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Degree of interconnectivity should be >= 0.",
              e.getMessage());
    }

    try {
      new DungeonModelImpl(6, 8, true, 20, -50, 1);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Percentage of caves with treasure should be between 0 and 100.",
              e.getMessage());
    }

    try {
      new DungeonModelImpl(6, 8, true, 20, 150, 1);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Percentage of caves with treasure should be between 0 and 100.",
              e.getMessage());
    }

    try {
      new DungeonModelImpl(3, 3, false, 20, 50, 1);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Rows and columns should be minimum 6.",
              e.getMessage());
    }

    try {
      new DungeonModelImpl(6, 8, false, 10, 50, 0);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Number of Otyugh should be atleast 1.",
              e.getMessage());
    }

    try {
      new DungeonModelImpl(6, 8, false, 10, 50, -5);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Number of Otyugh should be atleast 1.",
              e.getMessage());
    }
  }

  @Test
  public void testStartEndNotTunnel() {
    //Test to check if start and end positions are not tunnel
    assertTrue(nonWrappingDungeon.getStartCave().isCave());
    assertTrue(nonWrappingDungeon.getEndCave().isCave());

    assertTrue(nonWrappingDungeonFull.getStartCave().isCave());
    assertTrue(nonWrappingDungeonFull.getEndCave().isCave());

    assertTrue(nonWrappingDungeonNoDegree.getStartCave().isCave());
    assertTrue(nonWrappingDungeonNoDegree.getEndCave().isCave());

    assertTrue(nonWrappingDungeonNoTreasure.getStartCave().isCave());
    assertTrue(nonWrappingDungeonNoTreasure.getEndCave().isCave());

    assertTrue(wrappingDungeon.getStartCave().isCave());
    assertTrue(wrappingDungeon.getEndCave().isCave());

    assertTrue(wrappingDungeonFull.getStartCave().isCave());
    assertTrue(wrappingDungeonFull.getEndCave().isCave());

    assertTrue(wrappingDungeonNoDegree.getStartCave().isCave());
    assertTrue(wrappingDungeonNoDegree.getEndCave().isCave());

    assertTrue(wrappingDungeonNoTreasure.getStartCave().isCave());
    assertTrue(wrappingDungeonNoTreasure.getEndCave().isCave());
  }

  @Test
  public void testStartEndDistance() {
    //Test to check if start and end position has a distance greater than 5.
    Position expectedStartPosition = new Position(0, 7);
    Position expectedEndPosition = new Position(4, 1);
    assertEquals(expectedStartPosition, nonWrappingDungeon.getStartCave().getPosition());
    assertEquals(expectedEndPosition, nonWrappingDungeon.getEndCave().getPosition());
    assertEquals(10, nonWrappingDungeon.getDistance(
            nonWrappingDungeon.getStartCave().getPosition(),
            nonWrappingDungeon.getEndCave().getPosition()));

    assertEquals(7, wrappingDungeon.getDistance(
            wrappingDungeon.getStartCave().getPosition(),
            wrappingDungeon.getEndCave().getPosition()));


    assertTrue(nonWrappingDungeonFull.getDistance(
            nonWrappingDungeonFull.getStartCave().getPosition(),
            nonWrappingDungeonFull.getEndCave().getPosition()) >= 5);
    assertTrue(nonWrappingDungeonNoDegree.getDistance(
            nonWrappingDungeonNoDegree.getStartCave().getPosition(),
            nonWrappingDungeonNoDegree.getEndCave().getPosition()) >= 5);
    assertTrue(nonWrappingDungeonNoTreasure.getDistance(
            nonWrappingDungeonNoTreasure.getStartCave().getPosition(),
            nonWrappingDungeonNoTreasure.getEndCave().getPosition()) >= 5);
    assertTrue(wrappingDungeonFull.getDistance(
            wrappingDungeonFull.getStartCave().getPosition(),
            wrappingDungeonFull.getEndCave().getPosition()) >= 5);
    assertTrue(wrappingDungeonNoDegree.getDistance(
            wrappingDungeonNoDegree.getStartCave().getPosition(),
            wrappingDungeonNoDegree.getEndCave().getPosition()) >= 5);
    assertTrue(wrappingDungeonNoTreasure.getDistance(
            wrappingDungeonNoTreasure.getStartCave().getPosition(),
            wrappingDungeonNoTreasure.getEndCave().getPosition()) >= 5);

  }

  @Test
  public void testNoWrapping() {
    //For isWrapped = false, there should be no edge between the end locations.
    for (int j = 0; j < columns; j++) {
      assertFalse(nonWrappingDungeonFull.getLocation(
              new Position(0, j)).getPossibleDirections().contains(Direction.NORTH));
      assertFalse(nonWrappingDungeonFull.getLocation(
              new Position(rows - 1, j)).getPossibleDirections().contains(Direction.SOUTH));
    }
    for (int i = 0; i < rows; i++) {
      assertFalse(nonWrappingDungeonFull.getLocation(
              new Position(i, 0)).getPossibleDirections().contains(Direction.WEST));
      assertFalse(nonWrappingDungeonFull.getLocation(
              new Position(i, columns - 1)).getPossibleDirections().contains(Direction.EAST));
    }
  }

  @Test
  public void testWrapping() {
    //For isWrapped = true, there can be an edge between the end locations.
    // For full wrapping dungeon, all edges must be present.
    for (int j = 0; j < columns; j++) {
      assertTrue(wrappingDungeonFull.getLocation(
              new Position(0, j)).getPossibleDirections().contains(Direction.NORTH));
      assertTrue(wrappingDungeonFull.getLocation(
              new Position(rows - 1, j)).getPossibleDirections().contains(Direction.SOUTH));
    }
    for (int i = 0; i < rows; i++) {
      assertTrue(wrappingDungeonFull.getLocation(
              new Position(i, 0)).getPossibleDirections().contains(Direction.WEST));
      assertTrue(wrappingDungeonFull.getLocation(
              new Position(i, columns - 1)).getPossibleDirections().contains(Direction.EAST));
    }
  }


  @Test
  public void degreeOfInterConnectivity() {
    int totalEdgesNonWrapping = 0;
    int totalEdgesNonWrappingFull = 0;
    int totalEdgesNonWrappingNoDegree = 0;
    int totalEdgesWrapping = 0;
    int totalEdgesWrappingFull = 0;
    int totalEdgesWrappingNoDegree = 0;

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        totalEdgesNonWrapping += nonWrappingDungeon.getLocation(
                new Position(i, j)).getPossibleDirections().size();
        totalEdgesNonWrappingFull += nonWrappingDungeonFull.getLocation(
                new Position(i, j)).getPossibleDirections().size();
        totalEdgesNonWrappingNoDegree += nonWrappingDungeonNoDegree.getLocation(
                new Position(i, j)).getPossibleDirections().size();
        totalEdgesWrapping += wrappingDungeon.getLocation(
                new Position(i, j)).getPossibleDirections().size();
        totalEdgesWrappingFull += wrappingDungeonFull.getLocation(
                new Position(i, j)).getPossibleDirections().size();
        totalEdgesWrappingNoDegree += wrappingDungeonNoDegree.getLocation(
                new Position(i, j)).getPossibleDirections().size();

      }
    }
    totalEdgesNonWrapping /= 2;
    totalEdgesNonWrappingFull /= 2;
    totalEdgesNonWrappingNoDegree /= 2;
    totalEdgesWrapping /= 2;
    totalEdgesWrappingFull /= 2;
    totalEdgesWrappingNoDegree /= 2;

    //An MST will have rows*columns-1 edges.
    // Thus, degree of interconnectivity should be total edges - edges in MST.
    assertEquals(10, totalEdgesNonWrapping - (rows * columns - 1));
    assertEquals(35, totalEdgesNonWrappingFull - (rows * columns - 1));
    assertEquals(0, totalEdgesNonWrappingNoDegree - (rows * columns - 1));
    assertEquals(20, totalEdgesWrapping - (rows * columns - 1));
    assertEquals(49, totalEdgesWrappingFull - (rows * columns - 1));
    assertEquals(0, totalEdgesWrappingNoDegree - (rows * columns - 1));
  }

  @Test
  public void testTunnelNoTreasure() {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        LocationDescription location1 = nonWrappingDungeon.getLocation(new Position(i, j));
        if (location1.getPossibleDirections().size() == 2) {
          //For Tunnel we need to check it does not contain any treasure.
          assertTrue(location1.getTreasureMap().isEmpty());
        }

        LocationDescription location2 = wrappingDungeon.getLocation(new Position(i, j));
        if (location2.getPossibleDirections().size() == 2) {
          //For Tunnel we need to check it does not contain any treasure.
          assertTrue(location2.getTreasureMap().isEmpty());
        }
      }
    }
  }

  @Test
  public void testPercentageOfCavesWithTreasure() {
    int totalCavesNonWrapping = 0;
    int totalCavesWithTreasureNonWrapping = 0;

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        LocationDescription location = nonWrappingDungeon.getLocation(new Position(i, j));
        if (location.getPossibleDirections().size() != 2) {
          totalCavesNonWrapping++;
          if (!location.getTreasureMap().isEmpty()) {
            totalCavesWithTreasureNonWrapping++;
          }
        }
      }
    }
    //To check if number of caves having treasure satisfy the constraint mentioned by the user.
    assertEquals(50, (totalCavesWithTreasureNonWrapping * 100L) / totalCavesNonWrapping);

    totalCavesNonWrapping = 0;
    totalCavesWithTreasureNonWrapping = 0;

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        LocationDescription location =
                nonWrappingDungeonNoTreasure.getLocation(new Position(i, j));
        if (location.getPossibleDirections().size() != 2) {
          totalCavesNonWrapping++;
          if (!location.getTreasureMap().isEmpty()) {
            totalCavesWithTreasureNonWrapping++;
          }
        }
      }
    }
    //To check if number of caves having treasure satisfy the constraint mentioned by the user.
    assertEquals(0, (totalCavesWithTreasureNonWrapping * 100L) / totalCavesNonWrapping);
  }

  @Test
  public void testConnectivityOfLocations() {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        //To check if all locations are connected to atleast one other location
        assertTrue(nonWrappingDungeonNoDegree.getCurrentLocation()
                .getPossibleDirections().size() != 0);
        assertTrue(wrappingDungeonNoDegree.getCurrentLocation()
                .getPossibleDirections().size() != 0);
      }
    }
  }

  @Test
  public void testPlayerStartingPosition() {
    //To check if player is initially at the start position.
    assertEquals(nonWrappingDungeon.getStartCave().getPosition(),
            nonWrappingDungeon.getPlayerDescription().getPosition());
    assertEquals(wrappingDungeon.getStartCave().getPosition(),
            wrappingDungeon.getPlayerDescription().getPosition());
  }

  @Test
  public void testPlayerDescription() {
    //Test initial player description.
    String expectedDescription = "Player Description:\n"
            + "Position: (0, 7)\n"
            + "Treasures collected: {}\n"
            + "Arrows collected: 3";
    assertEquals(expectedDescription, nonWrappingDungeon.getPlayerDescription().toString());

    nonWrappingDungeon.movePlayer(Direction.WEST);
    nonWrappingDungeon.movePlayer(Direction.EAST);
    assertEquals(expectedDescription, nonWrappingDungeon.getPlayerDescription().toString());

    nonWrappingDungeon.pickItem(Treasure.RUBY);
    nonWrappingDungeon.pickItem(Treasure.SAPPHIRE);
    nonWrappingDungeon.pickItem(Treasure.SAPPHIRE);
    nonWrappingDungeon.pickItem(Treasure.SAPPHIRE);
    nonWrappingDungeon.pickItem(Treasure.SAPPHIRE);
    nonWrappingDungeon.pickItem(Treasure.SAPPHIRE);
    nonWrappingDungeon.movePlayer(Direction.WEST);

    expectedDescription = "Player Description:\n"
            + "Position: (0, 6)\n"
            + "Treasures collected: {Ruby=1, Sapphire=5}\n"
            + "Arrows collected: 3";
    assertEquals(expectedDescription, nonWrappingDungeon.getPlayerDescription().toString());
  }

  @Test
  public void testLocationDescription() {
    //Test initial location description.
    String expectedDescription = "Location Description:\n"
            + "Possible directions: [West]\n"
            + "Treasures: {Ruby=1, Sapphire=5}\n"
            + "Number of Arrows: 0";
    assertEquals(expectedDescription,
            nonWrappingDungeon.getCurrentLocation().toString());


    nonWrappingDungeon.movePlayer(Direction.WEST);
    expectedDescription = "Location Description:\n"
            + "Possible directions: [East, South]\n"
            + "Treasures: {}\n"
            + "Number of Arrows: 2";
    assertEquals(expectedDescription,
            nonWrappingDungeon.getCurrentLocation().toString());
  }

  @Test
  public void testPickTreasure() {
    try {
      nonWrappingDungeon.pickItem(null);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("null item is not valid.", e.getMessage());
    }

    Map<Treasure, Integer> expectedPlayerMap = new TreeMap<>();
    Map<Treasure, Integer> expectedLocationMap = new TreeMap<>();
    expectedLocationMap.put(Treasure.RUBY, 1);
    expectedLocationMap.put(Treasure.SAPPHIRE, 5);

    //Initial Player and Location Treasures
    assertEquals(expectedPlayerMap,
            nonWrappingDungeon.getPlayerDescription().getCollectedTreasures());
    assertEquals(expectedLocationMap, nonWrappingDungeon.getCurrentLocation().getTreasureMap());


    //Player collecting Ruby from Location.
    expectedLocationMap.remove(Treasure.RUBY);
    expectedPlayerMap.put(Treasure.RUBY, 1);

    nonWrappingDungeon.pickItem(Treasure.RUBY);
    assertEquals(expectedPlayerMap,
            nonWrappingDungeon.getPlayerDescription().getCollectedTreasures());
    assertEquals(expectedLocationMap, nonWrappingDungeon.getCurrentLocation().getTreasureMap());

    //Player Collecting Sapphire from Location.
    expectedLocationMap.put(Treasure.SAPPHIRE, 3);
    expectedPlayerMap.put(Treasure.SAPPHIRE, 2);
    nonWrappingDungeon.pickItem(Treasure.SAPPHIRE);
    nonWrappingDungeon.pickItem(Treasure.SAPPHIRE);

    assertEquals(expectedPlayerMap,
            nonWrappingDungeon.getPlayerDescription().getCollectedTreasures());
    assertEquals(expectedLocationMap, nonWrappingDungeon.getCurrentLocation().getTreasureMap());

    //Player coming back to the location.
    nonWrappingDungeon.movePlayer(Direction.WEST);
    nonWrappingDungeon.movePlayer(Direction.EAST);

    assertEquals(expectedPlayerMap,
            nonWrappingDungeon.getPlayerDescription().getCollectedTreasures());
    assertEquals(expectedLocationMap, nonWrappingDungeon.getCurrentLocation().getTreasureMap());

    try {
      nonWrappingDungeon.pickItem(Treasure.RUBY);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Location does not contain treasure: Ruby", e.getMessage());
    }
  }

  @Test
  public void testPickArrow() {
    nonWrappingDungeon.movePlayer(Direction.WEST);
    //Initial Player and Location Arrows
    assertEquals(3, nonWrappingDungeon.getPlayerDescription().countArrows());
    assertEquals(2, nonWrappingDungeon.getCurrentLocation().countArrows());

    nonWrappingDungeon.pickItem(Arrow.CROOKED_ARROW);
    assertEquals(4, nonWrappingDungeon.getPlayerDescription().countArrows());
    assertEquals(1, nonWrappingDungeon.getCurrentLocation().countArrows());

    nonWrappingDungeon.pickItem(Arrow.CROOKED_ARROW);
    assertEquals(5, nonWrappingDungeon.getPlayerDescription().countArrows());
    assertEquals(0, nonWrappingDungeon.getCurrentLocation().countArrows());

    nonWrappingDungeon.movePlayer(Direction.EAST);
    nonWrappingDungeon.movePlayer(Direction.WEST);
    assertEquals(5, nonWrappingDungeon.getPlayerDescription().countArrows());
    assertEquals(0, nonWrappingDungeon.getCurrentLocation().countArrows());

    try {
      nonWrappingDungeon.pickItem(Arrow.CROOKED_ARROW);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("No arrows to Pick.", e.getMessage());
    }
  }

  @Test
  public void testMovePlayerFail() {
    nonWrappingDungeon.movePlayer(Direction.WEST);
    try {
      nonWrappingDungeon.movePlayer(Direction.WEST);
    } catch (IllegalArgumentException e) {
      assertEquals("Direction West is not possible from here.", e.getMessage());
    }
  }

  @Test
  public void testMovePlayer() {
    assertEquals(new Position(0, 7),
            nonWrappingDungeon.getPlayerDescription().getPosition());
    nonWrappingDungeon.movePlayer(Direction.WEST);
    assertEquals(new Position(0, 6),
            nonWrappingDungeon.getPlayerDescription().getPosition());
    nonWrappingDungeon.movePlayer(Direction.SOUTH);
    assertEquals(new Position(1, 6),
            nonWrappingDungeon.getPlayerDescription().getPosition());
    nonWrappingDungeon.movePlayer(Direction.NORTH);
    assertEquals(new Position(0, 6),
            nonWrappingDungeon.getPlayerDescription().getPosition());
    nonWrappingDungeon.movePlayer(Direction.EAST);
    assertEquals(new Position(0, 7),
            nonWrappingDungeon.getPlayerDescription().getPosition());

    wrappingDungeon.movePlayer(Direction.NORTH);
    wrappingDungeon.movePlayer(Direction.NORTH);
    assertEquals(new Position(0, 4), wrappingDungeon.getPlayerDescription().getPosition());
    wrappingDungeon.movePlayer(Direction.NORTH);
    assertEquals(new Position(5, 4), wrappingDungeon.getPlayerDescription().getPosition());
    wrappingDungeon.movePlayer(Direction.WEST);
    assertEquals(new Position(5, 3), wrappingDungeon.getPlayerDescription().getPosition());
    wrappingDungeon.movePlayer(Direction.SOUTH);
    assertEquals(new Position(0, 3), wrappingDungeon.getPlayerDescription().getPosition());
    wrappingDungeon.movePlayer(Direction.EAST);
    assertEquals(new Position(0, 4), wrappingDungeon.getPlayerDescription().getPosition());
  }

  @Test
  public void testPercentageOfLocationsWithTreasure() {
    int locationsWithArrows = 0;

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        LocationDescription location = nonWrappingDungeon.getLocation(new Position(i, j));
        if (location.countArrows() != 0) {
          locationsWithArrows++;
        }
      }
    }
    //To check if number of caves having treasure satisfy the constraint mentioned by the user.
    assertEquals(50, (locationsWithArrows * 100L) / (rows * columns));


    //For percentage of locations with items = 0.
    locationsWithArrows = 0;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        LocationDescription location =
                nonWrappingDungeonNoTreasure.getLocation(new Position(i, j));
        if (location.countArrows() != 0) {
          locationsWithArrows++;
        }
      }
    }
    //To check if number of caves having treasure satisfy the constraint mentioned by the user.
    assertEquals(0, (locationsWithArrows * 100L) / (rows * columns));
  }

  @Test
  public void testnumOtyughs() {
    int totalOtyughsNonWrapping = 0;
    int totalOtyughsWrapping = 0;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        LocationDescription location =
                nonWrappingDungeon.getLocation(new Position(i, j));
        if (location.containsOtyugh()) {
          //Confirms that otyughs are in caves
          assertTrue(location.isCave());
          totalOtyughsNonWrapping++;
        }
        location = wrappingDungeon.getLocation(new Position(i, j));
        if (location.containsOtyugh()) {
          //Confirms that otyughs are in caves
          assertTrue(location.isCave());
          totalOtyughsWrapping++;
        }
      }
    }
    assertEquals(5, totalOtyughsNonWrapping);
    assertEquals(5, totalOtyughsWrapping);

    //Test to check if startCave does not contain Otyugh
    assertFalse(nonWrappingDungeon.getStartCave().containsOtyugh());
    assertFalse(nonWrappingDungeon.getStartCave().containsOtyugh());

    //Test to check if endcave contains an Otyugh
    assertTrue(nonWrappingDungeon.getEndCave().containsOtyugh());
    assertTrue(nonWrappingDungeon.getEndCave().containsOtyugh());
  }

  @Test
  public void testSmellLevel() {
    assertEquals(SmellLevel.NO_SMELL, wrappingDungeon.detectSmell());
    wrappingDungeon.movePlayer(Direction.SOUTH);
    assertEquals(SmellLevel.LESS_PUNGENT, wrappingDungeon.detectSmell());
    wrappingDungeon.movePlayer(Direction.SOUTH);
    assertEquals(SmellLevel.MORE_PUNGENT, wrappingDungeon.detectSmell());

    wrappingDungeon.movePlayer(Direction.NORTH);
    wrappingDungeon.movePlayer(Direction.NORTH);
    wrappingDungeon.movePlayer(Direction.NORTH);
    wrappingDungeon.movePlayer(Direction.NORTH);
    wrappingDungeon.movePlayer(Direction.EAST);
    wrappingDungeon.movePlayer(Direction.EAST);
    wrappingDungeon.movePlayer(Direction.EAST);
    wrappingDungeon.movePlayer(Direction.EAST);
    wrappingDungeon.movePlayer(Direction.NORTH);
    assertEquals(new Position(5, 0), wrappingDungeon.getPlayerDescription().getPosition());

    //Having 2 or more Otyugh at a distance of 2 case.
    assertEquals(SmellLevel.MORE_PUNGENT, wrappingDungeon.detectSmell());
  }

  @Test
  public void testShootFail() {
    try {
      wrappingDungeon.shoot(null, 4);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Direction cannot be null.", e.getMessage());
    }

    try {
      wrappingDungeon.shoot(Direction.SOUTH, 0);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Distance should be between 1 and 5.", e.getMessage());
    }

    wrappingDungeon.shoot(Direction.SOUTH, 1);
    wrappingDungeon.shoot(Direction.SOUTH, 2);
    wrappingDungeon.shoot(Direction.SOUTH, 3);
    assertEquals(0, wrappingDungeon.getPlayerDescription().countArrows());
    try {
      wrappingDungeon.shoot(Direction.SOUTH, 4);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Player does not have any arrows.", e.getMessage());
    }
  }

  @Test
  public void testShoot() {
    wrappingDungeon.movePlayer(Direction.SOUTH);
    wrappingDungeon.pickItem(Arrow.CROOKED_ARROW);
    assertTrue(wrappingDungeon.getLocation(new Position(4, 5)).containsOtyugh());
    assertEquals(HitStatus.MISS, wrappingDungeon.shoot(Direction.SOUTH, 1));
    assertEquals(HitStatus.HIT, wrappingDungeon.shoot(Direction.SOUTH, 2));
    assertEquals(HitStatus.KILLED, wrappingDungeon.shoot(Direction.SOUTH, 2));
    assertFalse(wrappingDungeon.getLocation(new Position(4, 5)).containsOtyugh());
    assertEquals(HitStatus.MISS, wrappingDungeon.shoot(Direction.SOUTH, 2));
  }

  @Test
  public void testShootBentTunnel() {
    RandomImpl.setSeed(0);
    DungeonModel model = new DungeonModelImpl(6, 8, false, 10, 50, 10);
    String output = "                                                            \n"
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
            + "                                                            \n";
    assertEquals(output, model.printDungeon());
    assertEquals(HitStatus.HIT, model.shoot(Direction.WEST, 2));
    assertEquals(HitStatus.KILLED, model.shoot(Direction.WEST, 2));
    assertEquals(HitStatus.MISS, model.shoot(Direction.WEST, 2));
  }

  @Test
  public void testShootFromTunnel() {
    RandomImpl.setSeed(0);
    DungeonModel model = new DungeonModelImpl(6, 8, false, 10, 50, 10);
    model.movePlayer(Direction.WEST);
    String output = "                                                            \n"
            + "    (M)    ( )    ( ) —— ( ) —— ( ) —— (M)    (P) —— (#)    \n"
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
            + "                                                            \n";
    assertEquals(output, model.printDungeon());
    assertEquals(HitStatus.HIT, model.shoot(Direction.SOUTH, 1));
    assertEquals(HitStatus.MISS, model.shoot(Direction.EAST, 1));
    assertEquals(HitStatus.MISS, model.shoot(Direction.WEST, 1));
  }

  @Test
  public void testShootStraightCave() {
    RandomImpl.setSeed(5);
    DungeonModel dungeon = new DungeonModelImpl(6, 8, false, 10, 50, 5);
    dungeon.movePlayer(Direction.WEST);
    dungeon.movePlayer(Direction.WEST);
    dungeon.movePlayer(Direction.WEST);
    String output = "                                                            \n"
            + "    ( ) —— ( )    ( ) —— ( ) —— ( ) —— (M)    ( ) —— ( )    \n"
            + "     |             |      |      |             |      |     \n"
            + "    ( )    (P) —— ( ) —— ( ) —— (#) —— (M)    ( )    ( )    \n"
            + "     |             |      |      |                    |     \n"
            + "    ( )    ( )    ( ) —— ( )    ( ) —— ( )    (&)    ( )    \n"
            + "     |      |      |                    |      |      |     \n"
            + "    ( ) —— ( ) —— (M) —— ( ) —— ( ) —— ( ) —— ( ) —— ( )    \n"
            + "     |      |      |      |      |             |      |     \n"
            + "    ( ) —— ( ) —— ( ) —— ( )    ( ) —— ( ) —— ( )    ( )    \n"
            + "            |      |             |             |      |     \n"
            + "    ( ) —— ( ) —— ( )    ( ) —— ( ) —— (M)    ( ) —— ( )    \n"
            + "                                                            \n";
    assertEquals(output, dungeon.printDungeon());
    assertEquals(HitStatus.HIT, dungeon.shoot(Direction.EAST, 4));
    assertEquals(HitStatus.KILLED, dungeon.shoot(Direction.EAST, 4));
    assertEquals(HitStatus.MISS, dungeon.shoot(Direction.EAST, 4));
  }

  @Test
  public void testShootWrongDistance1() {
    RandomImpl.setSeed(5);
    DungeonModel dungeon = new DungeonModelImpl(6, 8, false, 10, 50, 5);
    dungeon.movePlayer(Direction.WEST);
    dungeon.movePlayer(Direction.WEST);
    String output = "                                                            \n"
            + "    ( ) —— ( )    ( ) —— ( ) —— ( ) —— (M)    ( ) —— ( )    \n"
            + "     |             |      |      |             |      |     \n"
            + "    ( )    ( ) —— (P) —— ( ) —— (#) —— (M)    ( )    ( )    \n"
            + "     |             |      |      |                    |     \n"
            + "    ( )    ( )    ( ) —— ( )    ( ) —— ( )    (&)    ( )    \n"
            + "     |      |      |                    |      |      |     \n"
            + "    ( ) —— ( ) —— (M) —— ( ) —— ( ) —— ( ) —— ( ) —— ( )    \n"
            + "     |      |      |      |      |             |      |     \n"
            + "    ( ) —— ( ) —— ( ) —— ( )    ( ) —— ( ) —— ( )    ( )    \n"
            + "            |      |             |             |      |     \n"
            + "    ( ) —— ( ) —— ( )    ( ) —— ( ) —— (M)    ( ) —— ( )    \n"
            + "                                                            \n";
    assertEquals(output, dungeon.printDungeon());
    assertEquals(HitStatus.MISS, dungeon.shoot(Direction.SOUTH, 1));
    assertEquals(HitStatus.MISS, dungeon.shoot(Direction.SOUTH, 3));
    assertEquals(HitStatus.MISS, dungeon.shoot(Direction.EAST, 4));
  }

  @Test
  public void testShootWrongDistance2() {
    RandomImpl.setSeed(0);
    DungeonModel model = new DungeonModelImpl(6, 8, false, 10, 50, 10);
    String output = "                                                            \n"
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
            + "                                                            \n";
    assertEquals(output, model.printDungeon());
    assertEquals(HitStatus.MISS, model.shoot(Direction.WEST, 3));
    assertEquals(HitStatus.MISS, model.shoot(Direction.WEST, 4));
    assertEquals(HitStatus.MISS, model.shoot(Direction.WEST, 5));
  }

  @Test
  public void testGameOverKilled() {
    RandomImpl.setSeed(0);
    DungeonModel model = new DungeonModelImpl(6, 8, false, 10, 50, 10);
    String output = "                                                            \n"
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
            + "                                                            \n";
    assertEquals(output, model.printDungeon());
    model.movePlayer(Direction.WEST);
    model.movePlayer(Direction.SOUTH);
    assertTrue(model.getCurrentLocation().containsOtyugh());
    assertSame(model.getGameStatus(), GameStatus.GAME_OVER_KILLED);

    try {
      model.movePlayer(Direction.SOUTH);
      fail();
    } catch (IllegalStateException e) {
      assertEquals("Game is already over", e.getMessage());
    }

    try {
      model.pickItem(Arrow.CROOKED_ARROW);
      fail();
    } catch (IllegalStateException e) {
      assertEquals("Game is already over", e.getMessage());
    }

    try {
      model.shoot(Direction.NORTH, 2);
      fail();
    } catch (IllegalStateException e) {
      assertEquals("Game is already over", e.getMessage());
    }
  }

  @Test
  public void testEscapeOtyugh() {
    RandomImpl.setSeed(0);
    DungeonModel model = new DungeonModelImpl(6, 8, false, 10, 50, 10);
    String output = "                                                            \n"
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
            + "                                                            \n";
    assertEquals(output, model.printDungeon());
    model.movePlayer(Direction.WEST);
    assertEquals(HitStatus.HIT, model.shoot(Direction.SOUTH, 1));
    model.movePlayer(Direction.SOUTH);
    assertTrue(model.getCurrentLocation().containsOtyugh());
    assertSame(model.getGameStatus(), GameStatus.GAME_CONTINUE);
    //Player Escapes the Injured Otyugh
    model.movePlayer(Direction.NORTH);
    model.movePlayer(Direction.SOUTH);
    //Player gets eaten by the same otyugh once coming back
    assertSame(model.getGameStatus(), GameStatus.GAME_OVER_KILLED);

    try {
      model.movePlayer(Direction.SOUTH);
      fail();
    } catch (IllegalStateException e) {
      assertEquals("Game is already over", e.getMessage());
    }
  }

  @Test
  public void testGameOverWin() {
    RandomImpl.setSeed(0);
    DungeonModel model = new DungeonModelImpl(6, 8, false, 10, 50, 1);
    String output = "                                                            \n"
            + "    ( )    ( )    ( ) —— ( ) —— ( ) —— ( )    ( ) —— (P)    \n"
            + "     |      |      |      |                    |            \n"
            + "    ( ) —— ( ) —— ( )    ( )    ( ) —— ( ) —— ( ) —— ( )    \n"
            + "            |             |      |      |                   \n"
            + "    ( )    ( ) —— ( ) —— ( )    ( ) —— ( ) —— ( ) —— ( )    \n"
            + "     |             |                    |             |     \n"
            + "    ( )    ( ) —— ( ) —— ( )    ( ) —— ( ) —— ( ) —— ( )    \n"
            + "     |      |             |      |      |      |      |     \n"
            + "    ( ) —— (&) —— ( ) —— ( ) —— ( )    ( ) —— ( ) —— ( )    \n"
            + "     |      |      |      |      |      |      |            \n"
            + "    ( ) —— ( ) —— ( )    ( ) —— ( )    ( ) —— ( ) —— ( )    \n"
            + "                                                            \n";
    assertEquals(output, model.printDungeon());
    model.movePlayer(Direction.WEST);
    model.movePlayer(Direction.SOUTH);
    model.movePlayer(Direction.WEST);
    model.movePlayer(Direction.SOUTH);
    model.movePlayer(Direction.SOUTH);
    model.movePlayer(Direction.WEST);
    model.movePlayer(Direction.SOUTH);
    model.movePlayer(Direction.WEST);
    model.movePlayer(Direction.WEST);
    model.shoot(Direction.WEST, 1);
    model.shoot(Direction.WEST, 1);
    model.movePlayer(Direction.WEST);
    assertSame(model.getGameStatus(), GameStatus.GAME_OVER_WIN);

    try {
      model.movePlayer(Direction.SOUTH);
      fail();
    } catch (IllegalStateException e) {
      assertEquals("Game is already over", e.getMessage());
    }
  }

  @Test
  public void testPrintDungeon() {
    String output = "                                                            \n"
            + "    (M)    ( )    ( ) —— ( ) —— ( ) —— ( )    ( ) —— (P)    \n"
            + "     |      |      |      |                    |            \n"
            + "    ( ) —— ( ) —— ( )    ( )    ( ) —— ( ) —— ( ) —— ( )    \n"
            + "            |             |      |      |                   \n"
            + "    (M)    ( ) —— ( ) —— ( )    ( ) —— ( ) —— ( ) —— ( )    \n"
            + "     |             |                    |             |     \n"
            + "    ( )    ( ) —— (M) —— ( )    ( ) —— ( ) —— ( ) —— ( )    \n"
            + "     |      |             |      |      |      |      |     \n"
            + "    ( ) —— (&) —— ( ) —— (M) —— ( )    ( ) —— ( ) —— ( )    \n"
            + "     |      |      |      |      |      |      |            \n"
            + "    ( ) —— ( ) —— ( )    ( ) —— ( )    ( ) —— ( ) —— ( )    \n"
            + "                                                            \n";
    assertEquals(output, nonWrappingDungeon.printDungeon());

    output = "                                                            \n"
            + "    ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— (P) —— ( ) —— ( )    \n"
            + "     |      |      |      |      |      |      |      |     \n"
            + "    ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— ( )    \n"
            + "     |      |      |      |      |      |      |      |     \n"
            + "    ( ) —— (&) —— ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— ( )    \n"
            + "     |      |      |      |      |      |      |      |     \n"
            + "    ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— ( )    \n"
            + "     |      |      |      |      |      |      |      |     \n"
            + "    ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— ( )    \n"
            + "     |      |      |      |      |      |      |      |     \n"
            + "    ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— ( )    \n"
            + "                                                            \n";
    assertEquals(output, nonWrappingDungeonFull.printDungeon());

    output = "                                                            \n"
            + "    ( ) —— ( )    ( )    ( ) —— ( ) —— ( ) —— ( ) —— ( )    \n"
            + "     |      |      |      |             |                   \n"
            + "    ( ) —— (P) —— ( ) —— ( ) —— ( ) —— ( ) —— ( )    (&)    \n"
            + "     |      |      |                                  |     \n"
            + "    ( ) —— ( )    ( )    ( ) —— ( )    ( ) —— ( ) —— ( )    \n"
            + "     |      |             |      |      |      |      |     \n"
            + "    ( ) —— ( ) —— ( )    ( ) —— ( )    ( ) —— ( ) —— ( )    \n"
            + "     |             |      |                    |      |     \n"
            + "    ( )    ( ) —— ( ) —— ( )    ( ) —— ( ) —— ( ) —— ( )    \n"
            + "                          |             |      |      |     \n"
            + "    ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— ( )    \n"
            + "                                                            \n";
    assertEquals(output, nonWrappingDungeonNoTreasure.printDungeon());

    output = "                                                            \n"
            + "    (P)    ( ) —— ( )    ( )    ( ) —— ( )    ( )    (&)    \n"
            + "     |             |      |             |      |      |     \n"
            + "    ( )    ( )    ( ) —— ( ) —— ( )    ( ) —— ( )    ( )    \n"
            + "     |      |             |                    |      |     \n"
            + "    ( ) —— ( ) —— ( ) —— ( )    ( )    ( )    ( )    ( )    \n"
            + "     |             |             |      |      |      |     \n"
            + "    ( ) —— ( )    ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— ( )    \n"
            + "            |             |                    |            \n"
            + "    ( )    ( )    ( ) —— ( ) —— ( ) —— ( )    ( ) —— ( )    \n"
            + "     |      |                           |             |     \n"
            + "    ( ) —— ( ) —— ( )    ( ) —— ( ) —— ( ) —— ( )    ( )    \n"
            + "                                                            \n";
    assertEquals(output, nonWrappingDungeonNoDegree.printDungeon());

    output = "     |      |             |      |                    |     \n"
            + " —— ( ) —— (M) —— (M)    ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— \n"
            + "     |                           |      |      |      |     \n"
            + " —— ( ) —— ( ) —— ( )    ( ) —— ( )    ( ) —— ( ) —— ( ) —— \n"
            + "     |      |      |             |      |      |      |     \n"
            + " —— ( )    ( )    ( ) —— ( ) —— (P) —— ( )    ( ) —— (M) —— \n"
            + "     |      |      |      |      |                    |     \n"
            + "    (&) —— ( )    ( )    ( )    ( ) —— ( ) —— ( )    ( )    \n"
            + "     |             |      |      |             |      |     \n"
            + "    ( )    ( ) —— ( ) —— ( )    ( ) —— (M) —— ( )    ( )    \n"
            + "     |      |      |      |             |      |      |     \n"
            + " —— ( )    ( ) —— ( )    ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— \n"
            + "     |      |             |      |                    |     \n";
    assertEquals(output, wrappingDungeon.printDungeon());

    output = "     |      |      |      |      |      |      |      |     \n"
            + " —— ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— \n"
            + "     |      |      |      |      |      |      |      |     \n"
            + " —— ( ) —— ( ) —— ( ) —— (&) —— ( ) —— ( ) —— ( ) —— ( ) —— \n"
            + "     |      |      |      |      |      |      |      |     \n"
            + " —— ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— \n"
            + "     |      |      |      |      |      |      |      |     \n"
            + " —— ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— \n"
            + "     |      |      |      |      |      |      |      |     \n"
            + " —— (P) —— ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— \n"
            + "     |      |      |      |      |      |      |      |     \n"
            + " —— ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— ( ) —— \n"
            + "     |      |      |      |      |      |      |      |     \n";
    assertEquals(output, wrappingDungeonFull.printDungeon());
    output = "     |      |      |      |                    |            \n"
            + " —— ( ) —— ( ) —— ( )    ( ) —— ( ) —— ( )    ( )    ( ) —— \n"
            + "            |                           |      |      |     \n"
            + " —— ( ) —— ( ) —— ( ) —— ( )    ( ) —— ( ) —— ( ) —— (&) —— \n"
            + "                   |      |      |      |      |      |     \n"
            + " —— ( ) —— ( )    ( ) —— ( ) —— ( ) —— ( )    ( ) —— ( ) —— \n"
            + "            |                    |      |      |      |     \n"
            + "    ( )    ( ) —— ( ) —— ( ) —— ( ) —— ( )    ( ) —— ( )    \n"
            + "     |      |      |                    |      |      |     \n"
            + " —— ( ) —— (P)    ( ) —— ( ) —— ( ) —— ( ) —— ( )    ( ) —— \n"
            + "     |      |      |      |             |      |      |     \n"
            + " —— ( )    ( ) —— ( ) —— ( )    ( ) —— ( )    ( ) —— ( ) —— \n"
            + "     |      |      |      |                    |            \n";
    assertEquals(output, wrappingDungeonNoTreasure.printDungeon());
    output = "     |      |      |             |      |      |      |     \n"
            + " —— ( )    ( )    ( ) —— ( )    ( ) —— ( ) —— ( )    ( ) —— \n"
            + "     |      |             |      |             |            \n"
            + " —— ( )    ( )    ( ) —— (&)    ( )    ( ) —— (P)    ( ) —— \n"
            + "     |                    |             |      |            \n"
            + " —— ( )    ( ) —— ( ) —— ( ) —— ( )    ( )    ( )    ( ) —— \n"
            + "     |                                         |            \n"
            + "    ( )    ( ) —— ( ) —— ( ) —— ( )    ( )    ( ) —— ( )    \n"
            + "                   |      |             |      |            \n"
            + " —— ( ) —— ( ) —— ( )    ( )    ( ) —— ( ) —— ( ) —— ( ) —— \n"
            + "            |      |                                        \n"
            + "    ( ) —— ( )    ( ) —— ( )    ( )    ( )    ( )    ( )    \n"
            + "     |      |      |             |      |      |      |     \n";
    assertEquals(output, wrappingDungeonNoDegree.printDungeon());
  }
}
import org.junit.Test;

import dungeoncontroller.DungeonAsyncController;
import dungeonmodel.Direction;
import dungeonmodel.DungeonModel;
import dungeonmodel.Key;
import dungeonview.DungeonView;

import static org.junit.Assert.*;

/**
 * Tests to check the methods of {@link DungeonAsyncController}.
 * Covers the test of construction fail and testing controller method calls in isolation.
 */
public class DungeonAsyncControllerTest {

  @Test
  public void testConstructorFail() {
    try {
      new DungeonAsyncController(null);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("View cannot be null.", e.getMessage());
    }
  }

  @Test
  public void testMockingModelandController() {
    StringBuilder modelLog = new StringBuilder("");
    DungeonModel loggingModel = new MockModelLogging(modelLog);
    StringBuilder viewLog = new StringBuilder("");
    DungeonView loggingView = new MockViewLogging(viewLog);
    DungeonAsyncController controller = new DungeonAsyncController(loggingModel, loggingView);
    controller.start();
    controller.movePlayer(Direction.NORTH);
    controller.pickItem(Key.DOOR_KEY);
    controller.shootArrow(Direction.NORTH, 1);
    controller.killMonster();
    controller.exitDungeon();
    controller.createNewModel(6, 8, false, 10, 50, 1, 1, 1, 0);
    controller.resetModel();
    assertEquals("Move: North\n" +
            "Pick: DOOR_KEY\n" +
            "Shoot: Direction = North, Distance = 1\n" +
            "Kill Monster called.\n" +
            "Exit Dungeon called.\n", modelLog.toString());
    assertEquals("setFeatures called\n" +
            "makeVisible called\n" +
            "showString called for: \n" +
            "refresh called\n" +
            "playSound called for: res\\dungeonSounds\\treasure_pick.wav\n" +
            "showString called for: Picked Key.\n" +
            "refresh called\n" +
            "showString called for: You shoot an arrow into the darkness.\n" +
            "refresh called\n" +
            "showString called for: Aboleth was killed.\n" +
            "refresh called\n" +
            "playSound called for: res\\dungeonSounds\\win.wav\n" +
            "showString called for: Congrats, you reached the end location.\n" +
            "refresh called\n" +
            "setModel called\n" +
            "refresh called\n" +
            "setModel called\n" +
            "refresh called\n", viewLog.toString());
  }
}
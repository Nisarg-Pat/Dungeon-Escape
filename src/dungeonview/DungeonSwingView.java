package dungeonview;

import dungeoncontroller.Features;
import dungeonmodel.Arrow;
import dungeonmodel.Direction;
import dungeonmodel.Item;
import dungeonmodel.Key;
import dungeonmodel.ReadOnlyDungeonModel;
import dungeonmodel.Treasure;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

/**
 * The Swing Implementation of the Dungeon.
 * User can view the visited Dungeon, details of the current location
 * and Actions possible by the user.
 * Contains a menubar that can be used to create a new dungeon, reset the existing dungeon,
 * quit the game, show details of the dungeon and player, see the controls and enter any cheat code.
 * Contains a dungeon panel where user can see the visited dungeon.
 * Contains a current location panel which shows the details of the current location,
 * including all the items and monsters in the location.
 * Contains a player panel, where player can see details of the items picked
 * and buttons to help in the adventure.
 */
public class DungeonSwingView extends JFrame implements DungeonView {

  private final DungeonPopup dungeonPopup;
  private final DungeonMenuBar dungeonMenuBar;
  private final DungeonPanel dungeonPanel;
  private final JScrollPane scrollPane;
  private final LocationPanel locationPanel;
  private final PlayerPanel playerPanel;

  private ReadOnlyDungeonModel model;

  private boolean isShootMode;
  private Direction shootDirection;
  private boolean isVisibleMode;

  /**
   * Constructor to create a DungeonSwingView.
   */
  public DungeonSwingView() {
    super("Dungeon Game");
    this.setLocation(100, 100);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.isShootMode = false;
    shootDirection = null;
    isVisibleMode = false;

    try {
      BufferedImage image = ImageIO.read(new File("dungeonImages\\logo.png"));
      setIconImage(image);
    } catch (IOException e) {
      // Ignore Logo
    }

    dungeonPopup = new DungeonPopup(this);

    this.setLayout(new BorderLayout());

    dungeonMenuBar = new DungeonMenuBar(this);
    this.setJMenuBar(dungeonMenuBar);

    dungeonPanel = new DungeonPanel(this);
    scrollPane = new JScrollPane(dungeonPanel);
    add(scrollPane, BorderLayout.CENTER);

    locationPanel = new LocationPanel(this);
    add(locationPanel, BorderLayout.WEST);

    playerPanel = new PlayerPanel(this);
    add(playerPanel, BorderLayout.SOUTH);

    this.setFocusable(true);
    this.requestFocus();
  }

  @Override
  public void setModel(ReadOnlyDungeonModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null.");
    }
    this.model = model;
    showString("");
    dungeonPopup.setDefaultCloseOperation(HIDE_ON_CLOSE);
  }

  @Override
  public void setFeatures(Features features) {
    if (features == null) {
      throw new IllegalArgumentException("Features cannot be null");
    }
    dungeonPopup.setFeatures(features);
    dungeonMenuBar.setFeatures(features);
    dungeonPanel.setFeatures(features);
    playerPanel.setFeatures(features);

    this.addKeyListener(new KeyListener() {

      @Override
      public void keyTyped(KeyEvent e) {
        //Empty
      }

      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_S) {
          if (model.getPlayerDescription().countArrows() == 0) {
            playerPanel.showString("Player does not have any arrows");
          } else {
            isShootMode = true;
            playerPanel.showString("Click on direction to shoot.");
          }
          return;
        }
        Map<Integer, Direction> keyDirectionMap = new HashMap<>();
        keyDirectionMap.put(KeyEvent.VK_UP, Direction.NORTH);
        keyDirectionMap.put(KeyEvent.VK_RIGHT, Direction.EAST);
        keyDirectionMap.put(KeyEvent.VK_DOWN, Direction.SOUTH);
        keyDirectionMap.put(KeyEvent.VK_LEFT, Direction.WEST);
        if (keyDirectionMap.containsKey(e.getKeyCode())) {
          if (isShootMode) {
            shootDirection = keyDirectionMap.get(e.getKeyCode());
            playerPanel.showString("Enter the distance(1-5)");
          } else {
            features.movePlayer(keyDirectionMap.get(e.getKeyCode()));
          }
          isShootMode = false;
          return;
        } else {
          isShootMode = false;
        }
        Map<Integer, Item> itemMap = new HashMap<>();
        itemMap.put(KeyEvent.VK_1, Treasure.DIAMOND);
        itemMap.put(KeyEvent.VK_2, Treasure.RUBY);
        itemMap.put(KeyEvent.VK_3, Treasure.SAPPHIRE);
        itemMap.put(KeyEvent.VK_4, Arrow.CROOKED_ARROW);
        itemMap.put(KeyEvent.VK_5, Key.DOOR_KEY);
        itemMap.put(KeyEvent.VK_NUMPAD1, Treasure.DIAMOND);
        itemMap.put(KeyEvent.VK_NUMPAD2, Treasure.RUBY);
        itemMap.put(KeyEvent.VK_NUMPAD3, Treasure.SAPPHIRE);
        itemMap.put(KeyEvent.VK_NUMPAD4, Arrow.CROOKED_ARROW);
        itemMap.put(KeyEvent.VK_NUMPAD5, Key.DOOR_KEY);
        if (itemMap.containsKey(e.getKeyCode())) {
          if (shootDirection != null) {
            features.shootArrow(shootDirection, e.getKeyCode() % 48);
          } else {
            features.pickItem(itemMap.get(e.getKeyCode()));
          }
          shootDirection = null;
          return;
        } else {
          shootDirection = null;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
          features.killMonster();
        }
        showString("");
      }

      @Override
      public void keyReleased(KeyEvent e) {
        //Empty
      }
    });
  }

  @Override
  public void refresh() {
    repaint();
  }

  @Override
  public void makeVisible() {
    openPopup();
  }

  @Override
  public void showErrorMessage(String error) {
    playerPanel.showString(error);
  }

  @Override
  public void playSound(String path) {
    if (path == null) {
      throw new IllegalArgumentException("Path cannot be null.");
    }
    try {
      AudioInputStream audioStream = AudioSystem.getAudioInputStream(
              new BufferedInputStream(new FileInputStream(path)));
      AudioFormat format = audioStream.getFormat();
      DataLine.Info info = new DataLine.Info(Clip.class, format);
      Clip audioClip = (Clip) AudioSystem.getLine(info);
      audioClip.open(audioStream);
      audioClip.start();
    } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
      //Ignore to not play the sound.
    }
  }

  @Override
  public void showString(String s) {
    playerPanel.showString(s);
  }

  protected void openPopup() {
    dungeonPopup.initPopup(getX() + 200, getY() + 200);
  }

  protected void setSizes(int rows, int columns) {
    setMinimumSize(new Dimension(500, 300));
    setSize(228 + 64 * Math.min(columns, 16) + 115 + 64, 64 * Math.min(rows, 9) + 162 + 210);
    scrollPane.getVerticalScrollBar().setUnitIncrement(rows);
    scrollPane.getHorizontalScrollBar().setUnitIncrement(columns);
    dungeonPanel.setPreferredSize(new Dimension(64 * columns + 100, 64 * rows + 100));
    locationPanel.setPreferredSize(new Dimension(192 + 100, 192 + 100));
    playerPanel.setPreferredSize(new Dimension(64 * columns + 100 + 128 + 100, 210));
  }

  protected ReadOnlyDungeonModel getModel() {
    return model;
  }

  protected boolean hasModel() {
    return model != null;
  }

  protected boolean isVisibleMode() {
    return isVisibleMode;
  }

  protected void setVisibleMode(boolean visibleMode) {
    isVisibleMode = visibleMode;
    refresh();
  }

  protected void setShootMode(boolean value) {
    isShootMode = value;
  }
}

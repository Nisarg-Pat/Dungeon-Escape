package dungeonview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;

import dungeoncontroller.Features;
import dungeonmodel.Arrow;
import dungeonmodel.Direction;
import dungeonmodel.DungeonModel;
import dungeonmodel.HitStatus;
import dungeonmodel.Item;
import dungeonmodel.Key;
import dungeonmodel.ReadOnlyDungeonModel;
import dungeonmodel.Treasure;

public class DungeonSpringView extends JFrame implements DungeonView {

  DungeonPopup dungeonPopup;
  DungeonMenuBar dungeonMenuBar;
  DungeonPanel dungeonPanel;
  JScrollPane scrollPane;
  LocationPanel locationPanel;
  PlayerPanel playerPanel;

  ReadOnlyDungeonModel model;

  boolean isShootMode;

  public DungeonSpringView() {
    super("Dungeon Game");
    this.setLocation(100, 100);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.isShootMode = false;

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
    if(model == null) {
      throw new IllegalArgumentException("Model cannot be null.");
    }
    this.model = model;
    showString("");
    dungeonPopup.setDefaultCloseOperation(HIDE_ON_CLOSE);
  }

  protected ReadOnlyDungeonModel getModel() {
    return model;
  }

  protected boolean hasModel() {
    return model != null;
  }

  @Override
  public void setFeatures(Features features) {
    dungeonPopup.setFeatures(features);
    dungeonMenuBar.setFeatures(features);
    locationPanel.setFeatures(features);
    playerPanel.setFeatures(features);

    this.addKeyListener(new KeyListener() {

      @Override
      public void keyTyped(KeyEvent e) {
      }

      @Override
      public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_S) {
          isShootMode = true;
          playerPanel.showString("Click on direction to shoot.");
          return;
        }
        Map<Integer, Direction> keyDirectionMap = new HashMap<>();
        keyDirectionMap.put(KeyEvent.VK_UP, Direction.NORTH);
        keyDirectionMap.put(KeyEvent.VK_RIGHT, Direction.EAST);
        keyDirectionMap.put(KeyEvent.VK_DOWN, Direction.SOUTH);
        keyDirectionMap.put(KeyEvent.VK_LEFT, Direction.WEST);
        if (keyDirectionMap.containsKey(e.getKeyCode())) {
          if(isShootMode) {
            features.shootArrow(keyDirectionMap.get(e.getKeyCode()), 1);
          } else {
            features.movePlayer(keyDirectionMap.get(e.getKeyCode()));
          }
          isShootMode = false;
          return;
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
          features.pickItem(itemMap.get(e.getKeyCode()));
          return;
        }

        if(e.getKeyCode() == KeyEvent.VK_D) {
          features.killMonster();
        }
        showString("");
      }

      @Override
      public void keyReleased(KeyEvent e) {
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

  protected void openPopup() {
    dungeonPopup.initPopup(getX() + 200, getY() + 200);
  }

  protected void setSizes(int rows, int columns) {
//    setExtendedState(JFrame.MAXIMIZED_BOTH);
//    setUndecorated(true);
    setMinimumSize(new Dimension(500, 300));
    setSize(228 + 64 * Math.min(columns, 16) + 115 + 64, 64 * Math.min(rows, 9) + 162+210);
    scrollPane.getVerticalScrollBar().setUnitIncrement(rows);
    scrollPane.getHorizontalScrollBar().setUnitIncrement(columns);
    dungeonPanel.setPreferredSize(new Dimension(64 * columns + 100, 64 * rows + 100));
    locationPanel.setPreferredSize(new Dimension(192 + 100, 192 + 100));
    playerPanel.setPreferredSize(new Dimension(64 * columns + 100 + 128 + 100, 210));
  }

  @Override
  public void showErrorMessage(String error) {
    playerPanel.showString(error);
//    System.out.println("Error: " + error);
//    JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void playSound(String s) {
    Utilities.playSound(s);
  }

  @Override
  public void showString(String s) {
    playerPanel.showString(s);
  }
}

package dungeonview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.*;

import dungeoncontroller.Features;
import dungeonmodel.Arrow;
import dungeonmodel.Direction;
import dungeonmodel.DungeonModel;
import dungeonmodel.Item;
import dungeonmodel.Treasure;

public class DungeonSpringView extends JFrame implements DungeonView {

  DungeonPanel dungeonPanel;
  LocationPanel locationPanel;
  JScrollPane scrollPane;
  DungeonMenuBar dungeonMenuBar;
  DungeonPopup dungeonPopup;

  boolean isShootMode;

  public DungeonSpringView() {
    super("Dungeon Game");
    this.setLocation(100, 100);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.isShootMode = false;

    dungeonMenuBar = new DungeonMenuBar(this);
    this.setJMenuBar(dungeonMenuBar);

    this.setLayout(new BorderLayout());

    dungeonPanel = new DungeonPanel();
    scrollPane = new JScrollPane(dungeonPanel);
    add(scrollPane, BorderLayout.CENTER);

    locationPanel = new LocationPanel();
    add(locationPanel, BorderLayout.WEST);

    dungeonPopup = new DungeonPopup(this);

    this.setFocusable(true);
    this.requestFocus();
  }

  @Override
  public void setModel(DungeonModel model) {
    dungeonPanel.setModel(model);
    locationPanel.setModel(model);
    dungeonPopup.setModel(model);
  }

  @Override
  public void setFeatures(Features features) {
    dungeonPopup.setFeatures(features);
    dungeonMenuBar.setFeatures(features);
    locationPanel.setFeatures(features);

    this.addKeyListener(new KeyListener() {

      @Override
      public void keyTyped(KeyEvent e) {
      }

      @Override
      public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_Z) {
          isShootMode = true;
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
        itemMap.put(KeyEvent.VK_NUMPAD1, Treasure.DIAMOND);
        itemMap.put(KeyEvent.VK_NUMPAD2, Treasure.RUBY);
        itemMap.put(KeyEvent.VK_NUMPAD3, Treasure.SAPPHIRE);
        itemMap.put(KeyEvent.VK_NUMPAD4, Arrow.CROOKED_ARROW);

        if (itemMap.containsKey(e.getKeyCode())) {
          features.pickItem(itemMap.get(e.getKeyCode()));
          return;
        }
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
    setSize(228 + 64 * Math.min(columns, 16) + 115, 64 * Math.min(rows, 9) + 162);
    scrollPane.getVerticalScrollBar().setUnitIncrement(rows);
    scrollPane.getHorizontalScrollBar().setUnitIncrement(columns);
    dungeonPanel.setPreferredSize(new Dimension(64 * columns + 100, 64 * rows + 100));
    locationPanel.setPreferredSize(new Dimension(128 + 100, 128 + 100));
  }

  @Override
  public void showErrorMessage(String error) {
    System.out.println("Error: " + error);
//    JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void playSound(String s) {
    Utilities.playSound(s);
  }
}

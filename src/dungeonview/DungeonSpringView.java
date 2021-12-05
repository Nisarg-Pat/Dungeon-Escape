package dungeonview;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import dungeoncontroller.Features;
import dungeonmodel.Direction;
import dungeonmodel.DungeonModel;

public class DungeonSpringView extends JFrame implements DungeonView {

  DungeonPanel dungeonPanel;
  LocationPanel locationPanel;
  JScrollPane scrollPane;
  DungeonMenuBar dungeonMenuBar;
  DungeonPopup dungeonPopup;

  public DungeonSpringView() {
    super("Dungeon Game");
    this.setLocation(100, 100);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

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
  public void setFeatures(Features features) {
    dungeonPopup.setFeatures(features);
    dungeonMenuBar.setFeatures(features);

    this.addKeyListener(new KeyListener() {

      @Override
      public void keyTyped(KeyEvent e) {
      }

      @Override
      public void keyPressed(KeyEvent e) {
        Map<Integer, Direction> keyDirectionMap = new HashMap<>();
        keyDirectionMap.put(KeyEvent.VK_UP, Direction.NORTH);
        keyDirectionMap.put(KeyEvent.VK_RIGHT, Direction.EAST);
        keyDirectionMap.put(KeyEvent.VK_DOWN, Direction.SOUTH);
        keyDirectionMap.put(KeyEvent.VK_LEFT, Direction.WEST);
        System.out.println("Key Pressed");
        if (keyDirectionMap.containsKey(e.getKeyCode())) {
          features.movePlayer(keyDirectionMap.get(e.getKeyCode()));
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

  @Override
  public void setModel(DungeonModel model) {
    dungeonPanel.setModel(model);
    locationPanel.setModel(model);
    dungeonPopup.setModel(model);
  }

  protected void openPopup() {
    dungeonPopup.initPopup(getX() + 200, getY() + 200);
  }

  protected void setSizes(int rows, int columns) {
//    setExtendedState(JFrame.MAXIMIZED_BOTH);
//    setUndecorated(true);
    setMinimumSize(new Dimension(500, 300));
    setSize(64 * Math.min(columns, 16) + 200, 64 * Math.min(rows, 9) + 200);
    scrollPane.getVerticalScrollBar().setUnitIncrement(rows);
    scrollPane.getHorizontalScrollBar().setUnitIncrement(columns);
    dungeonPanel.setPreferredSize(new Dimension(64 * columns + 100, 64 * rows + 100));
    locationPanel.setPreferredSize(new Dimension(64 * 2 + 100, 64 * 2 + 100));
  }

  @Override
  public void showErrorMessage(String error) {
    System.out.println("Error: " + error);
    JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
  }
}

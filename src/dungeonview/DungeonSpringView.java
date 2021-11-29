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
  JPanel buttonPanel;
  JButton moveButton;

  public DungeonSpringView(DungeonModel model) {
    super("Dungeon Game");
    this.setLocation(0, 0);
    this.setSize(64*model.getColumns()+200, 64*model.getRows()+200);
//    this.setResizable(false);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    this.setLayout(new BorderLayout());
    dungeonPanel = new DungeonPanel(model);
    dungeonPanel.setSize(64*model.getColumns(), 64*model.getRows());
    add(dungeonPanel, BorderLayout.CENTER);

    this.setFocusable(true);
    this.requestFocus();

//    buttonPanel = new JPanel();
//    buttonPanel.setLayout(new FlowLayout());
//    this.add(buttonPanel, BorderLayout.SOUTH);

//    moveButton = new JButton("Move");
//    buttonPanel.add(moveButton, BorderLayout.SOUTH);
  }


  @Override
  public void setFeatures(Features features) {
    this.addKeyListener(new KeyListener() {

      @Override
      public void keyTyped(KeyEvent e) {
        Map<Character, Direction> keyDirectionMap= new HashMap<>();
        keyDirectionMap.put('w', Direction.NORTH);
        keyDirectionMap.put('d', Direction.EAST);
        keyDirectionMap.put('s', Direction.SOUTH);
        keyDirectionMap.put('a', Direction.WEST);
        System.out.println("Key Pressed");
        features.movePlayer(keyDirectionMap.get(e.getKeyChar()));
      }

      @Override
      public void keyPressed(KeyEvent e) {
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
    setVisible(true);
  }
}

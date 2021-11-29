package dungeonview;

import java.awt.*;
import java.io.File;
import java.util.Map;
import java.util.Set;

import javax.swing.*;

import dungeonmodel.Direction;
import dungeonmodel.DungeonModel;
import structureddata.LocationDescription;
import structureddata.Position;

public class DungeonPanel extends JPanel {

  //TODO change to read-only
  DungeonModel model;

  public DungeonPanel(DungeonModel model) {
    this.model = model;
    this.setLayout(new GridLayout(model.getRows(), model.getColumns()));

  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    for (int i = 0; i < model.getRows(); i++) {
      for (int j = 0; j < model.getColumns(); j++) {
        ImageIcon icon = new ImageIcon(getImageName(model.getLocation(new Position(i, j)).getPossibleDirections()));
        g2d.drawImage(icon.getImage(), j * 64+100, i * 64+100, this);
      }
    }
  }

  private String getImageName(Set<Direction> directionMap) {
    StringBuilder sb = new StringBuilder("dungeonImages\\directions\\");
    if (directionMap.contains(Direction.NORTH)) {
      sb.append("N");
    }
    if (directionMap.contains(Direction.EAST)) {
      sb.append("E");
    }
    if (directionMap.contains(Direction.SOUTH)) {
      sb.append("S");
    }
    if (directionMap.contains(Direction.WEST)) {
      sb.append("W");
    }
    sb.append(".png");
    return sb.toString();
  }
}

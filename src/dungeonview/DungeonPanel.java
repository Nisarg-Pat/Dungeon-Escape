package dungeonview;

import java.awt.*;
import java.util.Set;

import javax.swing.*;

import dungeonmodel.Direction;
import dungeonmodel.DungeonModel;
import structureddata.LocationDescription;
import structureddata.Position;

class DungeonPanel extends JPanel {

  //TODO change to read-only
  DungeonModel model;

  DungeonPanel(DungeonModel model) {
    this.model = model;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    for (int i = 0; i < model.getRows(); i++) {
      for (int j = 0; j < model.getColumns(); j++) {
        ImageIcon icon = new ImageIcon(getImageName(model.getLocation(new Position(i, j)).getPossibleDirections()));
        g2d.drawImage(icon.getImage(), j * 64+100, i * 64+100, this);
      }
    }

    LocationDescription currentLocation = model.getCurrentLocation();
    int currentRow = currentLocation.getPosition().getRow();
    int currentColumn = currentLocation.getPosition().getColumn();
    System.out.println("Row: "+currentRow+", Column: "+currentColumn);
    g2d.setColor(Color.RED);
    g2d.fillOval( currentColumn*64+132 - 16/2, currentRow*64+132 - 16/2, 16, 16);
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

  protected void setModel(DungeonModel model) {
    this.model = model;
  }
}

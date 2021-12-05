package dungeonview;

import java.awt.*;
import java.util.Set;

import javax.swing.*;

import dungeonmodel.DungeonModel;
import structureddata.LocationDescription;
import structureddata.Position;

class DungeonPanel extends JPanel {

  //TODO change to read-only
  DungeonModel model;

  DungeonPanel() {
    this.model = null;
  }

  protected void setModel(DungeonModel model) {
    this.model = model;
  }

  @Override
  protected void paintComponent(Graphics g) {
    if (model == null) {
      throw new IllegalStateException("Model cannot be null when painting dungeon.");
    }
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    for (int i = 0; i < model.getRows(); i++) {
      for (int j = 0; j < model.getColumns(); j++) {
        ImageIcon icon = new ImageIcon(Utilities.getImageName(model.getLocation(new Position(i, j)).getPossibleDirections()));
        Position getLocationPosition = Utilities.getLocationPosition(i, j);
        g2d.drawImage(icon.getImage(), getLocationPosition.getColumn(), getLocationPosition.getRow(), this);
      }
    }

    LocationDescription currentLocation = model.getCurrentLocation();
    int currentRow = currentLocation.getPosition().getRow();
    int currentColumn = currentLocation.getPosition().getColumn();
    System.out.println("Row: " + currentRow + ", Column: " + currentColumn);
    g2d.setColor(Color.RED);
    Position getCurrentPosition = Utilities.getPointPosition(currentLocation);
    g2d.fillOval(getCurrentPosition.getColumn(), getCurrentPosition.getRow(), 16, 16);
  }
}

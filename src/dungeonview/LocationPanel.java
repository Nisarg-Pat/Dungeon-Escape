package dungeonview;

import java.awt.*;
import java.util.Set;

import javax.swing.*;

import dungeonmodel.Direction;
import dungeonmodel.DungeonModel;
import structureddata.LocationDescription;
import structureddata.Position;

public class LocationPanel extends JPanel {
  DungeonModel model;

  public LocationPanel() {
    this.model = null;
  }

  protected void setModel(DungeonModel model) {
    this.model = model;
  }

  @Override
  protected void paintComponent(Graphics g) {
    if (model == null) {
      throw new IllegalStateException("Model cannot be null when painting location.");
    }

    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    LocationDescription currentLocation = model.getCurrentLocation();
    int currentRow = currentLocation.getPosition().getRow();
    int currentColumn = currentLocation.getPosition().getColumn();

    ImageIcon icon = new ImageIcon(Utilities.getImageName(model.getLocation(new Position(currentRow, currentColumn)).getPossibleDirections()));
    g2d.drawImage(icon.getImage(), 50, 50, 128, 128, this);
  }
}

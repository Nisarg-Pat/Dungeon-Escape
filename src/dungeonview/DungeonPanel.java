package dungeonview;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.*;

import dungeonmodel.DungeonModel;
import dungeonmodel.DungeonModelImpl;
import dungeonmodel.ReadOnlyDungeonModel;
import structureddata.LocationDescription;
import structureddata.Position;

class DungeonPanel extends JPanel {

  DungeonSpringView view;

  DungeonPanel(DungeonSpringView view) {
    this.view = view;
  }

  @Override
  protected void paintComponent(Graphics g) {
    if (view.getModel() == null) {
      throw new IllegalStateException("Model cannot be null when painting dungeon.");
    }
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    ReadOnlyDungeonModel model = view.getModel();

    LocationDescription currentLocation = model.getCurrentLocation();
    int currentRow = currentLocation.getPosition().getRow();
    int currentColumn = currentLocation.getPosition().getColumn();

    for (int i = 0; i < model.getRows(); i++) {
      for (int j = 0; j < model.getColumns(); j++) {
        try {
          BufferedImage image = ImageIO.read(new File(Utilities.getImageName(model.getLocation(new Position(i, j)).getPossibleDirections())));
          if(i == currentRow && j == currentColumn) {
            image = Utilities.getStenchedImage(model.detectSmell(), currentLocation.containsOtyugh(), image);
          }
          Position getLocationPosition = Utilities.getLocationPosition(i, j);
          g2d.drawImage(image, getLocationPosition.getColumn(), getLocationPosition.getRow(), this);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    g2d.setColor(Color.RED);
    Position getCurrentPosition = Utilities.getPointPosition(currentLocation);
    g2d.fillOval(getCurrentPosition.getColumn(), getCurrentPosition.getRow(), 16, 16);
  }
}

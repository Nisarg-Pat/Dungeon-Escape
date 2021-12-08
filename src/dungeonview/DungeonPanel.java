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
import dungeonmodel.SmellLevel;
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

//    for (int i = 0; i < model.getRows(); i++) {
//      for (int j = 0; j < model.getColumns(); j++) {
//        ImageIcon icon = new ImageIcon(Utilities.getImageName(model.getLocation(new Position(i, j)).getPossibleDirections()));
//        Position getLocationPosition = Utilities.getLocationPosition(i, j);
//        g2d.drawImage(icon.getImage(), getLocationPosition.getColumn(), getLocationPosition.getRow(), this);
//      }
//    }

    for (int i = 0; i < model.getRows(); i++) {
      for (int j = 0; j < model.getColumns(); j++) {
        LocationDescription location = model.getLocation(new Position(i, j));
        Position getLocationPosition = Utilities.getLocationPosition(i, j);
        Image image;
        if(location.isVisited()) {
          image = new ImageIcon(Utilities.getImageName(location.getPossibleDirections())).getImage();
        } else {
//          image = new ImageIcon(Utilities.getImageName(location.getPossibleDirections())).getImage();
          image = new ImageIcon("dungeonImages\\blank.png").getImage();
        }
        g2d.drawImage(image, getLocationPosition.getColumn(), getLocationPosition.getRow(), this);
        if (i == currentRow && j == currentColumn) {
          SmellLevel level = model.detectSmell();
          if (level == SmellLevel.MORE_PUNGENT || currentLocation.containsOtyugh()) {
            image = new ImageIcon("dungeonImages\\stench02.png").getImage();
            g2d.drawImage(image, getLocationPosition.getColumn(), getLocationPosition.getRow(), this);
          } else if (level == SmellLevel.LESS_PUNGENT) {
            image = new ImageIcon("dungeonImages\\stench01.png").getImage();
            g2d.drawImage(image, getLocationPosition.getColumn(), getLocationPosition.getRow(), this);
          }
        }
//        if(location.containsAboleth()) {
//          g2d.setColor(Color.RED);
//          Position getCurrentPosition = Utilities.getPointPosition(location);
//          g2d.fillOval(getCurrentPosition.getColumn(), getCurrentPosition.getRow(), 16, 16);
//        }
      }
    }

    g2d.setColor(Color.GREEN);
    Position getCurrentPosition = Utilities.getPointPosition(currentLocation);
    g2d.fillOval(getCurrentPosition.getColumn(), getCurrentPosition.getRow(), 16, 16);
  }
}

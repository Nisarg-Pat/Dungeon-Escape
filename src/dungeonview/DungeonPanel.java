package dungeonview;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.*;

import dungeoncontroller.Features;
import dungeonmodel.Direction;
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

    for (int i = 0; i < model.getRows(); i++) {
      for (int j = 0; j < model.getColumns(); j++) {
        LocationDescription location = model.getLocation(new Position(i, j));
        Position getLocationPosition = Utilities.getLocationPosition(i, j);
        Image image;
        if (location.isVisited() || view.isVisibleMode()) {
          image = new ImageIcon(Utilities.getImageName(location.getPossibleDirections())).getImage();
        } else {
          image = new ImageIcon("dungeonImages\\blank.png").getImage();
        }
        g2d.drawImage(image, getLocationPosition.getColumn(), getLocationPosition.getRow(), this);
        if ((i == currentRow && j == currentColumn)) {
          SmellLevel level = model.detectSmell();
          if (level == SmellLevel.MORE_PUNGENT || currentLocation.containsOtyugh()) {
            image = new ImageIcon("dungeonImages\\stench02.png").getImage();
            g2d.drawImage(image, getLocationPosition.getColumn(), getLocationPosition.getRow(), this);
          } else if (level == SmellLevel.LESS_PUNGENT) {
            image = new ImageIcon("dungeonImages\\stench01.png").getImage();
            g2d.drawImage(image, getLocationPosition.getColumn(), getLocationPosition.getRow(), this);
          }
        }
        if (view.isVisibleMode()) {
          if (location.containsAboleth()) {
            g2d.setColor(Color.RED);
            Position getCurrentPosition = Utilities.getPointPosition(location);
            g2d.fillOval(getCurrentPosition.getColumn(), getCurrentPosition.getRow(), 16, 16);
          }

          if (location.containsOtyugh()) {
            g2d.setColor(Color.DARK_GRAY);
            Position getCurrentPosition = Utilities.getPointPosition(location);
            g2d.fillOval(getCurrentPosition.getColumn(), getCurrentPosition.getRow(), 16, 16);
          }

          if (location.containsThief()) {
            g2d.setColor(Color.ORANGE);
            Position getCurrentPosition = Utilities.getPointPosition(location);
            g2d.fillOval(getCurrentPosition.getColumn(), getCurrentPosition.getRow(), 16, 16);
          }

          if (location.hasKey()) {
            g2d.setColor(Color.YELLOW);
            Position getCurrentPosition = Utilities.getPointPosition(location);
            g2d.fillOval(getCurrentPosition.getColumn(), getCurrentPosition.getRow(), 16, 16);
          }

          if (location.getPosition().equals(model.getEndCave().getPosition())) {
            g2d.setColor(Color.BLUE);
            Position getCurrentPosition = Utilities.getPointPosition(location);
            g2d.fillOval(getCurrentPosition.getColumn(), getCurrentPosition.getRow(), 16, 16);
          }
        }
      }
    }

    g2d.setColor(Color.GREEN);
    Position getCurrentPosition = Utilities.getPointPosition(currentLocation);
    g2d.fillOval(getCurrentPosition.getColumn(), getCurrentPosition.getRow(), 16, 16);
  }

  protected void setFeatures(Features features) {
    MouseAdapter mouseAdapter = new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        Position coordinates = getPositionFromClick(e.getX(), e.getY());
        Position currentPosition = view.getModel().getCurrentLocation().getPosition();
        Direction direction = getDirection(currentPosition, coordinates);
        if (direction != null) {
          features.movePlayer(direction);
          view.setShootMode(false);
        }
      }
    };
    this.addMouseListener(mouseAdapter);
  }

  private Direction getDirection(Position currentPosition, Position coordinates) {
    int totalRows = view.getModel().getRows();
    int totalColumns = view.getModel().getColumns();
    int rowdiff = (currentPosition.getRow() - coordinates.getRow());
    if (Math.abs(rowdiff) == totalRows - 1) {
      rowdiff = -(rowdiff) / Math.abs(rowdiff);
    }
    int columndiff = currentPosition.getColumn() - coordinates.getColumn();
    if (Math.abs(columndiff) == totalColumns - 1) {
      columndiff = -(columndiff) / Math.abs(columndiff);
    }
    if (Math.abs(rowdiff) + Math.abs(columndiff) == 1) {
      if (rowdiff == 1) {
        return Direction.NORTH;
      } else if (rowdiff == -1) {
        return Direction.SOUTH;
      } else if (columndiff == 1) {
        return Direction.WEST;
      } else if (columndiff == -1) {
        return Direction.EAST;
      }
    }
    return null;
  }

  private Position getPositionFromClick(int x, int y) {
    x -= 50;
    if (x < 0) {
      x += 64 * view.getModel().getColumns();
    }
    if (x < 0) {
      x += 64 * view.getModel().getColumns();
    }
    y -= 50;
    if (y < 0) {
      y += 64 * view.getModel().getRows();
    }
    return new Position((y / 64) % view.getModel().getRows(), (x / 64) % view.getModel().getColumns());
  }
}

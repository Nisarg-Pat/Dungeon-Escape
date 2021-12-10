package dungeonview;

import dungeoncontroller.Features;
import dungeonmodel.Direction;
import dungeonmodel.ReadOnlyDungeonModel;
import dungeonmodel.SmellLevel;
import structureddata.LocationDescription;
import structureddata.Position;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * A panel to represent the full dungeon.
 * Locations not visited by the player is hidden.
 * Player can click on nearby possible locations to move to that location.
 * Visibility: package-private
 */
class DungeonPanel extends JPanel {

  //Intentionally kept DungeonSwingView to tightly couple DungeonPopup with DungeonSwingView
  // and access protected methods of DungeonSwingView.
  private final DungeonSwingView view;

  //Intentionally kept DungeonSwingView to tightly couple DungeonPopup with DungeonSwingView
  // and access protected methods of DungeonSwingView.
  protected DungeonPanel(DungeonSwingView view) {
    if (view == null) {
      throw new IllegalArgumentException("View cannot be null");
    }
    this.view = view;
  }

  @Override
  protected void paintComponent(Graphics g) {
    if (g == null) {
      throw new IllegalArgumentException("Graphics cannot be null");
    }
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
          image = new ImageIcon(
                  Utilities.getImageName(location.getPossibleDirections())).getImage();
        } else {
          image = new ImageIcon("dungeonImages\\blank.png").getImage();
        }
        g2d.drawImage(image, getLocationPosition.getColumn(), getLocationPosition.getRow(), this);
        if ((i == currentRow && j == currentColumn)) {
          SmellLevel level = model.detectSmell();
          if (level == SmellLevel.MORE_PUNGENT || currentLocation.containsOtyugh()) {
            image = new ImageIcon("dungeonImages\\stench02.png").getImage();
            g2d.drawImage(image, getLocationPosition.getColumn(),
                    getLocationPosition.getRow(), this);
          } else if (level == SmellLevel.LESS_PUNGENT) {
            image = new ImageIcon("dungeonImages\\stench01.png").getImage();
            g2d.drawImage(image, getLocationPosition.getColumn(),
                    getLocationPosition.getRow(), this);
          }
        }
        if (view.isVisibleMode()) {
          Position getCurrentPosition = Utilities.getPointPosition(location);
          if (location.containsOtyugh()) {
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillOval(getCurrentPosition.getColumn(), getCurrentPosition.getRow(), 16, 16);
          }

          if (location.containsThief()) {
            g2d.setColor(Color.ORANGE);
            g2d.fillOval(getCurrentPosition.getColumn(), getCurrentPosition.getRow(), 16, 16);
          }

          if (location.getPosition().equals(model.getEndCave().getPosition())) {
            g2d.setColor(Color.BLUE);
            g2d.fillOval(getCurrentPosition.getColumn(), getCurrentPosition.getRow(), 16, 16);
          }

          if (location.hasKey()) {
            g2d.setColor(Color.YELLOW);
            g2d.fillOval(getCurrentPosition.getColumn(), getCurrentPosition.getRow(), 16, 16);
          }

          if (location.containsAboleth()) {
            g2d.setColor(Color.RED);
            g2d.fillOval(getCurrentPosition.getColumn(), getCurrentPosition.getRow(), 16, 16);
          }

          if (location.hasPit()) {
            g2d.setColor(Color.MAGENTA);
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
    if (features == null) {
      throw new IllegalArgumentException("Features cannot be null.");
    }
    MouseAdapter mouseAdapter = new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        Direction direction = getDirection(view.getModel().getCurrentLocation().getPosition(),
                getPositionFromClick(e.getX(), e.getY()));
        if (direction != null) {
          features.movePlayer(direction);
          view.setShootMode(false);
        }
      }
    };
    this.addMouseListener(mouseAdapter);
  }

  private Direction getDirection(Position currentPosition, Position coordinates) {
    if (currentPosition == null || coordinates == null) {
      throw new IllegalArgumentException("CurrentPostion or Click Coordinates cannot be null");
    }
    int rowDiff = (currentPosition.getRow() - coordinates.getRow());
    if (Math.abs(rowDiff) == view.getModel().getRows() - 1) {
      rowDiff = -(rowDiff) / Math.abs(rowDiff);
    }
    int columnDiff = currentPosition.getColumn() - coordinates.getColumn();
    if (Math.abs(columnDiff) == view.getModel().getColumns() - 1) {
      columnDiff = -(columnDiff) / Math.abs(columnDiff);
    }
    if (Math.abs(rowDiff) + Math.abs(columnDiff) == 1) {
      if (rowDiff == 1) {
        return Direction.NORTH;
      } else if (rowDiff == -1) {
        return Direction.SOUTH;
      } else if (columnDiff == 1) {
        return Direction.WEST;
      } else if (columnDiff == -1) {
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
    return new Position((y / 64) % view.getModel().getRows(),
            (x / 64) % view.getModel().getColumns());
  }
}

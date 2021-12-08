package dungeonview;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;

import dungeoncontroller.Features;
import dungeonmodel.Arrow;
import dungeonmodel.Item;
import dungeonmodel.Key;
import dungeonmodel.SmellLevel;
import dungeonmodel.Treasure;
import structureddata.LocationDescription;
import structureddata.Position;

class LocationPanel extends JPanel {
  DungeonSpringView view;

  LocationPanel(DungeonSpringView view) {
    this.view = view;
  }

  @Override
  protected void paintComponent(Graphics g) {
    if (view.getModel() == null) {
      throw new IllegalStateException("Model cannot be null when painting location.");
    }

    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    LocationDescription currentLocation = view.getModel().getCurrentLocation();
    Map<Treasure, Integer> treasureMap = currentLocation.getTreasureMap();

    try {
      Image image = new ImageIcon(Utilities.getImageName(currentLocation.getPossibleDirections())).getImage();
      g2d.drawImage(image, 50, 50, 192, 192, this);
      SmellLevel level = view.getModel().detectSmell();
      if (level == SmellLevel.MORE_PUNGENT || currentLocation.containsOtyugh()) {
        image = new ImageIcon("dungeonImages\\stench02.png").getImage();
        g2d.drawImage(image, 50, 50, 192, 192, this);
      } else if (level == SmellLevel.LESS_PUNGENT || currentLocation.containsOtyugh()) {
        image = new ImageIcon("dungeonImages\\stench01.png").getImage();
        g2d.drawImage(image, 50, 50, 192, 192, this);
      }

      if (currentLocation.containsOtyugh() && currentLocation.containsAboleth()) {
        image = new ImageIcon("dungeonImages\\otyugh.png").getImage();
        g2d.drawImage(image, 82, 75, 64, 44, this);
        image = new ImageIcon("dungeonImages\\aboleth.png").getImage();
        g2d.drawImage(image, 160, 75, 44, 44, this);
      } else if (currentLocation.containsOtyugh()) {
        image = new ImageIcon("dungeonImages\\otyugh.png").getImage();
        g2d.drawImage(image, 114, 75, 64, 44, this);
      } else if (currentLocation.containsAboleth()) {
        image = new ImageIcon("dungeonImages\\aboleth.png").getImage();
        g2d.drawImage(image, 124, 75, 44, 44, this);
      }

      int[] changes = new int[]{0, 0};
      if (!currentLocation.isCave()) {
        changes = Utilities.getChanges(currentLocation.getPossibleDirections());
      }
      if (currentLocation.getPosition().equals(view.getModel().getEndCave().getPosition())) {
        image = new ImageIcon("dungeonImages\\door.png").getImage();
        g2d.drawImage(image, 114, 120, 64, 64, this);
        image = new ImageIcon("dungeonImages\\player.png").getImage();
        g2d.drawImage(image, 120 + 3 * changes[1], 180 + 3 * changes[0], 48, 48, this);
      } else {
        image = new ImageIcon("dungeonImages\\player.png").getImage();
        g2d.drawImage(image, 120 + 3 * changes[1], 120 + 3 * changes[0], 48, 48, this);
      }

      int i = 0;
      Map<Item, Integer> itemMap = new HashMap<>();
      itemMap.put(Treasure.DIAMOND, 1);
      itemMap.put(Treasure.RUBY, 2);
      itemMap.put(Treasure.SAPPHIRE, 3);
      itemMap.put(Arrow.CROOKED_ARROW, 4);
      itemMap.put(Key.DOOR_KEY, 5);

      g2d.setFont(new Font("default", Font.BOLD, 25));

      for (Treasure treasure : treasureMap.keySet()) {
        image = ImageIO.read(new File(Utilities.getImageName(treasure)));
        g2d.drawString(itemMap.get(treasure) + ".", 50, 225 + i * 50 + 64);
        g2d.drawImage(image, 100, 200 + i * 50 + 64, this);
        g2d.drawString("x" + treasureMap.get(treasure), 150, 225 + i * 50 + 64);
        i++;
      }
      if (currentLocation.countArrows() > 0) {
        image = ImageIO.read(new File(Utilities.getImageName(Arrow.CROOKED_ARROW)));
        g2d.drawString(itemMap.get(Arrow.CROOKED_ARROW) + ".", 50, 225 + i * 50 + 64);
        g2d.drawImage(image, 100, 200 + i * 50 + 13 + 64, 30, 7, this);
        g2d.drawString("x" + currentLocation.countArrows(), 150, 225 + i * 50 + 64);
        i++;
      }
      if (currentLocation.hasKey()) {
        image = ImageIO.read(new File(Utilities.getImageName(Key.DOOR_KEY)));
        g2d.drawString(itemMap.get(Key.DOOR_KEY) + ".", 50, 225 + i * 50 + 64);
        g2d.drawImage(image, 100, 200 + i * 50 + 64, 28, 28, this);
        g2d.drawString("x" + 1, 150, 225 + i * 50 + 64);
      }
    } catch (IOException e) {
      view.showErrorMessage(e.getMessage());
    }
  }

  public void setFeatures(Features features) {

  }
}

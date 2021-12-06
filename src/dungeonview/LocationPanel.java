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
import dungeonmodel.DungeonModel;
import dungeonmodel.Item;
import dungeonmodel.Treasure;
import structureddata.LocationDescription;

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
//    int currentRow = currentLocation.getPosition().getRow();
//    int currentColumn = currentLocation.getPosition().getColumn();
    Map<Treasure, Integer> treasureMap = currentLocation.getTreasureMap();

    try {
      BufferedImage image = ImageIO.read(new File(Utilities.getImageName(currentLocation.getPossibleDirections())));
      image = Utilities.getStenchedImage(model.detectSmell(), currentLocation.containsOtyugh(), image);
      g2d.drawImage(image, 50, 50, 128, 128, this);
      if(currentLocation.containsOtyugh()) {
        image = ImageIO.read(new File("dungeonImages\\otyugh.png"));
        g2d.drawImage(image, 75, 75, this);
      }

      int i = 0;
      Map<Item, Integer> itemMap = new HashMap<>();
      itemMap.put(Treasure.DIAMOND, 1);
      itemMap.put(Treasure.RUBY, 2);
      itemMap.put(Treasure.SAPPHIRE, 3);
      itemMap.put(Arrow.CROOKED_ARROW, 4);

      for (Treasure treasure : treasureMap.keySet()) {
        image = ImageIO.read(new File(Utilities.getImageName(treasure)));
        g2d.setFont(new Font("default", Font.BOLD, 25));
        g2d.drawString(String.valueOf(itemMap.get(treasure)+"."), 50, 225 + i * 50);
        g2d.drawImage(image, 100, 200 + i * 50, this);
        g2d.drawString(String.valueOf(treasureMap.get(treasure)), 150, 225 + i * 50);
        i++;
      }
      if (currentLocation.countArrows() > 0) {
        image = ImageIO.read(new File(Utilities.getImageName(Arrow.CROOKED_ARROW)));
        g2d.setFont(new Font("default", Font.BOLD, 25));
        g2d.drawString(String.valueOf(itemMap.get(Arrow.CROOKED_ARROW)+"."), 50, 225 + i * 50);
        g2d.drawImage(image, 100, 200 + i * 50 + 13, 30, 7, this);
        g2d.drawString(String.valueOf(currentLocation.countArrows()), 150, 225 + i * 50);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void setFeatures(Features features) {

  }
}
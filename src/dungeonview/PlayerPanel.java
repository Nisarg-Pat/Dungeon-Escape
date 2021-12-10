package dungeonview;

import dungeoncontroller.Features;
import dungeonmodel.Arrow;
import dungeonmodel.GameStatus;
import dungeonmodel.Item;
import dungeonmodel.Key;
import dungeonmodel.ReadOnlyDungeonModel;
import dungeonmodel.Treasure;
import structureddata.PlayerDescription;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;


/**
 * A panel to show the details of the player along with the buttons
 * that player can click like Play Again, Kill Monster and Open door whenever possible.
 * Visibility: package-private
 */
class PlayerPanel extends JPanel {
  //Intentionally kept DungeonSwingView to tightly couple DungeonPopup with DungeonSwingView
  // and access protected methods of DungeonSwingView.
  private final DungeonSwingView view;

  private final JButton playAgain;
  private final JButton killMonster;
  private final JButton openDoor;
  private final JTextArea textArea;

  private String outputString;

  //Intentionally kept DungeonSwingView to tightly couple DungeonPopup with DungeonSwingView
  // and access protected methods of DungeonSwingView.
  PlayerPanel(DungeonSwingView view) {
    if (view == null) {
      throw new IllegalArgumentException("View cannot be null");
    }
    this.view = view;
    outputString = "";
    this.setLayout(null);

    textArea = new JTextArea();
    textArea.setFont(new Font("default", Font.BOLD, 20));
    textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
    textArea.setBounds(350, 10, 450, 100);
    textArea.setLineWrap(true);
    textArea.setFocusable(false);
    this.add(textArea);

    playAgain = new JButton("Play Again");
    playAgain.setBounds(350, 120, 100, 50);
    this.add(playAgain);

    openDoor = new JButton("Open Door");
    openDoor.setBounds(500, 120, 100, 50);
    this.add(openDoor);

    killMonster = new JButton("Kill Monster");
    killMonster.setBounds(650, 120, 120, 50);
    this.add(killMonster);
  }

  @Override
  protected void paintComponent(Graphics g) {
    if (g == null) {
      throw new IllegalArgumentException("Graphics cannot be null");
    }
    if (view.getModel() == null) {
      throw new IllegalStateException("Model cannot be null when painting player description.");
    }

    super.paintComponent(g);

    List<Treasure> treasures = new ArrayList<>();
    treasures.add(Treasure.DIAMOND);
    treasures.add(Treasure.RUBY);
    treasures.add(Treasure.SAPPHIRE);

    ReadOnlyDungeonModel model = view.getModel();
    PlayerDescription player = model.getPlayerDescription();
    Map<Treasure, Integer> treasureMap = player.getCollectedTreasures();

    Graphics2D g2d = (Graphics2D) g;
    g2d.setFont(new Font("default", Font.BOLD, 20));
    int i = 0;
    for (Treasure treasure : treasures) {
      drawImage(treasure, treasureMap.getOrDefault(treasure, 0), i, g2d);
      i++;
    }
    drawImage(Arrow.CROOKED_ARROW, player.countArrows(), i, g2d);
    i++;
    drawImage(Key.DOOR_KEY, player.hasKey() ? 1 : 0, i, g2d);

    if (model.getGameStatus() == GameStatus.GAME_CONTINUE) {
      playAgain.setVisible(false);
      openDoor.setVisible(
              model.getCurrentLocation().getPosition().equals(model.getEndCave().getPosition()));
      if (model.getCurrentLocation().containsAboleth()) {
        killMonster.setVisible(true);
        textArea.setText("Location contains an Aboleth. Kill it before it sees you.");
      } else if (model.getCurrentLocation().hasPitNearby()) {
        textArea.setText("It looks like the ground is not stable.");
      } else {
        killMonster.setVisible(false);
        textArea.setText(outputString);
      }
    } else {
      playAgain.setVisible(true);
      openDoor.setVisible(false);
      killMonster.setVisible(false);
      textArea.setText(outputString);
    }
  }

  private void drawImage(Item item, int number, int i, Graphics2D g2d) {
    if (item == null) {
      throw new IllegalArgumentException("Item cannot be null");
    }
    if (g2d == null) {
      throw new IllegalArgumentException("Graphics cannot be null");
    }
    Image image = new ImageIcon(Utilities.getImageName(item)).getImage();
    g2d.drawString(item.getStringFromNumber(number) + ".", 50, 30 + i * 35);
    double shrinkPercentage = (25.0) / image.getHeight(this);
    g2d.drawImage(image, 150, i * 35 + 12,
            (int) (image.getWidth(this) * shrinkPercentage), 25, this);
    g2d.drawString("x" + number, 200, 30 + i * 35);
  }


  protected void setFeatures(Features features) {
    if (features == null) {
      throw new IllegalArgumentException("Features cannot be null.");
    }
    playAgain.addActionListener(l -> {
      features.resetModel();
      view.requestFocus();
    });
    killMonster.addActionListener(l -> {
      features.killMonster();
      view.requestFocus();
    });
    openDoor.addActionListener(l -> {
      features.exitDungeon();
      view.requestFocus();
    });
  }

  protected void showString(String string) {
    if (string == null) {
      throw new IllegalArgumentException("String to be shown cannot be null");
    }
    outputString = string;
    this.repaint();
  }
}

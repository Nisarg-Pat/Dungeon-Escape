package dungeonview;

import java.awt.*;

import javax.swing.*;

import dungeoncontroller.Features;
import dungeonmodel.Treasure;
import structureddata.PlayerDescription;

class DungeonMenuBar extends JMenuBar {

  DungeonSpringView view;

  JMenu menu, infoMenu, cheatMenu;
  JDialog dungeonDetailsDialogue, playerDetailsDialogue, cheatDialogue;
  JMenuItem resetMenuItem, createMenuItem, quitMenuItem, dungeonDialogueItem, playerDialogueItem, cheatItem;
  JButton cheatButton;
  JTextField cheatField;

  DungeonMenuBar(DungeonSpringView view) {
    super();
    this.view = view;
    menu = new JMenu("Settings");

    resetMenuItem = new JMenuItem("Reset Game");
    menu.add(resetMenuItem);

    createMenuItem = new JMenuItem("Create New Game");
    menu.add(createMenuItem);

    quitMenuItem = new JMenuItem("Quit");
    menu.add(quitMenuItem);

    infoMenu = new JMenu("Info");

    dungeonDialogueItem = new JMenuItem("Dungeon Details");
    infoMenu.add(dungeonDialogueItem);

    playerDialogueItem = new JMenuItem("Player Details");
    infoMenu.add(playerDialogueItem);

    cheatMenu = new JMenu("Cheat");
    cheatItem = new JMenuItem("Cheat Code");
    cheatMenu.add(cheatItem);

    cheatDialogue = new JDialog();
    cheatDialogue.setLayout(new GridLayout(2, 0));
    cheatField = new JTextField("");
    cheatButton = new JButton("Enter");
    cheatDialogue.add(cheatField);
    cheatDialogue.add(cheatButton);
    cheatDialogue.pack();
    cheatDialogue.setLocation(view.getX() + 100, view.getY() + 100);

    this.add(menu);
    this.add(infoMenu);
    this.add(cheatMenu);
  }


  protected void setFeatures(Features features) {
    resetMenuItem.addActionListener(l -> {
      features.resetModel();
    });
    createMenuItem.addActionListener(l -> {
      view.openPopup();
    });
    quitMenuItem.addActionListener(l -> {
      features.exitProgram();
    });
    dungeonDialogueItem.addActionListener(l -> {
      dungeonDetailsDialogue = new JDialog();
      dungeonDetailsDialogue.setLayout(new GridLayout(6, 0));
      dungeonDetailsDialogue.add(new JTextArea(String.format("Rows: %d", view.getModel().getRows())));
      dungeonDetailsDialogue.add(new JTextArea(String.format("Columns: %d", view.getModel().getColumns())));
      dungeonDetailsDialogue.add(new JTextArea(String.format("IsWrapped: %s", view.getModel().getWrapped())));
      dungeonDetailsDialogue.add(new JTextArea(String.format("Degree of Connectivity: %d", view.getModel().getDegree())));
      dungeonDetailsDialogue.add(new JTextArea(String.format("Percentage of Caves having Treasure: %d", view.getModel().getPercentageItems())));
      dungeonDetailsDialogue.add(new JTextArea(String.format("Number of Otyughs: %d", view.getModel().countOtyughs())));
      dungeonDetailsDialogue.pack();
      dungeonDetailsDialogue.setLocation(view.getX() + 100, view.getY() + 100);
      dungeonDetailsDialogue.setVisible(true);
    });
    playerDialogueItem.addActionListener(l -> {
      playerDetailsDialogue = new JDialog();
      playerDetailsDialogue.setLayout(new GridLayout(5, 0));
      PlayerDescription playerDescription = view.getModel().getPlayerDescription();
      playerDetailsDialogue.add(new JTextArea(String.format("Diamonds: %d", playerDescription.getCollectedTreasures().getOrDefault(Treasure.DIAMOND, 0))));
      playerDetailsDialogue.add(new JTextArea(String.format("Ruby: %d", playerDescription.getCollectedTreasures().getOrDefault(Treasure.RUBY, 0))));
      playerDetailsDialogue.add(new JTextArea(String.format("Sapphire: %d", playerDescription.getCollectedTreasures().getOrDefault(Treasure.SAPPHIRE, 0))));
      playerDetailsDialogue.add(new JTextArea(String.format("Arrows: %d", playerDescription.countArrows())));
      playerDetailsDialogue.add(new JTextArea(String.format("Keys: %d", playerDescription.hasKey() ? 1 : 0)));
      playerDetailsDialogue.pack();
      playerDetailsDialogue.setLocation(view.getX() + 100, view.getY() + 100);
      playerDetailsDialogue.setVisible(true);
    });
    cheatItem.addActionListener(l -> {
      cheatDialogue.setVisible(true);
    });
    cheatButton.addActionListener(l -> {
      String text = cheatField.getText();
      if (text.equals("Show Dungeon")) {
        view.setVisibleMode(true);
      } else if (text.equals("Dont Show Dungeon")) {
        view.setVisibleMode(false);
      }
      cheatField.setText("");
      cheatDialogue.setVisible(false);
    });
  }
}

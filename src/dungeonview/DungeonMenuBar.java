package dungeonview;

import java.awt.*;

import javax.swing.*;

import dungeoncontroller.Features;
import dungeonmodel.Treasure;
import structureddata.PlayerDescription;

class DungeonMenuBar extends JMenuBar {

  DungeonSpringView view;

  JMenu menu, infoMenu, cheatMenu, controlsMenu;
  JDialog dungeonDetailsDialogue, playerDetailsDialogue, cheatDialogue, controlsDialogue;
  JMenuItem resetMenuItem, createMenuItem, quitMenuItem, dungeonDialogueItem, playerDialogueItem, cheatItem, controlsItem;
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

    controlsMenu = new JMenu("Controls");
    controlsItem = new JMenuItem("Show Controls");
    controlsMenu.add(controlsItem);

    controlsDialogue = new JDialog();
    controlsDialogue.setLayout(new GridLayout(10, 0));
    controlsDialogue.setLocation(view.getX() + 100, view.getY() + 100);
    controlsDialogue.add(new JTextArea("1. Use Arrow Keys or Click on Adjacent Cells to Move."));
    controlsDialogue.add(new JTextArea(""));
    controlsDialogue.add(new JTextArea("2. Click 1,2,3,4,5 to pick up respective items mentioned below location."));
    controlsDialogue.add(new JTextArea(""));
    controlsDialogue.add(new JTextArea("3. Click S and then Arrow Key and then number(1-5) to shoot an arrow."));
    controlsDialogue.add(new JTextArea(""));
    controlsDialogue.add(new JTextArea("4. Click D or Kill Monster button to kill the Moving Monster."));
    controlsDialogue.add(new JTextArea(""));
    controlsDialogue.add(new JTextArea("5. Click Open Door button to open the door. Requires a key."));
    controlsDialogue.add(new JTextArea(""));
    controlsDialogue.pack();


    this.add(menu);
    this.add(infoMenu);
    this.add(controlsMenu);
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
      dungeonDetailsDialogue.setLayout(new GridLayout(8, 0));
      dungeonDetailsDialogue.add(new JTextArea(String.format("Rows: %d", view.getModel().getRows())));
      dungeonDetailsDialogue.add(new JTextArea(String.format("Columns: %d", view.getModel().getColumns())));
      dungeonDetailsDialogue.add(new JTextArea(String.format("IsWrapped: %s", view.getModel().getWrapped())));
      dungeonDetailsDialogue.add(new JTextArea(String.format("Degree of Connectivity: %d", view.getModel().getDegree())));
      dungeonDetailsDialogue.add(new JTextArea(String.format("Percentage of Caves having Treasure: %d", view.getModel().getPercentageItems())));
      dungeonDetailsDialogue.add(new JTextArea(String.format("Number of Otyughs: %d", view.getModel().countOtyughs())));
      dungeonDetailsDialogue.add(new JTextArea(String.format("Number of Aboleths: %d", view.getModel().countAboleth())));
      dungeonDetailsDialogue.add(new JTextArea(String.format("Number of Thieves: %d", view.getModel().countThief())));
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
    controlsItem.addActionListener(l -> {
      controlsDialogue.setVisible(true);
    });
  }
}

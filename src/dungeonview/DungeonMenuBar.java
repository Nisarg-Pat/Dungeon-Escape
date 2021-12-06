package dungeonview;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;

import dungeoncontroller.Features;
import dungeonmodel.DungeonModel;
import dungeonmodel.Treasure;

class DungeonMenuBar extends JMenuBar {

  DungeonSpringView view;

  JMenu menu;
  JMenuItem resetMenuItem, createMenuItem, quitMenuItem;

  DungeonMenuBar(DungeonSpringView view){
    super();
    this.view = view;
    menu = new JMenu("Settings");
    menu.setMnemonic(KeyEvent.VK_A);
    menu.getAccessibleContext().setAccessibleDescription(
            "The only menu in this program that has menu items");
    this.add(menu);

    resetMenuItem = new JMenuItem("Reset Game");
    menu.add(resetMenuItem);

    createMenuItem = new JMenuItem("Create New Game");
    menu.add(createMenuItem);

    quitMenuItem = new JMenuItem("Quit");
    menu.add(quitMenuItem);

    this.add(menu);
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
  }
}

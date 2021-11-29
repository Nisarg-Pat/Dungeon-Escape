package dungeonview;

import javax.swing.*;

import dungeoncontroller.DungeonAsyncController;
import dungeonmodel.DungeonModel;

public class DungeonSpringView extends JFrame implements DungeonView {

  DungeonPanel dungeonPanel;

  public DungeonSpringView(DungeonModel model) {
    super("Dungeon Game");
    this.setLocation(200, 200);
    this.setSize(64*model.getColumns()+200, 64*model.getRows()+200);
//    this.setResizable(false);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    dungeonPanel = new DungeonPanel(model);
    dungeonPanel.setSize(64*model.getColumns(), 64*model.getRows());
    add(dungeonPanel);
  }


  @Override
  public void addClickListener(DungeonAsyncController listener) {

  }

  @Override
  public void refresh() {
    repaint();
  }

  @Override
  public void makeVisible() {
    setVisible(true);
  }
}

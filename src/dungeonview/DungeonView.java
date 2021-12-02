package dungeonview;

import dungeoncontroller.Features;
import dungeonmodel.DungeonModel;

public interface DungeonView {

  void setFeatures(Features features);

  void refresh();

  void makeVisible();

  void setModel(DungeonModel model);

  void showErrorMessage(String error);
}

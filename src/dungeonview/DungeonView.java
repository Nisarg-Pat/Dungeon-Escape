package dungeonview;

import dungeoncontroller.Features;
import dungeonmodel.DungeonModel;
import dungeonmodel.ReadOnlyDungeonModel;

public interface DungeonView {

  void setFeatures(Features features);

  void refresh();

  void makeVisible();

  void setModel(ReadOnlyDungeonModel model);

  void showErrorMessage(String error);

  void playSound(String s);

  void showString(String s);
}

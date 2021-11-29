package dungeonview;

import dungeoncontroller.Features;

public interface DungeonView {

  void setFeatures(Features features);

  void refresh();

  void makeVisible();
}

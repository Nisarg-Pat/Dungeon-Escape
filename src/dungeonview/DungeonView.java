package dungeonview;

import dungeoncontroller.DungeonAsyncController;

public interface DungeonView {


  void addClickListener(DungeonAsyncController listener);

  void refresh();

  void makeVisible();
}

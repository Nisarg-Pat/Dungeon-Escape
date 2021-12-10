package dungeonview;

import dungeoncontroller.Features;
import dungeonmodel.ReadOnlyDungeonModel;

/**
 * A view for the DungeonModel.
 * Contains methods such that a Graphical view of Dungeon can extend this
 * and implement it for better representation of the Dungeon.
 */
public interface DungeonView {

  /**
   * Sets the features to the view.
   *
   * @param features The features to be set in the view.
   */
  void setFeatures(Features features);

  /**
   * Refreshes the view for current representation.
   */
  void refresh();

  /**
   * Makes the view visible.
   */
  void makeVisible();

  /**
   * Sets the ReadOnlyDungeonModel to the view so that
   * read only methods can be used directly from the view.
   *
   * @param model The read-only dungeon model.
   */
  void setModel(ReadOnlyDungeonModel model);

  /**
   * Shows error message in the view.
   *
   * @param error The error string to be shown in the view.
   */
  void showErrorMessage(String error);

  /**
   * Plat the sound.
   *
   * @param path the path of the soundtrack.
   */
  void playSound(String path);

  /**
   * Shows a string in the view.
   *
   * @param string The string to be shown in the view.
   */
  void showString(String string);
}

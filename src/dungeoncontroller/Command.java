package dungeoncontroller;

import dungeonmodel.DungeonModel;

/**
 * A command to be executed by DungeonControllerImpl.
 * Player will have these commands as options to interact with the dungeon model.
 * Visibility: Package - private
 */
interface Command {
  /**
   * Executes the command for a particular model.
   *
   * @param model THe model to which to execute the command.
   */
  void execute(DungeonModel model);
}

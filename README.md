# Dungeon Escape

## Overview

Dungeon Escape is an escape game that contains a dungeon with a network of tunnels and caves that
are interconnected so that player can explore the entire world by traveling from cave to cave
through the tunnels that connect them.

A location in the dungeon where a player can explore, can be connected to at most four (4) other
locations: one to the north, one to the east, one to the south, and one to the west. A location can
further be classified as tunnel (which has exactly 2 entrances) or a cave (which has 1, 3 or 4
entrances).

Location can also contain Monsters like [Otyughs](https://forgottenrealms.fandom.com/wiki/Otyugh)
and [Aboleths](https://forgottenrealms.fandom.com/wiki/Aboleth). Otyughs are extremely smelly
creatures that lead solitary lives and would eat player if player enters its cave. Player can
collect arrows and shoot at a direction and distance in hope to kill an Otyugh. Aboleths are silent
roaming monsters that will kill the player instantly when it sees him. Player can kill Aboleth with
hand on before Aboleth sees the player.

The program simulates these basic properties of an adventure game where the player can move from one
location to other and collecting the treasures available in each location.

## List of Features of the Program

* The dungeon is generated randomly each time the game is played.
* Dungeon is represented as a 2D grid.
* There is a path from every cave in the dungeon to every other cave in the dungeon.
* Each dungeon can be constructed with a degree of interconnectivity. We define an interconnectivity
  = 0 when there is exactly one path from every cave in the dungeon to every other cave in the
  dungeon. Increasing the degree of interconnectivity increases the number of paths between caves.
* Dungeon can wrap itself around the edges.
* One cave is randomly selected as the start and one cave is randomly selected to be the end. The
  path between the start and the end locations is least of length 5.
* Each Cave can support three types of treasure: diamonds, rubies, and sapphires.
* Treasure are added to a specified percentage of caves. A cave can have more than one treasure.
* Each Location can have Crooked Arrows based on the same percentage.
* Player to enter the dungeon at the start.
* There is always at least one Otyugh in the dungeon located at the specially designated end cave.
* There is never an Otyugh at the start.
* Otyugh can be detected by smell.
* Player entering cave with living Otyugh will be killed.
* Player starts with 3 crooked arrows.
* It takes 2 hits to kill an Otyugh. Players has a 50% chance of escaping if the Otyugh if they
  enter a cave of an injured Otyugh that has been hit by a single crooked arrow.
* Player can move from their current location.
* Player can pick up treasure or arrow that is located in their same location.
* A player that has arrows, can attempt to slay an Otyugh by specifying a direction and distance in
  which to shoot their crooked arrow.
* Multiple Aboleth roams in the dungeon. Every alive Aboleth moves every 2 seconds.
* Player can kill an Aboleth before Aboleth starts moving again to adjacent location.
* Aboleth will kill the player during its movement.
* The tunnels in dungeon can contain thieves that will steal some treasure if player has any.
* The thief will then relocate to a different tunnel in the dungeon
* Player can fall into a pit and will be unable to move for 5 seconds and do any action. Player can
  sense a pit nearby.
* Player requires a key to open door in the end cave. Player needs to find the key through exploring
  the dungeon first.
* Game ends if player gets killed by a Monster or player successfully opens the end door and wins.

## Difference in Modes

The game can be played in 2 modes: Console mode and GUI mode.

1) Console mode requires Players to type the actions as mentioned in next section. Console mode does
   not feature Aboleths, Thieves, Pits and requirement of Keys(Same as Project 4).
2) GUI mode requires players to press keys and click buttons to interact with dungeon.

## How to Run

1) The res/ folder contains a Project05.jar file which can be run directly in IntelliJ or Eclipse
   Ide. The following command line arguments are required to play in Console mode:
    * rows: Number of rows in the dungeon. Should be at least 6.
    * columns: Number of Columns in the dungeon. Should be at least 6.
    * isWrapped: Whether the dungeon is wrapped around its end
    * degree: The degree of interconnectivity for the dungeon.
    * percentage: The percentage of caves having treasure in it. Should be between 0 and 100.
    * numOtyughs: Number of Otyughs in the dungeon. Should be at least 1.
2) The arguments should be in this specific order: rows columns isWrapped degree percentage
   numOtyughs
3) Use the following command for console mode: java -jar Project05.jar rows columns isWrapped degree
   percentage numOtyughs.
4) example-> java -jar Project05.jar 6 8 false 10 50 10
5) Use the following command for GUI mode from the main folder: java -jar res\Project05.jar NOTE:
   Due to the dependencies on Images and Sounds, it is required to run the command from the base
   folder and not res folder. Calling Project05.jar from res folder will not work as it will not
   load images and sounds.

## How to Use

Console Mode:

1) The dungeon will be randomly generated based on the command line inputs.
2) The description of the current location with Possible directions and the items in the location
   will be shown.
3) Player has Option to select one of the commands: (Move) M, (Pick) P, (Shoot) S.
4) Selecting one of the options will lead to their corresponding prompts.
    1) Move will require a direction: (North) N, (East) E, (South) S, (West) W
    2) Pick will require an item: (Diamond) D, (Ruby) R, (Sapphire) S, (Arrow) A
    3) Shoot will require a direction and a distance(between 1 and 5)';
5) Follow the prompts to explore the dungeon and reach the end location without dying to win.

GUI Mode:

1) A Popup will occur to create the dungeon and player needs to enter valid details to start a new
   Game.
2) On Clicking start player can see the current location, player details and dungeon on screen.
3) On the menu bar player can see the following:
    1) Settings: Player can create a new dungeon, reset the current dungeon or quit the game.
    2) Info: Player can look at dungeon or player description
    3) Controls: Player can look at the controls for the game
    4) Cheat: Player can enter Cheat code. eg: "Show Dungeon" and "Hide Dungeon"
4) Player needs to explore the dungeon, find items and end cave, and exit the dungeon successfully.
5) Controls are listed as below:
    1. Use Arrow Keys or Click on Adjacent Cells to Move.
    2. Click 1,2,3,4,5 to pick up respective items mentioned below location.
    3. Click S and then Arrow Key and then number(1-5) to shoot an arrow.
    4. Click D or Kill Monster button to kill the Moving Monster.
    5. Click Open Door button to open the door. Requires a key.

## Description of Examples/Sample Images

Sample Images are kept in res/sample_Images. Details of each image is as follows:

1) sample_1.png: The intitial popup to setup dungeon.
2) sample_2.png: The GUI just after starting the game.
3) sample_3.png: Player getting eaten by Aboleth.
4) sample_4.png: Player shooting and hitting an Otyugh.
5) sample_5.png: Player reaching the end location and opening the door.
6) sample_6.png: Thief stealing treasure from Player.
7) sample_7.png: Player falling in a pit.
8) sample_8.png: Trying to open door without Key.
9) sample_9.png: Picking an Item

## Design Changes

The following are the design changes from the original documents:

1) New Panels are added in DungeonView like LocationPanel, PlayerPanel, DungeonMenuBar, DungeonPopup
   instead of a single DungeonPanel.
2) DungeonAsyncController and DungeonView no longer has model as a required parameter.
3) ReadOnlyDungeonModel is used by View to get direct read-only info from Model.
4) New Features and functionalities are added to support Moving monster, Thieves, and Pits.

## UI Changes

The following are the UI changes from the original UI:

1) Info from current location is no longer displayed in the same space as the whole dungeon.
2) A seperate panel for current location description is added to get better display of the location.
3) Player panel is added below the Dungeon to give current items held by player.
4) Menubar is added.

## Assumptions

1) The treasures are distributed in the caves with the following probabilities:
    1) 1 treasure: 50%
    2) 2 treasures: 30%
    3) 3 treasures: 20%
2) The minimum rows and columns of the dungeon could be 6.
3) Player can find upto 3 arrows in a location.
4) Aboleths will see the player and kill only if the Aboleth tries to move. For the 2 dormant
   seconds, Player has chance to kill Aboleth.
5) Thieves can reside only in tunnels.
6) Pits can be found anyplace. Thus player will get stuck for 5 seconds and after that player gets
   out of pit and can do any action he likes from the same place.

## Limitations

1) The player cannot directly move to the cave connected to current cave. Player will stop at
   adjacent tunnel first.
2) Player can pick only one item at a time. For user to pick all items, separate commands need to be
   entered.
3) The GUI will not show more than 2 monsters in a dungeon which is a rare but possible scenario.

## Citations

1. Modifying Kruskal's Algorithm to Build the Dungeon.
   https://northeastern.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=7b3154e5-7740-4130-954f-adc201647fc8&start=3.952948
2. Thread reference for Aboleth:
   https://www.geeksforgeeks.org/killing-threads-in-java/
3. Sounds for the Dungeon:
   https://themushroomkingdom.net/wav.shtml

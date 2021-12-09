package dungeonview;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import dungeonmodel.Arrow;
import dungeonmodel.Direction;
import dungeonmodel.Item;
import dungeonmodel.Key;
import dungeonmodel.SmellLevel;
import dungeonmodel.Treasure;
import structureddata.LocationDescription;
import structureddata.PlayerDescription;
import structureddata.Position;

class Utilities {

  static int X_SPACE = 50;
  static int Y_SPACE = 50;

  static int IMAGE_WIDTH = 64;
  static int IMAGE_HEIGHT = 64;

  protected static String getImageName(Set<Direction> directionMap) {
    StringBuilder sb = new StringBuilder("dungeonImages\\directions\\");
    if (directionMap.contains(Direction.NORTH)) {
      sb.append("N");
    }
    if (directionMap.contains(Direction.EAST)) {
      sb.append("E");
    }
    if (directionMap.contains(Direction.SOUTH)) {
      sb.append("S");
    }
    if (directionMap.contains(Direction.WEST)) {
      sb.append("W");
    }
    sb.append(".png");
    return sb.toString();
  }

  protected static String getImageName(Item item) {
    if (item == Treasure.RUBY) {
      return "dungeonImages\\items\\ruby.png";
    } else if (item == Treasure.SAPPHIRE) {
      return "dungeonImages\\items\\sapphire.png";
    } else if (item == Treasure.DIAMOND) {
      return "dungeonImages\\items\\diamond.png";
    } else if (item == Arrow.CROOKED_ARROW) {
      return "dungeonImages\\items\\arrow.png";
    } else if (item == Key.DOOR_KEY) {
      return "dungeonImages\\items\\key.png";
    }
    return "";
  }

  protected static Position getLocationPosition(int row, int column) {
    return new Position(row * IMAGE_HEIGHT + Y_SPACE, column * IMAGE_WIDTH + X_SPACE);
  }

  protected static Position getPointPosition(LocationDescription location) {
    int row = location.getPosition().getRow() * IMAGE_HEIGHT + Y_SPACE + 32 - 8;
    int column = location.getPosition().getColumn() * IMAGE_WIDTH + X_SPACE + 32 - 8;

    if (!location.isCave()) {
      int[] changes = getChanges(location.getPossibleDirections());
      row += changes[0];
      column += changes[1];
    }
    return new Position(row, column);
  }

  public static int[] getChanges(Set<Direction> directionSet) {
    int[] changes = new int[2];
    if (directionSet.contains(Direction.NORTH) && directionSet.contains(Direction.EAST)) {
      changes[0] -= 9;
      changes[1] += 9;
    }
    if (directionSet.contains(Direction.NORTH) && directionSet.contains(Direction.WEST)) {
      changes[0] -= 9;
      changes[1] -= 9;
    }
    if (directionSet.contains(Direction.EAST) && directionSet.contains(Direction.SOUTH)) {
      changes[0] += 9;
      changes[1] += 9;
    }
    if (directionSet.contains(Direction.SOUTH) && directionSet.contains(Direction.WEST)) {
      changes[0] += 9;
      changes[1] -= 9;
    }
    return changes;
  }

//  protected static BufferedImage overlay(Image starting, String fpath, int offset) throws IOException {
//    BufferedImage overlay = ImageIO.read(new File(fpath));
//    int w = Math.max(starting.getWidth(), overlay.getWidth());
//    int h = Math.max(starting.getHeight(), overlay.getHeight());
//    BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
//    Graphics g = combined.getGraphics();
//    g.drawImage(starting, 0, 0, null);
//    g.drawImage(overlay, offset, offset, null);
//    return combined;
//  }

//  public static BufferedImage getStenchedImage(SmellLevel level, boolean containsOtyugh, Image image) {
//    try {
//      if (level == SmellLevel.LESS_PUNGENT) {
//        image = overlay(image, "dungeonImages\\stench01.png", 0);
//      } else if (level == SmellLevel.MORE_PUNGENT) {
//        image = overlay(image, "dungeonImages\\stench02.png", 0);
//      }
//      if (containsOtyugh) {
//        image = overlay(image, "dungeonImages\\stench01.png", 0);
//        image = overlay(image, "dungeonImages\\stench02.png", 0);
//      }
//    } catch (IOException e) {
//      //Empty catch to return original image.
//    }
//    return image;
//  }

  public static void playSound(String s) {
    File audioFile = new File(s);

    try {
      AudioInputStream audioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(audioFile)));

      AudioFormat format = audioStream.getFormat();

      DataLine.Info info = new DataLine.Info(Clip.class, format);

      Clip audioClip = (Clip) AudioSystem.getLine(info);

      audioClip.open(audioStream);

      audioClip.start();

    } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
      //Ignore to not play the sound
    }
  }
}

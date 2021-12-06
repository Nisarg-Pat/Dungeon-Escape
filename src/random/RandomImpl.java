package random;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Encapsulation of Random class to get both random and non-random behaviour and to test the Model.
 */
public class RandomImpl {

  private static long seed;

  private static final Random random = new Random();

  private static final Queue<Integer> queueOfNumbers = new LinkedList<>();

  /**
   * Gives a random integer in the specified limits.
   *
   * @param lower lower limit
   * @param upper upper limit
   * @return a random integer in the specified limits
   */
  public static int getIntInRange(int lower, int upper) {
    if (queueOfNumbers.isEmpty()) {
      return random.nextInt(upper - lower + 1) + lower;
    } else {
      int number = queueOfNumbers.poll();
      if (number < lower || number > upper) {
        return random.nextInt(upper - lower + 1) + lower;
      }
      return number;
    }
  }

  /**
   * Adds non random numbers to be returned for next calls.
   *
   * @param numbers the list of numbers to be added.
   */
  public static void addNumbers(int... numbers) {
    for (int number : numbers) {
      queueOfNumbers.add(number);
    }
  }

  /**
   * Sets a seed for non random behavior.
   *
   * @param newSeed seed value
   */
  public static void setSeed(long newSeed) {
    seed = newSeed;
    random.setSeed(seed);
  }

  public static long getSeed() {
    return seed;
  }
}

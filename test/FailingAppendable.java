import java.io.IOException;

/**
 * A Mock appendable that throws IOException for call to any of its function.
 * Only purpose is for testing.
 */
public class FailingAppendable implements Appendable {
  @Override
  public Appendable append(CharSequence csq) throws IOException {
    throw new IOException("Fail");
  }

  @Override
  public Appendable append(CharSequence csq, int start, int end) throws IOException {
    throw new IOException("Fail");
  }

  @Override
  public Appendable append(char c) throws IOException {
    throw new IOException("Fail");
  }
}

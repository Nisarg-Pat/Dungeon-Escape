import dungeoncontroller.Features;
import dungeonmodel.ReadOnlyDungeonModel;
import dungeonview.DungeonView;

public class MockViewLogging implements DungeonView {

  private final StringBuilder log;

  public MockViewLogging(StringBuilder log) {
    this.log = log;
  }

  @Override
  public void setFeatures(Features features) {
    log.append("setFeatures called\n");
  }

  @Override
  public void refresh() {
    log.append("refresh called\n");
  }

  @Override
  public void makeVisible() {
    log.append("makeVisible called\n");
  }

  @Override
  public void setModel(ReadOnlyDungeonModel model) {
    log.append("setModel called\n");
  }

  @Override
  public void showErrorMessage(String error) {
    log.append("showErrorMessage called for: ").append(error).append("\n");
  }

  @Override
  public void playSound(String path) {
    log.append("playSound called for: ").append(path).append("\n");
  }

  @Override
  public void showString(String string) {
    log.append("showString called for: ").append(string).append("\n");
  }
}

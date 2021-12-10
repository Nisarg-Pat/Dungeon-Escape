package dungeonview;

import dungeoncontroller.Features;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * A popup to set up the Dungeon Model. It can be used to start a new game.
 * Visibility: package-private
 */
class DungeonPopup extends JFrame {
  //Intentionally kept DungeonSwingView to tightly couple DungeonPopup with DungeonSwingView
  // and access protected methods of DungeonSwingView.
  private final DungeonSwingView view;

  private final JButton setButton;
  private final JButton closeButton;
  private final JTextField rowInputField;
  private final JTextField columnInputField;
  private final JTextField degreeInputField;
  private final JTextField itemInputField;
  private final JTextField otyughInputField;
  private final JTextField abolethInputField;
  private final JTextField thiefInputField;
  private final JTextField pitInputField;
  private final JRadioButton isWrappedTrue;

  //Intentionally kept DungeonSwingView to tightly couple DungeonPopup with DungeonSwingView
  // and access protected methods of the view.
  protected DungeonPopup(DungeonSwingView view) {
    super();
    if (view == null) {
      throw new IllegalArgumentException("View cannot be null");
    }
    this.view = view;
    setLayout(new GridLayout(12, 0));
    try {
      BufferedImage image = ImageIO.read(new File("res\\dungeonImages\\logo.png"));
      setIconImage(image);
    } catch (IOException e) {
      // Ignore Logo
    }

    this.setDefaultCloseOperation(EXIT_ON_CLOSE);

    JPanel rowPanel = new JPanel();
    rowPanel.setLayout(new GridLayout(1, 5));
    rowPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5, true));
    JTextArea textArea = new JTextArea("Enter Number of Rows(6-100):");
    textArea.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5, true));
    textArea.setFocusable(false);
    rowPanel.add(textArea);
    int rows = view.hasModel() ? view.getModel().getRows() : 6;
    rowInputField = new JTextField(String.valueOf(rows));
    rowInputField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
    rowPanel.add(rowInputField);
    this.add(rowPanel);

    JPanel columnPanel = new JPanel();
    columnPanel.setLayout(new GridLayout(1, 2));
    columnPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5, true));
    textArea = new JTextArea("Enter Number of Columns(6-100):");
    textArea.setFocusable(false);
    textArea.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5, true));
    columnPanel.add(textArea);
    int columns = view.hasModel() ? view.getModel().getColumns() : 8;
    columnInputField = new JTextField(String.valueOf(columns));
    columnInputField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
    columnPanel.add(columnInputField);
    this.add(columnPanel);

    JPanel isWrappedPanel = new JPanel();
    isWrappedPanel.setLayout(new GridLayout(1, 3));
    isWrappedPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5, true));
    textArea = new JTextArea("Is the dungeon wrapped: ");
    textArea.setFocusable(false);
    textArea.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5, true));
    isWrappedPanel.add(textArea);
    isWrappedTrue = new JRadioButton("Yes");
    JRadioButton isWrappedFalse = new JRadioButton("No");
    boolean isWrapped = view.hasModel() && view.getModel().getWrapped();
    if (isWrapped) {
      isWrappedTrue.setSelected(true);
    } else {
      isWrappedFalse.setSelected(true);
    }
    ButtonGroup group = new ButtonGroup();
    group.add(isWrappedTrue);
    group.add(isWrappedFalse);
    isWrappedPanel.add(isWrappedTrue);
    isWrappedPanel.add(isWrappedFalse);
    this.add(isWrappedPanel);

    JPanel degreePanel = new JPanel();
    degreePanel.setLayout(new GridLayout(1, 2));
    degreePanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5, true));
    textArea = new JTextArea("Enter The Degree of Connectivity(min 0):");
    textArea.setFocusable(false);
    textArea.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5, true));
    degreePanel.add(textArea);
    int degree = view.hasModel() ? view.getModel().getDegree() : 10;
    degreeInputField = new JTextField(String.valueOf(degree));
    degreeInputField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
    degreePanel.add(degreeInputField);
    this.add(degreePanel);

    JPanel itemPanel = new JPanel();
    itemPanel.setLayout(new GridLayout(1, 2));
    itemPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5, true));
    textArea = new JTextArea("Enter Percentage of Items(0-100):");
    textArea.setFocusable(false);
    textArea.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5, true));
    itemPanel.add(textArea);
    int items = view.hasModel() ? view.getModel().getPercentageItems() : 50;
    itemInputField = new JTextField(String.valueOf(items));
    itemInputField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
    itemPanel.add(itemInputField);
    this.add(itemPanel);

    JPanel otyughPanel = new JPanel();
    otyughPanel.setLayout(new GridLayout(1, 2));
    otyughPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5, true));
    textArea = new JTextArea("Enter Number of Otyughs(min 1):");
    textArea.setFocusable(false);
    textArea.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5, true));
    otyughPanel.add(textArea);
    int numOtyughs = view.hasModel() ? view.getModel().countOtyughs() : 1;
    otyughInputField = new JTextField(String.valueOf(numOtyughs));
    otyughInputField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
    otyughPanel.add(otyughInputField);
    this.add(otyughPanel);

    JPanel abolethPanel = new JPanel();
    abolethPanel.setLayout(new GridLayout(1, 2));
    abolethPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5, true));
    textArea = new JTextArea("Enter Number of Aboleths(0-100):");
    textArea.setFocusable(false);
    textArea.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5, true));
    abolethPanel.add(textArea);
    int numAboleth = view.hasModel() ? view.getModel().countAboleth() : 1;
    abolethInputField = new JTextField(String.valueOf(numAboleth));
    abolethInputField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
    abolethPanel.add(abolethInputField);
    this.add(abolethPanel);

    JPanel thiefPanel = new JPanel();
    thiefPanel.setLayout(new GridLayout(1, 2));
    thiefPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5, true));
    textArea = new JTextArea("Enter Number of Thieves(0-100):");
    textArea.setFocusable(false);
    textArea.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5, true));
    thiefPanel.add(textArea);
    int numThief = view.hasModel() ? view.getModel().countThief() : 1;
    thiefInputField = new JTextField(String.valueOf(numThief));
    thiefInputField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
    thiefPanel.add(thiefInputField);
    this.add(thiefPanel);

    JPanel pitPanel = new JPanel();
    pitPanel.setLayout(new GridLayout(1, 2));
    pitPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5, true));
    textArea = new JTextArea("Enter Number of Pits(0-100):");
    textArea.setFocusable(false);
    textArea.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5, true));
    pitPanel.add(textArea);
    int numPit = view.hasModel() ? view.getModel().countThief() : 0;
    pitInputField = new JTextField(String.valueOf(numPit));
    pitInputField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
    pitPanel.add(pitInputField);
    this.add(pitPanel);

    this.add(new JPanel());

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(1, 5));
    buttonPanel.add(new JPanel());
    setButton = new JButton("Start");
    buttonPanel.add(setButton);
    buttonPanel.add(new JPanel());
    closeButton = new JButton("Close");
    buttonPanel.add(closeButton);
    buttonPanel.add(new JPanel());
    this.add(buttonPanel);

    pack();
  }

  protected void initPopup(int x, int y) {
    setLocation(x, y);
    setVisible(true);
  }

  protected void setFeatures(Features features) {
    if (features == null) {
      throw new IllegalArgumentException("Features cannot be null.");
    }
    closeButton.addActionListener(l -> {
      if (view.hasModel()) {
        this.setVisible(false);
      } else {
        features.exitProgram();
      }
    });
    setButton.addActionListener(l -> {
      try {
        int rows = Integer.parseInt(rowInputField.getText());
        int columns = Integer.parseInt(columnInputField.getText());
        boolean isWrapped = isWrappedTrue.isSelected();
        int degreeOfInterconnectivity = Integer.parseInt(degreeInputField.getText());
        int percentageItems = Integer.parseInt(itemInputField.getText());
        int numOtyughs = Integer.parseInt(otyughInputField.getText());
        int numAboleth = Integer.parseInt(abolethInputField.getText());
        int numThief = Integer.parseInt(thiefInputField.getText());
        int numPits = Integer.parseInt(pitInputField.getText());
        if (rows > 100 || columns > 100 || numAboleth > 100 || numThief > 100 || numOtyughs > 100) {
          JOptionPane.showMessageDialog(this, "Enter valid details!");
        } else {
          features.createNewModel(rows, columns, isWrapped,
                  degreeOfInterconnectivity, percentageItems, numOtyughs,
                  numAboleth, numThief, numPits);
          this.setVisible(false);
          view.setSizes(rows, columns);
          view.setVisible(true);
        }
      } catch (IllegalArgumentException e) {
        JOptionPane.showMessageDialog(this, "Enter valid details!");
      }
    });
  }
}

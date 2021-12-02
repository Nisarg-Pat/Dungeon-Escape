package dungeonview;


import java.awt.*;

import javax.swing.*;

import dungeoncontroller.Features;

class DungeonPopup extends JDialog {
  DungeonSpringView view;

  JButton resetButton;
  JButton closeButton;
  JTextField rowInputField, columnInputField, degreeInputField, itemInputField, otyughInputField;
  JRadioButton isWrappedTrue, isWrappedFalse;

  static final String ACTION_CLOSE_BUTTON = "Close Button";
  static final String ACTION_RESET_BUTTON = "Reset Button";

  DungeonPopup(DungeonSpringView view) {
    super();
    this.view = view;
    setLayout(new GridLayout(10, 0));

    JPanel rowPanel = new JPanel();
    rowPanel.setLayout(new GridLayout(1, 2));
    rowPanel.add(new JTextArea("Enter Number of Rows:"));
    rowInputField = new JTextField(String.valueOf(view.model.getRows()));
    rowPanel.add(rowInputField);
    this.add(rowPanel);

    JPanel columnPanel = new JPanel();
    columnPanel.setLayout(new GridLayout(1, 2));
    columnPanel.add(new JTextArea("Enter Number of Columns:"));
    columnInputField = new JTextField(String.valueOf(view.model.getColumns()));
    columnPanel.add(columnInputField);
    this.add(columnPanel);

    JPanel isWrappedPanel = new JPanel();
    isWrappedPanel.setLayout(new GridLayout(1, 3));
    isWrappedPanel.add(new JTextArea("Is the dungeon wrapped: "));
    isWrappedTrue = new JRadioButton("Yes");
    isWrappedFalse = new JRadioButton("No");
    if (view.model.getWrapped()) {
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
    degreePanel.add(new JTextArea("Enter The Degree of Connectivity:"));
    degreeInputField = new JTextField(String.valueOf(view.model.getDegree()));
    degreePanel.add(degreeInputField);
    this.add(degreePanel);

    JPanel itemPanel = new JPanel();
    itemPanel.setLayout(new GridLayout(1, 2));
    itemPanel.add(new JTextArea("Enter Percentage of Items:"));
    itemInputField = new JTextField(String.valueOf(view.model.getPercentageItems()));
    itemPanel.add(itemInputField);
    this.add(itemPanel);

    JPanel otyughPanel = new JPanel();
    otyughPanel.setLayout(new GridLayout(1, 2));
    otyughPanel.add(new JTextArea("Enter Number of Columns:"));
    otyughInputField = new JTextField(String.valueOf(view.model.countOtyughs()));
    otyughPanel.add(otyughInputField);
    this.add(otyughPanel);

    resetButton = new JButton("Reset");
    this.add(resetButton);

    closeButton = new JButton("Close");
    this.add(closeButton);

    pack();
  }

  protected void setFeatures(Features features) {
    closeButton.addActionListener(l -> this.setVisible(false));
    resetButton.addActionListener(l ->
    {
      int rows = Integer.parseInt(rowInputField.getText());
      int columns = Integer.parseInt(columnInputField.getText());
      boolean isWrapped = isWrappedTrue.isSelected();
      int degreeOfInterconnectivity = Integer.parseInt(degreeInputField.getText());
      int percentageItems = Integer.parseInt(itemInputField.getText());
      int numOtyughs = Integer.parseInt(otyughInputField.getText());
      features.resetModel(rows, columns, isWrapped,
              degreeOfInterconnectivity, percentageItems, numOtyughs);
      this.setVisible(false);
      view.setSizes();
      view.setVisible(true);
    });
  }

  protected void initPopup(int x, int y) {
    setLocation(x, y);
    setVisible(true);
  }
}

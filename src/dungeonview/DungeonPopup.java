package dungeonview;


import java.awt.*;

import javax.swing.*;

import dungeoncontroller.Features;

class DungeonPopup extends JFrame {
  DungeonSpringView view;

  JButton setButton;
  JButton closeButton;
  JTextField rowInputField, columnInputField, degreeInputField, itemInputField, otyughInputField;
  JRadioButton isWrappedTrue, isWrappedFalse;

  DungeonPopup(DungeonSpringView view) {
    super();
    this.view = view;
    setLayout(new GridLayout(10, 0));
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);

    JPanel rowPanel = new JPanel();
    rowPanel.setLayout(new GridLayout(1, 2));
    rowPanel.add(new JTextArea("Enter Number of Rows:"));
    int rows = view.hasModel() ? view.getModel().getRows() : 6;
    rowInputField = new JTextField(String.valueOf(rows));
    rowPanel.add(rowInputField);
    this.add(rowPanel);

    JPanel columnPanel = new JPanel();
    columnPanel.setLayout(new GridLayout(1, 2));
    columnPanel.add(new JTextArea("Enter Number of Columns:"));
    int columns = view.hasModel() ? view.getModel().getColumns() : 8;
    columnInputField = new JTextField(String.valueOf(columns));
    columnPanel.add(columnInputField);
    this.add(columnPanel);

    JPanel isWrappedPanel = new JPanel();
    isWrappedPanel.setLayout(new GridLayout(1, 3));
    isWrappedPanel.add(new JTextArea("Is the dungeon wrapped: "));
    isWrappedTrue = new JRadioButton("Yes");
    isWrappedFalse = new JRadioButton("No");
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
    degreePanel.add(new JTextArea("Enter The Degree of Connectivity:"));
    int degree = view.hasModel() ? view.getModel().getDegree() : 10;
    degreeInputField = new JTextField(String.valueOf(degree));
    degreePanel.add(degreeInputField);
    this.add(degreePanel);

    JPanel itemPanel = new JPanel();
    itemPanel.setLayout(new GridLayout(1, 2));
    itemPanel.add(new JTextArea("Enter Percentage of Items:"));
    int items = view.hasModel() ? view.getModel().getPercentageItems() : 50;
    itemInputField = new JTextField(String.valueOf(items));
    itemPanel.add(itemInputField);
    this.add(itemPanel);

    JPanel otyughPanel = new JPanel();
    otyughPanel.setLayout(new GridLayout(1, 2));
    otyughPanel.add(new JTextArea("Enter Number of Otyughs:"));
    int numOtyughs = view.hasModel() ? view.getModel().countOtyughs() : 1;
    otyughInputField = new JTextField(String.valueOf(numOtyughs));
    otyughPanel.add(otyughInputField);
    this.add(otyughPanel);

    setButton = new JButton("Start");
    this.add(setButton);

    closeButton = new JButton("Close");
    this.add(closeButton);

    pack();
  }

  protected void initPopup(int x, int y) {
    setLocation(x, y);
    setVisible(true);
  }

  protected void setFeatures(Features features) {
    closeButton.addActionListener(l -> {
      if (view.hasModel()) {
        this.setVisible(false);
      } else {
        features.exitProgram();
      }
    });
    setButton.addActionListener(l -> {
      int rows = Integer.parseInt(rowInputField.getText());
      int columns = Integer.parseInt(columnInputField.getText());
      boolean isWrapped = isWrappedTrue.isSelected();
      int degreeOfInterconnectivity = Integer.parseInt(degreeInputField.getText());
      int percentageItems = Integer.parseInt(itemInputField.getText());
      int numOtyughs = Integer.parseInt(otyughInputField.getText());
      features.createModel(rows, columns, isWrapped,
              degreeOfInterconnectivity, percentageItems, numOtyughs);
      this.setVisible(false);
      view.setSizes(rows, columns);
      view.setVisible(true);
    });
  }
}

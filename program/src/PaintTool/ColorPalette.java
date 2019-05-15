/**
 * JAVA DRAWING APP
 * Tran Quang Huy & Nguyen Van Manh
 */

package PaintTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ColorPalette extends JPanel {

    protected ColorButton colorButtons[];     //array of ColorButton objects
    public static JPanel selectedColorDisplay;    //JPanel displaying the currently isSelected color
    protected Color colors[];                 //array of colors
    public static Color selectedColor;            //isSelected selectedColor

    public ColorPalette() {
        setBackground(Color.DARK_GRAY);                //set the background of the palette
        setLayout(new BorderLayout());                //set the layout of the palette
        colors = new Color[36];                       //create a Color array of 16 length for holding 92 colors
        for(int i = 0; i < 36; i++)
        {                                             //fill the array with 16 different colors
            colors[i] = Color.getHSBColor((float) i / (float) 36, 0.85f, 1.0f);
        }

        selectedColor = Color.black;                                    //set initial selectedColor to black
        selectedColorDisplay = new JPanel();                            //create a new JPanel for holding the selectedColor
        selectedColorDisplay.setPreferredSize(new Dimension(200, 200));   //set the size of the panel

        selectedColorDisplay.addMouseListener(new MouseAdapter() {      //add a mouse listener

            public void mousePressed(MouseEvent event) {                //when the user presses the mouse button
                {                                                       //display the JColorChooser window
                    selectedColorDisplay.setBackground(JColorChooser.showDialog(Paint.squarePad, "Change Color", Paint.squarePad.brushColor));
                    selectedColor = selectedColorDisplay.getBackground();                                   //change the isSelected color
                    Paint.squarePad.currentToolDetails.setColor(selectedColorDisplay.getBackground());     //change the ToolDetails color
                    Paint.squarePad.setCurrentColor(selectedColor);                                   //change the DrawingPanel brushColor
                }
            }
        });

        JPanel colorButtonsGrid = new JPanel();                     //create the ColorButton grid
        colorButtonsGrid.setBackground(Color.DARK_GRAY);
        colorButtonsGrid.setLayout(new GridLayout(6, 6, 4, 4));
        colorButtons = new ColorButton[colors.length];              //add the created colors to the ColorButton grid
        for (int i = 0; i < colorButtons.length; i++) {
            colorButtons[i] = new ColorButton(colors[i]);
            colorButtonsGrid.add(colorButtons[i]);
        }

        ColorPanel colorButtonsPanel = new ColorPanel(Color.DARK_GRAY);      //create the ColorPanel for holding the ColorButtons
        colorButtonsPanel.setLayout(new BorderLayout(4, 4));
        colorButtonsPanel.add(selectedColorDisplay, "South");
        colorButtonsPanel.add(colorButtonsGrid, "Center");
        JPanel colorButtonRows = new JPanel();                              //create the ColorButton panels
        colorButtonRows.setLayout(new BorderLayout());
        colorButtonRows.add(new ColorPanel(Color.DARK_GRAY), "West");
        colorButtonRows.add(new ColorPanel(Color.DARK_GRAY), "East");
        colorButtonRows.add(new ColorPanel(Color.DARK_GRAY), "South");
        colorButtonRows.add(new ColorPanel(Color.DARK_GRAY), "North");
        colorButtonRows.add(colorButtonsPanel, "Center");
        add(colorButtonRows, "Center");
    }

    public void deselectAll()
    {                                                            //deselect all color boxes
        for (ColorButton colorButton : colorButtons) colorButton.isSelected = false;
    }

    public void paintComponent(Graphics g)                      //overrides method in JComponent
    {
        super.paintComponent(g);
    }
}

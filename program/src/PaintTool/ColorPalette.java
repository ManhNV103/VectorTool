/**
 * JAVA DRAWING APP
 * @author Tran Quang Huy & Nguyen Van Manh
 */


package PaintTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * ColorPalette
 * A class that contains two panels having backgrounds showing pen and fill colors
 * Both panels are also implemented mouseListener to select multiple colors
 */
public class ColorPalette extends JPanel {

    // Initialize variables

    public static JPanel selectedColorDisplay;    // JPanel contains both pen and fill color's panels
    public static JPanel fillColorDisplay;       // JPanel for holding selectedFillColor
    public static JPanel penColorDisplay;        // JPanel for holding selectedPenColor
    public static Color selectedPenColor;        // Selected pen color
    public static Color selectedFillColor;       // Selected fill color

    /**
     * Default constructor
     */
    public ColorPalette() {
        setBackground(Color.DARK_GRAY);     //set the background of the palette
        setLayout(new BorderLayout());      //set the layout of the palette

        selectedFillColor = Color.GRAY;     //  initialize selected fill color
        fillColorDisplay = new JPanel();    //create a new JPanel for holding the selectedFillColor
        fillColorDisplay.setPreferredSize(new Dimension(100, 150));   //set the size of the panel
        fillColorDisplay.add(new JLabel("Fill Color"),BorderLayout.CENTER);
        fillColorDisplay.addMouseListener(new MouseAdapter() {      //add a mouse listener

            public void mousePressed(MouseEvent event) {                //when the user presses the mouse button
                {                                                       //display the JColorChooser window
                    fillColorDisplay.setBackground(JColorChooser.showDialog(Paint.squarePad, "Change Color", Paint.squarePad.fillColor));
                    selectedFillColor = fillColorDisplay.getBackground();   //change the selected fill color
                    Paint.squarePad.setCurrentFillColor(selectedFillColor); // set the fill color in squarePadDrawing
                }
            }
        });


        selectedPenColor = Color.GRAY;      // initialize selected pen color
        penColorDisplay = new JPanel();     // create a new JPanel for holding selectedPenColor
        penColorDisplay.setBackground(Color.WHITE); // set the background of panel
        penColorDisplay.setPreferredSize(new Dimension(100, 150));  // set the size of panel
        penColorDisplay.add(new JLabel("Pen Color"),BorderLayout.CENTER);
        penColorDisplay.addMouseListener(new MouseAdapter(){      // add a mouse listener
            public void mousePressed(MouseEvent event)            //when the user presses the mouse button
            {                                                     //display the JColorChooser window
                penColorDisplay.setBackground(JColorChooser.showDialog(Paint.squarePad, "Change Color", Paint.squarePad.penColor));
                selectedPenColor = penColorDisplay.getBackground();                                //change the selected pen color
                Paint.squarePad.currentToolDetails.setColor(penColorDisplay.getBackground());     //set the pen color in squarePadDrawing
                Paint.squarePad.setCurrentPenColor(selectedPenColor);
            }
        });

        // creates a panel for containing pen and fill color's panels
        selectedColorDisplay = new JPanel();
        selectedColorDisplay.setLayout(new BoxLayout(selectedColorDisplay,BoxLayout.X_AXIS));
        selectedColorDisplay.add(penColorDisplay);
        selectedColorDisplay.add(fillColorDisplay);


        ColorPanel colorButtonsPanel = new ColorPanel(Color.DARK_GRAY);      //create the ColorPanel for holding the ColorButtons
        colorButtonsPanel.setLayout(new BorderLayout(4, 4));
        colorButtonsPanel.add(selectedColorDisplay, "North");
        JPanel colorButtonRows = new JPanel();                              //create the ColorButton panels
        colorButtonRows.setLayout(new BorderLayout());
        colorButtonRows.add(new ColorPanel(Color.DARK_GRAY), "West");
        colorButtonRows.add(new ColorPanel(Color.DARK_GRAY), "East");
        colorButtonRows.add(new ColorPanel(Color.DARK_GRAY), "South");
        colorButtonRows.add(new ColorPanel(Color.DARK_GRAY), "North");
        colorButtonRows.add(colorButtonsPanel, "Center");
        add(colorButtonRows, "Center");
    }


    public void paintComponent(Graphics g)                      //overrides method in JComponent
    {
        super.paintComponent(g);
    }
}

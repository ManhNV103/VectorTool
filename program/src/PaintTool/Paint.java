/**
 * JAVA DRAWING APP
 * Tran Quang Huy & Nguyen Van Manh
 */


package PaintTool;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Paint extends JFrame implements ActionListener, Runnable {

    public static final int HEIGHT = 1200;
    public static final int WIDTH = 1200;

    private JPanel padDraw;

    private Menu menuBar;
    public static ColorPalette colorPalette;
    public static PaintToolPanel paintToolPanel;
    public static DrawingHistory historyPanel;
    public static SquarePadDrawing squarePad;

    /**
     * Constructor
     * @param name: name of the window
     * @throws HeadlessException
     */
    public Paint(String name) throws HeadlessException {
        super(name);
    }

    /**
     * Create the displayed GUI.
     */
    private void createGUI(){
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //Set the menu bar and add the label to the content pane.
        menuBar = new Menu();
        setJMenuBar(menuBar);

        //Create painting tool panel
        paintToolPanel = new PaintToolPanel();

        //Create the color palette
        colorPalette = new ColorPalette();

        paintToolPanel.add(colorPalette,BorderLayout.SOUTH);

        getContentPane().add(paintToolPanel,BorderLayout.WEST);

        historyPanel = new DrawingHistory();
        getContentPane().add(new JScrollPane(historyPanel),BorderLayout.EAST);

        // Create draw PadDraw
        squarePad = new SquarePadDrawing();
        squarePad.setBackground(Color.WHITE);
        padDraw = new JPanel();
        padDraw.setBackground(Color.LIGHT_GRAY);
        padDraw.setLayout(new GridBagLayout());
        padDraw.add(squarePad);

        getContentPane().add(padDraw, BorderLayout.CENTER);



        repaint();
        setLocationRelativeTo(null);
        setVisible(true);
        setStartingColor();

    }

    //set starting color to be used for drawing
    public void setStartingColor()     //set starting color to be used for drawing
    {
        ColorPalette.penColorDisplay.setBackground(Color.GRAY);
        ColorPalette.selectedPenColor = ColorPalette.penColorDisplay.getBackground();
        squarePad.currentToolDetails.setColor(ColorPalette.penColorDisplay.getBackground());
        squarePad.penColor = ColorPalette.selectedPenColor;

        ColorPalette.fillColorDisplay.setBackground(Color.WHITE);
        ColorPalette.selectedFillColor = ColorPalette.fillColorDisplay.getBackground();
        squarePad.fillColor = ColorPalette.selectedFillColor;

    }



    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void run() {
        createGUI();
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Paint("Vector Design Tool"));
    }
}

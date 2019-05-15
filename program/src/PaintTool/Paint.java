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

    public static final int HEIGHT = 1024;
    public static final int WIDTH = 1200;

    private JPanel padDraw;
    public static SquarePadDrawing squarePad;
    private Menu menuBar;
    public static ColorPalette colorPalette;
    public static PaintToolPanel paintToolPanel;

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
    private void CreateGUI(){
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create draw PadDraw
        squarePad = new SquarePadDrawing();
        squarePad.setBackground(Color.WHITE);
        padDraw = new JPanel();
        padDraw.setBackground(Color.LIGHT_GRAY);
        padDraw.setLayout(new GridBagLayout());
        padDraw.add(squarePad);

        getContentPane().add(new JScrollPane(padDraw), BorderLayout.CENTER);

        //Set the menu bar and add the label to the content pane.
        menuBar = new Menu();
        setJMenuBar(menuBar);

        //Create painting tool panel
        paintToolPanel = new PaintToolPanel();

        //Create the color palette
        colorPalette = new ColorPalette();

        paintToolPanel.add(colorPalette,BorderLayout.CENTER);

        getContentPane().add(paintToolPanel,BorderLayout.WEST);


        repaint();
        setLocationRelativeTo(null);
        setVisible(true);
        setStartingColor();

    }

    //set starting color to be used for drawing
    public void setStartingColor()     //set starting color to be used for drawing
    {
        ColorPalette.selectedColorDisplay.setBackground(Color.black);
        ColorPalette.selectedColor = ColorPalette.selectedColorDisplay.getBackground();
        squarePad.currentToolDetails.setColor(ColorPalette.selectedColorDisplay.getBackground());
        squarePad.brushColor = ColorPalette.selectedColor;
    }



    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void run() {
        CreateGUI();
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Paint("Vector Design Tool"));
    }
}

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

    private PadDraw padDraw;
    private Menu menuBar;
    public PaintToolPanel paintToolPanel;

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
        padDraw = new PadDraw();
        getContentPane().add(padDraw, BorderLayout.CENTER);

        //Set the menu bar and add the label to the content pane.
        menuBar = new Menu();
        setJMenuBar(menuBar);

        //Create painting tool panel
        paintToolPanel = new PaintToolPanel();

        getContentPane().add(paintToolPanel,BorderLayout.WEST);


        repaint();
        setVisible(true);

    }

    private JPanel createPanel(Color c){
        JPanel panel = new JPanel();
        panel.setBackground(c);
        return panel;
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

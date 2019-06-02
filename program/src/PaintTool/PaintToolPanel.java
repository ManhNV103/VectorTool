/**
 * JAVA DRAWING APP
 * @author Tran Quang Huy & Nguyen Van Manh
 */

package PaintTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * PaintToolPanel
 * A class contains instances of ToolButton classes and a reset button for clearing selected colors
 */
public class PaintToolPanel extends JPanel {


    //Initialize variables

    protected ToolButton toolButtons[];
    private JButton resetButton;

    private Icon clear = new ImageIcon(getClass().getResource("images/eraser.png"));
    private Icon rectangle = new ImageIcon(getClass().getResource("images/rectangle.png"));
    private Icon plot = new ImageIcon(getClass().getResource("images/dot1.jpg"));
    private Icon line = new ImageIcon(getClass().getResource("images/line-tool.png"));
    private Icon ellipse = new ImageIcon(getClass().getResource("images/oval.png"));
    private Icon polygon = new ImageIcon(getClass().getResource("images/polygon.png"));
    private JPanel toolPanel = new JPanel();

    /**
     * Default constructor
     */
    public PaintToolPanel(){
        setBackground(Color.DARK_GRAY);
        setPreferredSize(new Dimension(200, 0));
        setLayout(new BorderLayout(8, 8));

        // Grid Layout for toolPanel
        toolPanel.setLayout(new GridLayout(3, 2));
        toolPanel.setBackground(Color.DARK_GRAY);
        toolPanel.setPreferredSize(new Dimension(200, 300));
        toolButtons = new ToolButton[6];                        //create new array of buttons

        // initialize resetButton
        resetButton = new JButton("FILL OFF");
        resetButton.setHorizontalAlignment(SwingConstants.CENTER);
        resetButton.addActionListener(new ClickResetListener());

        // create panel for resetButton
        JPanel radioButtonPanel = new JPanel();
        BoxLayout radioButtonPanelLayout = new BoxLayout(radioButtonPanel,BoxLayout.X_AXIS);
        radioButtonPanel.setLayout(radioButtonPanelLayout);
        radioButtonPanel.add(resetButton);

        // add all ToolButton in array toolButton
        toolButtons[0] = new ToolButton(clear, ToolFactory.createTool(ToolFactory.CLEAR_TOOL));
        toolButtons[1] = new ToolButton(plot, ToolFactory.createTool(ToolFactory.PLOT_TOOL));
        toolButtons[2] = new ToolButton(line, ToolFactory.createTool(ToolFactory.LINE_TOOL));
        toolButtons[3] = new ToolButton(rectangle,ToolFactory.createTool(ToolFactory.RECTANGLE_TOOL));
        toolButtons[4] = new ToolButton(ellipse,ToolFactory.createTool(ToolFactory.ELLIPSE_TOOL));
        toolButtons[5] = new ToolButton(polygon,ToolFactory.createTool(ToolFactory.POLYGON_TOOL));


        for (ToolButton toolButton : toolButtons) toolPanel.add(toolButton);    //add buttons to tool panel

        // Add all panels in toolPanel
        this.add(toolPanel, BorderLayout.NORTH);
        this.add(radioButtonPanel,BorderLayout.CENTER);

    }

    /**
     * Class implements ActionListener for resetButton
     */
    private class ClickResetListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e){
            if(e.getSource() == resetButton){
                Paint.squarePad.setCurrentFillColor(new Color(1f,0f,0f,0 ));
                Paint.squarePad.fill =false;
                Paint.squarePad.outLines.push("FILL OFF\n");
            }
        }
    }

}
/**
 * JAVA DRAWING APP
 * Tran Quang Huy & Nguyen Van Manh
 */

package PaintTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaintToolPanel extends JPanel {


    //Initialize variables

    protected ToolButton toolButtons[];

    private JComboBox fillerType;

    private Icon pencil = new ImageIcon(getClass().getResource("images/IMG_OLOVKA_48.png"));
    private Icon clear = new ImageIcon(getClass().getResource("images/eraser.png"));
    private Icon undo = new ImageIcon(getClass().getResource("images/undo.png"));
    private Icon rectangle = new ImageIcon(getClass().getResource("images/rectangle.png"));
    private Icon plot = new ImageIcon(getClass().getResource("images/dot1.jpg"));
    private Icon line = new ImageIcon(getClass().getResource("images/line-tool.png"));
    private Icon ellipse = new ImageIcon(getClass().getResource("images/oval.png"));
    private Icon polygon = new ImageIcon(getClass().getResource("images/polygon.png"));
    private Icon filledRect = new ImageIcon(getClass().getResource("images/filled-rectangle.png"));
    private Icon filledEllipse = new ImageIcon(getClass().getResource("images/filled-oval.png"));
    private Icon filledPoly = new ImageIcon(getClass().getResource("images/filled-polygon.png"));
    private JPanel toolPanel = new JPanel();

    public PaintToolPanel(){
        setBackground(Color.DARK_GRAY);                          //customize the panel
        setPreferredSize(new Dimension(200, 0));
        setLayout(new BorderLayout(8, 8));

        String[] fillerTypes = {"PENCIL", "FILLED"};             //create String array for the combo box
        fillerType = new JComboBox(fillerTypes);                //create the combo box
        ComboBoxHandler handler = new ComboBoxHandler();        //creating a new instance of the ComboBoxHandler inner class
        fillerType.addActionListener(handler);                  //add combo box listener
        fillerType.setFont(new Font("Cambria", Font.BOLD, 16)); //set font

        toolPanel.setLayout(new GridLayout(4, 2));              //customize the tool panel
        toolPanel.setBackground(Color.DARK_GRAY);
        toolPanel.setPreferredSize(new Dimension(200, 300));
        toolButtons = new ToolButton[8];                        //create new array of buttons

        addToolButtons();
        addEmptyToolButtons();

        for (ToolButton toolButton : toolButtons) toolPanel.add(toolButton);    //add buttons to tool panel

        this.add(toolPanel, BorderLayout.NORTH);
        this.add(fillerType, BorderLayout.CENTER);
    }

    private void addToolButtons(){
        toolButtons[0] = new ToolButton(pencil,ToolFactory.createTool(ToolFactory.PENCIL_TOOL));
        toolButtons[1] = new ToolButton(clear,ToolFactory.createTool(ToolFactory.CLEAR_TOOL));
        toolButtons[2] = new ToolButton(undo,ToolFactory.createTool(ToolFactory.UNDO_TOOL));
        toolButtons[3] = new ToolButton(plot,ToolFactory.createTool(ToolFactory.PLOT_TOOL));
        toolButtons[4] = new ToolButton(rectangle,ToolFactory.createTool(ToolFactory.RECTANGLE_TOOL));
        toolButtons[5] = new ToolButton(line,ToolFactory.createTool(ToolFactory.LINE_TOOL));	    }

    private void addEmptyToolButtons(){
        toolButtons[5] = new ToolButton(rectangle,ToolFactory.createTool(ToolFactory.RECTANGLE_TOOL));
        toolButtons[6] = new ToolButton(ellipse,ToolFactory.createTool(ToolFactory.ELLIPSE_TOOL));
        toolButtons[7] = new ToolButton(polygon,ToolFactory.createTool(ToolFactory.POLYGON_TOOL));
    }


    private void addFilledToolButtons(){
        toolButtons[5] = new ToolButton(filledRect,ToolFactory.createTool(ToolFactory.FILLED_RECTANGLE_TOOL));
        toolButtons[6] = new ToolButton(filledEllipse,ToolFactory.createTool(ToolFactory.FILLED_ELLIPSE_TOOL));
        toolButtons[7] = new ToolButton(filledPoly,ToolFactory.createTool(ToolFactory.FILLED_POLYGON_TOOL));
    }

private class  ComboBoxHandler implements ActionListener  //combo box (filling type) even handling
{
    @Override
    public void actionPerformed(ActionEvent e)
    {
        fillerType = (JComboBox)e.getSource();
        int selectedValue = fillerType.getSelectedIndex();
        if (selectedValue ==0)          //if empty
        {
            for (ToolButton toolButton1 : toolButtons) toolPanel.remove(toolButton1);  //remove buttons
            revalidate();
            repaint();
            addEmptyToolButtons();                     //add basic buttons
            addFilledToolButtons();                 //add empty shape buttons
            for (ToolButton toolButton : toolButtons) toolPanel.add(toolButton);
        }
        else if (selectedValue == 1 )  //if filled
        {
            for (ToolButton toolButton1 : toolButtons) toolPanel.remove(toolButton1); //remove all buttons
            revalidate();
            repaint();
            addEmptyToolButtons();                     //add basic buttons
            addFilledToolButtons();                 //add empty shape buttons
            for (ToolButton toolButton : toolButtons) toolPanel.add(toolButton);
        }
    }
}


}

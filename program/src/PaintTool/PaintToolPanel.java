/**
 * JAVA DRAWING APP
 * Tran Quang Huy & Nguyen Van Manh
 */

package PaintTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PaintToolPanel extends JPanel {

    //Initialize variables

    protected ToolButton toolButtons[];

    private Icon pencil = new ImageIcon(getClass().getResource("images/IMG_OLOVKA_48.png"));
    private Icon fill = new ImageIcon(getClass().getResource("images/paint-brush.png"));
    private Icon undo = new ImageIcon(getClass().getResource("images/undo.png"));
    private Icon rectangle = new ImageIcon(getClass().getResource("images/rectangle.png"));
    private Icon plot = new ImageIcon(getClass().getResource("images/dot1.jpg"));
    private Icon line = new ImageIcon(getClass().getResource("images/line-tool.png"));
    private Icon ellipse = new ImageIcon(getClass().getResource("images/oval.png"));
    private Icon polygon = new ImageIcon(getClass().getResource("images/polygon.png"));

    private JPanel toolPanel = new JPanel();

    public PaintToolPanel(){
        setBackground(Color.DARK_GRAY);                          //customize the panel
        setPreferredSize(new Dimension(200, 0));
        setLayout(new BorderLayout(8, 8));

        toolPanel.setLayout(new GridLayout(4, 2));              //customize the tool panel
        toolPanel.setBackground(Color.DARK_GRAY);
        toolPanel.setPreferredSize(new Dimension(200, 300));
        toolButtons = new ToolButton[8];                        //create new array of buttons

        addToolButtons();

        for (ToolButton toolButton : toolButtons) toolPanel.add(toolButton);    //add buttons to tool panel

        this.add(toolPanel, BorderLayout.NORTH);
    }

    private void addToolButtons(){
        toolButtons[0] = new ToolButton(pencil,ToolFactory.createTool(ToolFactory.PENCIL_TOOL));
        toolButtons[1] = new ToolButton(fill,ToolFactory.createTool(ToolFactory.FILL_TOOL));
        toolButtons[2] = new ToolButton(undo,ToolFactory.createTool(ToolFactory.UNDO_TOOL));
        toolButtons[3] = new ToolButton(plot,ToolFactory.createTool(ToolFactory.PLOT_TOOL));
        toolButtons[4] = new ToolButton(rectangle,ToolFactory.createTool(ToolFactory.RECTANGLE_TOOL));
        toolButtons[5] = new ToolButton(line,ToolFactory.createTool(ToolFactory.LINE_TOOL));
        toolButtons[6] = new ToolButton(ellipse,ToolFactory.createTool(ToolFactory.ELLIPSE_TOOL));
        toolButtons[7] = new ToolButton(polygon,ToolFactory.createTool(ToolFactory.POLYGON_TOOL));
    }

}

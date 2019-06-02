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
 * ToolButton
 * A class that extends JButton and implements ActionListener for each Tool button 
 */
public class ToolButton extends JButton implements ActionListener {

    public JLabel label;
    public Tool tool;

    /**
     * Constructor with parameters Icon image for each Tool's button and Tool instance
     * @param icon
     * @param tool
     */

    public ToolButton(Icon icon, Tool tool)
    {
        label = new JLabel(icon);
        setLayout(new BorderLayout());
        add(label);
        this.tool = tool;
        addActionListener(this);
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
    }


    /**
     * Action method
     * @param event
     */

    public void actionPerformed(ActionEvent event)
    {
        Paint.squarePad.currentTool = tool;
        repaint();

    }


}

/**
 * JAVA DRAWING APP
 * Tran Quang Huy & Nguyen Van Manh
 */

package PaintTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolButton extends JButton implements ActionListener {

    public JLabel label;
    public Tool tool;

    /**
     * Constructors
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

    }


}

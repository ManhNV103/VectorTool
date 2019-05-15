/**
 * JAVA DRAWING APP
 * Tran Quang Huy & Nguyen Van Manh
 */

package PaintTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ColorButton extends JPanel {
    Color color;
    boolean isSelected;

    /**
     * Constructor
     * @param clr
     */
    public ColorButton(Color clr)
    {
        color = clr;                                //set selectedColor of the box
        isSelected = false;                         //set isSelected to false
        setBackground(color);                       //set background to the given selectedColor
        addMouseListener(new MouseHandler());       //add mouse event
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);               //call base method
        g.setColor(Color.LIGHT_GRAY);           //set selectedColor of
        g.drawRect(0, 0, getWidth(), getHeight());   //draw rectangle representing the selectedColor box
        if(isSelected)
        {                                      //change the appearance if the selectedColor box is isSelected
            g.setColor(Color.WHITE);
            g.drawRect(1, 1, getWidth(), getHeight());
            g.drawRect(-1, -1, getWidth(), getHeight());
        }
    }

    /**
     * Mouse Events
     */
    private class MouseHandler extends MouseAdapter
    {
        public void mousePressed(MouseEvent e)          //when the mouse button is pressed
        {
            Paint.colorPalette.deselectAll();  //deselect all colors
            isSelected = true; //set the current selectedColor to isSelected
            Paint.squarePad.setCurrentColor(color); //set the brush selectedColor of the draw panel to the isSelected selectedColor
            ColorPalette.selectedColorDisplay.setBackground(color); //set the selectedColor of the selectedColorDisplay
            repaint();                            //repaint the main application window

        }
        public void mouseReleased(MouseEvent e){ }

        public void mouseClicked(MouseEvent e) {}

        public void mouseEntered(MouseEvent e){ }

    }
}

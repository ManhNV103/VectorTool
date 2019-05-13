/**
 * JAVA DRAWING APP
 * Tran Quang Huy & Nguyen Van Manh
 */


package PaintTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;


// ALL the code below need to be fixed.


public class PadDraw extends JPanel{
    private SquarePanel pad;

    public PadDraw(){
        super();
        setBackground(Color.LIGHT_GRAY);
        setLayout(new GridBagLayout());
        pad = new SquarePanel();
        pad.setBackground(Color.WHITE);
        add(pad);
    }
}




class SquarePanel extends JPanel {
    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        Container c = getParent();
        if (c != null) {
            d = c.getSize();
        } else {
            return new Dimension(10, 10);
        }
        int w = (int) d.getWidth();
        int h = (int) d.getHeight();
        int s = (w < h ? w : h);
        return new Dimension(s, s);
    }
}
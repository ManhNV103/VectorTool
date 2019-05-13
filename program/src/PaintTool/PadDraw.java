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
import java.awt.image.BufferedImage;


// ALL the code below need to be fixed.


public class PadDraw extends JPanel{
    private SquarePad pad;
    private Image image;

    public PadDraw(){
        super();
        setBackground(Color.LIGHT_GRAY);
        setLayout(new GridBagLayout());
        pad = new SquarePad();
        pad.setBackground(Color.WHITE);
        add(pad);
    }

}

/**
 * Implementation of draw function should be implemented in this class (SquarePad).
 *
 */


class SquarePad extends JPanel {
    // an imgae to draw on
    Image image;

    // Graphics
    Graphics2D graphics2D;

    //Mouse coordinates
    int currentX, currentY, oldX, oldY;


    //Now for the constructors
    public SquarePad(){
        // THIS IS JUST TO TEST AND SEE IF WE CAN DRAW ON THE PANEL
        setDoubleBuffered(false);
        addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                oldX = e.getX();
                oldY = e.getY();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter(){
            public void mouseDragged(MouseEvent e){
                currentX = e.getX();
                currentY = e.getY();
                if(graphics2D != null)
                    graphics2D.drawLine(oldX, oldY, currentX, currentY);
                repaint();
                oldX = currentX;
                oldY = currentY;
            }

        });
    }

    public void paintComponent(Graphics g){
        if(image == null){
            image = createImage(getSize().width, getSize().height);
            graphics2D = (Graphics2D)image.getGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            clear();

        }
        g.drawImage(image, 0, 0, null);
    }



    public void clear(){
        graphics2D.setPaint(Color.white);
        graphics2D.fillRect(0, 0, getSize().width, getSize().height);
        graphics2D.setPaint(Color.black);
        repaint();
    }

    public void drawRectangle(){
        //check();
        graphics2D.drawRect(5, 5, 20, 40);
        repaint();
    }


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
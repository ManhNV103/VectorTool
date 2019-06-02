/**
 * JAVA DRAWING APP
 * @author Tran Quang Huy & Nguyen Van Manh
 */

package PaintTool;

import javax.swing.*;
import java.awt.*;

/**
 * ColorPanel
 * A class that extends JPanel and allows to select the color to set background
 */

public class ColorPanel extends JPanel {

    /**
     * Constructor with parameter Color
     * @param clr
     */
    public ColorPanel(Color clr)
    {
        setBackground(clr);     //set the background of the JPanel
    }
}

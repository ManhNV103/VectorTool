/**
 * JAVA DRAWING APP
 * Tran Quang Huy & Nguyen Van Manh
 */

package PaintTool;

import javax.swing.*;
import java.awt.*;

public class ColorPanel extends JPanel {

    /**
     * Constructor
     * @param clr
     */
    public ColorPanel(Color clr)
    {
        setBackground(clr);     //set the background of the JPanel
    }
}

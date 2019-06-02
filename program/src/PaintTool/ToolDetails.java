/**
 * JAVA DRAWING APP
 * @author Tran Quang Huy & Nguyen Van Manh
 */

package PaintTool;
import java.awt.*;

/**
 * ToolDetails
 * A class that extends the Tool class and provides a method for customizing the color
 */
public class ToolDetails extends Tool {
    private Color color;

    /**
     * Constructors with parameters Color for Tool and type parameter of Too
     * @param brushColor
     * @param type
     */

    public ToolDetails(Color brushColor, int type)
    {
        super(type);
        color = brushColor;
    }

    /**
     * Get color of the Tool
     * @return
     */

    public Color getColor()
    {
        return color;
    }

    /**
     * Set color of the Tool
     * @param clr
     */
    public void setColor(Color clr)
    {
        color = clr;
    }
}

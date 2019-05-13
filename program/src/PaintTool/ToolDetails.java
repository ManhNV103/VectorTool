/**
 * JAVA DRAWING APP
 * Tran Quang Huy & Nguyen Van Manh
 */

package PaintTool;
import java.awt.*;

public class ToolDetails extends Tool {
    protected Color color;

    // Constructors

    public ToolDetails(Color brushColor, int type)
    {
        super(type);
        color = brushColor;
    }

    // Methods

    public Color getColor()
    {
        return color;
    }

    public void setColor(Color clr)
    {
        color = clr;
    }
}

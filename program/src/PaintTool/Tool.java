/**
 * JAVA DRAWING APP
 * @author Tran Quang Huy & Nguyen Van Manh
 */

package PaintTool;

/**
 * Tool
 * A class used to represent the tool for drawing operation, with the given type parameter
 */
public class Tool {
    public int toolType;

    /**
     * Constructor with type parameter
     * @param type
     */
    public Tool (int type)
    {
        toolType = type;
    }
}

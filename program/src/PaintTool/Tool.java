/**
 * JAVA DRAWING APP
 * @author Tran Quang Huy & Nguyen Van Manh
 */

package PaintTool;

/**
 * Tool
 * A class used to create different Tool instances, with the given type parameter
 */
public class Tool {
    public int toolType;

    /**
     * Constructor with type parameter
     * @param type Integer
     */
    public Tool (int type)
    {
        toolType = type;
    }
}

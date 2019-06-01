/**
 * JAVA DRAWING APP
 * Tran Quang Huy & Nguyen Van Manh
 */

//class used to create different tool instances, depending on the given type parameter

package PaintTool;

/**
 * A class represent the Tool being used in drawing operation
 */
public class Tool {
    public int toolType;
    public Tool (int type)
    {
        toolType = type;
    }
}

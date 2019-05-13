/**
 * JAVA DRAWING APP
 * Tran Quang Huy & Nguyen Van Manh
 */


//Class which serves for creating the appropriate tool class
package PaintTool;

public class ToolFactory {
    //variables to hold different tool class instances
    private static Tool penTool;
    private static Tool fillTool;
    private static Tool undoTool;
    private static Tool rectTool;
    private static Tool plotTool;
    private static Tool lineTool;
    private static Tool ellipseTool;
    private static Tool polyTool;
    private static Tool currentTool;

    //static constant variables used to differentiate the tool classes
    public static final int PENCIL_TOOL = 0;
    public static final int FILL_TOOL = 1;
    public static final int UNDO_TOOL = 2;
    public static final int RECTANGLE_TOOL = 3;
    public static final int PLOT_TOOL = 4;
    public static final int LINE_TOOL = 5;
    public static final int ELLIPSE_TOOL = 6;
    public static final int POLYGON_TOOL = 7;

    /**
     * Creates a new instance of the Tool (depending on the given parameter), passing in the tool type parameter
     * Sets the  currentTool view to that of the newly created class
     * @param toolType  String parameter defining the type of the tool that is to be created
     * @return   currentTool
     */

    public static Tool createTool (int toolType)
    {
        switch(toolType)
        {
            case 0 :   if (penTool == null  )                //if class is null
                penTool =  new Tool(PENCIL_TOOL);             //create new class instance
                currentTool = penTool;                       //set currentTool to that class instance
                break;

            case 1 :   if (fillTool == null  )
                fillTool =  new Tool(FILL_TOOL);
                currentTool = fillTool;
                break;

            case 2 :   if (undoTool == null  )
                undoTool =  new Tool(UNDO_TOOL);
                currentTool = undoTool;
                break;

            case 3 :   if (rectTool == null  )
                rectTool =  new Tool(RECTANGLE_TOOL);
                currentTool = rectTool;
                break;

            case 4 :   if (plotTool == null  )
                plotTool =  new Tool(PLOT_TOOL);
                currentTool = plotTool;
                break;

            case 5 :   if (lineTool == null  )
                lineTool =  new Tool(LINE_TOOL);
                currentTool = lineTool;
                break;

            case 6 :   if (ellipseTool == null  )
                ellipseTool =  new Tool(ELLIPSE_TOOL);
                currentTool = ellipseTool;
                break;

            case 7 :   if (polyTool == null  )
                polyTool =  new Tool(POLYGON_TOOL);
                currentTool = polyTool;
                break;
        }

        return currentTool;
    }

}

/**
 * JAVA DRAWING APP
 * Tran Quang Huy & Nguyen Van Manh
 */


package PaintTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


// ALL the code below need to be fixed.

/**
 * Implementation of draw function should be implemented in this class (SquarePad).
 *
 */

class SquarePadDrawing extends JPanel implements MouseListener, MouseMotionListener {
    // an image to draw on
    Image image;

    // Graphics
    public Graphics2D graphics2D;

    //Mouse coordinates
    public int currentX, currentY, oldX, oldY;
    private Graphics2D dragGraphics;    // A graphics context for the off-screen image, to be used while a drag is in progress.

    int imageWidth, imageHeight;            // current width and height of OSI, used to check against the size of the window. If the size of the window changes, a new OSI is created
    int brushPoints[][];                //two-dimensional integer array used to display the brush effect
    public int startX, startY;         // the starting position of the mouse
    private boolean isDrawing;          // this is set to true when the user is isDrawing.
    protected Boolean mousePressed;     //indicates if the mouse is pressed
    public Color brushColor;            // the selectedColor that is used for the figure that is  being drawn.
    public Tool currentTool;            //indicates the isSelected Tool
    public ToolDetails currentToolDetails;  //isSelected tool details

    private String outfile = ""; // this records drawing actions (the content of the export file)


    //Now for the constructors
    public SquarePadDrawing(){
        setDoubleBuffered(false);
        addMouseListener(this);                         //add mouse listener
        addMouseMotionListener(this);                   //add mouse motion listener
        mousePressed = false;                           //set mousePressed to false
        brushColor = Color.BLACK;                       //set initial brush color
        currentTool = ToolFactory.createTool(ToolFactory.PENCIL_TOOL);             //set initial painting tool
        currentToolDetails = new ToolDetails(brushColor,  ToolFactory.PENCIL_TOOL);     //set initial painting tool properties
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

    /**
     * Method used to draw shapes in the graphics context
     * The Tool parameter determines which shape will be drawn
     * For a line, a line is drawn from the brushPoints (x1, y1) to brushPoints (x2, y2)
     * For other shapes, brushPoints (x1, y1) and (x2, y2) give two corners of the shape
     * @param graphics2D    Graphics class
     * @param currentTool  isSelected tool
     * @param pointX1     point x1
     * @param pointY1     point y1
     * @param pointX2     point x2
     * @param pointY2     point y2
     */
    public void drawGraphics(Graphics2D graphics2D, Tool currentTool, int pointX1, int pointY1, int pointX2, int pointY2)
    {
        int positionX, positionY;   // Top left corner of rectangle that contains the figure.
        int width, height;         // Width and height of rectangle that contains the figure.
        if (pointX1 >= pointX2)
        {  // pointX2 is left edge
            positionX = pointX2;
            width = pointX1 - pointX2;
        }
        else
        {   // pointX1 is left edge
            positionX = pointX1;
            width = pointX2 - pointX1;
        }
        if (pointY1 >= pointY2)
        {  // pointY2 is top edge
            positionY = pointY2;
            height = pointY1 - pointY2;
        }
        else
        {   // pointY1 is top edge
            positionY = pointY1;
            height = pointY2 - pointY1;
        }

        if (currentTool.toolType == ToolFactory.LINE_TOOL)                  //if isSelected tool is LINE
        {
            graphics2D.drawLine(pointX1, pointY1, pointX2, pointY2);
            repaint();
            return;
        }

        if(currentTool.toolType == ToolFactory.PLOT_TOOL){

            graphics2D.drawLine(pointX1, pointY1, pointX1, pointY1);
            repaint();
            return;
            
        }

        if (currentTool.toolType == ToolFactory.RECTANGLE_TOOL)            //if isSelected tool is RECTANGLE{
        {
            graphics2D.drawRect(positionX, positionY, width, height);
            repaint();
            return;
        }

        if (currentTool.toolType == ToolFactory.ELLIPSE_TOOL)                  //if isSelected tool is OVAL
        {
            graphics2D.drawOval(positionX, positionY, width, height);               //draw oval
            repaint();                                                      //repaint to properly display stroke
            return;
        }

        if (currentTool.toolType == ToolFactory.POLYGON_TOOL)
        {

        }


    }

    /**
     * Method used to repaint the rectangle
     * @param pointX1  point X
     * @param pointY1  point Y
     * @param pointX2  point X2
     * @param pointY2  point Y2
     */
    private void repaintRectangle(int pointX1, int pointY1, int pointX2, int pointY2)
    {
        int x, y;  // top left corner of rectangle that contains the figure
        int w, h;  // width and height of rectangle that contains the figure
        if (pointX2 >= pointX1)
        {   // pointX1 is left edge
            x = pointX1;
            w = pointX2 - pointX1;
        }
        else
        {   // pointX2 is left edge
            x = pointX2;
            w = pointX1 - pointX2;
        }
        if (pointY2 >= pointY1)
        {   // pointY1 is top edge
            y = pointY1;
            h = pointY2 - pointY1;
        }
        else
        {   // pointY2 is top edge.
            y = pointY2;
            h = pointY1 - pointY2;
        }
        repaint(x, y, w+1, h+1);      //add 1 pixel border along the right and bottom edges to allow for a pen overhang when isDrawing a line
    }

    /**
     * Method used to create the off-screen image
     * the method will create a new off-screen image if the size of the panel changes
     */
    private void createOffScreenImage()
    {
        if (image == null || imageWidth != getSize().width || imageHeight != getSize().height) {
            // Create the OSI, or make a new one if panel size has changed.
            image = null;  // (If OSI already exists, this frees up the memory.)
            image = createImage(getSize().width, getSize().height);
            imageWidth = getSize().width;
            imageHeight = getSize().height;
            Graphics graphics = image.getGraphics();  // Graphics context for isDrawing to OSI.
            graphics.setColor(getBackground());
            graphics.fillRect(0, 0, imageWidth, imageHeight);
            graphics.dispose();
        }
    }

    public int getHeight(){
        return getSize().height;
    }

    public int getWidth(){
        return getSize().width;
    }

    public void paintComponent(Graphics g){
        createOffScreenImage();                             //create off-screen image
        Graphics2D graphics = (Graphics2D)g;       //convert Graphics to Graphics2D
        g.drawImage(image, 0, 0, this);                //draw image
        if (isDrawing &&                                      //if isDrawing...
                currentTool.toolType != ToolFactory.PENCIL_TOOL &&
                currentTool.toolType != ToolFactory.FILL_TOOL &&
                currentTool.toolType != ToolFactory.UNDO_TOOL)
        {
            g.setColor(brushColor);                                             //set color
            drawGraphics(graphics, currentTool, startX, startY, currentX, currentY);     //call the drawGraphics method.

        }
    }



    public void clear(){
        graphics2D.setPaint(Color.white);
        graphics2D.fillRect(0, 0, getSize().width, getSize().height);
        graphics2D.setPaint(Color.BLACK);
        graphics2D.dispose();
        repaint();
    }




    private Color getCurrentColor()             //get the isSelected color from the TollDetails class
    {
        return currentToolDetails.getColor();

    }

    public void setCurrentColor(Color clr)         //set the isSelected color to the toolDetails class
    {
        brushColor = clr;
        currentToolDetails.setColor(clr);
    }

    /**
     * Use to set coordinates and update drawing when read from .vec file, used in Menu.java when file is imported
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */

    public void setCoordinatesAndDraw(int x1, int y1, int x2, int y2){


        oldX = startX = x1;
        oldY = startY = y1;
        brushColor = getCurrentColor();                 //get current color
        dragGraphics = (Graphics2D) image.getGraphics();  //convert Graphics
        dragGraphics.setColor(brushColor);              //set color
        dragGraphics.setBackground(getBackground());

        currentX = x2;
        currentY = y2;


        if (currentTool.toolType != ToolFactory.PENCIL_TOOL && currentTool.toolType != ToolFactory.FILL_TOOL && currentTool.toolType != ToolFactory.UNDO_TOOL)
        {
            repaintRectangle(startX, startY, oldX, oldY);
            if (currentX != startX && currentY != startY) {
                // Draw the shape only if both its height
                // and width are non-zero.
                drawGraphics(dragGraphics, currentTool, startX, startY, currentX, currentY);
                repaintRectangle(startX, startY, currentX, currentY);
                // Record what we've drawn
                if (currentTool.toolType == ToolFactory.LINE_TOOL){
                    outfile += "LINE" + " " + (double) startX / getWidth()  + " " + (double) startY / getHeight() + " "
                            + (double) currentX / getWidth()  + " " + (double) currentY / getHeight() + " " + "\n";
                }
                if (currentTool.toolType == ToolFactory.RECTANGLE_TOOL){
                    outfile += "RECTANGLE" + " " + (double) startX / getWidth()  + " " + (double) startY / getHeight() + " "
                            + (double) currentX / getWidth()  + " " + (double) currentY / getHeight() + " " + "\n";
                }
                if (currentTool.toolType == ToolFactory.ELLIPSE_TOOL){
                    outfile += "ELLIPSE" + " " + (double) startX / getWidth()  + " " + (double) startY / getHeight() + " "
                            + (double) currentX / getWidth()  + " " + (double) currentY / getHeight() + " " + "\n";
                }
                if (currentTool.toolType == ToolFactory.POLYGON_TOOL){

                }

            }
            else if (currentX == startX && currentY == startY && currentTool.toolType == ToolFactory.PLOT_TOOL) {
                drawGraphics(dragGraphics, currentTool, startX, startY, currentX, currentY);
                // Record the plot we've drawn
                outfile += "PLOT" + " " + (double) startX / getWidth()  + " " + (double) startY / getHeight() + "\n";

            }
        }
        dragGraphics.dispose();
        dragGraphics = null;
    }





    /**
     * Method called when the user presses the mouse button on the panel
     * begins the draw operation in which the user sketches a curve or draws a shape
     * for PENCIL or BRUSH, a new segment of the curve is drawn each time the user moves the mouse
     * for the other shapes, a "rubber band cursor" is used - the figure is drawn between the starting point
     * and the current mouse location
     * @param evt MouseEvent
     */
    public void mousePressed(MouseEvent evt)
    {
        if (isDrawing)                    // Ignore mouse presses that occur when user is already isDrawing a curve
            return;                     // if the user presses two mouse toolButtons at the same time

        oldX = startX = evt.getX();    // save mouse coordinates.
        oldY = startY = evt.getY();

        brushColor = getCurrentColor();                 //get current color
        dragGraphics = (Graphics2D) image.getGraphics();  //convert Graphics
        dragGraphics.setColor(brushColor);              //set color
        dragGraphics.setBackground(getBackground());
        isDrawing = true;                                 //start isDrawing
    }

    /**
     * to get output string to write to out put file. Used in Menu.java
     * @return
     */

    public String getOutFile(){
        return outfile;
    }

    /**
     * Method called when the user releases the mouse button
     * if the user was isDrawing a shape, the shape is drawn to the off-screen image
     * @param evt MouseEvent
     */
    public void mouseReleased(MouseEvent evt)
    {
        if (!isDrawing)
            return;             //return if the user isn't isDrawing.
        isDrawing = false;        //set isDrawing to false
        currentX = evt.getX();    //save mouse coordinates
        currentY = evt.getY();

        if (currentTool.toolType != ToolFactory.PENCIL_TOOL && currentTool.toolType != ToolFactory.FILL_TOOL && currentTool.toolType != ToolFactory.UNDO_TOOL)
        {
            repaintRectangle(startX, startY, oldX, oldY);
            if (currentX != startX && currentY != startY) {
                // Draw the shape only if both its height
                // and width are non-zero.
                drawGraphics(dragGraphics, currentTool, startX, startY, currentX, currentY);
                repaintRectangle(startX, startY, currentX, currentY);
                // Record what we've drawn
                if (currentTool.toolType == ToolFactory.LINE_TOOL){
                    outfile += "LINE" + " " + (double) startX / getWidth()  + " " + (double) startY / getHeight() + " "
                            + (double) currentX / getWidth()  + " " + (double) currentY / getHeight() + " " + "\n";
                }
                if (currentTool.toolType == ToolFactory.RECTANGLE_TOOL){
                    outfile += "RECTANGLE" + " " + (double) startX / getWidth()  + " " + (double) startY / getHeight() + " "
                            + (double) currentX / getWidth()  + " " + (double) currentY / getHeight() + " " + "\n";
                }
                if (currentTool.toolType == ToolFactory.ELLIPSE_TOOL){
                    outfile += "ELLIPSE" + " " + (double) startX / getWidth()  + " " + (double) startY / getHeight() + " "
                            + (double) currentX / getWidth()  + " " + (double) currentY / getHeight() + " " + "\n";
                }
                if (currentTool.toolType == ToolFactory.POLYGON_TOOL){

                }


            }
            else if (currentX == startX && currentY == startY && currentTool.toolType == ToolFactory.PLOT_TOOL) {
                drawGraphics(dragGraphics, currentTool, startX, startY, currentX, currentY);
                // Record what we've drawn
                outfile += "PLOT" + " " + (double) startX / getWidth()  + " " + (double) startY / getHeight() + "\n";

            }
        }

        dragGraphics.dispose();
        dragGraphics = null;
    }

    /**
     * Method called whenever the user moves the mouse while a mouse button is down
     * If the user is isDrawing a curve, draw a segment of the curve on the off-screen image, and repaint the part
     * and repaint the part of the panel that contains the new line segment.
     * Otherwise, just call repaint and let paintComponent() draw the shape on top of the picture in the off-screen image.
     * @param evt MouseEvent
     */
    public void mouseDragged(MouseEvent evt)
    {
        if (!isDrawing)
            return;  // return if the user isn't isDrawing.

        currentX = evt.getX();   // x-coordinate of mouse.
        currentY = evt.getY();   // y=coordinate of mouse.

        if (currentTool.toolType == ToolFactory.PENCIL_TOOL)
        {
            drawGraphics(dragGraphics, ToolFactory.createTool(ToolFactory.LINE_TOOL), oldX, oldY, currentX, currentY); // A CURVE is drawn as a series of LINEs.
            repaintRectangle(oldX, oldY, currentX, currentY);
        }

        else
        {
            repaintRectangle(startX, startY, oldX, oldY);     //repaint the rectangle that contains the previous version of the figure
            repaintRectangle(startX, startY, currentX, currentY);   //repaint the rectangle that contains the new version of the figure
        }

        oldX = currentX;  // Save coordinates for the next call to mouseDragged or mouseReleased.
        oldY = currentY;
    }





    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mouseMoved(MouseEvent e) {}


}
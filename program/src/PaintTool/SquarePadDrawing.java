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
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;


// ALL the code below need to be fixed.


/**
 * Implementation of draw function should be implemented in this class (SquarePad).
 *
 */

public class SquarePadDrawing extends JPanel implements MouseListener, MouseMotionListener {
    // an image to draw on
    private Image image;
    private Image backGround; //undonew
    private Stack<Image> savedImagesStack = new Stack<>(); //undonew
    private Stack<String> imageRecordStack = new Stack<>(); // this only record drawn images for the undo (not including set color, fill). So if you need to record everything for importing/exporting files, use outLines Stack below instead.
    private ImageStack<Image> historyImagesStack = new ImageStack<>(); // historyStack

    private Exception VecFileException;


    // Graphics
    private Graphics2D graphics;

    //Mouse coordinates
    private int currentX, currentY, oldX, oldY;
    private Graphics2D dragGraphics;    // A graphics context for the off-screen image, to be used while a drag is in progress.
    // Variables for polygon drawing
    private List<Polygon> polygons = new ArrayList<Polygon>();  //  List of normal polygons drawn
    private List<Polygon> filledPolygons = new ArrayList<Polygon>(); // List of filled polygons drawn
    private Polygon currentPolygon = new Polygon(); // Current polygon drawn
    private Polygon currentFilledPolygon = new Polygon(); // Current filled polygon drawn
    private List<Color> polyColor = new ArrayList<>();  // List of color corresponding to polygons
    private List<Color> polyFilledColor = new ArrayList<>(); // List of color corresponding to filled polygons
    private List<Integer> xPoly = new ArrayList<>();  // Store x coordinate of of each polygon in each line of vec file
    private List<Integer> yPoly = new ArrayList<>(); // Store x coordinate of of each polygon in each line of vec file
    private Polygon inputPolygon = new Polygon();    // A polygon from vec file
    private List<Polygon> inputPolygons = new ArrayList<>(); // List of polygons from vec file



    int imageWidth, imageHeight;            // current width and height of OSI, used to check against the size of the window. If the size of the window changes, a new OSI is created
    private int startX, startY;         // the starting position of the mouse
    private boolean isDrawing;          // this is set to true when the user is isDrawing.
    protected Boolean mousePressed;     //indicates if the mouse is pressed
    public Color brushColor;            // the selectedColor that is used for the figure that is  being drawn.
    public Tool currentTool;            //indicates the isSelected Tool
    public ToolDetails currentToolDetails;  //isSelected tool details

    private String outfile = ""; // this records drawing actions (the content of the export file)
    private Stack<String> outLines = new Stack<String>();
    private Image blankImage;

    private boolean fill;


    //Now for the constructors
    public SquarePadDrawing(){
        setDoubleBuffered(false);
        addMouseListener(this);                         //add mouse listener
        addMouseMotionListener(this);                   //add mouse motion listener
        mousePressed = false;                           //set mousePressed to false
        brushColor = Color.BLACK;                       //set initial brush color
        currentTool = ToolFactory.createTool(ToolFactory.LINE_TOOL);             //set initial painting tool
        currentToolDetails = new ToolDetails(brushColor,  ToolFactory.LINE_TOOL);     //set initial painting tool propertiesbrushColor
        imageRecordStack.push("New drawings");
        fill = false;
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
     * Method used to draw the all polygons in the graphic context
     * @param graphics2D Graphics class
     * @param polygons The list of polygons drawn by the user
     */

    public void drawPolygonGraphics(Graphics2D graphics2D, Polygon currentPolygon, List<Polygon> polygons,List<Color> listColor,boolean var)
    {
        for (Polygon polygon : polygons) {
            if(listColor.size() >= 1){
                int i = polygons.indexOf(polygon);
                Color c = listColor.get(i);
                drawPolygon(graphics2D, polygon,c,var);
            }
            else{
                drawPolygon(graphics2D, polygon,Color.BLACK,var);
            }

        }
        if(listColor.size() >= 1){
            drawPolygon(graphics2D,currentPolygon,listColor.get(listColor.size()-1),var);
        }
        drawPolygon(graphics2D,currentPolygon,Color.BLACK,var);
        repaint();

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

        if (currentTool.toolType == ToolFactory.CLEAR_TOOL){
            float[] fa = {10, 10, 10};
            BasicStroke bs = new BasicStroke(20, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 50, fa, 50 );
            graphics2D.setStroke(bs);
            graphics2D.drawLine(pointX1, pointY1, pointX2, pointY2);
            repaint();
            return;

        }

        if (currentTool.toolType == ToolFactory.RECTANGLE_TOOL)            //if isSelected tool is RECTANGLE{
        {
            graphics2D.drawRect(positionX, positionY, width, height);
            repaint();
            return;
        }

        if (currentTool.toolType == ToolFactory.ELLIPSE_TOOL)                  //if isSelected tool is ELLIPSE
        {
            graphics2D.drawOval(positionX, positionY, width, height);               //draw ellipse
            repaint();                                                      //repaint to properly display stroke
            return;
        }

        if (currentTool.toolType == ToolFactory.FILLED_ELLIPSE_TOOL)            //if isSelected tool is FILLED ELLIPSE
        {
            graphics2D.fillOval(positionX, positionY, width, height);      //draw filled ellipse
            return;
        }

        if (currentTool.toolType == ToolFactory.FILLED_RECTANGLE_TOOL)      //if isSelected tool is FILLED RECTANGLE
        {
            graphics2D.fillRect(positionX, positionY, width, height);      //draw filled rectangle
            return;
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

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        createOffScreenImage();                             //create off-screen image
        getPreferredSize();
        graphics = (Graphics2D) g;       //convert Graphics to Graphics2D
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);                //draw image
        if (isDrawing &&                                      //if isDrawing...
                currentTool.toolType != ToolFactory.CLEAR_TOOL)
        {
            g.setColor(brushColor);                                             //set color
            drawGraphics(graphics, currentTool, startX, startY, currentX, currentY);     //call the drawGraphics method.

        }
        if (currentTool.toolType != ToolFactory.CLEAR_TOOL){
            drawPolygonGraphics(graphics,currentPolygon,polygons,polyColor,false);
            drawPolygonGraphics(graphics,currentFilledPolygon,filledPolygons,polyFilledColor,true);
        }
    }




    public void clear(){
        graphics.fillRect(0, 0, getSize().width, getSize().height);
        graphics.dispose();
        repaint();

    }


    private Color getCurrentColor()             //get the isSelected color from the TollDetails class
    {
        if (currentTool.toolType != ToolFactory.CLEAR_TOOL)
        {
            return currentToolDetails.getColor();
        }
        else
        {
            return getBackground();
        }

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

    public void setCoordinatesAndDraw(int x1, int y1, int x2, int y2,Polygon polygon,Color color){

        saveToStack(image);
        saveToHistoryStack(image);
        oldX = startX = x1;
        oldY = startY = y1;
        brushColor = color;                 //get current color
        dragGraphics = (Graphics2D) image.getGraphics();  //convert Graphics
        dragGraphics.setColor(brushColor);              //set color
        dragGraphics.setBackground(getBackground());

        currentX = x2;
        currentY = y2;


        if ( currentTool.toolType != ToolFactory.CLEAR_TOOL)
        {
            repaintRectangle(startX, startY, oldX, oldY);
            drawPolygon(dragGraphics,polygon,brushColor,false);
            if (currentX != startX && currentY != startY) {
                // Draw the shape only if both its height
                // and width are non-zero.
                drawGraphics(dragGraphics, currentTool, startX, startY, currentX, currentY);
                repaintRectangle(startX, startY, currentX, currentY);
                // Record what we've drawn
                if (currentTool.toolType == ToolFactory.LINE_TOOL){
                    outLines.push("LINE" + " " + (double) startX / getWidth()  + " " + (double) startY / getHeight() + " "
                            + (double) currentX / getWidth()  + " " + (double) currentY / getHeight() + " " + "\n");
                    imageRecordStack.push("LINE" + " " + (double) startX / getWidth()  + " " + (double) startY / getHeight() + " "
                                    + (double) currentX / getWidth()  + " " + (double) currentY / getHeight() + " " + "\n");
                }
                if (currentTool.toolType == ToolFactory.RECTANGLE_TOOL){
                    outLines.push("RECTANGLE" + " " + (double) startX / getWidth()  + " " + (double) startY / getHeight() + " "
                            + (double) currentX / getWidth()  + " " + (double) currentY / getHeight() + " " + "\n");
                    imageRecordStack.push("RECTANGLE" + " " + (double) startX / getWidth()  + " " + (double) startY / getHeight() + " "
                            + (double) currentX / getWidth()  + " " + (double) currentY / getHeight() + " " + "\n");
                }
                if (currentTool.toolType == ToolFactory.ELLIPSE_TOOL){
                    outLines.push("ELLIPSE" + " " + (double) startX / getWidth()  + " " + (double) startY / getHeight() + " "
                            + (double) currentX / getWidth()  + " " + (double) currentY / getHeight() + " " + "\n");
                    imageRecordStack.push("ELLIPSE" + " " + (double) startX / getWidth()  + " " + (double) startY / getHeight() + " "
                            + (double) currentX / getWidth()  + " " + (double) currentY / getHeight() + " " + "\n");
                }
                if (currentTool.toolType == ToolFactory.POLYGON_TOOL){

                }

            }
            else if (currentX == startX && currentY == startY && currentTool.toolType == ToolFactory.PLOT_TOOL) {
                drawGraphics(dragGraphics, currentTool, startX, startY, currentX, currentY);
                // Record the plot we've drawn
                outLines.push("PLOT" + " " + (double) startX / getWidth()  + " " + (double) startY / getHeight() + "\n");
                imageRecordStack.push("PLOT" + " " + (double) startX / getWidth()  + " " + (double) startY / getHeight() + "\n");

            }
        }
        dragGraphics.dispose();
        dragGraphics = null;
    }


    public static void checkLine(String line) throws VecFileException {
        String[] splitArray = line.split(" ");
        if (!(splitArray[0].equals("FILL") || splitArray[0].equals("PEN") || splitArray[0].equals("PLOT") ||
                splitArray[0].equals("RECTANGLE") || splitArray[0].equals("ELLIPSE") || splitArray[0].equals("POLYGON"))) {
            throw new VecFileException("Invalid vec format: Wrong shapes");
        } else {
            if (splitArray[0].equals("FILL")) {
                System.out.println("1");
                if (!isColor(splitArray[1])) {
                    System.out.println("2");
                    throw new VecFileException("Invalid vec format: Color is not valid.");
                }

                if (splitArray.length != 2) {
                    System.out.println("3");
                    throw new VecFileException("Invalid format for FILL. Correct format: FILL [color]");
                }
            }
            if (splitArray[0].equals("PEN")) {
                if (!isColor(splitArray[1])) {
                    throw new VecFileException("Invalid vec format: Color is not valid.");
                }

                if (splitArray.length != 2) {
                    throw new VecFileException("Invalid format for PEN. Correct format: PEN [color]");
                }
            }
            if (splitArray[0].equals("PLOT")) {
                if (splitArray.length != 3) {
                    throw new VecFileException("Invalid vec format for PLOT. Correct format: ");
                }
                if (!isValidNumber((splitArray[1]))){
                    throw new VecFileException("Invalid vec format: Coordinates have to be numeric and in the range [0, 1]");
                }
            }


            // more here

        }
    }

    public static boolean isValidNumber(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }

        double num = Double.parseDouble(strNum);
        if (num >= 0 && num <=1){
            return true;
        }
        else {
            return false;
        }

    }

    public static boolean isColor(String color){
        try{
            Color.decode(color);
        }
        catch(NumberFormatException e){
            return false;
        }
        return true;
    }




    public void drawLine(String line){

        String[] splitArray = line.split("\\s+");
        Color c = Color.BLACK;

        if (!splitArray[1].equals("OFF") && splitArray.length == 2){ //0??
            try {
                c = Color.decode(splitArray[1]);
            }
            catch(NumberFormatException exp){
                System.out.println("Invalid color");
            }

        }


        if (splitArray[0].equals("FILL") && !splitArray[1].equals("OFF")){
            fill = true;
        }

        if (splitArray[0].equals("FILL") && splitArray[1].equals("OFF")){
            fill = false;
        }

        if (splitArray[0].equals("PEN")){
            //set color
        }


        /// Sai het roi. from here down

        if(fill){
            if (splitArray[0].equals("RECTANGLE")){
                currentTool = ToolFactory.createTool(6);
            }
            if (splitArray[0].equals("ELLIPSE")){
                currentTool = ToolFactory.createTool(7);
            }
            if (splitArray[0].equals("POLYGON")){
                currentTool = ToolFactory.createTool(8);
            }
        }
        else{
            if (splitArray[0].equals("RECTANGLE")){
                currentTool = ToolFactory.createTool(1);
            }
            if (splitArray[0].equals("PLOT")){
                currentTool = ToolFactory.createTool(2);
            }
            if (splitArray[0].equals("LINE")){
                currentTool = ToolFactory.createTool(3);
            }
            if (splitArray[0].equals("ELLIPSE")){
                currentTool = ToolFactory.createTool(4);
            }
            if (splitArray[0].equals("POLYGON")){
                currentTool = ToolFactory.createTool(5);
            }
        }


        //System.out.println("height" + Paint.squarePad.getHeight());
        //System.out.println("width" + Paint.squarePad.getWidth());
        int h = getHeight();
        int w = getWidth();

        if (splitArray[0].equals("PLOT")){
            int x1 = (int) Math.round(Double.parseDouble(splitArray[1]) * h);
            int y1 = (int) Math.round(Double.parseDouble(splitArray[2]) * w);
            setCoordinatesAndDraw(x1, y1, x1, y1,inputPolygon,c);
        }
            /*
            else if (splitArray[0].equals("POLYGON")){
                if(!xPoly.isEmpty() || !yPoly.isEmpty()){
                    xPoly = new ArrayList<>();
                    yPoly = new ArrayList<>();
                }
                for(int i = 1; i < splitArray.length;i++){
                    if(i%2 == 1){
                        int x = (int)  Math.round(Double.parseDouble(splitArray[i]) * h);
                        xPoly.add(x);
                    }
                    else {
                        int y = (int)  Math.round(Double.parseDouble(splitArray[i]) * w);
                        yPoly.add(y);
                    }
                }
                int[] xList = xPoly.stream().mapToInt(Integer::intValue).toArray();
                int[] yList = yPoly.stream().mapToInt(Integer::intValue).toArray();
                inputPolygon = new Polygon(xList,yList,xPoly.size());
                inputPolygons.add(inputPolygon);
                setCoordinatesAndDraw(0, 0, 0, 0,inputPolygon,c);
            }*/
        else if (!splitArray[0].equals("PEN") && !splitArray[0].equals("FILL")){
            int x1 = (int) Math.round(Double.parseDouble(splitArray[1]) * h);
            int y1 = (int) Math.round(Double.parseDouble(splitArray[2]) * w);
            int x2 = (int) Math.round(Double.parseDouble(splitArray[3]) * h);
            int y2 = (int) Math.round(Double.parseDouble(splitArray[4]) * w);
            setCoordinatesAndDraw(x1, y1, x2, y2,inputPolygon,c);

        }

        //System.out.println(savedImagesStack);

        repaint();
    }

    public void drawFromFile(Scanner scanner){
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            try{
                checkLine(line);
                drawLine(line);
            }
            catch(VecFileException e){
                System.out.println(e.getMessage());
            }

            // the whole thing after this should be inside try block.




            //System.out.println(splitArray[0]);
        }
    }


    /**
     * Method used to draw a polygon based on the number of points
     * @param g Graphics class
     * @param polygon the selected polygon
     * @param c Color
     */
    private void drawPolygon(Graphics g, Polygon polygon, Color c,boolean filledPoly) {
        if (polygon.npoints < 3) {
            if (polygon.npoints == 1) {
                g.setColor(c);
                g.fillOval(polygon.xpoints[0] - 2, polygon.ypoints[0] - 2, 4, 4);
            } else if (polygon.npoints == 2) {
                g.setColor(c);
                g.drawLine(polygon.xpoints[0], polygon.ypoints[0], polygon.xpoints[1], polygon.ypoints[1]);
            }
        } else {
            g.setColor(c);
            if (filledPoly){
                g.fillPolygon(polygon);
            }
            else{
                g.drawPolygon(polygon);
            }
        }
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

        if(currentTool.toolType != ToolFactory.POLYGON_TOOL && currentTool.toolType != ToolFactory.FILLED_POLYGON_TOOL){
            saveToStack(image);
        }
        System.out.println("Stack length " + savedImagesStack.size());



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

    public Stack<String> getOutLines(){
        return outLines;
    }
    public Stack<String> getImageRecordStack(){
        return imageRecordStack;
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


        if (currentTool.toolType != ToolFactory.CLEAR_TOOL)
        {
            repaintRectangle(startX, startY, oldX, oldY);
            if (currentX != startX && currentY != startY) {
                // Draw the shape only if both its height
                // and width are non-zero.
                drawGraphics(dragGraphics, currentTool, startX, startY, currentX, currentY);
                repaintRectangle(startX, startY, currentX, currentY);
                drawPolygonGraphics(dragGraphics,currentPolygon, polygons,polyColor,false);
                drawPolygonGraphics(graphics,currentFilledPolygon,filledPolygons,polyFilledColor,true);
                // Record what we've drawn
                if (currentTool.toolType == ToolFactory.LINE_TOOL){
                    outLines.push("LINE" + " " + (double) startX / getWidth()  + " " + (double) startY / getHeight() + " "
                            + (double) currentX / getWidth()  + " " + (double) currentY / getHeight() + " " + "\n");
                    imageRecordStack.push("LINE" + " " + (double) startX / getWidth()  + " " + (double) startY / getHeight() + " "
                            + (double) currentX / getWidth()  + " " + (double) currentY / getHeight() + " " + "\n");

                }
                if (currentTool.toolType == ToolFactory.RECTANGLE_TOOL){
                    outLines.push("RECTANGLE" + " " + (double) startX / getWidth()  + " " + (double) startY / getHeight() + " "
                            + (double) currentX / getWidth()  + " " + (double) currentY / getHeight() + " " + "\n");
                    imageRecordStack.push("RECTANGLE" + " " + (double) startX / getWidth()  + " " + (double) startY / getHeight() + " "
                            + (double) currentX / getWidth()  + " " + (double) currentY / getHeight() + " " + "\n");
                }
                if (currentTool.toolType == ToolFactory.ELLIPSE_TOOL){
                    outLines.push("ELLIPSE" + " " + (double) startX / getWidth()  + " " + (double) startY / getHeight() + " "
                            + (double) currentX / getWidth()  + " " + (double) currentY / getHeight() + " " + "\n");
                    imageRecordStack.push("ELLIPSE" + " " + (double) startX / getWidth()  + " " + (double) startY / getHeight() + " "
                            + (double) currentX / getWidth()  + " " + (double) currentY / getHeight() + " " + "\n");
                }
                if (currentTool.toolType == ToolFactory.POLYGON_TOOL){

                }


            }
            else if (currentX == startX && currentY == startY && currentTool.toolType == ToolFactory.PLOT_TOOL) {
                drawGraphics(dragGraphics, currentTool, startX, startY, currentX, currentY);
                // Record what we've drawn
                outLines.push("PLOT" + " " + (double) startX / getWidth()  + " " + (double) startY / getHeight() + "\n");
                imageRecordStack.push("ELLIPSE" + " " + (double) startX / getWidth()  + " " + (double) startY / getHeight() + " "
                        + (double) currentX / getWidth()  + " " + (double) currentY / getHeight() + " " + "\n");

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



        if (currentTool.toolType == ToolFactory.CLEAR_TOOL)
        {

            drawGraphics(dragGraphics, ToolFactory.createTool(ToolFactory.CLEAR_TOOL), oldX, oldY, currentX, currentY); // A CURVE is drawn as a series of LINEs.
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


    /**
     * Undo/Redo methods
     */

    public void undo() {
        if (savedImagesStack.size() > 0) {
            if (outLines.size() > 0){
                outLines.pop();
            }

            if (imageRecordStack.size() > 0){
                imageRecordStack.pop();
            }



            setImage(savedImagesStack.pop());

            System.out.println(outLines.size());
            System.out.println(imageRecordStack.size());
            System.out.println(savedImagesStack.size());
        }
    }

    public void updateImageOnRequest(){
        saveToStack(image);
    }

    public void popImagesFromStack(int i){
        // pop i (last) elements from the stack
        for (int k = 0; k < i; k++){
            setImage(savedImagesStack.pop());
        }
        for (int k = 0; k < i - 1; k++){
            imageRecordStack.pop();
        }

        // don't delete the line where we fill/pen.
        int count = 0;
        for (int k = outLines.size() -1; k >=0; k--){
            if(outLines.get(k).split(" ")[0] != "FILL" || outLines.get(k).split(" ")[0] != "PEN"){
                outLines.remove(k);
                count++;
            }
            if (count >= i - 1){
                break;
            }
        }

    }
    public Stack<Image> getImageStack(){
        return savedImagesStack;
    }

    public void blankImage(){
         setImage(savedImagesStack.get(0));
    }

    public void renderRequestImage(int i){
        setImage(savedImagesStack.get(i));
    }

    private void setImage(Image img) {
        graphics = (Graphics2D) img.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setPaint(Color.black);
        image = img;
        repaint();
    }
    /*
    public void setBackground(Image img) {
        backGround = copyImage(img);
        setImage(copyImage(img));
    }*/

    private BufferedImage copyImage(Image img) {
        BufferedImage copyOfImage = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_RGB);
        Graphics g = copyOfImage.createGraphics();
        g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
        return copyOfImage;
    }

    private void saveToStack(Image img) {
        savedImagesStack.push(copyImage(img));
        if (savedImagesStack.size() != 0){
            Menu.enableUndo();
        }
    }

    private void saveToHistoryStack(Image img) {
        historyImagesStack.push(copyImage(img));
    }

    public int getStackSize(){
        return savedImagesStack.size();
    }




    /**
     * Method used to record the number of mouse clicks to draw the polygon
     * Click the left mouse to add a point to the polygon
     * Double click the left mouse to complete a polygon
     * Right click to delete the current polygon
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if(currentTool.toolType == ToolFactory.POLYGON_TOOL) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                if (e.getClickCount() == 1) {
                    currentPolygon.addPoint(e.getX(), e.getY());
                    repaint();
                } else if (e.getClickCount() == 2) {
                    if (currentPolygon.npoints > 2) {
                        polygons.add(currentPolygon);
                    }
                    saveToStack(image);
                    System.out.println("Stack length " + savedImagesStack.size());

                    polyColor.add(brushColor);
                    currentPolygon = new Polygon();
                    repaint();
                }
            } /*else if (SwingUtilities.isRightMouseButton(e)) {
                currentPolygon = new Polygon();
                repaint();
            }*/
        }
        else if(currentTool.toolType == ToolFactory.FILLED_POLYGON_TOOL){
            if (SwingUtilities.isLeftMouseButton(e)) {
                if (e.getClickCount() == 1) {
                    currentFilledPolygon.addPoint(e.getX(), e.getY());
                    repaint();
                } else if (e.getClickCount() == 2) {
                    if (currentFilledPolygon.npoints > 2) {
                        filledPolygons.add(currentFilledPolygon);
                    }
                    saveToStack(image);
                    System.out.println("Stack length " + savedImagesStack.size());
                    polyFilledColor.add(brushColor);
                    currentFilledPolygon = new Polygon();
                    repaint();
                }
            }
            /*else if (SwingUtilities.isRightMouseButton(e)) {
                currentFilledPolygon = new Polygon();
                repaint();
            }*/
        }

    }


    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mouseMoved(MouseEvent e) {}


}

class ImageStack<E> extends Stack<E> {

    public ImageStack() {
        super();
    }

    @Override
    public Object push(Object object) {
        /*
        while (this.size() > maxSize) {
            this.remove(0);
        }*/
        return super.push((E) object);
    }
}
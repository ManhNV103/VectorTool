/**
 * JAVA DRAWING APP
 * @author Tran Quang Huy & Nguyen Van Manh
 */

package PaintTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;


/**
 * SquarePadDrawing
 * A Class that extends the JPanel and contains all the drawing logic with code
 */

public class SquarePadDrawing extends JPanel implements MouseListener, MouseMotionListener {
    private Image image;// an image to draw on
    private Image backGround; //undonew
    private Stack<Image> savedImagesStack = new Stack<>(); //undonew
    private Stack<String> imageRecordStack = new Stack<>(); // this only record drawn images for the undo (not including set color, fill). So if you need to record everything for importing/exporting files, use outLines Stack below instead.
    private ImageStack<Image> historyImagesStack = new ImageStack<>(); // historyStack

    private Exception VecFileException;

    // Graphics
    private Graphics2D graphics;

    //Mouse coordinates
    private int currentX, currentY; // Current x, y coordinates of mouse
    private int oldX, oldY;         // Previous x,y coordinates of mouse
    private Graphics2D dragGraphics;    // A graphics context for the off-screen image, to be used while a drag is in progress.

    // Variables for polygon drawing
    private ArrayList<Integer> xPoly = new ArrayList<>();  // Store x coordinate of of each polygon in each line of vec file
    private ArrayList<Integer> yPoly = new ArrayList<>(); // Store x coordinate of of each polygon in each line of vec file

    private int imageWidth, imageHeight;    // current width and height of image, used to check against the size of the window. If the size of the window changes, a new OSI is created
    private int startX, startY;         // the starting position of the mouse
    private boolean isDrawing;          // this is set to true when the user is isDrawing.

    ///public Color brushColor;            // the selectedColor that is used for the figure that is  being drawn.
    public Color penColor;              // current pen color for drawing
    public Color fillColor;             // current fill color for drawing
    public Tool currentTool;            //indicates the selected tool
    public ToolDetails currentToolDetails;  //selected tool details

    private String outfile = ""; // this records drawing actions (the content of the export file)
    public Stack<String> outLines = new Stack<String>();
    //private Image blankImage;
    private Color transparent = new Color(1f,0f,0f,0 );
    public boolean fill; //determine whether the shape is filled or not

    /**
     * Default constructor
     */
    public SquarePadDrawing(){
        setDoubleBuffered(false);
        addMouseListener(this);                         //add mouse listener
        addMouseMotionListener(this);                   //add mouse motion listener
        currentTool = ToolFactory.createTool(ToolFactory.LINE_TOOL);             //set initial painting tool
        currentToolDetails = new ToolDetails(penColor,  ToolFactory.LINE_TOOL);     //set initial color and type of painting tool details
        imageRecordStack.push("New drawings");
        fill = false;
        penColor = Color.BLACK; /// initial pen color
        fillColor = transparent; /// initial fill color
    }

    /**
     * Method used to scale the size of drawing screen
     */
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
     * Method used to record the number of mouse clicks to draw the polygon
     * Click the left mouse to add a point to the polygon
     * Right click to stop adding a point to the polygon and draw the polygon
     * @param e MouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if(currentTool.toolType == ToolFactory.POLYGON_TOOL) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                if (e.getClickCount() == 1) {
                    System.out.println("left tick");
                    xPoly.add(currentX);
                    yPoly.add(currentY);
                    repaint();
                    if(xPoly.size() % 2 ==0 && xPoly.size() > 0){
                        int i = xPoly.size();
                        graphics.drawLine(xPoly.get(i-2), yPoly.get(i-2), xPoly.get(i-1), yPoly.get(i-1));
                    }
                }
            }
            if (SwingUtilities.isRightMouseButton(e)) {
                xPoly.add(currentX);
                yPoly.add(currentY);
                System.out.println("right tick");
                saveToStack(image);
                String temp = "POLYGON ";
                for (int i = 0; i< xPoly.size(); i++){
                    temp += (double) xPoly.get(i) / getWidth() + " " + (double) yPoly.get(i) /getHeight();
                    if (i != xPoly.size() - 1){
                        temp += " ";
                    }
                    else {
                        temp += "\n";
                    }
                }

                drawGraphics(dragGraphics, currentTool, startX, startY, currentX, currentY, xPoly, yPoly);
                outLines.push(temp);
                imageRecordStack.push(temp);
                repaint();

            }

        }

    }

    /**
     * Method used to draw shapes in the graphics context
     * The Tool parameter determines which shape will be drawn
     * For a line, a line is drawn from the brushPoints (x1, y1) to brushPoints (x2, y2)
     * For other shapes, brushPoints (x1, y1) and (x2, y2) give two corners of the shape
     * @param graphics2D Graphics class
     * @param currentTool isSelected tool
     * @param pointX1 point x1
     * @param pointY1 point y1
     * @param pointX2 point x2
     * @param pointY2 point y2
     * @param xPolyList list of x coordinates of polygon
     * @param yPolyList list of y coordinates of polygons
     */
    public void drawGraphics(Graphics2D graphics2D, Tool currentTool, int pointX1, int pointY1, int pointX2, int pointY2, ArrayList<Integer> xPolyList, ArrayList<Integer> yPolyList)
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

        if (currentTool.toolType == ToolFactory.LINE_TOOL)  //if the selected Tool is LINE
        {
            graphics2D.setColor(penColor);
            graphics2D.drawLine(pointX1, pointY1, pointX2, pointY2);
            return;
        }

        if(currentTool.toolType == ToolFactory.PLOT_TOOL) // if the selected Tool is PLOT
        {
            graphics2D.setColor(penColor);
            graphics2D.drawLine(pointX1, pointY1, pointX1, pointY1);
            return;

        }

        if (currentTool.toolType == ToolFactory.CLEAR_TOOL) // if the selected Tool is CLEAR
        {
            float[] fa = {10, 10, 10};
            BasicStroke bs = new BasicStroke(20, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 50, fa, 50 );
            graphics2D.setStroke(bs);
            graphics2D.drawLine(pointX1, pointY1, pointX2, pointY2);
            return;

        }

        if (currentTool.toolType == ToolFactory.RECTANGLE_TOOL)    // if the selected Tool is RECTANGLE
        {
            graphics2D.setColor(penColor);
            graphics2D.drawRect(positionX, positionY, width, height);
            if(fill){
                graphics2D.setColor(fillColor);
                graphics2D.fillRect(positionX + 1, positionY + 1, width - 1, height - 1);
            }
            return;
        }

        if (currentTool.toolType == ToolFactory.ELLIPSE_TOOL)  // if the selected Tool is ELLIPSE
        {
            graphics2D.setColor(penColor);
            graphics2D.drawOval(positionX, positionY, width, height);

            if(fill){
                graphics2D.setColor(fillColor);
                graphics2D.fillOval(positionX + 1, positionY + 1, width - 2, height - 2);
            }

            return;
        }

        if (currentTool.toolType == ToolFactory.POLYGON_TOOL) // if the selected Tool is POLYGON
        {
            int[] xList = xPolyList.stream().mapToInt(Integer::intValue).toArray();
            int[] yList = yPolyList.stream().mapToInt(Integer::intValue).toArray();

            Polygon aPoly = new Polygon(xList, yList, xPolyList.size());
            graphics2D.setColor(penColor);
            graphics2D.drawPolygon(aPoly);
            if(fill){
                graphics2D.setColor(fillColor);
                graphics2D.fillPolygon(aPoly);
            }

            xPoly.clear();
            yPoly.clear();

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
            // Create the image, or make a new one if panel size has changed.
            image = null;  // (If image already exists, this frees up the memory.)
            image = createImage(getSize().width, getSize().height);
            imageWidth = getSize().width;
            imageHeight = getSize().height;
            Graphics graphics = image.getGraphics();  // Graphics context for isDrawing to OSI.
            graphics.setColor(getBackground());
            graphics.fillRect(0, 0, imageWidth, imageHeight);
            graphics.dispose();
        }
    }

    /**
     * Method used to get the height of the screen image
     * @return Integer
     */
    public int getHeight(){
        return getSize().height;
    }

    /**
     * Method used to get the width of the screen image
     * @return Integer
     */
    public int getWidth(){
        return getSize().width;
    }

    /**
     * overrides method in JComponent
     * @param g Graphics
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        createOffScreenImage();
        getPreferredSize();
        graphics = (Graphics2D) g;       //convert Graphics to Graphics2D
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0f));
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);  //draw image
        if (isDrawing && currentTool.toolType != ToolFactory.CLEAR_TOOL){
            drawGraphics(graphics, currentTool, startX, startY, currentX, currentY, xPoly, yPoly);     //call the drawGraphics method.
        }
    }

    /**
     * Method used to get the current color of the selected Tool
     * @return Color
     */
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

    /**
     * Method used to convert the current RGB color to Hex color written in the output file
     * @param colour Color
     * @return String
     * @throws NullPointerException Exception
     */
    public static String toHexString(Color colour) throws NullPointerException {
        String hexColour = Integer.toHexString(colour.getRGB() & 0xffffff);
        if (hexColour.length() < 6) {
            hexColour = "000000".substring(0, 6 - hexColour.length()) + hexColour;
        }
        return "#" + hexColour;
    }

    /**
     * Method used to set the current pen color and add drawing commands into the output file
     * @param clr Color
     */
    public  void setCurrentPenColor(Color clr)
    {
        penColor = clr;
        currentToolDetails.setColor(clr);
        outLines.push("PEN" + " " + toHexString(clr) + "\n");
    }

    /**
     * Method used to set the current fill color and add drawing commands into the output file
     * @param clr Color
     */
    public void setCurrentFillColor(Color clr)
    {
        fill = true;
        fillColor = clr;
        outLines.push("FILL" + " " + toHexString(clr) + "\n");
    }

    /**
     * Use to set coordinates and update drawing when read from .vec file, used in Menu.java when file is imported
     * @param x1 Integer
     * @param y1 Integer
     * @param x2 Integer
     * @param y2 Integer
     * @param penColorInput Color
     * @param fillColorInput Color
     */

    public void setCoordinatesAndDraw(int x1, int y1, int x2, int y2, Color penColorInput,Color fillColorInput){

        saveToStack(image);
        saveToHistoryStack(image);
        oldX = startX = x1;
        oldY = startY = y1;
        penColor = penColorInput;                 //get current color
        dragGraphics = (Graphics2D) image.getGraphics();  //convert Graphics
        dragGraphics.setColor(penColor);
        dragGraphics.setBackground(getBackground());

        currentX = x2;
        currentY = y2;


        if ( currentTool.toolType != ToolFactory.CLEAR_TOOL)
        {
            if (currentTool.toolType == ToolFactory.POLYGON_TOOL){
                //drawPolygon(dragGraphics,polygon,penColorInput,fillColorInput);

                //repaintRectangle(startX, startY, oldX, oldY);
                String temp = "POLYGON ";
                for (int i = 0; i< xPoly.size(); i++){
                    temp += (double) xPoly.get(i) / getWidth() + " " + (double) yPoly.get(i) /getHeight();
                    if (i != xPoly.size() - 1){
                        temp += " ";
                    }
                    else {
                        temp += "\n";
                    }
                }

                drawGraphics(dragGraphics, currentTool, startX, startY, currentX, currentY, xPoly, yPoly);
                outLines.push(temp);
                imageRecordStack.push(temp);

            }
            //repaintRectangle(startX, startY, oldX, oldY);
            if (currentX != startX && currentY != startY) {
                // Draw the shape only if both its height and width are non-zero.
                drawGraphics(dragGraphics, currentTool, startX, startY, currentX, currentY, xPoly, yPoly);
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
                drawGraphics(dragGraphics, currentTool, startX, startY, currentX, currentY, xPoly, yPoly);
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
        if (!(splitArray[0].equals("FILL") || splitArray[0].equals("LINE") || splitArray[0].equals("PEN") || splitArray[0].equals("PLOT") ||
                splitArray[0].equals("RECTANGLE") || splitArray[0].equals("ELLIPSE") || splitArray[0].equals("POLYGON"))) {
            throw new VecFileException("Invalid vec format: Wrong shapes");
        } else {
            if (splitArray[0].equals("FILL")) {
                if (!isColor(splitArray[1])) {
                    throw new VecFileException("Invalid vec format: Color is not valid.");
                }

                if (splitArray.length != 2) {
                    throw new VecFileException("Invalid format for FILL. Correct format: FILL [color].");
                }
            }
            if (splitArray[0].equals("PEN")) {
                if (!isColor(splitArray[1])) {
                    throw new VecFileException("Invalid vec format: Color is not valid.");
                }

                if (splitArray.length != 2) {
                    throw new VecFileException("Invalid format for PEN. Correct format: PEN [color].");
                }
            }
            if (splitArray[0].equals("PLOT")) {
                if (splitArray.length != 3) {
                    throw new VecFileException("Invalid vec format for PLOT. Correct format: PLOT [X] [Y].");
                }
                if (!isValidNumber((splitArray[1])) || !isValidNumber((splitArray[2]))){
                    throw new VecFileException("Invalid vec format: Coordinates have to be numeric and in the range [0, 1].");
                }
            }
            if (splitArray[0].equals("LINE")) {
                if (splitArray.length != 5) {
                    throw new VecFileException("Invalid vec format for LINE. Correct format: LINE [X0] [Y0] [X1] [Y1].");
                }
                if (!isValidNumber((splitArray[1])) || !isValidNumber((splitArray[2])) || !isValidNumber((splitArray[3])) || !isValidNumber((splitArray[4]))){
                    throw new VecFileException("Invalid vec format: Coordinates have to be numeric and in the range [0, 1].");
                }
            }
            if (splitArray[0].equals("RECTANGLE")) {
                if (splitArray.length != 5) {
                    throw new VecFileException("Invalid vec format for RECTANGLE. Correct format: RECTANGLE [X0] [Y0] [X1] [Y1].");
                }
                if (!isValidNumber((splitArray[1])) || !isValidNumber((splitArray[2])) || !isValidNumber((splitArray[3])) || !isValidNumber((splitArray[4]))){
                    throw new VecFileException("Invalid vec format: Coordinates have to be numeric and in the range [0, 1].");
                }
            }
            if (splitArray[0].equals("ELLIPSE")) {
                if (splitArray.length != 5) {
                    throw new VecFileException("Invalid vec format for ELLIPSE. Correct format: ELLIPSE [X0] [Y0] [X1] [Y1].");
                }
                if (!isValidNumber((splitArray[1])) || !isValidNumber((splitArray[2])) || !isValidNumber((splitArray[3])) || !isValidNumber((splitArray[4]))){
                    throw new VecFileException("Invalid vec format: Coordinates have to be numeric and in the range [0, 1].");
                }
            }
            if (splitArray[0].equals("POLYGON")) {
                if (splitArray.length % 2 != 1) { // polygon + even number of coordinates => odd
                    throw new VecFileException("Invalid vec format for POLYGON. Correct format: POLYGON [X0] [Y0] [X1] [Y1] ... X[2n] Y[2n].");
                }
                for (int i = 1; i < splitArray.length; i++) {
                    if (!isValidNumber((splitArray[i]))) {
                        throw new VecFileException("Invalid vec format: Coordinates have to be numeric and in the range [0, 1].");
                    }
                }
            }

        }
    }



    /**
     * Check whether the number is valid
     * @param strNum String
     * @return boolean
     */
    public static boolean isValidNumber(String strNum) {
        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }

        double num = Double.parseDouble(strNum);
        if (num >= 0 && num <= 1) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Check whether the string color input is color
     * @param color String
     * @return boolean
     */
    public static boolean isColor(String color) {
        try {
            Color.decode(color);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Method used to read draw each line of the input .vec file
     * @param line String
     */

    public void drawLineByLine(String line){

        String[] splitArray = line.split("\\s+");
        Color c = Color.BLACK;

        if (splitArray[0].equals("PEN")){
            penColor = Color.decode(splitArray[1]);
            outLines.push(line + "\n");
        }

        if (splitArray[0].equals("FILL") && !splitArray[1].equals("OFF")){
            outLines.push(line + "\n");
            fillColor = Color.decode(splitArray[1]);
            fill = true;
        }

        if (splitArray[0].equals("FILL") && splitArray[1].equals("OFF")){
            outLines.push(line + "\n");
            fillColor = transparent; /// should be none
        }

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

        int h = getHeight();
        int w = getWidth();

        if (splitArray[0].equals("RECTANGLE") || splitArray[0].equals("LINE") || splitArray[0].equals("ELLIPSE")){
            int x1 = (int) Math.round(Double.parseDouble(splitArray[1]) * h);
            int y1 = (int) Math.round(Double.parseDouble(splitArray[2]) * w);
            int x2 = (int) Math.round(Double.parseDouble(splitArray[3]) * h);
            int y2 = (int) Math.round(Double.parseDouble(splitArray[4]) * w);
            setCoordinatesAndDraw(x1, y1, x2, y2, penColor, fillColor);
        }

        //System.out.println("height" + Paint.squarePad.getHeight());
        //System.out.println("width" + Paint.squarePad.getWidth());


        if (splitArray[0].equals("PLOT")) {
            currentTool = ToolFactory.createTool(2);
            int x1 = (int) Math.round(Double.parseDouble(splitArray[1]) * h);
            int y1 = (int) Math.round(Double.parseDouble(splitArray[2]) * w);
            setCoordinatesAndDraw(x1, y1, x1, y1, penColor, fillColor);
        }
        if (splitArray[0].equals("POLYGON")) {
            currentTool = ToolFactory.createTool(5);
            if (!xPoly.isEmpty() || !yPoly.isEmpty()) {
                xPoly = new ArrayList<>();
                yPoly = new ArrayList<>();
            }
            for (int i = 1; i < splitArray.length; i++) {
                if (i % 2 == 1) {
                    int x = (int) Math.round(Double.parseDouble(splitArray[i]) * h);
                    xPoly.add(x);
                } else {
                    int y = (int) Math.round(Double.parseDouble(splitArray[i]) * w);
                    yPoly.add(y);
                }
            }
            setCoordinatesAndDraw(0, 0, 0, 0, penColor, fillColor);
        }
        //System.out.println(savedImagesStack);

        repaint();
    }

    /**
     * Method used to process all lines of input .vec file and draw on the screen image
     * @param scanner Scanner
     */
    public void drawFromFile(Scanner scanner){
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            try{
                checkLine(line);
                drawLineByLine(line);
            }
            catch(VecFileException e){
                System.out.println(e.getMessage());
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

        if(currentTool.toolType != ToolFactory.POLYGON_TOOL){
            saveToStack(image);
        }
        //System.out.println("Stack length " + savedImagesStack.size());



        oldX = startX = evt.getX();    // save mouse coordinates.
        oldY = startY = evt.getY();

        penColor = getCurrentColor();                 //get current color
        dragGraphics = (Graphics2D) image.getGraphics();  //convert Graphics
        dragGraphics.setColor(penColor);              //set color
        dragGraphics.setBackground(getBackground());
        isDrawing = true;                                 //start isDrawing
    }

    /**
     * to get output string to write to out put file. Used in Menu.java
     * @return stack of strings
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
     * @param e MouseEvent
     */
    public void mouseReleased(MouseEvent e)
    {
        if (!isDrawing)
            return;             //return if the user isn't isDrawing.
        isDrawing = false;        //set isDrawing to false
        currentX = e.getX();    //save mouse coordinates
        currentY = e.getY();

        if (currentTool.toolType != ToolFactory.CLEAR_TOOL)
        {
            repaintRectangle(startX, startY, oldX, oldY);
            if (currentX != startX && currentY != startY) {
                // Draw the shape only if both its height
                // and width are non-zero.
                drawGraphics(dragGraphics, currentTool, startX, startY, currentX, currentY, xPoly, yPoly);
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

            }
            else if (currentX == startX && currentY == startY && currentTool.toolType == ToolFactory.PLOT_TOOL) {
                drawGraphics(dragGraphics, currentTool, startX, startY, currentX, currentY, xPoly, yPoly);
                // Record what we've drawn
                outLines.push("PLOT" + " " + (double) startX / getWidth()  + " " + (double) startY / getHeight() + "\n");
                imageRecordStack.push("ELLIPSE" + " " + (double) startX / getWidth()  + " " + (double) startY / getHeight() + " "
                        + (double) currentX / getWidth()  + " " + (double) currentY / getHeight() + " " + "\n");

            }
        }

    }

    /**
     * Method called whenever the user moves the mouse while a mouse button is down
     * If the user is isDrawing a curve, draw a segment of the curve on the off-screen image, and repaint the part
     * and repaint the part of the panel that contains the new line segment.
     * Otherwise, just call repaint and let paintComponent() draw the shape on top of the picture in the off-screen image.
     * @param e MouseEvent
     */
    public void mouseDragged(MouseEvent e)
    {
        if (!isDrawing)
            return;  // returne if the user isn't isDrawing.

        currentX = e.getX();   // x-coordinate of mouse.
        currentY = e.getY();   // y=coordinate of mouse.
        //System.out.println(currentTool.toolType == ToolFactory.CLEAR_TOOL);

        if (currentTool.toolType == ToolFactory.CLEAR_TOOL)
        {

            drawGraphics(dragGraphics, ToolFactory.createTool(ToolFactory.CLEAR_TOOL), oldX, oldY, currentX, currentY, xPoly, yPoly); // A CURVE is drawn as a series of LINEs.
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

            //System.out.println(outLines.size());
            //System.out.println(imageRecordStack.size());
            //System.out.println(savedImagesStack.size());
        }
    }

    public void updateImageOnRequest(){
        saveToStack(image);
    }

    /**
     * Pop i last images from the stack
     * @param i Integer
     */
    public void popImagesFromStack(int i){
        for (int k = 0; k < i; k++){
            setImage(savedImagesStack.pop());
        }
        for (int k = 0; k < i - 1; k++){
            imageRecordStack.pop();
        }

        System.out.println("pop pop");

        // don't delete the line where we fill/pen.
        int count = 0;
        for (int k = outLines.size() -1; k >=0; k--){
            if(!outLines.get(k).split(" ")[0].equals("FILL") && !outLines.get(k).split(" ")[0].equals("PEN")){
                outLines.remove(k);
                count++;
                System.out.println(count);
            }
            if (count >= i - 1){
                break;
            }
        }

    }

    /**
     * Get the Undo stack
     * @return Stack of Image
     */
    public Stack<Image> getImageStack(){
        return savedImagesStack;
    }

    /**
     * Set the blank image
     */
    public void blankImage(){
        setImage(savedImagesStack.get(0));
    }

    /**
     * Set image from Undo stack with given parameter
     * @param i Integer
     */
    public void renderRequestImage(int i){
        setImage(savedImagesStack.get(i));
    }

    /**
     * Method used to set image of screen when using Undo Command
     * @param img Image
     */
    private void setImage(Image img) {
        graphics = (Graphics2D) img.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setPaint(Color.black);
        image = img;
        repaint();
    }

    private BufferedImage copyImage(Image img) {
        BufferedImage copyOfImage = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_RGB);
        Graphics g = copyOfImage.createGraphics();
        g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
        return copyOfImage;
    }

    /**
     * Save image to Undo stack
     * @param img Image
     */
    private void saveToStack(Image img) {
        savedImagesStack.push(copyImage(img));
        if (savedImagesStack.size() != 0){
            Menu.enableUndo();
        }
    }

    /**
     * Save image to HistoryStack
     * @param img Image
     */
    private void saveToHistoryStack(Image img) {
        historyImagesStack.push(copyImage(img));
    }

    /**
     * Get the size of Undo stack
     * @return Integer
     */
    public int getStackSize(){
        return savedImagesStack.size();
    }


    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mouseMoved(MouseEvent e) {}

}


/**
 * ImageStack class extends Stack for creating a history of all drawing operations
 * @param <E> Parameter
 */
class ImageStack<E> extends Stack<E> {

    /**
     * Default constructor
     */
    public ImageStack() {
        super();
    }

    /**
     * Method used to push object in the ImageStack
     * @param object Object
     * @return Object
     */
    @Override
    public Object push(Object object) {
        /*
        while (this.size() > maxSize) {
            this.remove(0);
        }*/
        return super.push((E) object);
    }
}
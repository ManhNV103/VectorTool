/**
 * JAVA DRAWING APP
 * Tran Quang Huy & Nguyen Van Manh
 */


package PaintTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;


public class Menu extends JMenuBar implements ActionListener {

    private JMenu menuFile;
    private JMenu menuEdit;
    private JMenu menuControl;
    private JMenu menuHelp;
    private JMenu menuAbout;

    private JMenuItem itemNew;
    private JMenuItem itemImport;
    private JMenuItem itemExport;
    private JMenuItem itemUndo;

    private final JFileChooser fc = new JFileChooser();

    public SquarePadDrawing pad2;




    public Menu(){
        super();
        this.createGUI();
        pad2 = new SquarePadDrawing();

    }

    /**
     * Create the Menu Bar GUI
     */

    public void createGUI(){
        //Create the menu bar.  Make it have a cyan background.
        setOpaque(true);
        setBackground(Color.cyan);


        // File menu
        menuFile = new JMenu("File");

        itemNew = new JMenuItem("New");
        menuFile.add(itemNew);
        itemNew.addActionListener(this);

        itemImport = new JMenuItem("Import");
        menuFile.add(itemImport);
        itemImport.addActionListener(this);

        itemExport = new JMenuItem("Export");
        menuFile.add(itemExport);
        itemExport.addActionListener(this);




        // Edit menu
        menuEdit = new JMenu("Edit");
        itemUndo = new JMenuItem("Undo");
        menuEdit.add(itemUndo);
        menuControl = new JMenu("Control");
        menuHelp = new JMenu("Help");
        menuAbout = new JMenu("About");

        add(menuFile);
        add(menuEdit);
        add(menuControl);
        add(menuHelp);
        add(menuAbout);

    }

    public void paintComponent(Graphics2D g)
    {
        super.paintComponent(g);

    }



    @Override
    public void actionPerformed(ActionEvent e) {
        Component source = (Component) e.getSource();
        if (source == itemImport){  // import file
            int returnVal = fc.showOpenDialog(Menu.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                String filename = file.getAbsolutePath();
                // Read vec file.
                try{
                    Scanner scanner = new Scanner(new File(filename));
                    while (scanner.hasNextLine()) {
                        String[] splitArray = scanner.nextLine().split("\\s+");

                        if (splitArray[0].equals("RECTANGLE")){
                            Paint.squarePad.currentTool = ToolFactory.createTool(3);
                        }
                        if (splitArray[0].equals("PLOT")){
                            Paint.squarePad.currentTool = ToolFactory.createTool(4);
                        }
                        if (splitArray[0].equals("LINE")){
                            Paint.squarePad.currentTool = ToolFactory.createTool(5);
                        }
                        if (splitArray[0].equals("ELLIPSE")){
                            Paint.squarePad.currentTool = ToolFactory.createTool(6);
                        }
                        if (splitArray[0].equals("POLYGON")){
                            Paint.squarePad.currentTool = ToolFactory.createTool(7);
                        }
                        //System.out.println("height" + Paint.squarePad.getHeight());
                        //System.out.println("width" + Paint.squarePad.getWidth());
                        int h = Paint.squarePad.getHeight();
                        int w = Paint.squarePad.getWidth();

                        if (splitArray[0].equals("PLOT")){
                            int x1 = (int) Math.round(Double.parseDouble(splitArray[1]) * h);
                            int y1 = (int) Math.round(Double.parseDouble(splitArray[2]) * w);
                            Paint.squarePad.setCoordinatesAndDraw(x1, y1, x1, y1);
                        }
                        else {
                            int x1 = (int) Math.round(Double.parseDouble(splitArray[1]) * h);
                            int y1 = (int) Math.round(Double.parseDouble(splitArray[2]) * w);
                            int x2 = (int) Math.round(Double.parseDouble(splitArray[3]) * h);
                            int y2 = (int) Math.round(Double.parseDouble(splitArray[4]) * w);
                            Paint.squarePad.setCoordinatesAndDraw(x1, y1, x2, y2);

                        }

                        repaint();

                        //System.out.println(splitArray[0]);
                    }
                    scanner.close();
                }
                catch (FileNotFoundException exception) {
                    exception.printStackTrace();
                }

                System.out.println(filename);
            }
            else if (returnVal == JFileChooser.CANCEL_OPTION) {
            }

        }
        if (source == itemExport){ // export file
            String outfile = Paint.squarePad.getOutFile();
            JFileChooser fileChooser = new JFileChooser();
            int retval = fileChooser.showSaveDialog(this);
            if (retval == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (file == null) {
                    return;
                }

                // Make sure file extension is ".vec"
                int index = file.getName().indexOf(".");
                String fileNameNoExtension = file.getName().substring(0, index);
                String fileName = file.getParentFile() + "/" + fileNameNoExtension + ".vec";
                System.out.println(fileName);

                try {
                    Writer out = new BufferedWriter(new FileWriter(fileName));
                    try {
                        out.write(outfile);
                    } finally {
                        out.close();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            System.out.println(outfile);




        }
    }
/*
    private class menuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Component source = (Component) e.getSource();
            if (source == itemImport){

                int returnVal = fc.showOpenDialog(Menu.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    String filename = file.getAbsolutePath();
                    //Paint.squarePad.drawGraphics();
                    // try to read vec file.
                    try{
                        Scanner scanner = new Scanner(new File(filename));
                        while (scanner.hasNextLine()) {
                            System.out.println(scanner.nextLine());
                        }
                        scanner.close();
                    }
                    catch (FileNotFoundException exception) {
                        exception.printStackTrace();
                    }

                    System.out.println(filename);
                }
                else if (returnVal == JFileChooser.CANCEL_OPTION) {
                }
            }



        }
    }


*/




}

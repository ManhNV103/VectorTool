/**
 * JAVA DRAWING APP
 * Tran Quang Huy & Nguyen Van Manh
 */


package PaintTool;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;
import java.util.Stack;

import static java.awt.event.InputEvent.CTRL_DOWN_MASK;

/**
 * A class that extends the JMenuBar and adds necessary menu items
 */
public class Menu extends JMenuBar implements ActionListener {

    private JMenu menuFile;
    private JMenu menuEdit;
    private JMenu menuControl;
    private JMenu menuHelp;
    private JMenu menuAbout;

    private JMenuItem itemNew;
    private JMenuItem itemImport;
    private JMenuItem itemExport;
    private static JMenuItem itemUndo;
    private static JMenuItem itemHistory;

    private final JFileChooser fc = new JFileChooser();

    public SquarePadDrawing pad2;


    public static void enableUndo(){
        itemUndo.setEnabled(true);
    }

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

        itemExport = new JMenuItem("Save to File");
        menuFile.add(itemExport);
        itemExport.addActionListener(this);


        // Edit menu
        menuEdit = new JMenu("Edit");

        itemUndo = new JMenuItem("Undo");
        itemUndo.setEnabled(false);
        //itemUndo.setAccelerator(KeyStroke.getKeyStroke('Z', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        itemUndo.setAccelerator(KeyStroke.getKeyStroke('Z', CTRL_DOWN_MASK));
        menuEdit.add(itemUndo);
        itemUndo.addActionListener(this);

        // Redo - prepare for history
        itemHistory = new JMenu("History");

        menuEdit.add(itemHistory);
        itemHistory.addActionListener(this);

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

    /**
     * Checking whether the input is vec file
     * @param filename
     * @return
     */
    public boolean isVecFile(String filename){
        if (filename.endsWith(".vec")){
            return true;
        }
        return false;
    }


    /**
     * Method used to navigate and filter the input files, export to new output file and implement Undo command
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Component source = (Component) e.getSource();
        if (source == itemImport){  // import file
            FileNameExtensionFilter filter = new FileNameExtensionFilter(".VEC file", "vec");
            fc.addChoosableFileFilter(filter);
            fc.setFileFilter(filter);
            int returnVal = fc.showOpenDialog(Menu.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //String name = file.getName();
                String fileName = file.getAbsolutePath();
                if (isVecFile(fileName)){
                    // filter .vec file only.
                    // Read vec file.
                    try{
                        Scanner scanner = new Scanner(new File(fileName));
                        Paint.squarePad.drawFromFile(scanner);
                        scanner.close();
                    }
                    catch (FileNotFoundException exception) {
                        exception.printStackTrace();
                    }
                }

                //System.out.println(filename);
            }
            else if (returnVal == JFileChooser.CANCEL_OPTION) {
            }

        }
        if (source == itemExport){ // export file
            Stack<String> outLines = Paint.squarePad.getOutLines();
            String outfile = "";
            for (String line: outLines){
                outfile += line;
            }

            JFileChooser fileChooser = new JFileChooser();
            int retval = fileChooser.showSaveDialog(this);
            if (retval == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (file == null) {
                    return;
                }

                // Make sure file extension is ".vec"
                String fileName;
                int index = file.getName().indexOf(".");
                if (index == -1){
                    fileName = file.getParentFile() + "/" + file.getName() + ".vec";
                }
                else {
                    fileName = file.getParentFile() + "/" + file.getName();
                    /*
                    String fileNameNoExtension = file.getName().substring(0, index);
                    fileName = file.getParentFile() + "/" + fileNameNoExtension + ".vec";
                    System.out.println(fileName);*/
                }

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
            //System.out.println(outfile);

        }
        if (source == itemUndo){  // import file
            Paint.squarePad.undo();
            if (Paint.squarePad.getStackSize() == 0){
                itemUndo.setEnabled(false);
            }
        }
        if (source == itemHistory){


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

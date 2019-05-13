/**
 * JAVA DRAWING APP
 * Tran Quang Huy & Nguyen Van Manh
 */


package PaintTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Menu extends JMenuBar {

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




    public Menu(){
        super();
        this.createGUI();
    }

    public void createGUI(){
        //Create the menu bar.  Make it have a cyan background.
        setOpaque(true);
        setBackground(Color.cyan);




        // File menu
        menuFile = new JMenu("File");

        itemNew = new JMenuItem("New");
        menuFile.add(itemNew);
        itemNew.addActionListener(new menuListener());

        itemImport = new JMenuItem("Import");
        menuFile.add(itemImport);
        itemImport.addActionListener(new menuListener());

        itemExport = new JMenuItem("Export");
        menuFile.add(itemExport);
        itemExport.addActionListener(new menuListener());




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

    private class menuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Component source = (Component) e.getSource();
            if (source == itemImport){

                int returnVal = fc.showOpenDialog(Menu.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    String filename = file.getAbsolutePath();
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







}

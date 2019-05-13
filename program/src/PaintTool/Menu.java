/**
 * JAVA DRAWING APP
 * Tran Quang Huy & Nguyen Van Manh
 */


package PaintTool;

import javax.swing.*;
import java.awt.*;

public class Menu extends JMenuBar {

    public Menu(){
        super();
        this.createGUI();
    }

    public void createGUI(){
        //Create the menu bar.  Make it have a cyan background.
        setOpaque(true);
        setBackground(Color.cyan);
        JMenu menuItem_1 = new JMenu("File");
        JMenu menuItem_2 = new JMenu("Export");
        JMenu menuItem_3 = new JMenu("Control");
        JMenu menuItem_4 = new JMenu("Help");
        JMenu menuItem_5 = new JMenu("About");
        add(menuItem_1);
        add(menuItem_2);
        add(menuItem_3);
        add(menuItem_4);
        add(menuItem_5);

    }




}

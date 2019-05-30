package PaintTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Stack;

public class DrawingHistory extends JPanel implements ActionListener {

    private JLabel history;
    private JButton updateBtn1;
    private JButton blankBtn;
    private JPanel historyPanel;
    private ArrayList<JButton> btnLists = new ArrayList<>();
    private Stack<Image> imageStacks = new Stack<>();

    public DrawingHistory(){
        super();
        this.createGUI();
    }



    public void createGUI(){

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        history = new JLabel("Drawing history:           ");
        this.add(history, BorderLayout.NORTH);

        updateBtn1 = new JButton("Update History");
        this.add(updateBtn1);
        updateBtn1.addMouseListener(new MyMouseListener());
        updateBtn1.addActionListener(this);

        blankBtn = new JButton("New drawing");
        blankBtn.addActionListener(this);
        this.add(blankBtn);
        blankBtn.setVisible(false);

        historyPanel = new JPanel();
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
        this.add(historyPanel);




    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Component source = (Component) e.getSource();
        if (source == updateBtn1){
            //imageStacks.clear();
            imageStacks = Paint.squarePad.getImageStack();
            System.out.println("Stack length : " + imageStacks.size());
            //blankBtn.setVisible(true);
            historyPanel.removeAll();
            btnLists.clear();
            revalidate();
            repaint();

            for(int i = 0; i < imageStacks.size(); i++){
                JButton temp = new JButton("State" + 1);
                temp.addActionListener(this);
                temp.addMouseListener(new MyMouseListener());
                btnLists.add(temp);
            }
            for(int i = 0; i < btnLists.size(); i++){
                historyPanel.add(btnLists.get(i));
                revalidate();
                repaint();
            }
        }
        if (source == blankBtn){
            if (Paint.squarePad.getImageStack().size() > 0){
                Paint.squarePad.blankImage();
            }

        }
        for(int i = 0; i < btnLists.size(); i++) {
            if (source == btnLists.get(i)){
                Paint.squarePad.renderRequestImage(i);
            }
        }

    }

    class MyMouseListener extends MouseAdapter {
        public void mouseClicked(MouseEvent evt) {
            if (SwingUtilities.isRightMouseButton(evt) && evt.getClickCount() == 1) {
                int i = btnLists.indexOf(evt.getSource());
                Paint.squarePad.renderRequestImage(i);
                Paint.squarePad.popImagesFromStack(btnLists.size()-i);
                historyPanel.removeAll();
                revalidate();
                repaint();
                btnLists.clear();
            }
        }


    }

}


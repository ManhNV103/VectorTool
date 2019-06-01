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
    private JButton updateBtn;
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
        history = new JLabel(" DRAWING HISTORY:     ");
        this.add(history, BorderLayout.NORTH);

        updateBtn = new JButton("Show History");
        this.add(updateBtn);
        updateBtn.addActionListener(this);

        blankBtn = new JButton("New drawing");
        blankBtn.addActionListener(this);
        this.add(blankBtn);
        blankBtn.setVisible(false);

        historyPanel = new JPanel();
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
        //historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
        this.add(historyPanel);




    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Component source = (Component) e.getSource();
        if (source == updateBtn){
            //imageStacks.clear();
            updateBtn.setEnabled(false);
            Paint.squarePad.updateImageOnRequest();
            Paint.squarePad.setEnabled(false);
            imageStacks = Paint.squarePad.getImageStack();
            System.out.println("Stack length : " + imageStacks.size());
            //blankBtn.setVisible(true);
            historyPanel.removeAll();
            btnLists.clear();
            revalidate();
            repaint();

            //
            Stack<String> stack = Paint.squarePad.getImageRecordStack();

            if(stack.size() == imageStacks.size()){
                historyPanel.add(new JLabel("   Double click a"));
                historyPanel.add(new JLabel("   button for the"));
                historyPanel.add(new JLabel("   corresponding "));
                historyPanel.add(new JLabel("   state before"));
                historyPanel.add(new JLabel("   continuing drawing."));
                historyPanel.add(new JLabel("    "));
                for(int i = 0; i < imageStacks.size(); i++){
                    String label;
                    if (stack.size() >= i){
                        label = stack.get(i).split(" ")[0];
                        JButton temp = new JButton(label);
                        temp.addActionListener(this);
                        temp.addMouseListener(new MyMouseListener());
                        btnLists.add(temp);
                    }
                }

            }
            else{
                updateBtn.setEnabled(true);
                historyPanel.add(new JLabel("   Problem with image"));
                historyPanel.add(new JLabel("   stacks: Number of"));
                historyPanel.add(new JLabel("   actions > number"));
                historyPanel.add(new JLabel("   of records"));
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
            /*
            if (SwingUtilities.isRightMouseButton(evt) && evt.getClickCount() == 1) {
                int i = btnLists.indexOf(evt.getSource());
                Paint.squarePad.renderRequestImage(i);
                Paint.squarePad.popImagesFromStack(btnLists.size()-i);
                historyPanel.removeAll();
                revalidate();
                repaint();
                btnLists.clear();
            }*/
            if (evt.getClickCount() == 2) {
                updateBtn.setEnabled(true);
                System.out.println("double-click");
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


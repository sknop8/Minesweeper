import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public abstract class Box {

    private Boolean isClicked;
    private Boolean isFlagged;
    final private GameCourt gc;
    final private int xPos;
    final private int yPos;
    
    public Box(GameCourt gc, int x, int y){
        isClicked = false;
        isFlagged = false;
        this.gc = gc;
        xPos = x;
        yPos = y;
    }
    public int getX(){
        return xPos;
    }
    public int getY(){
        return yPos;
    }
    public GameCourt getGC(){
        return gc;
    }
    public Boolean isClicked(){
        return isClicked;
    }
    public void click(){
        isClicked = !isClicked;
        if(!isFlagged) gc.clickCount(isClicked);
    }
    
    public void flag(){
        isFlagged = !isFlagged;
        gc.decFlag(isFlagged);
    }
    
    public Boolean isFlagged(){
        return isFlagged;
    }
    
    public JButton asJButton(){
        JButton button = new JButton();
        
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                click();
                isClicked = true;
                gc.updateField(); 
                    
            }
        });
        
        button.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
               if (e.getButton() == MouseEvent.BUTTON3) {
                  flag();
                  gc.updateField(); 
               }
            }
        });
        
        return button;
    }
    
    public abstract JLabel asJLabel();
    
    
    
}

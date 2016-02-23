import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.Border;

public class RegBox extends Box {
    private int bombNum;

    public RegBox(GameCourt gc, int x, int y){
        super(gc, x, y);
        bombNum = 0;
    }
    
    public void setBombNum(int n){
        bombNum = n;
    }
    
    public int getBombNum(){
        return bombNum;
    }
    
    public JButton asJButton(){
        JButton button = super.asJButton();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(bombNum == 0)
                    getGC().clearField(getX(), getY()); 
            }
        });
        return button;
    }
    
    public JLabel asJLabel(){
        String s = String.valueOf(bombNum);
        if(bombNum == 0) s = "";
        JLabel label =  new JLabel(s, JLabel.CENTER);
        label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));     
        return label;
    }
    
}

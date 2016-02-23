import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

public class BombBox extends Box{
    
    public BombBox(GameCourt gc, int x, int y){
        super(gc, x, y);
    }
    
    public JButton asJButton(){
        JButton button = super.asJButton();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getGC().setOffBombs();
            }
        });
        return button;
    }

    public JLabel asJLabel(){
        return new JLabel("\u25CF", JLabel.CENTER); 
    }
}

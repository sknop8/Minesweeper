/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 * 
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {
	
	private Box[][] field; //the grid
	private BombBox[] bombList;
	public boolean playing = false; // whether the game is running
	private JLabel status; //JLabel showing status of game(playing,win,loss)
	private JLabel bombsLeft; // JLabel showing bombs left to flag
	private JLabel timekeep; //JLabel showing current time of the game 
	private int timeCount; // Current time
	private int flagCount; //number of bombs to flag (shown on bombsLeft)
	private int clickCount; //how many boxes are either clicked or flagged
	private FileWriter writer;
	private String level;

	// Game constants
	public int BOARD_HEIGHT;
	public int BOARD_WIDTH;
	public int BOMB_NUMBER;
	
	
	// Update interval for timer, in milliseconds
	public static final int INTERVAL = 35;

	public GameCourt(int boardX, int boardY, int bombNum, 
	        JLabel status, JLabel bombsLeft, JLabel timekeep) {
		setBorder(BorderFactory.createLineBorder(Color.BLACK));

		// The timer is an object which triggers an action periodically
		// with the given INTERVAL. One registers an ActionListener with
		// this timer, whose actionPerformed() method will be called
		// each time the timer triggers. We define a helper method
		// called tick() that actually does everything that should
		// be done in a single timestep.
		Timer timer = new Timer(INTERVAL, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tick();
			}
		});
		timer.start();

		// Enable keyboard focus on the court area.
		// When this component has the keyboard focus, key
		// events will be handled by its key listener.
		setFocusable(true);
		
		BOARD_WIDTH = boardX;
		BOARD_HEIGHT = boardY;
		BOMB_NUMBER = bombNum;

		newField(boardX,boardY,bombNum);

		this.bombsLeft = bombsLeft;
        this.timekeep = timekeep;
        this.status = status;

		updateField();
		

	}
	
	//creates a new grid with the given dimensions and number of bombs
	private void newField(int boardX, int boardY, int bombNum){
	    BOARD_WIDTH = boardX;
	    BOARD_HEIGHT = boardY;
	    BOMB_NUMBER = bombNum;
	    level = getLevel(BOMB_NUMBER);
	    
	    System.out.println(BOMB_NUMBER);
	    System.out.println(level);
	    
	    GridLayout gridLayout = new GridLayout();
        this.setLayout(gridLayout);
        gridLayout.setColumns(BOARD_HEIGHT);
        gridLayout.setRows(BOARD_WIDTH);
        
        field = new Box[BOARD_WIDTH][BOARD_HEIGHT];
        for(int i = 0; i < BOARD_WIDTH; i++){
            for(int j = 0; j < BOARD_HEIGHT; j++){
                field[i][j] = new RegBox(this, i, j);
            }
        }
        
        bombList = new BombBox[BOMB_NUMBER];

        timeCount = 0;
        flagCount = BOMB_NUMBER;
        clickCount = BOARD_WIDTH*BOARD_HEIGHT - BOMB_NUMBER;
        this.setPreferredSize(new Dimension(BOARD_WIDTH*30,BOARD_HEIGHT*30));
        this.revalidate();
        
        writer=null;
        try {
            writer = new FileWriter("highscores.txt", true);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
 
	}
	//returns "e" "m" or "h" (easy, medium, or hard) given the number of bombs
	private String getLevel(int bombNum){
	    String s;
	    if(bombNum == 10)
	        s = "e";
	    else if(bombNum == 40)
	        s = "m";
	    else
	        s = "h";
	    return s;
	}

	/**
	 * (Re-)set the game to its initial state.
	 */
	public void reset(int boardX, int boardY, int bombNum) {
	    status.setText("");
	    playing = true;
	    newField(boardX, boardY, bombNum);
        this.revalidate();
	    
	    for(int i = 0; i < BOARD_WIDTH; i++){
            for(int j = 0; j < BOARD_HEIGHT; j++){
                Box box = field[i][j];
                if(box instanceof BombBox)
                    field[i][j] = new RegBox(this, i, j);
                if(box.isClicked()) box.click();
                if(box.isFlagged()) box.flag();
            }
        }
	    
	    //place the bombs at random
		int count = BOMB_NUMBER;
		while(count > 0){
		    int i = (int)(Math.random()*BOARD_WIDTH);
		    int j = (int)(Math.random()*BOARD_HEIGHT);
		    Box b = field[i][j];
		    if(b instanceof RegBox){
		        field[i][j] = new BombBox(this, i, j);
		        count--;
		        bombList[count] = (BombBox) field[i][j];
		    }
		}
		
		//set all the box's bomb numbers
		for(int i = 0; i < BOARD_WIDTH; i++){
		    for(int j = 0; j < BOARD_HEIGHT; j++){
		        Box box = field[i][j];
		        if(box instanceof RegBox){
    		        int bombs = 0;
    		        for(int x = -1; x < 2; x++){
    		            for(int y = -1; y < 2; y++){
    		                int a = i + x;
    		                int b = j + y;
    		                if(isValidLocation(a, b))
    		                    if(field[a][b] instanceof BombBox)
    		                        bombs++;
    		            }
    		        }
    		        ((RegBox) box).setBombNum(bombs);
		        }
		    }
		}	
		playing = true;
		bombsLeft.setText("Bombs left: " + String.valueOf(flagCount) + " |"); 
		timeCount = 0;
		
		updateField();
		requestFocusInWindow();
	}
	
	//Returns true if location is on the field
	private Boolean isValidLocation(int i, int j){
	    if(i < 0 || j < 0 || i >= BOARD_WIDTH || j >= BOARD_HEIGHT)
	        return false;
	    else return true;
	}
	
	//Called when a user clicks on an "empty" (next to 0 bombs) box. It will 
	//clear all the empty boxes adjacent to the clicked box, all the way until 
	//it finds nonempty boxes
	public void clearField(int i, int j){
	    for(int x = -1; x < 2; x++)
	        for(int y = -1; y < 2; y++){
	            int a = i + x;
	            int b = j + y;
	            if(isValidLocation(a, b)){
	                Box box = field[a][b];
	                if(!box.isClicked() && !box.isFlagged()){
	                    box.click();
	                    if(((RegBox) box).getBombNum() == 0)
	                        clearField(a,b);
	                }
	            }    
	        }
	}
	
	//when a bomb is clicked, all the bombs are set off!
	public void setOffBombs(){
	    for(Box b : bombList){
	        if(!b.isClicked()) b.click();
	    }
	    updateField();
	}
	
	public void updateField(){
	    this.removeAll();
	    for(int i = 0; i < BOARD_WIDTH; i++){
	       for(int j = 0; j < BOARD_HEIGHT; j++){
        	    Box newBox = field[i][j];
        	    if(newBox.isClicked()){
            	    if(newBox instanceof RegBox){ 
            	        this.add(newBox.asJLabel());
            	    }else{
            	        this.add(newBox.asJLabel());
                        status.setText("| You lose!");
            	    }
        	    }
        	    else{
        	        String s = "";
        	        if(newBox.isFlagged()) 
        	            s = "\u25B3"; //black triangle
        	        JButton button = newBox.asJButton();
        	        button.setText(s);
        	        button.setMargin(new Insets(0,0,0,0));
        	        this.add(button);
        	    }
	       }
	    } 
	}

	/**
	 * This method is called every time the timer defined in the constructor
	 * triggers.
	 */
	void tick() {
		if (playing) {
		    bombsLeft.setText("Bombs left: " + String.valueOf(flagCount) + " |"); 
			timeCount++;
			timekeep.setText("Time: " + timeCount/30 + " s");
			if((clickCount+flagCount) == 0){
			    status.setText("| You win!");
			    String name = "";
                while(name.equals("") || name.trim().length()==0){
                    name = (String)JOptionPane.showInputDialog(
                            this,
                            "You won with a time of "+timeCount/30+" seconds!\n"
                            + "Please enter your name:\n",
                            "Customized Dialog",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            "");
                }
                try {
                    writer.write(level + name + " " + timeCount+" \n");
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
			    playing = false;
			}
			if(status.getText()=="| You lose!"){
			    JOptionPane.showMessageDialog(this, "You lose!"
			            + " Click on a level to try again!");
			    playing = false;
			    try {
                    //writer.write(level+"name" + " " + timeCount+ " \n");
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
			}

			repaint();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	//@Override
	//public Dimension getPreferredSize() {
	//	return new Dimension(BOARD_HEIGHT*30, BOARD_WIDTH*30);
	//}
	public void decFlag(Boolean b){
	    if(b) flagCount--;
	    else flagCount++;
	}
	public void clickCount(Boolean b){
	    if(b) clickCount--;   
	}
}

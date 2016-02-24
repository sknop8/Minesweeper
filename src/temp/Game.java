/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
	public void run() {
		// NOTE : recall that the 'final' keyword notes inmutability
		// even for local variables.

		// Top-level frame in which game components live
		// Be sure to change "TOP LEVEL FRAME" to the name of your game
		final JFrame frame = new JFrame("MINESWAGGER");
		frame.setLocation(300, 200);

		// Status panel
		final JPanel status_panel = new JPanel();
		frame.add(status_panel, BorderLayout.SOUTH);
		JLabel bombsLeft = new JLabel();
		status_panel.add(bombsLeft);

		JLabel timekeep = new JLabel("Time: 0 s");
		status_panel.add(timekeep);
		
		final JLabel status = new JLabel("");
        status_panel.add(status);
        
        final JButton instr = new JButton("Instructions");
        instr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BufferedReader reader = null;
                String s="";
                try {
                    reader = new BufferedReader(new FileReader("instructions.txt"));
                    
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                try{
                    while(reader.ready()){
                        s += reader.readLine() + " \n";
                    }
                }catch(IOException e2){
                    e2.printStackTrace();
                }
                
                JOptionPane.showMessageDialog(frame, s);
            }
        });
        
        status_panel.add(instr);
		
		  // Main playing area
        final GameCourt court = new GameCourt(10,10,10,status,bombsLeft,timekeep);
        frame.add(court, BorderLayout.CENTER);
		
		// Levels buttons
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);
		
       
	
		final JButton easygame = new JButton("Easy");
		easygame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				court.reset(9,9,10);
				frame.setSize(350,415);
			}
		});
		control_panel.add(easygame);
		
		final JButton medgame = new JButton("Medium");
        medgame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.reset(16, 16, 40);
                frame.setSize(400,480);
            }
        });
        control_panel.add(medgame);
        
        final JButton hardgame = new JButton("Hard");
        hardgame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.reset(16, 30, 99);
                frame.setSize(700,480);
            }
        });
        control_panel.add(hardgame);
        
        final JButton highscores = new JButton("Highscores");
        highscores.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //ArrayList<String> scoreBoard = new ArrayList<String>();
                //ArrayList<Integer> highscores = new ArrayList<Integer>();
                TreeMap<Integer, String> easyMap =
                        new TreeMap<Integer, String> ();
                TreeMap<Integer, String> medMap =
                        new TreeMap<Integer, String> ();
                TreeMap<Integer, String> hardMap =
                        new TreeMap<Integer, String> ();
                ArrayList<TreeMap<Integer, String>> scoreMapList = 
                        new ArrayList<TreeMap<Integer, String>>();
                scoreMapList.add(0, easyMap);
                scoreMapList.add(1, medMap);
                scoreMapList.add(2, hardMap);
                BufferedReader reader = null;
                String contentsE = "Easy Level: \n";
                String contentsM = "Medium Level: \n";
                String contentsH = "Hard Level: \n";
                try {
                    reader = new BufferedReader(new FileReader("highscores.txt"));
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();} 
                
                try{
                    while(reader.ready()){
                        scoreMapList = inputScores(scoreMapList, reader.readLine());
                    }
                }catch (IOException e1) {
                    e1.printStackTrace();
                }   
                
                int c = 1;
                for (Entry<Integer, String> entry : scoreMapList.get(0).entrySet()) {
                    contentsE += "#"+ c +": "+ entry.getValue() + "-" 
                            + (entry.getKey()/30) + " seconds \n";
                    c++;
                    if(c > 5) break;
                }
                c = 1;
                for (Entry<Integer, String> entry : scoreMapList.get(1).entrySet()) {
                    contentsM += "#"+ c +": "+ entry.getValue() + "-" 
                            + (entry.getKey()/30) + " seconds \n";
                    c++;
                    if(c > 5) break;
                }
                
                c = 1;
                for (Entry<Integer, String> entry : scoreMapList.get(2).entrySet()) {
                    contentsH += "#"+ c +": "+ entry.getValue() + "-" 
                            + (entry.getKey()/30) + " seconds \n";
                    c++;
                    if(c > 5) break;
                }
                
                String scoreBoard = "Highscores:\n" + contentsE + "\n"
                        + contentsM + "\n" + contentsH;
           
                JOptionPane.showMessageDialog(frame,scoreBoard);
            }
            private ArrayList<TreeMap<Integer, String>> inputScores(
                    ArrayList<TreeMap<Integer, String>> scoreMapList, 
                    String line){
                if(line == "") return scoreMapList;
                int i = line.length()-2;
                while(i >= 0 && Character.isDigit(line.charAt(i))){
                    i--;
                }
                int score = Integer.parseInt(line.substring(i+1, line.length()-1));       
                String name = line.substring(1, i);
                if(line.charAt(0)=='e')
                    scoreMapList.get(0).put(score, name);
                else if(line.charAt(0)=='m')
                    scoreMapList.get(1).put(score, name);
                else if(line.charAt(0)=='h')
                    scoreMapList.get(2).put(score, name);
                return scoreMapList;
            }
        });
        control_panel.add(highscores);
        
		// Put the frame on the screen
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);	

		// Start game
		court.reset(9,9,10);
	}


	/*
	 * Main method run to start and run the game Initializes the GUI elements
	 * specified in Game and runs it IMPORTANT: Do NOT delete! You MUST include
	 * this in the final submission of your game.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Game());
	}
}

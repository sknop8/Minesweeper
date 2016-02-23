=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: sknop
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. You may copy and paste from your proposal
  document if you did not change the features you are implementing.

  1. 2D Array
  	
  	The field in this game will be represented by a 2D Array. This makes sense 
	since the Minesweeper field is a grid. Also, the location of each element 
	in the grid is important, so a 2D Array is perfect for this since it is 
	indexed. This will allow for an easy way to find the Box's adjacent to any
	Box.
	
	The 2D Array is an appropriate data structure because elements in the board
	are arranged in a rectangular grid and need to be indexed, and once the 
	board size is set, it will never be resized.

  2. I/O
  
  	After each run of the game, the time (from Game) and the user's name (from 
	user input) will be written to a file and then displayed in its respective
	level category. 
  	
  	This will require enabling persistent state between runs of the game. Scores
	and names written to the file will need to be searched and when read back,
	ordered. So, it makes sense to use I/O. This is non-trivial implementation
	because it involves storing of data in a file over multiple game runs,
	and search through the file to read back high scores according to their 
	level, and sorted in ascending order by time score.
  	
  	
  3. Inheritance/Subtyping
  
 	I used an inheritance hierarchy to model the different type of boxes 
	there can be on the grid. I will have an abstract class Box, with subclasses
	BombBox and RegBox.
	
	It makes sense to have subclasses because there are some elements of regular
	boxes that wouldn't make sense to have for bombs, such as the number of 
	bombs it is next to. Yet, BombBox's and RegBox's share similar display 
	properties such as they can both be either flagged or clicked. But, once 
	clicked they do very different things and have different displays (RegBox's
	display a number and may or may not cause other Box's to change, while 
	BombBox's display a black circle and end the game). So, it makes sense to 
	use inheritance and subtyping to implement different types of boxes, and it
	is non-trivial since are some properties BombBox's and RegBox's differ on,
	yet they are both Box's and do share some other properties.
	
	
  4. Recursion
  
  	I used recursion in my clearField() function in the GameCourt class. This
  	is called when a user clicks on an "empty" box that is next to no bombs,
  	and technically has a number 0. Clicking this empty box should in turn 
  	open all the empty boxes around the clicked one until it comes to
  	adjacent boxes that are adjacent to bombs and thus have a number. When the 
  	given box is empty and it has a neighbor box that is also empty, it calls 
  	the clearField() on this empty neighbor box as well. This process continues
  	until a non-empty box is found in all directions. 
  	
  	This recursive algorithm lends itself well to this feature in Minesweeper,
  	as a kind of "chain-reaction" occurs when a user clicks an empty box, and it 
  	can be any size or shape according to the randomly assigned bombs on the 
  	field. This application is non-trivial and makes sense since it must be 
  	called only when an empty box is clicked, and it has to search all the boxes
  	around the given box for emptiness as well and act accordingly. This is
  	the most efficient way to find this area spread of adjacent empty boxes. 


=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

	The Game class creates the buttons that dictate what level a player is in,
	the highscore button, and the instructions button. It also displays the 
	running time and the number of bombs left to flag (as determined by 
	the GameCourt class). It organizes data read from the highscore.txt file 
	(which is written into by GameCourt when a user wins), and displays it when 
	the highscore button is clicked. The actual game is played in the GameCourt 
	class. This class holds a 2D array that represents the field, and other game 
	constants such as the field size and the number of bombs. These constants 
	can be modified by the level buttons in Game. GameCourt's grid contains Box 
	objects, which can be of type BombBox or RegBox. Methods in the GameCourt 
	class alter this 2D array according to mouseclick events. The Box class is 
	abstract and gives a way to use this Box as either a JButton or a JPanel, 
	depending on whether it is clicked or not (as determined by GameCourt). 
	BombBox and RegBox extend Box. Box also has methods for when it is clicked 
	or flagged. BombBox represents a mine, and when clicked will call a function 
	in GameCourt that triggers all the bombs to "explode" and ends the game. 
	RegBox holds how many bombs it is next to, and will accordingly call the 
	clearField() method in GameCourt if it is an "empty" box.

- Revisit your proposal document. What components of your plan did you end up
  keeping? What did you have to change? Why?
	
	I kept the I/O component (highscores, user input when a game is won, and 
	importing instructions), inheritance/subtyping (Box, BombBox, RegBox), and 
	the 2D array (representation of the field). I changed testable component
	to recursion, since recursion was necessary for a good implementation of my
	clearField() method in GameCourt. 

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  
  Representing the field as a 2D array of Box's that were JButtons that turn 
  into JPanels when clicked was difficult at first, because I had to balance 
  where all the work was done between the GameCourt an the Box objects 
  themselves. Eventually, I made asJButton() and asJPanel() in Box that returned 
  a JButton or JPanel version of itself. 


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

	My design has a good separation of functionality, since different features
	are distributed over the Game class, the GameCourt class, and the Box 
	classes. Game only really deals with what is being displayed, while 
	GameCourt has the actual Minesweeper infrastructure implemented, and Box's
	hold their own information about themselves. A Box's state, such as if it 
	is clicked or flagged, cannot be altered directly by any other class.
	Likewise, GameCourt's level and time state cannot be altered directly, 
	and must be set and started by passing in values to the constructor or 
	to one of its update methods. If given the chance, I would refactor the 
	way the grid is repainted every time the field updates in GameCourt, since
	as of now it runs through the array, checks if it is clicked or flagged or 
	not, and then displays/repaints each box accordingly. If there was a way
	to only repaint the changed Box's this would improve the speed of the 
	display in my game.

========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.

	https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
	
	https://docs.oracle.com/javase/tutorial/essential/io/file.html
	
	http://www.fileformat.info/info/unicode/block/geometric_shapes/list.htm

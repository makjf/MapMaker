// includes the relevant Java classes to help with implementing the program
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
// class that contains the methods and fields for a Map (width, height)
// implemented using a 2-D array with a number of specified rows and columns
// border created on the edges to represent walls
public class Map 
{
	private char map [] []; // map used to store all the objects
	private int width, height; // dimensions of the map
	private Image treasureimage, keyimage, monsterimage; // images for the Treasure, Key, and Monster within the map
	private String arrayString = ""; // string that contains the characters representing each block

	public Map (int rows, int cols, int blockwidth, int blockheight) // constructor creates map based on number of columns and rows with the block size of each cell
	{
		width = blockwidth;  height = blockheight; // values are implemented in private variables to restrict access
		map = new char [rows] [cols]; // defines the size of the map

		for (int row = 0 ; row < rows ; row++) // processes all rows of the Map
			for (int col = 0 ; col < cols ; col++) // processes all columns of the Map
			{
				if (row == 0 || row == rows-1 || col == 0 || col == cols-1) // creates a border at the edges of the map (first and last rows/columns)
					map [row] [col] = 'W'; // put up a wall at the specified locations
				else
					map [row] [col] = 'S'; // blank space, not " " because S is easier to deal with in a String (file saving and loading)
			}

		// next section loads images for treasure, keys, and monsters
		treasureimage = loadImage ("/images/treasure.gif");
		keyimage = loadImage ("/images/key.gif");
		monsterimage = loadImage ("/images/monster.gif");
	}
	// used to access the length of the map
	public int rowaccess()
	{
		return map.length;
	}
	// used to access the width of the map
	public int colaccess()
	{
		return map[0].length;
	}

	// used to add monsters, keys, and treasures
	public void add (int r, int c, String item)
	{
		// subtract one since row 1 and column 1 are row 0 and column 0 for the array (program uses "human" columns and rows, starting from 1)
		r = r - 1;
		c = c - 1;
		if (item.equals("Treasure")) // if the user is adding a treasure
			map [r] [c] = 'T';	// add a treasure
		else if (item.equals("Monster")) // if the user is adding a monster
			map [r] [c] = 'M';	// add a monster
		else // if the user is adding a key
			map [r] [c] = 'K';	// add a key
	}

	// used to add doors and walls (positions to add whole wall selected by default at the beginning)
	// if the start location is larger than the end location, the method does nothing (leaves map unchanged)
	public void add (int r, int c, int start, int end, String choice, String element)
	{
		// subtract one since row 1 and column 1 are row 0 and column 0 for the array (program uses "human" columns and rows, starting from 1)
		r = r - 1;
		c = c - 1;

		if (element.equals("Door")) // if a door is selected to be added
		{
			if (choice.equals("Row")) // if the user is adding a row
			{
				for (int x = start - 1; x < end; x++) // goes through all indices selected
				{
					map [r] [x] = 'D'; // creates a door
				}
			}
			else // if the user is adding a column
			{
				for (int x = start - 1; x < end; x++) // goes through all indices selected
				{
					map [x] [c] = 'D'; // creates a door
				}
			}
		}
		else // if a wall is to be added
		{
			if (choice.equals("Row")) // if the user is adding a row
			{
				for (int x = start - 1; x < end; x++) // goes through all indices selected
				{
					map [r] [x] = 'W'; // creates a wall
				}
			}
			else // if the user is adding a column
			{
				for (int x = start - 1; x < end; x++) // goes through all indices selected
				{
					map [x] [c] = 'W'; // creates a wall
				}
			}
		}
	}

	public char search (String item) // used to search for a specific element and highlights it in yellow
	{
		char select; // selection character used to identify what object is being processed

		if (item.equals("Treasure")) // if the user is searching for a treasure
			select = 'T'; // treasure is selected
		else if (item.equals ("Key")) // if the user is searching for a key
			select = 'K'; // key is selected
		else if (item.equals ("Monster")) // if the user is searching for a monster
			select = 'M'; // monster is selected
		else if (item.equals ("Door")) // if the user is searching for a door
			select = 'D'; // door is selected
		else // if the element is a wall
			select = 'W'; // wall is selected

		// next loop searches through the whole array and switches the char to 'Y' to highlight the elements
		for (int row = 0 ; row < map.length; row++) // goes through all rows of the map
			for (int col = 0 ; col < map[0].length; col++) // goes through all columns of the map
			{
				if (map [row] [col] == select) // searches for the element specified
				{
					map [row] [col] = 'Y'; // changes it to be highlighted in yellow
				}
			}

		return select; // returns character of element selected
	}
	
	public boolean found () // checks if an element was found using the search method
	{
		boolean fnd = false;
		for (int row = 0 ; row < map.length; row++) // goes through all rows of the map
			for (int col = 0 ; col < map[0].length; col++) // goes through all columns of the map
			{
				if (map [row] [col] == 'Y') // if an element was found
					fnd = true; // indicate success of the search
			}
		return fnd;
	}

	public void reset (char selected) // resets element after it has been searched for
	{
		// next loop searches through the whole array and switches the char from 'Y' to original element before repainting
		for (int row = 0 ; row < map.length; row++) // searches through the rows of the map
		{
			for (int col = 0 ; col < map[0].length; col++) // searches through the columns of the map
			{
				if (map [row] [col] == 'Y') // searches for the element specified that has been highlighted
				{
					map [row] [col] = selected; // resets the highlighted items to their original state
				}
			}
		}
	}

	public void save (String directory, String fname) throws IOException // saves file in specified directory with specified name that can be loaded for future use
	{
		File f = new File(directory, fname + ".txt"); // creates new text file in the specified directory with specified name
		f.createNewFile(); // create a new file, even if one exists, overwrites with same name in the same directory
		FileWriter fw = new FileWriter (f); // FileWriter to write chars to file
		PrintWriter fileout = new PrintWriter (fw); // PrintWriter uses the FileWriter to print characters to the file

		for (int x = 0 ; x < map.length ; x++) // goes through all rows of the map
		{
			for (int a = 0; a < map[0].length; a++) // goes through all columns of the map
				fileout.print (map [x][a]); // writes all chars to file, goes through every row and column
			fileout.print(" "); // blank space printed to signify next row
		}
		fileout.close (); // close file after the map has been saved
	}

	public void load (File f) throws IOException // loads selected file representing a map
	{
		FileReader fr = new FileReader (f); // FileReader to read contents of file
		BufferedReader filein = new BufferedReader (fr); // BufferedReader that uses the FileReader to read the String

		String line = ""; // String that stores lines from the file
		arrayString = ""; // string to store the contents of the loaded file
		
		int rowlength = 0, columnlength = 0, counter = 0; // stores lengths of a row and a column, counts number of blank spaces so they can be cancelled out
		while ((line = filein.readLine ()) != null) // file has not ended
			arrayString = arrayString + line; // adds text to one String (arrayString will store the contents of the text file)
		filein.close (); // close file after loading

		if (arrayString.length() <= 528 && arrayString.length() % 2 == 0)
			// length of 528 represents the largest possible array, even length ensures that the number can be divided evenly into rows and columns
		{
			char[] array = arrayString.toCharArray(); // converts String to one dimensional array of characters with characters for slots
			int a = 0;
			char value = array[a];
			while (value != ' ') // keeps searching String until space signals end of a row
			{
				columnlength++; // determines length of a row
				a++; // switches to next location 
				value = array[a]; // sets value to be checked to the next one until an empty space is detected
			}

			for (int b = 0; b < array.length; b++) // goes through the entire array to check for invalid characters
			{
				value = array[b];
				if (value != 'W' && value != 'D' && value != 'T' && value != 'K' && value != 'Y' && value != 'M' && value != ' ') // if the character does not correspond to an element of a map or a location indicator
					array[b] = 'S'; // sets default to blank space
				else if (value == ' ') // if the value is row separator
					counter++; // increase counter to take away number of spaces that do not represent an actual block
			}
			rowlength = (arrayString.length() - counter) / columnlength; // calculates length of a row based off of the length of a column
			map = new char [rowlength][columnlength]; // resizes the map to represent the load file
			int index = 0; // index used to cycle through one dimensional array
			for (int b = 0; b < map.length; b++) // goes through all rows of the map
				for (int x = 0; x < map[0].length; x++) // goes through all columns of the map
				{
					if (array[index] != ' ') // excludes invalid blocks for the map
						map[b][x] = array[index]; // transfers characters over to the array representing the map
					else // if the block is invalid
					{
						index++; // skip an index
						map[b][x] = array[index]; // transfers characters over to the array representing the map
					}
					index++; // cycles through characters so they can be placed onto the map
				}
		}
	}

	// loads the Image from the file.
	private Image loadImage (String name)  // private access as images should only be loaded within the Map class
	{
		Image img = null; // sets image default to null to avoid unexpected behaviour
		try // tries to load image with specified name to retrieve required objects for the map
		{
			img = ImageIO.read (new File (name)); // load file into Image object
		}
		catch (IOException e) // catches exception if image is not found
		{
			System.out.println ("Image file not found."); // notifies user when the specified object image is not located
		}
		return img;
	}

	public void print (Graphics g)    // displays the map on the screen
	{
		boolean drawn = false; // used to determine if the rectangle has to be filled in or not
		for (int row = 0 ; row < map.length; row++) // goes through all rows of the map
		{
			for (int col = 0 ; col < map[0].length; col++) // goes through all columns of the map
			{
				if (map [row] [col] == 'W') // if cell is a wall
					g.setColor (Color.black); // walls are black 
				else if (map [row] [col] == 'D') // if cell is a door
					g.setColor (Color.red); // doors are red
				else if (map [row] [col] == 'S') // space will erase what was there
					g.setColor (Color.white); // blank spaces
				else if (map [row] [col] == 'T') // if the object is a treasure
				{
					g.drawImage(treasureimage, 95 + (col * width) + (32 - map[0].length) * height / 2, (row * height) + 30, null); // draws image of Treasure
					drawn = true; // boolean that prevents block from being filled if image is already displayed
				}
				else if (map [row] [col] == 'K') // if the object is a key
				{
					g.drawImage(keyimage, 95 + (col * width) + (32 - map[0].length) * height / 2, (row * height) + 30, null); // draws image of Key
					drawn = true; // boolean that prevents block from being filled if image is already displayed
				}
				else if (map [row] [col] == 'Y') // if the element has been searched for
				{
					g.setColor (Color.yellow); // highlights searched items in yellow
				}
				else if (map [row] [col] == 'M')// if the object is a monster
				{
					g.drawImage(monsterimage, 95 + (col * width) + (32 - map[0].length) * height / 2, (row * height) + 30, null); // draws image of Monster
					drawn = true; // boolean that prevents block from being filled if image is already displayed
				}

				if (!drawn) // if image has not been drawn in the selected block
					g.fillRect (col * width + 95 + (32 - map[0].length) * height / 2, (row * height)  + 30, width, height); // fill block, 95 and 15 to center within window
				else // sets drawn back to false to reset for next block
					drawn = false;
			}
		}
		g.setColor(Color.black); // sets color back to black to draw outline and labeling 
		for (int x = 0; x < map.length; x++) // for all rows except the first and last (excludes borders)
			for (int y = 0; y < map[0].length; y++) // for all columns except the first and last (excludes borders)
			{
				g.drawString("" + (x + 1), 50 + (32 - map[0].length) * height / 2, 55 + (x * height)); // draws labels on the left
				g.drawString("" + (y + 1), 105 + (y * width) + (32 - map[0].length) * height / 2, (map.length * height) + 60); // draws labels on the bottom
			}

		for (int x = 0; x < map.length; x++) // for all rows except the first and last (excludes borders)
			for (int y = 0; y < map[0].length; y++) // for all columns except the first and last (excludes borders)
				g.drawRect(95 + (y * width) + (32 - map[0].length) * height / 2, (x * height) + 30, width, height); // draws rectangle to create a grid
	}
}

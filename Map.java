
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

public class Map 
{
	private char map [] [];
	private int width, height;
	private Image treasureimage, keyimage, monsterimage;
	private String arrayString = ""; 

	public Map (int rows, int cols, int blockwidth, int blockheight) 
	{
		width = blockwidth;  height = blockheight; 
		map = new char [rows] [cols]; 

		for (int row = 0 ; row < rows ; row++) 
			for (int col = 0 ; col < cols ; col++) 
			{
				if (row == 0 || row == rows-1 || col == 0 || col == cols-1) 
					map [row] [col] = 'W'; 
				else
					map [row] [col] = 'S'; 
			}

		treasureimage = loadImage ("/images/treasure.gif");
		keyimage = loadImage ("/images/key.gif");
		monsterimage = loadImage ("/images/monster.gif");
	}
	
	public int rowaccess()
	{
		return map.length;
	}
	
	public int colaccess()
	{
		return map[0].length;
	}

	public void add (int r, int c, String item)
	{
		r = r - 1;
		c = c - 1;
		if (item.equals("Treasure")) 
			map [r] [c] = 'T';
		else if (item.equals("Monster")) 
			map [r] [c] = 'M';
		else 
			map [r] [c] = 'K';
	}

	public void add (int r, int c, int start, int end, String choice, String element)
	{
		r = r - 1;
		c = c - 1;

		if (element.equals("Door")) 
		{
			if (choice.equals("Row")) 
			{
				for (int x = start - 1; x < end; x++) 
				{
					map [r] [x] = 'D'; 
				}
			}
			else 
			{
				for (int x = start - 1; x < end; x++) 
				{
					map [x] [c] = 'D';
				}
			}
		}
		else 
		{
			if (choice.equals("Row")) 
			{
				for (int x = start - 1; x < end; x++) 
				{
					map [r] [x] = 'W'; 
				}
			}
			else 
			{
				for (int x = start - 1; x < end; x++) 
				{
					map [x] [c] = 'W'; 
				}
			}
		}
	}

	public char search (String item) 
	{
		char select;

		if (item.equals("Treasure")) 
			select = 'T';
		else if (item.equals ("Key")) 
			select = 'K';
		else if (item.equals ("Monster")) 
			select = 'M';
		else if (item.equals ("Door")) 
			select = 'D';
		else 
			select = 'W';

		
		for (int row = 0 ; row < map.length; row++) 
			for (int col = 0 ; col < map[0].length; col++) 
			{
				if (map [row] [col] == select) 
				{
					map [row] [col] = 'Y'; 
				}
			}

		return select; 
	}
	
	public boolean found () 
	{
		boolean fnd = false;
		for (int row = 0 ; row < map.length; row++) 
			for (int col = 0 ; col < map[0].length; col++) 
			{
				if (map [row] [col] == 'Y') 
					fnd = true; 
			}
		return fnd;
	}

	public void reset (char selected) 
	{
		
		for (int row = 0 ; row < map.length; row++)
		{
			for (int col = 0 ; col < map[0].length; col++)
			{
				if (map [row] [col] == 'Y')
				{
					map [row] [col] = selected; 
				}
			}
		}
	}

	public void save (String directory, String fname) throws IOException 
	{
		File f = new File(directory, fname + ".txt"); 
		f.createNewFile();
		FileWriter fw = new FileWriter (f); 
		PrintWriter fileout = new PrintWriter (fw); 

		for (int x = 0 ; x < map.length ; x++)
		{
			for (int a = 0; a < map[0].length; a++) 
				fileout.print (map [x][a]); 
			fileout.print(" "); 
		}
		fileout.close (); 
	}

	public void load (File f) throws IOException 
	{
		FileReader fr = new FileReader (f); 
		BufferedReader filein = new BufferedReader (fr); 

		String line = ""; 
		arrayString = "";
		
		int rowlength = 0, columnlength = 0, counter = 0; 
		while ((line = filein.readLine ()) != null) 
			arrayString = arrayString + line; 
		filein.close (); 

		if (arrayString.length() <= 528 && arrayString.length() % 2 == 0)
			
		{
			char[] array = arrayString.toCharArray(); 
			int a = 0;
			char value = array[a];
			while (value != ' ') 
			{
				columnlength++; 
				a++;
				value = array[a];
			}

			for (int b = 0; b < array.length; b++) 
			{
				value = array[b];
				if (value != 'W' && value != 'D' && value != 'T' && value != 'K' && value != 'Y' && value != 'M' && value != ' ') 
					array[b] = 'S'; 
				else if (value == ' ') 
					counter++; 
			}
			rowlength = (arrayString.length() - counter) / columnlength; 
			map = new char [rowlength][columnlength]; 
			int index = 0; 
			for (int b = 0; b < map.length; b++) 
				for (int x = 0; x < map[0].length; x++) 
				{
					if (array[index] != ' ') 
						map[b][x] = array[index]; 
					else 
					{
						index++; 
						map[b][x] = array[index]; 
					}
					index++; 
				}
		}
	}


	private Image loadImage (String name)  
	{
		Image img = null; 
		try 
		{
			img = ImageIO.read (new File (name)); 
		}
		catch (IOException e) 
		{
			System.out.println ("Image file not found."); 
		}
		return img;
	}

	public void print (Graphics g)    
	{
		boolean drawn = false; 
		for (int row = 0 ; row < map.length; row++)
		{
			for (int col = 0 ; col < map[0].length; col++)
			{
				if (map [row] [col] == 'W') 
					g.setColor (Color.black); 
				else if (map [row] [col] == 'D') 
					g.setColor (Color.red); 
				else if (map [row] [col] == 'S') 
					g.setColor (Color.white); 
				else if (map [row] [col] == 'T') 
				{
					g.drawImage(treasureimage, 95 + (col * width) + (32 - map[0].length) * height / 2, (row * height) + 30, null); 
					drawn = true; 
				}
				else if (map [row] [col] == 'K') 
				{
					g.drawImage(keyimage, 95 + (col * width) + (32 - map[0].length) * height / 2, (row * height) + 30, null); 
					drawn = true;
				}
				else if (map [row] [col] == 'Y') 
				{
					g.setColor (Color.yellow); 
				}
				else if (map [row] [col] == 'M')
				{
					g.drawImage(monsterimage, 95 + (col * width) + (32 - map[0].length) * height / 2, (row * height) + 30, null); 
					drawn = true; 
				}

				if (!drawn) 
					g.fillRect (col * width + 95 + (32 - map[0].length) * height / 2, (row * height)  + 30, width, height); 
				else 
					drawn = false;
			}
		}
		g.setColor(Color.black); 
		for (int x = 0; x < map.length; x++) 
			for (int y = 0; y < map[0].length; y++) 
			{
				g.drawString("" + (x + 1), 50 + (32 - map[0].length) * height / 2, 55 + (x * height)); 
				g.drawString("" + (y + 1), 105 + (y * width) + (32 - map[0].length) * height / 2, (map.length * height) + 60); 
			}

		for (int x = 0; x < map.length; x++) 
			for (int y = 0; y < map[0].length; y++) 
				g.drawRect(95 + (y * width) + (32 - map[0].length) * height / 2, (x * height) + 30, width, height); 
	}
}

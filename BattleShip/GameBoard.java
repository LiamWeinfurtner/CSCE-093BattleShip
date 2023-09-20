package BattleShip;
import java.util.ArrayList;

public class GameBoard
{
	int rowCount = 10;
	int colCount = 10;
	
	final String LINE_END = System.getProperty("line.separator"); 
	
	//Commented out the ArrayList implementation in favor of the 2D array implementation. 
	//ArrayList<ArrayList<Cell>> cells;
	ArrayList< Ship > myShips = new ArrayList<Ship>();
	
	// Create a 2D array containing Cell objects. Coordinate corresponds to location in the array
	Cell[][] board = new Cell[rowCount][colCount];

	//Constructor for GameBoard
	public GameBoard(int rowCount, int colCount)
	{
		this.rowCount = rowCount;
		this.colCount = colCount;
		
		//create the 2D array of cells
		int x = 0;
		int y = 0;
		for (x=0; x<rowCount; x++)
		{
			for (y=0; y<colCount; y++)
			{
				this.board[x][y] = new Cell();
			}
		}
	}
	  
    
	//TODO: Write function
	public String draw()
	{

		//draw the entire board... I'd use a StringBuilder object to improve speed
		//remember - you must draw one entire row at a time, and don't forget the
		//pretty border...
		StringBuilder boardDrawer = new StringBuilder(LINE_END);

		// Draw top border
		for (int i=-1; i<=this.colCount; i++)
		{
			if (i==-1) boardDrawer.append("+");
			else if (i==this.colCount) boardDrawer.append("+");
			else boardDrawer.append("-");
		}
		boardDrawer.append(LINE_END);

		// Draw out the game board. Iterate through each cell and append StringBuilder with its draw() method
		for (int i=0; i<this.rowCount; i++)
		{
			for (int j=-1; j<this.colCount; j++)
			{
				if ((j == -1) || (j == this.colCount)) {boardDrawer.append('|');}
				else 
				{
					Cell c = this.board[i][j];
					boardDrawer.append(c.draw());
				}
			}
			// Row done, move to next row
			boardDrawer.append(LINE_END);
		}

		// Draw bottom border
		for (int i=-1; i<=this.colCount; i++)
		{
			if (i==-1) boardDrawer.append("+");
			else if (i==this.colCount) boardDrawer.append("+");
			else boardDrawer.append("-");
		}
		boardDrawer.append(LINE_END);

		return boardDrawer.toString();
	}
	
	//add in a ship if it fully 1) fits on the board and 2) doesn't collide w/
	//an existing ship.
	//Returns true on successful addition; false, otherwise
	public boolean addShip(Ship s, Position sternLocation, HEADING bowDirection)
	{
		int shipLength = s.getLength();
		
		// ArrayList will store all "Positions" of the ship first to compare things against
		ArrayList<Position> shipSpawn = new ArrayList<Position>();

		// Add the stern location to the shipSpawn, then extrude out the length in the opposite of the bowDirection
		shipSpawn.add(sternLocation);
		if (bowDirection == HEADING.NORTH)
		{
			for (int i=0; i<shipLength; i++)
			{
				shipSpawn.add(new Position(sternLocation.x, sternLocation.y-i));
			}
		}
		else if (bowDirection == HEADING.EAST)
		{
			for (int i=0; i<shipLength; i++)
			{
				shipSpawn.add(new Position(sternLocation.x-i, sternLocation.y));
			}
		}
		else if (bowDirection == HEADING.SOUTH)
		{
			for (int i=0; i<shipLength; i++)
			{
				shipSpawn.add(new Position(sternLocation.x+i, sternLocation.y));
			}
		}
		else if (bowDirection == HEADING.WEST)
		{
			for (int i=0; i<shipLength; i++)
			{
				shipSpawn.add(new Position(sternLocation.x, sternLocation.y+i));
			}
		}

		// Now that we have the location of the ship, check to make sure that it is on the game board
		for (Position checkPosition : shipSpawn)
		{
			// Check if too small
			if ((checkPosition.x < 0) || (checkPosition.y < 0))
			{
				return false;
			}
			
			// Check if too big
			if ((checkPosition.x > rowCount-1) || (checkPosition.y > colCount-1));
			{
				return false;
			}
		}

		// Ship is on the board, make sure no other ships already exist in those locations
		for (Position checkPosition : shipSpawn)
		{
			// Grab cell at each position the ship will be spawned at
			Cell checkCell = board[checkPosition.x][checkPosition.y];
			// Check if ship exists at that position and return false if one does
			if (checkCell.getShip() != null)
			{
				return false;
			}
		}

		// If we made it this far, that means the ship will spawn on the board and without any collisions. Spawn the ship!
		for (Position spawnPosition : shipSpawn)
		{
			board[spawnPosition.x][spawnPosition.y].setShip(s);
		}

		return true;
	}
	
	//Returns A reference to a ship, if that ship was struck by a missle.
	//The returned ship can then be used to print the name of the ship which
	//was hit to the player who hit it.
	//Ensure you handle missiles that may fly off the grid
	//TODO: Write function
	public Ship fireMissle( Position coordinate )
	{
		return (new Destroyer("name"));
	}
	
	//Here's a simple driver that should work without touching any of the code below this point
	public static void main( String [] args )
	{
		System.out.println( "Hello World" );
		GameBoard b = new GameBoard( 10, 10 );	
		System.out.println( b.draw() );
		
		Ship s = new Cruiser( "Cruiser" );
		if( b.addShip(s, new Position(3,6), HEADING.WEST ) )
			System.out.println( "Added " + s.getName() + "Location is " );
		else
			System.out.println( "Failed to add " + s.getName() );
		
		s = new Destroyer( "Vader" );
		if( b.addShip(s, new Position(3,5), HEADING.NORTH ) )
			System.out.println( "Added " + s.getName() + "Location is " );
		else
			System.out.println( "Failed to add " + s.getName() );
		
		System.out.println( b.draw() );
		
		b.fireMissle( new Position(3,5) );
		System.out.println( b.draw() );
		b.fireMissle( new Position(3,4) );
		System.out.println( b.draw() );
		b.fireMissle( new Position(3,3) );
		System.out.println( b.draw() );
		
		b.fireMissle( new Position(0,6) );
		b.fireMissle( new Position(1,6) );
		b.fireMissle( new Position(2,6) );
		b.fireMissle( new Position(3,6) );
		System.out.println( b.draw() );
		
		b.fireMissle( new Position(6,6) );
		System.out.println( b.draw() );
	}

}

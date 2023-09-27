package BattleShip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Client
{
	final String NEWL = System.getProperty("line.separator");
	
	private String name = "Player";
	PrintWriter out = null;
	BufferedReader in = null;
	GameManager man = null;
	GameBoard board = new GameBoard(10,10);
	GameBoard targets = new GameBoard(10,10);
	
	Client( BufferedReader in, PrintWriter out, GameManager manager )
	{
		this.in = in;
		this.out = out;
		this.man = manager;
	}
	
	public void playGame() throws IOException
	{
		this.out.println( NEWL + NEWL + "   Missiles Away! Game has begun" );
		this.out.println( "   To Launch a missle at your enemy:" );
		this.out.println( "F 2 4" );
		this.out.println( "Fires a missile at coordinate x=2, y=4." );
		
		while (this.processCommand()) // put Code Here to process in game commands, after each command, print the target board and game board w/ updated state )
		{
			out.println( "------------------------" );
			out.println( "Target Board:" + this.targets.draw() );
			out.println( "Your Ships: " + this.board.draw() );
			out.println( "   Waiting for Next Command...\n\n" );
			out.flush();
			
			//Perform test here to see if we have run or lost
			if (this.allEnemyShipsAreDestroyed())
			{
				this.out.println("All enemy ships sunk, you are victorious!");
				break;
			}
			else if (this.allMyShipsAreDestroyed())
			{
				this.out.println("All your ships have sunk, you have been defeated!");
				break;
			}
		}
	}
	
	//Returns a bool, true iff all of this client's ships are destroyed
	boolean allMyShipsAreDestroyed()
	{
		for (Ship ship : board.myShips)
		{
			if (!ship.isAlive())
			{
				return true;
			}
		}

		return false;
	}

	//Returns a bool, true iff all of the opponent's ships are destroyed
	boolean allEnemyShipsAreDestroyed()
	{
		for (Ship ship : targets.myShips)
			{
				if (!ship.isAlive())
				{
					return true;
				}
			}
		return false;
	}

	//"F 2 4" = Fire command
	//"C Hello world, i am a chat message"
	//"D" - Redraw the latest game and target boards
	boolean processCommand() throws IOException
	{
		String input = this.in.readLine();
		String [] split = input.split("\\s+");

		if (split[0].equals("F"))
		{
			return this.processFireCmd(split);
		}
		else if (split[0].equals("C"))
		{
			return this.processChatCmd(input);
		}
		else if (split[0].equals("D"))
		{
			;
		}

		else this.out.println("Invalid command. Please enter a valid command.");
		
		return true;
	}
	
	//When a fire command is typed, this method parses the coordinates and launches a missle at the enemy
	boolean processFireCmd( String [] s )
	{
		// Create a new Position that holds the location to be fired on
		Position fireLocation = new Position(Integer.parseInt(s[1]), Integer.parseInt(s[1]));
		// Marks fired on location for the opponents board
		this.targets.fireMissle(fireLocation);
		// Using the reference to the other opponents board, call fireMissile from their client to affect their own board
		// and check whether it was a valid hit on an opponent's ship
		Ship targetShip = this.man.getOpponent(this).getGameBoard().fireMissle(fireLocation);

		if (targetShip != null)
		{
			this.out.println("Successful missile hit on target ship " + targetShip.getName());

			// Check if that sunk the enemy ship
			if (!targetShip.isAlive())
			{
				this.out.println(targetShip.getName() + "has sunk!");
			}
		}
		// Else, missed
		else
		{
			this.out.println("Missile fired... and missed!");
		}

		return true;
	}
	
	//Send a message to the opponent
	boolean processChatCmd( String s )
	{
		if (s.length() > 2)
		{
			this.man.getOpponent(this).out.println("Opponent said: " + s.substring(1).toString() ); 
			this.man.getOpponent(this).out.flush();
		}

		return true;
	}
	
	GameBoard getGameBoard() { return this.board; }
	
	public void initPlayer() throws IOException
	{
		// Asks for player name and reads name from user input
		out.println("Enter your name: ");
		this.name = in.readLine();
		
		// Instructions for setting up ships sent to player. Player makes 2 ships
		out.println("   You will now place 2 ships. You may choose between either a Cruiser (C) " );
		out.println("   and Destroyer (D)...");
		out.println("   Enter Ship info. An example input looks like:");
		out.println("\nD 2 4 S USS MyBoat\n");
		out.println("   The above line creates a Destroyer with the stern located at x=2 (col)," );
		out.println("   y=4 (row) and the front of the ship will point to the SOUTH (valid" );
		out.println("   headings are N, E, S, and W.\n\n" );
		out.println("   the name of the ship will be \"USS MyBoat\"");
		out.println("Enter Ship 1 information:" );
		out.flush();
		
		// Get ship locations from the player for all 2 ships (or more than 2 if you're using more ships)
		int numShips = 0;
		while (numShips < 2)
		{
			String userInput = in.readLine();
			// Read user input in this format: "<ship type char> <x coord int> <y coord int> <heading direction char> <name string>"
			String [] inputSplit = userInput.split(" ", 5);

			// Split apart user input
			String shipClass = inputSplit[0];
			int x = Integer.parseInt(inputSplit[1]);
			int y = Integer.parseInt(inputSplit[2]);
			String heading = inputSplit[3];

			// Parse for heading of ship
			HEADING h = null;
			if (heading.equals("N"))
			{
				h = HEADING.NORTH;
			}
			else if (heading.equals("E"))
			{
				h = HEADING.EAST;
			}
			else if (heading.equals("S"))
			{
				h = HEADING.SOUTH;
			}
			else if (heading.equals("W"))
			{
				h = HEADING.WEST;
			}

			Ship ship = null;

			// Grab ship type, cruiser or destroyer and build based off type
			if (shipClass.equals("D"))
			{
				ship = new Destroyer(name);
			}
			else if (shipClass.equals("C"))
			{
				ship = new Cruiser(name);
			}
			else 
			{
				System.out.println("Invalid command by player.");
				out.println("Invalid command.");
				out.flush();
			}
			
			// Attempt to add ship to this client's player gameboard. Returns false if failure
			if (this.board.addShip(ship, new Position(x, y), h))
			{
				numShips++;
				out.println(ship.getName() + " has been succesfully added to the board!");
			}
			else
			{
				System.out.println("Invalid ship command or position from user.");
				out.println("Invalid ship. Ship already exists or there was a placement collision on the board.");
				out.println("Please try again.");
				out.println(this.board.draw());
				out.flush();
			}

		}
		
		//After all game state is input, draw the game board to the client
		System.out.println( "Waiting for other player to finish their setup, then war will ensue!" );
		out.println("Gameboard initialization complete! Here is your game board:");
		out.println(this.board.draw());
		out.println("Once both players have completed their setup, the game will begin.");
	}
	
	String getName() { return this.name; }
	
	public static void main( String [] args )
	{
		
		
	}
}

package BattleShip;
import BattleShip.Client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class GameManager
{
	private ArrayList<Client> clients = new ArrayList<Client>();
	private ServerSocket listener = null;
	
	public GameManager()
	{		
	}
	
	//Returns a client reference to the opponent. This way, we can inspect attributes
	//and send messages between clients... Each client has a reference to the GameManager
	//so a client is able to use this method to get a reference to his opponent
	//TODO: Write function
	public Client getOpponent( Client me )
	{
		// Look into one of the clients, if it is the current client, return the other. If it is not, return that one
		if (clients.get(0) == me)
		{
			return clients.get(1);
		}
		else
		{
			return clients.get(0);
		}
	}
	
	//In a asychronous nature, begin playing the game. This should only occur after 
	//the players have been fully initialized.
	public void playGame()
	{
		//Each player may begin firing missiles at the other player. First player to lose all ships is the loser.
		//Asynchronously process missile fire commands from each player		
		clients.parallelStream().forEach( client -> 
		{
			try{ client.playGame(); }
			catch( IOException e ) { e.printStackTrace(); } 
		} );
		
	}
	
	//Create a server listener socket and wait for two clients to connect.
	//Use the new client socket to create a PrintWriter and BufferedReader
	//so you can pass these two streams into the constructor of a new client.
	//Don't forget about try/finally blocks, if needed
	boolean waitFor2PlayersToConnect() throws IOException
	{
		try
		{
			// Create new TCP socket listening on port 10000
			this.listener = new ServerSocket(10000);

			// Begin listening for connections, stop once two players have connected
			while(clients.size() < 2)
			{
				// Accept an incoming connection
				Socket clientSocket = listener.accept();
				
				// sending messages to client prints to the clientSocket output stream
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				// receiving messages from client reads from clientSocket input stream to the buffer in BufferedReader
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

				Client client = new Client(in, out, this);
				clients.add(client);
				System.out.println("A new client has joined the server.");
				out.println("Welcome to Battleship!");
				out.println("Please wait for another player to connect.");
			}
			return true;
		}
		finally
		{
			System.out.println("All clients have connected. Shutting down listener.");
		}
	}
	
	//let players initialize their name, and gameboard here. This should be done asynchronously
	void initPlayers() throws IOException
	{
		// Each client is going to call it's initPlayer() function to ask player for name and ship setup
		clients.parallelStream().forEach(
			client ->
			{
				try
				{
					client.initPlayer();
				}
				catch(IOException e)
				{
					System.out.print(e);
				}
				finally
				{
					
				}
			}
		);
	}
	
	
	//Main driver for the program... Hit Crtl-F11 in eclipse to launch the server...
	//Of course, it has to compile first...
	public static void main( String [] args ) throws IOException
	{
		GameManager m = new GameManager();
		
		System.out.println( "<<<---BattleShip--->>>" );
		System.out.println( "Waiting for two players to connect to TCP:10000" );
		m.waitFor2PlayersToConnect();
		System.out.println( "Clients have joined!!!");		
		m.initPlayers();
		System.out.println( m.clients.get(0).getName() + " vs " + m.clients.get(1).getName() + " Let's Rumble..." );
		m.playGame();		
		System.out.println( "Shutting down server now... Disconnecting Clients..." );
	}

}

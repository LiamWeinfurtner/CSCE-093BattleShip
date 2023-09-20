package BattleShip;

/**
 * Stores an x and y coordinate position. this.equals checks if a given Position object matches this Position object.
 */
public class Position
{
	public int x = 0;
	public int y = 0;
	
	/**
	 * 
	 * @param x
	 * @param y
	 */
	public Position( int x, int y )
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 *  
	 * @return True if both Positions are equal. False otherwise.
	 */
	public boolean equals( Position p )
	{
		if( this.x == p.x && this.y == p.y )
			return true;
		return false;
	}

}

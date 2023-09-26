package BattleShip;

public class Cell
{
	protected boolean struckByMissle = false;
	protected Ship ship = null;
	
	public Cell()
	{		
	}
	
	/**
	 * Returns boolean to check if Position has been struck.
	 * @return
	 */
	public boolean hasBeenStruckByMissile()
	{
		return this.struckByMissle;
	}
	
	/**
	 * Pass boolean value for whether the Position was struck.
	 * @param wasStruck
	 */
	public void hasBeenStruckByMissile( boolean wasStruck )
	{	
		this.struckByMissle = wasStruck;
	}
	
	/**
	 * Draws the value at the Position. If no ship exists at the Position and it was struck, draw an 'x'. If
	 * a ship does exist, call the ship's draw function to draw itself.
	 * @return If no ship exists, 'x' if struck or ' ' if not struck. Else return drawShipStatusAtCell().
	 */
	public char draw()
	{
		if( this.ship == null )
		{
			if( this.struckByMissle )
				return 'x';
			return ' ';
		}
		//a ship is at this cell
		return ship.drawShipStatusAtCell( this.struckByMissle );			
	}
	
	public Ship getShip() { return this.ship; }
	public void setShip( Ship s ) { this.ship = s; }

	public static void main(String[] args)
	{
	}

}

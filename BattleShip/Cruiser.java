package BattleShip;


public class Cruiser extends Ship
{

    //Constructor
    public Cruiser(String name)
    {
       super(name);
    }

    //TODO: Write function
    public int getLength()
    {
        return 4;
    }

    //TODO: Write function
    public char drawShipStatusAtCell(boolean isDamanged)
    {
        // Return lowercase if damaged, uppercase if not
        if (isDamanged)
        {
            return 'c';
        }
        else return 'C';
    }



}
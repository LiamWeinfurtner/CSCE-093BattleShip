package BattleShip;

class Destroyer extends Ship
{

    //Constructor
    public Destroyer(String name)
    {
        super(name);
    }

    public int getLength()
    {
        return 3;
    }

    public char drawShipStatusAtCell(boolean isDamanged)
    {
        // Return lowercase if damaged, uppercase if not
        if (isDamanged)
        {
            return 'd';
        }
        else return 'D';
    }



}
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
        return 1;
    }

    public char drawShipStatusAtCell(boolean isDamanged)
    {
        return 'a';
    }



}
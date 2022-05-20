public class Profile
{
    private String name;
    private int lvl;

    Profile(String name, int lvl)
    {
        this.name = name;
        this.lvl = lvl;
    }

    @Override
    public String toString()
    {
        return name;
    }
    public int getLvl(){return lvl;}
}
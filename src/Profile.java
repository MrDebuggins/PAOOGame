public class Profile
{
    private String name;
    private int lvl;
    public double[] records = {0,0,0};

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
    public void setLvl(int id)
    {
        if(id > 0 && id < 4 && id > lvl)
            lvl = id;
    }
    public void setRecord(int id, double rec)
    {
        if(id >= 0 && id < 3)
            if(records[id] > rec || records[id] == 0)
                records[id] = rec;
    }
}
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Level
{
    public Player player;
    private int groupsNr, staticObjsNr, dynamicObjsNr;
    private GameObj[] objects;

    public Level(int id)
    {
        loadLevel(id);
        player = player.getInstance();
    }

    /**
     * Returns 0 if loaded successfully, -1 if file not found, 1, 2 or 3 for wrong numbers of groups,
     * static objects and dynamic objects respectively
     * @param id level number
     * @return error code
     */
    public int loadLevel(int id)
    {
        Scanner scn;

        try
        {
            File file = new File("levels/lvl" + id + ".txt");
            scn = new Scanner(file);
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Error loading level "+id+". Message: "+e.getMessage());
            return -1;
        }

        groupsNr = scn.nextInt();
        staticObjsNr = scn.nextInt();
        dynamicObjsNr = scn.nextInt();

        objects = new GameObj[groupsNr + staticObjsNr + dynamicObjsNr];

        int err;
        err = loadGroups(scn);
        if(err != 0)
            return err;
        err = loadStatics(scn);
        if(err != 0)
            return err;
        err = loadDynamics(scn);
        if(err != 0)
            return err;


        return 0;
    }

    private int loadGroups(Scanner scn)
    {
        if(groupsNr < 0)
            return 1;
        else
        {
            int type;
            for(int i = 0; i < groupsNr; i++)
            {
                HitBox h = new HitBox();
                type = scn.nextInt();
                h.posx = scn.nextInt();
                h.posy = scn.nextInt();
                h.width = scn.nextInt();
                h.height = scn.nextInt();
                objects[i] = new BlockGroup(Type.values()[type], h);
            }
        }

        return 0;
    }

    private int loadStatics(Scanner scn)
    {
        if(staticObjsNr < 0)
            return 2;
        else
        {
            int type;
            for(int i = groupsNr; i < groupsNr + staticObjsNr; i++)
            {
                HitBox h = new HitBox();
                type = scn.nextInt();
                h.posx = scn.nextInt();
                h.posy = scn.nextInt();
                h.width = scn.nextInt();
                h.height = scn.nextInt();
                objects[i] = new BlockGroup(Type.values()[type], h);
            }
        }

        return 0;
    }

    private int loadDynamics(Scanner scn)
    {
        if(dynamicObjsNr < 0)
            return 3;
        else
        {
            int type;
            for(int i = groupsNr + staticObjsNr; i < groupsNr + staticObjsNr + dynamicObjsNr; i++)
            {
                HitBox h = new HitBox();
                type = scn.nextInt();
                h.posx = scn.nextInt();
                h.posy = scn.nextInt();
                h.width = scn.nextInt();
                h.height = scn.nextInt();
                objects[i] = new BlockGroup(Type.values()[type], h);
            }
        }

        return 0;
    }

    public void update()
    {
        player.movementHandler();
        for(int i = 0; i < objects.length; i++)
            player.collisionHandler(objects[i].getType(), objects[i].getHitbox());
        player.update();

        //update level dynamic objects
        for(int i = groupsNr + staticObjsNr; i < groupsNr + staticObjsNr + dynamicObjsNr; i++)
        {
            objects[i].update();
        }
    }

    public void render(Graphics g)
    {
        for(int i = 0; i < objects.length; i++)
        {
            objects[i].render(g);
        }

        player.render(g);
    }
}

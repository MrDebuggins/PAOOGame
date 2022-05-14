import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Level
{
    public Player player;
    private Camera camera;
    //private int spawnID = 0;
    private static GameObj spawnPoint = null;

    private static int groupsNr, staticObjsNr, dynamicObjsNr, ringsNr;
    private GameObj[] objects;

    public Level(int id)
    {
        int err = loadLevel(id);
        if(spawnPoint != null)
            player = new Player(spawnPoint.shape.posx + spawnPoint.shape.width, spawnPoint.shape.posy + spawnPoint.shape.height);
        else
        {
            System.out.println("Error loading game objects: No spawn point!");
            spawnPoint = new GameObj(Type.CPOINT, new HitBox(300, 350, 40, 40));
            player = new Player(300, 350);
        }

        if(err != 0)
            System.out.println("Error loading level! code: " + err+".");

        camera = new Camera();
    }

    /**
     * Returns 0 if loaded successfully, -1 if file not found error, 1, 2, 3 or 3 for wrong number of groups,
     * static objects, dynamic objects and rings respectively.
     * @param id level number
     * @return error code
     */
    private int loadLevel(int id)
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
        ringsNr = scn.nextInt();

        objects = new GameObj[groupsNr + staticObjsNr + dynamicObjsNr + ringsNr];

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
        err = loadRings(scn);
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
                type = scn.nextInt();
                objects[i] = new BlockGroup(Type.values()[type], new HitBox(scn.nextInt(), scn.nextInt(), scn.nextInt(), scn.nextInt()));
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
                type = scn.nextInt();
                objects[i] = new GameObj(Type.values()[type], new HitBox(scn.nextInt(), scn.nextInt(), scn.nextInt(), scn.nextInt()));

                if(type == 9)
                {
                    spawnPoint = objects[i];
                }
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
            for(int i = groupsNr + staticObjsNr; i < groupsNr + staticObjsNr + dynamicObjsNr; i++)
            {
                HitBox h = new HitBox();
                h.posx = scn.nextInt();
                h.posy = scn.nextInt();
                h.width = scn.nextInt();
                h.height = scn.nextInt();
                int dist = scn.nextInt();
                int dir = scn.nextInt();
                boolean direction;
                direction = dir != 0;
                objects[i] = new DynamicObj(h, dist, direction);
            }
        }

        return 0;
    }

    private int loadRings(Scanner scn)
    {
        if(ringsNr < 0)
            return 4;
        else
        {
            int type;
            for(int i = groupsNr + staticObjsNr + dynamicObjsNr; i < objects.length; i++)
            {
                type = scn.nextInt();
                objects[i] = new Ring(Type.values()[type], scn.nextInt(), scn.nextInt());
            }
            return 0;
        }
    }

    public void update()
    {
        //update camera
        camera.update(Game.width, Game.height, player,objects);

        player.movementHandler();

        for(int i = 0; i < objects.length; i++) //player collision handling
            objects[i].accept(player);

        player.update();

        //update level dynamic objects
        for(int i = groupsNr + staticObjsNr; i < groupsNr + staticObjsNr + dynamicObjsNr; i++)
            objects[i].update();
    }

    public static void setSpawn(GameObj s)
    {
        spawnPoint.active = true;
        spawnPoint.type = Type.CPOINT;
        spawnPoint = s;
    }
    public static HitBox getSpawn(){return spawnPoint.shape;}

    public static int getRingsNr(){return ringsNr;}

    public void render(Graphics g)
    {
        for(int i = 0; i < objects.length; i++)
            objects[i].render(g);

        player.render(g);

        for(int i = objects.length - ringsNr; i < objects.length; i++)
            objects[i].render(g);
    }
}

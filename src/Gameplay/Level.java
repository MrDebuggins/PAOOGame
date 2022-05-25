package Gameplay;

import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Level
{
    public Player player;
    private Camera camera;
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
            spawnPoint = new GameObj(ObjType.CPOINT, new HitBox(300, 350, 40, 40));
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

        try
        {
            loadGroups(scn);
            loadStatics(scn);
            loadDynamics(scn);
            loadRings(scn);
        }
        catch (InvalidLevelObjecsNums e)
        {
            System.out.println(e.getMessage());
        }

        return 0;
    }

    private void loadGroups(Scanner scn) throws InvalidLevelObjecsNums
    {
        if(groupsNr < 1)
            throw new InvalidLevelObjecsNums(1);
        else
        {
            int type;
            for(int i = 0; i < groupsNr; i++)
            {
                type = scn.nextInt();
                objects[i] = new BlockGroup(ObjType.values()[type], new HitBox(scn.nextInt(), scn.nextInt(), scn.nextInt(), scn.nextInt()));
            }
        }
    }

    private void loadStatics(Scanner scn) throws InvalidLevelObjecsNums
    {
        if(staticObjsNr < 0)
            throw new InvalidLevelObjecsNums(2);
        else
        {
            int type;
            for(int i = groupsNr; i < groupsNr + staticObjsNr; i++)
            {
                type = scn.nextInt();
                objects[i] = new GameObj(ObjType.values()[type], new HitBox(scn.nextInt(), scn.nextInt(), scn.nextInt(), scn.nextInt()));

                if(type == 9)
                {
                    spawnPoint = objects[i];
                }
            }
        }
    }

    private void loadDynamics(Scanner scn) throws InvalidLevelObjecsNums
    {
        if(dynamicObjsNr < 0)
            throw new InvalidLevelObjecsNums(3);
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
    }

    private void loadRings(Scanner scn) throws InvalidLevelObjecsNums
    {
        if(ringsNr < 1)
            throw new InvalidLevelObjecsNums(4);
        else
        {
            int type;
            for(int i = groupsNr + staticObjsNr + dynamicObjsNr; i < objects.length; i++)
            {
                type = scn.nextInt();
                objects[i] = new Ring(ObjType.values()[type], scn.nextInt(), scn.nextInt());
            }
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
        spawnPoint.type = ObjType.CPOINT;
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

    class InvalidLevelObjecsNums extends Exception
    {
        private int err;

        InvalidLevelObjecsNums(int err)
        {
            super("Invalid level parameters.");
            this.err = err;
        }

        @Override
        public String getMessage()
        {
            String msg = super.getMessage();
            switch (err)
            {
                case 1:
                    msg += "Number of block groups can't be less than 1!";
                    break;
                case 2:
                    msg += "Number of static objects can't be less than 0!";
                    break;
                case 3:
                    msg += "Number of dynamic objects can't be less than 0!";
                    break;
                case 4:
                    msg += "Number of rings can't be less than 1!";
                    break;
            }

            return msg;
        }
    }
}

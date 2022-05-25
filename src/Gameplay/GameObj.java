package Gameplay;

import main.TextureManager;
import java.awt.Graphics;

public class GameObj implements Element
{
    protected HitBox shape;
    protected ObjType type = ObjType.DEFAULT;
    protected boolean active = true;

    public GameObj()
    {
        type = ObjType.DEFAULT;
    }

    public GameObj(ObjType type, HitBox h)
    {
        this.type = type;
        shape = h;
    }

    public void render(Graphics g)
    {
        if(active || type == ObjType.CPOINT_CURRENT)
            TextureManager.renderTexture(g, type,shape, 0);
    }

    public void accept(Player p)
    {

        if(active && p.visitGameObj(this))
            collided();
    }

    public void collided()
    {
        active = false;

        if(type == ObjType.CPOINT)
        {
            type = ObjType.CPOINT_CURRENT;
            Level.setSpawn(this);
        }
    }

    public int update(){return 0;}
}
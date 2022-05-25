package Gameplay;

import main.TextureManager;
import java.awt.Graphics;

public class Ring extends GameObj
{
    protected HitBox top, bot;
    private int part = 2;

    Ring(ObjType type, int x, int y)
    {
        this.type = type;

        if(type == ObjType.RING_SMALL)

            shape = new HitBox(x, y, 20, 80);
        else
            shape = new HitBox(x, y, 32, 100);

        top = new HitBox(x + shape.width/4, y, shape.width/2, shape.width/4);
        bot = new HitBox(x + shape.width/4, y + shape.height - shape.width/4, shape.width/2, shape.width/4);
    }

    @Override
    public void accept(Player p) {
        if(p.visitRing(this))
            active = false;
    }

    public void render(Graphics g)
    {
        if(active)
        {
            if(type == ObjType.RING_SMALL)
                TextureManager.renderTexture(g, 11 + part - 1, (int)shape.posx + shape.width*((part-1)/2), (int)shape.posy, (int)shape.width, (int)shape.height);
            else
                TextureManager.renderTexture(g, 15 + part - 1, (int)shape.posx + shape.width*((part-1)/2), (int)shape.posy, (int)shape.width, (int)shape.height);
        }
        else
        {
            if(type == ObjType.RING_SMALL)
                TextureManager.renderTexture(g, 13 + part - 1, (int)shape.posx + shape.width*((part-1)/2), (int)shape.posy, (int)shape.width, (int)shape.height);
            else
                TextureManager.renderTexture(g, 17 + part - 1, (int)shape.posx + shape.width*((part-1)/2), (int)shape.posy, (int)shape.width, (int)shape.height);
        }

        if(part == 1)
            part = 2;
        else
            part = 1;
    }

}

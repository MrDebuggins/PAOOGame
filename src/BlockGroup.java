import java.awt.Graphics;

public class BlockGroup extends GameObj
{
    public BlockGroup(Type type, HitBox h)
    {
        setType(type);
        shape = h;
    }

    @Override public void render(Graphics g)
    {
        if(type == Type.BLOCK || type == Type.BOUNCE_BLOCK)
        {
            HitBox block = new HitBox(0, 0, 40, 40);
            for(int i = (int)shape.posx; i < shape.posx + shape.width; i += 40)
            {
                for(int j = (int)shape.posy; j < shape.posy + shape.height; j += 40)
                {
                    block.posx = i;
                    block.posy = j;
                    TextureManager.renderTexture(g, type, block, 0);
                }
            }
        }else if(type == Type.TRIANGLE)
        {
            HitBox block = new HitBox(0, 0, 40, 40);
            for(int i = (int)shape.posx; i < shape.posx + shape.width; i += 40)
            {
                for(int j = (int)shape.posy + (i - (int)shape.posx); j < shape.posy + shape.height; j += 40)
                {
                    block.posx = i;
                    block.posy = j;
                    if(i - (int)shape.posx == j - (int)shape.posy)
                        TextureManager.renderTexture(g, type, block, 0);
                    else
                        TextureManager.renderTexture(g, Type.BLOCK, block, 0);
                }
            }
        }
        else if(type == Type.RTRIANGLE)
        {
            boolean tri;
            HitBox block = new HitBox(0, 0, 40, 40);
            for(int i = (int)(shape.posx+shape.width-40); i >= shape.posx; i -= 40)
            {
                tri = true;
                for(int j = (int)(shape.posy+(shape.posx+shape.width - i-40)); j < shape.posy+shape.width; j += 40)
                {
                    block.posx = i;
                    block.posy = j;
                    if(tri)
                    {
                        TextureManager.renderTexture(g, type, block, 0);
                        tri = false;
                    }
                    else
                        TextureManager.renderTexture(g, Type.BLOCK, block, 0);
                }
            }
        }
    }

    @Override
    public void accept(Player p)
    {
        if(active)
            p.visitBlockGroup(this);
    }
}

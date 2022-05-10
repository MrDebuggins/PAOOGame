import java.awt.Graphics;

public class BlockGroup extends GameObj
{
    public BlockGroup(Type type, HitBox h)
    {
        this.type = type;
        shape = h;
    }

    @Override public void render(Graphics g)
    {
        int nr = (shape.width * shape.height)/50;

        HitBox block = new HitBox(0, 0, 50, 50);
        for(double i = shape.posx; i < shape.posx + shape.width; i += 50)
        {
            for(double j = shape.posy; j < shape.posy + shape.height; j += 50)
            {
                block.posx = i;
                block.posy = j;
                TextureManager.renderTexture(g, type, block, 0);
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

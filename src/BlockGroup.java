import java.awt.*;

public class BlockGroup extends GameObj
{
    public BlockGroup(Type type, HitBox h)
    {
        setType(type);
        shape = h;

        switch (type)
        {
            case RECTANGLE:
                Toolkit tk = Toolkit.getDefaultToolkit();
                texture = tk.getImage("assets/ground_block.png");
                break;
            default:
                tk = Toolkit.getDefaultToolkit();
                texture = tk.getImage("assets/ground_block.png");
                break;
        }
    }

    @Override public void render(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        for(int i = (int)shape.posx; i < shape.posx + shape.width; i += 40)
        {
            for(int j = (int)shape.posy; j < shape.posy + shape.height; j += 40)
            {
                g2d.drawImage(texture, i, j, 40, 40, null);
            }
        }
    }
}

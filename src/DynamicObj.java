import java.awt.*;

public class DynamicObj extends GameObj
{
    private int distance, passedDist;
    private double speed;
    private boolean direction;

    public DynamicObj()
    {
        type = Type.DAMAGE;
        passedDist = 0;
        Toolkit tk = Toolkit.getDefaultToolkit();
        texture = tk.getImage("assets/dyn_thorn.png");
    }

    @Override
    public void update()
    {
        if(passedDist <= distance)
        {
            if(direction)
            {
                shape.posx += speed;
                passedDist += Math.sqrt(speed * speed);
            }
            else
            {
                shape.posy += speed;
                passedDist += Math.sqrt(speed * speed);
            }
        }
        else
        {
            passedDist = 0;
            speed *= -1;
        }
    }
}

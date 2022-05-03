

public class DynamicObj extends GameObj
{
    private int distance, passedDist;
    private double speed = 2;
    private boolean direction;

    public DynamicObj(HitBox h, int d, boolean dir)
    {
        type = Type.ENEMY;
        shape = h;
        distance = d;
        direction = dir;
        passedDist = 0;
    }

    @Override
    public void update()
    {
        if(passedDist <= distance)
        {
            if(direction)
            {
                shape.posx += speed;
                passedDist += Math.sqrt(speed*speed);
            }
            else
            {
                shape.posy += speed;
                passedDist += Math.sqrt(speed*speed);
            }
        }
        else
        {
            passedDist = 0;
            speed *= -1;
        }
    }
}

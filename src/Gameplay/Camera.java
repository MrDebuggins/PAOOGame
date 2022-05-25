package Gameplay;

public class Camera
{
    public static double xOffset, yOffset;

    Camera()
    {
        xOffset = 0;
        yOffset = 0;
    }

    public void update(int screenW, int screenH, Player p, GameObj objects[])
    {
        if(p.shape.posx + xOffset < screenW*0.25 && p.getXVelocity() <= 0)
        {
            if(p.getXVelocity() != 0)
                xOffset += -p.getXVelocity();
            else
                xOffset += 10;
        }
        else if(p.shape.posx + xOffset > screenW*0.75 && p.getXVelocity() >= 0)
        {
            if(p.getXVelocity() != 0)
                xOffset += -p.getXVelocity();
            else
                xOffset += -10;
        }

        if(p.shape.posy + yOffset < screenH*0.25 && p.getYVelocity() <= 0)
        {
            if(p.getYVelocity() != 0)
                yOffset += -p.getYVelocity();
            else
                yOffset += 10;
        }
        else if(p.shape.posy + yOffset > screenH*0.75 && p.getYVelocity() >= 0)
        {
            if(p.getYVelocity() != 0)
                yOffset += -p.getYVelocity();
            else
                yOffset += -10;
        }
    }
}

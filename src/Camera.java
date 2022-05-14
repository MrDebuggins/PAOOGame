public class Camera
{
    public static double xOffset, yOffset;

    public void update(int screenW, int screenH, Player p, GameObj objects[])
    {
        if(p.shape.posx + xOffset < screenW*0.25 && p.getXVelocity() <= 0)
        {
//            p.shape.posx -= (int)p.getXVelocity();
//            for(int i = 0; i < objects.length; i++)
//                objects[i].shape.posx -= (int)p.getXVelocity();
            if(p.getXVelocity() != 0)
                xOffset += -p.getXVelocity();
            else
                xOffset += 10;
        }
        else if(p.shape.posx + xOffset > screenW*0.75 && p.getXVelocity() >= 0)
        {
//            p.shape.posx -= (int)p.getXVelocity();
//            for(int i = 0; i < objects.length; i++)
//                objects[i].shape.posx -= (int)p.getXVelocity();
            if(p.getXVelocity() != 0)
                xOffset += -p.getXVelocity();
            else
                xOffset += -10;
        }

        if(p.shape.posy + yOffset < screenH*0.25 && p.getYVelocity() <= 0)
        {
//            p.shape.posy -= (int)p.getYVelocity();
//            for(int i = 0; i < objects.length; i++)
//                objects[i].shape.posy -= (int)p.getYVelocity();
            if(p.getYVelocity() != 0)
                yOffset += -p.getYVelocity();
            else
                yOffset += 10;
        }
        else if(p.shape.posy + yOffset > screenH*0.75 && p.getYVelocity() >= 0)
        {
//            p.shape.posy -= (int)p.getYVelocity();
//            for(int i = 0; i < objects.length; i++)
//                objects[i].shape.posy -= (int)p.getYVelocity();
            if(p.getYVelocity() != 0)
                yOffset += -p.getYVelocity();
            else
                yOffset += -10;
        }
    }
}

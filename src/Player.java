import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

public class Player extends GameObj
{
    private static Player player = null;

    private int radius = 20;
    private double rotationAngle = 0;
    private double[] velocity = {0, 0}; //x axis, y axis, rotation angle
    private double gravity = 0.3;
    private boolean isGrounded = false;
    private boolean[] btnStates = {false, false, false};

    private Player()
    {
        shape = new HitBox();
        loadTexture(true);
    }

    public static Player getInstance()
    {
        if(player == null)
            player = new Player();

        return player;
    }

    private void loadTexture(boolean id)
    {
        shape.posx = 292;
        shape.posy = 280;
        shape.width = 40;
        shape.height = 40;

        if(id)
        {
            Toolkit tk = Toolkit.getDefaultToolkit();
            texture = tk.getImage("assets/player_small.png");
        }
        else
        {
            Toolkit tk = Toolkit.getDefaultToolkit();
            texture = tk.getImage("assets/player_big.png");
        }
    }

    @Override
    public void render(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform a = g2d.getTransform();    //save Graphics transform before rotating it
        g2d.rotate(Math.toRadians(rotationAngle*1.5), shape.posx, shape.posy);
        g2d.drawImage(texture, (int)shape.posx-shape.width/2, (int)shape.posy-shape.height/2, 40, 40, null);
        g2d.setTransform(a);    //restore Graphics transform
    }

    public void inputHandler(int eventType, KeyEvent e)
    {
        int k = e.getKeyCode();

        if(eventType == 0)
        {
            if(k == KeyEvent.VK_RIGHT)
                btnStates[0] = true;

            if(k == KeyEvent.VK_LEFT)
                btnStates[1] = true;

            if(k == KeyEvent.VK_UP)
                btnStates[2] = true;
        }
        else
        {
            if(k == KeyEvent.VK_RIGHT)
            {
                btnStates[0] = false;
            }

            if(k == KeyEvent.VK_LEFT)
            {
                btnStates[1] = false;
            }

            if(k == KeyEvent.VK_UP)
                btnStates[2] = false;
        }
    }

    public void movementHandler()
    {
        if(btnStates[0] && velocity[0] < 4)
            velocity[0] += 0.4;

        if(btnStates[1] && velocity[0] > -4)
            velocity[0] -= 0.4;

        if(btnStates[2])
        {
            if(isGrounded)
                velocity[1] = -10;
        }

        isGrounded = false;
    }

    public void collisionHandler(Type type, HitBox h)
    {
//        if(shape.posx >= h.posx && shape.posx <= h.posx + h.width)//vertical collision
//        {
//            if(shape.posy + radius >= h.posy && shape.posy < h.posy)//bottom collision
//            {
//                isGrounded = true;
//                if(velocity[1] > 4)
//                    velocity[1] *= -0.5;
//                else
//                    shape.posy = h.posy - radius;
//            }
//            else if(shape.posy - radius <= h.posy + h.height + 1 && shape.posy > h.posy + h.height && velocity[1] < 0)//top collision
//            {
//                shape.posy = h.posy + h.height + radius;
//                velocity[1] *= -0.2;
//            }
//        }
//        else if(shape.posy >= h.posy && shape.posy <= h.posy + h.height)//sides collision
//        {
//            System.out.println("sides");
//            if(shape.posx + radius >= h.posx - 1 && shape.posx < h.posx && velocity[0] > 0)
//            {
//                velocity[0] = 0;
//                shape.posx = h.posx - radius;
//            }
//            else if(shape.posx - radius <= h.posx + h.width + 1 && shape.posx > h.posx + h.width && velocity[0] < 0)
//            {
//                velocity[0] = 0;
//                shape.posx = h.posx + h.width + radius;
//            }
//        }

        if(!topSide(h))//object sides collisions
            if(!bottomSide(h))
                if(!leftSide(h))
                    if(!rightSide(h))
                    {
                        if(twoPointsDist(shape.posx, shape.posy, h.posx, h.posy) < radius)
                            leftTopCorner(h);
                        else if(twoPointsDist(shape.posx, shape.posy, h.posx + h.width, h.posy) < radius)
                            rightTopCorner(h);
                        else if(twoPointsDist(shape.posx, shape.posy, h.posx, h.posy + h.height) < radius)
                            botLeftCorner(h);
                        else if(twoPointsDist(shape.posx, shape.posy, h.posx + h.width, h.posy + h.height) < radius)
                            botRightCorner(h);
                    }
    }

    private void leftTopCorner(HitBox h)
    {
        if(velocity[1] >= 0)
            isGrounded = true;

        if(!btnStates[0])
        {
            if(velocity[1] > 0)
            {
                velocity[1] *= 0.7;
                velocity[0] = -(velocity[1] + gravity);
            }else
            {
                velocity[1] *= 1.2;
                velocity[0] = -(velocity[1] - gravity);
            }
        }else
        {
            if(velocity[1] > 0)
            {
                velocity[0] = -3;
            }else
            {
                velocity[0] = 0;
                velocity[1] *= 1.1;
            }
        }
    }
    private void rightTopCorner(HitBox h)
    {
        if(velocity[1] >= 0)
            isGrounded = true;

        if(!btnStates[1])
        {
            if(velocity[1] > 0)
            {
                velocity[1] *= 0.7;
                velocity[0] = velocity[1] + gravity;
            }else
            {
                velocity[1] *= 1.2;
                velocity[0] = velocity[1] - gravity;
            }
        }else
        {
            if(velocity[1] > 0)
            {
                velocity[0] = 3;
            }else
            {
                velocity[0] = 0;
                velocity[1] *= 1.1;
            }
        }
    }
    private void botLeftCorner(HitBox h)
    {
        if(velocity[1] < 0)
        {
            velocity[0] = -4;
            velocity[1] *= 0.7;
        }else
        {
            velocity[0] = -0.5;
            velocity[1] *= 1.3;
        }
    }
    private void botRightCorner(HitBox h)
    {
        if(velocity[1] < 0)
        {
            velocity[0] = 4;
            velocity[1] *= 0.7;
        }else
        {
            velocity[0] = 0.5;
            velocity[1] *= 1.3;
        }
    }
    private boolean topSide(HitBox h)
    {
        if(shape.posx >= h.posx && shape.posx <= h.posx + h.width && shape.posy + radius >= h.posy && shape.posy < h.posy)//collision with object top
        {
                isGrounded = true;
                if(velocity[1] > 4)
                    velocity[1] *= -0.5;
                else
                    shape.posy = h.posy - radius;

                return true;
        }else
            return false;
    }
    private boolean bottomSide(HitBox h)
    {
        if(shape.posx >= h.posx && shape.posx <= h.posx + h.width && shape.posy - radius <= h.posy + h.height && shape.posy > h.posy + h.height && velocity[1] < 0)//collision with object bottom
        {
            shape.posy = h.posy + h.height + radius;
            velocity[1] *= -0.2;
            return true;
        }
        return false;
    }
    private boolean leftSide(HitBox h)
    {
        if(shape.posy >= h.posy && shape.posy <= h.posy + h.height && shape.posx + radius >= h.posx - 1 && shape.posx < h.posx && velocity[0] > 0)//left side collision
        {
            velocity[0] = 0;
            shape.posx = h.posx - radius;
            return true;
        }
        return false;
    }
    private boolean rightSide(HitBox h)
    {
        if(shape.posy >= h.posy && shape.posy <= h.posy + h.height && shape.posx - radius <= h.posx + h.width + 1 && shape.posx > h.posx + h.width && velocity[0] < 0)//right side collision
        {
            velocity[0] = 0;
            shape.posx = h.posx + h.width + radius;
            return true;
        }
        return false;
    }

    public void update()
    {
        if(!isGrounded && velocity[1] < 10)
            velocity[1] = velocity[1] + gravity;

        if(!isGrounded || (velocity[1] < 0))
            shape.posy += velocity[1];

        shape.posx += velocity[0];
        if(!btnStates[0] && !btnStates[1] && velocity[0] != 0)
        {
            System.out.println(velocity[0]);
            if(velocity[0] < -0.1 || velocity[0] > 0.1)
            {
                velocity[0] *= 0.9;
            }
            else
            {
                velocity[0] = 0;
            }
        }

        rotationAngle += velocity[0];
    }

    public double twoPointsDist(double x1, double y1, double x2, double y2)
    {
        return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }
}

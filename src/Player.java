import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.time.LocalTime;

public class Player extends GameObj
{
    private static Player player = null;

    private double radius = 20;
    private double rotationAngle = 0;
    private double[] velocity = {0, 0}; //x axis, y axis, rotation angle
    private double gravity = 0.3;

    private boolean isGrounded = false;
    private boolean[] btnStates = {false, false, false};
    private boolean posUpdated = false;

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
                btnStates[0] = false;

            if(k == KeyEvent.VK_LEFT)
                btnStates[1] = false;

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
        if(shape.posx >= h.posx && shape.posx <= h.posx + h.width)//vertical collision
        {
            if(shape.posy + radius >= h.posy && shape.posy < h.posy)//object top collision
            {
                isGrounded = true;
                posUpdated = false;
                if(velocity[1] > 4)
                    velocity[1] *= -0.5;
                else
                    shape.posy = h.posy - radius;
            }
            else if(shape.posy - radius <= h.posy + h.height + 1 && shape.posy > h.posy + h.height && velocity[1] < 0)//object bottom collision
            {
                shape.posy = h.posy + h.height + radius;
                velocity[1] *= -0.6;
                posUpdated = false;
            }
        }
        else if(shape.posy >= h.posy && shape.posy <= h.posy + h.height)//sides collision
        {
            if(shape.posx + radius >= h.posx - 1 && shape.posx < h.posx && velocity[0] > 0)
            {
                velocity[0] = 0;
                shape.posx = h.posx - radius;
                posUpdated = false;
            }
            else if(shape.posx - radius <= h.posx + h.width + 1 && shape.posx > h.posx + h.width && velocity[0] < 0)
            {
                velocity[0] = 0;
                shape.posx = h.posx + h.width + radius;
                posUpdated = false;
            }
        }
        else if(twoPointsDist(shape.posx + velocity[0], shape.posy + velocity[1], h.posx, h.posy) <= radius && !posUpdated)//predict collision in next frame with top-left corner
            predictCollision(h.posx, h.posy);
        else if(twoPointsDist(shape.posx, shape.posy, h.posx, h.posy) <= radius+0.000001)//treat collision with top-left corner
            cornerCollision(h.posx, h.posy);
        else if(twoPointsDist(shape.posx + velocity[0], shape.posy + velocity[1], h.posx + h.width, h.posy) <= radius && !posUpdated)//predict collision with top-right corner
            predictCollision(h.posx + h.width, h.posy);
        else if(twoPointsDist(shape.posx, shape.posy, h.posx + h.width, h.posy) <= radius + 0.000001)//treat collision with top-right corner
            cornerCollision(h.posx + h.width, h.posy);
        else if(twoPointsDist(shape.posx+velocity[0], shape.posy+velocity[1], h.posx, h.posy + h.height) <= radius && !posUpdated)//predict collision with bot-left corner
            predictCollision(h.posx, h.posy+h.height);
        else if(twoPointsDist(shape.posx, shape.posy, h.posx, h.posy + h.height) <= radius + 0.000001)//treat collision with bot-left corner
            cornerCollision(h.posx, h.posy + h.height);
        else if(twoPointsDist(shape.posx+velocity[0], shape.posy+velocity[1], h.posx + h.width, h.posy + h.height) <= radius && !posUpdated)//predict collision with bot-right corner
            predictCollision(h.posx + h.width, h.posy + h.height);
        else if(twoPointsDist(shape.posx, shape.posy, h.posx + h.width, h.posy + h.height) <= radius + 0.000001)//treat collision with bot-right corner
            cornerCollision(h.posx + h.width, h.posy + h.height);
    }

    private void predictCollision(double x, double y)
    {
        double dist = twoPointsDist(shape.posx + velocity[0], shape.posy + velocity[1], x, y);
        double velocityMag = Math.sqrt(velocity[0]*velocity[0] + velocity[1]*velocity[1]);
        double cosa = (velocity[0]*(x-(shape.posx+velocity[0])) + velocity[1]*(y-(shape.posy+velocity[1])))/(velocityMag*dist);
        double a = Math.PI - Math.acos(cosa);
        double b = Math.PI - a - Math.asin((Math.sin(a)*dist)/radius);
        double surplus = radius*Math.sin(b)/Math.sin(a);
        double neededDist = velocityMag - surplus;

        shape.posx += (velocity[0] * neededDist / velocityMag);
        shape.posy += (velocity[1] * neededDist / velocityMag);
        rotationAngle += (velocity[0] * neededDist / velocityMag);
        posUpdated = true;
    }

    private void cornerCollision(double x, double y)
    {
        double vel2 = Math.sqrt(velocity[0]*velocity[0]+velocity[1]*velocity[1]);
        vel2 *= vel2;
        double v0 = x-velocity[0];
        double v1 = y-velocity[1];
        double b = twoPointsDist(v0, v1, shape.posx, shape.posy);
        double d = twoPointsDist(shape.posx, shape.posy, x, y);
        double s = (vel2-b*b-d*d)/(-2*d);
        double coef = s/d;
        double px = shape.posx + coef*(x-shape.posx);
        double py = shape.posy + coef*(y-shape.posy);
        double forcex = px-v0;
        double forcey = py-v1;

        velocity[0] = forcex+forcex;
        velocity[1] = forcey+forcey;


//        x -= c*(x-shape.posx);
//        y -= c*(y-shape.posy);
//        double v0 = x - velocity[0];
//        double v1 = y - velocity[1];
//
//        velocity[0] = (shape.posx - v0) * 2;
//        velocity[1] = (shape.posy - v1) * 2;

        posUpdated = false;
    }

    public void update()
    {
        if(!posUpdated)
        {
            if(!isGrounded && velocity[1] < 10)
                velocity[1] = velocity[1] + gravity;

            if(!isGrounded || (velocity[1] < 0))
                shape.posy += velocity[1];
            else if(!btnStates[2])
                velocity[1] = 0;

            shape.posx += velocity[0];
            if(!btnStates[0] && !btnStates[1] && velocity[0] != 0)
            {
                if(velocity[0] < -0.1 || velocity[0] > 0.1)
                {
                    if(isGrounded)
                        velocity[0] *= 0.9;
                    else
                        velocity[0] *= 0.96;
                }
                else
                {
                    velocity[0] = 0;
                }
            }

            rotationAngle += velocity[0];
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

    public double twoPointsDist(double x1, double y1, double x2, double y2)
    {
        return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }
}

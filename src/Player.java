import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Player extends GameObj implements Visitor
{
    private static Player player = null;

    private double radius = 20;
    private double rotationAngle = 0;
    private double[] velocity = {0, 0}; //x axis, y axis
    private double gravity = 0.3;
    private boolean isGrounded = false;
    private boolean[] btnStates = {false, false, false};
    private boolean posUpdated = false; //is set as true when collision detected in next frame, and is set as false when collision occurs

    Player()
    {
        shape = new HitBox(292, 280, 40, 40);
        type = Type.PLAYER_SMALL;
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
        if(shape.posx >= h.posx && shape.posx <= h.posx + h.width && shape.posy + radius >= h.posy && shape.posy < h.posy)//obj's top collision
        {
            isGrounded = true;
            posUpdated = false;
            if (velocity[1] > 4)
                velocity[1] *= -0.5;
            else
                shape.posy = h.posy - radius;
        }
        else if(shape.posx >= h.posx && shape.posx <= h.posx + h.width && shape.posy - radius <= h.posy + h.height + 1 && shape.posy > h.posy + h.height && velocity[1] < 0)//obj's bottom collision
        {
            shape.posy = h.posy + h.height + radius;
            velocity[1] *= -0.6;
            posUpdated = false;
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
            predictPointCollision(h.posx, h.posy);
        else if(twoPointsDist(shape.posx, shape.posy, h.posx, h.posy) <= radius+0.000001)//treat collision with top-left corner
            pointCollision(h.posx, h.posy);
        else if(twoPointsDist(shape.posx + velocity[0], shape.posy + velocity[1], h.posx + h.width, h.posy) <= radius && !posUpdated)//predict collision with top-right corner
            predictPointCollision(h.posx + h.width, h.posy);
        else if(twoPointsDist(shape.posx, shape.posy, h.posx + h.width, h.posy) <= radius + 0.000001)//treat collision with top-right corner
            pointCollision(h.posx + h.width, h.posy);
        else if(twoPointsDist(shape.posx+velocity[0], shape.posy+velocity[1], h.posx, h.posy + h.height) <= radius && !posUpdated)//predict collision with bot-left corner
            predictPointCollision(h.posx, h.posy+h.height);
        else if(twoPointsDist(shape.posx, shape.posy, h.posx, h.posy + h.height) <= radius + 0.000001)//treat collision with bot-left corner
            pointCollision(h.posx, h.posy + h.height);
        else if(twoPointsDist(shape.posx+velocity[0], shape.posy+velocity[1], h.posx + h.width, h.posy + h.height) <= radius && !posUpdated)//predict collision with bot-right corner
            predictPointCollision(h.posx + h.width, h.posy + h.height);
        else if(twoPointsDist(shape.posx, shape.posy, h.posx + h.width, h.posy + h.height) <= radius + 0.000001)//treat collision with bot-right corner
            pointCollision(h.posx + h.width, h.posy + h.height);
    }

    /**
     * Predict collision in next fram with point(x, y),
     * if collision will occur, player position for next frame
     * is set right in the place for collision.
     * @param x
     * @param y
     */
    private void predictPointCollision(double x, double y)
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

    private void pointCollision(double x, double y)
    {
        double vel2 = Math.sqrt(velocity[0]*velocity[0]+velocity[1]*velocity[1]);
        vel2 *= vel2;
        double v0 = x-velocity[0];
        double v1 = y-velocity[1];
        double b = twoPointsDist(v0, v1, shape.posx, shape.posy);
        double d = twoPointsDist(shape.posx, shape.posy, x, y);
        double s = (b*b+d*d-vel2)/(2*d);
        double coef = s/d;
        double px = shape.posx + coef*(x-shape.posx);
        double py = shape.posy + coef*(y-shape.posy);
        double forcex = 2*(px-v0);
        double forcey = 2*(py-v1);

        velocity[0] = (v0+forcex-x);
        velocity[1] = (v1+forcey-y);

        posUpdated = false;
    }

    public void visitBlockGroup(GameObj o)
    {
        HitBox h = o.getHitbox();

        if(shape.posx >= h.posx && shape.posx <= h.posx + h.width && shape.posy + radius >= h.posy && shape.posy < h.posy && (o.type != Type.TRIANGLE && o.type != Type.RTRIANGLE))//obj's top collision
        {
            isGrounded = true;
            posUpdated = false;
            if (velocity[1] > 4)
                velocity[1] *= -0.5;
            else
                shape.posy = h.posy - radius;
        }
        else if(shape.posx >= h.posx && shape.posx <= h.posx + h.width && shape.posy - radius <= h.posy + h.height + 1 && shape.posy > h.posy + h.height && velocity[1] < 0)//obj's bottom collision
        {
            shape.posy = h.posy + h.height + radius;
            velocity[1] *= -0.6;
            posUpdated = false;
        }
        else if(shape.posy >= h.posy && shape.posy <= h.posy + h.height)//sides collision
        {
            if(shape.posx + radius >= h.posx - 1 && shape.posx < h.posx && velocity[0] > 0 && o.type != Type.RTRIANGLE)
            {
                velocity[0] = 0;
                shape.posx = h.posx - radius;
                posUpdated = false;
            }
            else if(shape.posx - radius <= h.posx + h.width + 1 && shape.posx > h.posx + h.width && velocity[0] < 0 && o.type != Type.TRIANGLE)
            {
                velocity[0] = 0;
                shape.posx = h.posx + h.width + radius;
                posUpdated = false;
            }
        }
        else if(twoPointsDist(shape.posx + velocity[0], shape.posy + velocity[1], h.posx, h.posy) <= radius && !posUpdated)//predict collision in next frame with top-left corner
            predictPointCollision(h.posx, h.posy);
        else if(twoPointsDist(shape.posx, shape.posy, h.posx, h.posy) <= radius+0.000001)//treat collision with top-left corner
            pointCollision(h.posx, h.posy);
        else if(twoPointsDist(shape.posx + velocity[0], shape.posy + velocity[1], h.posx + h.width, h.posy) <= radius && !posUpdated)//predict collision with top-right corner
            predictPointCollision(h.posx + h.width, h.posy);
        else if(twoPointsDist(shape.posx, shape.posy, h.posx + h.width, h.posy) <= radius + 0.000001)//treat collision with top-right corner
            pointCollision(h.posx + h.width, h.posy);
        else if(twoPointsDist(shape.posx+velocity[0], shape.posy+velocity[1], h.posx, h.posy + h.height) <= radius && !posUpdated)//predict collision with bot-left corner
            predictPointCollision(h.posx, h.posy+h.height);
        else if(twoPointsDist(shape.posx, shape.posy, h.posx, h.posy + h.height) <= radius + 0.000001)//treat collision with bot-left corner
            pointCollision(h.posx, h.posy + h.height);
        else if(twoPointsDist(shape.posx+velocity[0], shape.posy+velocity[1], h.posx + h.width, h.posy + h.height) <= radius && !posUpdated)//predict collision with bot-right corner
            predictPointCollision(h.posx + h.width, h.posy + h.height);
        else if(twoPointsDist(shape.posx, shape.posy, h.posx + h.width, h.posy + h.height) <= radius + 0.000001)//treat collision with bot-right corner
            pointCollision(h.posx + h.width, h.posy + h.height);
    }

    public void visitGameObj(GameObj o)
    {
        HitBox h = o.getHitbox();

        if(shape.posx >= h.posx && shape.posx <= h.posx + h.width && shape.posy + radius >= h.posy && shape.posy < h.posy && (o.type != Type.TRIANGLE && o.type != Type.RTRIANGLE))//obj's top collision
        {
            if(o.type == Type.THORN)
                System.out.println("Got damage!");
            else if(o.type == Type.DEFLATTER || o.type == Type.PUMPER)
                switchShape(o.type);
        }
        else if(shape.posx >= h.posx && shape.posx <= h.posx + h.width && shape.posy - radius <= h.posy + h.height + 1 && shape.posy > h.posy + h.height && velocity[1] < 0)//obj's bottom collision
        {
            if(o.type == Type.THORN)
                System.out.println("Got damage!");
            else if(o.type == Type.DEFLATTER || o.type == Type.PUMPER)
                switchShape(o.type);
        }
        else if(shape.posy >= h.posy && shape.posy <= h.posy + h.height)//sides collision
        {
            if(shape.posx + radius >= h.posx - 1 && shape.posx < h.posx && velocity[0] > 0 && o.type != Type.RTRIANGLE)
            {
                if(o.type == Type.THORN)
                    System.out.println("Got damage!");
                else if(o.type == Type.DEFLATTER || o.type == Type.PUMPER)
                    switchShape(o.type);
            }
            else if(shape.posx - radius <= h.posx + h.width + 1 && shape.posx > h.posx + h.width && velocity[0] < 0 && o.type != Type.TRIANGLE)
            {
                if(o.type == Type.THORN)
                    System.out.println("Got damage!");
                else if(o.type == Type.DEFLATTER || o.type == Type.PUMPER)
                    switchShape(o.type);
            }
        }
        else if(twoPointsDist(shape.posx, shape.posy, h.posx, h.posy) <= radius+0.000001)//treat collision with top-left corner
            if(o.type == Type.THORN)
                System.out.println("Got damage!");
            else if(o.type == Type.DEFLATTER || o.type == Type.PUMPER)
                switchShape(o.type);
        else if(twoPointsDist(shape.posx, shape.posy, h.posx + h.width, h.posy) <= radius + 0.000001)//treat collision with top-right corner
            if(o.type == Type.THORN)
                System.out.println("Got damage!");
            else if(o.type == Type.DEFLATTER || o.type == Type.PUMPER)
                switchShape(o.type);
        else if(twoPointsDist(shape.posx, shape.posy, h.posx, h.posy + h.height) <= radius + 0.000001)//treat collision with bot-left corner
            if(o.type == Type.THORN)
                System.out.println("Got damage!");
            else if(o.type == Type.DEFLATTER || o.type == Type.PUMPER)
                switchShape(o.type);
        else if(twoPointsDist(shape.posx, shape.posy, h.posx + h.width, h.posy + h.height) <= radius + 0.000001)//treat collision with bot-right corner
            if(o.type == Type.THORN)
                System.out.println("Got damage!");
            else if(o.type == Type.DEFLATTER || o.type == Type.PUMPER)
                switchShape(o.type);
    }

    public void visitDynamicObj(GameObj o)
    {
        HitBox h = o.getHitbox();

        if(shape.posx >= h.posx && shape.posx <= h.posx + h.width && shape.posy + radius >= h.posy && shape.posy < h.posy && (o.type != Type.TRIANGLE && o.type != Type.RTRIANGLE))//obj's top collision
        {
            System.out.println("Got damage!");
        }
        else if(shape.posx >= h.posx && shape.posx <= h.posx + h.width && shape.posy - radius <= h.posy + h.height + 1 && shape.posy > h.posy + h.height && velocity[1] < 0)//obj's bottom collision
        {
            System.out.println("Got damage!");
        }
        else if(shape.posy >= h.posy && shape.posy <= h.posy + h.height)//sides collision
        {
            if(shape.posx + radius >= h.posx - 1 && shape.posx < h.posx && velocity[0] > 0 && o.type != Type.RTRIANGLE)
            {
                System.out.println("Got damage!");
            }
            else if(shape.posx - radius <= h.posx + h.width + 1 && shape.posx > h.posx + h.width && velocity[0] < 0 && o.type != Type.TRIANGLE)
            {
                System.out.println("Got damage!");
            }
        }
        else if(twoPointsDist(shape.posx + velocity[0], shape.posy + velocity[1], h.posx, h.posy) <= radius && !posUpdated)//predict collision in next frame with top-left corner
            predictPointCollision(h.posx, h.posy);
        else if(twoPointsDist(shape.posx, shape.posy, h.posx, h.posy) <= radius+0.000001)//treat collision with top-left corner
            System.out.println("Got damage!");
        else if(twoPointsDist(shape.posx + velocity[0], shape.posy + velocity[1], h.posx + h.width, h.posy) <= radius && !posUpdated)//predict collision with top-right corner
            predictPointCollision(h.posx + h.width, h.posy);
        else if(twoPointsDist(shape.posx, shape.posy, h.posx + h.width, h.posy) <= radius + 0.000001)//treat collision with top-right corner
            System.out.println("Got damage!");
        else if(twoPointsDist(shape.posx+velocity[0], shape.posy+velocity[1], h.posx, h.posy + h.height) <= radius && !posUpdated)//predict collision with bot-left corner
            predictPointCollision(h.posx, h.posy+h.height);
        else if(twoPointsDist(shape.posx, shape.posy, h.posx, h.posy + h.height) <= radius + 0.000001)//treat collision with bot-left corner
            System.out.println("Got damage!");
        else if(twoPointsDist(shape.posx+velocity[0], shape.posy+velocity[1], h.posx + h.width, h.posy + h.height) <= radius && !posUpdated)//predict collision with bot-right corner
            predictPointCollision(h.posx + h.width, h.posy + h.height);
        else if(twoPointsDist(shape.posx, shape.posy, h.posx + h.width, h.posy + h.height) <= radius + 0.000001)//treat collision with bot-right corner
            System.out.println("Got damage!");
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

    private void switchShape(Type t)
    {
        if(type == Type.PLAYER_SMALL && t == Type.PUMPER)
        {
            type = Type.PLAYER_BIG;
            radius = 30;
            shape.width = 60;
            shape.height = 60;
        }
        else if(type == Type.PLAYER_BIG && t == Type.DEFLATTER)
        {
            type = Type.PLAYER_SMALL;
            radius = 20;
            shape.width = 40;
            shape.height = 40;
        }
    }

    @Override
    public void render(Graphics g)
    {
       TextureManager.renderTexture(g, type, shape, (int)rotationAngle);
    }

    public double twoPointsDist(double x1, double y1, double x2, double y2)
    {
        return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Player extends GameObj implements Visitor
{
    private int lifesCounter = 3;
    private int ringsCounter = 0;
    public JLabel life_t, lifes_c, ring_t, rings_c;

    private double radius = 20;
    private double rotationAngle = 0;
    private double[] velocity = {0, 0}; //x axis, y axis
    private double gravity = 0.3;
    private boolean isGrounded = false;
    private boolean[] btnStates = {false, false, false}; //left, right and up arrows
    private boolean posUpdatedCorner, posUpdatedSurface; //is set as true when collision detected in next frame, and is set as false when collision occurs
    private int a, b = 0; //if you know, keep it a secret

    Player(double x, double y)
    {
        shape = new HitBox(x, y, 40, 40);
        type = Type.PLAYER_SMALL;

        life_t = new JLabel(new ImageIcon("assets/HUD_life.png"));
        lifes_c = new JLabel("x3");
        ring_t = new JLabel(new ImageIcon("assets/HUD_ring.png"));
        rings_c = new JLabel("x0");

        Dimension d = new Dimension(24, 24);

        life_t.setMinimumSize(new Dimension(44, 44));
        life_t.setPreferredSize(new Dimension(44, 44));
        life_t.setMaximumSize(new Dimension(44, 44));
        lifes_c.setMinimumSize(d);
        lifes_c.setPreferredSize(d);
        lifes_c.setMaximumSize(d);
        lifes_c.setFont(new Font("Calibri", Font.BOLD, 24));
        lifes_c.setVerticalAlignment(SwingConstants.TOP);
        ring_t.setMinimumSize(new Dimension(18, 44));
        ring_t.setPreferredSize(new Dimension(18, 44));
        ring_t.setMaximumSize(new Dimension(18, 44));
        rings_c.setMinimumSize(d);
        rings_c.setPreferredSize(d);
        rings_c.setMaximumSize(d);
        rings_c.setFont(new Font("Calibri", Font.BOLD, 24));
        rings_c.setVerticalAlignment(SwingConstants.TOP);
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
    }

    /**
     * Predict collision in next fram with point(x, y),
     * if collision will occur, player position for next frame
     * is set right in the place for collision.
     * @param x
     * @param y
     */
    private void predictPointCollision(Type t, double x, double y)
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

            posUpdatedCorner = true;
    }

    private void pointCollision(Type t, double x, double y)
    {
        if(!posUpdatedSurface)
        {
            double vel2 = Math.sqrt(velocity[0] * velocity[0] + velocity[1] * velocity[1]);
            vel2 *= vel2;
            double v0 = x - velocity[0];
            double v1 = y - velocity[1];
            double b = twoPointsDist(v0, v1, shape.posx, shape.posy);
            double d = twoPointsDist(shape.posx, shape.posy, x, y);
            double s = (b * b + d * d - vel2) / (2 * d);
            double coef = s / d;
            double px = shape.posx + coef * (x - shape.posx);
            double py = shape.posy + coef * (y - shape.posy);
            double forcex = 2 * (px - v0);
            double forcey = 2 * (py - v1);

            velocity[0] = (v0 + forcex - x)*1.1;
            double test = velocity[1];
            velocity[1] = (v1 + forcey - y);

            if(test == 0)
            {
                if(shape.posx < x && velocity[0] > 0 && shape.posy > y)
                {
                    shape.posx += -3;
                    velocity[0] *= -1;
                    //velocity[1] = -1;
                }
                else if(shape.posx > x && velocity[0] < 0 && shape.posy > y)
                {
                    shape.posx += 3;
                    velocity[0] *= -1;
                    //velocity[1] = -1;
                }
            }

            posUpdatedCorner = false;
        }
    }

    public boolean visitBlockGroup(BlockGroup o)
    {
        HitBox h = o.shape;

        if(velocity[1] != 0 && !posUpdatedSurface && !posUpdatedCorner && shape.posx+velocity[0] >= h.posx && shape.posx+velocity[0] <= h.posx + h.width && shape.posy+velocity[1] + radius >= h.posy && shape.posy+velocity[1] < h.posy)//top collision in next frame
        {
            double coef = (h.posy - radius - shape.posy)/velocity[1];
            shape.posy = h.posy - radius;
            shape.posx += velocity[0] * coef;
            posUpdatedSurface = true;
        }
        else if(shape.posx >= h.posx && shape.posx <= h.posx + h.width && shape.posy + radius >= h.posy && shape.posy < h.posy)//obj's top collision
        {
            shape.posy = h.posy - radius;
            isGrounded = true;
            posUpdatedSurface = false;
            if(btnStates[2])
            {
                if(o.type == Type.BOUNCE_BLOCK && velocity[1] > 4)
                {
                    velocity[1] *= -1.2;
                }
                else
                    velocity[1] = -10;
            }
            else if (velocity[1] > 4)
                velocity[1] *= -0.5;
            return true;
        }
        else if(velocity[1] != 0 && !posUpdatedSurface && !posUpdatedCorner && shape.posx+velocity[0] >= h.posx && shape.posx+velocity[0] <= h.posx + h.width && shape.posy+velocity[1] - radius <= h.posy + h.height && shape.posy+velocity[1] > h.posy + h.height && o.type.ordinal() > 1 && o.type.ordinal() < 6)//bottom collision in next frame
        {
            double coef = (shape.posy - radius - h.posy - h.height)/velocity[1];
            shape.posy = h.posy + h.height + radius;
            shape.posx += velocity[0] * coef;
            posUpdatedSurface = true;
        }
        else if(shape.posx >= h.posx && shape.posx <= h.posx + h.width && shape.posy - radius <= h.posy + h.height && shape.posy > h.posy + h.height && o.type.ordinal() > 1 && o.type.ordinal() < 6)//obj's bottom collision
        {
            velocity[1] *= -0.5;
            posUpdatedSurface = false;
            return true;
        }
        else if(shape.posy >= h.posy && shape.posy <= h.posy + h.height)//sides collision
        {
            if(shape.posx + radius >= h.posx - 1 && shape.posx < h.posx && velocity[0] > 0)
            {
                velocity[0] = 0;
                shape.posx = h.posx - radius;
                return true;
            }
            else if(shape.posx - radius <= h.posx + h.width + 1 && shape.posx > h.posx + h.width && velocity[0] < 0)
            {
                velocity[0] = 0;
                shape.posx = h.posx + h.width + radius;
                return true;
            }
        }
        else if(twoPointsDist(shape.posx + velocity[0], shape.posy + velocity[1], h.posx, h.posy) <= radius && !posUpdatedCorner && !posUpdatedSurface)//predict collision in next frame with top-left corner
            predictPointCollision(o.type, h.posx, h.posy);
        else if(twoPointsDist(shape.posx, shape.posy, h.posx, h.posy) <= radius+0.1)//treat collision with top-left corner
        {pointCollision(o.type, h.posx, h.posy); return true;}
        else if(twoPointsDist(shape.posx + velocity[0], shape.posy + velocity[1], h.posx + h.width, h.posy) <= radius && !posUpdatedCorner && !posUpdatedSurface)//predict collision with top-right corner
            predictPointCollision(o.type, h.posx + h.width, h.posy);
        else if(twoPointsDist(shape.posx, shape.posy, h.posx + h.width, h.posy) <= radius + 0.1)//treat collision with top-right corner
        {pointCollision(o.type, h.posx + h.width, h.posy); return true;}
        else if(twoPointsDist(shape.posx+velocity[0], shape.posy+velocity[1], h.posx, h.posy + h.height) <= radius && !posUpdatedCorner && !posUpdatedSurface)//predict collision with bot-left corner
            predictPointCollision(o.type, h.posx, h.posy+h.height);
        else if(twoPointsDist(shape.posx, shape.posy, h.posx, h.posy + h.height) <= radius + 0.1)//treat collision with bot-left corner
        {pointCollision(o.type, h.posx, h.posy + h.height); return true;}
        else if(twoPointsDist(shape.posx+velocity[0], shape.posy+velocity[1], h.posx + h.width, h.posy + h.height) <= radius && !posUpdatedCorner && !posUpdatedSurface)//predict collision with bot-right corner
            predictPointCollision(o.type, h.posx + h.width, h.posy + h.height);
        else if(twoPointsDist(shape.posx, shape.posy, h.posx + h.width, h.posy + h.height) <= radius + 0.1)//treat collision with bot-right corner
        {pointCollision(o.type, h.posx + h.width, h.posy + h.height); return true;}

        return false;
    }

    public boolean visitGameObj(GameObj o)
    {
        HitBox h = o.shape;
        boolean collided = false;

        if(o.type == Type.LIFE && twoPointsDist(shape.posx, shape.posy, h.posx + h.width/2, h.posy + h.height/2) <= radius + h.width/2)
            collided = true;
        else if(shape.posx >= h.posx && shape.posx <= h.posx + h.width && shape.posy + radius >= h.posy && shape.posy < h.posy)//obj's top collision
            collided = true;
        else if(velocity[1] != 0 && shape.posx >= h.posx && shape.posx <= h.posx + h.width && shape.posy - radius <= h.posy + h.height && shape.posy > h.posy + h.height)//obj's bottom collision
            collided = true;
        else if(shape.posy >= h.posy && shape.posy <= h.posy + h.height)//sides collision
        {
            if(shape.posx + radius >= h.posx - 1 && shape.posx < h.posx)
                collided = true;
            else if(shape.posx - radius <= h.posx + h.width + 1 && shape.posx > h.posx + h.width)
                collided = true;
        }
        else if(twoPointsDist(shape.posx, shape.posy, h.posx, h.posy) <= radius+0.000001)//treat collision with top-left corner
            collided = true;
        else if(twoPointsDist(shape.posx, shape.posy, h.posx + h.width, h.posy) <= radius + 0.000001)//treat collision with top-right corner
            collided = true;
        else if(twoPointsDist(shape.posx, shape.posy, h.posx, h.posy + h.height) <= radius + 0.000001)//treat collision with bot-left corner
            collided = true;
        else if(twoPointsDist(shape.posx, shape.posy, h.posx + h.width, h.posy + h.height) <= radius + 0.000001)//treat collision with bot-right corner
            collided = true;

        if(collided)
        {
            switch (o.type)
            {
                case PUMPER, DEFLATTER ->
                {
                    switchShape(o.type);
                    return false;
                }
                case THORN ->
                {
                    getDamage();
                    return false;
                }
                case CPOINT ->
                {
                    return true;
                }
                case LIFE ->
                {
                    lifesCounter++;
                    return true;
                }
            }
        }
        return false;
    }

    public void visitDynamicObj(DynamicObj o)
    {
        HitBox h = o.shape;

        if(shape.posx >= h.posx && shape.posx <= h.posx + h.width && shape.posy + radius >= h.posy && shape.posy < h.posy)//obj's top collision
            getDamage();
        else if(velocity[1] != 0 && shape.posx >= h.posx && shape.posx <= h.posx + h.width && shape.posy - radius <= h.posy + h.height && shape.posy > h.posy + h.height)//obj's bottom collision
            getDamage();
        else if(shape.posy >= h.posy && shape.posy <= h.posy + h.height)//sides collision
        {
            if(shape.posx + radius >= h.posx - 1 && shape.posx < h.posx)
                getDamage();
            else if(shape.posx - radius <= h.posx + h.width + 1 && shape.posx > h.posx + h.width)
                getDamage();
        }
        else if(twoPointsDist(shape.posx, shape.posy, h.posx, h.posy) <= radius+0.000001)//treat collision with top-left corner
            getDamage();
        else if(twoPointsDist(shape.posx, shape.posy, h.posx + h.width, h.posy) <= radius + 0.000001)//treat collision with top-right corner
            getDamage();
        else if(twoPointsDist(shape.posx, shape.posy, h.posx, h.posy + h.height) <= radius + 0.000001)//treat collision with bot-left corner
            getDamage();
        else if(twoPointsDist(shape.posx, shape.posy, h.posx + h.width, h.posy + h.height) <= radius + 0.000001)//treat collision with bot-right corner
            getDamage();
    }

    public boolean visitRing(Ring o)
    {
        HitBox h = o.shape;

        if(type == Type.PLAYER_BIG && o.type == Type.RING_SMALL && shape.posy > h.posy && shape.posy < h.posy + h.height)
        {
                if(velocity[0] > 0 && shape.posx + radius >= h.posx + h.width*0.5 + 3 && shape.posx + radius <= h.posx + h.width)
                {
                    velocity[0] = 0;
                    shape.posx = h.posx + h.width*0.5 - radius + 3;
                    return false;
                }
                else if(velocity[0] < 0 && shape.posx - radius <= h.posx + h.width*0.5 - 3 && shape.posx - radius >= h.posx)
                {
                    velocity[0] = 0;
                    shape.posx = h.posx + h.width*0.5 + radius - 3;
                    return false;
                }
        }
        else
        {
            visitBlockGroup(new BlockGroup(Type.BLOCK, o.top));
            visitBlockGroup(new BlockGroup(Type.BLOCK, o.bot));

            if(o.active && shape.posy > h.posy + radius && shape.posy < h.posy + h.height - radius && Math.abs(shape.posx - h.posx) < 4)
            {
                ringsCounter++;
                return true;
            }
        }

        return false;
    }

    public int update()
    {
        lifes_c.setText(Integer.toString(lifesCounter));
        rings_c.setText(Integer.toString(ringsCounter));

        if(ringsCounter == Level.getRingsNr())
            Main.switchState(5);
        else if(lifesCounter == 0)
            Main.switchState(4);
        else if(posUpdatedCorner)
        {
            if(a > 1)
            {
                posUpdatedCorner = false;
                a = 0;
            }
            else
                a++;
        }
        else if(posUpdatedSurface)
        {
            if(b > 1)
            {
                posUpdatedSurface = false;
                b = 0;
            }
            else
                b++;
        }
        else if(!posUpdatedSurface)
        {
            if(!isGrounded && velocity[1] < 20)
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
        isGrounded = false;

        return 0;
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

    public void getDamage()
    {
        lifesCounter--;

        velocity[0] = 0;
        velocity[1] = 0;

        HitBox h = Level.getSpawn();
        shape.posx = h.posx + h.width/2;
        shape.posy = h.posy + h.height/2;
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

    public double getXVelocity(){return velocity[0];}
    public double getYVelocity(){return velocity[1];}
}
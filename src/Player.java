import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

public class Player extends GameObj
{
    private static Player player = null;

    private int radius = 20;
    private int rotationAngle = 0;
    private double[] velocity = {0, 0, 0}; //x axis, y axis, rotation angle
    private double gravity = 0.3;
    private boolean isGrounded = false;

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

    public void movementHandler(int eventType, KeyEvent e)
    {
        if(eventType == 0)
        {
            switch (e.getKeyCode())
            {
                case KeyEvent.VK_UP:
                    if(isGrounded)
                    {
                        velocity[1] = -10;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    velocity[0] = 4;
                    velocity[2] = 4;
                    break;
                case KeyEvent.VK_LEFT:
                    velocity[0] = -4;
                    velocity[2] = -4;
                    break;
            }
        }
        else
        {
            if((e.getKeyCode() == KeyEvent.VK_RIGHT && velocity[0] > 0) || (e.getKeyCode() == KeyEvent.VK_LEFT && velocity[0] < 0))
            {
                velocity[0] = 0;
                velocity[2] = 0;
            }
        }
    }

    @Override
    public void render(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform a = g2d.getTransform();
        g2d.rotate(Math.toRadians(rotationAngle), shape.posx, shape.posy);
        g2d.drawImage(texture, (int)shape.posx-shape.width/2, (int)shape.posy-shape.height/2, 40, 40, null);
        g2d.setTransform(a);
    }

    public void collisionHandler(Type type, HitBox h)
    {
        if(shape.posx >= h.posx && shape.posx <= h.posx + h.width)//above or below
        {
            boolean tmp = isGrounded;
            isGrounded = false;
            if(shape.posy + radius >= h.posy - 1 && shape.posy < h.posy)
            {
                isGrounded = true;
                if(velocity[1] > 4)
                    velocity[1] *= -0.5;
            }
            else
                isGrounded = tmp;
        }
    }

    public void groundCollision()
    {
        isGrounded = false;
        if(shape.posy+radius >= Game.height-1)
        {
            isGrounded = true;
            if(velocity[1] > 4)
                velocity[1] *= -0.5;
        }
    }

    public void update()
    {
        if(!isGrounded && velocity[1] < 10)
            velocity[1] = velocity[1] + gravity;

        shape.posx += velocity[0];
        if(!isGrounded || (velocity[1] < 0))
            shape.posy += velocity[1];
        rotationAngle += velocity[2];
    }
}

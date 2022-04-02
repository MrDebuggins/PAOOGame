import java.awt.*;

public class GameObj
{
    protected HitBox shape;
    protected Image texture;
    protected Type type = Type.DEFAULT;

    public GameObj()
    {
        type = Type.DEFAULT;
    }

    public GameObj(Type type, HitBox h)
    {
        setType(type);
        shape = h;
    }

    public void render(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(texture, (int)shape.posx, (int)shape.posy, shape.width, shape.height, null);
    }

    public void setType(Type type){if(this.type == Type.DEFAULT){this.type = type;}}
    public Type getType(){return type;}
    public HitBox getHitbox(){return shape;}
    public void update(){}
}

enum Type
{
    DEFAULT,
    RECTANGLE,
    BOUNCE_RECTANGLE,
    TRIANGLE,
    RTRIANGLE,
    WATER,
    RESHAPE,
    DAMAGE,
    CHECKPOINT,
    LIFE,
    RING,
    END_LVL,
}

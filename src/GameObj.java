import java.awt.*;

public class GameObj implements Element
{
    protected HitBox shape;
    protected Type type = Type.DEFAULT;
    protected boolean active = true;

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
        if(active)
            TextureManager.renderTexture(g, type,shape, 0);
    }

    public void accept(Player p)
    {
        if(active)
            p.visitGameObj(this);
    }
    public void collided()
    {

    }

    public void setType(Type type){if(this.type == Type.DEFAULT){this.type = type;}}
    public Type getType(){return type;}
    public HitBox getHitbox(){return shape;}
    public void update(){}
}

enum Type
{
    PLAYER_SMALL,
    PLAYER_BIG,
    BLOCK,
    BOUNCE_BLOCK,
    TRIANGLE,
    RTRIANGLE,
    WATER,
    PUMPER,
    DEFLATTER,
    THORN,
    ENEMY,
    CHECKPOINT1,
    CHECKPOINT2,
    LIFE,
    RING,
    END_LVL,
    DEFAULT,
}

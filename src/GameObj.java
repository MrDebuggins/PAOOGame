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
        this.type = type;
        shape = h;
    }

    public void render(Graphics g)
    {
        if(active || type == Type.CPOINT_CURRENT)
            TextureManager.renderTexture(g, type,shape, 0);
    }

    public void accept(Player p)
    {

        if(active && p.visitGameObj(this))
            collided();
    }

    public void collided()
    {
        active = false;

        if(type == Type.CPOINT)
        {
            type = Type.CPOINT_CURRENT;
            Level.setSpawn(this);
        }
    }

    //public HitBox getHitbox(){return shape;}
    public void update(){}
}

enum Type
{
    PLAYER_SMALL,
    PLAYER_BIG,
    BLOCK,
    BOUNCE_BLOCK,
    PUMPER,
    DEFLATTER,
    THORN,
    ENEMY,
    CPOINT,
    CPOINT_CURRENT,
    LIFE,
    RING_SMALL,
    RING_BIG,
    DEFAULT,
}

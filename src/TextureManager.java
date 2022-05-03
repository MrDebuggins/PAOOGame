import java.awt.*;
import java.awt.geom.AffineTransform;

public class TextureManager
{
    private static Image[] textures = new Image[11];

    TextureManager()
    {
        Toolkit tk = Toolkit.getDefaultToolkit();
        textures[0] = tk.getImage("assets/player_small.png");
        textures[1] = tk.getImage("assets/player_big.png");
        textures[2] = tk.getImage("assets/ground_block.png");
        textures[3] = tk.getImage("assets/bounce.png");
        textures[4] = tk.getImage("assets/triangle.png");
        textures[5] = tk.getImage("assets/rtriangle.png");
        textures[6] = null;
        textures[7] = tk.getImage("assets/pumper.png");
        textures[8] = tk.getImage("assets/deflatter.png");
        textures[9] = tk.getImage("assets/thorn.png");
        textures[10] = tk.getImage("assets/enemy.png");
    }

    /**
     * Draw object's texture with specified HitBox. If it is a player texture,
     * then rot is the rotation angle for texture.
     *  Texture is chosen automatically corresponding to type.
     * @param g Graphics of Component in which to draw
     * @param t type of object needed to draw
     * @param h hitbox of object
     * @param rot rotation angle(used only for player)
     */
    public static void renderTexture(Graphics g, Type t, HitBox h, int rot)
    {
        Graphics2D g2d = (Graphics2D) g;

        if(t == Type.PLAYER_SMALL || t == Type.PLAYER_BIG)
        {
            AffineTransform a = g2d.getTransform();    //save Graphics transform before rotating it
            g2d.rotate(Math.toRadians(rot*1.5), h.posx, h.posy);
            g2d.drawImage(textures[t.ordinal()], (int)h.posx-h.width/2, (int)h.posy-h.height/2, h.width, h.height, null);
            g2d.setTransform(a);
        }
        else
        {
            g2d.drawImage(textures[t.ordinal()], (int)h.posx, (int)h.posy, h.width, h.height, null);
        }
    }
}

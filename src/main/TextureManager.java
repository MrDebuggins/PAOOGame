package main;

import Gameplay.Camera;
import Gameplay.HitBox;
import Gameplay.ObjType;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.geom.AffineTransform;

public class TextureManager
{
    private static Image[] textures = new Image[19];

    public TextureManager()
    {
        Toolkit tk = Toolkit.getDefaultToolkit();
        textures[0] = tk.getImage("assets/player_small.png");
        textures[1] = tk.getImage("assets/player_big.png");
        textures[2] = tk.getImage("assets/ground_block.png");
        textures[3] = tk.getImage("assets/bounce.png");
        textures[4] = tk.getImage("assets/pumper.png");
        textures[5] = tk.getImage("assets/deflatter.png");
        textures[6] = tk.getImage("assets/thorn.png");
        textures[7] = tk.getImage("assets/enemy.png");
        textures[8] = tk.getImage("assets/checkpoint1.png");
        textures[9] = tk.getImage("assets/checkpoint2.png");
        textures[10] = tk.getImage("assets/life.png");
        textures[11] = tk.getImage("assets/ring_small_active1.png");
        textures[12] = tk.getImage("assets/ring_small_active2.png");
        textures[13] = tk.getImage("assets/ring_small_caught1.png");
        textures[14] = tk.getImage("assets/ring_small_caught2.png");
        textures[15] = tk.getImage("assets/ring_big_active1.png");
        textures[16] = tk.getImage("assets/ring_big_active2.png");
        textures[17] = tk.getImage("assets/ring_big_caught1.png");
        textures[18] = tk.getImage("assets/ring_big_caught2.png");
    }

    /**
     * Draw object's texture with specified Game.HitBox. If it is a player texture,
     * then rot is the rotation angle for texture.
     *  Texture is chosen automatically corresponding to type.
     * @param g Graphics of Component in which to draw
     * @param t type of object needed to draw
     * @param h hitbox of object
     * @param rot rotation angle(used only for player)
     */
    public static void renderTexture(Graphics g, ObjType t, HitBox h, int rot)
    {
        Graphics2D g2d = (Graphics2D) g;

        if(t == ObjType.PLAYER_SMALL || t == ObjType.PLAYER_BIG)
        {
            AffineTransform a = g2d.getTransform();    //save Graphics transform before rotating it
            g2d.rotate(Math.toRadians(rot*1.5), h.posx + Camera.xOffset, h.posy + Camera.yOffset);
            g2d.drawImage(textures[t.ordinal()], (int)(h.posx-h.width/2 + Camera.xOffset), (int)(h.posy-h.height/2 + Camera.yOffset), h.width, h.height, null);
            g2d.setTransform(a);
        }
        else
        {
            g2d.drawImage(textures[t.ordinal()], (int)(h.posx + Camera.xOffset), (int)(h.posy + Camera.yOffset), h.width, h.height, null);
        }
    }

    public static void renderTexture(Graphics g, int id, int x, int y, int w, int h)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(textures[id], x + (int)Camera.xOffset, y + (int)Camera.yOffset, w, h, null);
    }
}

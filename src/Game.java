import javax.swing.JPanel;
import java.awt.*;

import java.awt.event.*;
import java.awt.geom.AffineTransform;

public class Game extends JPanel
{
    private Level lvl;
    public TextureManager textureManager;
    public static int width = 784;
    public static int height = 361;

    Game() throws java.awt.HeadlessException
    {
        textureManager = new TextureManager();
        lvl = new Level(1);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                lvl.player.inputHandler(0, e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                lvl.player.inputHandler(1, e);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        lvl.render(g);
    }

    public void render()
    {
        repaint();
    }

    public void update()
    {
        lvl.update();
        width = getWidth();
        height = getHeight();
    }
}

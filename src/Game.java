import javax.swing.JPanel;
import java.awt.Graphics;

import java.awt.event.*;
import java.awt.geom.AffineTransform;

public class Game extends JPanel
{
    AffineTransform at;
    Level lvl;
    public static int width = 784;
    public static int height = 361;

    Game() throws java.awt.HeadlessException
    {
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
                if(Main.lol)
                    System.out.println(Main.lol);
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
    }
}

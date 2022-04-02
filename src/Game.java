import javax.swing.*;
import java.awt.*;

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
                lvl.player.movementHandler(0, e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                lvl.player.movementHandler(1, e);
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

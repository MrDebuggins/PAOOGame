import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

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
            public void keyTyped(KeyEvent e) {if(e.getKeyChar() == 'r')lvl.player.getDamage();}

            @Override
            public void keyPressed(KeyEvent e) {
                lvl.player.inputHandler(0, e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                lvl.player.inputHandler(1, e);
            }
        });

        setLayout(new FlowLayout(FlowLayout.LEFT));

        add(lvl.player.life_t);
        add(lvl.player.lifes_c);
        add(lvl.player.ring_t);
        add(lvl.player.rings_c);
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

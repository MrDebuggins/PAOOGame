import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class Game extends State
{
    public static int lvlID = 0;
    public static Level level;
    public TextureManager textureManager;

    Game() throws java.awt.HeadlessException
    {
        textureManager = new TextureManager();
        level = new Level(lvlID);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {if(e.getKeyChar() == 'r') level.player.getDamage();}

            @Override
            public void keyPressed(KeyEvent e) {
                level.player.inputHandler(0, e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                level.player.inputHandler(1, e);
            }
        });

        setLayout(new FlowLayout(FlowLayout.LEFT));

        add(level.player.life_t);
        add(level.player.lifes_c);
        add(level.player.ring_t);
        add(level.player.rings_c);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        level.render(g);
    }

    public void update()
    {
        level.update();
        width = getWidth();
        height = getHeight();
    }
}

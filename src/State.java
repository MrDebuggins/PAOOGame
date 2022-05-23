import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Image;

abstract public class State extends JPanel
{
    public static int width = 784;
    public static int height = 361;
    protected static Image bg;

    State()
    {
        if(bg == null)
        {
            Toolkit tk = Toolkit.getDefaultToolkit();
            bg = tk.getImage("assets/ground_block.png");
        }
    }

    abstract public void update();

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
    }
}

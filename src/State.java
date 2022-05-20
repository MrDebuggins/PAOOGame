import javax.swing.*;
import java.awt.*;

abstract public class State extends JPanel
{
    public static int width = 784;
    public static int height = 361;

    public void nextState(int c_state, int target_state){}
    abstract public void update();

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
    }
}

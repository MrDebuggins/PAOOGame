import javax.swing.*;

public class Main
{
    public static JFrame f;
    public static Game game;

    public static void main(String[] args)
    {
        f=new JFrame();
        f.setSize(800,400);
        game = new Game();
        game.setSize(f.getWidth(), f.getHeight());
        game.setFocusable(true);
        f.add(game);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

        float currentTime, lastTime = System.nanoTime();
        int FPS = 0;

        while(true)
        {
            currentTime = System.nanoTime();
            if(currentTime - lastTime >= 16000000)
            {
                FPS = (int)(1/((currentTime - lastTime) / 1000000000));
                lastTime = currentTime;

                game.update();
                game.render();
            }
        }
    }
}

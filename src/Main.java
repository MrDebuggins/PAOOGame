import javax.swing.*;

public class Main
{
    public static JFrame f;
    public static Game game;
    public static boolean lol = false;

    public static void main(String[] args)
    {
        f=new JFrame();
        f.setSize(1000,600);
        game = new Game();
        game.setSize(f.getWidth(), f.getHeight());
        game.setFocusable(true);
        f.add(game);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

        float currentTime, lastTime = System.nanoTime();
        float FPS = 0;

        while(true)
        {
            currentTime = System.nanoTime();
            if(currentTime - lastTime >= 16000000)
            {
                lol = true;
                FPS = (1/((currentTime - lastTime) / 1000000000));
                lastTime = currentTime;
                //System.out.println(FPS);

                game.update();
                game.render();
                lol = false;
            }
        }
    }
}

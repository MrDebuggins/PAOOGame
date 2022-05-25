package main;

import Gameplay.Game;
import UI.*;

import javax.swing.JFrame;
import java.awt.Container;

public class Main
{
    public static JFrame f;
    public static int state = 1;
    public static State gameState;
    public static Profile profile;
    public static Game pauseCopy;

    public static void main(String[] args)
    {
        f=new JFrame();
        f.setSize(1000,600);
        Game.lvlID = 1;
        gameState = new MainMenu();
        gameState.setSize(f.getWidth(), f.getHeight());
        gameState.setFocusable(true);

        f.add(gameState);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

        float currentTime, lastTime = System.nanoTime();
        float FPS = 0;

        while(true)
        {
            currentTime = System.nanoTime();
            if(currentTime - lastTime >= 16000000)
            {
                //FPS = (1/((currentTime - lastTime) / 1000000000));
                lastTime = currentTime;

                gameState.update();
                gameState.repaint();
            }
        }
    }

    public static void switchState(int next_state)
    {
        if(next_state == 6)
            pauseCopy = (Game) gameState;
        Container c = f.getContentPane();
        c.removeAll();

        switch (next_state)
        {
            case 0:
                System.exit(0);
            case 1:
                gameState = new MainMenu();
                break;
            case 2:
                gameState = new LvlMenu();
                break;
            case 3:
                if(state == 6 && pauseCopy != null)
                    gameState = pauseCopy;
                else
                    gameState = new Game();
                break;
            case 4:
                gameState = new GameOverMenu();
                break;
            case 5:
                gameState = new EndLvlMenu();
                break;
            case 6:
                gameState = new PauseMenu();
                break;
        }

        c.add(gameState);
        gameState.requestFocusInWindow();
        f.validate();
        f.setVisible(true);

        state = next_state;
    }
}

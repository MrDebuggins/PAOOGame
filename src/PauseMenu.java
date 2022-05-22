import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PauseMenu extends State
{
    private JButton back, restart, mainMenu;

    PauseMenu()
    {
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e)
            {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                {
                    Main.switchState(3);
                }
            }
        });

        back = new JButton("CONTINUE");
        back.setFont(new Font("Calibri", Font.BOLD, 28));
        back.setBounds((int)(width*0.5)-100, (int)(height*0.3), 200, 40);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Main.switchState(3);
            }
        });

        restart = new JButton("RESTART");
        restart.setFont(new Font("Calibri", Font.BOLD, 28));
        restart.setBounds((int)(width*0.5)-100, (int)(height*0.5), 200, 40);
        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Main.pauseCopy = null;
                Main.switchState(3);
            }
        });

        mainMenu = new JButton("MAIN MENU");
        mainMenu.setFont(new Font("Calibri", Font.BOLD, 28));
        mainMenu.setBounds((int)(width*0.5)-100, (int)(height*0.7), 200, 40);
        mainMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Main.pauseCopy = null;
                Main.switchState(1);
            }
        });

        add(back); add(restart); add(mainMenu);
    }

    public void update()
    {
        if(width != getWidth() || height != getHeight())
        {
            width = getWidth();
            height = getHeight();
        }

        back.setBounds((int)(width*0.5)-100, (int)(height*0.3), 200, 40);
        restart.setBounds((int)(width*0.5)-100, (int)(height*0.5), 200, 40);
        mainMenu.setBounds((int)(width*0.5)-100, (int)(height*0.7), 200, 40);
    }
}

package UI;

import main.Main;

import javax.swing.JButton;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PauseMenu extends State
{
    private JButton back, restart, mainMenu;

    public PauseMenu()
    {
        setLayout(null);
        setBackground(Color.cyan);

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
        back.setBounds((int)(State.width*0.5)-100, (int)(State.height*0.3), 200, 40);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Main.switchState(3);
            }
        });

        restart = new JButton("RESTART");
        restart.setFont(new Font("Calibri", Font.BOLD, 28));
        restart.setBounds((int)(State.width*0.5)-100, (int)(State.height*0.5), 200, 40);
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
        mainMenu.setBounds((int)(State.width*0.5)-100, (int)(State.height*0.7), 200, 40);
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

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for(int i = 0; i < State.width; i+=50)
            g2d.drawImage(State.bg, i, State.height-50, 50, 50, null);
    }

    public void update()
    {
        if(State.width != getWidth() || State.height != getHeight())
        {
            State.width = getWidth();
            State.height = getHeight();
        }

        back.setBounds((int)(State.width*0.5)-100, (int)(State.height*0.3), 200, 40);
        restart.setBounds((int)(State.width*0.5)-100, (int)(State.height*0.5), 200, 40);
        mainMenu.setBounds((int)(State.width*0.5)-100, (int)(State.height*0.7), 200, 40);
    }
}

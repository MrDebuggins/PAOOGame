import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameOverMenu extends State
{
    private Image gameOver;
    private JButton restart, mainM;

    GameOverMenu()
    {
        setLayout(null);

        Toolkit tk = Toolkit.getDefaultToolkit();
        gameOver = tk.getImage("assets/pop.png");

        restart = new JButton("RESTART");
        restart.setFont(new Font("Calibri", Font.BOLD, 24));
        restart.setBounds((int)(width*0.2), (int)(height*0.8), 180, 40);
        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //reload lvl
            }
        });
        add(restart);

        mainM = new JButton("MAIN MENU");
        mainM.setFont(new Font("Calibri", Font.BOLD, 24));
        mainM.setBounds((int)(width*0.8)-180, (int)(height*0.8), 180, 40);
        mainM.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //switch state to main menu
            }
        });
        add(mainM);
    }

    public void update()
    {
        if(width != getWidth() || height != getHeight())
        {
            width = getWidth();
            height = getHeight();

            restart.setBounds((int)(width*0.2), (int)(height*0.8), 180, 40);
            mainM.setBounds((int)(width*0.8)-180, (int)(height*0.8), 180, 40);
        }
    }

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g.drawImage(gameOver, (int)(width*0.5)-100, (int)(height*0.2), 200, 200, null);
    }
}
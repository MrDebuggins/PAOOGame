import javax.swing.JButton;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameOverMenu extends State
{
    private Image gameOver;
    private JButton restart, mainM;

    GameOverMenu()
    {
        setLayout(null);
        setBackground(Color.cyan);

        Toolkit tk = Toolkit.getDefaultToolkit();
        gameOver = tk.getImage("assets/pop.png");

        restart = new JButton("RESTART");
        restart.setFont(new Font("Calibri", Font.BOLD, 24));
        restart.setBounds((int)(width*0.2), (int)(height*0.8), 180, 40);
        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Main.switchState(3);
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
                Main.switchState(1);
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
        g2d.drawImage(gameOver, (int)(width*0.5)-100, (int)(height*0.2), 200, 200, null);
        for(int i = 0; i < width; i+=50)
            g2d.drawImage(bg, i, height-50, 50, 50, null);
    }
}

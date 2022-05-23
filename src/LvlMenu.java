import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LvlMenu extends State
{
    private JButton[] lvls;
    private JButton back;

    LvlMenu()
    {
        setLayout(null);
        setBackground(Color.cyan);

        lvls = new JButton[3];

        lvls[0] = new JButton("1");
        lvls[0].setFont(new Font("Calibri", Font.BOLD, 28));
        lvls[0].setBounds((int)(width*0.3), (int)(height*0.2), 50, 50);
        lvls[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Game.lvlID = 1;
                Main.switchState(3);
            }
        });

        if(Main.profile.getLvl() < 2)
            lvls[1] = new JButton(new ImageIcon("assets/locked.png"));
        else
        {
            lvls[1] = new JButton("2");
            lvls[1].setFont(new Font("Calibri", Font.BOLD, 28));
            lvls[1].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    Game.lvlID = 2;
                    Main.switchState(3);
                }
            });
        }
        lvls[1].setBounds((int)(width*0.5)-25, (int)(height*0.2), 50, 50);

        if(Main.profile.getLvl() < 3)
            lvls[2] = new JButton(new ImageIcon("assets/locked.png"));
        else
        {
            lvls[2] = new JButton("3");
            lvls[2].setFont(new Font("Calibri", Font.BOLD, 28));
            lvls[2].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    Game.lvlID = 3;
                    Main.switchState(3);
                }
            });
        }
        lvls[2].setBounds((int)(width*0.7)-50, (int)(height*0.2), 50, 50);

        add(lvls[0]); add(lvls[1]); add(lvls[2]);

        back = new JButton("BACK");
        back.setFont(new Font("Calibri", Font.BOLD, 24));
        back.setBounds((int)(width*0.5)-50, (int)(height*0.8), 100, 40);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Main.switchState(1);
            }
        });

        add(back);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for(int i = 0; i < width; i+=50)
            g2d.drawImage(bg, i, height-50, 50, 50, null);
    }

    public void update()
    {
        if(width != getWidth() || height != getHeight())
        {
            width = getWidth();
            height = getHeight();
        }

            lvls[0].setBounds((int)(width*0.3), (int)(height*0.2), 50, 50);
            lvls[1].setBounds((int)(width*0.5)-25, (int)(height*0.2), 50, 50);
            lvls[2].setBounds((int)(width*0.7)-50, (int)(height*0.2), 50, 50);
            back.setBounds((int)(width*0.5)-50, (int)(height*0.8), 100, 40);
    }
}

package UI;

import Gameplay.Game;
import main.Main;

import javax.swing.JButton;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EndLvlMenu extends State
{
    private Image logo;
    private JButton next = null;
    private JButton mainMenu;

    public EndLvlMenu()
    {
        setLayout(null);
        setBackground(Color.cyan);

        if(Main.profile.getLvl() != 3 && Game.lvlID >= Main.profile.getLvl())
        {
            Main.profile.setLvl(Game.lvlID + 1);

            try
            {
                PreparedStatement pst = MainMenu.profilesDB.prepareStatement("UPDATE Profiles set level = ? WHERE name = ?");
                pst.setInt(1, Main.profile.getLvl());
                pst.setString(2, Main.profile.toString());
                pst.execute();
            }
            catch (SQLException e)
            {
                System.out.println(e.getMessage());
            }
        }

        Game.time = (System.nanoTime() - Game.time)/1000000000;
        Main.profile.setRecord(Game.lvlID - 1, Game.time);
        try
        {
            PreparedStatement pst = MainMenu.profilesDB.prepareStatement("UPDATE Profiles set lvl"+ Game.lvlID+" = ? WHERE name = ?");
            pst.setDouble(1, Main.profile.records[Game.lvlID-1]);
            pst.setString(2, Main.profile.toString());
            pst.execute();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        Toolkit tk = Toolkit.getDefaultToolkit();
        logo = tk.getImage("assets/logo.png");

        if(Game.lvlID != 3)
        {
            next = new JButton("NEXT LEVEL");
            next.setFont(new Font("Calibri", Font.BOLD, 28));
            next.setBounds((int)(State.width*0.2), (int)(State.height*0.8), 200, 40);
            next.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    Game.lvlID++;
                    Main.switchState(3);
                }
            });
            add(next);
        }

        mainMenu = new JButton("MAIN MENU");
        mainMenu.setFont(new Font("Calibri", Font.BOLD, 28));
        mainMenu.setBounds((int)(State.width*0.8)-200, (int)(State.height*0.8), 200, 40);
        mainMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Main.switchState(1);
            }
        });
        add(mainMenu);
    }

    public void update()
    {
        if(State.width != getWidth() || State.height != getHeight())
        {
            State.width = getWidth();
            State.height = getHeight();
        }

        if(next != null)
            next.setBounds((int)(State.width*0.2), (int)(State.height*0.8), 200, 40);

        mainMenu.setBounds((int)(State.width*0.8)-200, (int)(State.height*0.8), 200, 40);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(logo, (int)(State.width*0.5) - 240, 100, 480, 320, null);
        for(int i = 0; i < State.width; i+=50)
            g2d.drawImage(State.bg, i, State.height-50, 50, 50, null);
    }
}

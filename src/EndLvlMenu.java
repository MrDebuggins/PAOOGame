import com.sun.jdi.connect.spi.Connection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EndLvlMenu extends State
{
    private Image logo;
    private JButton next = null;
    private JButton mainMenu;

    EndLvlMenu()
    {


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

        Main.profile.setRecord(Game.lvlID, Game.time);
        try
        {
            PreparedStatement pst = MainMenu.profilesDB.prepareStatement("UPDATE Profiles set lvl"+Game.lvlID+" = ? WHERE name = ?");
            pst.setDouble(1, Main.profile.records[Game.lvlID]);
            pst.setString(2, Main.profile.toString());
            pst.execute();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        setLayout(null);

        Toolkit tk = Toolkit.getDefaultToolkit();
        logo = tk.getImage("assets/logo.png");

        if(Game.lvlID != 3)
        {
            next = new JButton("NEXT LEVEL");
            next.setFont(new Font("Calibri", Font.BOLD, 28));
            next.setBounds((int)(width*0.2), (int)(height*0.8), 200, 40);
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
        mainMenu.setBounds((int)(width*0.8)-200, (int)(height*0.8), 200, 40);
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
        if(width != getWidth() || height != getHeight())
        {
            width = getWidth();
            height = getHeight();
        }

        if(next != null)
            next.setBounds((int)(width*0.2), (int)(height*0.8), 200, 40);

        mainMenu.setBounds((int)(width*0.8)-200, (int)(height*0.8), 200, 40);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(logo, (int)(width*0.5) - 240, 100, 480, 320, null);
    }
}

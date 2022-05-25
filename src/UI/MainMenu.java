package UI;

import main.Main;
import main.Profile;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Objects;

public class MainMenu extends State
{
    private JButton play, exit;
    private JComboBox<Profile> selectProfile;
    private JTextField newGame;
    private JTable scoreboard;
    private DefaultTableModel scores;
    public static Connection profilesDB;
    private int selectedProfileLvl = 0;

    public MainMenu()
    {
        setLayout(null);
        setBackground(Color.cyan);

        connectToDB();

        selectProfile = new JComboBox();
        selectProfile.addItem(new Profile("NEW GAME", 1));
        selectProfile.setFont(new Font("Calibri", Font.BOLD, 24));
        selectProfile.setBounds((int)(State.width*0.1), 50, 300, 40);

        String[] columns = {"Name", "Game.Level 1", "Game.Level 2", "Game.Level 3"};
        Object[][] tst = {{"test", new TimeInSeconds(999), new TimeInSeconds(999), new TimeInSeconds(999)}};
        scores = new DefaultTableModel(tst, columns);
        scoreboard = new JTable(scores);
        scoreboard.setFont(new Font("Calibri", Font.PLAIN, 18));
        scoreboard.setBounds((int)(State.width*0.9)-400, 50, 400, (int)(State.width*0.8)-50);
        scoreboard.setDefaultEditor(Object.class, null);
        scoreboard.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        scoreboard.getColumnModel().getColumn(0).setPreferredWidth(220);
        scoreboard.getColumnModel().getColumn(1).setPreferredWidth(60);
        scoreboard.getColumnModel().getColumn(2).setPreferredWidth(60);
        scoreboard.getColumnModel().getColumn(3).setPreferredWidth(60);
        scoreboard.setRowHeight(30);

        add(scoreboard);

        try
        {
            Statement st = profilesDB.createStatement();
            ResultSet prof;
            prof = st.executeQuery("SELECT * FROM Profiles");

            boolean first = true;
            while (prof.next())
            {
                Profile p = new Profile(prof.getString("name"), prof.getInt("level"));
                p.setRecord(0, prof.getDouble("lvl1"));
                p.setRecord(1, prof.getDouble("lvl2"));
                p.setRecord(2, prof.getDouble("lvl3"));
                selectProfile.addItem(p);

                if(first)
                {
                    scores.setValueAt(new TimeInSeconds(prof.getDouble("lvl1")), 0, 1);
                    scores.setValueAt(new TimeInSeconds(prof.getDouble("lvl2")), 0, 2);
                    scores.setValueAt(new TimeInSeconds(prof.getDouble("lvl3")), 0, 3);
                    first = false;
                }
                else
                {
                    scores.addRow(new Object[]{prof.getString("name"), new TimeInSeconds(prof.getDouble("lvl1")), new TimeInSeconds(prof.getDouble("lvl2")), new TimeInSeconds(prof.getDouble("lvl3"))});
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("Reading from Data Base error: " + e.getMessage());
        }


        add(selectProfile);

        newGame = new JTextField(20);
        newGame.setFont(new Font("Calibri", Font.BOLD, 24));
        newGame.setBounds((int)(State.width*0.1), 200, 300, 40);
        add(newGame);

        play = new JButton("PLAY");
        play.setFont(new Font("Calibri", Font.BOLD, 24));
        play.setBounds(400, 300, 130, 40);
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(Objects.equals(selectProfile.getSelectedItem().toString(), "NEW GAME"))
                {
                    boolean b = false;
                    if(!Objects.equals(newGame.getText(), ""))
                    {
                        try {
                            PreparedStatement pst = profilesDB.prepareStatement("INSERT INTO Profiles(name,level) VALUES(?,?)");
                            pst.setString(1, newGame.getText());
                            pst.setInt(2, 1);
                            pst.execute();
                        }
                        catch (SQLException ex)
                        {
                            for(int i = 0; i < selectProfile.getItemCount(); i++)
                            {
                                if(Objects.equals(newGame.getText(), selectProfile.getItemAt(i).toString()))
                                {
                                    b = true;
                                    break;
                                }
                            }
                        }
                        finally
                        {
                            if(b)
                                newGame.setText("Enter a valid name!");
                            else
                            {
                                Main.profile = new Profile(newGame.getText(), 1);
                                Main.switchState(2);
                            }
                        }
                    }
                    else
                        newGame.setText("Enter a profile name!");
                }
                else
                {
                    Main.profile = (Profile) selectProfile.getSelectedItem();
                    Main.switchState(2);
                }
            }
        });
        add(play);

        exit = new JButton("EXIT");
        exit.setFont(new Font("Calibri", Font.BOLD, 24));
        exit.setBounds(400, 500, 200, 40);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    profilesDB.close();
                }
                catch (SQLException exception)
                {
                    System.out.println(exception.getMessage());
                }
                Main.switchState(0);
            }
        });
        add(exit);

    }

    private void connectToDB()
    {
        try
        {
            profilesDB = DriverManager.getConnection("jdbc:sqlite:DB\\ProfilesDB.db");
            System.out.println("Connected to ProfilesDB.");
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
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

        selectProfile.setBounds((int)(State.width*0.1), 50, 300, 40);
        scoreboard.setBounds((int)(State.width*0.9)-400, 50, 400, (int)(State.height*0.8)-10);
        newGame.setBounds((int)(State.width*0.1), 150, 300, 40);
        play.setBounds((int)(State.width*0.1), (int)(State.height*0.8), 100, 40);
        exit.setBounds((int)(State.width*0.1)+200, (int)(State.height*0.8), 100, 40);
    }

    public class TimeInSeconds
    {
        public double time;

        TimeInSeconds(double t){time = t;}

        public String toString()
        {
            if(time == 0)
                return "  ";
            else
                return new DecimalFormat("#.0# s").format(time);
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Objects;

public class MainMenu extends State
{
    private JButton play, exit;
    private JComboBox<Profile> selectProfile;
    private JTextField newGame;
    private Connection profilesDB;
    private int selectedProfileLvl = 0;

    MainMenu()
    {
        setLayout(null);

        connectToDB();

        selectProfile = new JComboBox();
        selectProfile.addItem(new Profile("NEW GAME", 1));
        selectProfile.setFont(new Font("Calibri", Font.BOLD, 24));
        selectProfile.setBounds(width/2 - 150, 100, 300, 40);

        try
        {
            Statement st = profilesDB.createStatement();
            ResultSet prof;
            prof = st.executeQuery("SELECT * FROM Profiles");
            while (prof.next())
                selectProfile.addItem(new Profile(prof.getString(2), prof.getInt(3)));
        }
        catch (SQLException e)
        {
            System.out.println("Reading from Data Base error: " + e.getMessage());
        }


        add(selectProfile);

        newGame = new JTextField(20);
        newGame.setFont(new Font("Calibri", Font.BOLD, 24));
        newGame.setBounds(width/2 - 150, 200, 300, 40);
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
                            System.out.println("Cannot save new game: " + ex.getMessage());
                        }
                        finally
                        {
                            Main.profile = new Profile(newGame.getText(), 1);
                            Main.switchState(2);
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
                System.exit(0);
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

    public void update()
    {
        if(width != getWidth() || height != getHeight())
        {
            width = getWidth();
            height = getHeight();
        }

            selectProfile.setBounds(width/2 - 150, (int)(height*0.2), 300, 40);
            newGame.setBounds(width/2 - 150, 200, 300, 40);
            play.setBounds((int)(width*0.2), (int)(height*0.8), 100, 40);
            exit.setBounds((int)(width*0.8) - 100, (int)(height*0.8), 100, 40);
    }
}

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;


/**
 * This GUI will display two buttons with options for playing the single player
 * mode
 * or the double player mode.
 * 
 * @author Jonathan Masih, Trevor Collins, Saif Ullah, Seth Coluccio
 * @version Spring 2022
 */
public class StartGame implements ActionListener, Runnable{
    private JButton onePlayer;
    private JButton twoPlayer;
    private static Image backgroundImage;
    private JFrame frame;
    private Clip clip;
    protected static final int  FRAMEWIDTH = 1100;
    protected static final int FRAMEHEIGHT = 850;
    
    /**
     * The run method to set up the graphical user interface
     */
    @Override
    public void run() {
        // create a JFrame in which we will build our very
        // tiny GUI, and give the window a name

        frame = new JFrame("Welcome to Space Invaders!");
        frame.setPreferredSize(new Dimension(FRAMEWIDTH,FRAMEHEIGHT));

        // tell the JFrame that when someone closes the
        // window, the application should terminate
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // JPanel for the buttons to pick the game mode
        JPanel gameModePanel = new JPanel();
        gameModePanel.setLayout(null);
        gameModePanel.setBounds(0, 0, 1000, 850);

        // Setting the background of the frame
        JPanel backGroundPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                // first, we should call the paintComponent method we are
                // overriding in JPanel
                super.paintComponent(g);
                // draw the background
                g.drawImage(backgroundImage, 0 ,  0 , frame.getWidth(), frame.getHeight(), this);
            }
        };
        backGroundPanel.setLayout(new BorderLayout());
        //Plays the background music 
        File audiofile = new File("spaceInvadersMusic.wav");
        try {
            AudioInputStream audioStream =  AudioSystem.getAudioInputStream(audiofile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    

        // to center buttons and selection label, make panel layout manager null
        // and position them in center of the panel with font metrics
        JLabel selection = new JLabel("Please select a game mode.");
        // selection.setPreferredSize(new Dimension(300, 50));
        selection.setFont(new Font("Verdana", Font.PLAIN, 20));
        selection.setForeground(Color.BLUE);
        selection.setBounds(gameModePanel.getWidth() / 2 - 150,
                gameModePanel.getHeight() / 3 - 50, 300, 50);

        onePlayer = new JButton("Single Player");
        onePlayer.setFont(new Font("Verdana", Font.PLAIN, 20));
        onePlayer.setBounds(gameModePanel.getWidth() / 2 - 100,
                gameModePanel.getHeight() / 3, 200, 50);

        twoPlayer = new JButton("Multiplayer");
        twoPlayer.setFont(new Font("Verdana", Font.PLAIN, 20));
        // twoPlayer.setPreferredSize(new Dimension(200, 50));
        twoPlayer.setBounds(gameModePanel.getWidth() / 2 - 100,
                gameModePanel.getHeight() / 3 + 75, 200, 50);

        onePlayer.addActionListener(this);
        twoPlayer.addActionListener(this);

        /*
         * JPanel selectionLabelPanel = new JPanel();
         * selectionLabelPanel.add(selection);
         * selectionLabelPanel.setPreferredSize(new Dimension(300, 50));
         * selectionLabelPanel.setOpaque(false);
         * JPanel oneplayerButtonPanel = new JPanel();
         * oneplayerButtonPanel.setPreferredSize(new Dimension(200, 50));
         * oneplayerButtonPanel.add(onePlayer);
         * oneplayerButtonPanel.setBounds(50, 50, 100, 100);
         * oneplayerButtonPanel.setOpaque(false);
         * JPanel twoPlayerButtonPanel = new JPanel();
         * twoPlayerButtonPanel.setPreferredSize(new Dimension(200, 50));
         * twoPlayerButtonPanel.add(twoPlayer);
         * twoPlayerButtonPanel.setOpaque(false);
         */
        gameModePanel.add(selection);
        gameModePanel.add(onePlayer);
        gameModePanel.add(twoPlayer);

        // makes the gameModePanel transparent
        gameModePanel.setOpaque(false);

    
        // display the window we've created
         // Sets the background of the frame to space image 
         // adds the buttons and message to the frame
        backGroundPanel.add(gameModePanel);
        frame.add(backGroundPanel);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Checks which action is performed and does something
     * depending on the action.
     * 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == onePlayer) {
             //makes a new singleplayer objects and starts it
           SinglePlayer game = new SinglePlayer(frame,backgroundImage ,clip);
           clip.stop();
           game.start();
        }
        if (e.getSource() == twoPlayer) {
            //makes a new doubleplayer objects and starts it
            DoublePlayer game = new DoublePlayer(frame, backgroundImage , clip );
            clip.stop();
           game.start();
        }

    }


    public static void main(String[] args) {
        // "javax.swing.plaf.nimbus.NimbusLookAndFeel")
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
        }
        // loads the image of the player
        Player.loadPlayerPic();
        //loads the image of the enemy
        EnemyPlayer.loadEnemyPic();
        // Load both scaled multiplayer images
        MultiPlayer1.loadPlayerPic();
        MultiPlayer2.loadEnemyPic();
        //loads the image of the alien
        Alien1.loadAlienPic();
        Alien2.loadAlien2Pic();
        Alien3.loadAlien3Pic();



        Toolkit toolkit = Toolkit.getDefaultToolkit();
        backgroundImage = toolkit.getImage("background.gif");
        javax.swing.SwingUtilities.invokeLater(new StartGame());
    }

}

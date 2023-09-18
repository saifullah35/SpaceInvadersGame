import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

/**
 * This setup is for two players to shoot at each other
 * Unlike Single Player, these players move similar to the game Astroids
 *
 * @author Jonathan Masih, Trevor Collins, Saif Ullah, Seth Coluccio, Tyler
 *         Streithorst
 * @version Spring 2022
 */
public class DoublePlayer extends Thread implements KeyListener, ActionListener {
    protected final static int GAME_PANEL_WIDTH = 800;
    protected final static int GAME_PANEL_HEIGHT = 750;
    protected static final int FRAMEWIDTH = 1100;
    protected static final int FRAMEHEIGHT = 850;
    protected static boolean debugMode = false;
    // Only set to false when a player wins, waiting to press 'Restart Game'
    private boolean playingGame = true;
    // amount to the move player on each key press
    protected static final int MOVE_BY = 5;
    // for repaint thread
    private static final int DELAY_TIME = 33;
    private JFrame frame;
    private Image backgroundImage;
    private Clip clip;
    protected static JPanel gamePanel;
    protected JLabel scoresLabel;
    private MultiPlayer1 player1;
    private MultiPlayer2 player2;
    private int player1Score = 0;
    private int player2Score = 0;
    protected static ArrayList<Shield> multiPlayerShieldList;
    private ArrayList<Alien> alienList;
    private boolean gameStarted;
    private JButton currentButton;
    private JComboBox<String> mapSelect;
    private String map; // This will be a string that is only updated when the 'Restart Game' button is
                        // pressed

    // Action booleans for rotating and moving
    private boolean keyPress_W = false;
    private boolean keyPress_A = false;
    private boolean keyPress_S = false;
    private boolean keyPress_D = false;
    private boolean keyPress_UP = false;
    private boolean keyPress_DOWN = false;
    private boolean keyPress_LEFT = false;
    private boolean keyPress_RIGHT = false;

    public DoublePlayer(JFrame frame, Image img, Clip clip) {
        this.frame = frame;
        this.backgroundImage = img;
        this.clip = clip;
        // Creates the players when the game starts
        this.player1 = new MultiPlayer1(new Point(100, MultiPlayer1.PLAYER1YPOS));
        this.player2 = new MultiPlayer2(new Point(630, MultiPlayer2.PLAYER2YPOS));
        // Makes the Arraylist shields
        multiPlayerShieldList = new ArrayList<Shield>();
    }

    /**
     * The run method to set up the graphical user interface
     */
    @Override
    public void run() {
        // Clears the frame from the Main menu and buttons so we can
        // Implement single player mode components
        frame.getContentPane().removeAll();
        frame.setTitle("Welcome to Multiplayer!");
        frame.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()));
        // window, the application should terminate
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Setting the background of the frame
        JPanel backGroundPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                // first, we should call the paintComponent method we are
                // overriding in JPanel
                super.paintComponent(g);
                // draw the background
                g.drawImage(backgroundImage, 0, 0, frame.getWidth(), frame.getHeight(), this);
            }
        };
        backGroundPanel.setLayout(new BorderLayout());

        // Plays the background music
        clip.loop(Clip.LOOP_CONTINUOUSLY);

        // making default sheilds
        multiPlayerShieldList.add(new Shield(new Point(100, Shield.SHIELDPOS - 50)));
        multiPlayerShieldList.add(new Shield(new Point(600, Shield.SHIELDPOS - 50)));
        multiPlayerShieldList.add(new Shield(new Point(100, 150)));
        multiPlayerShieldList.add(new Shield(new Point(600, 150)));

        gamePanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                // first, we should call the paintComponent method we are
                // overriding in JPanel
                super.paintComponent(g);
                g.setColor(Color.WHITE);
                // draws the border for the game
                g.drawRect(0, 0,  gamePanel.getWidth() - 1, GAME_PANEL_HEIGHT);

                // draws the shields, checking for collision
                for (int i = 0; i < multiPlayerShieldList.size(); i++) {
                    Shield s = multiPlayerShieldList.get(i);
                    for (int m = 0; m < MultiPlayer1.player1BulletsList.size(); m++) {
                        Point upperLeftEnemyBullet = MultiPlayer1.player1BulletsList.get(m).getUpperLeft();
                        Point upperLeftShield = s.getShieldUpperLeft();
                        if (Collision.bulletOverlapsObject(upperLeftEnemyBullet.x, upperLeftEnemyBullet.y,
                                Bullet.bulletWidth, Bullet.bulletHeight,
                                upperLeftShield.x, upperLeftShield.y,
                                Shield.SHIELDSIZEW, Shield.SHIELDSIZEL)) {
                            s.hitSheild();
                            MultiPlayer1.player1BulletsList.get(m).bulletHit();
                            MultiPlayer1.player1BulletsList.remove(m);
                        }
                    }
                    for (int n = 0; n < MultiPlayer2.player2BulletsList.size(); n++) {
                        Point upperLeftEnemyBullet = MultiPlayer2.player2BulletsList.get(n).getUpperLeft();
                        Point upperLeftShield = s.getShieldUpperLeft();
                        if (Collision.bulletOverlapsObject(upperLeftEnemyBullet.x, upperLeftEnemyBullet.y,
                                Bullet.bulletWidth, Bullet.bulletHeight,
                                upperLeftShield.x, upperLeftShield.y,
                                Shield.SHIELDSIZEW, Shield.SHIELDSIZEL)) {
                            s.hitSheild();
                            MultiPlayer2.player2BulletsList.get(n).bulletHit();
                            MultiPlayer2.player2BulletsList.remove(n);
                        }
                    }
                    if (s.isSheildBroken()) {
                        multiPlayerShieldList.remove(i);
                        i--;
                    } else {
                        s.paint(g);
                    }
                }

                // draws Players after checking to see if it was hit
                // The calculation uses 5 as the width/height of the hitbox to approximate the
                // trigonometry of the angled bullet
                for (int j = 0; j < MultiPlayer1.player1BulletsList.size(); j++) {
                    Point upperLeftBullet = MultiPlayer1.player1BulletsList.get(j).getUpperLeft();
                    Point P2UpperLeft = player2.getUpperLeft();

                    if (Collision.bulletOverlapsObject(upperLeftBullet.x, upperLeftBullet.y, 5, 5,
                            P2UpperLeft.x, P2UpperLeft.y, Player.PLAYERSIZE, Player.PLAYERSIZE)) {
                        player2.hitPlayer();
                        //player1Score = player1Score + 10;
                        MultiPlayer1.player1BulletsList.get(j).bulletHit();
                        MultiPlayer1.player1BulletsList.remove(j);
                        scoresLabel.setText("Blue: " + player1Score + "  Red: " + player2Score);
                    }
                }
                if (player2.getPlayer2Lives() > 0) {
                    player2.paint(g);
                } else {
                    player1Score = player1Score + 1;
                    if(player1Score == 3) {
                        scoresLabel.setText("Blue Wins");
                        playingGame = false;
                    }
                    else
                        newGame();
                }

                for (int k = 0; k < MultiPlayer2.player2BulletsList.size(); k++) {
                    Point upperLeftBullet = MultiPlayer2.player2BulletsList.get(k).getUpperLeft();
                    Point P1UpperLeft = player1.getUpperLeft();

                    if (Collision.bulletOverlapsObject(upperLeftBullet.x, upperLeftBullet.y, 5, 5,
                            P1UpperLeft.x, P1UpperLeft.y, Player.PLAYERSIZE, Player.PLAYERSIZE)) {
                        player1.hitPlayer();
                        //player2Score = player2Score + 10;
                        MultiPlayer2.player2BulletsList.get(k).bulletHit();
                        MultiPlayer2.player2BulletsList.remove(k);
                        scoresLabel.setText("Blue: " + player1Score + "  Red: " + player2Score);
                    }
                }
                if (player1.getPlayer1Lives() > 0) {
                    player1.paint(g);
                } else {
                    player2Score = player2Score + 1;
                    if(player2Score == 3) {
                        scoresLabel.setText("Red Wins");
                        playingGame = false;
                    }
                    else 
                        newGame();
                }

                // draws the bullets
                // Player 1
                int z = 0;
                while (z < MultiPlayer1.player1BulletsList.size()) {
                    MultiPlayerBullet b = MultiPlayer1.player1BulletsList.get(z);
                    if (b.isOffPanel()) {
                        MultiPlayer1.player1BulletsList.remove(z);
                    } else {
                        b.paint(g);
                        z++;
                    }
                }
                // Player 2
                z = 0;
                while (z < MultiPlayer2.player2BulletsList.size()) {
                    MultiPlayerBullet b = MultiPlayer2.player2BulletsList.get(z);
                    if (b.isOffPanel()) {
                        MultiPlayer2.player2BulletsList.remove(z);
                    } else {
                        b.paint(g);
                        z++;
                    }
                }
            }
        };

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    
                    try {
                        sleep(DELAY_TIME);
                    } catch (InterruptedException e) {
                    }
                    if(playingGame) {
                    // Decrease cooldown
                    player1.cooldown();
                    player2.cooldown();

                    // Rotate Player 1
                    if (keyPress_A)
                        player1.rotate(false); // false for counterclockwise
                    if (keyPress_D)
                        player1.rotate(true); // true for clockwise
                    // Move Player 1
                    if (keyPress_W)
                        player1.modifySpeed(0.1);
                    else if (player1.getSpeed() > 0.1)
                        player1.modifySpeed(-0.05);
                    if (keyPress_S)
                        player1.modifySpeed(-0.1);
                    else if (player1.getSpeed() < -0.1)
                        player1.modifySpeed(0.05);
                    player1.translate(MOVE_BY);

                    // Rotate Player 2
                    if (keyPress_LEFT)
                        player2.rotate(false); // false for counterclockwise
                    if (keyPress_RIGHT)
                        player2.rotate(true); // true for clockwise
                    // Move Player 2
                    if (keyPress_UP)
                        player2.modifySpeed(0.1);
                    else if (player2.getSpeed() > 0.1)
                        player2.modifySpeed(-0.05);
                    if (keyPress_DOWN)
                        player2.modifySpeed(-0.1);
                    else if (player2.getSpeed() < -0.1)
                        player2.modifySpeed(0.05);
                    player2.translate(MOVE_BY);

                    // Finally, Repaint
                    gamePanel.repaint();
                    }
                }
            }
        }.start();
        // sets the size of the game panel
        gamePanel.setPreferredSize(new Dimension(GAME_PANEL_WIDTH, GAME_PANEL_HEIGHT));
        gamePanel.setOpaque(false);


        // scoreboards panel
        JPanel scoreboardPanel = new JPanel();
        scoreboardPanel.setLayout(new BoxLayout(scoreboardPanel, BoxLayout.Y_AXIS));
        scoreboardPanel.setOpaque(false);

        // Space
        scoreboardPanel.add(new JLabel(" "));

        // The updating score JLabel
        scoresLabel = new JLabel("Blue: " + player1Score + "  Red: " + player2Score);
        scoresLabel.setFont(new Font(scoresLabel.getFont().getFontName(), scoresLabel.getFont().getStyle(), 24));
        scoresLabel.setForeground(Color.WHITE);
        scoresLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        scoreboardPanel.add(scoresLabel);
        scoreboardPanel.add(new JLabel(" "));
        scoreboardPanel.add(new JLabel(" "));

        // Add map selector and Jlabel
        JLabel mapLabel = new JLabel("Map Select");
        mapLabel.setForeground(Color.WHITE);
        mapLabel.setFont(new Font(mapLabel.getFont().getFontName(), mapLabel.getFont().getStyle(), 18));
        scoreboardPanel.add(mapLabel);
        mapSelect = new JComboBox<String>();
        mapSelect.addItem("Default");
        mapSelect.addItem("S-Shaped");
        mapSelect.addItem("The Wall");
        mapSelect.addItem("No Shields");
        mapSelect.setSelectedItem("Default");
        mapSelect.setFont(new Font(mapSelect.getFont().getFontName(), mapSelect.getFont().getStyle(), 16));
        mapSelect.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        mapSelect.setMaximumSize(new Dimension(150, 50));
        scoreboardPanel.add(mapSelect);
        map = "Default";

        // Space
        scoreboardPanel.add(new JLabel(" "));
        scoreboardPanel.add(new JLabel(" "));

        // button to restart the game
        currentButton = new JButton("Restart Game");
        currentButton.addActionListener(this);
        currentButton.setFont(new Font(currentButton.getFont().getFontName(), currentButton.getFont().getStyle(), 16));
        scoreboardPanel.add(currentButton);

        // Controls, this includes a lot of spaces to put it nicely at the bottom of the screen
        JLabel controlsLabel = new JLabel("<html><p align=\"center\"><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>Controls<br><br>Blue<br>W, A, S, D to move<br>SPACE to shoot<br><br>Red<br>Arrow Keys to move<br>ENTER to shoot<br><br></p></html>");
        controlsLabel.setForeground(Color.WHITE);
        controlsLabel.setFont(new Font(controlsLabel.getFont().getFontName(), controlsLabel.getFont().getStyle(), 15));
        scoreboardPanel.add(controlsLabel);

        // Add the game and scoreboard to the left and right side of the screen
        backGroundPanel.setLayout(new BorderLayout());
        backGroundPanel.add(gamePanel, BorderLayout.WEST);
        backGroundPanel.add(scoreboardPanel, BorderLayout.EAST);

        frame.add(backGroundPanel);
        // Checks if any key is pressed
        frame.addKeyListener(this);
        // Sets the keyboard focus on this frame
        frame.setFocusable(true);
        frame.requestFocus();
        // display the window we've created
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Sets the booleans to true when a move key is pressed
        // Fires when the appropriate key is pressed
        if (e.getKeyCode() == KeyEvent.VK_W) {
            keyPress_W = true;
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            keyPress_A = true;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            keyPress_S = true;
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            keyPress_D = true;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (player1.getShotCooldown() == 0) {
                player1.fireBullet(gamePanel);
                player1.setCooldown(4);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            keyPress_UP = true;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            keyPress_DOWN = true;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            keyPress_LEFT = true;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            keyPress_RIGHT = true;
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (player1.getShotCooldown() == 0) {
                player2.fireBullet(gamePanel);
                player2.setCooldown(4);
            }
        } else {
            return;
        }
    }

    /*
     * This method will set the boolean for the appropriate key to false so they stop rotating/moving
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            keyPress_W = false;
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            keyPress_A = false;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            keyPress_S = false;
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            keyPress_D = false;
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            keyPress_UP = false;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            keyPress_DOWN = false;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            keyPress_LEFT = false;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            keyPress_RIGHT = false;
        } else {
            return;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    /*
     * This method is only invoked when the 'Restart Game' button is pressed
     * It resets the score to zero, updates the map being played on,
     *  and starts a new game
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        frame.requestFocus();
        map = mapSelect.getSelectedItem().toString();
        player1Score = 0;
        player2Score = 0;
        playingGame = true;
        newGame();
    }

    /*
     * This is the reset method for whenever a ship is knocked out.
     * The scoreboard is updated, all action booleans are set to false, 
     * All bullets are cleared, 
     * and the shields are cleared and re-added based on the map
     */
    public void newGame() {
        // Update Scoreboard if they are in the middle of a game
        if(playingGame)
            scoresLabel.setText("Blue: " + player1Score + "   Red: " + player2Score);
        // Reset all controls booleans
        keyPress_W = false;
        keyPress_A = false;
        keyPress_S = false;
        keyPress_D = false;
        keyPress_UP = false;
        keyPress_DOWN = false;
        keyPress_LEFT = false;
        keyPress_RIGHT = false;
        MultiPlayer1.player1BulletsList.clear();
        MultiPlayer2.player2BulletsList.clear();

        // Clear and re-add the shields based on the selected map
        // Also call reset methods of the players, resetting speed, location, and
        // rotation
        multiPlayerShieldList.clear();
        if (map.equals("Default")) {
            player1.reset(new Point(100, MultiPlayer1.PLAYER1YPOS));
            player2.reset(new Point(630, MultiPlayer2.PLAYER2YPOS));
            multiPlayerShieldList.add(new Shield(new Point(100, Shield.SHIELDPOS - 50)));
            multiPlayerShieldList.add(new Shield(new Point(600, Shield.SHIELDPOS - 50)));
            multiPlayerShieldList.add(new Shield(new Point(100, 150)));
            multiPlayerShieldList.add(new Shield(new Point(600, 150)));
        } else if (map.equals("S-Shaped")) {
            player1.reset(new Point(50, MultiPlayer1.PLAYER1YPOS));
            player2.reset(new Point(680, MultiPlayer2.PLAYER2YPOS));
            multiPlayerShieldList.add(new Shield(new Point(0, Shield.SHIELDPOS - 50)));
            multiPlayerShieldList.add(new Shield(new Point(125, Shield.SHIELDPOS - 50)));
            multiPlayerShieldList.add(new Shield(new Point(250, Shield.SHIELDPOS - 50)));
            multiPlayerShieldList.add(new Shield(new Point(375, Shield.SHIELDPOS - 50)));
            multiPlayerShieldList.add(new Shield(new Point(325, 150)));
            multiPlayerShieldList.add(new Shield(new Point(450, 150)));
            multiPlayerShieldList.add(new Shield(new Point(575, 150)));
            multiPlayerShieldList.add(new Shield(new Point(700, 150)));
        } else if (map.equals("The Wall")) {
            player1.reset(new Point(200, MultiPlayer1.PLAYER1YPOS));
            player2.reset(new Point(530, MultiPlayer2.PLAYER2YPOS));
            multiPlayerShieldList.add(new Shield(new Point(0, 375)));
            multiPlayerShieldList.add(new Shield(new Point(100, 375)));
            multiPlayerShieldList.add(new Shield(new Point(200, 375)));
            multiPlayerShieldList.add(new Shield(new Point(300, 375)));
            multiPlayerShieldList.add(new Shield(new Point(400, 375)));
            multiPlayerShieldList.add(new Shield(new Point(500, 375)));
            multiPlayerShieldList.add(new Shield(new Point(600, 375)));
            multiPlayerShieldList.add(new Shield(new Point(700, 375)));
        } else {
            player1.reset(new Point(100, MultiPlayer1.PLAYER1YPOS));
            player2.reset(new Point(630, MultiPlayer2.PLAYER2YPOS));
        }

        gamePanel.repaint();
    }
}
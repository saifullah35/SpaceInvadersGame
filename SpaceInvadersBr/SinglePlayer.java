import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Flow;

import javax.swing.*;
import javax.swing.border.Border;
import javax.sound.sampled.*;

/**
 * This GUI will be set up to show one player attemoting to shoot aliends and
 * other ships
 * while avoiding their lasers as well.
 * 
 * @author Jonathan Masih, Trevor Collins, Saif Ullah, Seth Coluccio
 * @version Spring 2022
 */
public class SinglePlayer extends Thread implements KeyListener, ActionListener {
    protected final static int GAME_PANEL_WIDTH = 800;
    protected final static int GAME_PANEL_HEIGHT = 850;
    // amount to the move player on each key press
    protected static final int MOVE_BY = 15;
    // for repaint thread
    private static final int DELAY_TIME = 33;
    private JFrame frame;
    private Image backgroundImage;
    private Clip clip;
    protected static JPanel gamePanel;
    private Player player;
    private ArrayList<PlayerBullet> bulletList;
    private ArrayList<Shield> shieldList;
    private ArrayList<Alien> alienList;
    private ArrayList<EnemyPlayer> enemyList;

    // current point of the player
    private static int playerPoints = 0;
    // Jlabel for points
    private JLabel playerPointLabel;
    // Counter to slow down the shooting and stop spaming of player
    private int shootCounter = 5;
    // Keeps track of if the game is over or not.
    private boolean gameOver;
    // button to start and restart the game
    private JButton currentButton;
    private int level = 1;
    private JLabel levelLabel;
    private JLabel currentGameStatus;

    // Arraylist to keep track of players once the program starts
    // and labels for players
    private ArrayList<Player> playersList;
    private ArrayList<JLabel> playerLabels;
    private JLabel playerLivesLeft;
    // User High variable
    private JTextField playerName;
    private boolean playerNameInserted = false;
    private boolean keyPress_A = false;
    private boolean keyPress_D = false;
    private JPanel centerPanelForScoreboardPanel;
    private JPanel highScorePanel = new JPanel();

    public SinglePlayer(JFrame frame, Image img, Clip clip) {
        this.frame = frame;
        this.backgroundImage = img;
        this.clip = clip;
        this.gameOver = false;
        // Creates a player when the game started
        this.player = new Player(new Point(350, Player.PLAYERYPOS));
        // creates the ArrayList<Bullet>
        this.bulletList = new ArrayList<PlayerBullet>();
        // Makes the Arraylist shields
        this.shieldList = new ArrayList<Shield>();
        // Making array list of aliens
        this.alienList = new ArrayList<Alien>();
        // Creating array list of enemy players
        this.enemyList = new ArrayList<EnemyPlayer>();
        this.playersList = new ArrayList<Player>();
        this.playerLabels = new ArrayList<JLabel>();
    }

    /**
     * The run method to set up the graphical user interface
     */
    @Override
    public void run() {
        // Clears the frame from the Main menu and buttons so we can
        // Implement single player mode components
        frame.getContentPane().removeAll();
        frame.setTitle("Welcome to Single Player!");
        frame.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()));
        // window, the application should terminate
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        centerPanelForScoreboardPanel = new JPanel();
        centerPanelForScoreboardPanel.setLayout(new BoxLayout(centerPanelForScoreboardPanel, BoxLayout.Y_AXIS));

        frame.setResizable(true);
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

        // making sheilds
        Shield sheild1 = new Shield(new Point(130, Shield.SHIELDPOS));
        Shield sheild2 = new Shield(new Point(540, Shield.SHIELDPOS));
        shieldList.add(sheild1);
        shieldList.add(sheild2);

        // Making aliens
        alienList.add(new Alien1(new Point(150, Alien.ALIENYPOS1)));
        alienList.add(new Alien1(new Point(350, Alien.ALIENYPOS1)));
        alienList.add(new Alien1(new Point(550, Alien.ALIENYPOS1)));

        alienList.add(new Alien2(new Point(150, Alien.ALIENYPOS2)));
        alienList.add(new Alien2(new Point(350, Alien.ALIENYPOS2)));
        alienList.add(new Alien2(new Point(550, Alien.ALIENYPOS2)));

        alienList.add(new Alien3(new Point(150, Alien.ALIENYPOS3)));
        alienList.add(new Alien3(new Point(350, Alien.ALIENYPOS3)));
        alienList.add(new Alien3(new Point(550, Alien.ALIENYPOS3)));

        for (int k = 0; k < alienList.size(); k++) {
            alienList.get(k).start();
        }

        gamePanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                // first, we should call the paintComponent method we are
                // overriding in JPanel
                super.paintComponent(g);
                g.setColor(Color.WHITE);
                // draws the border for the game
                g.drawRect(0, 0, GAME_PANEL_WIDTH - 2, GAME_PANEL_HEIGHT);
                // draw the player
                player.paint(g);
                // meaning player is dead
                if (player.getPlayerLives() == 0) {
                    gameOver = true;
                    currentGameStatus.setText("Please press Restart Game to restart.");
                    // when the game is over kills all the enemy players so we don't have issues
                    // when we
                    // restart the game.
                   
                }

                // collision between the player and enemybullet
                if (!gameOver) {
                    int l = 0;
                    while (l < EnemyPlayer.enemiesBulletsList.size()) {
                        EnemyBullet bullet = EnemyPlayer.enemiesBulletsList.get(l);
                        Point upperLeftBullet = bullet.getUpperLeft();
                        Point upperLeftPlayer = player.getPlayerUpperLeft();
                        if (Collision.bulletOverlapsObject(upperLeftBullet.x, upperLeftBullet.y,
                                Bullet.bulletWidth, Bullet.bulletHeight,
                                upperLeftPlayer.x, upperLeftPlayer.y,
                                Player.PLAYERSIZE, Player.PLAYERSIZE)) {
                            player.hitPlayer();
                            bullet.bulletHit();
                            playerLivesLeft.setText("PlayerLives: " + Player.playerLives);
                            EnemyPlayer.enemiesBulletsList.remove(l);
                        } else {
                            bullet.paint(g);
                            l++;
                        }
                    }

                    // draws the shields and checks the collsion
                    int i = 0;
                    while (i < shieldList.size()) {
                        Shield s = shieldList.get(i);
                        for (int o = 0; o < EnemyPlayer.enemiesBulletsList.size(); o++) {
                            Point upperLeftEnemyBullet = EnemyPlayer.enemiesBulletsList.get(o).getUpperLeft();
                            Point upperLeftShield = s.getShieldUpperLeft();
                            if (Collision.bulletOverlapsObject(upperLeftEnemyBullet.x, upperLeftEnemyBullet.y,
                                    Bullet.bulletWidth, Bullet.bulletHeight,
                                    upperLeftShield.x, upperLeftShield.y,
                                    Shield.SHIELDSIZEW, Shield.SHIELDSIZEL)) {
                                s.hitSheild();
                                EnemyPlayer.enemiesBulletsList.get(o).bulletHit();
                                EnemyPlayer.enemiesBulletsList.remove(o);
                            }
                        }
                        if (s.isSheildBroken()) {
                            shieldList.remove(i);
                        } else {
                            s.paint(g);
                            i++;
                        }
                    }

                    // draws the aliens
                    int j = 0;
                    while (j < alienList.size()) {
                        Alien a = alienList.get(j);
                        for (int m = 0; m < bulletList.size(); m++) {
                            Point upperLeftBullet = bulletList.get(m).getUpperLeft();
                            Point upperLeftAlien = alienList.get(j).getUpperLeft();
                            if (Collision.bulletOverlapsObject(upperLeftBullet.x, upperLeftBullet.y,
                                    Bullet.bulletWidth, Bullet.bulletHeight,
                                    upperLeftAlien.x, upperLeftAlien.y,
                                    Alien.ALIENSIZE, Alien.ALIENSIZE)) {
                                alienList.get(j).hitAlien();
                                bulletList.get(m).bulletHit();
                                bulletList.remove(m);
                                playerPoints += Alien.point;
                                playerPointLabel.setText("Player Point: " + playerPoints);
                            }
                        }
                        if (a.isAlienHit()) {
                            alienList.remove(j);

                        } else {
                            a.paint(g);
                            j++;
                        }
                    }

                    // draws the EnemyShip and collision
                    int p = 0;
                    while (p < enemyList.size()) {
                        EnemyPlayer e = enemyList.get(p);
                        for (int z = 0; z < bulletList.size(); z++) {
                            Point upperLeftBullet = bulletList.get(z).getUpperLeft();
                            Point upperLeftEnemy = enemyList.get(p).getUpperLeft();
                            if (Collision.bulletOverlapsObject(upperLeftBullet.x, upperLeftBullet.y,
                                    Bullet.bulletWidth, Bullet.bulletHeight,
                                    upperLeftEnemy.x, upperLeftEnemy.y,
                                    Player.PLAYERSIZE, Player.PLAYERSIZE)) {
                                enemyList.get(p).hitEnemy();
                                bulletList.get(z).bulletHit();
                                bulletList.remove(z);
                                playerPoints += EnemyPlayer.point;
                                playerPointLabel.setText("Player Point: " + playerPoints);
                            }
                        }
                        if (e.getEnemyHitCount() == EnemyPlayer.ENEMEYHEALTH) {
                            enemyList.remove(p);

                        } else {
                            e.paint(g);
                            p++;
                        }
                    }
                    // draws the bullets
                    int k = 0;
                    while (k < bulletList.size()) {
                        Bullet b = bulletList.get(k);
                        if (b.isOffPanel()) {
                            bulletList.remove(k);
                        } else {
                            b.paint(g);
                            k++;
                        }
                    }

                    // draws enemy the bullets
                    int q = 0;
                    while (q < EnemyPlayer.enemiesBulletsList.size()) {
                        Bullet b = EnemyPlayer.enemiesBulletsList.get(q);
                        if (b.isOffPanel() || b.isHit()) {
                            EnemyPlayer.enemiesBulletsList.remove(q);
                        } else {
                            b.paint(g);
                            q++;
                        }
                    }
                }
                // After game ends
                if (gameOver) {
                    // When shots run out the button changes to play again
                    currentButton.setText("Restart Game");

                    // If the highscore panel already has 5 players the 6th player will replace
                    // the player with the least amount of points
                    if (playersList.size() == 5 && playerNameInserted == false) {
                        playerNameInserted = true;
                        int leastPoints = playersList.get(0).score;
                        int leastPointIndex = 0;
                        // Varible to see if the current points are greater then any
                        // other points on the highscore board
                        boolean minFound = false;
                        for (int r = 0; r < playersList.size(); r++) {
                            if (playersList.get(r).score < leastPoints && playersList.get(r).score < playerPoints) {
                                leastPoints = playersList.get(r).score;
                                leastPointIndex = r;
                                minFound = true;
                            }
                        }

                        // Replaces the player with the least amount of points.
                        // if there is aleast one score that is less then the current
                        // player score.
                        if (minFound == true) {
                            playersList.set(leastPointIndex, new Player(playerName.getText(), playerPoints));
                            playerLabels.get(leastPointIndex).setText(playerName.getText() + ": " + playerPoints);
                        }

                    } else if (playerNameInserted == false) {
                        playerNameInserted = true;
                        // If there less then 5 players it adds a new player
                        playersList.add(new Player(playerName.getText(), playerPoints));
                        JLabel currentPlayerLabel = new JLabel(playerName.getText() + ": " + playerPoints);
                        currentPlayerLabel.setForeground(Color.WHITE);
                        playerLabels.add(currentPlayerLabel);

                        // adds the players to the highscore panel
                        JPanel playerPanel = new JPanel();
                        playerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
                        playerPanel.setOpaque(false);
                        playerPanel.add(currentPlayerLabel);
                        highScorePanel.add(playerPanel);
                        highScorePanel.revalidate();
                    }

                    // Writes the current high score board to the file or updates it
                    // if it has 5 players already.
                    // try {
                    // FileWriter myWriter = new FileWriter("GameResults.txt");
                    // for (int w = 0; w < playerLabels.size(); w++) {
                    // myWriter.write(playerLabels.get(w).getText() + "\n");

                    // }
                    // myWriter.close();
                    // } catch (IOException e1) {
                    // System.out.println("An error occurred.");
                    // e1.printStackTrace();
                    // }
                }

            }
        };

        // Making an enemies
        EnemyPlayer enemy = new EnemyPlayer(gamePanel, new Point(100, EnemyPlayer.ENEMYPLAYERYPOS));
        enemy.start();
        enemyList.add(enemy);

        // sets the size of the game panel
        gamePanel.setPreferredSize(new Dimension(GAME_PANEL_WIDTH, GAME_PANEL_HEIGHT));
        gamePanel.setOpaque(false);

        currentGameStatus = new JLabel("<html>Space to shoot" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                "<br>" +
                "A and D to move</html>");
        currentGameStatus.setFont(new Font("Serif", Font.PLAIN, 20));
        currentGameStatus.setForeground(Color.WHITE);
        // scoreboards panel
        JPanel scoreboardPanel = new JPanel(new BorderLayout());
        // scoreboardPanel.setLayout(new BoxLayout( scoreboardPanel ,BoxLayout.Y_AXIS));

        scoreboardPanel.setOpaque(false);
        scoreboardPanel
                .setPreferredSize(new Dimension(StartGame.FRAMEWIDTH - GAME_PANEL_WIDTH - 50, 500));
        levelLabel = new JLabel("Level: " + level);
        levelLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        levelLabel.setForeground(Color.WHITE);
        playerPointLabel = new JLabel("Player Point: " + playerPoints);
        playerPointLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        playerPointLabel.setForeground(Color.WHITE);
        playerLivesLeft = new JLabel("PlayerLives: " + Player.playerLives);
        playerLivesLeft.setFont(new Font("Serif", Font.PLAIN, 20));
        playerLivesLeft.setForeground(Color.WHITE);

        centerPanelForScoreboardPanel.add(currentGameStatus);
        centerPanelForScoreboardPanel.add(playerLivesLeft);
        centerPanelForScoreboardPanel.add(playerPointLabel);
        centerPanelForScoreboardPanel.add(levelLabel);
        centerPanelForScoreboardPanel.setOpaque(false);
        // button to start the game and restart game

        currentButton = new JButton("Restart Game");
        currentButton.setSize(100, 100);
        currentButton.addActionListener(this);
        centerPanelForScoreboardPanel.add(currentButton);
        // the playerName (required field)

        // the customer's playerName (required field)
        JPanel playerNamePanel = new JPanel();
        JLabel playerNameLabel = new JLabel("Player Name: ");
        playerNameLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        playerNameLabel.setForeground(Color.WHITE);
        playerNamePanel.add(playerNameLabel);
        playerName = new JTextField("", 5);
        playerNamePanel.add(playerName);
        playerNamePanel.setOpaque(false);
        highScorePanel.setOpaque(false);
        highScorePanel.setLayout(new BoxLayout(highScorePanel, BoxLayout.Y_AXIS));
        highScorePanel.add(playerNamePanel);

        JPanel highScoreCenter = new JPanel();
        highScoreCenter.setOpaque(false);
        highScoreCenter.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel highScoreLabel = new JLabel("HIGH SCORES:");
        highScoreLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        highScoreLabel.setForeground(Color.WHITE);
        highScoreCenter.add(highScoreLabel);
        highScorePanel.add(highScoreCenter);
        // High Score
        JPanel scoreAndHighCenter = new JPanel();
        scoreAndHighCenter.setLayout(new FlowLayout(FlowLayout.CENTER));
        scoreAndHighCenter.add(highScorePanel);
        scoreAndHighCenter.setOpaque(false);
        scoreAndHighCenter.setPreferredSize(new Dimension(185, scoreAndHighCenter.getHeight()));

        JPanel masterPanelForScoresAndButtons = new JPanel();
        masterPanelForScoresAndButtons.setLayout(new BoxLayout(masterPanelForScoresAndButtons, BoxLayout.Y_AXIS));
        masterPanelForScoresAndButtons.setOpaque(false);
        scoreAndHighCenter.setOpaque(false);
        masterPanelForScoresAndButtons.add(centerPanelForScoreboardPanel);
        masterPanelForScoresAndButtons.add(scoreAndHighCenter);

        scoreboardPanel.add(masterPanelForScoresAndButtons);

        backGroundPanel.add(gamePanel, BorderLayout.WEST);
        backGroundPanel.add(scoreboardPanel, BorderLayout.EAST);

        // putting in JTextField action listener to it
        playerName.addActionListener(this);

        frame.add(backGroundPanel);
        // Checks if any key is pressed
        frame.addKeyListener(this);
        // Sets the keyboard focus on this frame
        frame.setFocusable(true);
        frame.requestFocus();
        // display the window we've created
        frame.pack();
        frame.setVisible(true);
        // construct and start a thread that will live as long as the program remains
        // active to call gamePanel.repaint() about 30 times per second, so individual
        // game objects do not need to do so.
        new Thread() {

            @Override
            public void run() {
                while (true) {
                    try {
                        sleep(DELAY_TIME);
                    } catch (InterruptedException e) {
                    }
                    if (enemyList.isEmpty() && alienList.isEmpty()) {
                        nextLevel();
                    }
                    // decrement so the player can shoot
                    if (shootCounter > 0) {
                        shootCounter--;
                    }
                    if (keyPress_A) {
                        player.translate(-MOVE_BY);
                    }
                    if (keyPress_D) {
                        player.translate(MOVE_BY);
                    }
                 
                    // for (int k = 0; k < alienList.size(); k++) {
                    //     for(int i = 0; i < alienList.size(); i++ ){
                    //         if(k == i){
                    //             break;
                    //         }
                    //         if(alienList.get(k).upperLeftOfAlien.x - alienList.get(i).upperLeftOfAlien.x < 200){
                    //             alienList.get(k).upperLeftOfAlien.x +=   alienList.get(k).upperLeftOfAlien.x + 200;
                    //         }
                    
                    //     }
                    // }
                    gamePanel.repaint();

                }
            }
        }.start();
    }

    /**
     * Method called when the player has killed all the aliens and
     * enemey ships to move the player into the next life if the player
     * is still alive
     * 
     */
    public void nextLevel() {
        level++;
        // Clear the bullet lists.
        bulletList.clear();
        EnemyPlayer.enemiesBulletsList.clear();

        levelLabel.setText("Level: " + level);
        if (level % 2 == 0) {
            shieldList.clear();
            // making sheilds
            Shield sheild1 = new Shield(new Point(130, Shield.SHIELDPOS));
            Shield sheild2 = new Shield(new Point(540, Shield.SHIELDPOS));
            shieldList.add(sheild1);
            shieldList.add(sheild2);
        }
        Alien.point += 10;
     
        if (EnemyPlayer.fireRate  > 21) {
            EnemyPlayer.fireRate -= 10;
        }else if (EnemyPlayer.fireRate <= 31 && EnemyPlayer.fireRate >=  21) {
            EnemyPlayer.fireRate -= 5;
        }
        else if( EnemyPlayer.fireRate < 21 &&  EnemyPlayer.fireRate >= 10 ) {
            EnemyPlayer.fireRate -= 2;
        }else if ( EnemyPlayer.fireRate < 5){
            EnemyPlayer.fireRate -= 1;
        }

        for (int i = 0; i < level; i++) {
            EnemyPlayer enemy = new EnemyPlayer(gamePanel, new Point(100, EnemyPlayer.ENEMYPLAYERYPOS));
            enemy.start();
            enemyList.add(enemy);
        }
        // Making aliens for each new level and increasing their point value
        Alien.point += 10;
        alienList.add(new Alien1(new Point(150, Alien.ALIENYPOS1)));
        alienList.add(new Alien1(new Point(350, Alien.ALIENYPOS1)));
        alienList.add(new Alien1(new Point(550, Alien.ALIENYPOS1)));

        alienList.add(new Alien2(new Point(150, Alien.ALIENYPOS2)));
        alienList.add(new Alien2(new Point(350, Alien.ALIENYPOS2)));
        alienList.add(new Alien2(new Point(550, Alien.ALIENYPOS2)));

        alienList.add(new Alien3(new Point(150, Alien.ALIENYPOS3)));
        alienList.add(new Alien3(new Point(350, Alien.ALIENYPOS3)));
        alienList.add(new Alien3(new Point(550, Alien.ALIENYPOS3)));

        Alien.movementSpeedx += 1;

        for (int k = 0; k < alienList.size(); k++) {
            alienList.get(k).start();
        }

    }

    /**
     * Restarts the game.
     * 
     */
    public void restartGame() {
        for (int i = 0; i < EnemyPlayer.enemiesBulletsList.size(); i++) {
            EnemyPlayer.enemiesBulletsList.get(i).bulletHit();
        }
        EnemyPlayer.enemiesBulletsList.clear();
        playerNameInserted = false;
        gameOver = false;
        level = 1;
        levelLabel.setText("Level: " + level);
        playerPoints = 0;
        playerPointLabel.setText("Player Point: " + playerPoints);
        alienList.clear();
        bulletList.clear();
        shieldList.clear();

        EnemyPlayer.fireRate = 51;

        Player.playerLives = 5;
        playerLivesLeft.setText("PlayerLives: " + Player.playerLives);
        for (int i = 0; i < enemyList.size(); i++) {
            enemyList.get(i).killEnemyPlayer();
        }
        enemyList.clear();

        // player = new Player(playerName.getText(), 0);

        currentGameStatus.setText("Space to shoot, A and D to move.");

        // Re draw the aliens, enemies. shield, and player
        alienList.add(new Alien1(new Point(150, Alien.ALIENYPOS1)));
        alienList.add(new Alien1(new Point(350, Alien.ALIENYPOS1)));
        alienList.add(new Alien1(new Point(550, Alien.ALIENYPOS1)));

        alienList.add(new Alien2(new Point(150, Alien.ALIENYPOS2)));
        alienList.add(new Alien2(new Point(350, Alien.ALIENYPOS2)));
        alienList.add(new Alien2(new Point(550, Alien.ALIENYPOS2)));

        alienList.add(new Alien3(new Point(150, Alien.ALIENYPOS3)));
        alienList.add(new Alien3(new Point(350, Alien.ALIENYPOS3)));
        alienList.add(new Alien3(new Point(550, Alien.ALIENYPOS3)));



        for (int k = 0; k < alienList.size(); k++) {
            alienList.get(k).start();
        }

        Shield sheild1 = new Shield(new Point(130, Shield.SHIELDPOS));
        Shield sheild2 = new Shield(new Point(540, Shield.SHIELDPOS));
        shieldList.add(sheild1);
        shieldList.add(sheild2);

        EnemyPlayer enemy = new EnemyPlayer(gamePanel, new Point(100, EnemyPlayer.ENEMYPLAYERYPOS));
        enemy.start();
        enemyList.add(enemy);

        frame.setFocusable(true);
        frame.requestFocus();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver) {
            Point currentPosPlayer1 = player.getPlayerCenter();
            // Moves the player depending on which button is pressed
            if (e.getKeyCode() == KeyEvent.VK_A) {
                keyPress_A = true;
            } else if (e.getKeyCode() == KeyEvent.VK_D) {
                keyPress_D = true;
            } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                if (shootCounter == 0) {
                    PlayerBullet bullet = new PlayerBullet(gamePanel, currentPosPlayer1);
                    bullet.start();
                    bulletList.add(bullet);
                    shootCounter = 5;
                }
                return;
            } else {
                return;
            }
        } else {
            return;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A) {
            keyPress_A = false;
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            keyPress_D = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playerName) {
            frame.setFocusable(true);
            frame.requestFocus();
        } else {
            JButton temp = (JButton) e.getSource();
            if (temp.getText().equals("Restart Game")) {
                restartGame();
            }

        }

    }

}
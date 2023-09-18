

1. To start the game, you go to git bash and compile all the classes using ‘javac *.java’. Then to start the game, you would type in ‘java StartGame’. 
This command will bring you to the game screen and a single user would have to play the single player mode and play the game. 
Two users would play the multiplayer mode to see who is the best player between them out of five rounds.

2. We included object oriented designs such as aliens, bullets, enemies, players, and shields. We also used an abstract class for bullets to make player bullets 
and enemy bullets. We used inheritance to create one base bullet class that is extended by the enemyBullet class. We have one base alien class with three 
different types of aliens that extends the alien class. We included event driven programming since we utilized buttons and keyboards that triggered events. 
We used the bullet class to animate the movement of the bullet, the alien class to animate the movement of the alien, and enemy player to animate its movement. 
We used Arraylists to dynamically allocate the size of the list for the bullets, aliens, and the enemy player. 
We incorporated music for the game, rotation of a buffered image, and made use of a graphics 2D for its rotate method for buffered images.

3. We divided up into two teams: one team (Saif, Jonathan, and Trevor) worked on the SinglePlayer class and the other team (Seth and Tyler) 
worked on the double player class. We texted each other when we committed code so we don’t run into merge conflicts.

4. We started the project together and wrote down the basic programs to initially start the game. Then we split up into two teams to finish the program efficiently.

Tyler:
	Early on in the project, my contributions were mostly bug fixing. When we split into groups to work on Single Player and Multiplayer, I worked on the rotation 
  and movement logic for the players. This involved relearning trigonometry and I also used that for the MultiPlayerBullet class. I also helped Seth with 
  the multiplayer scoreboard and with creating other maps/setups to play. 

Saif: 
	During the beginning of the project, I helped write code for the SinglePlayer class, alien classes, shield class, scoreboard for SinglePlayer. 
  I helped write code for the placement of the shields, aliens, player, and scoreboard gave us problems as well as the speed of the bullets, aliens, 
  and player since. I helped format the scoreboard and positioning it as well. Another issue was checking for the top 5 scores and writing the names 
  of players. Our group worked together by one person typing the code while the other two were looking for compiling errors and bugs. We would alternate 
  writing the code. I received a lot of help from Jonathan and Trevor to help me fix a lot of issues in the code.
  
Seth: 
  In the earlier stages of developing the program, I formatted the button and label components on the game mode panel in StartGame class so they’re p
  sitioned in the center of the start game panel. I also helped Tyler plan out the format of the double player maps and scoreboard, which he then 
  implemented the code for.
  
Jonathan:
  Added music to each mode of the game. I worked on implementing the functionality of the single player game mode. Implementing the player, alien, 
bullet, shield , and enemyPlayer classes. Working collisions between the player bullets and objects that it is supposed to hit are also collisions 
between enemy bullets, player and shields. The scoreboard with high scores that is updated when needed. 

Trevor:
	In the beginning of the project, I planned out the GUI format of how the intro screen would look, including the images of the aliens. 
  Throughout the project I helped with the creation of the shields and collisions of objects. I helped implement the movement of the aliens 
  along the x-axis towards the end of the project. I also contributed to the point system and the scoreboard GUI for the SinglePlayer portion of 
  the game. Testing was integral, and I made sure to test and search for bugs in the SinglePlayer.

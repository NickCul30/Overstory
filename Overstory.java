//Overstory.java
//Nicholas Culmone, Matthew Farias

//PLEASE RUN AND INSTALL THE 8bitoperator_jve.ttf BEFORE PLAYING

//In the game of Overstory, you find yourself to have fallen into a hole with no way back out the way you
//came. To get back home, you have to traverse through the ruins, but along the way there are monsters in
//which you have to fight.
//Our game is based on the game Undertale, and has identical menus and battle systems as the original game.
//The battle system is a turn based system, where you get an attack first (which is a timing based attack),
//then the enemy gets to attack. To avoid damage from them you must guide your heart away from the projectiles
//they are shooting at you.

/*Controls
 *Arrow Keys for movement (in and out of battle)
 *Ctrl to open menu
 *Shift to go back (in menu)
 *Enter to select (in menu, while talking, in battle, pick up item)
 *Hold Esc to exit the game (if you exit the game without doing this (e.g. closing) you will lose all your game progress)
 */



import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Robot.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.Timer;
import java.util.*;
import java.net.*;
import java.util.Random;
import javax.sound.sampled.AudioSystem;


public class Overstory extends JFrame implements ActionListener{
	Timer myTimer;
	GamePanel game;
		
    public Overstory() throws IOException{
		super("Overstory");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,650);

		myTimer = new Timer(10, this);	 // trigger every 10 ms
		
		game = new GamePanel(this);
		add(game);
		

		setResizable(false);
		setVisible(true);
    }
	
	public void start(){
		myTimer.start();
	}

	public void actionPerformed(ActionEvent evt){
		game.repaint();
		
	}

    public static void main(String[]args) throws IOException{
		Overstory frame = new Overstory();		
    }
}

class GamePanel extends JPanel implements KeyListener{
	private final int LEFT=0,RIGHT=1,UP=2,DOWN=3,STAT=4,ITEMSEL=5,ITEMINFO=6;
	private int dir = DOWN;
	
	private boolean[]keys;
	private boolean standStill=false; //player is moving or not
	private boolean canMove = true; //player is allowed to move
	private boolean enterPress; //player pressed enter
	
	private boolean inFight = false; //player is in a battle or not
	
	private ArrayList<ArrayList<String>> textToDraw = new ArrayList<ArrayList<String>>();
	//text that is to be drawn on the screen when encountering something (character or object).
	//Is an arraylist of arraylists of strings that holds what is to be printed
	
	private Overstory mainFrame;
	
	private Player p; //the player
	private ArrayList<Item>pItems = new ArrayList<Item>(); //player's items
	private Sword weapon; //player's current weapon
	
	private int frameCount = 0;
	private int textNum = 0; //a counter for what text is to be drawn at a certain time.
	private int bx,by; //x,y of the background, player remains in middle of screen, background is draw around them
	
	Font comicSans = new Font("Comic Sans MS", Font.BOLD,28);
	Font normText = new Font("8bitoperator JVE", Font.BOLD, 25);
	Font deadText = new Font("8bitoperator JVE", Font.BOLD, 50);
	
	Point pMid = new Point(400,325); //middle of the screen, where the player will always be
	
	private ArrayList<Level> levels = new ArrayList<Level>(); //arraylist of all levels
	private Level pLevel; //current level the player is in
	
	private HashTable<Item> items = new HashTable<Item>(); //all items in the game
	private HashTable<Sword> swords = new HashTable<Sword>(); //all swords in the game
	
	private boolean pTurn; //player's turn in battle
	private boolean eneTalk=true;
	private boolean eneText=true;
	private Enemy foe; //current enemy the player is against
	private ArrayList<Enemy>foeList=new ArrayList<Enemy>(); //All enemies in the game
	private final int MENU=0,ATK=1,ITEM=2,DEFEND=3,DEAD=4,ENDFIGHT=5,TALK = 6,WAIT = 7;
	private int screen; //current screen the player is in when in a battle or in menu
	//Menu variables
	private boolean atkSelect=true; //if the player has selected the attack option in the menu
		//images
	private Image atkPick = new ImageIcon("bMenu//fightSelect.png").getImage();
	private Image atk = new ImageIcon("bMenu//fight.png").getImage();
	private Image itemPick= new ImageIcon("bMenu//itemSelect.png").getImage();
	private Image item = new ImageIcon("bMenu//item.png").getImage();
	//Atk screen variables
	private boolean meterMove=true; //if the attack meter should move or not
	private Image meter = new ImageIcon("bMenu//atkMeter.png").getImage();
	private int lineX=120; //the position of the meter line
	private int change=6; //the amount of pixels the meter bar moves every frame
	private boolean pauseFlag=false; //if the program should be paused or not
	//Life bar vars
	private int foeBar=400; //the max size for the HP bar of the enemy
	private int pBar=160; //the max size for the HP bar of the player
	//Defend screen vars
	private int bombCount=0; //the counter for how any bombs have been used
	private int bombTimer=0; //the timeer to see when the next bomb should be fired
	private ArrayList<Bomb>bombList=new ArrayList<Bomb>(); //the list that hold all the bombs
	private int heartX,heartY; //the coordinates of the heart
		//images
	private Image heartPic = new ImageIcon("bSprites//heart.png").getImage();
	private Image blueHeartPic = new ImageIcon("bSprites//bHeart.png").getImage();
	private boolean canHit=true; //if the player can get hit or not
	private int hitCounter; //the counter that updates to see if there has been a long enough wait from the last hit
	//Random Variable
	private Random rand=new Random(); //a variable for getting random numbers
	//sound variables
	AudioClip BGM = Applet.newAudioClip(getClass().getResource("Music/Ruins.wav"));
	AudioClip encounter = Applet.newAudioClip(getClass().getResource("Music/Encounter.wav"));
	AudioClip battle = Applet.newAudioClip(getClass().getResource("Music/Battle.wav"));
	AudioClip atkHit = Applet.newAudioClip(getClass().getResource("Music/AtkHit.wav"));
	AudioClip gameOver = Applet.newAudioClip(getClass().getResource("Music/GameOver.wav"));
	AudioClip healSound = Applet.newAudioClip(getClass().getResource("Music/Heal.wav"));
	AudioClip menuMusic = Applet.newAudioClip(getClass().getResource("Music/MenuMusic.wav"));
	AudioClip torielSong = Applet.newAudioClip(getClass().getResource("Music/Toriel.wav"));
	AudioClip cry; //each foe will have their own sound that they make when they attack
	boolean playCry=true; //flag to see whether or not the flag should be played
	//flag to play toriel song
	boolean playToriel=true;
	
	//win condition
	private boolean winFlag=false;
	
	//nick's new
	private int hx,hy; //heart's x,y pos in battle
	private boolean inMenu; //if player's in menu
	private int mScreen = 1; //what is shown in the in game menu
	//menu flags & position indicatiors
	private boolean sSelect;
	private int iSelect,iOption;
	private boolean dPress,uPress,lPress,rPress,sPress;
	private Item iSelected;
	private int menuPos;
	
	private boolean inMain = false; //if the initial menu is exited and is in the main game
	private final int STORY = 0,TITLE = 1,INSTRUCT = 2,NAME = 3; //start menu vars
	private Image title = new ImageIcon("title.png").getImage();
	private Image instructions = new ImageIcon("instructions.png").getImage();
	public char[]letters = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	private Font normText2 = new Font("8bitoperator JVE", Font.BOLD, 32);
	private ArrayList<savePoint>saves = new ArrayList<savePoint>(); //all the possible save points in the game
	private int quitTimer = 0; //when holding esc the timer goes up, when 255 game saves and closes
	private boolean torielFlag; //done the tutorial
	private int tScreen = TALK; //what part of tutorial player is in
	private boolean talkFlag = false; //menu flags
	private boolean fightPlayed = false;
	private int spdMult = 1; //used to slow down projectiles during the tutorial
	private int battleWait = 0; //allows for a set time in between battles
	
	private PrintWriter saveWrite; //player's saved data
	private PrintWriter itemWrite; //items in levels saved data
	private PrintWriter swordWrite; //swords in levels saved data
	

	
	public GamePanel(Overstory m) throws IOException{
		keys = new boolean[KeyEvent.KEY_LAST+1];
		mainFrame = m;
		setSize(1600,900);
        addKeyListener(this);
        
        Scanner wordFile = new Scanner(new BufferedReader(new FileReader("saveFile0.txt")));
		p=new Player(wordFile.nextLine()); //initial save of player
		
		Scanner wordFile2 = new Scanner(new BufferedReader(new FileReader("enemies.txt")));
		while(wordFile2.hasNextLine()==true){
			foeList.add(new Enemy(wordFile2.nextLine())); //adds all enemies from text file to game
		}
		
		Scanner levelFile = new Scanner(new BufferedReader(new FileReader("levels.txt")));
		Scanner levelItems = new Scanner(new BufferedReader(new FileReader("levelItems.txt")));
		Scanner levelSwords = new Scanner(new BufferedReader(new FileReader("levelSwords.txt")));
		
		ArrayList<String>lvlTmp = new ArrayList<String>();
		
		//adding levels to game
		while(levelFile.hasNextLine()){
			String tmp = levelFile.nextLine();
			lvlTmp.add(tmp);
			levels.add(new Level(tmp,levelItems.nextLine(),levelSwords.nextLine()));
		}
		
		//adding all items to game
		Scanner itemFile = new Scanner(new BufferedReader(new FileReader("items.txt")));
		while(itemFile.hasNextLine()){
			items.add(new Item(itemFile.nextLine()));
		}
		
		//adding all swords to the game
		Scanner swordFile = new Scanner(new BufferedReader(new FileReader("swords.txt")));
		while(swordFile.hasNextLine()){
			swords.add(new Sword(swordFile.nextLine()));
		}
		
		//adding savepoints to the game
		Scanner sPointFile = new Scanner(new BufferedReader(new FileReader("savePoints.txt")));
		while(sPointFile.hasNextLine()){
			saves.add(new savePoint(sPointFile.nextLine()));
		}
		
		
		wordFile.close();
		wordFile2.close();
		levelFile.close();
		itemFile.close();
		swordFile.close();
		levelItems.close();
		levelSwords.close();
		sPointFile.close();
		
		//done when the player has an existing save file
		
		//updates player information using the saved file
		Scanner saveFileNew = new Scanner(new BufferedReader(new FileReader("saveFile0New.txt")));
		while(saveFileNew.hasNextLine()){
			p=new Player(saveFileNew.nextLine());
		}
		
		saveFileNew.close();
		
		//updates level data using the player's saved files
		Scanner itemFileNew = new Scanner(new BufferedReader(new FileReader("levelItemsNew.txt")));
		Scanner swordFileNew = new Scanner(new BufferedReader(new FileReader("levelSwordsNew.txt")));
		int lvlCount = 0;
		
		//goes through itemFileNew, swordFileNew, and levelFile to make a new level based on all
		//three of these text documents
		while(itemFileNew.hasNextLine()){
			String nextTmp = itemFileNew.nextLine();
			if(nextTmp.equals("")){
				levels.clear();
				lvlCount = 0;
				swordFileNew.nextLine();
			}
			else{
				levels.add(new Level(lvlTmp.get(lvlCount),nextTmp,swordFileNew.nextLine()));
				lvlCount ++;
			}
		}
		
		//files that will be saved to
		saveWrite = new PrintWriter("saveFile0New.txt", "UTF-8");
		itemWrite = new PrintWriter("levelItemsNew.txt", "UTF-8");
		swordWrite = new PrintWriter("levelSwordsNew.txt", "UTF-8");
		
		//setting player variables
		savePoint saveTmp = saves.get(p.getSave());
		pLevel = levels.get(saveTmp.getLevel());
		bx = saveTmp.getPos().x; //player's x,y pos relative to map
		by = saveTmp.getPos().y;
		weapon=swords.get(p.getWeapon().hashCode());
		torielFlag = p.getToriel();
		for(String s : p.getItems()){
			pItems.add(items.get(s.hashCode()));
		}
		menuMusic.loop();
        
        
        saveGame(p.getSave()); //saves game initally so if game is exited w/o saving, save file will remain
	}
	
    public void addNotify() {
        super.addNotify();
        requestFocus();
        mainFrame.start();
    }
	
	public void move(){
		BufferedImage m = pLevel.getMask(); //level's mask
		
		//triggering the tutorial
		if(torielFlag == false && new Color(m.getRGB((bx/2)+200,(by/2)+165)).equals(new Color(255,255,254))){
			if(playToriel){
				BGM.stop();
				torielSong.play();
				playToriel=false;
			}
			toriel();
		}
		
		//quitting thet game
		if(keys[KeyEvent.VK_ESCAPE]){
			quitTimer ++;
			if(quitTimer == 255){
				saveWrite.close();
				itemWrite.close();
				swordWrite.close();
				System.exit(0);
			}
		}
		else{
			quitTimer = 0;
		}
		
		//when player is not in the ingame menu
		if(!inMenu){
			int oldBx = bx,oldBy = by,oldDir = dir;
			//used to determine the direction the player is facing
			
			if(canMove){
				
				//moving the player, checks if the mask's colour is red (indicating that the player cannot move),
				//if not then the player can move in that direction
				if(keys[KeyEvent.VK_RIGHT]){
					if((new Color(m.getRGB((bx/2)+210,(by/2)+175)).equals(Color.red))!=true){
						bx += 4;
					}
					
				}
				if(keys[KeyEvent.VK_LEFT]){
					if((new Color(m.getRGB((bx/2)+190,(by/2)+175)).equals(Color.red))!=true){
						bx -= 4;
					}
				}
				if(keys[KeyEvent.VK_DOWN]){
					if((new Color(m.getRGB((bx/2)+200,(by/2)+180)).equals(Color.red))!=true){
						by += 4;
					}
				}
				if(keys[KeyEvent.VK_UP]){
					if((new Color(m.getRGB((bx/2)+200,(by/2)+165)).equals(Color.red))!=true){
						by -= 4;
					}
				}
				
				//checks to see if the player is interacting with something when pressing enter
				//different things that can be interacted with have different colours on the mask
				if(keys[KeyEvent.VK_ENTER] && enterPress){
					enterPress = false;
					ArrayList<Talk> talks = pLevel.getTalks();
					
					//checks if player is encountering something that will draw a textbox
					for(int i=0;i<talks.size();i++){
						if(new Color(m.getRGB((bx/2)+200,(by/2)+175)).equals(talks.get(i).getC())){
							drawText(talks.get(i).getText());
							canMove=false;
						}
					}
					
					ArrayList<ItemLevel> itemTmp = pLevel.getItems();
					
					//Checks if player is picking up an item
					for(int i=0;i<itemTmp.size();i++){
						Item tmp = itemTmp.get(i).getItem();
						if(keys[KeyEvent.VK_ENTER] && new Color(m.getRGB((bx/2)+200,(by/2)+175)).equals(tmp.getC())){
							enterPress = false;
							
							//item will be picked up unless you have a full inventory
							if(pItems.size() >= 5){
								drawText("You found a " + tmp.getName() + " lying on the floor... disgusting. /p Your inventory is full, so you drop the " + tmp.getName() + ".");
							}
							else{
								pItems.add(tmp);
								pLevel.getItems().remove(pLevel.getItems().get(i));
								drawText("You found a " + tmp.getName() + " lying on the floor... disgusting. /p You take it anyways.");
							}
						}
					}
					
					ArrayList<SwordLevel> swordTmp = pLevel.getSwords();
					
					//Checks if player is picking up an sword
					for(int i=0;i<swordTmp.size();i++){
						Sword tmp = swordTmp.get(i).getSword();
						if(keys[KeyEvent.VK_ENTER] && new Color(m.getRGB((bx/2)+200,(by/2)+175)).equals(tmp.getC())){
							enterPress = false;
							
							//sword will be replaced with what you have if the damage is stronger, otherwise sword will be dropped
							if(weapon.getDamage() > tmp.getDamage()){
								drawText("You found a " + tmp.getName() + " on the floor. /p It seems to do less damage than your " + weapon.getName() + " so you put it back down.");
							}
							else{
								drawText("You found a " + tmp.getName() + " on the floor. /p It is stronger than your " + weapon.getName() + " so you take it. /p This place has some really dangerous litter.");
								weapon = tmp;
							}
							pLevel.getSwords().remove(pLevel.getSwords().get(i));
						}
					}
					
					//Checks if player is encountering a save point, saves the game if player interacts with it
					int sCount = 0;
					for(savePoint s : saves){
						if(keys[KeyEvent.VK_ENTER] && new Color(m.getRGB((bx/2)+200,(by/2)+175)).equals(s.getC())){
							enterPress = false;
							drawText("Game Saved.");
							saveGame(sCount);
							p.setSave(sCount);
						}
						sCount ++;
					}
					
					if(new Color(m.getRGB((bx/2)+200,(by/2)+175)).equals(new Color(99,0,255))){ //win the game
						screen = DEAD;
						winFlag = true;
					}
				}
				
				//opens in game menu
				if(keys[KeyEvent.VK_CONTROL]){ //jun 6
					inMenu = true;
					mScreen = MENU;
					sSelect = false;
					standStill = true;
				}
			}
			
			//scrolls through the pages of text that is being displayed, if on the last page of the
			//text, closes the textbox and allows player to move again
			if(keys[KeyEvent.VK_ENTER] && !textToDraw.isEmpty() && enterPress){
				textNum ++;
				enterPress = false;
				
				if(textNum >= textToDraw.size()){
					textToDraw.clear();
					canMove = true;
					textNum = 0;
				}
			}
			if(keys[KeyEvent.VK_ENTER] == false){
				enterPress = true;
			}
			
			//this series of if statements allows the game to track the player's direction w/ more accuracy than
			//simply setting the player's direction when a key is pressed (doesn't account for more than one key
			//pressed at the same time)
			
			//moving strictly in one direction
			if(bx > oldBx && oldBy == by){
				dir = RIGHT;
			}
			else if(bx < oldBx && oldBy == by){
				dir = LEFT;
			}
			else if(bx == oldBx && oldBy < by){
				dir = DOWN;
			}
			else if(bx == oldBx && oldBy > by){
				dir = UP;
			}
			
			else if(by > oldBy && bx != oldBx){ //the player is moving diagonally down
				dir = DOWN;
			}
			else if(by < oldBy && bx != oldBx){ //the player is moving diagonally up
				dir = UP;
			}
			
			else{ //player is standing still
				dir = oldDir;
			}
			
			//if opposite directions are pressed at the same time, player will remain in idle
			if((keys[KeyEvent.VK_DOWN]==false && keys[KeyEvent.VK_UP]==false && 
				keys[KeyEvent.VK_LEFT]==false && keys[KeyEvent.VK_RIGHT]==false)
					
				|| (keys[KeyEvent.VK_DOWN] && keys[KeyEvent.VK_UP] &&
				keys[KeyEvent.VK_LEFT]==false && keys[KeyEvent.VK_RIGHT]==false)
					
				|| (keys[KeyEvent.VK_LEFT] && keys[KeyEvent.VK_RIGHT]) &&
				keys[KeyEvent.VK_DOWN]==false && keys[KeyEvent.VK_UP]==false){
					
				standStill=true;
			}
			else if(canMove){
				standStill=false;
			}
			else{
				standStill = true;
			}
			
			//checks to see if the player goes in an entrance to another level by looking at the
			//currect level's mask
			for(int i=0;i<levels.size();i++){
				boolean doBreak = false;
				ArrayList<Entrance> tmp = levels.get(i).getEntrances();
				
				for(int j=0;j<tmp.size();j++){
					if(new Color(m.getRGB((bx/2)+200,(by/2)+175)).equals(tmp.get(j).getC())){
						pLevel = levels.get(i);
						Entrance tmp2 = tmp.get(j);
						bx = tmp2.getX();
						by = tmp2.getY();
						doBreak = true;
						break;
					}
				}
				if(doBreak) break;
			}
			
			//randomly starts a battle when the player is walking, will not start a battle within
			//10 seconds of a previous battle
			if(!standStill && torielFlag && battleWait > 1000){
				int randInt=rand.nextInt(500);
				if(randInt==21){
					battleWait = 0;
					int foeNum=rand.nextInt(foeList.size()-1);
					inFight=true;
					eneText=eneTalk=true;
					foe=foeList.get(foeNum+1);
					screen=MENU;
					BGM.stop();
					encounter.play();
					pause(1000);
					cry = Applet.newAudioClip(getClass().getResource("enemies/"+foe.getName()+".wav"));
					battle.play();
				}
			}
			else{
				battleWait ++;
			}
		}
		
		//When the player is in the ingame menu
		else{
			if(!keys[KeyEvent.VK_SHIFT]){
				sPress = false;
			}
			if(!keys[KeyEvent.VK_ENTER]){
				enterPress = true;
			}
		
			if(mScreen == MENU){ //first screen of the menu, has 2 options, selecting items or player's stats
				if(keys[KeyEvent.VK_DOWN]){
					sSelect = true;
				}
				if(keys[KeyEvent.VK_UP]){
					sSelect = false;
				}
				if(keys[KeyEvent.VK_SHIFT] && sPress == false){ //exits menu
					inMenu = false;
					sPress = true;
				}
				if(keys[KeyEvent.VK_ENTER] && enterPress){
					enterPress = false;
					if(sSelect){
						mScreen = STAT;
					}
					else if(sSelect == false && pItems.size() > 0){
						mScreen = ITEM;
						iSelect = 0;
					}
				}
			}
			
			//player's stats, only has one option, to go back to main part of menu
			else if(mScreen == STAT){
				if(keys[KeyEvent.VK_SHIFT] && sPress == false){
					mScreen = MENU;
					sPress = true;
				}
			}
			
			//allows the player to select any one of the items they have obtained
			else if(mScreen == ITEM){
				if(keys[KeyEvent.VK_SHIFT] && sPress == false){ //goes to prev part in menu
					mScreen = MENU;
					sPress = true;
				}
				
				if(keys[KeyEvent.VK_DOWN] && dPress == false){
					dPress = true;
					if(iSelect + 1 < pItems.size()){
						iSelect ++;
					}
				}
				if(!keys[KeyEvent.VK_DOWN]) dPress = false;
				
				if(keys[KeyEvent.VK_UP] && uPress == false){
					uPress = true;
					if(iSelect - 1 >= 0){
						iSelect --;
					}
				}
				if(!keys[KeyEvent.VK_UP]) uPress = false;
				
				if(keys[KeyEvent.VK_ENTER] && enterPress){
					mScreen = ITEMSEL;
					iOption = 0;
					enterPress = false;
					iSelected = pItems.get(iSelect); //the item the player has selected
				}
			}
			
			//when an item has been selected the player is given the options to use item, drop item, or look at the
			//stats of the item (how much it heals)
			else if(mScreen == ITEMSEL){
				if(keys[KeyEvent.VK_SHIFT] && sPress == false){ //goes to prev part in menu
					mScreen = ITEM;
					sPress = true;
				}
				
				if(keys[KeyEvent.VK_LEFT] && dPress == false){
					dPress = true;
					if(iOption - 1 >= 0){
						iOption --;
					}
				}
				if(!keys[KeyEvent.VK_LEFT]) dPress = false;
				
				if(keys[KeyEvent.VK_RIGHT] && uPress == false){
					uPress = true;
					if(iOption + 1 <= 2){
						iOption ++;
					}
				}
				if(!keys[KeyEvent.VK_RIGHT]) uPress = false;
				
				if(keys[KeyEvent.VK_ENTER] && enterPress){
					enterPress = false;
					Item tmp = pItems.get(iSelect);
					
					if(iOption == 0){ //uses item
						healSound.play();
						p.heal(tmp.getValue());
						pItems.remove(tmp);
						mScreen = MENU;
					}
					else if(iOption == 1){ //drops item
						pItems.remove(tmp);
						mScreen = MENU;
					}
					else if(iOption == 2){ //gets item info
						mScreen = ITEMINFO;
					}
				}
			}
			
			//displays the stats of the item selected
			else if(mScreen == ITEMINFO){
				if(keys[KeyEvent.VK_SHIFT] && sPress == false){ //goes to prev part in menu
					mScreen = ITEMSEL;
					sPress = true;
				}
			}
		}
	}
	
	//the main menu of the game, displays this when the game is first started
	public void startMenu(){
		if(mScreen == TITLE){ //displays the starting graphic of the game
			if(keys[KeyEvent.VK_ENTER] && enterPress){
				enterPress=false;
				mScreen = INSTRUCT;
			}
			if(keys[KeyEvent.VK_ENTER] == false){ //press enter to advance
				enterPress = true;
			}
		}
		
		
		else if(mScreen == INSTRUCT){ //displays the instructions of the game
			if(keys[KeyEvent.VK_ENTER] && enterPress){
				enterPress=false;
				
				//if the player has a save file, then the game will start instead of going to
				//the "enter name" screen in the menu
				if(p.getName().length() == 0){
					mScreen = NAME;
				}
				else{
					inMain = true;
					menuMusic.stop();
					BGM.loop();
				}
			}
			if(keys[KeyEvent.VK_ENTER] == false){
				enterPress = true;
			}
		}
		
		//allows the player to enter their name in the game, displays all of the letters and can navigate through them
		//to enter name
		else if(mScreen == NAME){
			if(keys[KeyEvent.VK_ENTER] && enterPress){
				enterPress=false;
				if(menuPos <= 25){
					if(p.getName().length() < 6){
						p.setName(p.getName() + letters[menuPos]);
					}
				}
				else if(menuPos == 28){ //deletes char
					String tmp = p.getName();
					if(tmp.length() > 0){
						p.setName(tmp.substring(0, tmp.length()-1));
					}
				}
				else if(menuPos == 29 && p.getName().length() > 0){ //starts game
					inMain = true;
					menuMusic.stop();
					BGM.loop();
				}
			}
			if(keys[KeyEvent.VK_ENTER] == false){
				enterPress = true;
			}
					
			//navigation through menu
			if(keys[KeyEvent.VK_LEFT] && lPress == false){
				lPress = true;
				if(menuPos - 1 >= 0){
					menuPos --;
				}
			}
			if(!keys[KeyEvent.VK_LEFT]) lPress = false;
				
			if(keys[KeyEvent.VK_RIGHT] && rPress == false){
				rPress = true;
				if(menuPos < 29){
					menuPos ++;
				}
			}
			if(!keys[KeyEvent.VK_RIGHT]) rPress = false;
			
			if(keys[KeyEvent.VK_DOWN] && dPress == false){
				dPress = true;
				if(menuPos + 5 <= 29){
					menuPos += 5;
				}					
			}
			if(!keys[KeyEvent.VK_DOWN]) dPress = false;
			
			if(keys[KeyEvent.VK_UP] && uPress == false){
				uPress = true;
				if(menuPos - 5 >= 0){
					menuPos -= 5;
				}
			}
			if(!keys[KeyEvent.VK_UP]) uPress = false;
			
			if(menuPos == 26) menuPos = 25; //deadzones in the menu
			if(menuPos == 27) menuPos = 28;
			
			
		}
	}
	
	//the player will come across this in the very first screen. This is a tutorial for the attacking and defending of the game
	public void toriel(){
		if(tScreen == TALK){ //the first part of the tutorial, the player talks to Toriel
			canMove = false;
			if(textToDraw.isEmpty() && talkFlag){
				tScreen = DEFEND;
				canMove = true;
			}
			
			if(tScreen == TALK){
				drawText("Hello there. /p My name is Toriel /n I live in these Ruins. /p I have never seen you before. " +
				"How did you manage to get here? /p * You tell Toriel that you fell in a hole and ended up here /p " +
				"Oh well we need to get you home then! /p First however I'll show you how to defend against enemies since we have a lot of hostile creatures around here." +
				" /p To avoid damage against enemies you must use the 'arrow keys' (whatever those are) /p You use them to guide your heart away from whatever they are firing at you." +
				" /p Let's try it out!");
			}				
			talkFlag = true;
		}
		else if(tScreen == WAIT){ //when the tutorial is finished
			spdMult = 1;
			torielFlag = true;
			torielSong.stop();
			BGM.loop();
		}
		else if(tScreen == DEFEND){ //Toriel will attack you to once you are done talking, sets vars so she attacks first and does battle
			screen = DEFEND;
			pTurn = false;
			playCry = false;
			inFight=true;
			eneText=eneTalk=true;
			heartX=400;
	    	heartY=525;
			foe=foeList.get(0);
			spdMult = 3;
			torielSong.stop();
			encounter.play();
			pause(1000);
			battle.play();
	    	
			battleMove();
			tScreen = WAIT;
		}
	}
	
	//This is the move class that is used for the battles
	public void battleMove(){
		//player's turn
		if(pTurn){
			if(screen==MENU){ //player chooses to either use an item or attack the foe
				if(keys[KeyEvent.VK_LEFT]){
					atkSelect=true;
				}
				if(keys[KeyEvent.VK_RIGHT] && torielFlag){ //player can only use items after the toriel fight
					atkSelect=false;
				}
				if(keys[KeyEvent.VK_ENTER] && enterPress){ //player picks their choice
					enterPress=false;
					if(atkSelect){
						screen=ATK;
					}
					else{
						screen=ITEM;
						menuPos = 0;
					}
				}
				if(keys[KeyEvent.VK_ENTER] == false){
					enterPress = true;
				}
			}
			else if(screen==ATK){ //player presses Enter to stop the bar and finish their attack
				
				if(keys[KeyEvent.VK_ENTER] && enterPress){
					meterMove=false;
					enterPress=false;
				}
				if(keys[KeyEvent.VK_ENTER] == false){
					enterPress = true;
				}
				
			}
			else if(screen == ITEM){ //player picks the item they want to use or returns back to the menu
				
				//navigation through the menu
				if(keys[KeyEvent.VK_LEFT] && lPress == false){
					lPress = true;
					if(menuPos - 1 >= 0){
						menuPos --;
					}
				}
				if(!keys[KeyEvent.VK_LEFT]) lPress = false;
				
				if(keys[KeyEvent.VK_RIGHT] && rPress == false){
					rPress = true;
					if(menuPos < pItems.size()){
						menuPos ++;
					}
				}
				if(!keys[KeyEvent.VK_RIGHT]) rPress = false;
				
				if(keys[KeyEvent.VK_DOWN] && dPress == false){
					dPress = true;
					if(menuPos + 3 <= pItems.size()){
						menuPos += 3;
					}					
				}
				if(!keys[KeyEvent.VK_DOWN]) dPress = false;
				
				if(keys[KeyEvent.VK_UP] && uPress == false){
					uPress = true;
					if(menuPos - 3 >= 0){
						menuPos -= 3;
					}
				}
				if(!keys[KeyEvent.VK_UP]) uPress = false;
				
				if(keys[KeyEvent.VK_ENTER] && enterPress){ //the player selects an item or chooses to go back a screen
					enterPress=false;
					if(menuPos == 0){ //goes back a screen
						screen = MENU;
					}
					else{ //heals the player with the item selected
						p.heal(pItems.get(menuPos - 1).getValue());
						pItems.remove(pItems.get(menuPos - 1));
						screen=DEFEND;
	    				pTurn=false;
	    				heartX=400;
	    				heartY=525;
	    				healSound.play();
						pause(500);
					}
				}
				if(keys[KeyEvent.VK_ENTER] == false) enterPress = true;
			}
		}
		else{
			//foe's turn
			if(screen!=DEFEND){ //at the start of the fight the foe says one of their phrases
				if(eneTalk){
					if(eneText){
						drawText(foe.getPhrase());
						eneText=false;
					}
					
					//the foe's catch phrase
					if(keys[KeyEvent.VK_ENTER] && !textToDraw.isEmpty() && enterPress){
						textNum ++;
						enterPress = false;
						
						if(textNum >= textToDraw.size()){
							textToDraw.clear();
							eneTalk = false;
							textNum = 0;
						}
					}
					if(keys[KeyEvent.VK_ENTER] == false){
						enterPress = true;
					}
				}
				else if(screen!=DEAD){ //fail safe to return to the menu
					eneTalk=false;
					pTurn=true;
					screen=MENU;
					
				}
			}
			else if(screen==DEFEND){ //player move the heart around inside of the box with the arrow keys
				if(keys[KeyEvent.VK_RIGHT] && heartX<476){
					heartX += 2;
				}
				if(keys[KeyEvent.VK_LEFT] && heartX>308){
					heartX -= 2;
				}
				if(keys[KeyEvent.VK_UP] && heartY>460){
					heartY -= 2;
				}
				if(keys[KeyEvent.VK_DOWN] && heartY<596){
					heartY += 2;
				}
			}
		}
		if(screen==DEAD){ //lets the user exit the game with Esc and closes the files
			if(keys[KeyEvent.VK_ESCAPE]){
				quitTimer =255;
				if(quitTimer == 255){
					saveWrite.close();
					itemWrite.close();
					swordWrite.close();
					System.exit(0);
				}
			}
		}
	}
	
    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }
    
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
    
    public void paintComponent(Graphics g){
    	if(inMain){ //if the player is in the main game instead of the opening menu
	    	if(pauseFlag){ //pauses the game for a second
	    		pause(1000);
	    		pauseFlag=false;
	    	}
	    	g.setColor(Color.black);
	    	g.fillRect(0,0,800,650);
	    	g.setFont(normText);
	    	//draw background
	    	if(inFight == false){ //if the player is currently in a fight or not
	    		move();
	    		//draw background
	    		g.drawImage(pLevel.getBack(),-bx,-by,pLevel.getBack().getWidth()*2,pLevel.getBack().getHeight()*2,this);
	    		
	    		//draws items and swords on the ground
	    		for(ItemLevel itemp : pLevel.getItems()){
					g.drawImage(itemp.getItem().getSprite(),-bx + itemp.getX() + 400,-by + itemp.getY() + 325,20,20,this);
				}
				for(SwordLevel swordTmp : pLevel.getSwords()){
					g.drawImage(swordTmp.getSword().getSprite(),-bx + swordTmp.getX() + 400,-by + swordTmp.getY() + 325,20,20,this);
				}
		   	
		    	//drawing sprites
		    	
		    	if(standStill || inMenu){ //if the foe is in the menu or standing still it draws the still sprite 
		    		frameCount=0;
		    		Image spr = p.getSprite(dir,0);
		    		g.drawImage(spr,400-(int)(spr.getWidth(null)*1.75/2),325-(int)(spr.getHeight(null)*1.75/2),(int)(spr.getWidth(null)*1.75),(int)(spr.getHeight(null)*1.75),this);
		    	}
		    	else if(standStill==false){ //else it runs a counter that updates every frame and every 10 frames it draws the next sprite
		    		frameCount+=1;
		    		Image spr = p.getSprite(dir,frameCount/10);
		    		g.drawImage(spr,400-(int)(spr.getWidth(null)*1.75/2),325-(int)(spr.getHeight(null)*1.75/2),(int)(spr.getWidth(null)*1.75),(int)(spr.getHeight(null)*1.75),this);
		    		if(frameCount == 39){
		    			frameCount=0;
		    		}
		    	}
		    	 g.setFont(normText2);
		    	
		    	//the colour of the quitting text being drawn, has 0 opacity when ESC not being pressed
		    	g.setColor(new Color(255,255,255,quitTimer));
			    
			    g.drawString("QUITTING...",10,30); //draws quitting text, most of the time opacity is 0
			    g.setColor(Color.black);

		    	
		    	if(inMenu){ //when the player is in the ingame manu
		    		g.setFont(normText);	    	
		    		g.setColor(Color.white);
			    	g.fillRect(40,50,140,100);
			    	g.setColor(Color.black);
			    	g.fillRect(48,58,124,84);
			    	g.setColor(Color.white);
			    	g.drawString("ITEM",95,87);
			    	g.drawString("STAT",95,127);
		    		if(mScreen == MENU){ //intial menu screen
		    			if(sSelect){
		    				g.drawImage(heartPic,64,113,17,17,this);
		    			}
		    			else{
		    				g.drawImage(heartPic,64,73,17,17,this);
		    			}
					}
					else if(mScreen == STAT){ //stats screen
						g.setColor(Color.white);
				    	g.fillRect(200,50,300,275);
				    	g.setColor(Color.black);
				    	g.fillRect(208,58,284,259);
				    	g.setColor(Color.white);
				    	
				    	g.drawString('"' + p.getName() + '"',230,87);
				    	g.drawString("HP: " + p.getHp() + "/" + p.getMaxHp(),230,137);
				    	g.drawString("WEAPON: " + weapon,230,167);
					}
					else if(mScreen == ITEM || mScreen == ITEMSEL || mScreen == ITEMINFO){ //items screens
						g.setColor(Color.white);
				    	g.fillRect(200,50,300,275);
				    	g.setColor(Color.black);
				    	g.fillRect(208,58,284,259);
				    	g.setColor(Color.white);
				    	int tmp = 0;
				    	for(Item i : pItems){
				    		g.drawString(i.getName(),260,87 + tmp*40);
				    		tmp ++;
				    	}
				    	
				    	//draws the heart at different places depending on what screen thet player is in, also adds things to
				    	//the item screen when on a screen past ITEM (ex. ITEMSEL,ITEMINFO)
				    	if(mScreen == ITEM){
				    		g.drawImage(heartPic,230,72 + 40*iSelect,17,17,this);
				    	}
				    	if(mScreen == ITEMSEL){
				    		g.drawString("USE        DROP       INFO",250,300);
				    		g.drawImage(heartPic,224 + 88*iOption,285,17,17,this);
				    	}
				    	if(mScreen == ITEMINFO){
				    		g.drawString("Heals " + iSelected.getValue() + " HP",250,300);
				    	}
					}
		    	}
		    	if(screen==DEAD){ //prints GAME OVER on the screen and allows the user to exit with Esc
	    			g.setFont(deadText);
	    			g.setColor(Color.black);
	    			g.fillRect(0,0,800,700);
	    			g.setColor(Color.white);
	    			if(winFlag){
	    				g.drawString("YOU WIN!!",300,300);
	    			}
	    			else{
	    				g.drawString("GAME OVER",300,300);
	    			}
		    	}
	    	}
	    	else{ //if the player is in a battle
	    		battleMove();
	    		//draws the foe sprite and health bar
	    		g.drawImage(foe.getSprite(),275,70,this);
	    		g.setColor(Color.white);
	    		g.fillRect(0,450,794,172);
	    		g.setColor(Color.black);
	    		g.fillRect(8,458,778,156);
	    		g.setColor(Color.red);
	    		g.fillRect(200,40,400,20);
	    		g.setColor(Color.yellow);
	    		g.fillRect(200,40,foeBar,20);

	    		if(screen==MENU){ //draws the menu icons
	    			drawStats(g);
	    			if(atkSelect){
	    				g.drawImage(atkPick,220,480,this);
	    				g.drawImage(item,520,480,this);
	    			}
	    			else{
	    				g.drawImage(atk,220,480,this);
	    				g.drawImage(itemPick,520,480,this);
	    			}
	    		}
	    		else if(screen==ATK){ //when the player is conducting their attack
	    			g.drawImage(meter,100,470,this);
	    			if(meterMove){ //moves the line on the meter back and forward
	    				g.setColor(Color.white);
	    				g.fillRect(lineX,470,10,130);
	    				lineX+=change;
	    				if(lineX>650 || lineX<110){ //reverses the direction the bar moves
	    					change*=-1;
	    				}
	    			}
	    			else{ //when the user has declared their attack
	    				int dmg=0;
	    				dmg=(int)(Math.abs((376-lineX)*((double)(weapon.getDamage())/282)));
	    				dmg=weapon.getDamage()-dmg;
	    				g.setColor(Color.red);
	    				g.drawString(Integer.toString(dmg),lineX,440);
	    				screen=MENU;
	    				lineX=120;
	    				meterMove=true;
	    				foe.hit(dmg);
	    				foeBar=(int)(((double)(foe.getHp())/(double)(foe.getMaxHp()))*400);
	    				pauseFlag=true;//delaying for a second to display damage done for the user
	    							   //paintcomponent must complete to draw new item so the program is
	    							   //paused at the start of the next loop of paintcomponent
	    				atkHit.play();
	    				if(foe.getHp()<1){ //if the foe is dead it ends the fight
	    					screen=ENDFIGHT;
	    				}
	    				else{ //goes to the enemy's turn
	    					screen=DEFEND;
	    					pTurn=false;
	    					heartX=400;
	    					heartY=525;
	    				}
	    			}
	    		}
	    		else if(screen==DEFEND){ //the foe's attack
	    			int spawnRect = rand.nextInt(20)+30;
	    			if(playCry){ //play's the foe's battle cry
	    				cry.play();
	    				playCry=false;
	    			}
	    			drawStats(g);
	    			g.setColor(Color.blue);
	    			g.fillRect(300,450,200,175);
	    			g.setColor(Color.black);
	    			g.fillRect(308,458,184,156);
	    			if(canHit){	//if the foe can be hit it draws a red heart, otherwise it is blue
	    				g.drawImage(heartPic,heartX,heartY,this);
	    			}
	    			else{
	    				g.drawImage(blueHeartPic,heartX,heartY,this);
	    			}
	    			if(bombCount<foe.getAtkNum() || bombList.size()!=0){ //checks to see that the foe is not done firing bombs
	    				Rectangle pRect=new Rectangle(heartX,heartY,20,20);
	    				if(bombTimer%spawnRect==0 && bombCount<foe.getAtkNum()){ //creates a new bomb if more bombs can be fired and there has been a random delay
	    					double bombX=508;
	    					double bombY=458.01+rand.nextInt(170);
	    					double bombVelX=formula(bombX-heartX,bombY-heartY,bombX-heartX);
	    					double bombVelY=formula(bombX-heartX,bombY-heartY,bombY-heartY);
	    					bombCount+=1;
	    					bombList.add(new Bomb(bombX,bombY,bombVelX/spdMult,bombVelY/spdMult));
	    				}
	    				for(Bomb i : bombList){ //moves the bombs and checks to see if they hit the player
	    					i.move();
	    					Rectangle bRect=new Rectangle((int)(i.getX()),(int)(i.getY()),10,10);
	    					if(bRect.intersects(pRect) && canHit==true){
	    						atkHit.play();
	    						p.hit(foe.getDamage());
	    						pBar=(int)(((double)(p.getHp())/(double)(p.getMaxHp()))*160);
	    						canHit=false;
	    					}
	    					g.setColor(Color.red);
	    					g.fillRect((int)(i.getX()),(int)(i.getY()),10,10);
	    				}
	    				if(bombList.size()==foe.getAtkNum()){ //if all the bombs have been fired and the last one has passed 0 in the x axis
	    					if(bombList.get(foe.getAtkNum() - 1).getX()<0){
	    						bombList.clear();
	    					}
	    				}
	    				if(canHit==false){ //raises the hit counter and changes canHit to true after 40 frames
	    					hitCounter+=1;
	    					if(hitCounter==40){
	    						canHit=true;
	    						hitCounter=0;
	    					}
	    				}
	    				bombTimer++;
	    			}
	    			else{ //resets variables and changes it to the user's turn
	    				hitCounter=bombTimer=bombCount=0;
	    				canHit=true;
	    				screen=MENU;
	    				playCry=true;
	    			}
	    			
	    			if(p.getHp()==0){ //changes the screen to game over when the user's hp is at 0
	    				screen=DEAD;
	    				battle.stop();
	    				cry.stop();
	    				gameOver.loop();
	    			}
	    			
	    		}
	    		else if(screen==DEAD){ //prints GAME OVER on the screen and allows the user to exit with Esc
	    			g.setFont(deadText);
	    			g.setColor(Color.black);
	    			g.fillRect(0,0,800,700);
	    			g.setColor(Color.white);
	    			if(winFlag){
	    				g.drawString("YOU WIN!!",300,300);
	    			}
	    			else{
	    				g.drawString("GAME OVER",300,300);
	    			}
	    			//the colour of the quitting text being drawn, has 0 opacity when ESC not being pressed
	    			g.setColor(new Color(255,255,255,quitTimer));
			    	g.setFont(normText2);
			    	g.drawString("QUITTING...",10,30);
			    	g.setColor(Color.black);
	    		}
	    		else if(screen==ENDFIGHT){ //Tells the user they have defeated the foe and resets variables
	    			drawText("You have defeated "+foe.getName()+"!");
	    			foeBar=400;
	    			foe.respawn();
	    			inFight=pTurn=false;
	    			eneText=eneTalk=true;
	    			battle.stop();
	    			BGM.loop();
	    		}
	    		
	    		//when the player chooses to use an item instead of attacking
	    		else if(screen == ITEM){
	    			g.setColor(Color.white);
	    			g.setFont(normText2);
	    			
	    			g.drawString("Back",65,510);
	    			
	    			//draws the names of the items in a 2x3 grid
	    			for(int i=0;i<pItems.size();i++){
	    				if(i <= 1){
	    					g.drawString(pItems.get(i).getName(),315 + i*250,510);
	    				}
	    			}
	    			for(int i=2;i<pItems.size();i++){
	    				g.drawString(pItems.get(i).getName(),65 + (i-2)*250,580);
	    			}
	    			
	    			//position of the heart on the screen
	    			if(menuPos <= 2){
		    			g.drawImage(heartPic,30 + 250*menuPos,490,20,20,this);
		    		}
		    		else{
		    			g.drawImage(heartPic,30 + 250*(menuPos-3),560,20,20,this);
		    		}
	    		}
	    	}
	    		
    	}
    	
    	//when the player is not in the main game and the main menu is to be drawn
    	else{
    		startMenu();
    		g.setColor(Color.white);
    		g.setFont(normText2);
    		if(mScreen == TITLE){ //title screen
    			g.drawImage(title,0,0,800,650,this);
    		}
    		else if(mScreen == INSTRUCT){ //instructions screen
    			g.drawImage(instructions,0,-100,800,650,this);
    		}
    		else if(mScreen == NAME){ //enter name screen
    			g.setColor(Color.black);
    			g.fillRect(0,0,800,650);
    			g.setColor(Color.white);
    			for(int i=0;i<26;i++){ //draws all letters of the alphabet
    				g.drawString("" + letters[i] + "",125 + (i%5)*125,225 + (i/5)*70);
    			}
    			//delete and start buttons 
    			g.drawString("Del",500,575);
    			g.drawString("Start",625,575);
    			g.drawImage(heartPic,95 + 125*(menuPos%5),205 + (menuPos/5)*70,20,20,this); //heart
    			g.drawString("Enter Name: " + p.getName(),125,125);
    		}
    		    		
    	}
    	
    	g.setFont(comicSans);
    	if(!textToDraw.isEmpty()){ //drawing textbox on the screen
    		g.setColor(Color.white);
    		g.fillRect(0,450,794,172);
    		g.setColor(Color.black);
    		g.fillRect(8,458,778,156);
    		g.setColor(Color.white);
    		
    		for(int i=0;i<textToDraw.get(textNum).size();i++){
    			g.drawString(textToDraw.get(textNum).get(i),100,490+(i*35));
    		}
    	}
    }
    
   	//loads buffered images when given the filename
    public BufferedImage loadImage(String fileName){
        BufferedImage image = null;
        try
        {
            URL url = getClass().getResource(fileName);
            image = ImageIO.read(url);
        }
        catch(IllegalArgumentException iae)
        {
            System.err.println("arg: " + iae.getMessage());
        }
        catch(IOException ioe)
        {
            System.err.println("read: " + ioe.getMessage());
        }
        if(image == null)
        {
            image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
            System.out.println("unable to load image, returning default");
        }
        return image;
    }
    
    //draws text to the screen in the form of a textbox at the bottom of the screen:
    //splits the text in such a way that each page of text has 4 lines, and every line
    //of text cannot have over 35 characters in it (advances to next line when so)
    public void drawText(String str){ 
    	canMove=false;
		textToDraw.clear();
		String[]words = str.split(" "); //seperates all words by a space
		String tmp = ""; //current line of text
		ArrayList<String> tmp2 = new ArrayList<String>(); //current lines of text on currect page
		boolean lnFlag, pgFlag; //flags to detect if the next line/page is needed to be made
		
		for(String w : words){
			lnFlag = false;
			pgFlag = false;
			
			if(w.equals("/n")){ //automatically advances to next line if this is in the string entered
				lnFlag = true;
			}
			else if(w.equals("/p")){ //automatically advances to next page if this is in the string entered
				lnFlag = true;
				pgFlag = true;
			}
			else{ //adds to current line
				tmp += w + " ";
			}
			
			if(tmp.length() >=35 || lnFlag){ //when the char count is maxed for that line, makes new line
				tmp2.add(tmp);
				tmp = "";
				
				if(tmp2.size() >= 4 || pgFlag){ //when line count for that page is maxed, makes new page
					textToDraw.add(tmp2);
					tmp2 = new ArrayList<String>(tmp2);
					tmp2.clear();
				}
			}
		}
		tmp2.add(tmp); //adds to arraylists
		textToDraw.add(tmp2);
	}
	
    //saves the game based on what save point you encounter (int s is savepoint num)
    public void saveGame(int s){
    	//prints to saveFile0New.txt , updates player information
    	saveWrite.print("" + p.getName() + "," + p.getHp() + "," + p.getMaxHp() + "," + weapon + ",sans," + s + "," + torielFlag);
    	for(Item i : pItems){
    		saveWrite.print("," + i.getName());
    	}
    	
    	//advances to next line in all save files so game can be saved multiple times w/o reloading the file
    	saveWrite.println("");
    	itemWrite.println("");
    	swordWrite.println("");
    	
    	//prints to LevelItemsNew.txt and LevelSwordsNew.txt , saves where the items and swords are in a level 
    	for(Level l : levels){
    		ArrayList<ItemLevel> tmpItem = l.getItems();
    		itemWrite.print("" + tmpItem.size());
    		for(ItemLevel i : tmpItem){
    			itemWrite.print("," + i.getItem().getName()  + "," + i.getX() + "," + i.getY());
    		}
    		itemWrite.println("");
    		
    		ArrayList<SwordLevel> tmpSwords = l.getSwords();
    		swordWrite.print("" + tmpSwords.size());
    		for(SwordLevel i : tmpSwords){
    			swordWrite.print("," + i.getSword().getName()  + "," + i.getX() + "," + i.getY());
    		}
    		swordWrite.println("");
    	}
    }
    
    //stops the program for n miliseconds
    public void pause(int n){
    	try {
			Thread.sleep(n);
		} 
		catch (Exception e) {
			Thread.currentThread().interrupt();
		}
    }
    
    //done to get the direction any projectile will take when shot in order to aim at the player
    public double formula(double x, double y, double side){
    	return (2.6*side)/Math.hypot(x,y);
    }
    
    //draws the player's HP and name on screen
    public void drawStats(Graphics g){
    	//added the pBar update
    	pBar=(int)(((double)(p.getHp())/(double)(p.getMaxHp()))*160);
    	//Moved Health over 20 to the right
    	g.setColor(Color.white);
    	g.drawString(p.getName(),20,500);
    	g.drawString("HP: "+Integer.toString(p.getHp())+"/"+Integer.toString(p.getMaxHp()),110,500);
    	//drawing health bar
    	g.setColor(Color.red);
	    g.fillRect(20,520,160,20);
	    g.setColor(Color.yellow);
	    g.fillRect(20,520,pBar,20);
    }
}
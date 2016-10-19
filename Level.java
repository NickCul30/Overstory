//Level.java
//Nick Culmone, Matt Farias
//A level in the game. Has background/mask, entrances, interactions, items
//and swords that can be picked up on the ground.

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

public class Level{
	private String name; //name of level
	private BufferedImage back; //the level's background
	private BufferedImage mask; //the level's mask
	private ArrayList<Entrance> entrances = new ArrayList<Entrance>(); //all the entrances to the level
	
	//all the interactions you can have with the level that will result in a textbox being drawn
	private ArrayList<Talk> talks = new ArrayList<Talk>();
	
	//all of the items that can be picked up off the ground in the level
	private ArrayList<ItemLevel> items = new ArrayList<ItemLevel>();
	
	//all of the swords that can be picked up off the ground in the level
	private ArrayList<SwordLevel> swords = new ArrayList<SwordLevel>();
	
	
	public Level(String line,String iLine,String sLine) throws IOException{
		String[]stats = line.split(",");
		name = stats[0];
		back = loadImage("backgrounds\\" + stats[0] + ".png");
		mask = loadImage("backgrounds\\" + stats[1] + ".png");
		
		//Entrances
		//=========
		
		//adds to the arraylist of entrances based on the levels.txt file
		int entCount = Integer.parseInt(stats[2].trim());
		for(int i=0;i<entCount;i++){
			int x = Integer.parseInt(stats[3 + i*5]);
			int y = Integer.parseInt(stats[4 + i*5]);
			int r = Integer.parseInt(stats[5 + i*5]);
			int g = Integer.parseInt(stats[6 + i*5]);
			int b = Integer.parseInt(stats[7 + i*5]);
			entrances.add(new Entrance(x,y,r,g,b));			
		}
		
		//adds to the arraylist of interactions based on the levels.txt file
		int tmp = 3 + entCount*5;
		int talkCount = Integer.parseInt(stats[tmp]);
		for(int i=0;i<talkCount;i++){
			String text = stats[tmp + 1 + i*4];
			int r = Integer.parseInt(stats[tmp + 2 + i*4]);
			int g = Integer.parseInt(stats[tmp + 3 + i*4]);
			int b = Integer.parseInt(stats[tmp + 4 + i*4]);
			talks.add(new Talk(text,r,g,b));
		}
		
		//Items/Swords
		//============
		
		//all of the items & swords in the game
		HashTable<Sword> swordList = new HashTable<Sword>();
		HashTable<Item> itemList = new HashTable<Item>();
		
		//loads all the items & swords in the game so their hashCodes can be used when making new items and swords that are
		//on the floor for the player to pick up
		Scanner wordFile =
			new Scanner(new BufferedReader(new FileReader("swords.txt")));
		
		while(wordFile.hasNextLine()==true){
			swordList.add(new Sword(wordFile.nextLine()));
		}
		Scanner wordFile2 =
			new Scanner(new BufferedReader(new FileReader("items.txt")));
		
		while(wordFile2.hasNextLine()==true){
			itemList.add(new Item(wordFile2.nextLine()));
		}
		
		wordFile.close();
		wordFile2.close();
		
		
		String stats2[] = iLine.split(",");
		
		int iCount = Integer.parseInt(stats2[0]);
		
		//loads all of the items that are in the level
		for(int i=0;i<iCount;i++){
			String iName = stats2[1 + i*3];
			int ix = Integer.parseInt(stats2[2 + i*3]);
			int iy = Integer.parseInt(stats2[3 + i*3]);
			Item tmp2 = itemList.get(iName.hashCode());
			items.add(new ItemLevel(tmp2,ix,iy));
		}
		
		
		String stats3[] = sLine.split(",");
		
		int sCount = Integer.parseInt(stats3[0]);
		
		//loads all the swords that are in the level
		for(int i=0;i<sCount;i++){
			String sName = stats3[1 + i*3];
			int sx = Integer.parseInt(stats3[2 + i*3]);
			int sy = Integer.parseInt(stats3[3 + i*3]);
			Sword tmp3 = swordList.get(sName.hashCode());
			swords.add(new SwordLevel(tmp3,sx,sy));
		}
		
	}
	
	//getters
	public String getName(){
		return name;
	}
	public BufferedImage getBack(){
		return back;
	}
	public BufferedImage getMask(){
		return mask;
	}
	public ArrayList<Entrance> getEntrances(){
		return entrances;
	}
	public ArrayList<Talk> getTalks(){
		return talks;
	}
	public ArrayList<ItemLevel> getItems(){
		return items;
	}
	public ArrayList<SwordLevel> getSwords(){
		return swords;
	}
	
	//loads buffered images, used to load background and mask
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
}
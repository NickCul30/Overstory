//Enemy.java
//Matthew Farias, Nicholas Culmone

/*The class for foes that the player will face during the game
 */

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

public class Enemy{
	private String name; //name of the foe
	private Image sprite; //sprite image
	private int damage; //how much damage each bomb this foe does
	private int hp; //The amount of HP the foe currently has
	private int maxHp; //the Max HP for the foe
	private int atkNum; //how many bombs this foe will fire every turn
	private ArrayList<String>phrases=new ArrayList<String>(); //the list of phrases it can say
	private Random rand=new Random(); //variable for getting random numbers
	
	public Enemy(String line){
		String[]values=line.split(",");
		name = values[0];
		sprite = new ImageIcon("enemies\\" + values[1]).getImage();
		damage = Integer.parseInt(values[2].trim());
		hp = Integer.parseInt(values[3].trim());
		maxHp = Integer.parseInt(values[4].trim());
		atkNum = Integer.parseInt(values[5].trim());
		for(int i=6;i<values.length;i++){
			phrases.add(values[i]);
		}
	}
	//getter methods
	public String getName(){
		return name;
	}
	public int getDamage(){
		return damage;
	}
	public String getPhrase(){
		int i=rand.nextInt(phrases.size());
		return phrases.get(i);
	}
	public int getHp(){
		return hp;
	}
	public int getMaxHp(){
		return maxHp;
	}
	public int getAtkNum(){
		return atkNum;
	}
	public Image getSprite(){
		return sprite;
	}
	
	public void hit(int n){ //enemy loses n HP
		hp-=n;
	}
	public void respawn(){ //Resets the HP
		hp=maxHp;
	}
}
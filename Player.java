//Player.java
//Nicholas Culmone, Matthew Farias
/*This is the class for the player object and stores all the game progress
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


public class Player{
	private int maxHp, hp; //HP variables
	private String name; //The player's name
	private ArrayList<String>items = new ArrayList<String>(); //the list of all items the player currently has
	private String weapon; //the weapon equiped
	private String sName; //name of the sprite folder/sprites
	private ArrayList<ArrayList<Image>>sprites=new ArrayList<ArrayList<Image>>(); //a list that holds all the sprites
	private HashTable<Item>itemList=new HashTable<Item>(); //list of all items in the game
	private HashTable<Sword>swordList=new HashTable<Sword>(); //list of all weapons in the game
	private int save; //the save point the player is at
	private boolean toriel; //if the tutorial has been completed
	
	
	public Player(String n){
		String[]values=n.split(",");
		name=values[0];
		hp=Integer.parseInt(values[1].trim());
		maxHp=Integer.parseInt(values[2].trim());
		weapon=values[3];
		sName=values[4];
		save = Integer.parseInt(values[5]);
		toriel = Boolean.parseBoolean(values[6]);
		
		for(int i=7;i<values.length;i++){ //for the rest of the values in the line being read, they will be added as the items the player has
			items.add(values[i]);
		}
		String[]dir={"Left","Right","Up","Down"};
		Image pic;
		for(int i=0;i<4;i++){ //adds all the sprites to the list
			sprites.add(new ArrayList<Image>());
			for(int j=0;j<4;j++){
				pic=new ImageIcon(sName+"//"+sName+dir[i]+"//"+sName+dir[i]+j+".png").getImage();
				sprites.get(i).add(pic);
			}
		}
		
	}
	
	//getter methods 
	public String getName(){
		return name;
	}
	public int getHp(){
		return hp;
	}
	public int getMaxHp(){
		return maxHp;
	}
	public ArrayList<String> getItems(){
		return items;
	}
	public String getWeapon(){
		return weapon;
	}
	public int getSave(){
		return save;
	}
	public boolean getToriel(){
		return toriel;
	}
	
	public HashTable<Item> getItemList(){
		return itemList;
	}
	
	public HashTable<Sword> getSwordList(){
		return swordList;
	}
	public Image getSprite(int pos, int n){
		return sprites.get(pos).get(n);
	}
	
	public void fullHeal(){ //resets the hp
		hp = maxHp;
	}
	public void hit(int n){ //player takes n damage
		hp-=n;
		if(hp<0){
			hp=0;
		}
	}
	public void heal(int n){ //olayer is healed by n amount (if higher than maxHp it is reduced to maxHp
		hp += n;
		if(hp > maxHp){
			hp = maxHp;
		}
	}
	//setters
	public void setName(String n){
		name = n;
	}
	public void setSave(int n){
		save = n;
	}
}
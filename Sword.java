//Sword.java
//Nicholas Culmone, Matt Farias
//An sword in the game that does damage to an enemy.

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.Timer;
import java.util.*;

public class Sword{
	private String name; //name of sword
	private Image sprite; //sword's sprite
	private int damage; //amt of damage sword does per hit (at max)
	private Color c; //colour in the mask specified for that sword
	
	public Sword(String line){
		String[]values=line.split(",");
		name = values[0];
		sprite = new ImageIcon("swords\\" + values[1] +"0.png").getImage();
		damage = Integer.parseInt(values[2].trim());
		int r = Integer.parseInt(values[3]);
		int g = Integer.parseInt(values[4]);
		int b = Integer.parseInt(values[5]);
		c = new Color(r,g,b);
	}
	
	//getters
	public String getName(){
		return name;
	}
	public Image getSprite(){
		return sprite;
	}
	public int getDamage(){
		return damage;
	}
	public Color getC(){
		return c;
	}
	public String toString(){
		return name;
	}
	
	//done so the item can be accessed in other classes when only given the name,
	//this is done when save files are loaded
	@Override
		public int hashCode(){
			return name.hashCode();
		}
}
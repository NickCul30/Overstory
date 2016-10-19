//Item.java
//Nicholas Culmone, Matthew Farias
//An item in the game that can heal the player.

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

public class Item{
	private String name; //name of item
	private Image sprite; //item's sprite
	private int value; //the amt of hp it can heal
	private Color c; //colour in the mask specified for that item
	
	public Item(String line){
		String[]values=line.split(",");
		name = values[0];
		sprite = new ImageIcon("items\\" + values[1] +"0.png").getImage();
		value = Integer.parseInt(values[2].trim());
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
	public int getValue(){
		return value;
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
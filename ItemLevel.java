//ItemLevel.java
//Nicholas Culmone & Matthew Farias
//Class for items that are in the level:
//eg. an item on the floor that the player can pick up.

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


public class ItemLevel{
	private Item item; //The item itself
	private int x; //x,y position that the sprite is to be drawn at
	private int y;
	
	public ItemLevel(Item i,int x1,int y1){
		item = i;
		x = x1;
		y = y1;
	}
	
	//getters
	public Item getItem(){
		return item;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public String toString(){
		return item.getName();
	}
}
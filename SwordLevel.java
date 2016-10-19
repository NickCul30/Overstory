//SwordLevel.java
//Nicholas Culmone & Matthew Farias
//Class for swords that are in the level:
//eg. an sword on the floor that the player can pick up.

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


public class SwordLevel{
	private Sword sword; //the sword itself
	private int x; //x,y position that the sprite is to be drawn at
	private int y;
	
	public SwordLevel(Sword s,int x1,int y1){
		sword = s;
		x = x1;
		y = y1;
	}
	
	//getters
	public Sword getSword(){
		return sword;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public String toString(){
		return sword.getName();
	}
}
//savePoint.java
//Nicholas Culmone & Matthew Farias
//the save points scattered throughout the game.

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

public class savePoint{
	private int level; //which level the save point is in
	private Point pos; //the x,y position where the player will spawn when the game is loaded with this savepoint
	private Color c; //colour in the mask specified for that savePoint
	
    public savePoint(String line){
    	String[]stats = line.split(",");
    	
    	level = Integer.parseInt(stats[0]);
    	
    	int x = Integer.parseInt(stats[1]);
    	int y = Integer.parseInt(stats[2]);
    	pos = new Point(x,y);
    	
    	int r = Integer.parseInt(stats[3]);
    	int g = Integer.parseInt(stats[4]);
    	int b = Integer.parseInt(stats[5]);
    	c = new Color(r,g,b);
    }
    
    //getters
    public int getLevel(){
    	return level;
    }
    public Point getPos(){
    	return pos;
    }
    public Color getC(){
    	return c;
    }
}
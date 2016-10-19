//Entrance.java
//Nicholas Culmone & Matthew Farias
//An entrance to any given level

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

public class Entrance {
	//the spawn point of the player (x,y)
	private int x;
	private int y;
	//the colour in he mask specified for that entrance
	private Color c;
	
    public Entrance(int x,int y,int r,int g,int b){
    	this.x = x;
    	this.y = y;
    	c = new Color(r,g,b);    	
    }
    
    //getters
    public int getX(){
    	return x;
    }
    public int getY(){
    	return y;
    }
    public Color getC(){
    	return c;
    }
    
}
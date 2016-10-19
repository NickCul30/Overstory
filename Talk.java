//Talk.java
//Nicholas Culmone & Matthew Farias
//An interaction that the player can have when they press enter on something that can be
//talked to.

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

public class Talk{
	private String text; //the dialogue
	private Color c; //colour in the mask specified for that interaction
	
	public Talk(String t,int r,int g,int b){
		text = t;
		c = new Color(r,g,b);
	}
	
	//getters
	public String getText(){
		return text;
	}
	public Color getC(){
		return c;
	}
}
//Bomb.java
//Matthew Farias, Nicholas Culmone

/*This class is a new object type of projectiles that are shot at the
 *player during the battle
 */
public class Bomb{
	private double x,y,velX,velY;	
	
	public Bomb(double x1,double y1,double velX1, double velY1){
		x = x1;
		y = y1;
		velX=velX1;
		velY=velY1;
	}
	
	//getter methods
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
	public double getVelX(){
		return velX;
	}
	public double getVelY(){
		return velY;
	}
	public void move(){ //moves the bomb over by the velocity
		x-=velX;
		y-=velY;
	}
}
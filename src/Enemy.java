import java.awt.Rectangle;
import java.util.Random;


public class Enemy implements Runnable{
	
	private Rectangle AI;
	Random r = new Random();
	private int xDir;
	private int yDir;
	
	/**
	 * FORMAT IS AS FOLLOWS
	 * return r.nextInt(highNumber - lowNumber + 1) + lowNumber; 
	 * @return
	 */
	
	public Enemy(){
		AI = new Rectangle();
		AI.x = getX();
		AI.y = getY();
		AI.width = 20;
		AI.height = 22;
	}
	
	public int getX(){
		return r.nextInt(550 - 40 + 1) + 40;
	}
	
	public int getY(){
		return r.nextInt(250 - 25 + 1) + 25;
	}
	
	public int getDirection(){
		//Array of directions.
		int[] dirs = new int[3];
		dirs[0] = -1;
		dirs[1] = 0;
		dirs[2] = 1;
		int dir = dirs[r.nextInt(3)];
		return dir;
	}
	
	public void setXDir(int dir){
		xDir = dir;
	}
	
	public void setYDir(int dir){
		yDir = dir;
	}
	
	public void move(){
		AI.x += xDir;
		AI.y += yDir;
	}
	
	public void run(){
		move();
	}

}

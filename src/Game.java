import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Game extends JFrame implements Runnable{
	
	//Menu
	private boolean gameStarted = false;
	Rectangle startButton = new Rectangle(225, 250, 150, 50);
	Rectangle quitButton = new Rectangle(225, 325, 150, 50);
	private boolean startHover;
	private boolean quitHover;
	
	//Enemy class for x,y coordinates
	private Enemy enemy = new Enemy();
	
	//Enemies
	private Rectangle enemy1;
	private int e1x = enemy.getX();
	private int e1y = enemy.getY();
	
	//Text and shit
	private int score;
	private int pp = 10;
	private boolean gameOver = false;
	
	//User's character
	private int x, y, xDir, yDir;
	
	//Bullet
	private boolean readyToFire, shot = false;
    private int bx, by; //Bullet coordinates
    private Rectangle bullet;    
	
	//Double buffering
	private Image dbImage;
	private Graphics dbG;
	
	//Font
	private Font font = new Font("Consolas", Font.PLAIN, 20);
	private Font titleFont = new Font("Consolas", Font.BOLD, 50);
	
	//User loaded images
	Image face;
	Image background;
	Image enemyFace;
	Image title;
	
	//Best run
	private int bestScore = 0;
	
	/**
	 * Class to listen for key inputs based on presses and releases.
	 * @author Omkar
	 *
	 */
	public class Keyboard extends KeyAdapter{
		
		public void keyPressed(KeyEvent e){
			int keyCode = e.getKeyCode();
			
			if(keyCode == e.VK_ENTER){
			}
			
			if(keyCode == e.VK_LEFT){
				if(!(pp == 0))
					setxDir(-2);
				else
					endGame();
			}			
			if(keyCode == e.VK_RIGHT){
				if(!(pp == 0))
					setxDir(2);
				else
					endGame();
			}			
			if(keyCode == e.VK_UP){
				if(!(pp == 0)){
					
				}else
					endGame();
			}			
			if(keyCode == e.VK_DOWN){
				if(!(pp == 0)){
					
				}else
					endGame();
			}
			if(keyCode == e.VK_SPACE){
				if(pp == 0)
					gameOver = true;
				else if(bullet == null){
	        		readyToFire = true;
	        	}
	        	if(readyToFire && !gameOver && gameStarted){
	        		by = y - 8;
	        		bx = x + 18;
	        		bullet = new Rectangle(bx, by, 3, 5);
	        		shot = true;
	        		pp--;
	        	}
        		readyToFire = false;
	        }
			if(keyCode == e.VK_Y){
				if(gameOver){
					resetGame();
				}
			}
			if(keyCode == e.VK_N){
				if(gameOver){
					toMenu();
				}
			}

		}
		
		public void keyReleased(KeyEvent e){
			int keyCode = e.getKeyCode();
			
			if(keyCode == e.VK_ENTER){
				
			}
			if(keyCode == e.VK_LEFT){
				setxDir(0);
			}			
			if(keyCode == e.VK_RIGHT){
				setxDir(0);
			}			
			if(keyCode == e.VK_UP){
			}			
			if(keyCode == e.VK_DOWN){
			}
			if(keyCode == e.VK_SPACE){
	        	readyToFire = false;
	        	if(bullet.y <= -5){
	        		bullet = new Rectangle(0, 0, 0, 0);
	        		shot = false;
	        		readyToFire = true;
	        		
	        	}
	        }
			if(keyCode == e.VK_Y){
				
			}
			if(keyCode == e.VK_N){
				
			}
			
			e.consume();
		}
	}
	//END OF KEY LISTENER CLASS//
	
	/**
	 * Class to listen to Mouse inputs. (Used for the menu.)
	 */
	public class Mouse extends MouseAdapter{
		
		public void mousePressed(MouseEvent m){
			int mouseX = m.getX();
			int mouseY = m.getY();
			
			if(mouseX > startButton.x && mouseX < startButton.x + startButton.width && mouseY > startButton.y && mouseY < startButton.y + startButton.height){
				if(!gameStarted)
					startGame();
			}
			
			if(mouseX > quitButton.x && mouseX < quitButton.x + quitButton.width && mouseY > quitButton.y && mouseY < quitButton.y + quitButton.height){
				if(!gameStarted)
					System.exit(0);
			}
		}
		
		public void mouseReleased(MouseEvent m){
			
		}
	}
	
	/**
	 * Constructor to create the keyListener class AL and set up basic defaults x position, y position, Title, Window Size, Resizability, Visibility, and Default Close Operation.
	 */
	public Game(){
		
		//Load images
		ImageIcon bg = new ImageIcon("src/images/BG.jpg");
		ImageIcon i = new ImageIcon("src/images/face.png");
		ImageIcon ef = new ImageIcon("src/images/enemyface.png");
		ImageIcon ttl = new ImageIcon("src/images/title.png");
		face = i.getImage();
		background = bg.getImage();
		enemyFace = ef.getImage();
		title = ttl.getImage();
		
		//Game properties
		addKeyListener(new Keyboard());
		addMouseListener(new Mouse());
		setTitle("Splash! - Developed by Omkar Moghe");
		setSize(600, 600);
		setResizable(false);
		setVisible(true);
		setBackground(Color.GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		x = 270;
		y = 540;
	}
	
	public void run(){
		try{
			while(true){
				shoot();
				move();
				Thread.sleep(5);
			}
		}catch(Exception e){
			System.out.println("Error" + e.getMessage());
		}
	}
	
	public void startGame(){
		gameStarted = true;
	}
	
	public void resetGame(){
		gameStarted = true;
		gameOver = false;
		
		//Reset HUD (Score & PP)
		pp = 10;
		score = 0;
		
		//Reset enemy
		e1x = enemy.getX();
		e1y = enemy.getY();
		
		//Reset character
		x = 270;
		y = 540;
	}
	
	public void toMenu(){
		gameStarted = false;
		gameOver = false;
		pp = 10;
		score = 0;
	}
	
	public void endGame(){
		gameStarted = true;
		gameOver = true;
	}
	
	/**
	 * Method to move the object in the x and y direction. Also handles collision detection. (-x/+x = left/right). (-y/+y = up/down).
	 */
	public void move(){
		x += xDir;
		
		//Edge of screen collision detection.
		if(x <= 0)
			x = 0;
		if(x >= getWidth()-60)
			x = getWidth()-60;
	}
	
	public void moveEnemy(){
		
	}
	
	/**
	 * Method to shoot the bullet. Currently shoots straight up (-y dir).
	 */
	public void shoot(){
		if(shot){
			bullet.y -= 5;
			collision();
		}
	}
	
	/**
	 * Method to reset bullet and enemy when collision is detected. Collision detection occurs here as well.
	 */
	public void collision(){
		if(enemy1.intersects(bullet)){
			//Reset bullet
			bullet = new Rectangle(0, 0, 0, 0);
    		shot = false;
    		readyToFire = true;
    		
    		//Reset enemy
    		enemy1 = new Rectangle(0, 0, 0, 0);
    		
    		e1x = enemy.getX();
    		e1y = enemy.getY();
    		
    		//Add score
    		score += 10;
    		
    		//Add ammo
    		pp++;
		}
	}
	
	public void setxDir(int xD){
		xDir = xD;
	}
	
	public void setyDir(int yD){
		yDir = yD;
	}
	
	/**
	 * Double buffering to refresh the paint screen. DONT TOUCH THIS SHIT.
	 */
	public void paint(Graphics g){
		dbImage = createImage(getWidth(), getHeight());
		dbG = dbImage.getGraphics();
		paintComponent(dbG);
		g.drawImage(dbImage, 0, 0, this);
	}
	
	/**
	 * Game drawings. Add/remove stuff here.
	 */
	public void paintComponent(Graphics g){
		//Background image
		g.drawImage(background, 0, 0, this);
		//g.drawString(mX + ", " + mY, 10, 300); MOUSE COORDS FOR TESTING
		
		if(!gameStarted){
			//Set font
			g.setFont(titleFont);
			g.setColor(Color.WHITE);
			
			//Title
			g.drawImage(title, 170, 75, this);
			
			//Menu buttons
			if(!startHover){
				g.setColor(Color.BLACK);
				g.fillRect(startButton.x, startButton.y, startButton.width, startButton.height);
			}else{
				g.setColor(Color.RED);
				g.fillRect(startButton.x, startButton.y, startButton.width, startButton.height);
			}
			
			if(!quitHover){
				g.setColor(Color.BLACK);
				g.fillRect(quitButton.x, quitButton.y, quitButton.width, quitButton.height);
			}else{
				g.setColor(Color.RED);
				g.fillRect(quitButton.x, quitButton.y, quitButton.width, quitButton.height);
			}
			g.setFont(font);
			g.setColor(Color.WHITE);
			g.drawString("Start", startButton.x + 47, startButton.y + 30);
			g.drawString("Quit", quitButton.x + 50, quitButton.y + 30);
			
			
		}else{
			//Score
			g.setFont(font);
			g.setColor(Color.WHITE);
			g.drawString("Score: " + score, 10, 50);
			
			//Ammo
			g.drawString("PP: " + pp, 500, 590);
			
			//User character
			g.drawImage(face, x, y, this);
			
			//Enemies
			enemy1 = new Rectangle(e1x, e1y, 20, 22);
			g.drawImage(enemyFace, e1x, e1y, this);
			moveEnemy();
			
			//Angles (NOT IN USE)
			double angle = (Math.atan2(y - e1y, x - e1x));
			
			
			//Bullet
			if(shot){
	        	g.setColor(Color.BLACK);
	        	g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);	        		
	        }
			
			//Game over
			if(gameOver){
				if(score > bestScore)
					bestScore = score;
				g.setFont(titleFont);
				g.setColor(Color.RED);
				g.drawString("GAME OVER", 170, 250);
				g.setFont(font);
				g.drawString("Final Score: " + score, 210, 290);
				g.drawString("Best Score: " + bestScore, 210, 320);
				g.drawString("Play again? (Y/N)", 210, 350);
			}
		}
		
		repaint();
	}
	
}

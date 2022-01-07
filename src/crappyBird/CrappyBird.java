package crappyBird;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

public class CrappyBird implements ActionListener, MouseListener, KeyListener{
	
	public static CrappyBird crappyBird;
	
	public final int WIDTH = 1200, HEIGHT = 800;
	
	public Renderer renderer;
	
	public Rectangle bird;
	
	public int ticks;
	
	public int yMotion;
	
	public int score;
	
	public ArrayList<Rectangle> pipes;
	
	public boolean gameOver, started;
	
	public Random rand;
	
	public CrappyBird() {
		
		JFrame jframe = new JFrame();
		
		Timer timer = new Timer(20, this);
		
		renderer = new Renderer();
		
		jframe.add(renderer);
		
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jframe.setSize(WIDTH, HEIGHT);
		
		jframe.addMouseListener(this);
		
		jframe.addKeyListener(this);
		
		jframe.setResizable(false);
		
		jframe.setTitle("Crappy Bird");
		
		jframe.setVisible(true);
		
		bird = new Rectangle( WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
		
		rand = new Random();
		
		pipes = new ArrayList<Rectangle>();
		
		//populate pipes list
		addPipe(true);
		addPipe(true);
		addPipe(true);
		addPipe(true);
		
		timer.start();
				
	}
	
	public void addPipe(boolean start) {
		
		int space = 300;
		
		int pipeWidth = 100;
		
		//minimum height for pipe is 50 + however much to make diverse pipes
		int pipeHeight = 50 + rand.nextInt(300);
		
		if(start) { //if it's a starting pipe
		
			//this pipe will come from the top
			pipes.add(new Rectangle(WIDTH + pipeWidth + pipes.size() * 300, HEIGHT - pipeHeight - 120, pipeWidth, pipeHeight));
		
			//this pipe will come from the bottom
			pipes.add(new Rectangle(WIDTH + pipeWidth + (pipes.size() - 1) * 300, 0, pipeWidth, HEIGHT - pipeHeight - space));
		
		}
		
		else { //appended to last pipe we have
			
			//this pipe will come from the top
			pipes.add(new Rectangle(pipes.get(pipes.size() - 1).x + 600, HEIGHT - pipeHeight - 120, pipeWidth, pipeHeight));
		
			//this pipe will come from the bottom
			pipes.add(new Rectangle(pipes.get(pipes.size() - 1).x , 0, pipeWidth, HEIGHT - pipeHeight - space));
			
		}
		
	}
	
	public void paintPipe(Graphics g, Rectangle pipe) {
		
		g.setColor(Color.green.darker());
		g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
		
	}
	
	public void jump() {
		
		if(gameOver) {
			
			bird = new Rectangle( WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
			
			pipes.clear();
			
			yMotion = 0;
			
			score = 0;
			
			//populate pipes list
			addPipe(true);
			addPipe(true);
			addPipe(true);
			addPipe(true);
			
			gameOver = false;
			
		}
		
		if(!started) {
			
			started = true;
			
		}
		
		else if(!gameOver) {
			
			if(yMotion > 0) {
				
				yMotion = 0;
				
			}
			
			//gravity makes bird sink a little for every jump
			yMotion -= 10;
			
		}
		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		ticks++;
		
		int speed = 10;
		
		if(started) {
		
			for(int i = 0; i < pipes.size(); i ++) {
			
				Rectangle pipe = pipes.get(i);
			
				pipe.x -= speed;
			
			}
		
			if(ticks % 2 == 0 && yMotion < 15) {
			
				yMotion += 2;
			
			}
		
			for(int i = 0; i < pipes.size(); i ++) {
			
				Rectangle pipe = pipes.get(i);
			
				//if we approach edge of screen, remove pipe
				if(pipe.x + pipe.width < 0) {
				
					pipes.remove(pipe);
				
					//if it's a pipe coming from the top, add another pipe
					if(pipe.y == 0) {
				
						addPipe(false);
				
					}
				}
			
			}
		
			bird.y += yMotion;
		
			//when bird encounters pipe
			for(Rectangle pipe : pipes) {
				
				//when bird is at center of column and only once
				if(pipe.y == 0 && bird.x + bird.width / 2 > pipe.x + pipe.width / 2 - 10 && bird.x + bird.width / 2 < pipe.x + pipe.width / 2 + 10) {
					
					score += 10;
					
				}
			
				//if bird hits pipe
				if(bird.intersects(pipe)) {
				
					gameOver = true;
					
					if(bird.x <= pipe.x) {
						
						//so bird gets swept away by next pipe
						bird.x = pipe.x - bird.width;
						
					}
					
					else {
						
						if(pipe.y != 0) {
							
							bird.y = pipe.y - bird.height;
							
						}
						
						else if(bird.y < pipe.height) {
							
							bird.y = pipe.height;
							
						}
					}
				
				}
			
			}
		
			//when bird hits top or bottom
			if(bird.y > HEIGHT - 120 || bird.y < 0) {
			
				gameOver = true;
			
			}
			
			//when bird hits pipe at high height, it sinks
			if(bird.y + yMotion >= HEIGHT - 120) {
				
				bird.y = HEIGHT - 120 - bird.height;
				
			}
		
		}
		
		renderer.repaint();
		
	}
	
	public void repaint(Graphics g) {
		
		//sky
		g.setColor(Color.cyan);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		//dirt
		g.setColor(Color.orange.darker().darker());
		g.fillRect(0, HEIGHT - 120, WIDTH, 120);
		
		
		//grass
		g.setColor(Color.green);
		g.fillRect(0, HEIGHT - 120, WIDTH, 20);
		
		//bird
		g.setColor(Color.yellow.darker());
		g.fillRect(bird.x, bird.y, bird.width, bird.height);
		
		for(Rectangle pipe : pipes) {
			
			paintPipe(g, pipe);
			
		}
		
		g.setColor(Color.black);
		
		g.setFont(new Font("Arial", 1, 100));
		
		if(!started) {
			
			g.drawString("Click to start", 340, HEIGHT / 2 - 20);
			
		}
		
		if(gameOver) {
			
			g.drawString("Game Over", 340, HEIGHT / 2 - 50);
			
			g.drawString(String.valueOf(score), 570, HEIGHT / 2 + 50);
			
		}
		
		if(!gameOver && started) {
			
			g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
			
		}
		
	}
	
	public static void main(String[] args) {
		
		crappyBird = new CrappyBird();
		
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		jump();
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			
			jump();
			
		}
		
	}

}

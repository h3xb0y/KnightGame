package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

import TileMap.Background;

import javax.imageio.ImageIO;

public class MenuState extends GameState
{
	private Background bg;
	private Image image1;
	
	private int currentChoice = 0;
	private String[] options = 
		{
			"Start",
			"Quit"
		};

	private Font font,footFont,titleFont;
	
	public MenuState(GameStateManager gsm)
	{
		this.gsm = gsm;
		
		try 
		{
			bg = new Background("/Backgrounds/menubg.gif", 1);
			bg.setVector(-0.1, 0);
			image1 = ImageIO.read(getClass().getResourceAsStream("/Backgrounds/git-icon.gif"));

			titleFont = new Font("Century Gothic", Font.PLAIN, 28);
			
			footFont = new Font("Arial", Font.PLAIN, 9);
//			footColor = new Font(Color.blue);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void init() {}
	public void update() 
	{
		bg.update();
	}
	public void draw(Graphics2D g ) {
		// draw bg
		bg.draw(g);
		
		// draw title
		g.setColor(Color.GREEN);
		g.setFont(titleFont);
		g.drawString("Knight Game", 70, 70);
		g.drawImage(image1, 70, 210, null);
		g.setFont(footFont);
		g.setColor(Color.BLACK);
		g.drawString("github.com/h3xb0y/KnightGame", 95, 230);



		//draw menu options
		g.setFont(font);
		for(int i = 0; i < options.length; i++) 
		{
			if (i == currentChoice)
			{
				g.setColor(Color.GREEN);

			}
			else 
			{
				g.setColor(Color.BLACK);
			}
			g.drawString(options[i], 145, 140 + i * 15);
		}
	}
	
	private void select()
	{
		if(currentChoice == 0)
		{
			// start
			gsm.setState(GameStateManager.LEVEL1STATE);
			
		}
		if(currentChoice == 1)
		{
			// quit
			System.exit(0);
		}
	}
	
	public void keyPressed(int k) 
	{
		if(k == KeyEvent.VK_ENTER)
		{
			select();
		}
		if(k == KeyEvent.VK_UP)
		{
			currentChoice --;
			if(currentChoice == -1)
			{
				currentChoice = options.length -1;
			}
		}
		if(k == KeyEvent.VK_DOWN)
		{
			currentChoice ++;
			if(currentChoice == options.length)
			{
				currentChoice = 0;
			}
		}
	}
	public void keyReleased(int k) {}
}

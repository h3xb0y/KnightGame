package Entity.HUD;

import Entity.Player;
import Main.GameApplication;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.imageio.ImageIO;
import javax.swing.*;

public class HUD {
    private JFrame window;
	private Player player;
    private Image image1,
            image2,image3,
            inventory,shop,stats;
    private boolean inventoryEnabled = false,
            shopEnabled = false,
            statsEnabled = false;
    private Font font,barFont;
    private int x,y;

    public HUD(Player p)
    {
        player = p;

        try
        {
            image1 = ImageIO.read(getClass().getResourceAsStream("/HUD/hud.gif"));
            image2 = ImageIO.read(getClass().getResourceAsStream("/HUD/buttons.gif"));
            image3 = ImageIO.read(getClass().getResourceAsStream("/HUD/bar.gif"));
            inventory = ImageIO.read(getClass().getResourceAsStream("/HUD/inventory.gif"));
            shop = ImageIO.read(getClass().getResourceAsStream("/HUD/shop.gif"));
            stats = ImageIO.read(getClass().getResourceAsStream("/HUD/stats.gif"));

            font = new Font("Arial", Font.PLAIN, 14);
			barFont = new Font("Arial", Font.PLAIN, 10);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}


	}


	public void draw(Graphics2D g)
	{
		g.drawImage(image1, 0, 10, null);
		g.drawImage(image2, 275, -3, null);
		g.drawImage(image3, -30, 50, null);
		g.setFont(barFont);
		g.setColor(Color.YELLOW);
		g.drawString(String.valueOf(player.getMoney()),35,66);
		g.setColor(Color.RED);
		g.drawString(player.getHunger()+"%",35,81);
		g.setFont(font);
        g.fillRect(46,13,player.getHealth()*9,8);
		g.setColor(Color.blue);
		g.fillRect(46,24,player.getArmor()*9,8);
		g.setColor(Color.green);
		g.fillRect(46,35,player.getFire()/55,7);

        window = GameApplication.getWindow();
        window.addMouseListener(listener);
        if (inventoryEnabled){
            // инвентарь рыцаря
            new DrawInventory(g,player,new Font("Arial", Font.PLAIN, 9),inventory,x,y);

        }
        if (shopEnabled){
            // магазин
           new DrawShop(g,player,new Font("Arial", Font.PLAIN, 9),shop,x,y);
        }
        if (statsEnabled){
            // статы рыцаря
            new DrawStats(g,player,barFont,stats);
        }

	}

    /**
     * отслеживаем координаты нажатия мыши
     * и соответственно включаем/выключаем
     * инвентарь, магазин, статистику персонажа
     */
    private MouseListener listener = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            x=e.getX()/2;
            y=e.getY()/2;
            if ((x>290 && x<315) && (y>20 && y<40)) {
                inventoryEnabled = true;
                statsEnabled = false;
                shopEnabled = false;
            }
            if ((x>290 && x<315) && (y>45 && y<75)) {
                inventoryEnabled = false;
                statsEnabled = true;
                shopEnabled = false;
            }
            if ((x>290 && x<315) && (y>75 && y<100)) {
                inventoryEnabled = false;
                statsEnabled = false;
                shopEnabled = true;
            }
            if ((x>290 && x<315) && (y>105 && y<130)) {
                inventoryEnabled = false;
                statsEnabled = false;
                shopEnabled = false;
            }

        }
    };


}

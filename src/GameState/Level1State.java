package GameState;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Entity.*;
import Entity.Enemies.Bat;
import Entity.Enemies.Devil;
import Entity.HUD.HUD;
import Entity.Items.*;
import Entity.Enemies.Slugger;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

public class Level1State extends GameState
{
	private static TileMap tileMap;
	private Background bg;
	
	private Player player;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Item> items;
	private ArrayList<Explosion> explosions;
	
	private HUD hud;
	
	public Level1State(GameStateManager gsm)
	{
		this.gsm = gsm;
		init();
	}
	
	public void init() 
	{
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/grasstileset.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(0.07);
		
		
		bg = new Background("/Backgrounds/grassbg1.gif", 0.01);
		
		player = new Player(tileMap);
		player.setPosition(100, 100);
		
		populateEnemies();
		
		explosions = new ArrayList<Explosion>();
		
		hud = new HUD(player);
	}
	
	private void populateEnemies()
	{
		enemies = new ArrayList<Enemy>();
		items = new ArrayList<Item>();
		
		Slugger s;
		Bat b;
		Devil d;
		KnightSwordItem KnightSwordItem;
		KnightShieldItem ShieldItem;
		Point[] points = new Point[] {
			new Point(200,100),
			new Point(860, 200),
			new Point(1525, 200),
			new Point(1680, 200),
			new Point(1800, 200)
		};
		for(int i = 0; i < points.length; i++)
		{
			if (i<3) {
				if (i == 0)
				{
					KnightSwordItem = new KnightSwordItem(tileMap);
					KnightSwordItem.setPosition(points[i].x, points[i].y);
					items.add(KnightSwordItem);
				} else if (i==1) {
					ShieldItem = new KnightShieldItem(tileMap);
					ShieldItem.setPosition(points[i].x, points[i].y);
					items.add(ShieldItem);
				} else {
					d = new Devil(tileMap);
					d.setPosition(points[i].x, points[i].y);
					enemies.add(d);
				}
			} else {
				b = new Bat(tileMap);
				b.setPosition(points[i].x, points[i].y);
				enemies.add(b);
			}
		}
		b = new Bat(tileMap);
		b.setPosition(1000, 200);
		enemies.add(b);
		d = new Devil(tileMap);
		d.setPosition(850, 200);
		enemies.add(d);
		// кидаем шмотки в начало карты для тестинга
		AmuletItem AmuletItem = new AmuletItem(tileMap);
		AmuletItem.setPosition(210,100);
		items.add(AmuletItem);
		ArmorItem armorItem = new ArmorItem(tileMap);
		armorItem.setPosition(220,100);
		items.add(armorItem);
		KnightHelmetItem knightHelmetItem = new KnightHelmetItem(tileMap);
		knightHelmetItem.setPosition(230,100);
		items.add(knightHelmetItem);
		KnightSword2Item knightSword2Item = new KnightSword2Item(tileMap);
		knightSword2Item.setPosition(240,100);
		items.add(knightSword2Item);
		KnightShield2Item knightShield2Item = new KnightShield2Item(tileMap);
		knightShield2Item.setPosition(250,100);
		items.add(knightShield2Item);
		KnightBootsItem knightBootsItem = new KnightBootsItem(tileMap);
		knightBootsItem.setPosition(260,100);
		items.add(knightBootsItem);
	}
	
	
	public void update() 
	{
		// update player
		player.update();
		
		tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
		
		// set background
		bg.setPosition(tileMap.getx(), tileMap.gety()); 
		
		// attack enemies
		player.checkAttack(enemies);
		player.checkItem(items);
		
		// проверяем и апдейтим всех ботов на карте
		for(int i = 0; i < enemies.size(); i++)
		{
			Enemy e  = enemies.get(i);
			e.update();
			if(e.isDead())
			{
				//добавляем 150 монет за убийство бота и опыт
				player.AddMoney(150);
				player.AddExp(50);
				//удаляем бота с карты
				enemies.remove(i);
				i--;
				explosions.add(new Explosion(e.getx(), e.gety()));
			}
		}
		// проверям и апдейтим итемы на карте
		for (int i = 0 ; i < items.size();i++){
			Item it = items.get(i);
			it.update();
			if(it.isPicked())
			{
				//удаляем предмет с карты
				items.remove(i);
				i--;
				explosions.add(new Explosion(it.getx(), it.gety()));
			}
		}
		
		// update all explosions
		for(int i = 0; i < explosions.size(); i ++)
		{
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove())
					{
						explosions.remove(i);
						i--;
					}
		}
		
	}
	
	public void draw(Graphics2D g) 
	{
		// draw background
		bg.draw(g); 
		
		// draw tilemap
		tileMap.draw(g);	
		
		// draw player
		player.draw(g);
		
		// draw enemies
		for(int i = 0; i < enemies.size(); i++)
		{
			enemies.get(i).draw(g);
		}

		for(int i = 0; i < items.size(); i++)
		{
			items.get(i).draw(g);
		}
		
		// draw explosions
		for(int i = 0; i < explosions.size(); i++)
		{
			explosions.get(i).setMapPosition((int)tileMap.getx(), (int)tileMap.gety());
			explosions.get(i).draw(g);
		}
		
		// draw hud
		hud.draw(g);
	}
	
	public void keyPressed(int k) 
	{
		if(k == KeyEvent.VK_LEFT)
		{
			player.setLeft(true);
		}
		if(k == KeyEvent.VK_RIGHT)
		{
			player.setRight(true);
		}
		if(k == KeyEvent.VK_UP)
		{
			player.setJumping(true);
		}
		if(k == KeyEvent.VK_DOWN)
		{
			player.setDown(true);
		}

		if(k == KeyEvent.VK_R)
		{
			player.setScratching();
		}
		if(k == KeyEvent.VK_F)
		{
			player.setFiring();
		}
		if(k == KeyEvent.VK_SPACE)
		{
			player.setScratching();
		}
		if (k == KeyEvent.VK_C){
			player.setClothe(true);
		}
	}
	
	public void keyReleased(int k) 
	{
		if(k == KeyEvent.VK_LEFT)
		{
			player.setLeft(false);
		}
		if(k == KeyEvent.VK_RIGHT)
		{
			player.setRight(false);
		}
		if(k == KeyEvent.VK_UP)
		{
			player.setUp(false);
		}
		if(k == KeyEvent.VK_DOWN)
		{
			player.setDown(false);
		}

	}

	public static TileMap getTileMap() {
		return tileMap;
	}
}

package Entity;

import TileMap.*;

import java.util.ArrayList;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends MapObject 
{
	// основные атрибуты рыцаря
	private int health;
	private int armor;
	private int hunger;
	private int money;
	private int fire;
	private int maxFire;
	private int exp;
	public int level;
	private boolean dead;
	private boolean flinching;
	private long flinchTimer;

	//предметы героя
	private Item playerArmor;
	private Item playerSword;
	private Item playerShield;
	private Item playerOther;
	private Item playerBoots;
	private Item playerHelmet;

	private boolean clothe;
	
	// фаербол
	private boolean firing;
	private int fireCost;
	private int fireBallDamage;
	private ArrayList<FireBall> fireBalls;
	private ArrayList<Item> playerItems;
	
	// параметр урона
	private boolean scratching;

	private int scratchDamage;
	private int startscratchDamage;
	private int scratchRange;

	//бонусные параметры
	private int bonusDamage;
	private int bonusMovespeed;
	private int bonusArmor;
	private int bonusHelmet;
	private int bonusHealth;
	private int bonusShield;
	// скольжение
	private boolean gliding;
	
	// массивы для спрайтов
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {2, 8, 1, 2, 4, 2, 5};
	
	// параметры дла анимации
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int GLIDING = 4;
	private static final int FIREBALL = 5;
	private static final int SCRATCHING = 6;
	
	public Player(TileMap tm) 
	{
		super(tm);
		//основные параметры героя
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		moveSpeed = 0.9;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		
		facingRight = true;
		
		health = 5;
		armor = 5;
		fire = maxFire = 2500;
		money = 1000;
		hunger = 100;
		level = 1; exp = 0;
		
		fireCost = 200;
		fireBallDamage = 5;
		fireBalls = new ArrayList<>();
		
		scratchDamage = 8;
		bonusDamage = 0;
		scratchRange = 40;

		playerItems = new ArrayList<Item>();
		// подгружаем спрайты
		try 
		{
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/playersprites.gif"));
			sprites = new ArrayList<>();
			for(int i = 0; i < 7; i++)
			{
				BufferedImage [] bi = new BufferedImage[numFrames[i]];
				for(int j = 0; j < numFrames[i]; j++)
				{
					if(i != 6)
					{
						bi[j] = spritesheet.getSubimage(j * width, i * height, width, height);						
					}
					else 
					{
						bi[j] = spritesheet.getSubimage(j * width * 2, i * height, width * 2, height);
					}
				}
				sprites.add(bi);
			}			
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}		
		
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);

		//запускаем поток для вычитания 1% голода
		run.start();
	}

	//Getters
	public int getHealth() { return health+bonusHealth; }
	public int getArmor(){ return armor+bonusArmor+bonusHelmet+bonusShield;}
	public int getMoney(){ return money;}
	public int getFire() { return fire; }
	public int getHunger(){ return hunger;}
	public int getLevel(){ return level;}
	public int getExp(){return exp;}
	public int getHeroDamage(){return scratchDamage+bonusDamage;}
	public String getMoveSpeed(){return String.valueOf(moveSpeed+bonusMovespeed);}
	public Item getPlayerArmor() {	return playerArmor;	}
	public Item getPlayerSword() {	return playerSword;	}
	public Item getPlayerShield() {	return playerShield;}
	public Item getPlayerOther() {	return playerOther;	}
	public Item getPlayerBoots() {	return playerBoots;	}
	public Item getPlayerHelmet() {	return playerHelmet;}
	public boolean isClothe() {return clothe;}

	//Setters
	public void setPlayerArmor(Item playerArmor) {	this.playerArmor = playerArmor;	}
	public void setPlayerSword(Item playerSword) {	this.playerSword = playerSword;	}
	public void setPlayerShield(Item playerShield) {this.playerShield = playerShield;	}
	public void setPlayerOther(Item playerOther) {	this.playerOther = playerOther;	}
	public void setPlayerBoots(Item playerBoots) {	this.playerBoots = playerBoots;	}
	public void setPlayerHelmet(Item playerHelmet) {this.playerHelmet = playerHelmet;	}
	public void setBonusDamage(int bonusDamage){this.bonusDamage = bonusDamage;}
	public void setBonusMovespeed(int bonusMovespeed){this.bonusMovespeed = bonusMovespeed;}
	public void setBonusArmor(int bonusArmor){this.bonusArmor = bonusArmor;}
	public void setBonusHelmet(int bonusHelmet){this.bonusHelmet = bonusHelmet;}
	public void setBonusHealth (int bonusHealth){this.bonusHealth = bonusHealth;}
	public void setBonusShield (int bonusShield){this.bonusShield = bonusShield;}
	public void setFiring() {firing = true;}
	public void setClothe(boolean clothe) {	this.clothe = clothe;}
	public void addPlayerItem(Item item){
		playerItems.add(item);
	}
	public void setHealth(int health){
		this.health =  health;
		if (this.hunger>100) this.health = 100;
	}
	public boolean setMoney(int price){
		if (money>=price) {
			money -=price;
			return true;
		}
		else
			return false;
	}
	public void setHunger(int hunger){
		this.hunger +=  hunger;
		if (this.hunger>100)
			this.hunger = 100;
	}

	public boolean useHammer(){
		if (armor < 5) {
			armor = 5;
			return true;
		}
		else
			return false;
	}

	private void AttackHealth(int damage) {
		if (damage >= bonusHealth) {
			int lastHealth = damage - bonusHealth;
			bonusHealth = 0;
			health -= lastHealth;
		} else {
			bonusHealth-=damage;
		}
	}
	private void AttackArmor(int damage){
		if (damage >= bonusArmor) {
			int lastHealth = damage - bonusArmor;
			bonusArmor = 0;
			armor -= lastHealth;
		} else {
			bonusArmor-=damage;
		}
	}

	public ArrayList<Item> getPlayerItems(){return playerItems;}
	public void AddMoney(int money){this.money += money;}
	public void AddExp(int exp){
		this.exp += exp;
		if (this.exp>=100) {
			level++;
			this.exp -= 100;
		}
	}
	public void setScratching() {scratching = true;}
	
	public void setGliding(boolean b){gliding = b;}
	public void checkAttack(ArrayList<Enemy> enemies)
	{
		// loop through enemies
		for (Enemy e : enemies) {
			//scratch attack
			if (scratching) {
				if (facingRight) {
					if (e.getx() > x && e.getx() < x + scratchRange && e.gety() > y - height / 2 && e.gety() < y + height / 2) {
						e.hit(scratchDamage+bonusDamage);
					}
				} else {
					if (e.getx() < x && e.getx() > x - scratchRange && e.gety() > y - height / 2 && e.gety() < +height / 2) {
						e.hit(scratchDamage+bonusDamage);
					}
				}
			}
			// fireballs
			for (FireBall fireBall : fireBalls) {
				if (fireBall.intersects(e)) {
					e.hit(fireBallDamage);
					fireBall.setHit();
					break;
				}
			}

			// проверка столкновения с ботом
			if (intersects(e)) {

				hit(e.getDamage());
			}
		}
	}

	/**
	 * метод предназначен для поднятия предметов
	 */
	public void checkItem(ArrayList<Item> items)
	{
		for (Item item : items) {
			// столкновение с предметом
			if (intersects(item)) {
				playerItems.add(item);
				item.hit(scratchDamage);
			}
		}
	}

	//метод нанесение урона персонажу
	private void hit(int damage)
	{
		if(flinching)
		{
			return;
		}
		if (getArmor() > 0){
			AttackArmor(damage);
		} else {
			AttackHealth(damage);
			if (health < 0) {
				health = 0;
			}
			if (health == 0) {
				dead = true;
			}
		}
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
	private void getNextPosition()
	{
		// movement
		if(left)
		{
			dx -= moveSpeed;
			if(dx < -maxSpeed)
			{
				dx = -maxSpeed;
			}
		}
		else if(right)
		{
			dx += moveSpeed;
			if(dx > maxSpeed)
			{
				dx = maxSpeed;
			}
		}
		else 
		{
			if(dx > 0)
			{
				dx -= stopSpeed;
				if(dx < 0)
				{
					dx = 0;
				}
			}
			else if(dx < 0) 
			{
				dx += stopSpeed;
				if(dx > 0)
				{
					dx = 0;
				}
			}
		}
		
		// can't move while attacking, except in air
		if((currentAction == SCRATCHING)  || (currentAction == FIREBALL) && !(jumping || falling))
		{
			dx = 0;
		}
		
		// jumping
		if(jumping  && !falling)
		{
			dy = jumpStart;
			falling = true;
		}
		
		// falling
		if(falling)
		{
			if(dy > 0 && gliding)
			{
				dy += fallSpeed * 0.1;
			}
			else 
			{
				dy += fallSpeed;
			}
			if(dy > 0)
			{
				jumping = false;
			}
			if(dy < 0 && !jumping) 
			{
				dy += stopJumpSpeed;
			}
			if(dy > maxFallSpeed)
			{
				dy = maxFallSpeed;
			}
		}
	}

	//обновление и проверка персонажа
	public void update()
	{
		//проверка бонусных предметов на персонаже
		if (getPlayerSword()==null) setBonusDamage(0);
		if (getPlayerShield()==null) setBonusShield(0);
		if (getPlayerArmor()==null) setBonusArmor(0);
		if (getPlayerBoots()==null) setBonusMovespeed(0);
		if (getPlayerHelmet()==null) setBonusHelmet(0);
		if (getPlayerOther()==null) setBonusHealth(0);
		// обновляем местоположение
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// проверка остановки атаки
		if(currentAction == SCRATCHING)
		{
			if(animation.hasPlayedOnce())
			{
				scratching = false;
			}
		}
		if(currentAction == FIREBALL)
		{
			if(animation.hasPlayedOnce())
			{
				firing = false;
			}
		}
		
		// фаербол
		fire += 1;
		if(fire > maxFire)
		{
			fire = maxFire;
		}
		if(firing && currentAction != FIREBALL)
		{
			if(fire > fireCost)
			{
				fire -= fireCost;
				FireBall fb = new FireBall(tileMap, facingRight);
				fb.setPosition(x, y);
				fireBalls.add(fb);
			}
		}
		//обновление фаерболов
		for(int i = 0; i < fireBalls.size(); i++)
		{
			fireBalls.get(i).update();
			if(fireBalls.get(i).shouldRemove())
			{
				fireBalls.remove(i);
				i--;
			}
		}
		
		// столкновение
		if(flinching)
		{
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 1000)
			{
				flinching = false;
			}
		}
		
		
		// анимация
		if(scratching)
		{
			if(currentAction != SCRATCHING)
			{
				currentAction = SCRATCHING;
				animation.setFrames(sprites.get(SCRATCHING));
				animation.setDelay(50);
				width = 60;
			}
		}
		else if(firing) 
		{
			if(currentAction != FIREBALL)
			{
				currentAction = FIREBALL;
				animation.setFrames(sprites.get(FIREBALL));
				animation.setDelay(100);
				width = 30;
			}
		}
		else if(dy > 0)		
		{
			if(gliding) 
			{
				if(currentAction != GLIDING)
				{
					currentAction = GLIDING;
					animation.setFrames(sprites.get(GLIDING));
					animation.setDelay(100);
					width = 30;
					
				}
			}
			else if(currentAction != FALLING)
			{
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(100);
				width = 30;
			}
		}
		else if(dy < 0)
		{
			if(currentAction != JUMPING)
			{
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(-1);
				width = 30;
			}
		}
		else if(left || right)
		{
			if(currentAction != WALKING)
			{
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(40);
				width = 30;
			}
		}
		else 
		{
			if(currentAction != IDLE)
			{
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
				width = 30;		
			}
		}		
		
		animation.update();
		
		// изменение направления
		if(currentAction != SCRATCHING && currentAction != FIREBALL)
		{
			if(right) 
			{
				facingRight = true;			
			}
			if(left)
			{
				facingRight = false;
			}
		}
	}

	/**
	 * поток для вычитания 1% голода каждую секунду
	 */
	private Thread run = new Thread(() -> {
		do {
			try {
				Thread.sleep(1000);
				if (hunger > 0)
					hunger--;
				else
					health--;

			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		} while (true);
	});

	//прорисовка
	public void draw(Graphics2D g)
	{
		setMapPosition();
		
		// фаерболы
		for (FireBall fireBall : fireBalls) {
			fireBall.draw(g);
		}
		
		// герой
		if(flinching)
		{
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2  == 0)
			{
				return;
			}
		}		
		super.draw(g);
	}
}
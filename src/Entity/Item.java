package Entity;

import TileMap.TileMap;

/**
 * модель предмета
 */
public class Item extends MapObject {
    protected int health;
    protected int maxHealth;
    protected boolean picked;
    private String name;

    public int getBonus() {
        return bonus;
    }

    private  int bonus;


    private String type;
    protected String description;
    protected boolean flinching;
    protected long flinchTimer;

    public Item(TileMap tm)
    {
        super(tm);

    }

    public void update()
    {

    }
    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getDescription() {
        return description;
    }

    public boolean isPicked() { return picked; }

    protected void getNextPosition()
    {
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

        // falling
        if(falling)
        {
            dy += fallSpeed;
        }
    }
    public void hit(int damage)
    {
        if(picked || flinching)
        {
            return;
        }
        health -= damage;
        if(health < 0)
        {
            health = 0;
        }
        if(health == 0)
        {
            picked = true;
        }
        flinching = true;
        flinchTimer = System.nanoTime();
    }

    public String getType() {
        return type;
    }
}

package Entity.Items;

import Entity.Animation;
import Entity.Item;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class KnightShieldItem extends Item {
    private BufferedImage[] sprites;

    private String name = "Knight Shield";
    private String description = "Magic amulet";
    private String type = "shield";

    private int bonus = 5;

    public KnightShieldItem(TileMap tm)
    {
        super(tm);

        fallSpeed = 0.2;
        maxFallSpeed = 10.00;

        width = 30;
        height = 30;
        cwidth = 20;
        cheight = 20;

        health = maxHealth = 2;
        // подгружаем спрайты
        try
        {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Items/shield.gif"));
            sprites = new BufferedImage[3];
            for(int i = 0; i < sprites.length; i++)
            {
                sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(300);

        right = true;
        facingRight = true;
    }

    private void getNextPosition()
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

    public void update()
    {
        // обновление местоположения на карте
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
        //update animation
        animation.update();
    }

    public void draw(Graphics2D g)
    {
        setMapPosition();
        super.draw(g);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getBonus() {
        return bonus;
    }
}

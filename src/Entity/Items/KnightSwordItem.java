package Entity.Items;

import Entity.Animation;
import Entity.Item;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class KnightSwordItem extends Item {
    private BufferedImage[] sprites;

    private String name = "Knight Sword";



    private int bonus = 2;


    private String type = "weapon";
    private String description = "Knight Sword!";

    public KnightSwordItem(TileMap tm)
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
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Items/sword.gif"));
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



    public void update()
    {
        // обновление местоположения на карте
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
        //анимация
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
    public  int getBonus() {return bonus;}

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getType() {
        return type;
    }
}

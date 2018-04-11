package Entity.HUD;


import Entity.Items.AppleItem;
import Entity.Items.ElixirItem;
import Entity.Items.HammerItem;
import Entity.Items.StoneItem;
import Entity.Player;
import GameState.Level1State;

import javax.imageio.ImageIO;
import java.awt.*;

/**
 * класс для прорисовки магазина
 */
public class DrawShop {
    private Graphics2D g;
    private Image apple,hammer,stone,elixir;
    private Player player;
    private Font font;
    private final static int appleItemPrice = 50,
                            hammerItemPrice = 100,
                            stoneItemPrice = 250,
                            elixirItemPrice = 400;
    private AppleItem appleItem;
    private HammerItem hammerItem;
    private StoneItem stoneItem;
    private ElixirItem elixirItem;
    private Image shop;
    private int x,y;

    //конструктор
    private DrawShop(){}
    DrawShop(Graphics2D g, Player player, Font font, Image shop,int x, int y){
        this.shop = shop;
        this.font = font;
        this.g = g;
        this.player = player;
        this.x = x;
        this.y = y;
        PreDraw();
        LoadIcons();
        Draw();
        ListenClicks();
    }

    //подгрузка иконок
    private void LoadIcons(){
        try {
            apple = ImageIO.read(getClass().getResourceAsStream("/Icons/apple.gif"));
            elixir = ImageIO.read(getClass().getResourceAsStream("/Icons/elixir.gif"));
            hammer = ImageIO.read(getClass().getResourceAsStream("/Icons/hammer.gif"));
            stone = ImageIO.read(getClass().getResourceAsStream("/Icons/stone.gif"));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //инициализация
    private void PreDraw(){
        appleItem = new AppleItem(Level1State.getTileMap());
        hammerItem = new HammerItem(Level1State.getTileMap());
        elixirItem = new ElixirItem(Level1State.getTileMap());
        stoneItem = new StoneItem(Level1State.getTileMap());
    }

    //прорисовка
    private void Draw(){
        g.drawImage(shop, 0, 10, null);
        g.setColor(Color.white);
        g.drawString("Shop",80,78);
        g.setFont(font);
        g.drawImage(apple, 73, 100, null);
        g.drawString(appleItem.getName(),95,113);
        g.drawImage(elixir, 73, 125, null);
        g.drawString(elixirItem.getName(),95,138);
        g.drawImage(hammer, 73, 150, null);
        g.drawString(hammerItem.getName(),95,163);
        g.drawImage(stone, 73, 175, null);
        g.drawString(stoneItem.getName(),95,188);
    }

    //метод для прослушивания кликов по магазину
    private void ListenClicks() {
        if ((x>73 && x<173)&&(y>116 && y<131)){
            g.drawString("Bonus: "+appleItem.getBonus(),190,135);
            g.drawString("Type: "+appleItem.getType(),190,125);
            g.drawString(appleItem.getDescription(),190,115);
            g.drawString(appleItem.getName(),192,105);
            g.drawString("Price: "+appleItemPrice,190,145);
            g.drawString("Press C ",190,155);
            if (player.isClothe()){
                if (player.setMoney(appleItemPrice)) {
                    player.addPlayerItem(appleItem);
                    player.setClothe(false);
                }
                else
                    g.drawString("No money:c",190,165);
            }
        } else if ((x>73 && x<173)&&(y>140 && y<160)){
            g.drawString("Bonus: "+elixirItem.getBonus(),190,135);
            g.drawString("Type: "+elixirItem.getType(),190,125);
            g.drawString(elixirItem.getDescription(),190,115);
            g.drawString(elixirItem.getName(),192,105);
            g.drawString("Price: "+elixirItemPrice,190,145);
            g.drawString("Press C ",190,155);
            if (player.isClothe()){
                if (player.setMoney(elixirItemPrice)) {
                    player.addPlayerItem(elixirItem);
                    player.setClothe(false);
                }
                else
                    g.drawString("No money:c",190,165);
            }

        } else if ((x>73 && x<173)&&(y>165 && y<180)){
            g.drawString("Bonus: "+hammerItem.getBonus(),190,135);
            g.drawString("Type: "+hammerItem.getType(),190,125);
            g.drawString(hammerItem.getDescription(),190,115);
            g.drawString(hammerItem.getName(),192,105);
            g.drawString("Price: "+hammerItemPrice,190,145);
            g.drawString("Press C ",190,155);
            if (player.isClothe()){
                if (player.setMoney(hammerItemPrice)) {
                    player.addPlayerItem(hammerItem);
                    player.setClothe(false);
                }
                else
                    g.drawString("No money:c",190,165);
            }

        } else if ((x>73 && x<173)&&(y>189 && y<206)){
            g.drawString("Bonus: "+stoneItem.getBonus(),190,135);
            g.drawString("Type: "+stoneItem.getType(),190,125);
            g.drawString(stoneItem.getDescription(),190,115);
            g.drawString(stoneItem.getName(),192,105);
            g.drawString("Price: "+stoneItemPrice,190,145);
            g.drawString("Press C ",190,155);
            if (player.isClothe()){
                if (player.setMoney(stoneItemPrice)) {
                    player.addPlayerItem(stoneItem);
                    player.setClothe(false);
                }
                else
                    g.drawString("No money:c",190,165);
            }
        }
        else
            player.setClothe(false);

    }
}

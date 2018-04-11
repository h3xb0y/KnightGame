package Entity.HUD;

import Entity.Item;
import Entity.Player;
import com.sun.istack.internal.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * класс предназначен для прорисовки инвентаря
 * и в целом в этом классе реализована система инвентаря
 */
public class DrawInventory {
    private Image amulet,apple,armor,boots,elixir,hammer,helmet,shield,shield2,stone,sword,sword2;
    private ArrayList<Item> playerItems;
    private Point[] points = new Point[] {
            new Point(88,60),
            new Point(108, 60),
            new Point(128, 60),
            new Point(148, 60),
            new Point(88, 80),
            new Point(108, 80),
            new Point(128, 80),
            new Point(148, 80),
            new Point(88, 100),
            new Point(108, 100),
            new Point(128, 100),
            new Point(148, 100)
    };
    private int x,y;
    private Player player;
    private Font font;
    private Graphics2D g;
    private JFrame window;
    private int count;
    private boolean clothe;
    private Image inventoryBackground;
    private Map<Item,Point> itemPositions;

    //конструктор
    private DrawInventory(){}
    DrawInventory(Graphics2D g, Player player, Font font, Image inventoryBackground,int x, int y){
        this.player = player;
        this.font = font;
        this.g = g;
        this.inventoryBackground = inventoryBackground;
        this.x = x;
        this.y = y;
        this.count = 0;
        LoadIcons();
        PreDraw();
        Draw();
        ListenClicks();
    }
    //подгрузка иконок
    private void LoadIcons(){
        try {
            amulet = ImageIO.read(getClass().getResourceAsStream("/Icons/amulet.gif"));
            apple = ImageIO.read(getClass().getResourceAsStream("/Icons/apple.gif"));
            armor = ImageIO.read(getClass().getResourceAsStream("/Icons/armor.gif"));
            boots = ImageIO.read(getClass().getResourceAsStream("/Icons/boots.gif"));
            elixir = ImageIO.read(getClass().getResourceAsStream("/Icons/elixir.gif"));
            hammer = ImageIO.read(getClass().getResourceAsStream("/Icons/hammer.gif"));
            helmet = ImageIO.read(getClass().getResourceAsStream("/Icons/helmet.gif"));
            shield = ImageIO.read(getClass().getResourceAsStream("/Icons/shield.gif"));
            shield2 = ImageIO.read(getClass().getResourceAsStream("/Icons/shield2.gif"));
            stone = ImageIO.read(getClass().getResourceAsStream("/Icons/stone.gif"));
            sword = ImageIO.read(getClass().getResourceAsStream("/Icons/sword.gif"));
            sword2 = ImageIO.read(getClass().getResourceAsStream("/Icons/sword2.gif"));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //инициализация
    private void PreDraw(){
        playerItems = player.getPlayerItems();
        itemPositions = new HashMap<Item,Point>();
    }

    //прорисовка статических элементов инвентаря
    private void Draw(){
        g.drawImage(inventoryBackground, 0, 10, null);
        int size = 0;
        for (Item playerItem : playerItems) {
            g.drawImage(getIcon(playerItem),points[size].x,points[size].y,null);

            itemPositions.put(playerItem,points[size]);

            size++;
        }
        g.setColor(Color.white);
        g.drawString("Knight Inventory",90,38);
    }

    //метод для прослушивания кликов по инвентарю
    private void ListenClicks(){
        showClothingItems();
        int count = 1;
        g.setColor(Color.white);
        g.setFont(font);
        for (Map.Entry<Item,Point> entry: itemPositions.entrySet()){
            if ((entry.getValue().x<x && entry.getValue().x+15>x)
                    && (entry.getValue().y+15<y && entry.getValue().y+30>y) ) {
                g.drawString(entry.getKey().getName(), 80, 200);
                g.drawString("Type: " + entry.getKey().getType(), 80, 210);
                if (checkItem(entry.getKey())) {
                    g.drawString("Press C for taking off", 80, 220);
                    if (player.isClothe()) {
                        PlayerTakingOff(entry.getKey());
                    }
                } else {
                        g.drawString("Press C for clothing", 80, 220);
                        if (player.isClothe()) {
                            PlayerClothing(entry.getKey());
                        }
                    }
                    player.setClothe(false);
                }

        }


    }
    private Image getIcon(Item playerItem){
        switch (playerItem.getName()){
            case "Knight Sword":
                return sword;
            case "Amulet":
                return amulet;
            case "Knight Shield":
                return shield;
            case "Magic Shield":
                return shield2;
            case "Knight Armor":
                return armor;
            case "Knight Boots":
                return boots;
            case "Elixir":
                return elixir;
            case "Hammer":
                return hammer;
            case "Knight Helmet":
                return helmet;
            case "Stone":
                return stone;
            case "Apple":
                return apple;
            case "Magic Sword":
                return sword2;
            default:
                return shield;

        }
    }

    //метод для добавление оружия на героя и бонусов
    private void PlayerClothing(Item item){
        switch (item.getType()){
            case "weapon":
                player.setPlayerSword(item);
                player.setBonusDamage(player.getPlayerSword().getBonus());
                break;
            case "other":
                switch (item.getName()){
                    case "Amulet":
                        player.setPlayerOther(item);
                        player.setBonusHealth(player.getPlayerOther().getBonus());
                        break;
                    case "Apple":
                        player.setHunger(75);
                        playerItems.remove(item);
                        break;
                    case "Elixir":
                        player.setHealth(5);
                        playerItems.remove(item);
                        break;
                    case "Hammer":
                        if (player.useHammer())
                            playerItems.remove(item);
                        break;
                    case "Stone":
                        player.useHammer();
                        player.setHealth(5);
                        playerItems.remove(item);
                        break;
                }
                break;
            case "helmet":
                player.setPlayerHelmet(item);
                player.setBonusHelmet(player.getPlayerHelmet().getBonus());
                break;
            case "shield":
                player.setPlayerShield(item);
                player.setBonusShield(player.getPlayerShield().getBonus());
                break;
            case "armor":
                player.setPlayerArmor(item);
                player.setBonusArmor(player.getPlayerArmor().getBonus());
                break;
            case "boots":
                player.setPlayerBoots(item);
                player.setBonusMovespeed(player.getPlayerBoots().getBonus());
                break;
        }
    }

    //метод для проверки, одет ли переданый предмет на героя
    private boolean checkItem(Item item) throws NullPointerException{
        switch (item.getType()) {
            case "weapon":
                return player.getPlayerSword() != null && item.getName().equals(player.getPlayerSword().getName());
            case "other":
                return player.getPlayerOther() != null && item.getName().equals(player.getPlayerOther().getName());
            case "helmet":
                return player.getPlayerHelmet() != null && item.getName().equals(player.getPlayerHelmet().getName());
            case "shield":
                return player.getPlayerShield() != null && item.getName().equals(player.getPlayerShield().getName());
            case "armor":
                return player.getPlayerArmor() != null && item.getName().equals(player.getPlayerArmor().getName());
            case "boots":
                return player.getPlayerBoots() != null && item.getName().equals(player.getPlayerBoots().getName());
            default:
                return false;
        }
    }

    //метод для удаление предмета с героя
    private void PlayerTakingOff(Item item){
        switch (item.getType()){
            case "weapon":
                player.setPlayerSword(null);
                break;
            case "other":
                player.setPlayerOther(null);
                break;
            case "helmet":
                player.setPlayerHelmet(null);
                break;
            case "shield":
                player.setPlayerShield(null);
                break;
            case "armor":
                player.setPlayerArmor(null);
                break;
            case "boots":
                player.setPlayerBoots(null);
                break;
        }
    }

    //метод для отображение предметов на персонаже
    private void showClothingItems(){
        if (player.getPlayerArmor()!= null)
            g.drawImage(getIcon(player.getPlayerArmor()),207,87,null);
        if (player.getPlayerHelmet()!= null)
            g.drawImage(getIcon(player.getPlayerHelmet()),206,61,null);
        if (player.getPlayerOther()!= null)
            g.drawImage(getIcon(player.getPlayerOther()),230,74,null);
        if (player.getPlayerSword()!= null)
            g.drawImage(getIcon(player.getPlayerSword()),183,98,null);
        if (player.getPlayerBoots()!= null)
            g.drawImage(getIcon(player.getPlayerBoots()),207,135,null);
        if (player.getPlayerShield()!= null)
            g.drawImage(getIcon(player.getPlayerShield()),230,98,null);
    }
}

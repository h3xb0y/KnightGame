package Entity.HUD;

import Entity.Player;

import java.awt.*;

public class DrawStats {
    private Graphics2D g;
    private Player player;
    private Font font;
    private Image stats;

    //конструктор
    public DrawStats(Graphics2D g, Player player, Font font, Image stats){
        this.stats = stats;
        this.font = font;
        this.g = g;
        this.player = player;
        Draw();
    }
    private void Draw(){
        g.drawImage(stats, 5, 0, null);
        g.setColor(Color.white);
        //строчки
        g.drawString(String.valueOf(player.getLevel()),209,38);
        g.drawString("Knight stats",65,36);
        g.setFont(font);
        g.drawString("Health("+player.getHealth()+" points)",60,70);
        //проверка наличия бонуса
        if (player.getPlayerOther()!=null){
            g.drawString("Bonus:",60,82);
            g.drawString(player.getPlayerOther().getName(),100,82);
        }
        g.drawString("Armor("+player.getArmor()+" points)",60,115);
        //тут тоже
        if (player.getPlayerArmor()!=null || player.getPlayerHelmet()!=null || player.getPlayerShield()!=null) {
            String Bonus="";
            if (player.getPlayerArmor()!= null) Bonus +=player.getPlayerArmor().getName()+" ";
            if (player.getPlayerHelmet()!= null) Bonus +=player.getPlayerHelmet().getName()+" ";
            if (player.getPlayerShield()!= null) Bonus +=player.getPlayerShield().getName();
            g.drawString("Bonus:",60,127);
            g.drawString(Bonus, 100, 127);
        }
        g.drawString("Hunger("+player.getHunger()+"%)",60,180);
        g.drawString("Money: "+player.getMoney(),165,70);
        g.drawString("Hero damage: "+player.getHeroDamage(),165,90);
        g.drawString("Movespeed: "+player.getMoveSpeed(),165,110);
        //полоски
        g.setColor(Color.cyan);
        g.fillRect(231,20,6, player.getExp()/4);
        g.setColor(Color.green);
        g.fillRect(65,90,player.getHealth()*7,8);
        g.setColor(Color.BLUE);
        g.fillRect(65,140,player.getArmor()*7,8);
        g.setColor(Color.RED);
        g.fillRect(65,187,player.getHunger()/3,9);
    }
}

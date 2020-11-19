package ru.fomin.battleship.client.map;

import javax.swing.*;
import java.awt.*;

public class Cell extends JButton {
    /*Status:
    * 0-unknown cell/sea
    * 1-empty cell after shoot
    * 2-wounded
    * 3-dead
    * 4-preparing for posting ships
    * 5-known cell free
    * 6-known cell with ship*/
    private int status;
    private  JLabel img=new JLabel();
    public Cell(int status){
        setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
        this.status=status;
        setImage(status);
    }
    public void setImage(int status){
        this.status=status;
        switch (status){
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                setIcon(new ImageIcon(getClass().getResource("../img/sea.png")));
                break;
            case 6:
                break;
        }
    }
}

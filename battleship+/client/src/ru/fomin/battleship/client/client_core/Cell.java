package ru.fomin.battleship.client.client_core;

import javax.swing.*;

public class Cell extends JButton {
    /*Status:
    * 0-unknown cell/sea
    * 1-empty cell after shoot
    * 2-wounded
    * 3-dead
    * 4-preparing for posting ship*/
    private int status=0;
}
